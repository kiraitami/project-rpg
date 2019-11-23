package com.example.apprpg.interfaces;

import android.net.Uri;

import com.example.apprpg.models.Character;
import com.example.apprpg.models.Post;
import com.example.apprpg.models.User;

public interface AddPostContract {

    interface AddPostView {
        void getIntentData();
        void configurePermissions();
        void onCreatingPost();
        void onEmptyFields();
        void onAddImageClick();
        void onNoImageError();
        void onAddPostSuccessful();
        void onEditPostSuccessful();
        void onPermissionsError();
        void onAddImageError(String error);
    }

    interface AddPostPresenter {
        void addPostImage(Uri selectedImage, String userId);
        void requestNewPost(String title, String description, Uri selectedImage, User user, Character character);
        void editPost(String title, String description, Uri selectedImage, Post pos);
        void destroyView();
    }

}
