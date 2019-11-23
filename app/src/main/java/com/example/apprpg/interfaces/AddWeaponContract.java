package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BaseAddInventoryObjectContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Weapon;
//import com.example.apprpg.model.User;

public interface AddWeaponContract {

    interface AddWeaponView extends BaseAddInventoryObjectContract.View {
    }

    interface AddWeaponPresenter extends BaseAddInventoryObjectContract.Presenter{
        void requestNewWeapon(String name, String description, String damage, String amount, Character character, Weapon weapon);
    }
}
