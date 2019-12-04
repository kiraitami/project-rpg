package com.example.apprpg.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apprpg.R;
import com.example.apprpg.interfaces.TavernContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Post;
import com.example.apprpg.models.User;
import com.example.apprpg.notification.BaseUrlsAndTopics;
import com.example.apprpg.presenter.TavernPresenter;
import com.example.apprpg.ui.activities.AddPostActivity;
import com.example.apprpg.ui.activities.MainActivity;
import com.example.apprpg.ui.activities.PostDetailsActivity;
import com.example.apprpg.ui.adapters.TavernAdapter;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.example.apprpg.utils.StringNodes.NODE_POST;
import static com.example.apprpg.utils.StringNodes.NODE_POST_COUNT;

/**
 * A simple {@link Fragment} subclass.
 */
public class TavernFragment extends Fragment
        implements TavernContract.TavernView {

    private FloatingActionButton fab_create_post;
    private ProgressBar progressBar;
    private RecyclerView recyclerPosts;
    private View fragmentView;
    private Toolbar toolbar;

    private User user;
    private Character character;
    private int postCount = 0;

    private TavernPresenter tavernPresenter;

    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;

    public TavernFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_tavern, container, false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tavernPresenter = new TavernPresenter(this);
        getIntentData();
        setViewsById();

        setHasOptionsMenu(true);
        toolbar.setTitle(getResources().getString(R.string.tavern));

        clickEventActions();
        onRecyclerScroll();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadCharacterPostAmountFromFirebase();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPostsFromFirebase();
    }

    @Override
    public void onPause() {
        databaseReference.removeEventListener(valueEventListener);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        tavernPresenter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tavern, menu);
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.getItem(0).setChecked(user.getEnablePostNotifications());
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.op_menu_subscribe_tavern){
            if (!item.isChecked()){
                item.setChecked(true);
                FirebaseMessaging.getInstance().subscribeToTopic(BaseUrlsAndTopics.TAVERN_TOPIC);
            }
            else {
                item.setChecked(false);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(BaseUrlsAndTopics.TAVERN_TOPIC);
            }
            user.setEnablePostNotifications(item.isChecked());
            user.saveInFirebase();
        }

        else if (item.getItemId() == R.id.op_menu_all_characters){
            ((MainActivity) Objects.requireNonNull(getActivity())).updateUserAndCharacter(user, character);
            ((MainActivity) Objects.requireNonNull(getActivity())).goToAllCharacters();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void getIntentData() {
        Bundle data = getArguments();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
    }

    @Override
    public void setViewsById() {
        fab_create_post = getActivity().findViewById(R.id.fab_profile_main);
        recyclerPosts = fragmentView.findViewById(R.id.recycler_tavern);
        progressBar = fragmentView.findViewById(R.id.progress_tavern);
        toolbar = getActivity().findViewById(R.id.toolbar_main_activity);
    }

    @Override
    public void clickEventActions() {
        fab_create_post.setOnClickListener(view -> {
            requestPost();
        });
    }

    @Override
    public void requestPost() {
        tavernPresenter.verifyPostPermissions( character.getCanPost(), postCount);
    }

    @Override
    public void loadPostsFromFirebase() {
        onLoadingFromFirebase();
        databaseReference = FirebaseHelper.getFirebaseRef().child(NODE_POST);
        valueEventListener = databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Post> postList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            Post post = data.getValue(Post.class);
                            postList.add(post);
                        }

                        if (isAdded()) {
                            onLoadingFromFirebaseSuccess();
                            Collections.reverse(postList);
                            showAllPosts(postList);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void loadCharacterPostAmountFromFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference.child(NODE_POST_COUNT)
                .child(character.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postCount = (int) dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void showAllPosts(List<Post> postList) {
        recyclerPosts.setHasFixedSize(true);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPosts.setAdapter(new TavernAdapter(postList, getContext(), tavernPresenter, Glide.with(this), character.getName()));
    }

    @Override
    public void onPostClick(Post post) {
        Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
        intent.putExtra(getResources().getString(R.string.post_object), post);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        startActivityForResult(intent,0);
    }

    @Override
    public void onPostPermissionAccepted() {
        Intent intent = new Intent(getActivity(), AddPostActivity.class);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        startActivityForResult(intent,0);
    }

    @Override
    public void onPostLimit() {
        Toast.makeText(getContext(), getResources().getString(R.string.toast_post_limit), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPostPermissionDenied() {
        Toast.makeText(getContext(), getResources().getString(R.string.toast_cant_post), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRecyclerScroll() {
        recyclerPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE){
                    fab_create_post.hide();
                }
                else {
                    fab_create_post.show();
                }
            }
        });
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
