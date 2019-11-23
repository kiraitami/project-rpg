package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BasePresenterContract;
import com.example.apprpg.interfaces.base.BaseViewContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentViewContract;
import com.example.apprpg.models.Character;

import java.util.List;

public interface MyCharactersContract {

    interface MyCharactersView extends BaseViewContract, RecyclerFragmentViewContract {
        void loadFromFirebase();
        void showCharacters(List<Character> characterList);
        void onCharacterClick(Character character);
    }

    interface MyCharactersPresenter extends BasePresenterContract {
        void onCharacterClick(Character character);
    }
}

