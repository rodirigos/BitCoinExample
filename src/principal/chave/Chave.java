/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal.chave;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * Essa classe gera o par de chaves dos processos. Todos os metodos de
 * codificacao e decodificao eh feito aqui.
 *
 * @author Rodrigo
 */
public class Chave {

    private final KeyPairGenerator keyGen;
    private final KeyPair keyPair;

    /**
     *
     */
    public final PublicKey publicKey;
    public final PrivateKey privateKey;
    /**
     * Classe chave que guarda todos os processos envolvendo autenticacao e uso de chaves
     * no programa. Esse construtor gera o par de chaves assimetricas
     * @throws java.security.NoSuchAlgorithmException
     */
    public Chave() throws NoSuchAlgorithmException {
        // Inicializando a chave. Todo processo so possui uma classe chave
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        keyPair = keyGen.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }
    
    

    /**
     * Essa funcao serve para autenticar usuarios de outros processos.
     * @param mensagem: Mensagem a ser decodificada usando o chave publica. Essa funcao eh usada para
     * verificar a autenticidade de uma transacao feita por outro usuario
     * @param chave: A chave publica do outro usuario. Quando verificada a autenticidade, a transacao pode ser autorizada
     * @return: Retorna a mensagem decodificada
     */
    public static String decodificarUsandoPublic(byte[] mensagem, PublicKey chave) {
        byte[] decodificada = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, chave);
            decodificada = cipher.doFinal(mensagem);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
        }
        return new String(decodificada);
    }

    /**
     * Essa funcao eh feita para decodificar uma mensagem que foi enviada exclusivamente ao 
     * usuario detentor da chave privada. No caso, o proprio processo. 
     * @param mensagem: Mensagem que foi decodificada com a chave publica do usuario
     * @param chave: Chave privada do usuario que somente ele possui
     * @return: Retorna a mensagem decodificada
     */
    public static String decodificarUsandoPrivate(byte[] mensagem, PrivateKey chave) {
        byte[] decodificada = null;
        try {

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, chave);
            decodificada = cipher.doFinal(mensagem);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
        }
        return new String(decodificada);

    }

    /**
     * Essa funcao eh necessaria quando o usuario quer mandar uma mensagem a um usuario especifico
     * usando a chave publica do usuario
     * @param texto: Mensagem a ser enviada ao usuario detentor da chave privada
     * capaz de decodificar a mensagem
     * @param chave: Chave publica que vai decodificar a mensagem
     * @return: Retorna um vetor de bytes que vai ser convertido em uma string posteriormente
     */
    public static byte[] codificarUsandoPublic(String texto, PublicKey chave) {
        byte[] textoCodificado = null;
        try {

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, chave);
            textoCodificado = cipher.doFinal(texto.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
        }
        return textoCodificado;
    }

    /**
     * Essa funcao eh usada para o processo enviar uma mensagem que vai identificar ele 
     * com a assinatura digital. Sera enviada no quando entrar no broadcast e quando realizar 
     * uma mineracao
     * @param texto: Texto que vai autenticar o processos para os outros processos
     * @param chave: Chave privada que somente o processo possui
     * @return: o texto codificado que somente a chave publica do usuario pode abrir
     */
    public static byte[] codificarUsandoPrivate(String texto, PrivateKey chave) {
        byte[] textoCodificado = null;
        try {

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, chave);
            textoCodificado = cipher.doFinal(texto.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
        }
        return textoCodificado;
    }

}
