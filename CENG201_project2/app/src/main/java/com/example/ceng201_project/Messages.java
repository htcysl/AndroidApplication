package com.example.ceng201_project;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 *  Messages class for creating sipesific message.
 *
 *  All message send to Server with json string format.
 *  To simplfy this object of message used with "Gson" libs.
 *  All messages creating from message class and convert it to json String.
 *
 *  The template of a message is :
 *
 *  {"senderId": SENDERID , "receiverId": RECEİVERID , "type" : TYPE, "data": MSG}
 *
 *
 *
 *
 * **/

public abstract class Messages {
    public static final int REQUEST_USER = 0;     // to client
    public static final int STRING_MESSAGE = 1;   // server & client
    public static final int RESPONSE_USER = 2;    // client
    public static final int REGISTER_NEW_USER = 3;  // server
    public static final int FIRST_MESSAGE = 4 ;




    /**
     *  General ussage method for all simple message. All message of send to another client
     *  should use this function
     *
     *  input:
     *      senderPhoneNumber := String sender number "+905555522929" etc.
     *      receiverPhoneNum  := String receiver number
     *      message := String message of client
     *      db := ClientDatabase for get receiver public key
     *
     *  return:
     *       Encoded and Encrypted String json message
     *
     *
     * **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String createStringMessage(String senderPhoneNum, String receiverPhoneNum, String message, ClientDatabase db)
            throws NoSuchAlgorithmException, SQLException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {

        Encryption E = new Encryption();
        PublicKey key;
        String publickey = db.getUser(receiverPhoneNum).getPublicKey();
        key = E.decodeBase64ToPublicKey(publickey);


        Message m = new Message();
        m.setSenderId(senderPhoneNum);
        m.setReciverId(receiverPhoneNum);
        m.setType(Messages.STRING_MESSAGE);
        m.setData(message,key);

        return toJson(m);
    }


    /**
     *  This method create message to send server client id,
     *
     *  When a clinet connect to server server need to know client id to redirect other
     *  client message to the user
     *
     *  This method uses after every connection requst to server
     *
     * **/
    public static String firstMessage(String clientID) throws NoSuchAlgorithmException {
        Message m = new Message();
        m.setSenderId(clientID);
        m.setReciverId("Server");
        m.setType(Messages.FIRST_MESSAGE);

        return toJson(m);
    }


    /**
     *  This method create message for reqister a user to server database
     *
     *  When User reqister in application application should send a message
     *  to server to reqister user to server database
     *
     * */
    public static  String reqisterUserMessage(User rootUser) throws NoSuchAlgorithmException {
        Message m = new Message();
        m.setType(Messages.REGISTER_NEW_USER);
        m.setReciverId("Server");
        m.setData(toJson(rootUser));
        return toJson(m);
    }


    /**
     *  This method create message with phone number for requst user information.
     *
     * **/
    public  static  String createUserRequestMessage(String phonenumber) throws NoSuchAlgorithmException {
        // User request message different type that
        Message m = new Message();
        m.setSenderId("");
        m.setReciverId("Server");
        m.setType(Messages.REQUEST_USER);
        m.setData(phonenumber);
        return toJson(m);
    }


    /**
     *  Server create response of requst user from client with this method
     *
     *
     *  {"senderId": SENDERID , "receiverId": RECEİVERID , "type" : TYPE,
     *  "data":
     *  "{"profilePicture": PHOTO, "username" : USERNAME , "userID" : USERID , "publicKey" : PUBLİCKEY}
     *  "}
     *
     *
     * */
    public  static String userResponse(User user) throws NoSuchAlgorithmException {
        Message m = new Message();
        m.setSenderId("Server");
        m.setType(Messages.RESPONSE_USER);
        m.setData(toJson(user));

        return toJson(m);
    }


    /**
     *  Converting User class based json string to User object and return
     *
     * */
    public static  User convertRequstedUser(String jsonString){
        Gson gson = new Gson();
        String userjson = toObject(jsonString).getData();
        return gson.fromJson(userjson, User.class);
    }


    /**
     *  Convert Message class based json string Message class and return
     *
     * **/
    public static Message toObject(String jsonString){
        Gson gson = new Gson();
        return  gson.fromJson(jsonString, Message.class);
    }


    /**
     * Convert any object to json format
     *
     * **/
    public static String toJson(Object m){
        Gson gson =  new Gson();
        return gson.toJson(m);
    }



}





