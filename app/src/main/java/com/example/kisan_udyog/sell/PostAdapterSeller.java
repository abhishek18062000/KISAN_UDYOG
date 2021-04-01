package com.example.kisan_udyog.sell;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kisan_udyog.R;
import com.example.kisan_udyog.models.PostModel;

import java.util.List;

public class PostAdapterSeller extends RecyclerView.Adapter<PostAdapterSeller.MyHolder> {

    Context context;
    List<PostModel> postModelList;

    public PostAdapterSeller(Context context, List<PostModel> postModelList) {
        this.context = context;
        this.postModelList = postModelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_post , parent , false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String title = postModelList.get(position).getpTitle();
        String description = postModelList.get(position).getpDescription();
        String image = postModelList.get(position).getpImage();
        String price=postModelList.get(position).getpPrice();
        String quantity =postModelList.get(position).getpQuantity();
        String username=postModelList.get(position).getpUsername();
        String profilePhoto=postModelList.get(position).getProfile_pic();

        holder.postTitle.setText(title);
        holder.postDescription.setText(description);
        holder.postQuantity.setText(quantity+" kgs.");
        holder.postPrice.setText(price+ " Rs.");
        holder.postUsername.setText(username);

        Glide.with(context).load(image).into(holder.postImage);
        Glide.with(context).load(profilePhoto).into(holder.profilePic);
        //now we will add library to load image
    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView postImage,profilePic;
        TextView postTitle , postDescription,postPrice,postQuantity,postUsername;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.postImage);
            postTitle = itemView.findViewById(R.id.postTitle);
            postDescription = itemView.findViewById(R.id.postDescription);
            postPrice=itemView.findViewById(R.id.postPrice);
            postQuantity=itemView.findViewById(R.id.postQuantity);
            postUsername=itemView.findViewById(R.id.username);
            profilePic=itemView.findViewById(R.id.profilePic);

        }
    }
}
