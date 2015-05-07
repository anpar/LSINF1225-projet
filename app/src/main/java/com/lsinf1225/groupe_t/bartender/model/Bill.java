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

    /**
     * Fournit la liste de tous les éléments de la collection de l'utilisateur courant.
     *
     * @return Liste d'éléments.
     */
    public static ArrayList<Bill> getBills() {
        return getBills(null, null);
    }

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

    public Bill(int table_number) {
        this.date = ""; // TODO update for real date

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

        this.id_bill = new_id_bill;
        this.table_number = table_number;
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
    private static ArrayList<Bill> getBills(String selection, String[] selectionArgs) {
        ArrayList<Bill> bills = new ArrayList<Bill>();
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();
        String[] columns = new String[]{"id_bills"};
        Cursor c = db.query("bills", columns, selection, selectionArgs, null, null, "id_bills ASC");

        if(c != null && c.moveToFirst()) {
            while (!c.isAfterLast()) {
                int id_bill = c.getInt(0);
                Bill bill = Bill.get(id_bill);
                bills.add(bill);
                c.moveToNext();
            }

            c.close();
        }

        db.close();

        return bills;
    }

    /**
     * Fournit la liste de tous les éléments de la collection de l'utilisateur courant dont le nom
     * contient searchsearchQuery.
     *
     * @param searchQuery Requête de recherche.
     *
     * @return Liste d'éléments de collection répondant à la requête de recherche.
     */
    public static ArrayList<Bill> searchBill(String searchQuery) {
        String selection = "id_bills" + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchQuery + "%"};

        // Les critères de selection sont passés à la sous-méthode de récupération des éléments.
        return getBills(selection, selectionArgs);
    }



    public void close() {
        int result=0;
        //result = Order.remove_order(id_bill);
        if (result == 0) {
            // TODO erreur pas d'élément supprimé
        }
    }

    /**
     * Fournit l'instance d'un élément de collection présent dans la base de données. Si l'élément
     * de collection n'est pas encore instancié, une instance est créée.
     *
     * @param id_bill Id de l'élément de collection.
     *
     * @return L'instance de l'élément de collection.
     * @pre L'élément correspondant à l'id donné doit exister dans la base de données.
     */
    public static Bill get(int id_bill) {
        Bill ci = Bill.billSparseArray.get(id_bill);
        if (ci != null) {
            return ci;
        }
        return new Bill(id_bill);
    }



}