package com.example.apprpg.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprpg.models.Skill;
import com.example.apprpg.presenter.SkillsPresenter;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.R;

import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.MyViewHolder> {

    private SkillsPresenter presenter;
    private List<Skill> skillList;

    public SkillAdapter(List<Skill> skillList, SkillsPresenter presenter) {
        this.skillList = skillList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_skill, parent, false);
        return new SkillAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Skill skill = skillList.get(position);

        holder.name.setText(skill.getName());
        holder.description.setText(skill.getDescription());

        if (skill.getDamage() == null || skill.getDamage().trim().isEmpty())
            holder.damage_layout.setVisibility(View.GONE);
        else
            holder.damage.setText(skill.getDamage());


        if (skill.getCost() == null || skill.getCost().trim().isEmpty())
            holder.cost_layout.setVisibility(View.GONE);
        else
            holder.cost.setText(skill.getCost());


        holder.favorite.setVisibility( skill.getFavorite() > 0 ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(view -> presenter.onItemClick(skill));

    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, description, damage, cost;
        LinearLayout damage_layout, cost_layout;
        ImageView favorite;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.skill_adapter_name);
            description = itemView.findViewById(R.id.skill_adapter_desc);
            damage = itemView.findViewById(R.id.skill_adapter_damage);
            cost = itemView.findViewById(R.id.skill_adapter_cost);
            favorite = itemView.findViewById(R.id.skill_adapter_favorite);
            damage_layout = itemView.findViewById(R.id.adapter_skill_layout_damage);
            cost_layout = itemView.findViewById(R.id.adapter_skill_layout_cost);

        }
    }
}