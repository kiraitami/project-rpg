package com.example.apprpg.ui.activities;


/*
11-2019

Made by L
This is my first App and I hope you enjoy it
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;


import com.example.apprpg.R;
import com.google.firebase.database.DatabaseReference;

import cat.ereza.customactivityoncrash.config.CaocConfig;

public class FirstActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*
        CaocConfig.Builder.create()
                .trackActivities(true)
                .restartActivity(FirstActivity.class)
                .errorDrawable(R.drawable.ic_very_dissatisfied_240dp)
                .apply();
*/
       // validateVersion();
        goToLoginActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void validateVersion(){
        /*
        databaseReference = FirebaseHelper.getFirebaseRef().child(StringNodes.NODE_VERSION_CONTROL);
        databaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (b && dataSnapshot != null) {
                    long versionInFireBase = (long) dataSnapshot.getValue();
                    if (versionInFireBase == 0 || versionInFireBase > BuildConfig.VERSION_CODE ) {
                        exit();
                    } else {
                        goToLoginActivity();
                    }
                }
                else if (databaseError != null){
                    Toast.makeText(FirstActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
                else {
                    exit();
                }
            }
        });
        */
    }

    private void goToLoginActivity(){
        ImageView fade = findViewById(R.id.first_bg);
        fade.setAlpha(0f);
        fade.animate().setDuration(2000).alpha(1f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity(new Intent(FirstActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.alert_version_error_title))
                .setMessage(getResources().getString(R.string.alert_version_error_msg))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.alert_version_error_positive), (dialogInterface, i) -> finishAndRemoveTask());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
