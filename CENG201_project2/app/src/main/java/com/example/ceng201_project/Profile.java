package com.example.ceng201_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Profile extends AppCompatActivity {


    private Button btnLogOut , btnUpload ;
    private ImageView imgProfile ;

    private Uri imagePath ;  /** (INFO) PHOTO DATA TYPE IS URI */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnLogOut  = findViewById(R.id.btnLogOut);
        btnUpload  = findViewById(R.id.btnUpLoadImage);
        imgProfile = findViewById(R.id.profile_img) ;




        /**
         * Set on click listeners
         * */

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    uploadImage() ;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile.this,FriendsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });



        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * Below code tells system open galley
                 * */

                Intent photoIntent = new Intent(Intent.ACTION_PICK) ;
                photoIntent.setType("image/*") ;
                startActivityForResult(photoIntent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data!=null) { // data is image
            imagePath = data.getData();  // data type is Uri
            getImageInImageView() ;


        }
    }

    private void getImageInImageView() {
        Bitmap bitmap = null ;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgProfile.setImageBitmap(bitmap);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadImage() throws NoSuchAlgorithmException, SQLException, IOException {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        boolean flag = false ; // normally false
        Encryption E = new Encryption();


        // Adding image to database
        try {
            MainActivity.database.addRootUserPhoto(imgProfile.toString(),null);  // you can Add nick name also
            flag = true;
        }catch (Exception e){

        }

        // Sent Root user data to server for record
        MainActivity.client.reqisterRootUserToServer();



        if(flag){
            Toast.makeText(Profile.this,"Photo uploaded !",Toast.LENGTH_SHORT).show() ;
        }
        else
        {
            Toast.makeText(Profile.this,"Photo is not uploaded !",Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();

    }



}