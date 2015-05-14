package com.lsinf1225.groupe_t.bartender.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import com.lsinf1225.groupe_t.bartender.MySQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Order {
    public static final String DB_TABLE_ORDERS = "orders";

    public static final String DB_COL_ID = "id_order";
    public static final String DB_COL_DATE = "date";
    public static final String DB_COL_LOGIN_WAITER = "login_waiter";
    public static final String DB_COL_TABLE_NUMBER = "table_number";
    public static final String DB_TABLE_ORDERS_DETAILS = "order_details";
    public static final String DB_TABLE_BILLS = "bills";

    public static String order_by = DB_COL_ID;
    public static String order = "ASC";
    private int id_order;

    private int table_number;
    private String date;
    private String login_waiter;
    private float total;

    public Order(int id_order){
        this.id_order = id_order;
        Order.orderSparseArray.put(id_order, this);
        loadData();
    }

    public int getId() {
        return id_order;
    }
    public float getTotal(){  return total;}
    public String getLogin_waiter(){
        return  login_waiter;
    }
    public int getTable_number() {
        return table_number;
    }
    public void setTotal(float total) {
        this.total = total;
    }

    private void loadData() {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        String[] columns = new String[]{DB_COL_ID,DB_COL_DATE,DB_COL_LOGIN_WAITER, DB_COL_TABLE_NUMBER};

        String selection = DB_COL_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id_order)};

        Cursor c = db.query(DB_TABLE_ORDERS, columns, selection, selectionArgs, null, null, null);

        c.moveToFirst();

        this.id_order = c.getInt(0);
        this.login_waiter = c.getString(2);
        this.table_number = c.getInt(3);
        c.close();

    }

    public static ArrayList<Order> getOrders() {
        return getOrders(null, null);
    }

    /******************************************************************************
     * Partie static de la classe.
     ******************************************************************************/
    private static final SparseArray<Order> orderSparseArray = new SparseArray<>();

    public static ArrayList<Order> getOrders(String selection, String[] selectionArgs) {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        String[] columns = new String[]{DB_COL_ID};
        Cursor c = db.query(DB_TABLE_ORDERS, columns, selection, selectionArgs, null, null, Order.order_by + " " + Order.order);

        if(c != null && c.moveToFirst()) {
            while (!c.isAfterLast()) {
                int order_id = c.getInt(0);
                Order order = Order.get(order_id);
                orders.add(order);
                c.moveToNext();
            }

            c.close();
        }

        db.close();

        return orders;
    }

    public static Order get(int id_order) {
        Order ci = Order.orderSparseArray.get(id_order);
        if (ci != null) {
            return ci;
        }
        return new Order(id_order);
    }

    public static void reverseOrder() {
        if (Order.order.equals("ASC")) {
            Order.order = "DESC";
        } else {
            Drink.order = "ASC";
        }
    }

    public String toString() {
        Integer num = getTable_number();
        return num.toString() + " - " + getLogin_waiter();
    }

    public static int addOrder(int table_number) {
        SQLiteDatabase db = MySQLiteHelper.get().getWritableDatabase();

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues contentValues=new ContentValues();
        contentValues.put(DB_COL_TABLE_NUMBER, table_number);
        contentValues.put(DB_COL_DATE, parse.format(new Date()));
        contentValues.put(DB_COL_LOGIN_WAITER, User.getConnectedUser().getLogin());
        return (int)db.insert(DB_TABLE_ORDERS,null,contentValues);
    }

    public static int remove_order(int table_number) {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        // On supprime la facture
        String where = DB_COL_TABLE_NUMBER + " = ?";
        String whereArg1[] = {String.valueOf(table_number)};
        int r = db.delete(DB_TABLE_BILLS,where,whereArg1);

        // On sélectionne les id de commande correspondant à la table
        String columns[] = new String[]{DB_COL_ID};
        where = DB_COL_TABLE_NUMBER + " = ?";
        String whereArg2[] = {Integer.toString(table_number)};
        Cursor cursor = db.query(DB_TABLE_ORDERS, columns, where, whereArg2, null, null, null);

        cursor.moveToFirst();

        // On supprimer les order details correspondant au id de commande
        int id;
        int s = 0;
        where = DB_COL_ID + " = ?";
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(0);
            String whereArg3[] = {Integer.toString(id)};
            s += db.delete(DB_TABLE_ORDERS_DETAILS, where, whereArg3);
            cursor.moveToNext();
        }
        cursor.close();

        // On supprimer les commandes
        where = DB_COL_TABLE_NUMBER + " = ?";
        String whereArg4[] = {Integer.toString(table_number)};
        int q = db.delete(DB_TABLE_ORDERS,where,whereArg4);
        cursor.close();
        return r+q+s;
    }

    public static ArrayList<Integer> getAllTable(){
        ArrayList<Integer> tables = new ArrayList<>();
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        String[] columns = new String[]{DB_COL_TABLE_NUMBER};
        Cursor c = db.query(true,DB_TABLE_ORDERS, columns, null, null, null, null, null,null);

        if(c != null && c.moveToFirst()) {
            while (!c.isAfterLast()) {
                Integer table = c.getInt(0);
                tables.add(table);
                c.moveToNext();
            }

            c.close();
        }

        db.close();
        tables.removeAll(Bill.getAllTable());
        return tables;
    }
}