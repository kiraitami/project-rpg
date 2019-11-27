package com.example.apprpg.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprpg.models.Maho;
import com.example.apprpg.presenter.MahoPresenter;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.R;

import java.util.List;

public class MahoAdapter extends RecyclerView.Adapter<MahoAdapter.MyViewHolder> {

    private MahoPresenter presenter;
    private List<Maho> mahoList;

    public MahoAdapter(List<Maho> mahoList, MahoPresenter presenter) {
        this.mahoList = mahoList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_maho, parent, false);
        return new MahoAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Maho maho = mahoList.get(position);

        holder.name.setText(maho.getName());
        holder.description.setText(maho.getDescription());
        holder.cost.setText(maho.getCost());

        if (maho.getDamage() == null || maho.getDamage().trim().isEmpty())
            holder.difficulty_layout.setVisibility(View.GONE);
        else
            holder.difficulty.setText(maho.getDifficulty());

        if (maho.getDamage() == null || maho.getDamage().trim().isEmpty())
            holder.damage_layout.setVisibility(View.GONE);
        else
            holder.damage.setText(maho.getDamage());


        holder.favorite.setVisibility( maho.getFavorite() > 0 ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return mahoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, description, damage, cost, difficulty;
        LinearLayout damage_layout, difficulty_layout;
        ImageView favorite;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.maho_adapter_name);
            description = itemView.findViewById(R.id.maho_adapter_desc);
            damage = itemView.findViewById(R.id.maho_adapter_damage);
            cost = itemView.findViewById(R.id.maho_adapter_cost);
            difficulty = itemView.findViewById(R.id.maho_adapter_difficulty);
            favorite = itemView.findViewById(R.id.maho_adapter_favorite);
            damage_layout = itemView.findViewById(R.id.adapter_maho_layout_damage);
            difficulty_layout = itemView.findViewById(R.id.adapter_maho_layout_difficulty);

        }
    }
}