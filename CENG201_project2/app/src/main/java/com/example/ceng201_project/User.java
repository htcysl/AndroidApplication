package com.example.ceng201_project;

public class User {

    private String profilePicture;   /** profilePicture */
    private String username;  /** alter nickname to username */
    private String userID;   /** phone number */
    private String publicKey;

    public User() {

    }

    public User(String photo, String nickname, String userID, String publicKey){
        this.profilePicture = photo;
        this.username = nickname;
        this.userID = userID;
        this.publicKey = publicKey;
    }


    public String getProfilePicture(){return this.profilePicture;}
    public String getUsername(){return  this.username;}
    public String getUserID(){return this.userID;}
    public String getPublicKey(){return  this.publicKey;}


}


