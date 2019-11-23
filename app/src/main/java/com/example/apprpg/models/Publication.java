package com.example.apprpg.models;

import com.example.apprpg.interfaces.base.FirebaseObjectContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Publication implements Serializable, FirebaseObjectContract {

    private String id;
    private String characterId;
    private String characterName;
    private String userId;
    private String characterPictureUrl;
    private String userName;
    private String description;
    private MyCustomDate publicationDate;
    private boolean wasEdited;
    private List<String> likersList = new ArrayList<>();

    public Publication() {
    }

    public boolean like(String likerId){
        if (!likersList.contains(likerId)) {
            likersList.add(likerId);
            return true;
        }
        return false;
    }

    public boolean unlike(String unlikerId){
        if (likersList.contains(unlikerId)) {
            likersList.remove(unlikerId);
            return true;
        }
        return false;
    }

    public void updateLikersInFirebase(List<String> newLikersList){

    }

    @Override
    public void saveInFirebase() {

    }

    @Override
    public void deleteFromFirebase() {

    }

    @Override
    public String generateId() {
        return getId();
    }

    public List<String> getLikersList() {
        return likersList;
    }

    public String getCharacterPictureUrl() {
        return characterPictureUrl;
    }

    public void setCharacterPictureUrl(String characterPictureUrl) {
        this.characterPictureUrl = characterPictureUrl;
    }

    public boolean getWasEdited() {
        return wasEdited;
    }

    public void setWasEdited(boolean wasEdited) {
        this.wasEdited = wasEdited;
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

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MyCustomDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(MyCustomDate publicationDate) {
        this.publicationDate = publicationDate;
    }
}
