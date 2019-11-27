package com.example.apprpg.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.apprpg.models.Post;
import com.example.apprpg.presenter.TavernPresenter;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TavernAdapter extends RecyclerView.Adapter<TavernAdapter.MyViewHolder> {

    private final RequestManager requestManager;
    private TavernPresenter tavernPresenter;
    private List<Post> postList;
    private Context context;
    private String visitorName;

    public TavernAdapter(List<Post> postList, Context context, TavernPresenter tavernPresenter, RequestManager requestManager, String visitorName) {
        this.requestManager = requestManager;
        this.postList = postList;
        this.context = context;
        this.tavernPresenter = tavernPresenter;
        this.visitorName = visitorName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tavern, parent, false);
        return new TavernAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = postList.get(position);

        holder.post_title.setText(post.getTitle());

        holder.post_desc.setText(post.getDescription());

        holder.character_name.setText(post.getCharacterName());
        holder.player_name.setText(post.getUserName());
        holder.likes_count.setText(String.valueOf(post.getLikersList().size()));

        requestManager.load(post.getImageUrl())
                .override(720, 450) //todo: test better performing
                .thumbnail(0.1f)
                .into(holder.post_image);

        requestManager.load(post.getCharacterPictureUrl())
                .thumbnail(0.1f)
                .into(holder.character_picture);


        holder.btn_like.setLiked( post.getLikersList().contains(visitorName) );

        holder.btn_like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                post.like(visitorName);
                post.updateLikersInFirebase(post.getLikersList());
                holder.likes_count.setText(String.valueOf(post.getLikersList().size()));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                post.unlike(visitorName);
                post.updateLikersInFirebase(post.getLikersList());
                holder.likes_count.setText(String.valueOf(post.getLikersList().size()));
            }
        });

        holder.itemView.setOnClickListener(view -> tavernPresenter.onPostClick(post));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView post_image;
        CircleImageView character_picture;
        TextView post_title, post_desc, character_name, player_name, likes_count;
        LikeButton btn_like;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.tavern_adapter_post_image);
            character_picture = itemView.findViewById(R.id.tavern_adapter_profile_picture);
            post_title = itemView.findViewById(R.id.tavern_adapter_post_title);
            post_desc = itemView.findViewById(R.id.tavern_adapter_post_desc);
            character_name = itemView.findViewById(R.id.tavern_adapter_character_name);
            player_name = itemView.findViewById(R.id.tavern_adapter_player_name);
            btn_like = itemView.findViewById(R.id.tavern_adapter_button_like);
            likes_count = itemView.findViewById(R.id.tavern_adapter_tv_likes_count);
        }
    }
}
