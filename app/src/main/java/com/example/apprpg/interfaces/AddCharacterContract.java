package com.example.apprpg.interfaces;

import android.net.Uri;

import com.example.apprpg.models.Character;

public interface AddCharacterContract {

    interface AddCharacterView {
        void getIntentData();

        void onEmptyField();

        void onVerifyingNickname();

        void onCollisionNickname();

        void onAddImageError(String error);

        void onImageEmpty();

        void onCreateSuccessful(Character character);

    }

    interface AddCharacterPresenter {

        void requestNewCharacter(String name, String breed, String classe, Uri profilePictureUrl, String userId);

        void verifyNickname();

        void createCharacter();

        void addProfileImage();

        void destroyView();
    }
}
