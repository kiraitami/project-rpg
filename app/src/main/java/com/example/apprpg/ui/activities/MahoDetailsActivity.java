package com.example.apprpg.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprpg.interfaces.MahoDetailsContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Maho;
import com.example.apprpg.models.User;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;


public class MahoDetailsActivity extends AppCompatActivity
        implements MahoDetailsContract.MahoDetailsView {


    private TextView description, damage, cost, difficulty;
    private LinearLayout damage_layout, cost_layout, difficulty_layout;
    private ViewGroup content_layout;
    private Toolbar toolbar;

    private User user;
    private Character character;
    private Maho maho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maho_details);

        getIntentData();
        setViewsById();
        showData();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        if (maho!= null)
            maho.saveInFirebase();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        exitWithAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            user = (User) data.getSerializableExtra(getResources().getString(R.string.user_object));
            character = (Character) data.getSerializableExtra(getResources().getString(R.string.character_object));
            maho = (Maho) data.getSerializableExtra(getResources().getString(R.string.maho_object));
            showData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maho_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle( getResources().getString( maho.getFavorite() > 0 ?
                R.string.menu_unfavorite_maho_title :
                R.string.menu_favorite_maho_title ));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.op_menu_favorite:
                onFavoriteClick();
                break;
            case R.id.op_menu_edit:
                onEditClick();
                break;
            case R.id.op_menu_delete:
                onDeleteClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getIntentData() {
        Bundle data = getIntent().getExtras();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
        maho = (Maho) data.getSerializable(getResources().getString(R.string.maho_object));
    }

    @Override
    public void setViewsById() {
        description = findViewById(R.id.maho_details_description);
        damage = findViewById(R.id.maho_details_damage);
        damage_layout = findViewById(R.id.maho_details_damage_layout);
        cost = findViewById(R.id.maho_details_cost);
        cost_layout = findViewById(R.id.maho_details_cost_layout);
        difficulty = findViewById(R.id.maho_details_difficulty);
        difficulty_layout = findViewById(R.id.maho_details_difficulty_layout);
        content_layout = findViewById(R.id.linear_layout_maho_details);
        toolbar = findViewById(R.id.toolbar_maho_details);
    }

    @Override
    public void showData() {
        toolbar.setTitle(maho.getName());
        StringHelper.formatToDescription(maho.getDescription(), description);
        cost.setText(maho.getCost());
        hideLayoutIfEmptyValue(maho.getCost(), cost, cost_layout);
        hideLayoutIfEmptyValue(maho.getDamage(), damage, damage_layout);
        hideLayoutIfEmptyValue(maho.getDifficulty(), difficulty, difficulty_layout);
    }

    private void hideLayoutIfEmptyValue(String value, TextView textView ,LinearLayout layout){
        if (value == null || value.trim().isEmpty())
            layout.setVisibility(View.GONE);
        else
            textView.setText(value);
    }

    @Override
    public void exitWithAnimation() {
        long screenWidth = ScreenDimensHelper.getScreenWidth(getWindowManager());
        content_layout.animate().translationX(screenWidth).setDuration(350).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = getIntent();
                intent.putExtra(getResources().getString(R.string.user_object), user);
                intent.putExtra(getResources().getString(R.string.character_object), character);
                intent.putExtra(getResources().getString(R.string.fragment_object), StringNodes.FRAGMENT_MAHO);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onFavoriteClick() {
        maho.setFavorite( maho.getFavorite() > 0 ? 0 : 1 );
    }

    @Override
    public void onEditClick() {
        Intent intent = new Intent(this, AddMahoActivity.class);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.maho_object), maho);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onDeleteClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getResources().getString(R.string.alert_title_delete, maho.getName());
        builder.setTitle(title)
                .setMessage(getResources().getString(R.string.alert_msg_delete))
                .setPositiveButton(getResources().getString(R.string.alert_positive_button_delete), (dialogInterface, i) -> {
                    maho.deleteFromFirebase();
                    maho = null;
                    Toast.makeText(this, getResources().getString(R.string.maho_deleted_successful), Toast.LENGTH_SHORT).show();
                    exitWithAnimation();
                })
                .setNegativeButton(getResources().getString(R.string.alert_negative_button_delete), null)
                .create()
                .show();
    }

}
