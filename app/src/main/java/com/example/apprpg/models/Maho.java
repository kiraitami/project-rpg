package com.example.apprpg.models;

import com.example.apprpg.interfaces.base.FirebaseObjectContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import static com.example.apprpg.utils.StringNodes.NODE_MAHO;

public class Maho extends InventoryItem
        implements Serializable, FirebaseObjectContract {


    private String damage;
    private String difficulty;
    private String cost;


    public Maho() {
    }

    @Override
    public void saveInFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_MAHO)
                .child(getCharacterId())
                .child(generateId())
                .setValue(this);
    }

    @Override
    public void deleteFromFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_MAHO)
                .child(getCharacterId())
                .child(getId())
                .removeValue();
    }

    @Override
    public String generateId() {
        if (getId() == null || getId().isEmpty()){
            DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
            String id = databaseReference
                    .child(NODE_MAHO)
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
