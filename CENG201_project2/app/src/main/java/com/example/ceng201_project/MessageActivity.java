package com.example.ceng201_project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {


    private RecyclerView recyclerView ;
    private EditText edtMessageInput ;
    private TextView txtChattingWith ;
    private ProgressBar progressBar ;
    private ImageView imgToolbar ,imgSend ;

    private ArrayList<Message> messages ;
    MessageAdapter messageAdapter ;


    String usernameOfTheRoommate , chattingWithUser; // phone number


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        usernameOfTheRoommate = getIntent().getStringExtra("username_of_roommate") ;
        chattingWithUser = getIntent().getStringExtra("userId_of_roommate") ;

        recyclerView = findViewById(R.id.recyclerMessages) ;
        imgSend = findViewById(R.id.imgSendMessage) ;
        edtMessageInput = findViewById(R.id.edtText) ;
        txtChattingWith = findViewById(R.id.txtChattingWith) ;
        progressBar = findViewById(R.id.progressMessages) ;
        imgToolbar = findViewById(R.id.img_toolbar)  ;


        txtChattingWith.setText(usernameOfTheRoommate);
        messages = new ArrayList<>() ;





        imgSend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               try {
                   MainActivity.database.addMessage(MainActivity.client.clientId,getIntent().getStringExtra("userId_of_roommate"),edtMessageInput.getText().toString());
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               }


               try {
                   appendMessageListener();
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               } catch (NoSuchAlgorithmException e) {
                   e.printStackTrace();
               }


               edtMessageInput.setText("");
           }
       });
        messageAdapter = new MessageAdapter(messages,getIntent().getStringExtra("my_img"),getIntent().getStringExtra("img_of_roommate"),MessageActivity.this) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);


    }



    private void appendMessageListener() throws SQLException, NoSuchAlgorithmException {
           messages.clear();
           messages = MainActivity.database.getAllMessageOfChat(usernameOfTheRoommate,chattingWithUser);
           messageAdapter.notifyDataSetChanged();
           recyclerView.scrollToPosition(messages.size()-1);    // keep messages on the bottom
           recyclerView.setVisibility(View.VISIBLE);
           progressBar.setVisibility(View.GONE);

    }

}