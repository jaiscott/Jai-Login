package com.zohologin.demo.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zohologin.demo.R;
import com.zohologin.demo.Validation.CheckValue;
import com.zohologin.demo.database.MyDatabase;
import com.zohologin.demo.session.Session;
import com.zohologin.demo.session.SessionManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginWelcomeActivity extends Activity {
    @Bind(R.id.signOut)
    Button signOut;
    @Bind(R.id.nameValue)
    TextView tvName;
    @Bind(R.id.emailValue)
    TextView tvEmail;
    @Bind(R.id.phoneNumerValue)
    TextView tvPhoneNumber;
    SessionManager sessionManager;
    String userId;
    Session session;
    MyDatabase dbh;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_welcome);
        ButterKnife.bind(this);

        window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        sessionManager = new SessionManager(getApplicationContext());
        userId = sessionManager.fetchUser();

        dbh = new MyDatabase(LoginWelcomeActivity.this, MyDatabase.DATABASE_NAME, null, MyDatabase.database_VERSION);
        dbh.getReadableDatabase();

        //fetch user datas
        if (CheckValue.isValid(userId)) {
            List<Session> sessionValues = dbh.fetchUserDetails(userId);
            session = new Session();
            tvName.setText(sessionValues.get(0).getName());
            tvEmail.setText(sessionValues.get(0).getEmail());
            tvPhoneNumber.setText(sessionValues.get(0).getPhoneNumber());
        }
    }

    @OnClick(R.id.signOut)
    void signOut() {
        sessionManager.logoutUser(getApplicationContext());
        LoginWelcomeActivity.this.finish();
    }
}
