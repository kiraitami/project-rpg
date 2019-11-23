package com.example.apprpg.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.apprpg.interfaces.NotesContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Note;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.NotesPresenter;
import com.example.apprpg.ui.activities.AddNoteActivity;
import com.example.apprpg.ui.activities.NoteDetailsActivity;
import com.example.apprpg.R;
import com.example.apprpg.ui.adapters.NoteAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment
        implements NotesContract.NoteView {


    private FloatingActionButton fab_add_note;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View view;
    private Toolbar toolbar;

    private User user;
    private Character character;

    private NotesPresenter presenter;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notes, container, false);

        presenter = new NotesPresenter(this);

        getIntentData();
        setViewsById();

        toolbar.setTitle(getResources().getString(R.string.notes));

        clickEventActions();
        setHasOptionsMenu(true);
        onRecyclerScroll();

        presenter.loadFromFirebase(character.getId(), this);

        return view;
    }

    @Override
    public void onDetach() {
        recyclerView.setAdapter(null);
        recyclerView = null;
        presenter.removeEventListener();
        presenter.onDestroyView();
        presenter = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.order_by_favorite:
                if (item.isChecked()) {
                    presenter.showAll();
                }
                else {
                    presenter.orderByFavorite();
                }
                item.setChecked(!item.isChecked());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getIntentData() {
        Bundle data = getArguments();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
    }

    @Override
    public void setViewsById() {
        fab_add_note = getActivity().findViewById(R.id.fab_profile_main);
        toolbar = getActivity().findViewById(R.id.toolbar_main_activity);
        recyclerView = view.findViewById(R.id.recycler_notes);
        progressBar = view.findViewById(R.id.progress_notes);
    }

    @Override
    public void clickEventActions() {
        fab_add_note.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddNoteActivity.class);
            intent.putExtra(getResources().getString(R.string.user_object), user);
            intent.putExtra(getResources().getString(R.string.character_object), character);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    public void showNote(List<Note> noteList) {
        if (isAdded()) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new NoteAdapter(noteList, presenter));
        }
    }

    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
        intent.putExtra(getResources().getString(R.string.note_object), note);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onRecyclerScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE){
                    fab_add_note.hide();
                }
                else {
                    fab_add_note.show();
                }
            }
        });
    }

    @Override
    public void onLoadingFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFromFirebaseSuccess() {
        progressBar.setVisibility(View.GONE);
    }
}
