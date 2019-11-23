package com.example.apprpg.models;


import com.example.apprpg.interfaces.base.FirebaseObjectContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import static com.example.apprpg.utils.StringNodes.NODE_ARMORY;

public class Weapon extends InventoryItem
        implements Serializable, FirebaseObjectContract {


    private String damage;

    public Weapon() {
    }

    @Override
    public void saveInFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();

        databaseReference
                .child(NODE_ARMORY)
                .child(getCharacterId())
                .child(generateId())
                .setValue(this);
    }

    @Override
    public void deleteFromFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_ARMORY)
                .child(getCharacterId())
                .child(getId())
                .removeValue();
    }

    @Override
    public String generateId() {
        if (getId() == null || getId().isEmpty()){
            DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
            String id = databaseReference
                    .child(NODE_ARMORY)
                    .child(getCharacterId())
                    .push()
                    .getKey();

            setId(id);
        }
        return getId();
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }


}
