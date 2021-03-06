package com.example.apprpg.ui.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprpg.R;
import com.example.apprpg.interfaces.SkillsContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Skill;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.SkillsPresenter;
import com.example.apprpg.ui.activities.AddSkillActivity;
import com.example.apprpg.ui.activities.SkillDetailsActivity;
import com.example.apprpg.ui.adapters.SkillAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SkillsFragment extends Fragment
        implements SkillsContract.SkillsView {


    private FloatingActionButton fab_add_skill;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View view;
    private Toolbar toolbar;

    private User user;
    private Character character;

    private SkillsPresenter presenter;

    private boolean orderByFavorite;

    public SkillsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_skills, container, false);

        presenter = new SkillsPresenter(this);

        getIntentData();
        setViewsById();

        toolbar.setTitle(getResources().getString(R.string.skills));

        clickEventActions();
        setHasOptionsMenu(true);
        onRecyclerScroll();

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(getResources().getString(R.string.base_key_pref),MODE_PRIVATE);
        orderByFavorite = sharedPreferences.getBoolean(getResources().getString(R.string.order_by_favorites_skills_key),false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadFromFirebase(character.getId(), this, orderByFavorite);
    }

    @Override
    public void onPause() {
        saveOrderByFavorites();
        super.onPause();
    }

    @Override
    public void onDetach() {
        recyclerView.setAdapter(null);
        recyclerView = null;
        presenter.onDestroyView();
        presenter = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.getItem(0).setChecked(orderByFavorite);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.order_by_favorite:
                if (item.isChecked()) {
                    presenter.showAll();
                }
                else {
                    presenter.orderByFavorite();
                }
                orderByFavorite = !item.isChecked();
                item.setChecked(orderByFavorite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveOrderByFavorites(){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(getResources().getString(R.string.base_key_pref), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getResources().getString(R.string.order_by_favorites_skills_key), orderByFavorite);
        editor.apply();
    }

    @Override
    public void getIntentData() {
        Bundle data = getArguments();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
    }

    @Override
    public void setViewsById() {
        fab_add_skill = getActivity().findViewById(R.id.fab_profile_main);
        toolbar = getActivity().findViewById(R.id.toolbar_main_activity);
        recyclerView = view.findViewById(R.id.recycler_skills);
        progressBar = view.findViewById(R.id.progress_skills);
    }

    @Override
    public void clickEventActions() {
        fab_add_skill.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddSkillActivity.class);
            intent.putExtra(getResources().getString(R.string.user_object), user);
            intent.putExtra(getResources().getString(R.string.character_object), character);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    public void showItems(List<Skill> skillList) {
        if (isAdded()) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new SkillAdapter(skillList, presenter));
        }
    }

    @Override
    public void onItemClick(Skill skill) {
        Intent intent = new Intent(getActivity(), SkillDetailsActivity.class);
        intent.putExtra(getResources().getString(R.string.skill_object), skill);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onRecyclerScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE){
                    fab_add_skill.hide();
                }
                else {
                    fab_add_skill.show();
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
