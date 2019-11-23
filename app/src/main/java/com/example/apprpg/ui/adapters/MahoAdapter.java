package com.example.apprpg.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        StringHelper.formatToDescription(maho.getDescription(), holder.description);
        holder.damage.setText(maho.getDamage());
        holder.cost.setText(maho.getCost());
        holder.difficulty.setText(maho.getDifficulty());

        holder.itemView.setOnClickListener(view -> presenter.onItemClick(maho));

        holder.favorite.setVisibility( maho.getFavorite() > 0 ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return mahoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, description, damage, cost, difficulty;
        ImageView favorite;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.maho_adapter_name);
            description = itemView.findViewById(R.id.maho_adapter_desc);
            damage = itemView.findViewById(R.id.maho_adapter_damage);
            cost = itemView.findViewById(R.id.maho_adapter_cost);
            difficulty = itemView.findViewById(R.id.maho_adapter_difficulty);
            favorite = itemView.findViewById(R.id.maho_adapter_favorite);

        }
    }
}