package com.example.apprpg.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprpg.models.Xp;
import com.example.apprpg.presenter.ExperiencePresenter;
import com.example.apprpg.R;

import java.util.List;

public class XpAdapter extends RecyclerView.Adapter<XpAdapter.MyViewHolder> {

    private List<Xp> xpList;
    private ExperiencePresenter presenter;

    public XpAdapter(List<Xp> xpList, ExperiencePresenter presenter) {
        this.xpList = xpList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_xp, parent, false);
        return new XpAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Xp xp = xpList.get(position);

        holder.amount.setText(String.valueOf(xp.getXpAmount()));
        holder.date.setText(xp.getDate().formatMyDateToString("HH:mm\ndd/MM/yyy"));
        holder.itemView.setOnClickListener(view -> presenter.onXpClick(position));
    }

    @Override
    public int getItemCount() {
        return xpList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView amount, date;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.adapter_xp_amount);
            date = itemView.findViewById(R.id.adapter_xp_date);

        }
    }
}