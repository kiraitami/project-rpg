package com.example.apprpg.ui.fragments;


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
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprpg.interfaces.ExperienceContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.MyCustomDate;
import com.example.apprpg.models.User;
import com.example.apprpg.models.Xp;
import com.example.apprpg.presenter.ExperiencePresenter;
import com.example.apprpg.utils.FirebaseHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;
import com.example.apprpg.ui.adapters.XpAdapter;
import com.example.apprpg.ui.dialog.AddXpDialog;
import com.example.apprpg.ui.dialog.UpdateAttributeDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExperienceFragment extends Fragment
        implements ExperienceContract.ExperienceView {


    private FloatingActionButton fab_add_xp;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View view;
    private Toolbar toolbar;
    private TextView level, current_xp, total_xp;
    private AddXpDialog xpDialog;

    private User user;
    private Character character;
    private List<Xp> xpList;

    private ExperiencePresenter presenter;

    private final static int SUBTRACT = -1, ADD = 1;

    public ExperienceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_experience, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new ExperiencePresenter(this);

        getIntentData();
        setViewsById();

        toolbar.setTitle(character.getName());

        clickEventActions();
        setHasOptionsMenu(true);
        onRecyclerScroll();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadFromFirebase();
    }

    @Override
    public void onPause() {
        updateChangesInFirebase();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void getIntentData() {
        Bundle data = getArguments();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
    }

    @Override
    public void setViewsById() {
        fab_add_xp = getActivity().findViewById(R.id.fab_profile_main);
        toolbar = getActivity().findViewById(R.id.toolbar_main_activity);
        recyclerView = view.findViewById(R.id.recycler_xp);
        progressBar = view.findViewById(R.id.progress_experience);
        level = view.findViewById(R.id.xp_level);
        current_xp = view.findViewById(R.id.xp_current);
        total_xp = view.findViewById(R.id.xp_total);
    }

    @Override
    public void clickEventActions() {
        fab_add_xp.show();
        fab_add_xp.setOnClickListener(view1 -> {
            xpDialog = new AddXpDialog(getActivity());
            xpDialog
                    .addListener(view2 -> {
                        requestNewXp(ADD);
                    })
                    .subtractListener(view2 -> {
                        requestNewXp(SUBTRACT);
                    })
                    .show();
        });

        level.setOnClickListener(view1 -> {
            UpdateAttributeDialog dialog = new UpdateAttributeDialog(getActivity(), character.getLevel(), getResources().getString(R.string.xp));
            dialog.setTitle(getResources().getString(R.string.update_level_title))
                    .setInputMaxLength(getResources().getInteger(R.integer.max_length_level_value))
                    .positiveListener(view2 -> {
                        character.setLevel(dialog.getInputtedValue());
                        level.setText(String.valueOf(character.getLevel()));
                        dialog.dismiss();
                        Toast.makeText(getContext(), getResources().getString(R.string.level_updated_successful), Toast.LENGTH_SHORT).show();
                    })
                    .negativeListener(view2 -> dialog.dismiss())
                    .show();
        });

        current_xp.setOnClickListener(view1 -> {
            UpdateAttributeDialog dialog = new UpdateAttributeDialog(getActivity(), character.getCurrentXP(), getResources().getString(R.string.current_xp));
            dialog.setTitle(getResources().getString(R.string.update_current_xp))
                    .setInputMaxLength(getResources().getInteger(R.integer.max_length_xp_value))
                    .positiveListener(view2 -> {
                        character.setCurrentXP(dialog.getInputtedValue());
                        current_xp.setText(String.valueOf(character.getCurrentXP()));
                        dialog.dismiss();
                        Toast.makeText(getContext(), getResources().getString(R.string.xp_updated_successful), Toast.LENGTH_SHORT).show();
                    })
                    .negativeListener(view2 -> dialog.dismiss())
                    .show();
        });


    }

    private void requestNewXp(int operator){
        presenter.requestNewXp(xpDialog.getInputtedXp(), new MyCustomDate(Calendar.getInstance()), operator);
    }

    @Override
    public void showXp(List<Xp> xpList) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new XpAdapter(xpList, presenter));

        int totalXp = 0;
        for (Xp xp : xpList){
            totalXp += xp.getXpAmount();
        }
        character.setTotalXP(totalXp);
        level.setText(String.valueOf(character.getLevel()));
        current_xp.setText(String.valueOf(character.getCurrentXP()));
        total_xp.setText(getResources().getString(R.string.xp_history, totalXp));
    }

    @Override
    public void onXpClick(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle(R.string.alert_remove_xp_title)
                .setMessage(R.string.alert_remove_xp_msg)
                .setPositiveButton(R.string.alert_remove_btn, (dialogInterface, i) -> {
                    xpList.remove(position);
                    showXp(xpList);
                })
                .setNegativeButton(R.string.alert_cancel_btn, null)
                .create()
                .show();
    }

    @Override
    public void addXp(Xp xp) {
        xpList.add(xp);
        showXp(xpList);
    }

    @Override
    public void loadFromFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef().child(StringNodes.NODE_XP).child(character.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        xpList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            xpList.add( data.getValue(Xp.class) );
                        }
                        if (isAdded()) {
                            showXp(xpList);
                        }
                        databaseReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void updateChangesInFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(StringNodes.NODE_XP)
                .child(character.getId())
                .setValue(xpList);

        character.saveInFirebase();
    }

    @Override
    public void onRecyclerScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE){
                    fab_add_xp.hide();
                }
                else {
                    fab_add_xp.show();
                }
            }
        });
    }

    @Override
    public void onAddXpSuccessful() {
        xpDialog.dismiss();
        Toast.makeText(getActivity(), getResources().getString(R.string.xp_added_successful), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddXpError() {
        Toast.makeText(getActivity(), getResources().getString(R.string.xp_added_failed), Toast.LENGTH_SHORT).show();
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
