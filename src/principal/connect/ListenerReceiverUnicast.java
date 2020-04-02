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
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import principal.Database;
import principal.Wallet;
import principal.WalletOwner;
import principal.chave.Chave;
import static principal.connect.ListenerReceiveMulticast.BUFFER;
import static principal.connect.ListenerReceiveMulticast.dormir;
import principal.mensagens.MensagemAutorizacao;
import principal.mensagens.MensagemRequerimentoAutorizacao;
import principal.mensagens.MensagemRequerimentoCompra;
import principal.transacoes.Transacao;

/**
 * Classe que espera as mensagens unicast dos processos.
 *
 * @author Rodrigo
 */
public class ListenerReceiverUnicast implements Runnable {

    private final int porta;
    private final DatagramSocket socket;
    private final MessageSenderMulticast messageSenderMulticast;
    private final MessageSenderUnicast messageSenderUnicast;
    private final Database banco;
    private final WalletOwner carteiraDono;
    private final ArrayList<MensagemAutorizacao> listaAutorizacoes = new ArrayList<>();

    /**
     * Classe que recebe todas as mensagens unicast
     * @param mSenderMult O objeto que envia mensagens multicast
     * @param mSenderUni O objeto que evia mensagens unicast
     * @param banco O banco do projeto
     * @param socket O datagram socket usado para enviar mensagens
     * @param carteiraDono dados da carteira do usuario 
     * @throws IOException
     */
    public ListenerReceiverUnicast(MessageSenderMulticast mSenderMult, MessageSenderUnicast mSenderUni,
            Database banco, DatagramSocket socket, WalletOwner carteiraDono) throws IOException {
        this.messageSenderMulticast = mSenderMult;
        this.messageSenderUnicast = mSenderUni;
        // Alocando uma porta livre
        this.socket = socket;
        this.porta = socket.getLocalPort();
        this.banco = banco;
        // System.out.println(porta);
        this.carteiraDono = carteiraDono;

    }

    @Override
    public void run() {
       // System.out.println("Thread unicast inicializada");
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
        
        tipo = mensagem.getClass().getName();
        System.out.println("Recebi uma unicast de tipo "+ tipo);
        switch (tipo) {
            case "principal.Wallet":
                receberCarteira((Wallet) mensagem);
                break;
            case "principal.mensagens.MensagemRequerimentoCompra":
                // Alguem quer suas moedas // autorizar e mandar transacao
                tratarRequerimentoCompra((MensagemRequerimentoCompra) mensagem);
                break;
            case "principal.mensagens.MensagemAutorizacao":
                 adicionarTransacao((MensagemAutorizacao) mensagem);
                break;
            default:
                break;
        }
    }
    
    private synchronized void receberCarteira(Wallet mensagem) {
        if( procuraCarteira(mensagem) == false){
        banco.carteiras.add(mensagem);
        }
    }
    
    private synchronized boolean procuraCarteira(Wallet carteira){
        // Procurnando a carteira no banco de dados
        for(int i =0 ; i< banco.carteiras.size(); i++){
            if(carteira.id == banco.carteiras.get(i).id){
            return true;
            }
        }
    return false;
    }

    private synchronized void tratarRequerimentoCompra(MensagemRequerimentoCompra requerimentoCompra) {
       // O comprador deve ser diferente do vendedor
       if(requerimentoCompra.id == carteiraDono.id){
           System.out.println("comprou de si mesmo");
       }else{
        // Verificando se eu possuo a quantidade de moedas necessarias para transferir para
        Wallet vendedor = banco.carteiras.get(procuraCarteiraDono());
        Wallet comprador = requerimentoCompra.comprador;
        System.out.println("Tratando o requerimento da compra ");
        // o comprador
        if (vendedor.getMoedas() >= requerimentoCompra.moedas+1) {
            // Tenho moedas suficientes. 
            System.out.println("tenho dinheiro");
            String decodificada;
            decodificada = Chave.decodificarUsandoPublic(requerimentoCompra.mensagemCriptografada, 
                    requerimentoCompra.comprador.publicKey);
            if (verificaSenha(decodificada) == true) {
                System.out.println("a senha do comprador confere");
                // Codificando a mensagem com a sua privada
                byte[] codificadaVendedor;
                // Codificando a mensagem do vendedor.
                System.out.println("codificando a mensagem do vendedor para autenticacao");
                codificadaVendedor = Chave.codificarUsandoPrivate("swordfish", carteiraDono.retornaChave().privateKey);
                
                MensagemRequerimentoAutorizacao requerimentoAutorizacao = new MensagemRequerimentoAutorizacao(comprador,
                        vendedor, requerimentoCompra.moedas, requerimentoCompra.mensagemCriptografada,
                        codificadaVendedor, requerimentoCompra.id);
                messageSenderMulticast.enviarMensagem(requerimentoAutorizacao); 
            }

        }

    }
    
    }
    
    private synchronized void adicionarTransacao(MensagemAutorizacao autorizacao){
        // Primeiramente verificar se a transacao ja existe, Sera feiro isso pelo ID.
        System.out.println("procurando a transacao no banco");
        if( procurarTransacaoBanco(autorizacao.id) == false){
            System.out.println("criando uma transacao");
                // criando uma transacao          
            
        listaAutorizacoes.add(autorizacao);
        
        dormir(200);
        Collections.sort(listaAutorizacoes, (MensagemAutorizacao t, MensagemAutorizacao t1) -> 
                t.tempoValidacao.compareTo(t1.tempoValidacao));       
        Transacao tmp = new Transacao(listaAutorizacoes.get(0).minerador,
                listaAutorizacoes.get(0).vendedor, listaAutorizacoes.get(0).comprador,
                listaAutorizacoes.get(0).moedas, listaAutorizacoes.get(0).id);
        System.out.println("enviando a transacao para todos");
        listaAutorizacoes.removeAll(listaAutorizacoes);
        messageSenderMulticast.enviarMensagem(tmp);
        }
      
    
    }
    
    private synchronized boolean procurarTransacaoBanco(int id){
        for(int i=0 ; i< banco.transacoes.size() ; i++){
           if( id == banco.transacoes.get(i).id){
           return true;
        }
    
    }
    return false;
    }
    
    private synchronized boolean verificaSenha(String senha) {
        return senha.equals("swordfish");
    }

    private synchronized int procuraCarteiraDono() {
        for (int i = 0; i < banco.carteiras.size(); i++) {
            if (banco.carteiras.get(i).retornaPorta() == this.porta) {
                return i;
            }
        }
        return -1;
    }

    public synchronized Object recebeMensagem() {
        Object object = null;
       
        try {
            byte[] tamanho = new byte[BUFFER];
            DatagramPacket messageIn = new DatagramPacket(tamanho, tamanho.length);
            socket.receive(messageIn);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(tamanho);
            ObjectInputStream objIn = new ObjectInputStream(byteIn);
            object = objIn.readObject();
            messageIn.setLength(tamanho.length);
            byteIn.reset();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ListenerReceiveMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(object.getClass().toString());
        return object;
    }

    public int getPorta() {
        return porta;
    }

    public synchronized Database getBanco() {
        return banco;
    }

}
