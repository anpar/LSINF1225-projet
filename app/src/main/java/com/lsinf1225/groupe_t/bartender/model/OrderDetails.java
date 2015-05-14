package com.lsinf1225.groupe_t.bartender.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lsinf1225.groupe_t.bartender.MySQLiteHelper;

import java.util.ArrayList;
import java.util.Date;

public class OrderDetails {
    private static final String DB_TABLE_ORDER_DETAILS="order_details";
    private static final String DB_COLUMN_ID_ORDER = "id_order";
    private static final String DB_COLUMN_ID_DRINK="id_drink";
    private static final String DB_COLUMN_QUANTITY="quantity";

    private Drink drink;
    private int idOrder;
    private int quantity;
    public OrderDetails(int id_order,Drink drink,int quantity){
        this.drink=drink;
        this.idOrder=id_order;
        this.quantity=quantity;
    }

    public Drink getDrink() {
        return drink;
    }
    public int getId_order() {
        return idOrder;
    }
    public int getQuantity() {
        return quantity;
    }
    public static ArrayList<OrderDetails> getOrderDetails(int id_order) {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        String[] columns = new String[]{DB_COLUMN_ID_ORDER,DB_COLUMN_ID_DRINK,DB_COLUMN_QUANTITY};

        String selection = DB_COLUMN_ID_ORDER + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id_order)};

        Cursor c = db.query(DB_TABLE_ORDER_DETAILS, columns, selection, selectionArgs, null, null, null);
        float total=0;
        ArrayList<OrderDetails> list = new ArrayList<>();
        OrderDetails item;
        Drink drink;
        if(c != null && c.moveToFirst()) {
            while (!c.isAfterLast()) {
                drink=Drink.get(c.getInt(1));
                Log.d("ValeurDrink", "id_drink" + c.getInt(1));
                total=total+(drink.getPrice()*c.getInt(2));
                item = new OrderDetails(c.getInt(0),drink, c.getInt(2));
                c.moveToNext();
                list.add(item);
            }

            c.close();
        }
        Order.get(id_order).setTotal(total);
        Log.d("ValeurTotal", "Valeur du total" + total);
        db.close();
        return list;
    }

    public static int addDrink(int id_order, int quantity, int id_drink) {
        SQLiteDatabase db = MySQLiteHelper.get().getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(DB_COLUMN_ID_DRINK, id_drink);
        contentValues.put(DB_COLUMN_ID_ORDER, id_order);
        contentValues.put(DB_COLUMN_QUANTITY, quantity);
        return (int)db.insert(DB_TABLE_ORDER_DETAILS,null,contentValues);
    }
}
