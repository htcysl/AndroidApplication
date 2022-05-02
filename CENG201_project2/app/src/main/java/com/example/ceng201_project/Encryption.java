package com.example.ceng201_project;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encryption {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public PrivateKey getPrivate(){
        return this.privateKey;
    }

    public PublicKey getPublic(){
        return this.publicKey;
    }



    /**
     *  This method generate RSA key pair,
     *  key size is 1 byte for stable server-clinet connection when sending public key.
     *
     **/
    public void generateKeyPair() throws NoSuchAlgorithmException{
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair pair = generator.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }


    /**
     *
     *  This method encrypt imput string message wiht public key.
     *  this message just dcrypt wiht private key that pair of this public key
     *
     *
     **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encryptString(PublicKey publicKey, String message) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);

        // Convert stirng message to byte
        byte[] secretMessagebyte = message.getBytes(StandardCharsets.UTF_8);
        // Encrypt byte message with public key
        byte[] encrypted = cipher.doFinal(secretMessagebyte);

        // return base64 string of encrypted byte
        return this.encodeBase64(encrypted);

    }



    /**
     * This method reverse process of encryptString
     *
     * Dcrypt encrypted string to message and return it
     *
     * **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String  decryptString(PrivateKey privateKey2, String encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // decode message to bytearray
        byte[] encryptedDataByte = this.decodeBase64(encryptedData);

        Cipher cipher =  Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey2);

        // decrypt message
        byte[] decryptedMessageByte = cipher.doFinal(encryptedDataByte);

        // return string of the decrypted message byte
        return (new String(decryptedMessageByte,StandardCharsets.UTF_8));

    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encodeBase64(byte[] data){ return Base64.getEncoder().encodeToString(data); }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] decodeBase64(String data){
        return Base64.getDecoder().decode(data);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encodeBase64FromPublicKey(PublicKey publicKey){ return this.encodeBase64(publicKey.getEncoded());}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String encodeBase64FromPrivateKey(PrivateKey privateKey){return this.encodeBase64(privateKey.getEncoded());}


    /**
     *  Decode public and private base64 format of tkeys in database to classes
     *
     *
     *
     * **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public PublicKey decodeBase64ToPublicKey(String data) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        // get byte array of base64 public key
        byte[] byteKey = Base64.getDecoder().decode(data);
        // change bytearray to publickey with alogirhm
        PublicKey key = kf.generatePublic(new X509EncodedKeySpec(byteKey));
        return  key;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PrivateKey decodeBase64ToPrivateKey(String data) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        byte[] byteKey = Base64.getDecoder().decode(data);
        PrivateKey key = kf.generatePrivate(new PKCS8EncodedKeySpec(byteKey));
        return key;
    }









}
