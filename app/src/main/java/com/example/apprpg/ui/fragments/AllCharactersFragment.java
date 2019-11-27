package com.example.apprpg.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.apprpg.R;
import com.example.apprpg.interfaces.MyCharactersContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.MyCharactersPresenter;
import com.example.apprpg.ui.activities.ProfileVisitorActivity;
import com.example.apprpg.ui.adapters.CharactersAdapter;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.example.apprpg.utils.StringNodes.NODE_CHARACTER;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllCharactersFragment extends Fragment
        implements MyCharactersContract.MyCharactersView {

    private FloatingActionButton fab_add;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View view;
    private Toolbar toolbar;

    private User user;
    private Character character;

    private List<Character> characterList = new ArrayList<>();

    private MyCharactersPresenter presenter;

    private ValueEventListener eventListener;
    private DatabaseReference databaseReference;

    public AllCharactersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_characters, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new MyCharactersPresenter(this);

        getIntentData();
        setViewsById();

        toolbar.setTitle(R.string.all_characters);
        fab_add.hide();

        clickEventActions();

        loadFromFirebase();
    }

    @Override
    public void onDestroyView() {
        databaseReference.removeEventListener(eventListener);
        presenter.onDestroyView();
        super.onDestroyView();
    }


    @Override
    public void loadFromFirebase() {
        onLoadingFromFirebase();
        databaseReference = FirebaseHelper.getFirebaseRef().child(NODE_CHARACTER);
        eventListener = databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        characterList.clear();
                        for(DataSnapshot usersIds : dataSnapshot.getChildren()){

                            for (DataSnapshot charactersIds : usersIds.getChildren()){
                                Character c = charactersIds.getValue(Character.class);
                                if (c != null) {
                                    characterList.add(c);
                                }

                            }
                        }

                        if (isAdded()) {
                            onLoadingFromFirebaseSuccess();
                            Collections.sort(characterList, (character, t1) -> character.getName().compareTo(t1.getName()));
                            showCharacters(characterList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void showCharacters(List<Character> characterList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CharactersAdapter(characterList, Glide.with(this), presenter));
    }

    @Override
    public void onCharacterClick(Character hostCharacter) {
        if (character.getId().equals(hostCharacter.getId())) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(getResources().getString(R.string.dialog_visit_title, hostCharacter.getName()))
                .setMessage(getResources().getString(R.string.dialog_visit_msg, hostCharacter.getName()))
                .setPositiveButton(getResources().getString(R.string.dialog_visit_confirm),(dialogInterface, i) -> {
                    Intent intent = new Intent(getActivity(), ProfileVisitorActivity.class);
                    intent.putExtra(getResources().getString(R.string.user_object), user);
                    intent.putExtra(getResources().getString(R.string.character_object), character);
                    intent.putExtra(getResources().getString(R.string.host_character_object), hostCharacter);
                    startActivityForResult(intent, 0);
                })
                .setNegativeButton(getResources().getString(R.string.dialog_visit_cancel), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void getIntentData() {
        Bundle data = getArguments();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
    }

    @Override
    public void setViewsById() {
        fab_add = getActivity().findViewById(R.id.fab_profile_main);
        toolbar = getActivity().findViewById(R.id.toolbar_main_activity);
        recyclerView = view.findViewById(R.id.recycler_all_characters);
        progressBar = view.findViewById(R.id.progress_all_characters);
    }

    @Override
    public void clickEventActions() {

    }

    @Override
    public void onRecyclerScroll() {

    }

    @Override
    public void onLoadingFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFromFirebaseSuccess() {
        progressBar.setVisibility(View.GONE);
    }

}
