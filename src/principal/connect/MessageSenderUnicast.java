/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal.connect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Classe criada para mandar uma mensagem unicast. Como o programa eh executado
 * localmente, cada porta significa um processo.
 *
 * @author Rodrigo
 */
public class MessageSenderUnicast {

    // private int porta;
    private final String IP = "localhost";
    private InetAddress ipAddress;

    /**
     *
     * @param ipAddress O ip address guardado da maquina que executa o programa.
     * @throws IOException
     */
    public MessageSenderUnicast(InetAddress ipAddress) throws IOException {
        this.ipAddress = ipAddress;

    }

    /**
     * Funcao que envia mensagem unicast a uma determinada porta dentro do localhost
     * @param object Objeto que sera enviado a outro processo
     * @param porta Porta utilizada para o envioo
     * @throws IOException
     */
    public synchronized void sendMessage(Object object, int porta) throws IOException {
        //  System.out.println(porta);
        DatagramSocket datagramSocket = new DatagramSocket();
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
        objectOut.writeObject(object);
        byte[] dadosEnviados = byteOut.toByteArray();
        DatagramPacket messageOut = new DatagramPacket(dadosEnviados, dadosEnviados.length, ipAddress, porta);
        datagramSocket.send(messageOut);
        //dormir(50);
        //System.out.println("Enviado");
        objectOut.close();

    }

    /**
     * Funcao para o programa entrar em tempo de espera por determinado tempo. ELe
     * e utilizado para problemas com sincronizacao
     * @param tempo Tempo de espera
     */
    public void dormir(int tempo) {
        try {
            sleep(tempo);
        } catch (InterruptedException ex) {

        }
    }
}
