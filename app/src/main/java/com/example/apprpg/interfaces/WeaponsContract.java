package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BasePresenterContract;
import com.example.apprpg.interfaces.base.BaseViewContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentPresenterContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentViewContract;
import com.example.apprpg.models.Weapon;

import java.util.List;

public interface WeaponsContract {

    interface WeaponsView extends BaseViewContract, RecyclerFragmentViewContract {
        void showWeapons(List<Weapon> weaponList);
        void onWeaponClick(Weapon weapon);
    }

    interface WeaponsPresenter extends BasePresenterContract, RecyclerFragmentPresenterContract {
        void onWeaponClick(Weapon weapon);
        void loadFromFirebase(String characterId, WeaponsContract.WeaponsView view);
    }
}
