package com.example.apprpg.models;

import com.example.apprpg.interfaces.base.FirebaseObjectContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import static com.example.apprpg.utils.StringNodes.NODE_COMMENT;
import static com.example.apprpg.utils.StringNodes.NODE_LIKERS_LIST;

public class Comment extends Publication
        implements Serializable, FirebaseObjectContract {

    private String body;
    private String postId;

    public Comment() {
    }

    @Override
    public void updateLikersInFirebase(List<String> newLikersList) {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_COMMENT)
                .child(postId)
                .child(getId())
                .child(NODE_LIKERS_LIST)
                .setValue(newLikersList);
    }

    @Override
    public void saveInFirebase() {
        setPublicationDate(new MyCustomDate(Calendar.getInstance()));
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_COMMENT)
                .child(postId)
                .child(generateId())
                .setValue(this);
    }

    @Override
    public void deleteFromFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_COMMENT)
                .child(postId)
                .child(getId())
                .removeValue();
    }

    @Override
    public String generateId() {
        if (getId() == null || getId().isEmpty()) {
            DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
            String myId = databaseReference
                    .child(NODE_COMMENT)
                    .push()
                    .getKey();

            setId(myId);
        }
        return getId();
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
