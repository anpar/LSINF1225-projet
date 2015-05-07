package com.lsinf1225.groupe_t.bartender.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import com.lsinf1225.groupe_t.bartender.MySQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Bill {
    private static final String DB_TABLE_BILLS =  "bills";

    private static final String DB_COL_ID_BILL = "id_bill";
    private static final String DB_COL_DATE = "date";
    private static final String DB_COL_TABLE_NUMBER = "table_number";

    /**
     * Contient les instances déjà existantes des objets afin d'éviter de créer deux instances du
     * même objet.
     */
    private static final SparseArray<Bill> billSparseArray = new SparseArray<Bill>();

    private int id_bill;
    private int table_number;
    private String date;

    public int getId_bill() {
        return id_bill;
    }

    public String getDate() {
        return date;
    }

    public int getTable_number() {
        return table_number;
    }

    public Bill(String date, int id_bill, int table_number) {
        this.date = date;
        this.id_bill = id_bill;
        this.table_number = table_number;
    }

    public Bill(int id_bill) {
        this.id_bill = id_bill;
        Bill.billSparseArray.put(id_bill, this);
        loadData();
    }

    private void loadData() {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        String[] columns = new String[]{DB_COL_ID_BILL, DB_COL_DATE, DB_COL_TABLE_NUMBER};
        String selection = DB_COL_ID_BILL + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id_bill)};

        Cursor c = db.query(DB_TABLE_BILLS, columns, selection, selectionArgs, null, null, null);

        c.moveToFirst();

        this.id_bill = c.getInt(0);
        this.date = c.getString(1);
        this.table_number = c.getInt(2);
        c.close();
        db.close();
    }

    public static ArrayList<Bill> getBills() {
        ArrayList<Bill> bills = new ArrayList<Bill>();
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        String[] columns = new String[]{DB_COL_ID_BILL};
        Cursor c = db.query(DB_TABLE_BILLS, columns, null, null, null, null, null);

        if(c != null && c.moveToFirst()) {
            while (!c.isAfterLast()) {
                int drink_id = c.getInt(0);
                Bill bill = Bill.get(drink_id);
                bills.add(bill);
                c.moveToNext();
            }

            c.close();
        }

        db.close();

        return bills;
    }

    public static Bill get(int id_bill) {
        Bill ci = Bill.billSparseArray.get(id_bill);
        if (ci != null) {
            return ci;
        }
        return new Bill(id_bill);
    }

    public void addBill(int table_number) {
        // create a new bill in db
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        String[] columns = {"id_bill"};
        Cursor c = db.query("bills",columns,null ,null, null,null,"id_bill ASC");

        c.moveToFirst();

        int new_id_bill=1;
        while(!c.isAfterLast()) {
            if (c.getInt(0) != new_id_bill) {
                break;
            }
            new_id_bill++;
        }
        c.close();

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ContentValues contentValues=new ContentValues();
        contentValues.put("id_bill", new_id_bill);
        contentValues.put("table_number", table_number);
        contentValues.put("login_waiter", User.getConnectedUser().getLogin());
        contentValues.put("date", parse.format(new Date()));

        db.insert("bills",null,contentValues);

    }

    public float getTotal() {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        String sel[] = {""+id_bill};
        Cursor cursor = db.rawQuery("SELECT D.quantity, Dr.price FROM drinks Dr, bills B, order_details D,orders O WHERE D.id_drink = Dr.id_drink AND B.table_number = O.table_number AND O.id_order = D.id_order AND B.id_bill = ?",sel);

        cursor.moveToFirst();

        float total = 0;

        while (!cursor.isAfterLast()) {
            total += cursor.getInt(0)*cursor.getFloat(1);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        return total;
    }
}