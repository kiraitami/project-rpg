package com.example.apprpg.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.apprpg.interfaces.DiceRollContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Dice;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.DiceRollPresenter;
import com.example.apprpg.utils.FirebaseHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;
import com.example.apprpg.ui.adapters.DiceRollAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.circlemenu.CircleMenuView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DiceRollActivity extends AppCompatActivity
        implements DiceRollContract.DiceRollView {

    private NumberPicker number_picker;
    private TextView result, result_details;
    private TextInputEditText input_modifier;
    private CircleMenuView circle_menu;
    private Toolbar toolbar;

    private User user;
    private Character character;
    private Dice dice;
    private List<Dice> history = new ArrayList<>();
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;

    private DiceRollPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_roll);

        presenter = new DiceRollPresenter(this);

        getIntentData();
        setViewsById();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.dice_roll));

        clickEventActions();
        number_picker.setMinValue(1);
        number_picker.setMaxValue(10);
        number_picker.setWrapSelectorWheel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistoryFromFirebase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        presenter.resizeHistory(character.getId(), history);
        presenter.onDestroyView();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.fragment_object), StringNodes.FRAGMENT_ATTRIBUTES);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dice_roll, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.op_menu_dice_history:
                if (history != null) {
                    showRollHistory();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getIntentData() {
        Bundle data = getIntent().getExtras();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
    }

    @Override
    public void setViewsById() {
        circle_menu = findViewById(R.id.circle_menu_dice_roll);
        number_picker = findViewById(R.id.dice_number_picker);
        result = findViewById(R.id.roll_result);
        result_details = findViewById(R.id.roll_result_details);
        input_modifier = findViewById(R.id.input_dice_modfier);
        toolbar = findViewById(R.id.toolbar_roll_activity);
    }

    @Override
    public void clickEventActions() {
        circle_menu.setEventListener(new CircleMenuView.EventListener(){
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                super.onMenuOpenAnimationStart(view);
                result.setVisibility(View.INVISIBLE);
                result_details.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationStart(view, buttonIndex);
                int modifierValue = input_modifier.getText() == null || input_modifier.getText().toString().isEmpty() ?
                        0 : Integer.parseInt(input_modifier.getText().toString());

                dice = presenter.buildAndRollDice(character, modifierValue,
                        number_picker.getValue(),buttonIndex);
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationEnd(view, buttonIndex);
                showResult();
            }
        });
    }

    @Override
    public void showResult() {
        String details = dice.getRollDetails() + (dice.getRollModifier()!= 0 ? getResources().getString(R.string.roll_details_modifier, dice.getRollModifier()) :"");
        result.setVisibility(View.VISIBLE);
        result_details.setVisibility(View.VISIBLE);
        result.setText(String.valueOf(dice.getRollResult()));
        result_details.setText(details);
    }

    @Override
    public void loadHistoryFromFirebase() {
        databaseReference = FirebaseHelper.getFirebaseRef().child(StringNodes.NODE_ROLLS).child(character.getId());
        valueEventListener = databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        history.clear();
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            history.add( data.getValue(Dice.class) );
                        }
                        Collections.reverse(history);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void showRollHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_dice_roll_history, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_roll_history);
        TextView character_name = view.findViewById(R.id.name_roll_history);
        character_name.setText(character.getName());
        builder.setView(view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DiceRollAdapter(history, this));
        AlertDialog alertDialog = builder.create();
        character_name.setOnClickListener(view1 -> alertDialog.dismiss());
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }
}
