package com.example.apprpg.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.apprpg.models.Character;
import com.example.apprpg.presenter.MyCharactersPresenter;
import com.example.apprpg.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.MyViewHolder>{

    private final RequestManager requestManager;
    private MyCharactersPresenter presenter;
    private List<Character> characterList;

    public CharactersAdapter(List<Character> characterList, RequestManager requestManager, MyCharactersPresenter presenter) {
        this.requestManager = requestManager;
        this.characterList = characterList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_character, parent, false);
        return new CharactersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Character character = characterList.get(position);

        requestManager.load(character.getProfilePictureUrl()).thumbnail(0.1f).into(holder.profile_picture);
        holder.character_name.setText(character.getName());
        holder.character_breed.setText(character.getBreed());
        holder.itemView.setOnClickListener(view -> presenter.onCharacterClick(character));

    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile_picture;
        private TextView character_name;
        private TextView character_breed;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_picture = itemView.findViewById(R.id.character_adapter_profile_picture);
            character_name = itemView.findViewById(R.id.character_adapter_name);
            character_breed = itemView.findViewById(R.id.character_adapter_profile_breed);
        }
    }
}
