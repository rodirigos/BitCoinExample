///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package teste;
//
//import java.security.InvalidKeyException;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//
///**
// *
// * @author Rodrigo
// */
//public class TesteRsa {
//
//    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
//        // Gerando os pares de chaves 
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(512);
//        KeyPair keyPair = keyGen.genKeyPair();
//        // Colocando a chave publica em bytes
//        //byte[] publicKey = keyGen.genKeyPair().getPublic().getEncoded();
//        PublicKey publicKey = keyPair.getPublic();
//        // Coloando a chave privada em bytes
//        //byte[] privateKey = keyGen.genKeyPair().getPrivate().getEncoded();
//        PrivateKey privateKey = keyPair.getPrivate();
//
//        // Criando uma mensagem para o teste
//        String mensagem = "Testando a criptografia";
//
//        // Bateria de teste
//        // Testando a codificacao com a chave publica
//        byte[] codificado = codificarUsandoPublic(mensagem, publicKey);
//        String mensagemCodificada = new String(codificado);
//        System.out.println(mensagemCodificada);
//        //decodifiando a mensagem com a chave privada
//        String mensageDecodificada = decodificarUsandoPrivate(codificado, privateKey);
//        System.out.println(mensageDecodificada);
//
//        // Testando fazer o inverso
//        byte[] codificado2 = codificarUsandoPrivate(mensagem, privateKey);
//        String mensagemCodificada2 = new String(codificado2);
//        System.out.println(mensagemCodificada2);
//        String mensagemDecodificada2 = decodificarUsandoPublic(codificado2, publicKey);
//        System.out.println(mensagemDecodificada2);
//
//    }
//
//    public void imprimiChave(byte[] publicKey) {
//        StringBuffer retString = new StringBuffer();
//        for (int i = 0; i < publicKey.length; ++i) {
//            retString.append(Integer.toHexString(0x0100 + (publicKey[i] & 0x00FF)).substring(1));
//        }
//        System.out.println(retString);
//
//    }
//
//    public static String decodificarUsandoPublic(byte[] mensagem, PublicKey chave) {
//        byte[] decodificada = null;
//        try {
//
//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.DECRYPT_MODE, chave);
//            decodificada = cipher.doFinal(mensagem);
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
//        }
//        return new String(decodificada);
//    }
//
//    public static String decodificarUsandoPrivate(byte[] mensagem, PrivateKey chave) {
//        byte[] decodificada = null;
//        try {
//
//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.DECRYPT_MODE, chave);
//            decodificada = cipher.doFinal(mensagem);
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
//        }
//        return new String(decodificada);
//
//    }
//
//    public static byte[] codificarUsandoPublic(String texto, PublicKey chave) {
//        byte[] textoCodificado = null;
//        try {
//
//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.ENCRYPT_MODE, chave);
//            textoCodificado = cipher.doFinal(texto.getBytes());
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
//        }
//        return textoCodificado;
//    }
//
//    public static byte[] codificarUsandoPrivate(String texto, PrivateKey chave) {
//        byte[] textoCodificado = null;
//        try {
//
//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.ENCRYPT_MODE, chave);
//            textoCodificado = cipher.doFinal(texto.getBytes());
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
//        }
//        return textoCodificado;
//    }
//
//}
