package com.zohologin.demo.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zohologin.demo.Validation.CheckValue;
import com.zohologin.demo.ui.MainActivity;

/**
 * Created by jaivignesh.m.jt on 9/25/2016.
 */
public class SessionManager {
    Context _context;
    Editor editor;
    SharedPreferences pref;
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_Name = "id";
    String userId;
    Boolean isLoggedIn;

    public SessionManager(Context context) {
        this._context = context;
        pref = context.getSharedPreferences("SessionPref", context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String id) {
        editor.clear();
        editor.commit();

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_Name, id);
        editor.commit();
    }


    public String fetchUser() {
        userId = pref.getString("id", "");
        Session session = new Session();
        if (CheckValue.isValid(userId)) {
            userId = userId;
        }
        return userId;
    }

    public Boolean isUserLoggedIn() {
        isLoggedIn = pref.getBoolean(IS_LOGIN, false);
        return isLoggedIn;
    }

    public void logoutUser(Context context) {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


}
