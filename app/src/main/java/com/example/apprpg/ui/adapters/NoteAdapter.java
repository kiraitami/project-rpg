package com.example.apprpg.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprpg.models.Note;
import com.example.apprpg.presenter.NotesPresenter;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    private NotesPresenter presenter;
    private List<Note> noteList;

    public NoteAdapter(List<Note> noteList, NotesPresenter presenter) {
        this.noteList = noteList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notes, parent, false);
        return new NoteAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Note note = noteList.get(position);

        holder.name.setText(note.getName());
        StringHelper.formatToDescription(note.getDescription(), holder.description);

        holder.itemView.setOnClickListener(view -> presenter.onNoteClick(note));

        holder.favorite.setVisibility( note.getFavorite() > 0 ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;
        ImageView favorite;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.note_adapter_name);
            description = itemView.findViewById(R.id.note_adapter_desc);
            favorite = itemView.findViewById(R.id.note_adapter_favorite);

        }
    }
}