package com.example.apprpg.models;

import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.security.SecureRandom;
import java.util.Calendar;

import static com.example.apprpg.utils.StringNodes.NODE_ROLLS;

public class Dice {

    private int numberOfFaces;
    private int diceAmount;
    private int rollModifier;
    private int rollResult;
    private String id;
    private String rollDetails;
    private String characterName;
    private MyCustomDate date;

    public Dice() {
    }

    public void roll(String characterId){
        SecureRandom randomRoll = new SecureRandom();
        int diceResult;
        StringBuilder details = new StringBuilder();
        rollResult = 0;

        for (int i = 0; i < diceAmount; i ++){
            diceResult = randomRoll.nextInt(numberOfFaces)+1;
            rollResult += diceResult;
            details.append( i < diceAmount-1 ? (diceResult + " + ") : diceResult);
        }

        rollDetails = details.toString();
        rollResult += rollModifier;
        date = new MyCustomDate(Calendar.getInstance());

        saveInFirebase(characterId);
    }

    private void saveInFirebase(String characterId){
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        id = databaseReference.child(NODE_ROLLS)
                .child(characterId)
                .push()
                .getKey();

        databaseReference.child(NODE_ROLLS)
                .child(characterId)
                .child(id)
                .setValue(this);
    }

    public void removeDiceFromFirebase(String characterId){
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference.child(NODE_ROLLS)
                .child(characterId)
                .child(id)
                .removeValue();
    }

    public int getNumberOfFaces() {
        return numberOfFaces;
    }

    public void setNumberOfFaces(int numberOfFaces) {
        this.numberOfFaces = numberOfFaces;
    }

    public int getDiceAmount() {
        return diceAmount;
    }

    public void setDiceAmount(int diceAmount) {
        this.diceAmount = diceAmount > 10 ? 10 : diceAmount;
    }

    public int getRollModifier() {
        return rollModifier;
    }

    public void setRollModifier(int rollModifier) {
        this.rollModifier = rollModifier;
    }

    public int getRollResult() {
        return rollResult;
    }

    public String getRollDetails() {
        return rollDetails;
    }

    public String getId() {
        return id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public MyCustomDate getDate() {
        return date;
    }
}
