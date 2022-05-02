package com.example.ceng201_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.userHolder> {


    private ArrayList<User> users ;
    private Context context ;
    private OnUserClickListener onUserClickListener ;  // interface

    public UserAdapter(ArrayList<User> users,Context context,OnUserClickListener onUserClickListener) {
       this.users = users ;
       this.context  = context ;
       this.onUserClickListener = onUserClickListener ;
    }


    interface OnUserClickListener{
        void onUserClicked(int position ) ;  // position in side user arrayList
    }


    /**  return new user with view */
    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_holder,parent,false);
        return new userHolder(view) ;
    }



    @Override
    public void onBindViewHolder(@NonNull userHolder holder, int position) {
         holder.txtUsername.setText(users.get(position).getUsername());

         /**
          * Glide := inflate image view with profile picture, we need to use
          *
          *  error means if any error occur use R.drawable.account_img
          * */

        Glide.with(context).load(users.get(position).getProfilePicture()).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.imageView) ;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    /**  inner class which will be for view holder   */

    class userHolder extends RecyclerView.ViewHolder{
        TextView txtUsername ;
        ImageView imageView ;

        public userHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListener.onUserClicked(getAdapterPosition());
                }
            });
            txtUsername = itemView.findViewById(R.id.txtUsername)  ;
            imageView  = itemView.findViewById(R.id.img_pro) ;

        }
    }


}
