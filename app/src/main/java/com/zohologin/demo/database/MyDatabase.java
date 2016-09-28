package com.zohologin.demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zohologin.demo.Validation.CheckValue;
import com.zohologin.demo.session.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaivignesh.m.jt on 9/25/2016.
 */
public class MyDatabase extends SQLiteOpenHelper {
    public static final int database_VERSION = 1;

    public static final String DATABASE_NAME = "LoginCrediantals.db";
    public static final String LOGIN_TABLE_NAME = "LogIn";
    public static final String NAME = "NAME";
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    public static final String PHONENUMBER = "PhoneNumber";

    public MyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_LOGIN = "CREATE TABLE " + LOGIN_TABLE_NAME + "("
                + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT not null," + EMAIL
                + " TEXT not null unique ," + PASSWORD + " TEXT not null," + PHONENUMBER
                + " TEXT not null" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_LOGIN);
    }

    public Boolean insert_login_details(String name, String email,
                                        String password, String phoneNumner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        values.put(PHONENUMBER, phoneNumner);
        long rowInserted = db.insert(LOGIN_TABLE_NAME, null, values);
        db.close();
        if (rowInserted != -1) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isEmailExists(String emailAddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT  *  FROM " + LOGIN_TABLE_NAME + " WHERE LOWER(" + EMAIL + ") = LOWER(?)";
        cursor = db.rawQuery(selectQuery, new String[]{emailAddress});
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public String userLogin(String email, String password) {
        String userId = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT  id  FROM " + LOGIN_TABLE_NAME + " WHERE LOWER(" + EMAIL + ") = LOWER(?) AND " + PASSWORD + " = ?";
        cursor = db.rawQuery(selectQuery, new String[]{email, password});
        if (cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndex("id"));
        }
        cursor.close();
        db.close();
        return userId;
    }


    public List<Session> fetchUserDetails(String id) {
        String userName = null, email = null, phoneNumber = null;
        List<Session> getsetList = new ArrayList<Session>();
        Session session = new Session();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String selectQuery = "SELECT  EMAIL,PHONENUMBER,NAME  FROM " + LOGIN_TABLE_NAME + " WHERE id = ?";
        cursor = db.rawQuery(selectQuery, new String[]{id});
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex(NAME));
            email = cursor.getString(cursor.getColumnIndex(EMAIL));
            phoneNumber = cursor.getString(cursor.getColumnIndex(PHONENUMBER));
        }
        if (CheckValue.isValid(userName)) {
            session.setName(userName);
        }
        if (CheckValue.isValid(email)) {
            session.setEmail(email);
        }
        if (CheckValue.isValid(phoneNumber)) {
            session.setPhoneNumber(phoneNumber);
        }
        getsetList.add(session);
        cursor.close();
        db.close();
        return getsetList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
