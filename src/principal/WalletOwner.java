/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.concurrent.ThreadLocalRandom;
import principal.chave.Chave;


/**
 * A carteira dono eh uma unica carteira no processo. Ela tem acesso a chave privada
 * do processo e a unica capaz de codificar e deocidificar mensagens. Ela sera usada 
 * as transacoes e ela que possui a assinatura digital do processo
 * @author Rodrigo
 */
public class WalletOwner {

    // Somente para o proprio processo
    private final Chave chave;
    public int id;

    /**
     * Para isolar alguns campos relacionados ao processo como a chave privada.
     * A classe carteira dono tem acesso a todos os metodos que verificam a
     * autenticidade do processo.
     * @throws java.security.NoSuchAlgorithmException Erro retornado caso a chave
     * nao consiga ser criada
     */
    public WalletOwner() throws NoSuchAlgorithmException {
        this.chave = new Chave();
        this.id = retornaId(5000);
    }
    
    private int retornaId(int range) {
        return ThreadLocalRandom.current().nextInt(range);
    }
    
    public Chave retornaChave(){
     return chave;
    }
    
    /**
     * Retorna a chave publica
     * @return a chave publica
     */
    public PublicKey retornaChavePublica(){
        return chave.publicKey;
    }

}
