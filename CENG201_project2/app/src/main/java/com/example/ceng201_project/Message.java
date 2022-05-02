package com.example.ceng201_project;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Message extends Messages{

    private String senderId;
    private String receiverId;
    private int type;
    private String data;


    public Message()  {

    }


    public String getSenderId(){
        return  this.senderId;
    }

    public String getReciverId(){
        return this.receiverId;

    }

    public int getType(){
        return this.type;
    }

    public String getData(){
        return  this.data;
    }

    public void setSenderId(String senderId) throws NoSuchAlgorithmException {
        this.senderId = senderId;
    }

    public void setReciverId(String receiverId) throws NoSuchAlgorithmException {
        this.receiverId = receiverId;
    }

    public void setType(int type){
        this.type = type;
    }


    /**
     *  setData method with no ancryption this is used for sending user information to server
     *  and receive it.
     *
     *
     * */
    public void setData(String data){
        this.data = data;
    }


    /**
     *  setData method to encryption message.
     *
     *  This method need receiver public key to encrypt message
     *
     * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setData(String data,PublicKey publicKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        Encryption e = new Encryption();
        this.data = e.encryptString(publicKey,data);
    }
}

