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

import com.example.apprpg.interfaces.ItemsDetailsContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.InventoryItem;
import com.example.apprpg.models.User;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;


public class ItemDetailsActivity extends AppCompatActivity
        implements ItemsDetailsContract.ItemsDetailsView {


    private TextView description, amount;
    private ViewGroup content_layout;
    private Toolbar toolbar;

    private User user;
    private Character character;
    private InventoryItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        getIntentData();
        setViewsById();
        showData();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        if (item!= null)
            item.saveInFirebase();
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
            item = (InventoryItem) data.getSerializableExtra(getResources().getString(R.string.item_object));
            showData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle( getResources().getString( item.getFavorite() > 0 ?
                R.string.menu_unfavorite_item_title :
                R.string.menu_favorite_item_title ));
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
    public void exitWithAnimation() {
        long screenWidth = ScreenDimensHelper.getScreenWidth(getWindowManager());
        content_layout.animate().translationX(screenWidth).setDuration(350).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = getIntent();
                intent.putExtra(getResources().getString(R.string.user_object), user);
                intent.putExtra(getResources().getString(R.string.character_object), character);
                intent.putExtra(getResources().getString(R.string.fragment_object), StringNodes.FRAGMENT_ITEMS);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
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
        description = findViewById(R.id.item_details_description);
        amount = findViewById(R.id.item_details_amount);
        content_layout = findViewById(R.id.linear_layout_item_details);
        toolbar = findViewById(R.id.toolbar_item_details);
    }

    @Override
    public void onFavoriteClick() {
        item.setFavorite( item.getFavorite() > 0 ? 0 : 1 );
    }

    @Override
    public void onEditClick() {
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.item_object), item);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onDeleteClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getResources().getString(R.string.alert_title_delete, item.getName());
        builder.setTitle(title)
                .setMessage(getResources().getString(R.string.alert_msg_delete))
                .setPositiveButton(getResources().getString(R.string.alert_positive_button_delete), (dialogInterface, i) -> {
                    item.deleteFromFirebase();
                    item = null;
                    Toast.makeText(this, getResources().getString(R.string.item_deleted_successful), Toast.LENGTH_SHORT).show();
                    exitWithAnimation();
                })
                .setNegativeButton(getResources().getString(R.string.alert_negative_button_delete), null)
                .create()
                .show();
    }


    @Override
    public void showData() {
        toolbar.setTitle(item.getName());
        StringHelper.formatToDescription(item.getDescription(), description);
        amount.setText(String.valueOf(item.getAmount()));
    }

}
