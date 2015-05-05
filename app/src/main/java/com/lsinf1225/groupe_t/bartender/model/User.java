package com.lsinf1225.groupe_t.bartender.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;

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
        ArrayList<User> users = getUsers();
        if(users.contains(this)) {
            int indexOfUser = users.indexOf(this);
            this.id = users.get(indexOfUser).getId();
            this.type = users.get(indexOfUser).getType();
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
     * Contains instances of already existing users.
     */
    private static SparseArray<User> userSparseArray = new SparseArray<>();

    /**
     * Connected user.
     */
    private static User connectedUser = null;

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
    public static ArrayList<User> getUsers() {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        String [] columns = {DB_COLUMN_ID, DB_COLUMN_NAME, DB_COLUMN_PASSWORD, DB_COLUMN_TYPE};
        Cursor cursor = db.query(DB_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();

        ArrayList<User> users = new ArrayList<>();
        while(!cursor.isAfterLast()) {
            int uId = cursor.getInt(0);
            String uLogin = cursor.getString(1);
            String uPassword = cursor.getString(2);
            String uType = cursor.getString(3);

            User user = User.userSparseArray.get(uId);
            if(user == null) {
                user = new User(uId, uLogin, uPassword, uType);
            }

            users.add(user);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return users;
    }
}
