package com.example.apprpg.presenter;


import androidx.annotation.NonNull;


import com.example.apprpg.interfaces.NotesContract;
import com.example.apprpg.models.Note;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.apprpg.utils.StringNodes.NODE_NOTES;

public class NotesPresenter
        implements NotesContract.NotePresenter {


    private NotesContract.NoteView view;
    private ValueEventListener valueEventListener;
    private List<Note> allNotesInFirebase = new ArrayList<>();

    public NotesPresenter(NotesContract.NoteView view) {
        this.view = view;
    }

    @Override
    public void loadFromFirebase(String characterId, NotesContract.NoteView view) {
        view.onLoadingFromFirebase();
        valueEventListener = databaseReference
                .child(NODE_NOTES)
                .child(characterId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allNotesInFirebase.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            allNotesInFirebase.add( data.getValue(Note.class) );
                        }

                        view.onLoadingFromFirebaseSuccess();
                        Collections.reverse(allNotesInFirebase);
                        view.showNote(allNotesInFirebase);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void showAll() {
        view.showNote(allNotesInFirebase);
    }

    @Override
    public void orderByFavorite() {
        List<Note> sortedNoteList = new ArrayList<>(allNotesInFirebase);
        Collections.sort(sortedNoteList);
        view.showNote(sortedNoteList);
    }

    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onNoteClick(Note note) {
        view.onNoteClick(note);
    }

    @Override
    public void removeEventListener() {
        databaseReference.removeEventListener(valueEventListener);
    }

}
