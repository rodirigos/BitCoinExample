/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal.transacoes;

import java.io.Serializable;
import principal.Wallet;

/**
 *
 * @author Rodrigo
 */
public class Transacao implements Serializable{

    public Wallet vendedor;
    public Wallet comprador;
    public Wallet minerador;
    public int moedas;
    public int id;

   

    /**
     * @param vendedor Vendedor do produto. Ele ira disponiblizar as moedas
     * @param comprador
     *
     */
    public Transacao(Wallet minerador, Wallet vendedor, Wallet comprador, int moedas, int id) {
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.moedas = moedas;
        this.minerador = minerador;
        this.id = id;
    }
    
   
}
