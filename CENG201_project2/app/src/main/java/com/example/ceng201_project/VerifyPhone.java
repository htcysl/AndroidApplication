package com.example.ceng201_project;




/*

    The twillo library that we will use for phone verification can only
    work by using an external service. It was possible to do this,
    but we had to postpone it because we did not have enough time.
    The function that sends the message is ready, the only thing to do
    is to write it to the server and create a request from the client.





import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;


public class VerifyPhone {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";




    public static void sendVerificationCode(String number , String msg){
        String m = "Your verification number is :"+msg +" dont share anyone!";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(number),
                "MGd883765ea36e2383c96fc86f48078620",
                m).create();


    }



}
*/