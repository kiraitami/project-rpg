package com.example.apprpg.presenter;

import android.net.Uri;

import com.example.apprpg.interfaces.CharacterProfileContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.apprpg.utils.StringNodes.NODE_COVER_IMAGES;
import static com.example.apprpg.utils.StringNodes.NODE_PROFILE_IMAGES;

public class CharacterProfilePresenter
        implements CharacterProfileContract.CharacterProfilePresenter {

    private CharacterProfileContract.CharacterProfileView view;

    private boolean canSaveCover = false, canSavePicture = false;
    private int numberOfUploadingRequest = 0;

    public CharacterProfilePresenter(CharacterProfileContract.CharacterProfileView view) {
        this.view = view;
    }

    @Override
    public void addProfilePicture(Uri selectedImage, User user, Character character) {

        if (canSavePicture) {

            numberOfUploadingRequest++;

            StorageReference firebaseStorage = FirebaseHelper.getFirebaseStorage();
            StorageReference imageReference = firebaseStorage
                    .child(NODE_PROFILE_IMAGES)
                    .child(user.getId())
                    .child(character.getId());

            UploadTask uploadTask = imageReference.putFile(selectedImage);

            uploadTask.addOnSuccessListener(taskSnapshot ->
                    imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        character.setProfilePictureUrl(uri.toString());
                        character.saveInFirebase();
                        numberOfUploadingRequest--;
                        canSavePicture = false;
                        if (view != null) {
                            view.updateCharacterProfileUrl(uri.toString());
                            finalizeUploads();
                        }
            }))
                    .addOnFailureListener(e -> {
                        numberOfUploadingRequest--;
                        if (view != null) {
                            view.onSavingFailed(e.getMessage());
                        }
            });

        }
    }

    @Override
    public void addCoverPicture(Uri selectedImage, User user, Character character) {

        if (canSaveCover) {

            numberOfUploadingRequest++;

            StorageReference firebaseStorage = FirebaseHelper.getFirebaseStorage();
            StorageReference imageReference = firebaseStorage
                    .child(NODE_COVER_IMAGES)
                    .child(user.getId())
                    .child(character.getId());

            UploadTask uploadTask = imageReference.putFile(selectedImage);

            uploadTask.addOnSuccessListener(taskSnapshot ->
                    imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        character.setCoverPictureUrl(uri.toString());
                        character.saveInFirebase();
                        numberOfUploadingRequest--;
                        canSaveCover = false;
                        if (view != null) {
                            view.updateCharacterCoverUrl(uri.toString());
                            finalizeUploads();
                        }
            }))
                    .addOnFailureListener(e -> {
                        numberOfUploadingRequest--;
                        if (view != null) {
                            view.onSavingFailed(e.getMessage());
                        }
            });

        }
    }

    @Override
    public String updateBiography(String biography) {
        if (biography != null && !biography.isEmpty()){
            return biography;
        }
        return " ";
    }

    private void finalizeUploads(){
        if (numberOfUploadingRequest <= 0){
            view.onImageSaveSuccessful();
        }
    }

    @Override
    public void destroyView() {
        view = null;
    }


    public void setCanSaveCover(boolean canSaveCover) {
        this.canSaveCover = canSaveCover;
    }

    public void setCanSavePicture(boolean canSavePicture) {
        this.canSavePicture = canSavePicture;
    }
}
