package com.example.apprpg.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apprpg.interfaces.AddPostContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Post;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.AddPostPresenter;
import com.example.apprpg.utils.Permissions;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.R;

public class AddPostActivity extends AppCompatActivity
        implements AddPostContract.AddPostView {

    private EditText input_title, input_description;
    private ImageView post_image;
    private ImageButton btn_add_post;
    private ProgressBar progress_bar;
    private ViewGroup content_layout;

    private User user;
    private Character character;
    private Post post = null;

    private boolean loading = false;

    private AddPostPresenter addPostPresenter;

    private final int ADD_IMAGE_REQUEST_CODE = 0;
    private Uri selectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        addPostPresenter = new AddPostPresenter(this);

        getIntentData();
        setViewsById();
        setClickListenersEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showData();
    }

    @Override
    public void onBackPressed() {
        if (!loading) {
            exit();
        }
    }

    @Override
    protected void onDestroy() {
        addPostPresenter.destroyView();
        addPostPresenter = null;
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissionResult : grantResults){
            if (permissionResult == PackageManager.PERMISSION_DENIED){
                onPermissionsError();
            }
        }
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data!= null){
            post_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            selectedImage = data.getData();
            post_image.setImageURI(selectedImage);
        }
    }

    private void setViewsById(){
        input_title = findViewById(R.id.input_title_add_post);
        input_description = findViewById(R.id.input_desc_add_post);
        post_image = findViewById(R.id.add_post_image);
        progress_bar = findViewById(R.id.progress_add_post);
        content_layout = findViewById(R.id.linear_layout_add_post);
        btn_add_post = findViewById(R.id.btn_add_post);
    }

    private void setClickListenersEvents(){
        post_image.setOnClickListener(view -> onAddImageClick());

        btn_add_post.setOnClickListener(view -> {
            if (post == null) {
                addPostPresenter.requestNewPost(input_title.getText().toString(),
                        input_description.getText().toString(),
                        selectedImage,
                        user,
                        character);
            }

            else {
                addPostPresenter.editPost(input_title.getText().toString(),
                        input_description.getText().toString(),
                        selectedImage,
                        post);
            }
        });
    }

    private void setEnableFields(boolean enableFields){
        loading = enableFields;
        input_title.setEnabled(enableFields);
        input_description.setEnabled(enableFields);
        btn_add_post.setEnabled(enableFields);
        post_image.setEnabled(enableFields);

        if (enableFields){
            progress_bar.setVisibility(View.GONE);
            btn_add_post.setVisibility(View.VISIBLE);
        }
        else {
            progress_bar.setVisibility(View.VISIBLE);
            btn_add_post.setVisibility(View.GONE);
        }
    }

    private void showData(){
        if (post != null){
            Glide.with(getApplicationContext()).load(post.getImageUrl()).thumbnail(0.1f).into(post_image);
            input_title.setText(post.getTitle());
            input_description.setText(post.getDescription());
        }
    }

    private void exit(){
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.post_object), post);
        setResult(RESULT_OK, intent);
        exitWithAnimation();
    }

    private void exitWithAnimation(){
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
    public void getIntentData() {
        Bundle data = getIntent().getExtras();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
        post = (Post) data.getSerializable(getResources().getString(R.string.post_object));
    }

    @Override
    public void configurePermissions() {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        Permissions.validattePermissions(permissions, this, ADD_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onCreatingPost() {
        setEnableFields(false);
    }

    @Override
    public void onEmptyFields() {
        setEnableFields(true);
        Toast.makeText(this, getResources().getString(R.string.empty_field_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddImageClick() {
        configurePermissions();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ADD_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onEditPostSuccessful() {
        Toast.makeText(this, getResources().getString(R.string.post_edited_successful), Toast.LENGTH_SHORT).show();
        exit();
    }

    @Override
    public void onNoImageError() {
        setEnableFields(true);
        Toast.makeText(this, getResources().getString(R.string.toast_no_image_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddPostSuccessful() {
        Toast.makeText(this, getResources().getString(R.string.post_created_successful), Toast.LENGTH_SHORT).show();
        exit();
    }

    @Override
    public void onPermissionsError() {
        setEnableFields(true);
        Toast.makeText(this, getResources().getString(R.string.toast_permission_error), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onAddImageError(String error) {
        setEnableFields(true);
        Toast.makeText(this, getResources().getString(R.string.toast_add_image_error)+error, Toast.LENGTH_LONG).show();
    }
}
