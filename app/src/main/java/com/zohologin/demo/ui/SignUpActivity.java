package com.zohologin.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zohologin.demo.R;
import com.zohologin.demo.Validation.CheckValue;
import com.zohologin.demo.database.MyDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends Activity {
    Window window;
    @Bind(R.id.joinTab)
    Button joinButton;
    @Bind(R.id.signInTabLogin)
    Button signInTabButton;
    @Bind(R.id.userName)
    EditText name;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.phoneNumber)
    EditText phoneNumber;
    @Bind(R.id.createAccount)
    Button createAccount;
    String nameValue, emailValue, phoneNumberValue, passwordValue;
    private Pattern pattern;
    private Matcher matcher;
    MyDatabase dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        dbh = new MyDatabase(SignUpActivity.this, MyDatabase.DATABASE_NAME, null, MyDatabase.database_VERSION);
        dbh.getReadableDatabase();
        dbh.getWritableDatabase();
        email.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        joinButton.setEnabled(false);
        window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        final String PASSWORD_PATTERN = "((?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%]).{6,20})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    @OnClick(R.id.createAccount)
    void setCreateAccount() {
        nameValue = name.getText().toString();
        emailValue = email.getText().toString();
        phoneNumberValue = phoneNumber.getText().toString();
        passwordValue = password.getText().toString();
        matcher = pattern.matcher(passwordValue);
        name.setError(null);
        email.setError(null);
        password.setError(null);
        phoneNumber.setError(null);
        if (nameValue.trim().length() == 0) {
            name.setError(getString(R.string.empty_field));
            name.requestFocus();
        } else if (emailValue.trim().length() == 0) {
            email.setError(getString(R.string.empty_field));
            email.requestFocus();
        } else if (!CheckValue.isEmailValid(emailValue)) {
            email.setError(getString(R.string.invalid_email));
            email.requestFocus();
        } else if (passwordValue.trim().length() == 0) {
            password.setError(getString(R.string.empty_field));
            password.requestFocus();
        } else if (!CheckValue.isPsswordValid(passwordValue)) {
            password.setError(getString(R.string.password_restrication));
            password.requestFocus();
        } else if (phoneNumberValue.trim().length() == 0) {
            phoneNumber.setError(getString(R.string.empty_field));
            phoneNumber.requestFocus();
        } else if (phoneNumberValue.trim().length() != 10) {
            phoneNumber.setError(getString(R.string.invalid_phone));
            phoneNumber.requestFocus();
        } else {
            if (!dbh.isEmailExists(emailValue)) {
                Boolean loginInsert = dbh.insert_login_details(nameValue, emailValue, passwordValue, phoneNumberValue);
                dbh.close();
                if (loginInsert) {
                    Toast.makeText(SignUpActivity.this, R.string.reg_succ, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                    SignUpActivity.this.finish();
                    SignUpActivity.this.startActivity(i);
                } else {
                    Toast.makeText(SignUpActivity.this, R.string.reg_fail, Toast.LENGTH_LONG).show();
                }
            } else {
                View view = SignUpActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                Toast.makeText(SignUpActivity.this, R.string.alreadyUser, Toast.LENGTH_LONG).show();
            }

        }
    }

    @OnClick(R.id.signInTabLogin)
    void signInNavigation() {
        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
        SignUpActivity.this.finish();
        startActivity(i);
        overridePendingTransition(R.anim.enter, R.anim.exit);

    }
}
