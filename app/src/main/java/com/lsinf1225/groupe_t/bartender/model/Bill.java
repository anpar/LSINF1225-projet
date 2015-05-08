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

    private static final String DB_TABLE_ORDER = "orders";
    private static final String DB_COL_ID_ORDER = "id_order";

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

    public static boolean addBill(int table_number) {
        boolean addSuccessful = false;
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        String[] columns = new String[]{DB_COL_ID_BILL};
        String where = DB_COL_TABLE_NUMBER + " = ?";
        String[] whereArgs = new String[]{Integer.toString(table_number)};
        Cursor c = db.query(DB_TABLE_BILLS, columns, where, whereArgs, null, null, null);
        c.moveToFirst();
        // Si il y a déjà une facture ouverte pour cette table
        if(c.getCount() >= 1) {
            c.close();
            db.close();
            return addSuccessful;
        }

        db.close();
        db = MySQLiteHelper.get().getReadableDatabase();
        columns = new String[]{DB_COL_ID_ORDER};
        where = DB_COL_TABLE_NUMBER + " = ?";
        whereArgs = new String[]{Integer.toString(table_number)};
        c = db.query(DB_TABLE_ORDER, columns, where, whereArgs, null, null, null);
        c.moveToFirst();
        // Si il n'y a aucune commande en cours pour cette table
        if(c.getCount() <= 0) {
            c.close();
            db.close();
            return addSuccessful;
        }

        db.close();
        db = MySQLiteHelper.get().getWritableDatabase();

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues=new ContentValues();
        contentValues.put(DB_COL_TABLE_NUMBER, table_number);
        contentValues.put(DB_COL_DATE, parse.format(new Date()));
        addSuccessful = db.insert(DB_TABLE_BILLS,null,contentValues) > 0;

        return(addSuccessful);
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

    public static boolean close(int table_number) {
        boolean removeSuccessful = false;
        removeSuccessful = Order.remove_order(table_number) > 0;

        return removeSuccessful;
    }

    public static ArrayList<Integer> getAllTable(){

        ArrayList<Integer> tables = new ArrayList<Integer>();
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        String[] columns = new String[]{DB_COL_TABLE_NUMBER};
        Cursor c = db.query(true,DB_TABLE_BILLS, columns, null, null, null, null, null,null);

        if(c != null && c.moveToFirst()) {
            while (!c.isAfterLast()) {
                Integer table = c.getInt(0);
                tables.add(table);
                c.moveToNext();
            }

            c.close();
        }

        db.close();

        return tables;

    }
}