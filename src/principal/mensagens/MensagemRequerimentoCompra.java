/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal.mensagens;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import principal.Wallet;

/**
 * Essa classe eh exclusivamente para ser mandada em modo unicast ... Significa
 * que voce deseja comprar algo de alguem.
 *
 * @author Rodrigo
 */
public class MensagemRequerimentoCompra implements Serializable {

    // O nome do comprador e a quantidade de moedas que ele deseja 
    // do vendedor.
    public Wallet comprador;
    public int moedas;
    public byte[] mensagemCriptografada;
    public int id;

    public MensagemRequerimentoCompra(Wallet comprador, int moedas, byte[] mensagemCriptografada) {
        this.comprador = comprador;
        this.moedas = moedas;
        this.mensagemCriptografada = mensagemCriptografada;
        this.id = retornaId(5000);
        
    }
       private int retornaId(int range) {
        return ThreadLocalRandom.current().nextInt(range);
    }
    

}
