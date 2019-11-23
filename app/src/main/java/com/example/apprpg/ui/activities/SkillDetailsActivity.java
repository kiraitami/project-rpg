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

import com.example.apprpg.interfaces.SkillDetailsContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Skill;
import com.example.apprpg.models.User;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;


public class SkillDetailsActivity extends AppCompatActivity
        implements SkillDetailsContract.SkillDetailsView {


    private TextView description, damage, cost, price, cooldown;
    private ViewGroup content_layout;
    private Toolbar toolbar;

    private User user;
    private Character character;
    private Skill skill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_details);

        getIntentData();
        setViewsById();
        showData();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        if (skill!= null)
            skill.saveInFirebase();
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
            skill = (Skill) data.getSerializableExtra(getResources().getString(R.string.skill_object));
            showData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skill_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle( getResources().getString( skill.getFavorite() > 0 ?
                R.string.menu_unfavorite_skill_title :
                R.string.menu_favorite_skill_title ));
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
        skill = (Skill) data.getSerializable(getResources().getString(R.string.skill_object));
    }

    @Override
    public void setViewsById() {
        description = findViewById(R.id.skill_details_description);
        damage = findViewById(R.id.skill_details_damage);
        cost = findViewById(R.id.skill_details_cost);
        price = findViewById(R.id.skill_details_price);
        cooldown = findViewById(R.id.skill_details_cooldown);
        content_layout = findViewById(R.id.linear_layout_skill_details);
        toolbar = findViewById(R.id.toolbar_skill_details);
    }

    @Override
    public void showData() {
        toolbar.setTitle(skill.getName());
        description.setText(skill.getDescription());
        damage.setText(skill.getDamage());
        cost.setText(skill.getCost());
        price.setText(skill.getPrice());
        cooldown.setText(skill.getCooldown());
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
                intent.putExtra(getResources().getString(R.string.fragment_object), StringNodes.FRAGMENT_SKILLS);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onFavoriteClick() {
        skill.setFavorite( skill.getFavorite() > 0 ? 0 : 1 );
    }

    @Override
    public void onEditClick() {
        Intent intent = new Intent(this, AddSkillActivity.class);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.skill_object), skill);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onDeleteClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getResources().getString(R.string.alert_title_delete, skill.getName());
        builder.setTitle(title)
                .setMessage(getResources().getString(R.string.alert_msg_delete))
                .setPositiveButton(getResources().getString(R.string.alert_positive_button_delete), (dialogInterface, i) -> {
                    skill.deleteFromFirebase();
                    skill = null;
                    Toast.makeText(this, getResources().getString(R.string.skill_deleted_successful), Toast.LENGTH_SHORT).show();
                    exitWithAnimation();
                })
                .setNegativeButton(getResources().getString(R.string.alert_negative_button_delete), null)
                .create()
                .show();
    }

}
