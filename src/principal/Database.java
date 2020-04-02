/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.io.Serializable;
import java.util.ArrayList;
import principal.transacoes.Transacao;

/**
 * Essa classe vai ser atualizada para todos os usuarios da rede peer to peer
 *
 * @author Rodrigo
 */
public class Database implements Serializable {
// Database eh singleton
    static Database banco = new Database();


    volatile public ArrayList<Wallet> carteiras;
    volatile public ArrayList<Transacao> transacoes;

    private Database() {
        this.carteiras = new ArrayList<>();
        this.transacoes = new ArrayList<>();
    }
    static public Database getBanco() {
        return banco;
    }
}
