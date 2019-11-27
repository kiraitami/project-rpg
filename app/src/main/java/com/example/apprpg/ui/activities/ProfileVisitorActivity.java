package com.example.apprpg.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apprpg.interfaces.ProfileVisitorContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.R;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.apprpg.utils.StringNodes.NODE_CHARACTER;

public class ProfileVisitorActivity extends AppCompatActivity
        implements ProfileVisitorContract.ProfileVisitorView {


    private User guestUser;
    private Character hostCharacter = null, guestCharacter = null;
    private String hostCharacterId;
    private ImageView cover_image;
    private CircleImageView profile_picture;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsing_toolbar_l;
    private TextView bio_description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_visitor);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getIntentData();
        setViewsById();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadHostCharacterFromFirebase();

        clickEventActions();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getIntentData() {
        Bundle data = getIntent().getExtras();
        guestUser = (User) data.getSerializable(getResources().getString(R.string.user_object));
        guestCharacter = (Character) data.getSerializable(getResources().getString(R.string.character_object));
        hostCharacter = (Character) data.getSerializable(getResources().getString(R.string.host_character_object));
        hostCharacterId = data.getString(getResources().getString(R.string.host_character_id));
    }

    @Override
    public void setViewsById() {
        cover_image = findViewById(R.id.cover_image_visitor_profile);
        profile_picture = findViewById(R.id.profile_picture_visitor_profile);
        toolbar = findViewById(R.id.toolbar_inside_collapsing_visitor_profile);
        collapsing_toolbar_l = findViewById(R.id.collapsing_toolbar_visitor_profile);
        bio_description = findViewById(R.id.profile_bio_visitor_profile);
    }

    @Override
    public void clickEventActions() {

    }

    @Override
    public void loadHostCharacterFromFirebase(){
        if (hostCharacter == null) {
            DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
            databaseReference.child(NODE_CHARACTER)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot usersIds : dataSnapshot.getChildren()) {
                                if (usersIds.child(hostCharacterId).exists()) {
                                    hostCharacter = usersIds.child(hostCharacterId).getValue(Character.class);
                                    onLoadHostSuccess();
                                }
                            }
                            if (hostCharacter == null) {
                                onLoadHostFailed();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        else {
            showData();
        }
    }

    @Override
    public void onLoadHostSuccess() {
        showData();
    }

    @Override
    public void onLoadHostFailed() {
        // todo: Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        exit();
    }

    @Override
    public void showData() {
        Glide.with(getApplicationContext())
                .load(hostCharacter.getProfilePictureUrl())
                .thumbnail(0.2f)
                .into(profile_picture);

        Glide.with(getApplicationContext())
                .load(hostCharacter.getCoverPictureUrl())
                .thumbnail(0.2f)
                .into(cover_image);

        collapsing_toolbar_l.setTitle(hostCharacter.getName());
        StringHelper.formatToDescription(hostCharacter.getBioDescription(), bio_description);
    }

    private void exit(){
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.user_object), guestUser);
        intent.putExtra(getResources().getString(R.string.character_object), guestCharacter);
        setResult(RESULT_OK, intent);
        finish();
    }
}
