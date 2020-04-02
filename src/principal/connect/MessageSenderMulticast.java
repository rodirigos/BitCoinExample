/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal.connect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import static principal.Process.dormir;

/**
 * Essa classe manda uma mensagem multicast. Ela ja eh adaptada para mandar
 * mensagens multicast
 *
 * @author Rodrigo
 */
public final class MessageSenderMulticast {

    private MulticastSocket multiCastSocket;
    private InetAddress group;
    private int porta;
    private String ip;

    /** 
     * Classe que cuida do envioo de mensagem por multicast
     *
     * @param multiCastSocket O socket usado para enviar as mensagens
     * @param group O grupo que as mensagens serao enviadas
     * @param ip O endereco que sera usado
     * @param porta A porta utilizada pela conexao
     */
    public MessageSenderMulticast(MulticastSocket multiCastSocket, InetAddress group, String ip, int porta) {
        this.multiCastSocket = multiCastSocket;
        this.group = group;
        this.ip = ip;
        this.porta = porta;
        inicializaConexao(multiCastSocket);

    }

    /**
     * Inicializa a conexao
     * @param ms O multicastsocket utilizado
     */
    public void inicializaConexao(MulticastSocket ms) {
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

    private synchronized void enviarBytes(byte[] mensagem) {
        try {
            DatagramPacket messageOut = new DatagramPacket(mensagem, mensagem.length, group, porta);
            multiCastSocket.send(messageOut);
            //System.out.println("Enviado");
        } catch (IOException ex) {

        }
    }
    /**
     * Essa classe enviar uma mensagem serializada. Ela eh essencial na
     * comunicacao entre os processos. Ela pode ser usada para enviar qualquer
     * objeto serialiado
     *
     * @param object O objeto pode ser qualquer classe participante das
     * mensagens o tratamento sera feito depois
     */
    public synchronized void enviarMensagem(Object object) {
        ObjectOutputStream objectOut = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(object);
            byte[] dadosEnviados = byteOut.toByteArray();
            enviarBytes(dadosEnviados);
            dormir(100);
        } catch (IOException ex) {

        } finally {
            try {
                objectOut.close();
            } catch (IOException ex) {

            }
        }

    }
}
