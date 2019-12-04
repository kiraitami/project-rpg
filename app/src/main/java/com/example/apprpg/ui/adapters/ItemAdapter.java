package com.example.apprpg.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprpg.models.InventoryItem;
import com.example.apprpg.presenter.ItemsPresenter;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.R;


import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private ItemsPresenter presenter;
    private List<InventoryItem> itemList;

    public ItemAdapter(List<InventoryItem> itemList, ItemsPresenter presenter) {
        this.itemList = itemList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_items, parent, false);
        return new ItemAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        InventoryItem item = itemList.get(position);

        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());

        holder.itemView.setOnClickListener(view -> presenter.onItemClick(item));

        holder.favorite.setVisibility( item.getFavorite() > 0 ? View.VISIBLE : View.GONE );

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;
        ImageView favorite;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_adapter_name);
            description = itemView.findViewById(R.id.item_adapter_desc);
            favorite = itemView.findViewById(R.id.item_adapter_favorite);

        }
    }
}