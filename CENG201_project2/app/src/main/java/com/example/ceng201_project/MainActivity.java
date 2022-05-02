package com.example.ceng201_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private EditText edtPhoneNumber ;
    private Button btnSignUp;
    public static ClientDatabase database ; // database static
    public static  Client client ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtPhoneNumber = findViewById(R.id.edtPhoneNum);
        btnSignUp = findViewById(R.id.btnSubmit);


        client = new Client("127.0.0.1", 5556); // Client
        try {
            // Start Client functinolaty
            client.startClient() ;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        database = client.createDatabaseOnesTime(MainActivity.this);  // database


        /**
         * User is already in database
         * */


        if (database.existsRootuser()) {
            startActivity(new Intent(MainActivity.this, FriendsActivity.class));
            finish();
        } else {
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String phoneNumber = edtPhoneNumber.getText().toString();
                    edtPhoneNumber.setText(null);
                    if (phoneNumber.length() == 10) {
                        startActivity(new Intent(MainActivity.this, Correction.class)
                                .putExtra("phoneNumber", phoneNumber));
                    } else {
                        Toast.makeText(MainActivity.this, "The phone number must be 10 digits.\n PLEASE ENTER AGAIN ... ", Toast.LENGTH_LONG).show();

                    }
                }
            });


        }
    }}