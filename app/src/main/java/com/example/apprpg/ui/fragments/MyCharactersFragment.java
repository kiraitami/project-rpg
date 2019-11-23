package com.example.apprpg.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.apprpg.interfaces.MyCharactersContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.MyCharactersPresenter;
import com.example.apprpg.ui.activities.AddCharacterActivity;
import com.example.apprpg.ui.activities.MainActivity;
import com.example.apprpg.utils.FirebaseHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;
import com.example.apprpg.ui.adapters.CharactersAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCharactersFragment extends Fragment implements MyCharactersContract.MyCharactersView {

    private FloatingActionButton fab_add;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View view;
    private Toolbar toolbar;

    private User user;
    private List<Character> characterList = new ArrayList<>();

    private MyCharactersPresenter presenter;

    private ValueEventListener eventListener;
    private DatabaseReference databaseReference;

    public MyCharactersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_characters, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new MyCharactersPresenter(this);

        getIntentData();
        setViewsById();

        toolbar.setTitle(R.string.my_characters);

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
        databaseReference = FirebaseHelper.getFirebaseRef().child(StringNodes.NODE_CHARACTER).child(user.getId());
        eventListener = databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        characterList.clear();
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            characterList.add( data.getValue(Character.class) );
                        }
                        if (isAdded()) {
                            onLoadingFromFirebaseSuccess();
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
    public void onCharacterClick(Character character) {
        user.setCurrentCharacterId(character.getId());
        user.saveInFirebase();
        ((MainActivity) Objects.requireNonNull(getActivity())).updateUserAndCharacter(user, character);
        ((MainActivity) Objects.requireNonNull(getActivity())).backToBeforeMyCharactersFragment();
    }

    @Override
    public void getIntentData() {
        Bundle data = getArguments();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
    }

    @Override
    public void setViewsById() {
        fab_add = getActivity().findViewById(R.id.fab_profile_main);
        toolbar = getActivity().findViewById(R.id.toolbar_main_activity);
        recyclerView = view.findViewById(R.id.recycler_my_characters);
        progressBar = view.findViewById(R.id.progress_my_characters);
    }

    @Override
    public void clickEventActions() {
        fab_add.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddCharacterActivity.class);
            intent.putExtra(getResources().getString(R.string.user_object), user);
            startActivity(intent);
        });
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
