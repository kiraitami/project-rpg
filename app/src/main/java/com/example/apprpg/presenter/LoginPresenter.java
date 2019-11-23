package com.example.apprpg.presenter;

import androidx.annotation.NonNull;

import com.example.apprpg.interfaces.LoginContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.example.apprpg.utils.StringNodes.NODE_CHARACTER;
import static com.example.apprpg.utils.StringNodes.NODE_USER;

public class LoginPresenter
        implements LoginContract.LoginPresenter {

    private LoginContract.LoginView loginView;
    private FirebaseAuth auth;

    private User user;

    public LoginPresenter(LoginContract.LoginView loginView){
        this.loginView = loginView;
    }


    @Override
    public void authenticateLogin(String email, String password) {
        if ( email == null || email.trim().isEmpty() || password ==null || password.trim().isEmpty()){
            loginView.onEmptyField();
        }

        else {
            loginView.onAuthenticatingLogin();
            auth = FirebaseHelper.getFirebaseAuth();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                if (task.isSuccessful()){

                    Objects.requireNonNull(auth.getCurrentUser()).reload().addOnCompleteListener(task2 -> {
                       if (auth.getCurrentUser().isEmailVerified()){
                           readUserFromFirebase(auth.getCurrentUser().getUid());
                       }
                       else {
                           loginView.onNonVerifiedEmail();
                           loginView.onAuthenticatingLoginFinished();
                       }
                   });
               }

               else {
                   try {
                       throw Objects.requireNonNull(task.getException());
                   }
                   catch (FirebaseAuthInvalidCredentialsException e) {
                       loginView.onWrongEmailOrPassword();
                   }
                   catch (FirebaseAuthInvalidUserException e){
                       loginView.onUnexistentUser();
                   }
                   catch (Exception e){
                       e.printStackTrace();
                       loginView.onExceptionLoginOrRegister(e.getMessage());
                   }
                   loginView.onAuthenticatingLoginFinished();
               }
            });
        }
    }

    @Override
    public void createNewUser(String nick, String email, String password, String confirmPassword) {
        if ( nick == null || nick.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()
                || confirmPassword == null || confirmPassword.trim().isEmpty() ){

            loginView.onEmptyField();

        }

        else if (!password.equals(confirmPassword)){
            loginView.onUnmatchedPassword();
        }

        else {
            loginView.onRequestNewUser();
            auth = FirebaseHelper.getFirebaseAuth();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            user = new User.Builder()
                                    .id(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                                    .email(email)
                                    .password(password)
                                    .nickname(nick)
                                    .build();
                            user.saveInFirebase();

                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()){
                                    loginView.registerSuccessful(user);
                                }
                                else {
                                    loginView.onRegisterFailed();
                                }
                            });
                        }

                        else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            }
                            catch (FirebaseAuthUserCollisionException e){
                                loginView.onDuplicatedEmail();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                loginView.onExceptionLoginOrRegister(e.getMessage());
                            }

                            loginView.onRegisterFailed();
                        }
                    });
        }

    }

    //get user from firebase and check if user has at last one character
    @Override
    public void readUserFromFirebase(String userId) {
        loginView.onLoadingUserAndCharacter();
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef().child(NODE_USER).child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            if (user.getCurrentCharacterId() == null || user.getCurrentCharacterId().isEmpty()) {
                                loginView.onUserHasNoCharacter(user);
                            } else {
                                readCharacterFromFirebase(user);
                            }
                        }
                        else {
                            loginView.onLoginError();
                        }
                        databaseReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void readCharacterFromFirebase(User user) {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef().child(NODE_CHARACTER)
                .child(user.getId())
                .child(user.getCurrentCharacterId());
        databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Character character = dataSnapshot.getValue(Character.class);
                        loginView.loginSuccessful(user, character);
                        databaseReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void destroyView() {
        this.loginView = null;
    }

    @Override
    public void forgotPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            loginView.onInvalidEmail();
        }
        else {
            auth = FirebaseHelper.getFirebaseAuth();
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    loginView.onRecoverAccountSuccess();
                }
                else {
                    loginView.onRecoverAccountError(Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        }
    }

}
