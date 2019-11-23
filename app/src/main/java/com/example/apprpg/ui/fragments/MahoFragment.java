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

import com.example.apprpg.interfaces.MahosContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Maho;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.MahoPresenter;
import com.example.apprpg.ui.activities.AddMahoActivity;
import com.example.apprpg.ui.activities.MahoDetailsActivity;
import com.example.apprpg.R;
import com.example.apprpg.ui.adapters.MahoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MahoFragment extends Fragment
        implements MahosContract.MahoView {


    private FloatingActionButton fab_add_maho;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View view;
    private Toolbar toolbar;

    private User user;
    private Character character;

    private MahoPresenter presenter;

    public MahoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_maho, container, false);

        presenter = new MahoPresenter(this);

        getIntentData();
        setViewsById();

        toolbar.setTitle(getResources().getString(R.string.maho));

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
        fab_add_maho = getActivity().findViewById(R.id.fab_profile_main);
        toolbar = getActivity().findViewById(R.id.toolbar_main_activity);
        recyclerView = view.findViewById(R.id.recycler_maho);
        progressBar = view.findViewById(R.id.progress_maho);
    }

    @Override
    public void clickEventActions() {
        fab_add_maho.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AddMahoActivity.class);
            intent.putExtra(getResources().getString(R.string.user_object), user);
            intent.putExtra(getResources().getString(R.string.character_object), character);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    public void showItems(List<Maho> mahoList) {
        if (isAdded()) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new MahoAdapter(mahoList, presenter));
        }
    }

    @Override
    public void onItemClick(Maho maho) {
        Intent intent = new Intent(getActivity(), MahoDetailsActivity.class);
        intent.putExtra(getResources().getString(R.string.maho_object), maho);
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
                    fab_add_maho.hide();
                }
                else {
                    fab_add_maho.show();
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
