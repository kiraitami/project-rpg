package com.example.apprpg.presenter;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.apprpg.interfaces.AddPostContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Post;
import com.example.apprpg.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.apprpg.utils.StringHelper.formatToPostTitle;
import static com.example.apprpg.utils.StringNodes.NODE_POST_IMAGES;

public class AddPostPresenter
        implements AddPostContract.AddPostPresenter {

    private AddPostContract.AddPostView postView;

    private Post post;

    public AddPostPresenter(AddPostContract.AddPostView postView) {
        this.postView = postView;
    }

    @Override
    public void addPostImage(Uri selectedImage, String userId) {
        StorageReference firebaseStorage = FirebaseHelper.getFirebaseStorage();
        StorageReference imageReference = firebaseStorage
                .child(NODE_POST_IMAGES)
                .child(userId)
                .child(post.getId());

        UploadTask uploadTask = imageReference.putFile(selectedImage);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        post.setImageUrl(uri.toString());
                        post.saveInFirebase();

                        if (postView != null) { // if user back pressed
                            if (post.getWasEdited()) {
                                postView.onEditPostSuccessful();
                            } else {
                                postView.onAddPostSuccessful();
                            }
                        }

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (postView != null) { // if user back pressed
                    postView.onAddImageError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void requestNewPost(String title, String description, Uri selectedImage, User user, Character character) {
        if (selectedImage == null){
            postView.onNoImageError();
            return;
        }
        postView.onCreatingPost();
        if (validateFields(title, description, selectedImage)){
            post = new Post();
            post.generateId();
            post.setTitle(formatToPostTitle(title));
            post.setDescription(description);
            post.setCharacterId(character.getId());
            post.setCharacterName(character.getName());
            post.setUserId(user.getId());
            post.setUserName(user.getNickname());
            post.setCharacterPictureUrl(character.getProfilePictureUrl());
            post.setWasEdited(false);
            addPostImage(selectedImage, user.getId());
        }
    }

    @Override
    public void editPost(String title, String description, Uri selectedImage, Post post) {
        postView.onCreatingPost();
        if (validateFields(title, description, selectedImage)){
            post.setWasEdited(true);
            post.setTitle(formatToPostTitle(title));
            post.setDescription(description);
            this.post = post;
            if (selectedImage == null){ //will be null if user doesn't change image
             this.post.saveInFirebase();
             postView.onEditPostSuccessful();
            }
            else {
                addPostImage(selectedImage, post.getUserId());
            }
        }
    }

    private boolean validateFields(String title, String description, Uri selectedImage){
        if (title.trim().isEmpty()|| formatToPostTitle(title).isEmpty()|| description.trim().isEmpty()){
            postView.onEmptyFields();
            return false;
        }
        return true;
    }

    @Override
    public void destroyView() {
        this.postView = null;
    }
}
