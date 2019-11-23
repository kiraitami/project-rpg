package com.example.apprpg.interfaces.base;

import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

public interface RecyclerFragmentPresenterContract {

    DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();

    void showAll();
    void orderByFavorite();
    void removeEventListener();
    void onDestroyView();
}
