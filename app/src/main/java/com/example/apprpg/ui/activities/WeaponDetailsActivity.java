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
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprpg.interfaces.WeaponDetailsContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.example.apprpg.models.Weapon;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;


public class WeaponDetailsActivity extends AppCompatActivity
        implements WeaponDetailsContract.WeaponDetailsView {

    private TextView description, damage, amount;
    private ViewGroup content_layout;
    private Toolbar toolbar;

    private User user;
    private Character character;
    private Weapon weapon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapon_details);

        getIntentData();
        setViewsById();
        showData();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        if (weapon!= null)
            weapon.saveInFirebase();
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
            weapon = (Weapon) data.getSerializableExtra(getResources().getString(R.string.weapon_object));
            showData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weapon_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle( getResources().getString( weapon.getFavorite() > 0 ?
                R.string.menu_unfavorite_weapon_title :
                R.string.menu_favorite_weapon_title ));
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
        weapon = (Weapon) data.getSerializable(getResources().getString(R.string.weapon_object));
    }

    @Override
    public void setViewsById() {
        description = findViewById(R.id.weapon_details_description);
        damage = findViewById(R.id.weapon_details_damage);
        amount = findViewById(R.id.weapon_details_amount);
        content_layout = findViewById(R.id.linear_layout_weapon_details);
        toolbar = findViewById(R.id.toolbar_weapon_details);
    }

    @Override
    public void showData() {
        toolbar.setTitle(weapon.getName());
        description.setText(weapon.getDescription());
        damage.setText(weapon.getDamage());
        amount.setText(String.valueOf(weapon.getAmount()));
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
                intent.putExtra(getResources().getString(R.string.fragment_object), StringNodes.FRAGMENT_WEAPONS);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onFavoriteClick() {
        weapon.setFavorite( weapon.getFavorite() > 0 ? 0 : 1 );
    }

    @Override
    public void onEditClick() {
        Intent intent = new Intent(this, AddWeaponActivity.class);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.weapon_object), weapon);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onDeleteClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getResources().getString(R.string.alert_title_delete, weapon.getName());
        builder.setTitle(title)
                .setMessage(getResources().getString(R.string.alert_msg_delete))
                .setPositiveButton(getResources().getString(R.string.alert_positive_button_delete), (dialogInterface, i) -> {
                    weapon.deleteFromFirebase();
                    weapon = null;
                    Toast.makeText(this, getResources().getString(R.string.weapon_deleted_successful), Toast.LENGTH_SHORT).show();
                    exitWithAnimation();
                })
                .setNegativeButton(getResources().getString(R.string.alert_negative_button_delete), null)
                .create()
                .show();
    }




}
