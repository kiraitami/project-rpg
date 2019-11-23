package com.example.apprpg.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.apprpg.interfaces.WeaponsContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.example.apprpg.models.Weapon;
import com.example.apprpg.presenter.WeaponsPresenter;
import com.example.apprpg.ui.activities.AddWeaponActivity;
import com.example.apprpg.ui.activities.WeaponDetailsActivity;
import com.example.apprpg.R;
import com.example.apprpg.ui.adapters.WeaponAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeaponsFragment extends Fragment
        implements WeaponsContract.WeaponsView {


    private FloatingActionButton fab_add_weapon;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View view;
    private Toolbar toolbar;

    private User user;
    private Character character;

    private WeaponsPresenter presenter;

    public WeaponsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_weapons, container, false);

        presenter = new WeaponsPresenter(this);

        getIntentData();
        setViewsById();

        toolbar.setTitle(getResources().getString(R.string.armory));

        clickEventActions();
        setHasOptionsMenu(true);
        onRecyclerScroll();

        presenter.loadFromFirebase(character.getId(), this);

        return view;
    }

    @Override
    public void onDetach() {
        recyclerView.setAdapter(null);
        recyclerView = null;
        presenter.removeEventListener();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.order_by_favorite:
                if (item.isChecked()) {
                    presenter.showAll();
                }
                else {
                    presenter.orderByFavorite();
                }
                item.setChecked(!item.isChecked());
                break;
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
        fab_add_weapon = getActivity().findViewById(R.id.fab_profile_main);
        toolbar = getActivity().findViewById(R.id.toolbar_main_activity);
        recyclerView = view.findViewById(R.id.recycler_weapons);
        progressBar = view.findViewById(R.id.progress_weapons);
    }

    @Override
    public void clickEventActions() {
        fab_add_weapon.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddWeaponActivity.class);
            intent.putExtra(getResources().getString(R.string.user_object), user);
            intent.putExtra(getResources().getString(R.string.character_object), character);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    public void showWeapons(List<Weapon> weaponList) {
        if (isAdded()) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new WeaponAdapter(weaponList, presenter));
        }
    }

    @Override
    public void onWeaponClick(Weapon weapon) {
        Intent intent = new Intent(getActivity(), WeaponDetailsActivity.class);
        intent.putExtra(getResources().getString(R.string.weapon_object), weapon);
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
                    fab_add_weapon.hide();
                }
               else {
                    fab_add_weapon.show();
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
