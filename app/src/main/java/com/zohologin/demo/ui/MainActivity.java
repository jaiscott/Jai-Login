package com.zohologin.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.zohologin.demo.R;
import com.zohologin.demo.Validation.CheckValue;
import com.zohologin.demo.database.MyDatabase;
import com.zohologin.demo.session.SessionManager;


import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {
    Window window;

    @Bind(R.id.signInUserName)
    EditText userNameEditText;
    @Bind(R.id.signInPassword)
    EditText passwordEditText;
    @Bind(R.id.signInButton)
    Button signInButton;
    @Bind(R.id.joinTab)
    Button joinButton;
    @Bind(R.id.signInTabLogin)
    Button signInTahButton;
    private Pattern pattern;
    // private String emailPattern;
    String usernameValue, passwordValue;
    MyDatabase dbh;
    SessionManager sessionManager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private final int MIN_SEESION_DURATION = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        signInTahButton.setEnabled(false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setMinimumSessionDuration(MIN_SEESION_DURATION);

        dbh = new MyDatabase(MainActivity.this, MyDatabase.DATABASE_NAME, null, MyDatabase.database_VERSION);
        dbh.getReadableDatabase();

        sessionManager = new SessionManager(getApplicationContext());

        window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    @OnClick(R.id.signInButton)
    void signIn() {
        String userName, password;
        userName = userNameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        passwordEditText.setError(null);
        userNameEditText.setError(null);
        if (CheckValue.isValid(userName) && userName.length() == 0) {
            userNameEditText.setError(getString(R.string.empty_field));
        } else if (userName.length() > 0 && !CheckValue.isEmailValid(userNameEditText.getText().toString())) {
            userNameEditText.setError(getString(R.string.invalid_email));
        } else if (CheckValue.isValid(password) && password.length() == 0) {
            passwordEditText.setError(getString(R.string.empty_field));
        } else {
            if (CheckValue.isValid(userName) && CheckValue.isValid(password)) {
                String loginId = dbh.userLogin(userName, password);
                if (CheckValue.isValid(loginId)) {
                    sessionManager.createLoginSession(loginId);
                    Intent i = new Intent(MainActivity.this, LoginWelcomeActivity.class);
                    i.putExtra("UserId", loginId);
                    MainActivity.this.finish();
                    MainActivity.this.startActivity(i);
                } else {
                    View view = MainActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    if (dbh.isEmailExists(userName)) {
                        Toast.makeText(MainActivity.this, R.string.invalid_pass, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.invalid_user, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }


    @OnClick(R.id.joinTab)
    void joinInNavigation() {
        Intent i = new Intent(MainActivity.this, SignUpActivity.class);
        MainActivity.this.finish();
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
