package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.AddWeaponContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Weapon;
import com.example.apprpg.utils.StringHelper;

public class AddWeaponPresenter
        implements AddWeaponContract.AddWeaponPresenter {


    private AddWeaponContract.AddWeaponView view;

    public AddWeaponPresenter(AddWeaponContract.AddWeaponView view) {
        this.view = view;
    }

    @Override
    public void requestNewWeapon(String name, String description, String damage, String amount, Character character, Weapon weapon) {
        view.onAddLoading();
        boolean isNewWeapon = weapon == null;

        if (name.trim().isEmpty() || StringHelper.formatToName(name).isEmpty()
                || description.trim().isEmpty()
                || damage.trim().isEmpty() || StringHelper.removeEmojis(damage).isEmpty()
                || amount.trim().isEmpty()){
            view.onEmptyFields();
        }
        else {
            if (isNewWeapon) {
                weapon = new Weapon();
            }

            weapon.setCharacterId(character.getId());
            weapon.setName(StringHelper.formatToName(name));
            weapon.setDescription(description);
            weapon.setDamage(StringHelper.removeEmojis(damage));
            weapon.setAmount(Integer.parseInt(amount));
            weapon.setFavorite(0);
            weapon.saveInFirebase();

            if (isNewWeapon) {
                view.onAddSuccessful();
            }
            else {
                view.onEditSuccessful();
            }
        }
    }

    @Override
    public void onDestroyView() {
        view = null;
    }
}
