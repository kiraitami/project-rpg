package com.example.apprpg.presenter;


import androidx.annotation.NonNull;

import com.example.apprpg.interfaces.WeaponsContract;
import com.example.apprpg.models.Weapon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.apprpg.utils.StringNodes.NODE_ARMORY;

public class WeaponsPresenter
        implements WeaponsContract.WeaponsPresenter {


    private WeaponsContract.WeaponsView view;
    private ValueEventListener valueEventListener;
    private List<Weapon> allWeaponsInFirebase = new ArrayList<>();

    public WeaponsPresenter(WeaponsContract.WeaponsView view) {
        this.view = view;
    }

    @Override
    public void loadFromFirebase(String characterId, WeaponsContract.WeaponsView view, boolean showOrdered) {
        view.onLoadingFromFirebase();
        databaseReference
                .child(NODE_ARMORY)
                .child(characterId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allWeaponsInFirebase.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            allWeaponsInFirebase.add( data.getValue(Weapon.class) );
                        }

                        view.onLoadingFromFirebaseSuccess();
                        Collections.reverse(allWeaponsInFirebase);
                        if (showOrdered) {
                            orderByFavorite();
                        }
                        else {
                            view.showWeapons(allWeaponsInFirebase);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void showAll() {
        view.showWeapons(allWeaponsInFirebase);
    }

    @Override
    public void orderByFavorite() {
        List<Weapon> sortedWeaponList = new ArrayList<>(allWeaponsInFirebase);
        Collections.sort(sortedWeaponList);
        view.showWeapons(sortedWeaponList);
    }

    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onWeaponClick(Weapon weapon) {
        view.onWeaponClick(weapon);
    }

    @Override
    public void removeEventListener() {
    }

}
