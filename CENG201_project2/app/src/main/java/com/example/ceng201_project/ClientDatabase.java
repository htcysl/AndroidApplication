package com.example.ceng201_project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientDatabase extends SQLiteOpenHelper {


    public ClientDatabase(@Nullable Context context) {
        super(context, "Client.db", null, 1);
    }



    @Override


    /**
     *  Creating tables for Client.db
     *
     * **/
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String  RootUser = "CREATE TABLE RootUser(id INTEGER PRIMARY KEY AUTOINCREMENT,photo TEXT ,nickName TEXT,userId TEXT ,publicKey TEXT ,privateKey TEXT )";
        String  userTable = "CREATE TABLE User(photo TEXT NOT NULL,nickName TEXT NOT NULL,userId TEXT NOT NULL,publicKey TEXT NOT NULL)";
        String messagesTable = "CREATE TABLE Messages(senderId TEXT NOT NULL, recieverId TEXT NOT NULL, message TEXT NOT NULL)";

        sqLiteDatabase.execSQL(RootUser);
        sqLiteDatabase.execSQL(userTable);
        sqLiteDatabase.execSQL(messagesTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    /**
     *  existsRootuser this function check if RootUser Exist or not
     *
     *  if RootUser exist no need to verfy number and reqister proccess
     *
     *
     * **/
    public boolean existsRootuser(){
        String query = "SELECT userId FROM RootUser WHERE id=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            return  true;

        }
        else {
            return  false;

        }


    }


    /**
     *  this method add phone number that verified to RootUser table.
     *  The RootUser is user of application
     *
     * **/

    public boolean addPhooneNumber(String phoneNumber){
        // Phone number should be formated as "+90**********"
        String query = "UPDATE RootUser SET userId='"+phoneNumber+"' WHERE id=1";

        SQLiteDatabase db =this.getWritableDatabase();

        db.execSQL(query);
        return true;

    }


    public void addRootUserPhoto(String Base64Picture,@Nullable String nickname){
        String query = null;
        if(nickname != null)
             query = "UPDATE RootUser SET photo='" + Base64Picture +"',nickName='"+ nickname+"' WHERE id=1";
        else
             query = "UPDATE RootUser SET photo='" + Base64Picture +"' WHERE id=1";

        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }



    public void addRootAuthTokens(String publickeyBase64 , String privateKeyBase64) {
        String query = "UPDATE RootUser SET publicKey='"+publickeyBase64+"',privateKey='"+privateKeyBase64+"'  WHERE id=1";

        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL(query);
        db.close();

    }

    /**
     *  Get RootUser from database and return it with User obbject
     *
     *  every sectioon of reqistration procces datas insert to databsese and this method all datas to send server.
     *  Server get user information to send other user.
     *
     *
     * **/
    public User getRootUserToSendServer()  {
        String query = "SELECT * FROM RootUser WHERE id=1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        User user = null;

        if(cursor.moveToFirst()){
            String photo = cursor.getString(0);
            String nickname = cursor.getString(1);
            String userId = cursor.getString(2);
            String publicKey = cursor.getString(3);
            user = new User(photo,nickname,userId,publicKey);

        }else {

        }

        cursor.close();
        db.close();

        return user;
    }


    /**
     *  The database of User store user information
     *  this method add user information to database
     *
     * */
    public void addUser(User user)  {
        String query = "INSERT INTO User VALUES('"+ user.getProfilePicture()+"','"+user.getUsername()+"','"+user.getUserID()+"','"+user.getPublicKey()+"')";

        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL(query);
        db.close();

    }

    /**
     *  When a message send or recieve message should store in database for no losting
     *
     *  Message tables have 3 column sender, reciever,message
     *
     * **/
    public void addMessage(String senderId, String recieverId, String message) throws SQLException {
        String query = "INSERT INTO Messages VALUES('"+ senderId +"','"+ recieverId + "','" +message + "')" ;

        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    /**
     *  Geting user information with their phone number
     *
     * **/
    public User getUser(String phoneNumber) throws SQLException {
        String query = "SELECT * FROM User WHERE userId='"+phoneNumber+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        User user = null;

        if(cursor.moveToFirst()){
            String photo = cursor.getString(0);
            String nickname = cursor.getString(1);
            String userId = cursor.getString(2);
            String publicKey = cursor.getString(3);
            user = new User(photo,nickname,userId,publicKey);

        }else {

        }

        cursor.close();
        db.close();

        return user;
    }

    /**
     *  A DELETE query to remove message
     * **/
    public void removeMessage(String message) throws SQLException {
        String query = "DELETE FROM TABLE WHERE message='"+ message +"'";
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }


    /**
     *  This method get 2 paramater sender and reciever id ( phone number)
     *
     *  All message store in database like sender -> reciever = message format
     *  to get all message of chat and just one query to stability,
     *  Query;
     *
     *  (senderId='"+senderId + "' AND recieverId='"+ reciverId +"') OR (senderId='"+ reciverId +"' AND recieverId='"+ senderId +"')"
     *
     *  this query format get all data of chat that two people (sender, receiver) message just one query
     *
     *  All message read into to message object just add 2 paramater sender,message . All message return with arraylist
     *
     * **/
    public ArrayList<Message> getAllMessageOfChat(String senderId, String reciverId) throws SQLException, NoSuchAlgorithmException {
        String query="SELECT senderId,message FROM Messages WHERE (senderId='"+senderId + "' AND recieverId='"+ reciverId +"') OR (senderId='"+ reciverId +"' AND recieverId='"+ senderId +"')";

        ArrayList<Message> arrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);



        if(cursor.moveToFirst()){

            do{
                String sender = cursor.getString(0);
                String message = cursor.getString(1);
                Message m = new Message();
                m.setSenderId(sender);
                m.setReciverId(message);
                arrayList.add(m);

            }while (cursor.moveToNext());

        }else {

        }


        return arrayList;


    }


    /**
     *  Geting all user from User tables and return all user with arraylist
     *
     *
     * **/
    public ArrayList<User> getAllUser(){
        ArrayList<User> tmp = new ArrayList<>();
        String query = "SELECT * FROM User";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {


                String photo = cursor.getString(0);
                String nickname = cursor.getString(1);
                String userId = cursor.getString(2);
                String publicKey = cursor.getString(3);
                User user = new User(photo,nickname,userId,publicKey);
                tmp.add(user);

            }while (cursor.moveToNext());
        }
        else {

        }
        return tmp;

    }



}
