package com.example.apprpg.interfaces;

import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;

public interface LoginContract {

    interface LoginView{
        void onAuthenticatingLogin();
        void onAuthenticatingLoginFinished();
        void onWrongEmailOrPassword();
        void onNonVerifiedEmail();
        void onDuplicatedEmail();
        void onEmptyField();
        void onUnmatchedPassword();
        void onUnexistentUser();
        void onExceptionLoginOrRegister(String e);
        void onLoginError();
        void onLoadingUserAndCharacter();
        void onUserHasNoCharacter(User user);
        void loginSuccessful(User user, Character character);
        void onRequestNewUser();
        void registerSuccessful(User user);
        void onRegisterFailed();
        void onInvalidEmail();
        void onRecoverAccountError(String error);
        void onRecoverAccountSuccess();
    }


    interface LoginPresenter{
        void authenticateLogin(String email, String password);
        void createNewUser(String nick, String email, String password, String confirmPassword);
        void readUserFromFirebase(String userId);
        void readCharacterFromFirebase(User user);
        void destroyView();
        void forgotPassword(String email);
    }
}
