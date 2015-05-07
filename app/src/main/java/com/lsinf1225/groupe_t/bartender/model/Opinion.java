package com.lsinf1225.groupe_t.bartender.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.MySQLiteHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Opinion {
    private static final String DB_COLUMN_ID_DRINK = "id_drink";
    private static final String DB_COLUMN_LOGIN_CLIENT = "login_client";
    private static final String DB_COLUMN_VALUE = "value";
    private static final String DB_COLUMN_COMMENT = "comment";
    private static final String DB_TABLE = "ratings";

    private int id_drink;
    private String login_client;
    private float note;
    private String comment;

    public Opinion(int id_drink, String login_client, float note, String comment) {
        this.id_drink = id_drink;
        this.login_client = login_client;
        this.note = note;
        this.comment = comment;
    }

    public int getId_drink() {
        return id_drink;
    }

    public String getLogin_client() {
        return login_client;
    }

    public String getComment() {
        return comment;
    }

    public float getNote() {
        return note;
    }

    public static ArrayList<Opinion> getComments(int id_drink) {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        String[] columns = new String[]{DB_COLUMN_ID_DRINK, DB_COLUMN_LOGIN_CLIENT, DB_COLUMN_VALUE, DB_COLUMN_COMMENT};

        String selection = DB_COLUMN_ID_DRINK + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id_drink)};

        Cursor c = db.query(DB_TABLE, columns, selection, selectionArgs, null, null, null);

        ArrayList<Opinion> list = new ArrayList<>();
        Opinion item;
        if(c != null && c.moveToFirst()) {
            while (!c.isAfterLast()) {
                item = new Opinion(c.getInt(0), c.getString(1), c.getInt(2), c.getString(3));
                c.moveToNext();
                list.add(item);
            }

            c.close();
        }

        db.close();
        return list;
    }

    public boolean equals(Object obj) {
        Opinion op = (Opinion) obj;
        return(op.getLogin_client().equals(login_client));
    }

    public static boolean add(int id_drink, String login, float note, String comment) {
        boolean addSuccessful = false;

        SQLiteDatabase db = MySQLiteHelper.get().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_ID_DRINK, id_drink);
        values.put(DB_COLUMN_LOGIN_CLIENT, login);
        values.put(DB_COLUMN_VALUE, note);
        Log.d("AddComment", "value of comment" + comment);
        values.put(DB_COLUMN_COMMENT, comment);
        addSuccessful = db.insert(DB_TABLE, null, values) > 0;

        return addSuccessful;
    }
}
