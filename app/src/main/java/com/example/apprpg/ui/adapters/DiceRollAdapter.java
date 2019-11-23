package com.example.apprpg.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprpg.models.Dice;
import com.example.apprpg.R;

import java.util.List;

public class DiceRollAdapter extends RecyclerView.Adapter<DiceRollAdapter.MyViewHolder> {

    private List<Dice> diceList;
    private Context context;

    public DiceRollAdapter(List<Dice> diceList, Context context) {
        this.diceList = diceList;
        this.context = context;
    }

    @NonNull
    @Override
    public DiceRollAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_roll_history, parent, false);
        return new DiceRollAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiceRollAdapter.MyViewHolder holder, int position) {

        Dice dice = diceList.get(position);

        StringBuilder ndices = new StringBuilder();
        ndices.append(dice.getDiceAmount())
                .append("d")
                .append(dice.getNumberOfFaces())
                .append(": ");

        String details = dice.getRollDetails() + (dice.getRollModifier()!= 0 ? context.getResources().getString(R.string.roll_details_modifier, dice.getRollModifier()) :"");

        holder.dices.setText(ndices.toString());
        holder.result.setText(String.valueOf(dice.getRollResult()));
        holder.details.setText(details.replaceAll("\n",""));
        holder.date.setText(dice.getDate().formatMyDateToString("HH:mm\ndd/MM/yyy"));
    }

    @Override
    public int getItemCount() {
        return diceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView result, dices, details, date;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            result = itemView.findViewById(R.id.adapter_roll_result);
            dices = itemView.findViewById(R.id.adapter_roll_dices);
            details = itemView.findViewById(R.id.adapter_roll_result_details);
            date = itemView.findViewById(R.id.adapter_roll_date);

        }
    }
}