package com.example.apprpg.models;

import com.example.apprpg.interfaces.base.FirebaseObjectContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import static com.example.apprpg.utils.StringNodes.NODE_SKILL;

public class Skill extends InventoryItem
        implements FirebaseObjectContract, Serializable {


    private String damage;
    private String price;
    private String cost;
    private String cooldown;

    public Skill() {
    }

    @Override
    public void saveInFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_SKILL)
                .child(getCharacterId())
                .child(generateId())
                .setValue(this);
    }

    @Override
    public void deleteFromFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_SKILL)
                .child(getCharacterId())
                .child(getId())
                .removeValue();
    }

    @Override
    public String generateId() {
        if (getId() == null || getId().isEmpty()){
            DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
            String id = databaseReference
                    .child(NODE_SKILL)
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCooldown() {
        return cooldown;
    }

    public void setCooldown(String cooldown) {
        this.cooldown = cooldown;
    }
}
