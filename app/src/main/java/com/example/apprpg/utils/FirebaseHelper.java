package com.example.apprpg.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {

    private static FirebaseAuth auth;
    private static DatabaseReference ref;
    private static StorageReference storage;

    public static DatabaseReference getFirebaseRef(){
        if(ref == null){
            ref = FirebaseDatabase.getInstance().getReference();
        }
        return ref;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static StorageReference getFirebaseStorage(){
        if( storage == null ){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }

}
