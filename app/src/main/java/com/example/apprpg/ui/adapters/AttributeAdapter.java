package com.example.apprpg.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprpg.models.CharacterAttribute;
import com.example.apprpg.presenter.AttributesPresenter;
import com.example.apprpg.R;

import java.util.List;

public class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.MyViewHolder> {

    private AttributesPresenter presenter;
    private List<CharacterAttribute> attributeList;

    public AttributeAdapter(List<CharacterAttribute> attributeList, AttributesPresenter presenter) {
        this.attributeList = attributeList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attributes, parent, false);
        return new AttributeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(attributeList.get(position).getName());
        holder.value.setText(String.valueOf(attributeList.get(position).getValue()));

        holder.itemView.setOnClickListener(view -> {
            presenter.onAttributeClick(position);
        });

    }

    @Override
    public int getItemCount() {
        return attributeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, value;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.adapter_attribute_name);
            value = itemView.findViewById(R.id.adapter_attribute_value);

        }
    }
}