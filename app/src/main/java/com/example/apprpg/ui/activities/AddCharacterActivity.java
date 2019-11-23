package com.example.apprpg.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apprpg.interfaces.AddCharacterContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.AddCharacterPresenter;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCharacterActivity extends AppCompatActivity 
        implements AddCharacterContract.AddCharacterView {
    
    private TextInputLayout input_name_l, input_breed_l, input_classe_l;
    private TextInputEditText input_name, input_breed, input_classe;
    private ImageButton btn_create_character;
    private CircleImageView profile_image;
    private ImageView bg_fade;
    private ViewGroup content_layout;
    private ProgressBar progress_bar;

    private User user;
    
    private AddCharacterPresenter addCharacterPresenter;
    private Uri selectedImageUri = null;

    private final int REQUEST_IMAGE_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_character);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        addCharacterPresenter = new AddCharacterPresenter(this);

        getIntentData();
        setViewsById();
        setClickListenersEvents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null){
            selectedImageUri = data.getData();
            profile_image.setImageURI(selectedImageUri);
        }
    }

    @Override
    protected void onDestroy() {
        addCharacterPresenter.destroyView();
        addCharacterPresenter = null;
        super.onDestroy();
    }

    private void setViewsById(){
        input_name_l = findViewById(R.id.input_character_name_l);
        input_breed_l = findViewById(R.id.input_character_breed_l);
        input_classe_l = findViewById(R.id.input_character_classe_l);
        input_name = findViewById(R.id.input_character_name);
        input_breed = findViewById(R.id.input_character_breed);
        input_classe = findViewById(R.id.input_character_classe);
        btn_create_character = findViewById(R.id.btn_create_character);
        profile_image = findViewById(R.id.character_profile_image);
        content_layout = findViewById(R.id.scroll_layout_add_character);
        bg_fade = findViewById(R.id.bg_fade_add_character);
        progress_bar = findViewById(R.id.progress_add_character);
    }

    private void setClickListenersEvents() {

        profile_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_CODE);
        });

        btn_create_character.setOnClickListener(view ->{
            clearAllErrors();

            addCharacterPresenter.requestNewCharacter(
                    input_name.getText().toString(),
                    input_breed.getText().toString(),
                    input_classe.getText().toString(),
                    selectedImageUri,
                    user.getId()
                );
        });
    }


    private void finishWithAnimation(Character character){
        long screenWidth = ScreenDimensHelper.getScreenWidth(getWindowManager());

        content_layout.animate().translationX(screenWidth).setDuration(350).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                content_layout.setVisibility(View.GONE);

                bg_fade.setAlpha(0f);
                bg_fade.setVisibility(View.VISIBLE);
                bg_fade.animate().alpha(1f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        Toast.makeText(AddCharacterActivity.this, getResources().getString(R.string.character_created_successful)+character.getName(), Toast.LENGTH_SHORT ).show();
                        Intent intent = new Intent(AddCharacterActivity.this, MainActivity.class);
                        user.setCurrentCharacterId(character.getId());
                        user.saveInFirebase();
                        intent.putExtra(getResources().getString(R.string.user_object), user);
                        intent.putExtra(getResources().getString(R.string.character_object), character);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void clearAllErrors(){
        input_name_l.setError(null);
        input_breed_l.setError(null);
        input_classe_l.setError(null);
    }

    private void setEnableFields(boolean enableFields){
        input_name.setEnabled(enableFields);
        input_breed.setEnabled(enableFields);
        input_classe.setEnabled(enableFields);
        btn_create_character.setEnabled(enableFields);

        if (enableFields){
            progress_bar.setVisibility(View.GONE);
            btn_create_character.setVisibility(View.VISIBLE);
        }
        else {
            progress_bar.setVisibility(View.VISIBLE);
            btn_create_character.setVisibility(View.GONE);
        }
    }

    @Override
    public void getIntentData() {
        Bundle data = getIntent().getExtras();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
    }

    @Override
    public void onEmptyField() {
        Toast.makeText(this, getResources().getString(R.string.empty_field_error), Toast.LENGTH_SHORT).show();
        setEnableFields(true);
    }

    @Override
    public void onVerifyingNickname() {
        setEnableFields(false);
    }

    @Override
    public void onCollisionNickname() {
        input_name_l.setError(getResources().getString(R.string.collision_character_name_error));
        setEnableFields(true);
    }

    @Override
    public void onAddImageError(String error) {
        Toast.makeText(this, getResources().getString(R.string.toast_add_image_error)+error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImageEmpty() {
        Toast.makeText(this, getResources().getString(R.string.toast_add_image_empty_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateSuccessful(Character character) {
        finishWithAnimation(character);
    }
}
