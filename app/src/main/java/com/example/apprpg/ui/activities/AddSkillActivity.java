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

import com.example.apprpg.interfaces.AddSkillContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Skill;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.AddSkillPresenter;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;

public class AddSkillActivity
        extends AppCompatActivity implements AddSkillContract.AddSkillView {

    private AddSkillPresenter presenter;

    private EditText input_name, input_desc, input_cost, input_price, input_damage, input_cooldown;
    private ImageButton btn_add;
    private ProgressBar progressBar;
    private ViewGroup content_layout;

    private User user;
    private Character character;
    private Skill skill = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skill);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        presenter = new AddSkillPresenter(this);

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
        if (skill == null)
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
        skill = (Skill) data.getSerializable(getResources().getString(R.string.skill_object));
    }

    @Override
    public void setViewsById() {
        input_name = findViewById(R.id.input_skill_name);
        input_desc = findViewById(R.id.input_skill_desc);
        input_damage = findViewById(R.id.input_skill_damage);
        input_cost = findViewById(R.id.input_skill_cost);
        input_price = findViewById(R.id.input_skill_price);
        input_cooldown = findViewById(R.id.input_skill_cooldown);
        btn_add = findViewById(R.id.btn_add_skill);
        progressBar = findViewById(R.id.progress_bar_add_skill);
        content_layout = findViewById(R.id.linear_layout_add_skill);
    }

    @Override
    public void showData() {
        if (skill != null){
            input_name.setText(skill.getName());
            input_desc.setText(skill.getDescription());
            input_damage.setText(skill.getDamage());
            input_cost.setText(skill.getCost());
            input_price.setText(skill.getPrice());
            input_cooldown.setText(skill.getCooldown());
        }
    }

    @Override
    public void clickEventActions() {
        btn_add.setOnClickListener(view -> presenter.requestNewSkill(
                input_name.getText().toString(),
                input_desc.getText().toString(),
                input_damage.getText().toString(),
                input_cost.getText().toString(),
                input_price.getText().toString(),
                input_cooldown.getText().toString(),
                character.getId(),
                skill
        ));
    }

    @Override
    public void backToFragment() {
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.fragment_object), StringNodes.FRAGMENT_SKILLS);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void exitWithAnimation() {
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
    public void setEnableFields(boolean enableFields) {
        if (enableFields){
            btn_add.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        else {
            btn_add.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        input_name.setEnabled(enableFields);
        input_desc.setEnabled(enableFields);
        input_damage.setEnabled(enableFields);
        input_price.setEnabled(enableFields);
        input_cost.setEnabled(enableFields);
        input_cooldown.setEnabled(enableFields);
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
        Toast.makeText(this, getResources().getString(R.string.skill_created_successful), Toast.LENGTH_SHORT).show();
        backToFragment();
        exitWithAnimation();
    }

    @Override
    public void onAddFailed() {
        setEnableFields(true);
    }

    @Override
    public void onEditSuccessful() {
        Toast.makeText(this, getResources().getString(R.string.skill_edited_successful), Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.skill_object), skill);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        setResult(RESULT_OK, intent);
        exitWithAnimation();
    }

}
