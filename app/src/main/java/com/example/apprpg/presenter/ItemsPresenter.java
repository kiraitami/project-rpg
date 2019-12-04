package com.example.apprpg.presenter;


import androidx.annotation.NonNull;

import com.example.apprpg.interfaces.ItemsContract;
import com.example.apprpg.models.InventoryItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.apprpg.utils.StringNodes.NODE_INVENTORY;

public class ItemsPresenter
        implements ItemsContract.ItemsPresenter {


    private ItemsContract.ItemsView view;

    private ValueEventListener valueEventListener;

    private List<InventoryItem> allItemsInFirebase = new ArrayList<>();

    public ItemsPresenter(ItemsContract.ItemsView view) {
        this.view = view;
    }

    @Override
    public void loadFromFirebase(String characterId, ItemsContract.ItemsView view, boolean showOrdered) {
        view.onLoadingFromFirebase();
        databaseReference
                .child(NODE_INVENTORY)
                .child(characterId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allItemsInFirebase.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            allItemsInFirebase.add( data.getValue(InventoryItem.class) );
                        }

                        view.onLoadingFromFirebaseSuccess();
                        Collections.reverse(allItemsInFirebase);
                        if (showOrdered){
                            orderByFavorite();
                        }
                        else {
                            view.showItems(allItemsInFirebase);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void showAll() {
        view.showItems(allItemsInFirebase);
    }

    @Override
    public void orderByFavorite() {
        List<InventoryItem> sortedItemList = new ArrayList<>(allItemsInFirebase);
        Collections.sort(sortedItemList);
        view.showItems(sortedItemList);
    }

    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onItemClick(InventoryItem item) {
        view.onItemClick(item);
    }

    @Override
    public void removeEventListener() {
    }

}
