/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.io.Serializable;
import java.net.InetAddress;
import java.security.PublicKey;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Classe que carrega todos os dados publicos de um processo. 
 * @author Rodrigo
 */
public class Wallet implements Serializable {

    private int moedas;
    public int id;
    public PublicKey publicKey;
    public int preco;
    private InetAddress iNetAddress;
    private int porta;

    /**
     * Esse construtor eh usado para adicionar os nomes no banco de dados. Os
     * bancos de dados nao tem acesso a chave privada do usuario
     *
     * @param publicKey chave publica do usuario
     * @param id Id que o usuario esta utilizando
     * @param iNetAddress endereco ip da maquina. Nao usado pois os testes foram 
     * feitos localmente
     * @param porta Porta que a carteira esta ligada para o uso no servidor unicast
     */
    public Wallet(PublicKey publicKey, int id, InetAddress iNetAddress, int porta) {
        this.publicKey = publicKey;
        this.id = id;
        this.moedas = 100;
        this.iNetAddress = iNetAddress;
        this.preco = retornaPreco(1, 15);
        this.porta = porta;

    }

    // Aqui tem um metodo que transforma em bytes a chave publica somente usado
    // caso a chave public seja do proprio metodo
    public byte[] getChavePublica() {
        return publicKey.getEncoded();
    }

    /**
     * Cria um numero randomico com um minimo e um maximo
     * @param min O minimo valor do numero randomico
     * @param max O maximo valor do numero randomico    
     * @return o numero randomico
     */
    public int retornaPreco(int min, int max) {
        int random = ThreadLocalRandom.current().nextInt(min, max + 1);
        return random;
    }

    public int getMoedas() {
        return moedas;
    }

    public void descontaMoedas(int desconto) {
        this.moedas = moedas + desconto;
    }

    public int retornaPorta() {
        return porta;
    }
}
