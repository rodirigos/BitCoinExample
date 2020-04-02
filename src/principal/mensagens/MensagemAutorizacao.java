/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal.mensagens;

import java.io.Serializable;
import java.sql.Timestamp;
import principal.Wallet;

/**
 *
 * @author Rodrigo
 */
public class MensagemAutorizacao implements Serializable{

    public Wallet comprador;
    public Wallet vendedor;
    public int moedas;
    public Timestamp tempoValidacao;
    public int id;
    public Wallet minerador;

    public MensagemAutorizacao(Wallet comprador, Wallet vendedor, int moedas,
            int idTransacao, Wallet minerador) {
        this.comprador = comprador;
        this.vendedor = vendedor;
        this.moedas = moedas;
        this.tempoValidacao = new Timestamp(System.currentTimeMillis());
        this.id = id;
        this.minerador = minerador;

    }

}
