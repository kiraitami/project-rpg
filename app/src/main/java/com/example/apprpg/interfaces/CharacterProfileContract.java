package com.example.apprpg.interfaces;

import android.net.Uri;

import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;

public interface CharacterProfileContract {

    interface CharacterProfileView {
        void getIntentData();
        void showData();
        void loadImages();
        void configurePermissions();
        void onAddCoverImageClick();
        void onAddProfilePictureClick();
        void onEditCharacterClick();
        void onPermissionsError();
        void onLoadSaving();
        void updateCharacterCoverUrl(String coverUrl);
        void updateCharacterProfileUrl(String profileUrl);
        void onEditCharacterSuccessful();
        void onImageSaveSuccessful();
        void onSavingFailed(String error);
    }

    interface CharacterProfilePresenter {
        void addProfilePicture(Uri selectedImage, User user, Character character);
        void addCoverPicture(Uri selectedImage, User user, Character character);
        String updateBiography(String biography);
        void destroyView();
    }
}
