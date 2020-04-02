/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal.mensagens;

import java.io.Serializable;
import principal.Wallet;

/**
 * Essa mensagem eh enviada em modo broadcast para pedir que validem a transacao
 * a transacao eh passada juntamente com essa mensagem
 * @author Rodrigo
 */
public class MensagemRequerimentoAutorizacao implements Serializable{
   public Wallet comprador;
   public Wallet vendedor;
   public int moedas;
   public byte[] codificadaComprador;
   public byte[] codificadaVendedor;
   public int idTransacao;

    public MensagemRequerimentoAutorizacao(Wallet comprador, Wallet vendedor, 
            int moedas, byte[] codificadaComprador, byte[] codificadaVendedor, int idTransacao) {
        this.comprador = comprador;
        this.vendedor = vendedor;
        this.moedas = moedas;
        this.codificadaComprador = codificadaComprador;
        this.codificadaVendedor = codificadaVendedor;
        this.idTransacao = idTransacao;
       
    }   
    
   
    
}
