package com.example.apprpg.presenter;


import androidx.annotation.NonNull;

import com.example.apprpg.interfaces.MahosContract;
import com.example.apprpg.models.Maho;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.apprpg.utils.StringNodes.NODE_MAHO;

public class MahoPresenter
        implements MahosContract.MahoPresenter {


    private MahosContract.MahoView view;

    private ValueEventListener valueEventListener;

    private List<Maho> allMahosInFirebase = new ArrayList<>();

    public MahoPresenter(MahosContract.MahoView view) {
        this.view = view;
    }

    @Override
    public void loadFromFirebase(String characterId, MahosContract.MahoView view, boolean showOrdered) {
        view.onLoadingFromFirebase();
        databaseReference
                .child(NODE_MAHO)
                .child(characterId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allMahosInFirebase.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            allMahosInFirebase.add( data.getValue(Maho.class) );
                        }

                        view.onLoadingFromFirebaseSuccess();
                        Collections.reverse(allMahosInFirebase);
                        if (showOrdered) {
                            orderByFavorite();
                        }
                        else {
                            view.showItems(allMahosInFirebase);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void showAll() {
        view.showItems(allMahosInFirebase);
    }

    @Override
    public void orderByFavorite() {
        List<Maho> sortedMahoList = new ArrayList<>(allMahosInFirebase);
        Collections.sort(sortedMahoList);
        view.showItems(sortedMahoList);
    }

    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onItemClick(Maho maho) {
        view.onItemClick(maho);
    }

    @Override
    public void removeEventListener() {
    }

}
