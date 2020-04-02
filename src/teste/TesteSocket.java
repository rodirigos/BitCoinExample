///*
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package teste;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.DatagramPacket;
//import java.net.InetAddress;
//import java.net.MulticastSocket;
//import java.net.SocketException;
//import java.security.PublicKey;
//
///**
// *
// * @author Rodrigo
// */
//public class TesteSocket {
//   MulticastSocket ms ;
//   Thread conThread;
//    Variavel temporaria
//   String args;
//   public PublicKey publicKey;
//  /**
//     * @param s: Socket vindo do processo. A classe serve para descentralizar 
//     * o codigo do processo
//     * @param publicKey
//     * @param args: Argumentos vindos da main para definir as transacoes
//     * @throws java.io.IOException
//     */ 
//  public TesteSocket(String args , PublicKey publicKey) throws IOException{ 
//  this.ms = new MulticastSocket();
//  this.args = args;
//  this.publicKey = publicKey;
//  }
//
//  public void inicializarConexao(){
//   try {
//       args give message contents and destination multicast group (e.g. "228.5.6.7")
//            InetAddress group = InetAddress.getByName(args);
//             Criando uma variavel de saida
//            boolean exit = false;
//            ms = new MulticastSocket(6789);
//            ms.joinGroup(group);
//
//            conThread = new Thread(new TesteListener(ms));
//            conThread.start();
//               
//            while (exit == false) {
//                 BufferedReader buffRead = new BufferedReader(new InputStreamReader(System.in));
//                  byte[] m = args[0].getBytes();
//                byte[] m3 = buffRead.readLine().getBytes();
//                DatagramPacket messageOut = new DatagramPacket(m3, m3.length, group, 6789);
//                 O s eh o socket que sera compartilhado com a thread
//               ms.send(messageOut);
//                if ("exit".equals(m3.toString())) {
//                    exit = true;        
//                }
//            }
//            conThread.interrupt();
//            ms.leaveGroup(group);
//        } catch (SocketException e) {
//            System.out.println("Socket: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("IO: " + e.getMessage());
//        } finally {
//            if (ms != null) {
//                ms.close();
//            }
//        }
//    }
//  
//  
//  
//  }
//
//
