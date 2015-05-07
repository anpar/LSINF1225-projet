package com.lsinf1225.groupe_t.bartender.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lsinf1225.groupe_t.bartender.MySQLiteHelper;

/**
 * User class.
 * Note : this is highly inspired from the user Class written
 * by Damien Mercier in TP10.
 */
public class User {
    private static final String DB_COLUMN_ID = "u_id";
    private static final String DB_COLUMN_NAME = "u_login";
    private static final String DB_COLUMN_PASSWORD = "u_password";
    private static final String DB_COLUMN_TYPE = "u_type";
    private static final String DB_TABLE = "users";
    public static final String EMPTY = "";

    private int id;
    private String login;
    private String password;
    private String type;

    /*
        Constructor
     */
    public User(int uId, String uLogin, String uPassword, String uType) {
        this.id = uId;
        this.login = uLogin;
        this.password = uPassword;
        this.type = uType;
    }

    /*
        Getters
     */
    public int getId() {return id;}
    public String getLogin() {return login;}
    protected String getPassword() {return password;}
    public String getType() {return type;}

    /**
     * Connects the current user.
     */
    public boolean login() {
        User user = User.passwordMatch(this.login, this.password);
        if(user != null) {
            this.id = user.getId();
            this.type = user.getType();
            connectedUser = this;
            return(true);
        }

        return(false);
    }

    public String toString() {return getLogin();}

    @Override
    public boolean equals(Object obj) {
        User toCompare = (User) obj;
        return(this.login.equals((String) toCompare.getLogin()) && this.password.equals((String) toCompare.getPassword()));
    }

    /***************************************************************************
     *                      Static part of the class
     ***************************************************************************/

    /**
     * Connected user.
     */
    private static User connectedUser = null;

    /**
     * Check if the connectedUser is a waiter
     */
    public static boolean isWaiter() {
        if (User.connectedUser != null) {
            return(User.connectedUser.getType().equals((String) "waiter"));
        }

        return false;
    }

    /**
     * Returns the connected user.
     */
    public static User getConnectedUser() {return User.connectedUser;}

    /**
     * Logout the connected user.
     */
    public static void logout() {
        User.connectedUser = null;
    }

    /**
     * Provides the users list.
     */
    public static User passwordMatch(String login, String password) {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        String [] columns = {DB_COLUMN_ID, DB_COLUMN_NAME, DB_COLUMN_PASSWORD, DB_COLUMN_TYPE};
        String where = DB_COLUMN_NAME + " = ? AND " + DB_COLUMN_PASSWORD + " = ?";
        String[] whereArgs = {login, password};
        Cursor cursor = db.query(DB_TABLE, columns, where, whereArgs, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 1) {
            int uId = cursor.getInt(0);
            String uLogin = cursor.getString(1);
            String uPassword = cursor.getString(2);
            String uType = cursor.getString(3);

            User user = new User(uId, uLogin, uPassword, uType);
            cursor.close();
            db.close();

            return user;
        }

        return(null);
    }

    public static boolean isNew(String username) {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        String [] columns = {DB_COLUMN_ID, DB_COLUMN_NAME, DB_COLUMN_PASSWORD, DB_COLUMN_TYPE};
        String where = DB_COLUMN_NAME + " = ?";
        String[] whereArgs = {username};
        Cursor cursor = db.query(DB_TABLE, columns, where, whereArgs, null, null, null);
        cursor.moveToFirst();

        if(cursor.getCount() == 1) {
            return false;
        }

        return(true);
    }

    public static boolean add(User newUser) {
        boolean addSuccessful = false;

        SQLiteDatabase db = MySQLiteHelper.get().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_NAME, newUser.getLogin());
        values.put(DB_COLUMN_PASSWORD, newUser.getPassword());
        values.put(DB_COLUMN_TYPE, (String) "customer");
        addSuccessful = db.insert(DB_TABLE, null, values) > 0;

        return addSuccessful;
    }
}
