package com.example.ceng201_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Correction extends AppCompatActivity {


    private TextView textView ;
    private EditText edtText ;
    private Button btnOkay ;
    private String typedCode,sentCode="111";
    Intent intent  ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction);

        textView = findViewById(R.id.txtCodeInfo) ;
        edtText = findViewById(R.id.edtCode);
        btnOkay = findViewById(R.id.btnOkay) ;


        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                typedCode = edtText.getText().toString();
                edtText.setText(null);

                intent = getIntent() ;
                String phoneNumber = "+90"+intent.getStringExtra("phoneNumber") ;

                /**
                 * whether or not  the sending code is correct  ==> VerifyPhone number
                 * --> codeCome := Correct code
                 * */


                if (sentCode.equals(typedCode)) {
                    Toast.makeText(Correction.this, "You are log in ", Toast.LENGTH_LONG).show();
                    // log in succesfully add


                    MainActivity.database.addPhooneNumber(phoneNumber);


                     startActivity( new Intent(Correction.this,Profile.class));
                     finish();

                    /**
                     *

                     *    if the user want to upload now to click UPLOAD PHOTO (go to gallery  )
                     *    if not user press LOG OUT button will be seen friend activity
                     *
                     * */


                } else {

                    Toast.makeText(Correction.this, "Wrong code,code is sent ! ", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}