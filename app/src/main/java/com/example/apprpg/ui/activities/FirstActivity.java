package com.example.apprpg.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.apprpg.BuildConfig;
import com.example.apprpg.R;
import com.example.apprpg.models.VersionControl;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Objects;

import cat.ereza.customactivityoncrash.config.CaocConfig;

import static com.example.apprpg.utils.StringNodes.NODE_VERSION_CONTROL;


/*

Made by Gabriel "L" Kuniyoshi
~November-2019

This is my first App and I hope you enjoy it

*/

public class FirstActivity extends AppCompatActivity {

    private VersionControl versionControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set custom activity on crash
        //There you can get the details if app crashes
        CaocConfig.Builder.create()
                .trackActivities(true)
                .restartActivity(FirstActivity.class)
                .errorDrawable(R.drawable.ic_very_dissatisfied_240dp)
                .apply();

        validateVersion();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void validateVersion(){
        /*
        Search newest version data in Firebase
         * Version number
         * Newest version download link
         * If user can login in app even using an outdated installed version
        */
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef().child(NODE_VERSION_CONTROL);
        databaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (b && dataSnapshot != null) {
                    versionControl = dataSnapshot.getValue(VersionControl.class);

                    if ( versionControl != null &&
                       ( versionControl.getVersionNumber() == 0 || versionControl.getVersionNumber() > BuildConfig.VERSION_CODE ) ) {
                        showOutDatedDialog();
                    }
                    else {
                        goToLoginActivity();
                    }
                }
                else if (databaseError != null){
                    Toast.makeText(FirstActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
                else {
                    showOutDatedDialog();
                }
            }
        });

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

    private void showOutDatedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_deprecated_version, null);
        TextView tv_dialog = dialogView.findViewById(R.id.alert_version_error_body);
        tv_dialog.setText(getResources().getString(R.string.alert_version_error_msg, versionControl.getNewestVersionLink()));

        builder.setView(dialogView)
                .setCancelable(true)
                .setOnDismissListener(dialogInterface -> {
                    if (versionControl.getAllowOutDatedLogin()){
                        goToLoginActivity();
                    }
                    else {
                        finishAndRemoveTask();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }
}
