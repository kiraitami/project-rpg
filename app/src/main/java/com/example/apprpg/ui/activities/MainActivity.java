package com.example.apprpg.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apprpg.interfaces.MainActivityContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.MainActivityPresenter;
import com.example.apprpg.ui.fragments.AllCharactersFragment;
import com.example.apprpg.utils.NetworkUtils;
import com.example.apprpg.R;
import com.example.apprpg.ui.fragments.AttributesFragment;
import com.example.apprpg.ui.fragments.ExperienceFragment;
import com.example.apprpg.ui.fragments.ItemsFragment;
import com.example.apprpg.ui.fragments.MahoFragment;
import com.example.apprpg.ui.fragments.MyCharactersFragment;
import com.example.apprpg.ui.fragments.NotesFragment;
import com.example.apprpg.ui.fragments.SkillsFragment;
import com.example.apprpg.ui.fragments.TavernFragment;
import com.example.apprpg.ui.fragments.WeaponsFragment;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainActivityContract.MainActivityView {


    private ImageView cover_image;
    private CircleImageView profile_picture;
    private TextView nav_header_title, nav_header_sub_title;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private Toolbar toolbar;

    private Fragment targetFragment, fragmentBeforeMyCharacters;

    private User user;
    private Character character = null;

    private MainActivityPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        presenter = new MainActivityPresenter(this);

        getIntentData();
        setViewsById();
        setSupportActionBar(toolbar);
        setClickEventsActions();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setNavHeaderWithCharacterData();

        fragmentBeforeMyCharacters = new TavernFragment();
        targetFragment = new TavernFragment();
        presenter.updateFragment(getResources().getString(R.string.default_fragment));


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetworkConnection();
    }

    @Override
    protected void onDestroy() {
        presenter.destroyView();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void checkNetworkConnection() {
        if (!NetworkUtils.isOnline(this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.toast_offline_title))
                    .setMessage(getResources().getString(R.string.toast_offline_msg))
                    .setPositiveButton(getResources().getString(R.string.toast_offline_positive), (dialogInterface, i) -> {
                        checkNetworkConnection();
                    })
                    .setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    /*
    Receiving and Updating User and Character Object across the Fragments
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null){

            character = (Character) data.getSerializableExtra(getResources().getString(R.string.character_object));
            user = (User) data.getSerializableExtra(getResources().getString(R.string.user_object));
            String fragmentName = data.getStringExtra(getResources().getString(R.string.fragment_object));
            presenter.updateFragment(fragmentName != null ? fragmentName : getResources().getString(R.string.default_fragment));
            setNavHeaderWithCharacterData();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_attributes:
                setFragment(new AttributesFragment());
                break;

            case R.id.nav_xp:
                setFragment(new ExperienceFragment());
                break;

            case R.id.nav_notes:
                setFragment(new NotesFragment());
                break;

            case R.id.nav_tavern:
                setFragment(new TavernFragment());
                break;

            case R.id.nav_items:
                setFragment(new ItemsFragment());
                break;

            case R.id.nav_weapons:
                setFragment(new WeaponsFragment());
                break;

            case R.id.nav_skills:
                setFragment(new SkillsFragment());
                break;

            case R.id.nav_magic:
                setFragment(new MahoFragment());
                break;

            case R.id.nav_change_character:
                targetFragment = new MyCharactersFragment();
                break;

            case R.id.nav_log_out:
                exitDialog();
                break;
        }

        changeCurrentFragment(targetFragment);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if ( !(targetFragment instanceof TavernFragment) ){
            targetFragment = new TavernFragment();
            changeCurrentFragment(targetFragment);
        }
        else {
            exitDialog();
        }
    }

    private void setViewsById(){
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_main_activity);
        navigationView = findViewById(R.id.nav_view);

        headerView = navigationView.getHeaderView(0);
        profile_picture = headerView.findViewById(R.id.nav_image_profile);
        cover_image = headerView.findViewById(R.id.nav_cover_image);
        nav_header_title = headerView.findViewById(R.id.nav_header_title);
        nav_header_sub_title = headerView.findViewById(R.id.nav_header_sub_title);

    }

    private void setClickEventsActions(){
        profile_picture.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CharacterProfileActivity.class);
            intent.putExtra(getResources().getString(R.string.user_object), user);
            intent.putExtra(getResources().getString(R.string.character_object), character);
            startActivityForResult(intent, 4);
        });
    }

    private void setNavHeaderWithCharacterData() {
        if (character.getProfilePictureUrl()!= null){
            Glide.with(this).load(character.getProfilePictureUrl()).into(profile_picture);
        }
        if (character.getCoverPictureUrl()!= null){
            Glide.with(this).load(character.getCoverPictureUrl()).centerCrop().into(cover_image);
        }

        nav_header_title.setText(character.getName());
        nav_header_sub_title.setText(character.getBreed());
    }

    private void exitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.toast_exit_title))
                .setMessage(getResources().getString(R.string.toast_exit_msg))
                .setPositiveButton(getResources().getString(R.string.toast_exit_positive), (dialogInterface, i) -> {
                    finishAndRemoveTask();
                })
                .setNegativeButton(getResources().getString(R.string.toast_exit_negative), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void goToAllCharacters(){
        setFragment(new AllCharactersFragment());
        changeCurrentFragment(targetFragment);
    }

    private void setFragment(Fragment desiredFragment){
        targetFragment = desiredFragment;
        fragmentBeforeMyCharacters = targetFragment;
    }

    @Override
    public void changeCurrentFragment(Fragment fragment){
        checkNetworkConnection();

        if (fragment != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(getResources().getString(R.string.user_object), user);
            bundle.putSerializable(getResources().getString(R.string.character_object), character);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.frame_layout_profile, fragment);
            fragmentTransaction.commit();
        }
    }

    public void backToBeforeMyCharactersFragment(){
        if (fragmentBeforeMyCharacters instanceof MyCharactersFragment){
            changeCurrentFragment(new TavernFragment());
        }
        else {
            changeCurrentFragment(fragmentBeforeMyCharacters);
        }
    }

    @Override
    public void getIntentData() {
        Bundle data = getIntent().getExtras();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
    }

    @Override
    public void updateUserAndCharacter(User user, Character character) {
        this.user = user;
        this.character = character;
        setNavHeaderWithCharacterData();
    }
}
