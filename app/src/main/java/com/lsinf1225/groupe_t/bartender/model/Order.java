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
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
/**
 * Created by Louis on 7/05/2015.
 */
public class Order {

    /*
     * Noms des tables et des colonnes dans la base de données.
     */
    public static final String DB_TABLE_ORDERS = "orders";
    public static final String DB_TABLE_ORDER_DETAILS = "order_details";

    public static final String DB_COL_ID = "id_order";
    public static final String DB_COL_DATE = "date";
    public static final String DB_COL_LOGIN_WAITER = "login_waiter";
    public static final String DB_COL_TABLE_NUMBER = "table_number";

    public static final String DB_COL_ID_DRINK = "id_drink";
    public static final String DB_COL_QUANTITY = "quantity";


    /* Pour éviter les ambiguités dans les requêtes, il faut utiliser le format
     *      nomDeTable.nomDeColonne
     * lorsque deux tables possèdent le même nom de colonne.
     */
    public static final String DB_COL_ORDERS_ID = DB_TABLE_ORDERS + "." + DB_COL_ID;
    public static final String DB_COL_ORDER_DETAILS_ID = DB_TABLE_ORDER_DETAILS + "." + DB_COL_ID;

    /*
     * Pour joindre les deux tables dans une même requête.
     */
    public static final String DB_TABLES = DB_TABLE_ORDERS + " INNER JOIN " + DB_TABLE_ORDER_DETAILS + " ON " + DB_COL_ORDERS_ID + " = " + DB_COL_ORDER_DETAILS_ID;


    /**
     * Nom de colonne sur laquelle le tri est effectué
     */
    public static String order_by = DB_COL_ORDERS_ID;
    /**
     * Ordre de tri : ASC pour croissant et DESC pour décroissant
     */
    public static String order = "ASC";

    /**
     * ID unique de notre élément courant. Correspond à id_drink dans la base de données.
     */
    private int id_order;

    /**
     * liste de boissons commandées
     */
    private ArrayList<Drink> drink_list;

    /**
     * liste du nombres de boissons commandées
     */
    private ArrayList<Integer> list_quantity;

    /**
     * numéro de la table
     */
    private int table_number;

    /**
     * date de la commande
     */
    private String date;

    /**
     *  login du serveur qui a servit la commande
     */
    private String login_waiter;


    /**
     * Constructeur de notre élément de collection. Initialise une instance de l'élément présent
     * dans la base de données.
     *
     * @note Ce constructeur est privé (donc utilisable uniquement depuis cette classe). Cela permet
     * d'éviter d'avoir deux instances différentes d'un même élément dans la base de données, nous
     * utiliserons la méthode statique get(ciId) pour obtenir une instance d'un élément de notre
     * collection.
     */
    public Order(int id_order){
        this.id_order = id_order;
        Order.orderSparseArray.put(id_order, this);
        loadData();
    }

    /**
     * Constructeur à utiliser pour initialiser une commande.
     * @param drink :boisson commandée
     * @param id_order : id de la commande initialisée
     * @param table_number : numéro de la table où la commande est passée
     * @param login_waiter : login du serveur qui prend la commande
     * @param quantity : quantité de la boisson commandée
     */
    public Order(Drink drink,int id_order, int table_number, String login_waiter, int quantity){
        this.drink_list.add(drink);
        this.id_order = id_order;
        this.table_number = table_number;
        this.login_waiter = login_waiter;
        this.list_quantity.add(quantity);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        this.date = dateFormat.format(cal.getTime());  //TODO pas sur que ca fonctionne
    }


    /**
     * Fournit l'id de l'élément de collection courant.
     */
    public int getId() {
        return id_order;
    }


   public ArrayList<Drink> getDrink_list(){
       return drink_list;
   }

   public String getDate(){
       return date;
   }

   public String getLogin_waiter(){
       return  login_waiter;
   }

   public int getTable_number() {
       return table_number;
   }

   public ArrayList<Integer> getList_quantity(){
       return list_quantity;
   }


    /**
     * Ajoute une boisson à la commande en cours
     * @param drink : boisson à ajouter
     * @param quantity : quantité commandée
     */
    public void AddDrink(Drink drink, int quantity)
    {
        this.drink_list.add(drink);
        this.list_quantity.add(quantity);
    }

    /**
     * Place tous les éléments de la commande en cours dans la base
     * SQL.
     */
    public void pushOrder(){

        SQLiteDatabase db = MySQLiteHelper.get().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COL_ORDERS_ID,this.id_order);
        values.put(DB_COL_DATE, date);
        values.put(DB_COL_LOGIN_WAITER, login_waiter);
        values.put(DB_COL_TABLE_NUMBER, table_number);

