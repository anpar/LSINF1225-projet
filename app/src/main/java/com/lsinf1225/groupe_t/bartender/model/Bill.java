package com.lsinf1225.groupe_t.bartender.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lsinf1225.groupe_t.bartender.MySQLiteHelper;

/**
 * Created by Louis on 7/05/2015.
 */
public class Bill {

    private int id_bill;
    private int table_number;
    private String date;
    // addition
    //
    //recherche addition dans db par table + calculer total d'une commande + close = supprimer tous les commande associé a une table spécifique


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

    float getTotal() {

        String[] columns = new String[]{"table_number" };

        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        // db.query("bills",columns,"id_bill = ?",id_bill,);
        String sel[] = {""+id_bill};
        Cursor cursor = db.rawQuery("SELECT D.quantity, Dr.price FROM drinks Dr, bills B,order_details D,orders O WHERE D.id_drink = Dr.id_drink AND B.table_number = O.table_number AND O.id_order = D.id_order AND B.id_bill = ?",sel);

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
        //Order.delete(id_bill);
    }

}