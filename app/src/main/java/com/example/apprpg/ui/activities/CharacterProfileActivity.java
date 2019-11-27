package com.example.apprpg.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apprpg.interfaces.CharacterProfileContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.CharacterProfilePresenter;
import com.example.apprpg.utils.Permissions;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.R;
import com.example.apprpg.ui.dialog.EditCharacterDialog;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CharacterProfileActivity extends AppCompatActivity
        implements CharacterProfileContract.CharacterProfileView {


    private ImageView cover_image;
    private CircleImageView profile_picture;
    private AlertDialog alertDialog;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsing_toolbar_l;
    private TextView bio_description, edit_bio;
    private EditText input_bio;
    private Button btn_update_bio, btn_update_bio_cancel;
    private NestedScrollView nested_scroll;

    private EditCharacterDialog editCharacterDialog;

    private User user;
    private Character character;

    private CharacterProfilePresenter presenter;

    private Uri selectedCoverImage = null, selectedProfilePicture = null;

    private final int REQUEST_PROFILE_PICTURE_CODE = 2;
    private final int REQUEST_COVER_IMAGE_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_profile);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        presenter = new CharacterProfilePresenter(this);

        getIntentData();
        setViewsById();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadImages();
        showData();
        setClickEventsActions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_character_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_add_profile_picture:
                onAddProfilePictureClick();
                break;

            case R.id.menu_add_cover_image:
                onAddCoverImageClick();
                break;

            case R.id.menu_edit_character:
                onEditCharacterClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null){

            switch (requestCode) {
                case REQUEST_PROFILE_PICTURE_CODE:
                    selectedProfilePicture = data.getData();
                    profile_picture.setImageURI(selectedProfilePicture);
                    presenter.setCanSavePicture(true);
                    break;

                case REQUEST_COVER_IMAGE_CODE:
                    selectedCoverImage = data.getData();
                    cover_image.setImageURI(selectedCoverImage);
                    presenter.setCanSaveCover(true);
                    break;
            }

            makeSaveEditionsSnackBar();
        }
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
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        character.saveInFirebase();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.destroyView();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void getIntentData() {
        Bundle data = getIntent().getExtras();
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
    }

    @Override
    public void showData() {
        collapsing_toolbar_l.setTitle(character.getName());
        StringHelper.formatToDescription(character.getBioDescription(), bio_description);
    }

    @Override
    public void loadImages(){
        Glide.with(this)
                .load(character.getProfilePictureUrl())
                .thumbnail(0.2f)
                .override(720,720)
                .into(profile_picture);

        Glide.with(this)
                .load(character.getCoverPictureUrl())
                .override(1080,720)
                .thumbnail(0.2f)
                .into(cover_image);
    }


    private void setViewsById(){
        cover_image = findViewById(R.id.cover_image_char_prof);
        profile_picture = findViewById(R.id.profile_picture_char_prof);
        toolbar = findViewById(R.id.toolbar_inside_collapsing_char_prof);
        collapsing_toolbar_l = findViewById(R.id.collapsing_toolbar_char_prof);
        bio_description = findViewById(R.id.profile_bio);
        edit_bio = findViewById(R.id.edit_profile_bio);
        input_bio = findViewById(R.id.input_profile_bio);
        btn_update_bio = findViewById(R.id.btn_update_bio);
        btn_update_bio_cancel = findViewById(R.id.btn_update_bio_cancel);
        nested_scroll = findViewById(R.id.nested_scroll_character_profile);
    }

    private void setClickEventsActions(){
        btn_update_bio.setOnClickListener(view -> {
            character.setBioDescription(presenter.updateBiography(input_bio.getText().toString()));
            StringHelper.formatToDescription(character.getBioDescription(),bio_description);
            setEditBioVisibility(false);
        });

        btn_update_bio.setOnClickListener(view -> {
            character.setBioDescription(presenter.updateBiography(input_bio.getText().toString()));
            StringHelper.formatToDescription(character.getBioDescription(),bio_description);
            setEditBioVisibility(false);
        });

        edit_bio.setOnClickListener(view -> {
            input_bio.setText(character.getBioDescription());
            setEditBioVisibility(true);
        });

    }

    private void makeSaveEditionsSnackBar(){
        Snackbar.make(nested_scroll, getResources().getString(R.string.snackbar_update_profile_images),Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.snackbar_update_profile_images_btn), view -> {
                    onLoadSaving();
                    presenter.addCoverPicture(selectedCoverImage, user, character);
                    presenter.addProfilePicture(selectedProfilePicture, user, character);
                })
                .setActionTextColor(Color.WHITE)
                .show();
    }

    private void setEditBioVisibility(boolean visible){
        if (visible) {
            input_bio.setHint(getResources().getString(R.string.dialog_hint_input_bio, character.getName()));
            input_bio.setVisibility(View.VISIBLE);
            btn_update_bio_cancel.setVisibility(View.VISIBLE);
            btn_update_bio.setVisibility(View.VISIBLE);
            bio_description.setVisibility(View.GONE);
        }
        else {
            input_bio.setVisibility(View.GONE);
            btn_update_bio_cancel.setVisibility(View.GONE);
            btn_update_bio.setVisibility(View.GONE);
            bio_description.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void configurePermissions() {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        Permissions.validattePermissions(permissions, this, REQUEST_COVER_IMAGE_CODE);
    }

    @Override
    public void onAddCoverImageClick() {
        configurePermissions();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_COVER_IMAGE_CODE);
    }

    @Override
    public void onAddProfilePictureClick() {
        configurePermissions();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PROFILE_PICTURE_CODE);
    }

    @Override
    public void onEditCharacterClick() {
        editCharacterDialog = new EditCharacterDialog(this, character);
        editCharacterDialog.setOnDismissListener(dialogInterface -> {
            if (editCharacterDialog.getSuccess()){
                character = editCharacterDialog.getCharacter();
                onEditCharacterSuccessful();
            }
        });
        editCharacterDialog.show();
    }

    @Override
    public void onPermissionsError() {
        Toast.makeText(this, getResources().getString(R.string.toast_permission_error), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onLoadSaving() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder
                .setTitle(getResources().getString(R.string.alert_title_loading))
                .setView(getLayoutInflater().inflate(R.layout.dialog_loading,null))
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    @Override
    public void updateCharacterCoverUrl(String coverUrl) {
        character.setCoverPictureUrl(coverUrl);
    }

    @Override
    public void updateCharacterProfileUrl(String profileUrl) {
        character.setProfilePictureUrl(profileUrl);
    }

    @Override
    public void onEditCharacterSuccessful() {
        Toast.makeText(this, getResources().getString(R.string.character_edited_successful), Toast.LENGTH_SHORT).show();
        showData();
    }

    @Override
    public void onImageSaveSuccessful() {
        Toast.makeText(this, getResources().getString(R.string.toast_update_image_successful), Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
        loadImages();
    }

    @Override
    public void onSavingFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
