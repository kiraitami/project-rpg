package com.example.apprpg.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprpg.models.Weapon;
import com.example.apprpg.presenter.WeaponsPresenter;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.R;

import java.util.List;

public class WeaponAdapter extends RecyclerView.Adapter<WeaponAdapter.MyViewHolder> {
    
    private WeaponsPresenter presenter;
    private List<Weapon> weaponList;

    public WeaponAdapter(List<Weapon> weaponList, WeaponsPresenter presenter) {
        this.weaponList = weaponList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_weapon, parent, false);
        return new WeaponAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Weapon weapon = weaponList.get(position);

        holder.name.setText(weapon.getName());
        holder.description.setText(weapon.getDescription());


        if (weapon.getDamage() == null || weapon.getDamage().trim().isEmpty())
            holder.damage_layout.setVisibility(View.GONE);
        else
            holder.damage.setText(weapon.getDamage());

        holder.itemView.setOnClickListener(view -> presenter.onWeaponClick(weapon));

        holder.favorite.setVisibility( weapon.getFavorite() > 0 ? View.VISIBLE : View.GONE);


    }

    @Override
    public int getItemCount() {
        return weaponList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, description, damage;
        LinearLayout damage_layout;
        ImageView favorite;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.weapon_adapter_name);
            description = itemView.findViewById(R.id.weapon_adapter_desc);
            damage = itemView.findViewById(R.id.weapon_adapter_damage);
            favorite = itemView.findViewById(R.id.weapon_adapter_favorite);
            damage_layout = itemView.findViewById(R.id.adapter_weapon_layout_damage);
        }
    }
}