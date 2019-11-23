package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BaseAddInventoryObjectContract;
import com.example.apprpg.models.Note;

public interface AddNoteContract {
    interface AddNoteView extends BaseAddInventoryObjectContract.View {

    }
    interface AddNotePresenter extends BaseAddInventoryObjectContract.Presenter{
        void requestNewNote(String title, String description, String characterId, Note note);
    }
}
