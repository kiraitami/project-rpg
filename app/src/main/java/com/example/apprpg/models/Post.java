package com.example.apprpg.models;

import com.example.apprpg.interfaces.base.FirebaseObjectContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import static com.example.apprpg.utils.StringNodes.NODE_COMMENT;
import static com.example.apprpg.utils.StringNodes.NODE_LIKERS_LIST;
import static com.example.apprpg.utils.StringNodes.NODE_POST;
import static com.example.apprpg.utils.StringNodes.NODE_POST_COUNT;

public class Post extends Publication
        implements Serializable, FirebaseObjectContract {

    private String imageUrl;
    private String title;

    public Post() {
    }

    @Override
    public void updateLikersInFirebase(List<String> newLikersList) {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_POST)
                .child(getId())
                .child(NODE_LIKERS_LIST)
                .setValue(newLikersList);
    }


    @Override
    public void saveInFirebase() {
        setPublicationDate(new MyCustomDate(Calendar.getInstance()));
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_POST)
                .child(generateId())
                .setValue(this);

        databaseReference
                .child(NODE_POST_COUNT)
                .child(getCharacterId())
                .child(getId())
                .setValue(getTitle());
    }

    @Override
    public void deleteFromFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_POST)
                .child(getId())
                .removeValue();

        databaseReference
                .child(NODE_POST_COUNT)
                .child(getCharacterId())
                .child(getId())
                .removeValue();

        databaseReference
                .child(NODE_COMMENT)
                .child(getId())
                .removeValue();
    }

    @Override
    public String generateId() {
        if (getId() == null || getId().isEmpty()) {
            DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
            String myId = databaseReference
                    .child(NODE_POST)
                    .push()
                    .getKey();
            setId(myId);
        }
        return getId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
