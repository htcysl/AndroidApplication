package com.example.ceng201_project;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Client {
    public String clientPhoto;
    public String clientId;
    public PublicKey publicKey;
    public PrivateKey privateKey;

    private Socket client;
    private ClientDatabase db;
    private Encryption E;


    private String ipv4 ;
    private int port;


    /**
     *   Consturactor need server ipv4 address and port to conndect it
     */
    public Client(String ipv4,int port){
        this.ipv4 = ipv4;
        this.port = port;
    }


    /**
     *  Start recieving message form server
     *
     * */
    public void startClient() throws IOException, NoSuchAlgorithmException {
        this.client = new Socket(ipv4,port);
        Receiver receiver = new Receiver(this.client.getInputStream(), this.db);
        //Start recieving message from server
        receiver.start();
        // Sent userıd to server to recive message
        firstMessage();

    }




    public ClientDatabase createDatabaseOnesTime(Context context){
        db = new ClientDatabase(context);
        return db;
    }

    /**
     *   This function create key pair (public and private) every client needs to
     *   create their own key pair and they will store private key and publich publickey
     *
     *   When I client want to send message to our client it should ecrypt message with our public key
     *   So ve can read data with our private key
     *
     *   this funcion will start just ones time at installation of app
     *
     **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createPublicPrivateKey() throws NoSuchAlgorithmException, SQLException {
        E = new Encryption();
        E.generateKeyPair();
        this.privateKey = E.getPrivate();
        this.publicKey = E.getPublic();
        this.db.addRootAuthTokens(this.E.encodeBase64FromPublicKey(publicKey),this.E.encodeBase64FromPrivateKey(privateKey));

    }



    /**
     *   this method inputs all people in addresbook with arraylist and requsts tehem from server
     *
     *
     **/
    public void getAllUserInfo(ArrayList UserPhoneNumberList) throws NoSuchAlgorithmException, IOException {

        // Request All user in the list
        for(int i=0 ; i< UserPhoneNumberList.size() ; i++){

            // Create message and send it to server
            String message = Messages.createUserRequestMessage( (String) UserPhoneNumberList.get(i));
            sendMessage(message);
        }


    }

    /**
     *
     *   After reqistration this method send information to server
     *
     *
     **/
    public void reqisterRootUserToServer() throws SQLException, NoSuchAlgorithmException, IOException {
        User user = this.db.getRootUserToSendServer();
        String message = Messages.reqisterUserMessage(user);
        sendMessage(message);
    }




    private void firstMessage() throws IOException, NoSuchAlgorithmException {
        String message = Messages.firstMessage(this.clientId);
        sendMessage(message);
    }


    public void sendMessage(String msg) throws IOException {
        Sender sender = new Sender(this.client.getOutputStream());
        sender.setMessage(msg);
        sender.start();
    }







}


class Receiver extends Thread{
    /**
     *  Receiver class, a class that always round background and recieve message,
     *  Recieve message -> parse message -> Database
     *
     * **/




    private InputStream in;
    private ClientDatabase db;
    private Encryption E;
    private PrivateKey privateKey;

    byte[] data;
    String message;


    public Receiver(InputStream in, ClientDatabase db){
        this.in = in;
        this.db = db;
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run(){
        /**
         * Read everytime message and send message to message processor
         *
         *
         **/

        while (true){
            try {
                in.read(data);
                message = new String(data);
                messageProccessor(message);



            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }



        }

    }


    /**
     *  This method convert json string to message object and maka decision
     *  if message is RESPONSE_USER that means server send client a user infomation
     *  if message is STRING_MESSAGE that means server redirect a message from another user to us
     *
     *
     **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void messageProccessor(String message) throws SQLException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        // Convert string message to object message
        Message m = Messages.toObject(message);

        if(m.getType() == Messages.RESPONSE_USER){
            // if the response have User object as string we convert it to object
            User user = Messages.convertRequstedUser(m.getData());
            // Add it to database
            this.db.addUser(user);

        }
        else if(m.getType() == Messages.STRING_MESSAGE){
            // if Message type String message this message encrypted with our publickey
            // just our private key dcrypt it
            String senderNum = m.getSenderId();
            String receiverNum = m.getReciverId();
            String data = m.getData();
            String decryptedMessage = E.decryptString(this.privateKey,data);

            // add message to database
            this.db.addMessage(senderNum,receiverNum,decryptedMessage);


        }
    }



}





class Sender extends Thread{
    /**
     *  Sender class for message sending procces handing
     *
     *
     * **/



    private OutputStream out;
    public String message;


    Sender(OutputStream out){
        this.out = out;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void run(){
        try {
            out.write(message.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

