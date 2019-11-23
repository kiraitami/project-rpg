package com.example.apprpg.presenter;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.apprpg.interfaces.AddCharacterContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.example.apprpg.models.Character;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.apprpg.utils.StringHelper.formatToName;
import static com.example.apprpg.utils.StringNodes.NODE_CHARACTER;
import static com.example.apprpg.utils.StringNodes.NODE_CHARACTER_NAME;
import static com.example.apprpg.utils.StringNodes.NODE_PROFILE_IMAGES;

public class AddCharacterPresenter
        implements AddCharacterContract.AddCharacterPresenter {

    private AddCharacterContract.AddCharacterView view;

    private Character character;
    private String name, breed, classe, userId;
    private Uri profilePictureUri;

    public AddCharacterPresenter(AddCharacterContract.AddCharacterView view) {
        this.view = view;
    }


    @Override
    public void requestNewCharacter(String name, String breed, String classe, Uri profilePictureUrl, String userId) {

        if (name == null || name.trim().isEmpty()
                || breed == null || breed.trim().isEmpty()
                || classe == null || classe.trim().isEmpty()) {

            view.onEmptyField();

        }
        else if (formatToName(name).isEmpty() || formatToName(breed).isEmpty() || formatToName(classe).isEmpty()){
            view.onEmptyField();
        }

        else if (profilePictureUrl == null){
            view.onImageEmpty();
        }

        else {
            this.name = formatToName(name);
            this.breed = formatToName(breed);
            this.classe = formatToName(classe);
            this.userId = userId;
            this.profilePictureUri = profilePictureUrl;
            verifyNickname();
        }
    }

    @Override
    public void verifyNickname() {
        view.onVerifyingNickname();
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(NODE_CHARACTER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isNickValid = true;

                        for (DataSnapshot userId : dataSnapshot.getChildren()){

                            for (DataSnapshot characterId : userId.getChildren()){

                                String nameToCompare = characterId.child(NODE_CHARACTER_NAME).getValue(String.class);

                                if ( name.equals( nameToCompare )){
                                    isNickValid = false;
                                    view.onCollisionNickname();
                                    break;
                                }
                            }
                        }

                        if (isNickValid){
                            createCharacter();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void createCharacter() {
        character = new Character();
        character.setUserId(userId);
        character.setName(name);
        character.setBreed(breed);
        character.setClasse(classe);
        character.generateId();
        if (profilePictureUri != null){
            addProfileImage();
        }
    }

    @Override
    public void addProfileImage() {
        StorageReference firebaseStorage = FirebaseHelper.getFirebaseStorage();
        StorageReference imageReference = firebaseStorage
                .child(NODE_PROFILE_IMAGES)
                .child(userId)
                .child(character.getId());

        UploadTask uploadTask = imageReference.putFile(profilePictureUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        character.setProfilePictureUrl(uri.toString());
                        character.saveInFirebase();
                        view.onCreateSuccessful(character);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.onAddImageError(e.getMessage());
            }
        });
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
