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

import com.example.apprpg.interfaces.NoteDetailsContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Note;
import com.example.apprpg.models.User;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;


public class NoteDetailsActivity extends AppCompatActivity
        implements NoteDetailsContract.NoteDetailsView {

    private TextView description;
    private ViewGroup content_layout;
    private Toolbar toolbar;

    private User user;
    private Character character;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        getIntentData();
        setViewsById();
        showData();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getResources().getString(R.string.notes));
    }

    @Override
    protected void onPause() {
        if (note!= null)
            note.saveInFirebase();
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
            note = (Note) data.getSerializableExtra(getResources().getString(R.string.note_object));
            showData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle( getResources().getString( note.getFavorite() > 0 ?
                R.string.menu_unfavorite_note_title :
                R.string.menu_favorite_note_title ));
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
        note = (Note) data.getSerializable(getResources().getString(R.string.note_object));
    }

    @Override
    public void setViewsById() {
        description = findViewById(R.id.note_details_description);
        content_layout = findViewById(R.id.linear_layout_note_details);
        toolbar = findViewById(R.id.toolbar_note_details);
    }

    @Override
    public void showData() {
        toolbar.setTitle(note.getName());
        description.setText(note.getDescription());
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
                intent.putExtra(getResources().getString(R.string.fragment_object), StringNodes.FRAGMENT_NOTES);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onFavoriteClick() {
        note.setFavorite( note.getFavorite() > 0 ? 0 : 1 );
    }

    @Override
    public void onEditClick() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.note_object), note);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onDeleteClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getResources().getString(R.string.alert_title_delete, note.getName());
        builder.setTitle(title)
                .setMessage(getResources().getString(R.string.alert_msg_delete))
                .setPositiveButton(getResources().getString(R.string.alert_positive_button_delete), (dialogInterface, i) -> {
                    note.deleteFromFirebase();
                    note = null;
                    Toast.makeText(this, getResources().getString(R.string.note_deleted_successful), Toast.LENGTH_SHORT).show();
                    exitWithAnimation();
                })
                .setNegativeButton(getResources().getString(R.string.alert_negative_button_delete), null)
                .create()
                .show();
    }




}
