package com.lsinf1225.groupe_t.bartender.model;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

import com.lsinf1225.groupe_t.bartender.MySQLiteHelper;

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



    /**
     * Nom de colonne sur laquelle le tri est effectué
     */
    public static String order_by = DB_COL_ID;
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
     * Constructeur de notre élément de collection. Initialise une instance de l'élément présent
     * dans la base de données.
     *
     * @note Ce constructeur est privé (donc utilisable uniquement depuis cette classe). Cela permet
     * d'éviter d'avoir deux instances différentes d'un même élément dans la base de données, nous
     * utiliserons la méthode statique get(ciId) pour obtenir une instance d'un élément de notre
     * collection.
     */


    public Order(Drink drink,int id_order, int table_number, String login_waiter, int quantity){
        this.drink_list.add(drink);
        this.id_order = id_order;
        this.table_number = table_number;
        this.login_waiter = login_waiter;
        this.list_quantity.add(quantity);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        this.date = dateFormat.format(cal.getTime()); //TODO pas sur que ca fonctionne

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
     * (Re)charge les informations depuis la base de données.
     *
     * @pre L'id de l'élément est indiqué dans this.id et l'élément existe dans la base de données.
     * @post Les informations de l'élément sont chargées dans les variables d'instance de la classe.
     */
    private void loadData() {
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        String[] columns = new String[]{DB_COL_ID,DB_COL_DATE,DB_COL_LOGIN_WAITER, DB_COL_TABLE_NUMBER};

        String selection = DB_COL_ID + " = ? ";
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
     * Fournit la liste de tous les éléments de la collection de l'utilisateur courant dont le nom
     * contient searchsearchQuery.
     *
     * @param searchQuery Requête de recherche.
     *
     * @return Liste d'éléments de collection répondant à la requête de recherche.
     */
    public static ArrayList<Order> searchOrder(String searchQuery) {
        String selection = DB_COL_LOGIN_WAITER + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchQuery + "%"};

        // Les critères de selection sont passés à la sous-méthode de récupération des éléments.
        return getOrders(selection, selectionArgs);
    }

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


}