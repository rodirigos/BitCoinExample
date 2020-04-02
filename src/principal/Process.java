/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import principal.connect.ListenerReceiveMulticast;
import principal.connect.MessageSenderMulticast;
import java.net.DatagramSocket;
import principal.chave.Chave;
import principal.connect.ListenerReceiverUnicast;
import principal.connect.MessageSenderUnicast;
import principal.mensagens.MensagemRequerimentoCompra;
import static java.lang.Thread.sleep;

/**
 * Classe que engloba a maioria dos processos.
 *
 * @author Rodrigo
 */
public class Process {

    /**
     * *
     */
    public  static MulticastSocket multiCastSocket;
    public static Thread listenerReceiveMulticast;
    public static InetAddress group;
    public static final String IP = "224.0.0.2";
    public static final int PORTA = 6789;
    public static boolean menuLoop;
    public static ListenerReceiveMulticast listenerHandlerMulticast;
    public static MessageSenderMulticast messageSendermc; 
    public static InetAddress unicast;
    public static Thread listenerReceiverUnicast;
    public static ListenerReceiverUnicast listenerHandlerUnicast;
    public static MessageSenderUnicast messageSenderuc;
    public static DatagramSocket unicastSocket;
    public static WalletOwner carteiraDono;
    public static Wallet carteiraDonocopia;
    /*O processo eh o minerador. Ele proura verificar se uma transacao eh valida ou nao
        . Para validar a transacao deve ser chamado logo a necessidade de um listener soh para
        escutar se existe alguma transacao para ser validada */
    public Process() throws IOException, NoSuchAlgorithmException {
 
        //Pegando o local host ( Usado somente para computadores diferentes)
        unicast = InetAddress.getLocalHost();
        // Criando o unicastsocket ealocando uma porta disponivel
        unicastSocket = new DatagramSocket(0);     
        // Criando um multicastsocket
        multiCastSocket = new MulticastSocket(PORTA);
        // Criando o enviador de mensagem. Vale lembrar que fica guardado o nome da maq e ip
        messageSenderuc = new MessageSenderUnicast(unicast);     
        // Criando o servidor multicast
        // Criando o enviador de mensagens
        messageSendermc = new MessageSenderMulticast(multiCastSocket, group, IP, PORTA);
        // Criando o usuario        
         carteiraDono = new WalletOwner();
        // Criando um server unicast com a primeira porta disponivel
        listenerHandlerUnicast = new ListenerReceiverUnicast(messageSendermc, messageSenderuc, Database.getBanco(), unicastSocket, carteiraDono);
        // Criando o servidor unicast 
        // Por motivos de limitacao de usar no mesmo pc a porta sera o diferencial de cada server unicast
        listenerReceiverUnicast = new Thread(listenerHandlerUnicast);
        listenerReceiverUnicast.start();        
        //Imprimindo a porta
        System.out.println(listenerHandlerUnicast.getPorta());
       
        carteiraDonocopia = new Wallet(carteiraDono.retornaChavePublica(), carteiraDono.id, unicast, 
                listenerHandlerUnicast.getPorta());    
        listenerHandlerMulticast = new ListenerReceiveMulticast(messageSenderuc,messageSendermc,
                Database.getBanco(), multiCastSocket, group, PORTA, IP, carteiraDono, carteiraDonocopia);
        listenerReceiveMulticast = new Thread(listenerHandlerMulticast);
        listenerReceiveMulticast.start();
        // Enviando carteira para mostrar que voce chegou a outros 
        messageSendermc.enviarMensagem(carteiraDonocopia);
        // esperando a resposta com os unicasts dos outros usuarios
        dormir(300);
        // Adicionando o usuario no proprio banco
        Database.getBanco().carteiras.add(carteiraDonocopia);
 
      
    }
    
    /**
     * O envioo da mensagem para pedir moedas de um vendedor.
     * @param moedas A quantidade de moedas a ser pedida
     * @param indice O indice no banco do usuario do processo do outro usuario
     * @throws IOException
     */
    public static void enviarMensagemCompra(int moedas, int indice ) throws IOException{
        byte[] criptografada;
        criptografada = Chave.codificarUsandoPrivate("swordfish", carteiraDono.retornaChave().privateKey);
        MensagemRequerimentoCompra requerimentoCompra = new MensagemRequerimentoCompra(carteiraDonocopia,
                 moedas, criptografada);
         messageSenderuc.sendMessage(requerimentoCompra, Database.getBanco().carteiras.get(indice).retornaPorta());
    }
    
    public static void imprimirConteudo(Database banco) {
        for (int i = 0; i < banco.carteiras.size(); i++) {
            System.out.println("As carteiras de id:"+banco.carteiras.get(i).id);
            System.out.println("Com "+banco.carteiras.get(i).getMoedas());
        }
        for(int j = 0 ; j< banco.transacoes.size(); j++){
            System.out.println(banco.transacoes.get(j).moedas);
        
        }
    }

    public static void dormir(int tempo) {
        try {
            sleep(tempo);
        } catch (InterruptedException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
