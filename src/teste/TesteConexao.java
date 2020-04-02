///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package teste;
//
//import java.io.IOException;
//import java.security.NoSuchAlgorithmException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import principal.Banco;
//import principal.Carteira;
//import principal.CarteiraDono;
//import principal.connect.ConexaoMulticast;
//import principal.mensagem.GerenciadorMensagens;
//import static java.lang.Thread.sleep;
//import java.net.MulticastSocket;
//import principal.connect.ListenerReceive;
//import principal.mensagem.MensagemCredencial;
//
//
//
///**
// *
// * @author Rodrigo
// */
//public class TesteConexao {
//
//    public static void main(String args[]) throws IOException, NoSuchAlgorithmException  {
//        // Inializando o banco para todo
//       MulticastSocket multicastSocket = new MulticastSocket(6789);
//       Banco banco = new Banco();
//       ConexaoMulticast conexaoMulti = new ConexaoMulticast("228.5.6.7", multicastSocket);
//       GerenciadorMensagens gerenciadorMensagens = new GerenciadorMensagens(banco, conexaoMulti);
//       Thread receiverThread;
//       receiverThread = new Thread(new ListenerReceive(multicastSocket, gerenciadorMensagens));
//       receiverThread.start();
//        System.out.println("Thread iniciada");
//       // Criando o dono da carteira
//       CarteiraDono carteiraDono = new CarteiraDono();
//       Carteira carteira = new Carteira(carteiraDono.retornaChavePublica(), carteiraDono.id);
//       // Criando uma mensagem credencial
//       MensagemCredencial credencial = new MensagemCredencial(carteira);
//        System.out.println("Enviando a credencial");
//       conexaoMulti.enviarMensagem(credencial);
//        dormir(200);
//        imprimirCarteiras(banco);
//        
//    }
//    
//    public static void imprimirCarteiras(Banco banco){
//        for(int i =0 ; i > banco.carteiras.size(); i++){
//            System.out.println(banco.carteiras.get(i));
//        }
//    }
//    
//    
//    public static void dormir(int tempo){
//         try {
//            sleep(tempo);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(TesteConexao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