        for (int i = 0; i <drink_list.size() ; i++) {
            this.pushDrink(drink_list.get(i),list_quantity.get(i),db);
        }
        db.close();
    }

    public void pushDrink(Drink drink, Integer quantity, SQLiteDatabase db){

        ContentValues values = new ContentValues();
        values.put(DB_COL_ID_DRINK,drink.getId_drink() );
        values.put(DB_COL_QUANTITY,quantity);
        values.put(DB_COL_ORDERS_ID, this.id_order);
        db.insert(DB_TABLE_ORDER_DETAILS, null, values);

    }





    /**
     * (Re)charge les informations depuis la base de données.
     *
     * @pre L'id de l'élément est indiqué dans this.id et l'élément existe dans la base de données.
     * @post Les informations de l'élément sont chargées dans les variables d'instance de la classe.
     */
    private void loadData() {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        String[] columns = new String[]{DB_COL_ORDERS_ID, DB_COL_DATE,DB_COL_LOGIN_WAITER, DB_COL_TABLE_NUMBER};

        String selection = DB_COL_ORDERS_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id_order)};
        Log.d("Menu", "Trying to retrieve " + id_order);

        Cursor c = db.query(DB_TABLE_ORDERS, columns, selection, selectionArgs, null, null, null);

        c.moveToFirst();

        this.id_order = c.getInt(0);
       //TODO date
        this.login_waiter = c.getString(2);
        this.table_number = c.getInt(3);
        c.close();


    }

    /**
     * Fournit la liste de tous les éléments de la collection de l'utilisateur courant.
     *
     * @return Liste d'éléments.
     */
    public static ArrayList<Order> getOrders() {
        return getOrders(null, null);
    }

    /******************************************************************************
     * Partie static de la classe.
     ******************************************************************************/

    /**
     * Contient les instances déjà existantes des objets afin d'éviter de créer deux instances du
     * même objet.
     */
    private static final SparseArray<Order> orderSparseArray = new SparseArray<Order>();



    /**
     * Fournit la liste de tous les objets correspondant aux critères de sélection demandés.
     *
     * Cette méthode est une sous-méthode de getSongs et de searchSongs.
     *
     * @param selection     Un filtre déclarant quels éléments retourner, formaté comme la clause
     *                      SQL WHERE (excluant le WHERE lui-même). Donner null retournera tous les
     *                      éléments.
     * @param selectionArgs Vous pouvez inclure des ? dans selection, qui seront remplacés par les
     *                      valeurs de selectionArgs, dans leur ordre d'apparition dans selection.
     *                      Les valeurs seront liées en tant que chaînes.
     *
     * @return Liste d'objets. La liste peut être vide si aucun objet ne correspond.
     */
    private static ArrayList<Order> getOrders(String selection, String[] selectionArgs) {
        ArrayList<Order> orders = new ArrayList<Order>();
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        String[] columns = new String[]{DB_COL_ORDERS_ID};
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

    /**
     * Fournit l'instance d'un élément de collection présent dans la base de données. Si l'élément
     * de collection n'est pas encore instancié, une instance est créée.
     *
     * @param id_order Id de l'élément de collection.
     *
     * @return L'instance de l'élément de collection.
     * @pre L'élément correspondant à l'id donné doit exister dans la base de données.
     */
    public static Order get(int id_order) {
        Order ci = Order.orderSparseArray.get(id_order);
        if (ci != null) {
            return ci;
        }
        return new Order(id_order);
    }


    /**
     * Inverse l'ordre de tri actuel.
     *
     * @pre La valeur de Song.order est soit ASC soit DESC.
     * @post La valeur de Song.order a été modifiée et est soit ASC soit DESC.
     */
    public static void reverseOrder() {
        if (Order.order.equals("ASC")) {
            Order.order = "DESC";
        } else {
            Drink.order = "ASC";
        }
    }

    /**
     * Fournit une représentation sous forme de texte du morceau. Utilisé pour la liste dans
     * PlayerActivity.
     */
    public String toString() {
        Integer num = getTable_number();
        return num.toString() + " - " + getLogin_waiter();
    }

    /**
     *  Supprime les bill de la base de donné et tous les info la concernant
     *
     * @param id_bill
     * @return nombre d'élément (orders/bill)supprimé de la base de donnée
     */
    int remove_order(int id_bill) {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        String re[]= {""+id_bill};
        Cursor c = db.rawQuery("SELECT B.table_number FROM bills B WHERE B.id_number = ?",re);

        int table_number = 0;
        while (!c.isAfterLast()) {
            table_number = c.getInt(0);
            c.moveToNext();
        }
        c.close();

        String salt[] = {""+id_bill};
        int r = db.delete("bills","id_bill = ?",salt);

        String arg[] = {""+table_number};
        Cursor cursor = db.rawQuery("SELECT DISTINCT D.id_order FROM order_details D,orders O WHERE D.id_order = O.id_order AND O.table_number = ?",arg);

        cursor.moveToFirst();

        int id;
        int s=0;
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(0);
            String pepper[] = {""+id};
            s += db.delete("order_details","id_order = ?",pepper);
            cursor.moveToNext();
        }
        cursor.close();
        String fred[] = {""+id_bill,""+table_number};
        int q = db.delete("orders","id_bill = ? AND table_number = ?",fred);

        return r+q+s;
    }

}
