package com.example.noteboard.adapters;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteboard.R;
import com.example.noteboard.models.Post;
import com.example.noteboard.viewmodels.MainViewModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {

    private onItemClickListener listener;

    ArrayList<Post> postArrayList;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public MainRecyclerAdapter() {
        this.postArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MainRecyclerAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main,parent,false);
        return new MainRecyclerAdapter.MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerAdapter.MainViewHolder holder, int position) {
        Post post = postArrayList.get(position);
        holder.title.setText(post.getTitle());
        holder.content.setText(post.getContent());
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
        private final TextView content;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtPostTitle);
            content = itemView.findViewById(R.id.txtPostContent);

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

    // TODO if you are smart enough please make this work through the PostsRepository
    public void loadPostsData(){
        db.collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                postArrayList.add(dc.getDocument().toObject(Post.class));
                                Log.i("TAG", dc.getDocument().getId());
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
    }


    public interface onItemClickListener {
        void onItemClickListener(Post post);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}
