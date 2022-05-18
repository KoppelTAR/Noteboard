package com.example.noteboard.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteboard.R;
import com.example.noteboard.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {
    ArrayList<Post> postArrayList;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    public MainRecyclerAdapter() {
        this.postArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main,parent,false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Post post = postArrayList.get(position);
        holder.title.setText(post.getTitle());
        holder.content.setText(post.getContent());
        Log.i("TAG", "inside onbind ");
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public void updatePostList(final ArrayList<Post> postArrayList){
        this.postArrayList = postArrayList;
        Log.i("TAG", "inside update ");
        notifyDataSetChanged();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView content;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtPostTitle);
            content = itemView.findViewById(R.id.txtPostContent);
        }
    }
}
