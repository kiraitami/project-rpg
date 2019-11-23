package com.example.apprpg.models;

import com.example.apprpg.interfaces.base.FirebaseObjectContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import static com.example.apprpg.utils.StringNodes.NODE_INVENTORY;

public class InventoryItem
        implements Serializable, FirebaseObjectContract, Comparable<InventoryItem> {

    private String id;
    private String characterId;
    private String name;
    private String description;
    private int favorite;
    private int amount;

    public InventoryItem() {
    }

    @Override
    public void saveInFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();

        databaseReference
                .child(NODE_INVENTORY)
                .child(getCharacterId())
                .child(generateId())
                .setValue(this);
    }

    @Override
    public void deleteFromFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_INVENTORY)
                .child(getCharacterId())
                .child(getId())
                .removeValue();
    }

    @Override
    public String generateId() {
        if (getId() == null || getId().isEmpty()){

            DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
            String id = databaseReference
                    .child(NODE_INVENTORY)
                    .child(getCharacterId())
                    .push()
                    .getKey();

            setId(id);
        }

        return getId();
    }

    @Override
    public int compareTo(InventoryItem inventoryItem) {
        if (this.favorite > inventoryItem.getFavorite())
            return -1;

        else if (this.favorite < inventoryItem.getFavorite())
            return 1;

        return 0;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
