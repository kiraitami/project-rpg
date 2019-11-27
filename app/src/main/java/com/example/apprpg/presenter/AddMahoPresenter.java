package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.AddMahoContract;
import com.example.apprpg.models.Maho;

import static com.example.apprpg.utils.StringHelper.formatToName;
import static com.example.apprpg.utils.StringHelper.removeEmojis;

public class AddMahoPresenter implements AddMahoContract.AddMahoPresenter {


    private AddMahoContract.AddMahoView view;


    public AddMahoPresenter(AddMahoContract.AddMahoView view) {
        this.view = view;
    }

    @Override
    public void requestNewMaho(String name, String description, String damage, String difficulty, String cost, String characterId, Maho maho) {
        view.onAddLoading();
        boolean isNewMaho = maho == null;

        if (name.trim().isEmpty() || removeEmojis(name).isEmpty()
                ||description.trim().isEmpty()
                || cost.trim().isEmpty() || removeEmojis(cost.trim()).isEmpty()
        ){
            view.onEmptyFields();
        }
        else {
            if (isNewMaho) {
                maho = new Maho();
            }

            maho.setCharacterId(characterId);
            maho.setName(removeEmojis(name));
            maho.setDescription(removeEmojis(description));
            maho.setDamage(removeEmojis(damage).trim().isEmpty() ? removeEmojis(damage).trim() : null);
            maho.setDifficulty(removeEmojis(difficulty).trim().isEmpty() ? removeEmojis(difficulty).trim() : null);
            maho.setCost(removeEmojis(cost).trim());
            maho.setFavorite(0);
            maho.saveInFirebase();

            if (isNewMaho) {
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
