package com.example.apprpg.interfaces.base;

public interface FirebaseObjectContract {
    void saveInFirebase();
    void deleteFromFirebase();
    String generateId();
}
