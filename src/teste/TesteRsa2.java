///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package teste;
//
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//
///**
// *
// * @author Rodrigo
// */
//// 
//public class TesteRsa2 {
//     public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(512);
//        byte[] publicKey = keyGen.genKeyPair().getPublic().getEncoded();
//        StringBuffer retString = new StringBuffer();
//        retString.append("[");
//        for (int i = 0; i < publicKey.length; ++i) {
//            retString.append(publicKey[i]);
//            retString.append(", ");
//        }
//        retString = retString.delete(retString.length()-2,retString.length());
//        retString.append("]");
//        System.out.println(retString); //e.g. [48, 92, 48, .... , 0, 1]
//    }
//}
