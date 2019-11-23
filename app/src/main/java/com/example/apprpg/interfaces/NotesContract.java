package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BasePresenterContract;
import com.example.apprpg.interfaces.base.BaseViewContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentPresenterContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentViewContract;
import com.example.apprpg.models.Note;

import java.util.List;

public interface NotesContract {
    interface NoteView extends BaseViewContract, RecyclerFragmentViewContract {
        void showNote(List<Note> noteList);
        void onNoteClick(Note note);
    }
    interface NotePresenter extends BasePresenterContract, RecyclerFragmentPresenterContract {
        void onNoteClick(Note note);
        void loadFromFirebase(String characterId, NotesContract.NoteView view);
    }
}
