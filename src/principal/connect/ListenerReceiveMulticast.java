/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal.connect;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import principal.Database;
import principal.Wallet;
import principal.WalletOwner;
import principal.chave.Chave;
import principal.mensagens.MensagemAutorizacao;
import principal.mensagens.MensagemRequerimentoAutorizacao;
import principal.transacoes.Transacao;
import static java.lang.Thread.sleep;

/**
 * Classe que fica constantemente ligada e recebe todas as mensagens mandadas
 * por multicast.
 *
 * @author Rodrigo
 */
public class ListenerReceiveMulticast implements Runnable {

    volatile String nomeFuncao;
    private MulticastSocket ms;
    private volatile Database banco;
    private InetAddress group;
    private final int porta;
    static final int BUFFER = 32768 * 2;
    public String ip;
    private final MessageSenderMulticast messageSendermc;
    private final MessageSenderUnicast messageSenderuc;
    private final WalletOwner carteiraDono;
    public boolean updateBanco;
    public Wallet carteira;

    /**
     * Construtor de multicast 
     *
     * @param messageSenderuc A objeto que envia mensagens Unicast
     * @param messageSendermc A objeto que envia mensagens multicast
     * @param banco O banco de dados do processo
     * @param conexao O multicast socket que recebe mensagens multicast udp
     * @param group O grupo que o o processo ira entrar
     * @param porta A porta que multicast vai se conectar
     * @param ip O endereco ip que para a conexao
     * @param carteiraDono Classe responsavel pelo processo e unica que possui a chave privada
     * @param carteiraDonoCopia Mesma classe so que sem a chave privada
     */
    public ListenerReceiveMulticast(MessageSenderUnicast messageSenderuc, MessageSenderMulticast messageSendermc,
            Database banco, MulticastSocket conexao, InetAddress group, int porta, String ip, WalletOwner carteiraDono, Wallet carteiraDonoCopia) {
        this.messageSendermc = messageSendermc;
        this.messageSenderuc = messageSenderuc;
        this.ms = conexao;
        this.banco = banco;
        this.group = group;
        this.porta = porta;
        this.ip = ip;
        this.carteiraDono = carteiraDono;
        this.carteira = carteiraDonoCopia;
    }

    @Override
    public void run() {
        inicializaConexao();
        while (true) {
            identificaObjeto(recebeMensagem());
        }
    }

    /**
     * Verifica que objeto foi enviado na thread de recebimento e faz a
     * separacao de tratamento necessaria
     *
     * @param mensagem Trata-se do que foi recebido pelo thread de recebimento.
     */
    public synchronized void identificaObjeto(Object mensagem) {
        String tipo;
        if (mensagem == null) {
            return;
        }
        tipo = mensagem.getClass().getName();
        System.out.println("Recebi uma Multicast de " + tipo);
        switch (tipo) {
            case "principal.Wallet":
                //Recebeu uma credencial
                System.out.println("Entrei no if da carteira");
                entraNoSistema((Wallet) mensagem);
                break;
            case "principal.mensagens.MensagemRequerimentoAutorizacao":
                // Autoriza a transacao
                autorizaTransacao((MensagemRequerimentoAutorizacao) mensagem);
                break;
            case "principal.transacoes.Transacao":
                atualizaTransacao((Transacao) mensagem);
                break;
            default:
                break;
        }
    }

