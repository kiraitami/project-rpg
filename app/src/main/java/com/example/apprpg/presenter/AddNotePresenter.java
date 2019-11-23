package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.AddNoteContract;
import com.example.apprpg.models.Note;
import com.example.apprpg.utils.StringHelper;

public class AddNotePresenter implements AddNoteContract.AddNotePresenter {


    private AddNoteContract.AddNoteView view;


    public AddNotePresenter(AddNoteContract.AddNoteView view) {
        this.view = view;
    }

    @Override
    public void requestNewNote(String name, String description, String characterId, Note note) {
        view.onAddLoading();
        boolean isNewNote = note == null;

        if (name.trim().isEmpty() || StringHelper.formatToPostTitle(name).isEmpty()
                || description.trim().isEmpty() ){
            view.onEmptyFields();
        }
        else {
            if (isNewNote) {
                note = new Note();
            }

            note.setCharacterId(characterId);
            note.setName(StringHelper.formatToPostTitle(name));
            note.setDescription(description);
            note.setFavorite(0);
            note.saveInFirebase();

            if (isNewNote) {
                view.onAddSuccessful();
            }
            else {
                view.onEditSuccessful();
            }
        }
    }

    @Override
    public void onDestroyView() {
        this.view =  null;
    }
}
