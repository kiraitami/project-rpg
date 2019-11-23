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

        if (name.trim().isEmpty() || formatToName(name).isEmpty()
                ||description.trim().isEmpty()
                || damage.trim().isEmpty() || removeEmojis(damage.trim()).isEmpty()
                || difficulty.trim().isEmpty() || removeEmojis(description.trim()).isEmpty()
                || cost.trim().isEmpty() || removeEmojis(cost.trim()).isEmpty()
        ){
            view.onEmptyFields();
        }
        else {
            if (isNewMaho) {
                maho = new Maho();
            }

            maho.setCharacterId(characterId);
            maho.setName(formatToName(name));
            maho.setDescription(removeEmojis(description));
            maho.setDamage(removeEmojis(damage));
            maho.setDifficulty(removeEmojis(difficulty));
            maho.setCost(removeEmojis(cost));
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
