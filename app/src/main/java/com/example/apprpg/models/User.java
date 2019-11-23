package com.example.apprpg.models;

import com.example.apprpg.interfaces.base.FirebaseObjectContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import static com.example.apprpg.utils.StringNodes.NODE_USER;

public class User
        implements Serializable, FirebaseObjectContract {

    private String id;
    private String email;
    private String password;
    private String nickname;
    private String currentCharacterId;

    public User() { // empty constructor for firebase
    }

    private User(String id, String email, String password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public static class Builder {
        private String id;
        private String email;
        private String password;
        private String nickname;

        public Builder() {

        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public User build(){
            return new User(id, email, password, nickname);
        }

    }

    @Override
    public void saveInFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference.child(NODE_USER)
                .child(this.id)
                .setValue(this);
    }

    @Override
    public void deleteFromFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference.child(NODE_USER)
                .child(this.id)
                .removeValue();
    }

    @Override
    public String generateId() {
        return getId();
    }


    public String getCurrentCharacterId() {
        return currentCharacterId;
    }

    public void setCurrentCharacterId(String currentCharacterId) {
        this.currentCharacterId = currentCharacterId;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
