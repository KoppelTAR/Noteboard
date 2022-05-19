package com.example.noteboard.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteboard.R;
import com.example.noteboard.models.Post;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {
    ArrayList<Post> postArrayList;

    private onItemClickListener listener;

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
        holder.author.setText(post.getPostAuthor());
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public void updatePostList(final ArrayList<Post> postArrayList){
        this.postArrayList = postArrayList;
        notifyDataSetChanged();
    }

    class MainViewHolder extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView author;
        private final TextView content;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtPostTitle);
            content = itemView.findViewById(R.id.txtPostContent);
            author = itemView.findViewById(R.id.txtPostAuthor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClickListener(postArrayList.get(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClickListener(Post post);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}