    /**
     * Primeira entrada no sistema do usuario. Veririca que existe um banco 
     * ou recebe um banco de um primeiro usuario via multicast
     *
     * @param carteira Os dados de um processo vizinho
     */
    public synchronized void entraNoSistema(Wallet carteira) {
        // Se for o primeiro processo o banco esta vazio
        // Salvando para ser usado depois
        Wallet tmp = carteira;
        
        if (banco.carteiras.size() > 0) {
            try {
                System.out.println("adicionando um vizinho");
                banco.carteiras.add(tmp);
                //messageSendermc.enviarMensagem(banco);
                System.out.println("Me mandando para porta: " + tmp.retornaPorta());
                messageSenderuc.sendMessage(this.carteira, tmp.retornaPorta());
                dormir(100);
            } catch (IOException ex) {
                Logger.getLogger(ListenerReceiveMulticast.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    /**
     * Apos receber um requerimento de compra. Esse metodo verifica a quantidade de moedas
     * que o processo possui para fazer a requisicao da autorizacao para algum processo minerar
     *
     * @param requerimentoAutorizacao
     */
    public synchronized void autorizaTransacao(MensagemRequerimentoAutorizacao requerimentoAutorizacao) {
        // verificando se nao eh o proprio comprador ou vendedor
        if (this.carteira.id == requerimentoAutorizacao.comprador.id || this.carteira.id == requerimentoAutorizacao.vendedor.id) {
            System.out.println("nao recebe ser for o vendedor ou comprador");
        } else {
            // verificando a autenticidade de ambas as carteiras
            System.out.println("verificando autenticidade das carteiras");
            String decodificadaComprador;
            String decodificadaVendedor;
            decodificadaComprador = Chave.decodificarUsandoPublic(requerimentoAutorizacao.codificadaComprador,
                    requerimentoAutorizacao.comprador.publicKey);
            decodificadaVendedor = Chave.decodificarUsandoPublic(requerimentoAutorizacao.codificadaVendedor,
                    requerimentoAutorizacao.vendedor.publicKey);
            // Fazendo a validacao
            if (decodificadaComprador.equals("swordfish") && decodificadaVendedor.equals("swordfish")) {
                System.out.println("Os dois passaram pela assinatura digital");
                //Ambos sao validos.
                MensagemAutorizacao autorizacao = new MensagemAutorizacao(requerimentoAutorizacao.comprador,
                        requerimentoAutorizacao.vendedor, requerimentoAutorizacao.moedas,
                        requerimentoAutorizacao.idTransacao,
                        this.carteira);
                try {
                    messageSenderuc.sendMessage(autorizacao, requerimentoAutorizacao.vendedor.retornaPorta());
                } catch (IOException ex) {
                    Logger.getLogger(ListenerReceiveMulticast.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private synchronized void atualizaTransacao(Transacao transacao) {
        // Procurando os indices da transacao
        System.out.println("atualizando as transacoes");
        int indexVendedor = procuraIndice(transacao.vendedor.id);
        int indexComprador = procuraIndice(transacao.comprador.id);
        int indexMinerador = procuraIndice(transacao.minerador.id);
        int moedas = transacao.moedas;
        System.out.println("A quantidade de moedas eh: " + transacao.moedas);
        
        banco.carteiras.get(indexVendedor).descontaMoedas(-moedas - 1);
        banco.carteiras.get(indexComprador).descontaMoedas(moedas);
        banco.carteiras.get(indexMinerador).descontaMoedas(1);
        banco.transacoes.add(transacao);

    }

    private synchronized void updateBanco(Object bancoRede) {
        // Database so atualiza uma vez
        if (updateBanco == false) {
            System.out.println("dando update no banco");
            this.banco = Database.class.cast(bancoRede);
            this.updateBanco = true;
        }
    }

    public Database getBanco() {
        return banco;
    }

    private void inicializaConexao() {
        try {
            // args give message contents and destination multicast group (e.g. "IP")
            group = InetAddress.getByName(ip);
            ms = new MulticastSocket(porta);
            ms.joinGroup(group);
            //ms.leaveGroup(group);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }

    }

    public static void dormir(int tempo) {
        try {
            sleep(tempo);
        } catch (InterruptedException ex) {
            Logger.getLogger(ListenerReceiveMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Essa funcao inicia a leitura de qualquer mensagem serializada. Ela pode
     * conter objetos serializados. Ela retorna o objeto lido.
     *
     * @return O objeto que foi enviado.
     */
    private Object recebeMensagem() {
        Object object = null;
        try {
            byte[] tamanho = new byte[BUFFER];
            DatagramPacket messageIn = new DatagramPacket(tamanho, tamanho.length, group, porta);
            ms.receive(messageIn);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(tamanho);
            ObjectInputStream objIn = new ObjectInputStream(byteIn);
            object = objIn.readObject();
            messageIn.setLength(tamanho.length);
            byteIn.reset();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ListenerReceiveMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
        return object;
    }

    private int procuraIndice(int indice) {
        for (int i = 0; i < banco.carteiras.size(); i++) {
            if (indice == banco.carteiras.get(i).id) {
                return i;
            }
        }
        return -1;
    }

    private boolean verificaSenha(String senha) {
        return senha.equals("swordfish");
    }

}
