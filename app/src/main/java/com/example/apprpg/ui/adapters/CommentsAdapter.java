package com.example.apprpg.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.apprpg.interfaces.PostDetailsContract;
import com.example.apprpg.models.Comment;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{

    private final RequestManager requestManager;
    private final PostDetailsContract.PostDetailsView postDetailsView;
    private List<Comment> commentList;
    private String visitorName;

    public CommentsAdapter(RequestManager requestManager, PostDetailsContract.PostDetailsView postDetailsView, List<Comment> commentList, String visitorName) {
        this.requestManager = requestManager;
        this.postDetailsView = postDetailsView;
        this.commentList = commentList;
        this.visitorName = visitorName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comments, parent, false);
        return new CommentsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comment comment = commentList.get(position);

        requestManager.load(comment.getCharacterPictureUrl()).thumbnail(0.1f).into(holder.profile_picture);

        holder.likes_count.setText(String.valueOf(comment.getLikersList().size()));
        holder.character_name.setText(comment.getCharacterName());
        holder.player_name.setText(comment.getUserName());
        holder.comment.setText(comment.getBody());
        holder.date.setText(comment.getPublicationDate().formatMyDateToString("HH:mm\ndd/MM/yyy"));


        holder.btn_like.setLiked( comment.getLikersList().contains(visitorName) );

        holder.delete_comment.setVisibility( visitorName.equals(comment.getCharacterName()) ? View.VISIBLE : View.GONE );

        holder.delete_comment.setOnLongClickListener(view -> {
            comment.deleteFromFirebase();
            notifyItemRemoved(position);
            return false;
        });

        holder.profile_picture.setOnClickListener(view -> postDetailsView.visitDialog(comment.getCharacterId(), comment.getCharacterName()));

        holder.itemView.setOnLongClickListener(view -> {
            postDetailsView.showLikersDialog(comment.getLikersList());
            return false;
        });

        holder.btn_like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                comment.like(visitorName);
                comment.updateLikersInFirebase(comment.getLikersList());
                holder.likes_count.setText(String.valueOf(comment.getLikersList().size()));
                notifyDataSetChanged();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                comment.unlike(visitorName);
                comment.updateLikersInFirebase(comment.getLikersList());
                holder.likes_count.setText(String.valueOf(comment.getLikersList().size()));
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile_picture;
        private ImageButton delete_comment;
        private LikeButton btn_like;
        private TextView character_name;
        private TextView player_name;
        private TextView comment;
        private TextView likes_count;
        private TextView date;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_picture = itemView.findViewById(R.id.profile_picture_comment);
            character_name = itemView.findViewById(R.id.character_name_comment);
            player_name = itemView.findViewById(R.id.player_name_comment);
            comment = itemView.findViewById(R.id.comment);
            likes_count = itemView.findViewById(R.id.tv_likes_count_comment);
            btn_like = itemView.findViewById(R.id.button_like_comment);
            delete_comment = itemView.findViewById(R.id.delete_comment);
            date = itemView.findViewById(R.id.date_comment);
        }
    }
}
