package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.MyCharactersContract;
import com.example.apprpg.models.Character;


public class MyCharactersPresenter
        implements MyCharactersContract.MyCharactersPresenter {

    private MyCharactersContract.MyCharactersView view;


    public MyCharactersPresenter(MyCharactersContract.MyCharactersView view) {
        this.view = view;
    }

    @Override
    public void onCharacterClick(Character character) {
        view.onCharacterClick(character);
    }

    @Override
    public void onDestroyView() {
        this.view = null;
    }
}
