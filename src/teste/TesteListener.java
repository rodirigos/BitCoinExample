///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package teste;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.MulticastSocket;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author Rodrigo
// */
//public class TesteListener implements Runnable {
//    // Usando o socket como referencia
//    private MulticastSocket s;
//    public TesteListener(MulticastSocket s){
//        this.s = s;
//    }
//    
//    // Essa thread escuta todas as mensagens enviadas no broadcast
//    @Override
//    public void run() {
//       while(true){
//        try {
//            byte[] buffer = new byte[1000];
//            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
//            s.receive(messageIn);
//            System.out.println("Received:" + new String(messageIn.getData()));
//        } catch (IOException ex) {
//            Logger.getLogger(TesteListener.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//    
//    }
//    
//}
