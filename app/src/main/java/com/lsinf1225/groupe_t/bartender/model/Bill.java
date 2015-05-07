package com.lsinf1225.groupe_t.bartender.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lsinf1225.groupe_t.bartender.MySQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Bill {

    private int id_bill;
    private int table_number;
    private String date;

    public int getId_bill() {
        return id_bill;
    }

    public String getDate() {
        return date; // !!!!!!! !!!! TODO à vérifié car date dans sql est type date pas String
    }

    public int getTable_number() {
        return table_number;
    }

    public Bill(String date, int id_bill, int table_number) {
        this.date = date;
        this.id_bill = id_bill;
        this.table_number = table_number;
    }

    void addBill(int table_number) {
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

    float getTotal() {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        // db.query("bills",columns,"id_bill = ?",id_bill,);
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

    void close() {
        int result=0;
        //result = Order.remove_order(id_bill);
        if (result == 0) {
            // TODO erreur pas d'élément supprimé
        }
    }

}