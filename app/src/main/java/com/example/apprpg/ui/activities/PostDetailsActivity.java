package com.example.apprpg.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apprpg.interfaces.PostDetailsContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Comment;
import com.example.apprpg.models.Post;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.PostDetailsPresenter;
import com.example.apprpg.utils.FirebaseHelper;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;
import com.example.apprpg.ui.adapters.CommentsAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailsActivity extends AppCompatActivity
        implements PostDetailsContract.PostDetailsView {


    private ImageView post_image;
    private LinearLayout add_comment_layout, post_owner_layout, likers_layout;
    private LikeButton btn_like;
    private CircleImageView character_picture, visitor_picture;
    private Toolbar toolbar;
    private TextView post_title, post_description, user_name, character_name, likes_count, post_edited, post_date;
    private RecyclerView comments_recycler;
    private AlertDialog alertDialog;

    private ValueEventListener commentEventListener;
    private DatabaseReference databaseReference;

    private PostDetailsPresenter presenter;

    private User user;
    private Character character;
    private Post post;
    private static final int EDIT_POST_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        presenter = new PostDetailsPresenter(this);

        getIntentData();
        setViewsById();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setClickListenersEvents();
        showPostData();

    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCommentsFromFirebase();
    }

    @Override
    protected void onPause() {
        databaseReference.removeEventListener(commentEventListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.destroyView();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            user = (User) data.getSerializableExtra(getResources().getString(R.string.user_object));
            character = (Character) data.getSerializableExtra(getResources().getString(R.string.character_object));
            if (requestCode == EDIT_POST_REQUEST_CODE){
                post = (Post) data.getSerializableExtra(getResources().getString(R.string.post_object));
            }
            showPostData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.menu_group_edit_post, character.getId().equals(post.getCharacterId()));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.op_menu_edit_post:
                goToEditPost();
                break;
            case R.id.op_menu_delete_post:
                deletePostDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setViewsById(){
        post_image = findViewById(R.id.post_details_image);
        toolbar = findViewById(R.id.toolbar_inside_collapsing_post_details);
        post_title = findViewById(R.id.title_post_details);
        post_description = findViewById(R.id.desc_post_details);
        character_picture = findViewById(R.id.profile_picture_post_details);
        user_name = findViewById(R.id.player_name_post_details);
        character_name = findViewById(R.id.character_name_post_details);
        likes_count = findViewById(R.id.tv_likes_count_post_details);
        comments_recycler = findViewById(R.id.comments_recycler);
        btn_like = findViewById(R.id.button_like_post_details);
        add_comment_layout = findViewById(R.id.add_comment_layout);
        visitor_picture = findViewById(R.id.visitor_profile_picture);
        post_owner_layout = findViewById(R.id.post_owner_layout);
        likers_layout = findViewById(R.id.likers_layout);
        post_edited = findViewById(R.id.post_edited_details);
        post_date = findViewById(R.id.date_post_details);
    }

    private void setClickListenersEvents(){
        btn_like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                post.like(character.getName());
                post.updateLikersInFirebase(post.getLikersList());
                likes_count.setText(String.valueOf(post.getLikersList().size()));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                post.unlike(character.getName());
                post.updateLikersInFirebase(post.getLikersList());
                likes_count.setText(String.valueOf(post.getLikersList().size()));
            }
        });

        add_comment_layout.setOnClickListener(view -> {
            presenter.requestComment(character.getCanPost());
        });

        likers_layout.setOnLongClickListener(view -> {
            showLikersDialog();
            return false;
        });

        post_owner_layout.setOnClickListener(view -> {
            if (!post.getCharacterId().equals(character.getId())) {
                visitDialog(post.getCharacterId(), post.getCharacterName());
            }
        });

    }

    private void deletePostDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.alert_delete_post_title))
                .setMessage(getResources().getString(R.string.alert_delete_post_msg))
                .setPositiveButton(getResources().getString(R.string.alert_delete_post_positive), (dialogInterface, i) -> {
                    post.deleteFromFirebase();
                    Toast.makeText(this, getResources().getString(R.string.toast_delete_post_success), Toast.LENGTH_SHORT).show();
                    exit();
                })
                .setNegativeButton(getResources().getString(R.string.alert_delete_post_negative), null)
                .create();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void goToEditPost(){
        Intent intent = new Intent(PostDetailsActivity.this, AddPostActivity.class);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        intent.putExtra(getResources().getString(R.string.post_object), post);
        startActivityForResult(intent, EDIT_POST_REQUEST_CODE);
    }

    private void exit(){
        Intent intent = getIntent();
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void getIntentData() {
        Bundle data = getIntent().getExtras();
        post = (Post) Objects.requireNonNull(data).getSerializable(getResources().getString(R.string.post_object));
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
    }

    @Override
    public void showPostData() {
        Glide.with(getApplicationContext()).load(post.getImageUrl()).override(720, 720).thumbnail(0.1f).into(post_image);
        Glide.with(getApplicationContext()).load(post.getCharacterPictureUrl()).thumbnail(0.1f).into(character_picture);
        Glide.with(getApplicationContext()).load(character.getProfilePictureUrl()).thumbnail(0.1f).into(visitor_picture);

        post_title.setText(post.getTitle());
        post_date.setText(post.getPublicationDate().formatMyDateToString(getResources().getString(R.string.date_post_pattern)));
        post_edited.setVisibility(post.getWasEdited() ? View.VISIBLE : View.GONE);
        StringHelper.formatToDescription(post.getDescription(), post_description);
        user_name.setText(post.getUserName());
        character_name.setText(post.getCharacterName());
        btn_like.setLiked( post.getLikersList().contains(character.getName()) );
        likes_count.setText( String.valueOf(post.getLikersList().size()) );

    }

    @Override
    public void loadCommentsFromFirebase() {
        databaseReference = FirebaseHelper.getFirebaseRef().child(StringNodes.NODE_COMMENT).child(post.getId());
        commentEventListener = databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Comment> commentList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            Comment comment = data.getValue(Comment.class);
                            commentList.add(comment);
                        }
                        showCommentsList(commentList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void commentAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_comment, null);
        EditText input_comment = view.findViewById(R.id.input_comment_dialog);
        Button btn_comment = view.findViewById(R.id.button_comment_dialog);
        String hint = getResources().getString(R.string.hint_type_a_comment, character.getName());
        input_comment.setHint(hint);
        builder.setView( view );

        btn_comment.setOnClickListener(view1 -> {
            presenter.validateComment( input_comment.getText().toString(), user, character, post.getId() );
        });

        alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

    }

    @Override
    public void visitDialog(String hostCharacterId, String hostCharacterName){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialog_visit_title, hostCharacterName))
                .setMessage(getResources().getString(R.string.dialog_visit_msg, hostCharacterName))
                .setPositiveButton(getResources().getString(R.string.dialog_visit_confirm),(dialogInterface, i) -> {
                    Intent intent = new Intent(PostDetailsActivity.this, ProfileVisitorActivity.class);
                    intent.putExtra(getResources().getString(R.string.user_object), user);
                    intent.putExtra(getResources().getString(R.string.character_object), character);
                    intent.putExtra(getResources().getString(R.string.host_character_object), hostCharacterId);
                    startActivityForResult(intent, 0);
                })
                .setNegativeButton(getResources().getString(R.string.dialog_visit_cancel), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void showLikersDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_likers_list, null);
        TextView likers_names = view.findViewById(R.id.tv_dialog_likers_names);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.dialog_likers_title)).append("\n\n");
        for (String name : post.getLikersList()){
            stringBuilder.append(name).append("\n");
        }
        likers_names.setText(stringBuilder.toString());

        builder.setView( view );

        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    @Override
    public void showCommentsList(List<Comment> commentList) {
        comments_recycler.setHasFixedSize(true);
        comments_recycler.setLayoutManager(new LinearLayoutManager(this));
        comments_recycler.setAdapter(new CommentsAdapter( Glide.with(getApplicationContext()), this, commentList, character.getName() ));
    }

    @Override
    public void onCommentSuccessful() {
        alertDialog.dismiss();
        Toast.makeText(this, getResources().getString(R.string.alert_empty_comment_successful), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentEmptyField() {
        Toast.makeText(this, getResources().getString(R.string.alert_empty_comment_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCantComment() {
        Toast.makeText(this, getResources().getString(R.string.toast_cant_comment), Toast.LENGTH_LONG).show();
    }
}
