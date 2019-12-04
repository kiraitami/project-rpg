package com.example.apprpg.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprpg.interfaces.LoginContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.LoginPresenter;
import com.example.apprpg.utils.ScreenDimensHelper;
import com.example.apprpg.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity
    implements LoginContract.LoginView {

    private TextInputEditText input_email_register, input_password_register, input_confirm_password, input_nickname, input_email_login, input_password_login;
    private TextInputLayout input_email_register_l, input_password_register_l, input_confirm_password_l, input_nickname_l, input_email_login_l, input_password_login_l;
    private ImageButton btn_login, btn_register;
    private ImageView bg_fade;
    private TextView tv_swap_login_to_register, tv_swap_register_to_login, tv_forgot_password;
    private ViewGroup content_login, content_register;
    private ProgressBar progress_login, progress_register;

    private LoginPresenter loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setViewsById();
        setViewsConfig();
        setClickListenersEvents();

        loginPresenter = new LoginPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.base_key_pref),MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString(getResources().getString(R.string.login_key_pref),null);
        input_email_login.setText(savedEmail);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.destroyView();
        loginPresenter = null;
        super.onDestroy();
    }

    private void setViewsById(){
        input_email_login_l = findViewById(R.id.input_email_login_l);
        input_email_login = findViewById(R.id.input_email_login);
        input_password_login = findViewById(R.id.input_password_login);
        input_password_login_l = findViewById(R.id.input_password_login_l);
        input_email_register = findViewById(R.id.input_email_register);
        input_password_register = findViewById(R.id.input_password_register);
        input_confirm_password = findViewById(R.id.input_confirm_password);
        input_nickname = findViewById(R.id.input_nickname);
        input_email_register_l = findViewById(R.id.input_email_l);
        input_password_register_l = findViewById(R.id.input_password_l);
        input_confirm_password_l = findViewById(R.id.input_confirm_password_l);
        input_nickname_l = findViewById(R.id.input_nickname_l);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        tv_swap_login_to_register = findViewById(R.id.tv_swap_login_to_register);
        tv_swap_register_to_login = findViewById(R.id.tv_swap_register_to_login);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        content_login = findViewById(R.id.linear_layout_login);
        content_register = findViewById(R.id.linear_layout_register);
        progress_login = findViewById(R.id.progress_bar_login);
        progress_register = findViewById(R.id.progress_bar_register);
        bg_fade = findViewById(R.id.bg_fade_login);
    }

    private void setViewsConfig() {
        input_confirm_password_l.setPasswordVisibilityToggleEnabled(true);
        input_password_register_l.setPasswordVisibilityToggleEnabled(true);
        input_password_login_l.setPasswordVisibilityToggleEnabled(true);
    }

    private void setClickListenersEvents(){
        tv_swap_login_to_register.setOnClickListener(view -> {
            clearAllFields();
            clearAllErrors();
            swapContentWithAnimation(content_login, content_register);
        });

        tv_swap_register_to_login.setOnClickListener(view -> {
            clearAllFields();
            clearAllErrors();
            swapContentWithAnimation(content_register, content_login);
        });

        btn_login.setOnClickListener(view -> {
            clearAllErrors();
            loginPresenter.authenticateLogin(
                    input_email_login.getText().toString(),
                    input_password_login.getText().toString()
            );
        });

        btn_register.setOnClickListener(view -> {
            clearAllErrors();
            loginPresenter.createNewUser(
                    input_nickname.getText().toString(),
                    input_email_register.getText().toString(),
                    input_password_register.getText().toString(),
                    input_confirm_password.getText().toString()
            );
        });

        tv_forgot_password.setOnClickListener(view -> {
            loginPresenter.forgotPassword(input_email_login.getText().toString());
        });
    }

    private void loginAnimationStart(){
        long screenWidth = ScreenDimensHelper.getScreenWidth(getWindowManager());

        content_login.animate().translationX(screenWidth).setDuration(350).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                content_login.setVisibility(View.GONE);

                bg_fade.setAlpha(0f);
                bg_fade.setVisibility(View.VISIBLE);
                bg_fade.animate().alpha(1f).setDuration(500).setListener(null);
            }
        });

    }

    private void swapContentWithAnimation(ViewGroup currentViewGroup, ViewGroup desiredViewGroup){
        long screenWidth = ScreenDimensHelper.getScreenWidth(getWindowManager());

        currentViewGroup.animate().translationX(screenWidth).setDuration(350).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentViewGroup.setVisibility(View.GONE);

                desiredViewGroup.setTranslationX(screenWidth);
                desiredViewGroup.animate().translationX(0f).setDuration(350).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        desiredViewGroup.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void clearAllFields(){
        input_nickname.setText(null);
        input_email_login.setText(null);
        input_email_register.setText(null);
        input_password_login.setText(null);
        input_password_register.setText(null);
        input_confirm_password.setText(null);
    }

    private void clearAllErrors(){
        input_email_login_l.setError(null);
        input_password_login_l.setError(null);
        input_email_register_l.setError(null);
        input_password_register_l.setError(null);
        input_confirm_password_l.setError(null);
        input_nickname_l.setError(null);
    }

    private void setEnableFields(boolean enableFields){
        input_email_login.setEnabled(enableFields);
        input_password_login.setEnabled(enableFields);
        btn_login.setEnabled(enableFields);

        input_nickname.setEnabled(enableFields);
        input_email_register.setEnabled(enableFields);
        input_password_register.setEnabled(enableFields);
        input_confirm_password.setEnabled(enableFields);
        btn_register.setEnabled(enableFields);

        if (enableFields){
            progress_login.setVisibility(View.GONE);
            progress_register.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
            btn_register.setVisibility(View.VISIBLE);
        }
        else {
            progress_login.setVisibility(View.VISIBLE);
            progress_register.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);
            btn_register.setVisibility(View.GONE);
        }
    }


    @Override
    public void onAuthenticatingLogin() {
        setEnableFields(false);
    }

    @Override
    public void onAuthenticatingLoginFinished() {
        setEnableFields(true);
    }

    @Override
    public void onWrongEmailOrPassword() {
        input_email_login_l.setError(getResources().getString(R.string.wrong_email_or_password_error));
        input_password_login_l.setError(getResources().getString(R.string.wrong_email_or_password_error));
        tv_forgot_password.setVisibility(View.VISIBLE);
        setEnableFields(true);
    }

    @Override
    public void onNonVerifiedEmail() {
        input_email_login_l.setError(getResources().getString(R.string.non_confirmed_email_error));
        setEnableFields(true);
    }

    @Override
    public void onDuplicatedEmail() {
        input_email_register_l.setError(getResources().getString(R.string.duplicated_email_error));
        setEnableFields(true);
    }

    @Override
    public void onEmptyField() {
        Toast.makeText(this, getResources().getString(R.string.empty_field_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnmatchedPassword() {
        input_confirm_password_l.setError(getResources().getString(R.string.confirm_password_error));
        setEnableFields(true);
    }

    @Override
    public void onUnexistentUser() {
        input_email_login_l.setError(getResources().getString(R.string.unexistent_user_error));
        setEnableFields(true);
    }

    @Override
    public void onExceptionLoginOrRegister(String e) {
        Toast.makeText(this, e, Toast.LENGTH_LONG).show();
        setEnableFields(true);
    }

    @Override
    public void onLoadingUserAndCharacter() {
        loginAnimationStart();
    }

    @Override
    public void onLoginError() {
        Toast.makeText(this, getResources().getString(R.string.toast_on_login_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserHasNoCharacter(User user) {
        Intent intent = new Intent(LoginActivity.this, AddCharacterActivity.class);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginSuccessful(User user, Character character) {
        //remember email in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.base_key_pref),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.login_key_pref), user.getEmail());
        editor.apply();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.user_object), user);
        intent.putExtra(getResources().getString(R.string.character_object), character);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestNewUser() {
        setEnableFields(false);
    }

    @Override
    public void registerSuccessful(User user) {
        clearAllFields();
        clearAllErrors();
        setEnableFields(true);
        Toast.makeText(this, getResources().getString(R.string.user_registered_successful), Toast.LENGTH_SHORT).show();
        swapContentWithAnimation(content_register, content_login);
        input_email_login.setText(user.getEmail());
    }

    @Override
    public void onRegisterFailed() {
        setEnableFields(true);
    }

    @Override
    public void onRecoverAccountError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecoverAccountSuccess() {
        Toast.makeText(this, getResources().getString(R.string.toast_recover_email_send_successful), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInvalidEmail() {
        Toast.makeText(this, getResources().getString(R.string.toast_invalid_email), Toast.LENGTH_SHORT).show();
    }

}
