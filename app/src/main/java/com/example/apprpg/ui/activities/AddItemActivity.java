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

import com.example.apprpg.interfaces.AddItemContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.InventoryItem;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.AddItemPresenter;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;

public class AddItemActivity 
        extends AppCompatActivity implements AddItemContract.AddItemView {

    private AddItemPresenter presenter;
    
    private EditText input_name, input_desc, input_amount;
    private ImageButton btn_add;
    private ProgressBar progressBar;
    private ViewGroup content_layout;

    private User user;
    private Character character;
    private InventoryItem item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        presenter = new AddItemPresenter(this);

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
        if (item == null) {
            backToFragment();
        }
        exitWithAnimation();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyView();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void showData() {
        if (item != null){
            input_name.setText(item.getName());
            input_desc.setText(item.getDescription());
            input_amount.setText(String.valueOf(item.getAmount()));
        }
    }

    @Override
    public void onEmptyFields() {
        Toast.makeText(this, getResources().getString(R.string.empty_field_error), Toast.LENGTH_SHORT).show();
        setEnableFields(true);
    }

    @Override
    public void backToFragment() {
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.fragment_object), StringNodes.FRAGMENT_ITEMS);
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
        input_amount.setEnabled(enableFields);
    }

    @Override
    public void onAddLoading() {
        setEnableFields(false);
    }

    @Override
    public void onAddSuccessful() {
        Toast.makeText(this, getResources().getString(R.string.item_created_successful), Toast.LENGTH_SHORT).show();
        backToFragment();
        exitWithAnimation();
    }

    @Override
    public void onAddFailed() {
        setEnableFields(true);
    }

    @Override
    public void onEditSuccessful() {
        Toast.makeText(this, getResources().getString(R.string.item_edited_successful), Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.item_object), item);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        setResult(RESULT_OK, intent);
        exitWithAnimation();
    }

    @Override
    public void getIntentData() {
        Bundle data = getIntent().getExtras();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
        item = (InventoryItem) data.getSerializable(getResources().getString(R.string.item_object));
    }

    @Override
    public void setViewsById() {
        input_name = findViewById(R.id.input_item_name);
        input_desc = findViewById(R.id.input_item_desc);
        input_amount = findViewById(R.id.input_item_amount);
        btn_add = findViewById(R.id.btn_add_item);
        progressBar = findViewById(R.id.progress_bar_add_item);
        content_layout = findViewById(R.id.linear_layout_add_item);
    }

    @Override
    public void clickEventActions() {
        btn_add.setOnClickListener(view -> presenter.requestNewItem(
                input_name.getText().toString(),
                input_desc.getText().toString(),
                input_amount.getText().toString(),
                character.getId(),
                item
        ));
    }
}
