package com.example.apprpg.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apprpg.interfaces.AddWeaponContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.example.apprpg.models.Weapon;
import com.example.apprpg.presenter.AddWeaponPresenter;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;

public class AddWeaponActivity extends AppCompatActivity
        implements AddWeaponContract.AddWeaponView {

    private AddWeaponPresenter presenter;

    private EditText input_name, input_desc, input_damage, input_amount;
    private ImageButton btn_add_weapon;
    private ProgressBar progressBar;
    private ViewGroup content_layout;

    private User user;
    private Character character;
    private Weapon weapon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weapon);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        presenter = new  AddWeaponPresenter(this);

        getIntentData();
        setViewsById();
        clickEventActions();

    }

    @Override
    protected void onStart() {
        super.onStart();
        showData();
    }

    @Override
    public void onBackPressed() {
        if (weapon == null)
            backToFragment();
        exitWithAnimation();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyView();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void getIntentData() {
        Bundle data = getIntent().getExtras();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
        weapon = (Weapon) data.getSerializable(getResources().getString(R.string.weapon_object));
    }

    @Override
    public void setViewsById(){
        input_name = findViewById(R.id.input_weapon_name);
        input_desc = findViewById(R.id.input_weapon_desc);
        input_damage = findViewById(R.id.input_weapon_damage);
        input_amount = findViewById(R.id.input_weapon_amount);
        btn_add_weapon = findViewById(R.id.btn_add_weapon);
        progressBar = findViewById(R.id.progress_bar_add_weapon);
        content_layout = findViewById(R.id.linear_layout_add_weapon);
    }

    @Override
    public void showData() {
        if (weapon != null){
            input_name.setText(weapon.getName());
            input_desc.setText(weapon.getDescription());
            input_damage.setText(weapon.getDamage());
            input_amount.setText(String.valueOf(weapon.getAmount()));
        }
    }

    @Override
    public void clickEventActions() {
        btn_add_weapon.setOnClickListener(view -> presenter.requestNewWeapon(
                input_name.getText().toString(),
                input_desc.getText().toString(),
                input_damage.getText().toString(),
                input_amount.getText().toString(),
                character,
                weapon
        ));
    }

    @Override
    public void backToFragment() {
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.fragment_object), StringNodes.FRAGMENT_WEAPONS);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void exitWithAnimation(){
        long screenWidth = ScreenDimensHelper.getScreenWidth(getWindowManager());
        content_layout.animate().translationX(screenWidth).setDuration(350).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                finish();
            }
        });
    }

    @Override
    public void setEnableFields(boolean enableField){
        if (enableField){
            btn_add_weapon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        else {
            btn_add_weapon.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        input_name.setEnabled(enableField);
        input_desc.setEnabled(enableField);
        input_damage.setEnabled(enableField);
        input_amount.setEnabled(enableField);
    }

    @Override
    public void onAddLoading() {
        setEnableFields(false);
    }

    @Override
    public void onEmptyFields() {
        Toast.makeText(this, getResources().getString(R.string.empty_field_error), Toast.LENGTH_SHORT).show();
        setEnableFields(true);
    }

    @Override
    public void onAddSuccessful() {
        Toast.makeText(this, getResources().getString(R.string.weapon_created_successful), Toast.LENGTH_SHORT).show();
        backToFragment();
        exitWithAnimation();
    }

    @Override
    public void onAddFailed() {
        setEnableFields(true);
    }

    @Override
    public void onEditSuccessful() {
        Toast.makeText(this, getResources().getString(R.string.weapon_edited_successful), Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.weapon_object), weapon);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        setResult(RESULT_OK, intent);
        exitWithAnimation();
    }
}
