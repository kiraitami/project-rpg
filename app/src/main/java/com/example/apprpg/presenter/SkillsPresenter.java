package com.example.apprpg.presenter;


import androidx.annotation.NonNull;

import com.example.apprpg.interfaces.SkillsContract;
import com.example.apprpg.models.Skill;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.apprpg.utils.StringNodes.NODE_SKILL;

public class SkillsPresenter
        implements SkillsContract.SkillsPresenter {


    private SkillsContract.SkillsView view;

    private ValueEventListener valueEventListener;

    private List<Skill> allSkillsInFirebase = new ArrayList<>();

    public SkillsPresenter(SkillsContract.SkillsView view) {
        this.view = view;
    }

    @Override
    public void loadFromFirebase(String characterId, SkillsContract.SkillsView view, boolean showOrdered) {
        view.onLoadingFromFirebase();
        databaseReference
                .child(NODE_SKILL)
                .child(characterId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allSkillsInFirebase.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            allSkillsInFirebase.add( data.getValue(Skill.class) );
                        }

                        view.onLoadingFromFirebaseSuccess();
                        Collections.reverse(allSkillsInFirebase);
                        if (showOrdered) {
                            orderByFavorite();
                        }
                        else {
                            view.showItems(allSkillsInFirebase);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void showAll() {
        view.showItems(allSkillsInFirebase);
    }

    @Override
    public void orderByFavorite() {
        List<Skill> sortedSkillList = new ArrayList<>(allSkillsInFirebase);
        Collections.sort(sortedSkillList);
        view.showItems(sortedSkillList);
    }

    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onItemClick(Skill skill) {
        view.onItemClick(skill);
    }

    @Override
    public void removeEventListener() {
    }

}
