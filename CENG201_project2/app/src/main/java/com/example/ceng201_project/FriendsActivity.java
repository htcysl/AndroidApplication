package com.example.ceng201_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.sql.SQLException;
import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {


    private RecyclerView recyclerView ;
    private ArrayList<User> users ;
    private ProgressBar progressBar ;
    private UserAdapter userAdapter ;
    UserAdapter.OnUserClickListener onUserClickListener ;   /**  OnUserClickListener is interface in UserAdapter  class  :) */

    private SwipeRefreshLayout swipeRefreshLayout ;

    String myImageUrl ;

    Intent  x ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        progressBar = findViewById(R.id.progressBar) ;
        users = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler) ;
        swipeRefreshLayout = findViewById(R.id.swipeLayout) ;


        myImageUrl = MainActivity.client.clientPhoto ;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getUsers();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });


        onUserClickListener = new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(int position) {
                startActivity(new Intent(FriendsActivity.this,MessageActivity.class)
                   .putExtra("username_of_roommate",users.get(position).getUsername())
                   .putExtra("userId_of_roommate",users.get(position).getUserID())      // userId := phone number
                   .putExtra("img_of_roommate",users.get(position).getProfilePicture())
                   .putExtra("publicKey_of_roommate",users.get(position).getPublicKey())
                   .putExtra("my_img",myImageUrl)  // own image
                );

            }
        };

        try {
            getUsers();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);

        return  true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       if(item.getItemId() == R.id.menu_item_profile) {
           startActivity(new Intent(FriendsActivity.this,Profile.class));
       }

        return super.onOptionsItemSelected(item);
    }

    private void getUsers() throws SQLException {

        users.clear();


        users.add(new User("","Ayse","1234567890","")) ;
        users.add(new User("","Can","1234567891","")) ;
        users.add(new User("","Ali","1234567892","")) ;
        // Get Users from database
       // users = MainActivity.database.getAllUser() ;

        userAdapter = new UserAdapter(users,FriendsActivity.this,onUserClickListener) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
        recyclerView.setAdapter(userAdapter);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);







    }



}