package com.lsinf1225.groupe_t.bartender.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.MySQLiteHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Représente un élément de collection et permet de le gérer.
 *
 * Cette classe représente un élément de collection et permet de le gérer. Elle utilise pour cela la
 * base de données par l'intermédiaire du MySQLiteHelper.
 *
 * Les méthodes statiques permettent de récupérer des listes d'éléments de collection. Afin de
 * déterminer l'ordre de tri des éléments pour ces méthodes, les variables de classe order et
 * order_by sont utilisées. La variable order fait référence au nom de colonne sur lequel est
 * effectué le tri. Et la variable order_by est soit ASC (pour un tri croissant) ou DESC (pour un
 * tri décroissant).
 *
 * @author Damien Mercier
 * @version 1
 */
public class Drink {

    /*
     * Noms des tables et des colonnes dans la base de données.
     */
    public static final String DB_TABLE_DRINKS = "drinks";
    public static final String DB_TABLE_RATINGS = "ratings";

    public static final String DB_COL_ID = "id_drink";
    public static final String DB_COL_NAME_DRINK = "name_drink";
    public static final String DB_COL_PRICE = "price";
    public static final String DB_COL_AVAILABLE_QUANTITY = "available_quantity";
    public static final String DB_COL_VOLUME = "volume";
    public static final String DB_COL_DESCRIPTION = "description";
    public static final String DB_COL_ICON = "icon";
    public static final String DB_COL_MAX_STOCK = "max_stock";
    public static final String DB_COL_THRESHOLD = "threshold";
    public static final String DB_COL_CATEGORY = "category";
    public static final String DB_COL_SUBCATEGORY = "subcategory";

    public static final String DB_COL_LOGIN_CLIENT = "login_client";
    public static final String DB_COL_VALUE = "value";
    public static final String DB_COL_COMMENT = "comment";

    /* Pour éviter les ambiguités dans les requêtes, il faut utiliser le format
     *      nomDeTable.nomDeColonne
     * lorsque deux tables possèdent le même nom de colonne.
     */
    public static final String DB_COL_DRINK_ID = DB_TABLE_DRINKS + "." + DB_COL_ID;
    public static final String DB_COL_RATING_ID = DB_TABLE_RATINGS + "." + DB_COL_ID;

    /*
     * Pour joindre les deux tables dans une même requête.
     */
    public static final String DB_TABLES = DB_TABLE_DRINKS + " INNER JOIN " + DB_TABLE_RATINGS + " ON " + DB_COL_DRINK_ID + " = " + DB_COL_RATING_ID;


    /**
     * Nom de colonne sur laquelle le tri est effectué
     */
    public static String order_by = DB_COL_NAME_DRINK;
    /**
     * Ordre de tri : ASC pour croissant et DESC pour décroissant
     */
    public static String order = "ASC";

    /**
     * ID unique de notre élément courant. Correspond à id_drink dans la base de données.
     */
    private int id_drink;

    /**
     * Note (rating) de notre élément courant entre 0 et 5. Correspond à value dans la base de
     * données. Est facultatif. Comme la note dépend de l'utilisateur, on a ici la liste des
     * différentes notes par id de l'utilisateur.
     */
    private SparseArray<Float> rating;

    /**
     * Nom du fichier photo de notre élément courant. Correspond à icon dans la base de
     * données. Est facultatif.
     */
    private String icon;

    /**
     * Nom de la boisson courante.
     */
    private String name_drink;

    /**
     * Prix de la boisson courante.
     */
    private float price;

    /**
     * Quantité disponible.
     */
    private int available_quantity;

    /**
     * Volume
     */
    private float volume;

    /**
     * Description
     */
    private String description;

    /**
     * Stock max
     */
    private int max_stock;

    /**
     * Threshold
     */
    private int threshold;

    /**
     * Category
     */
    private String category;

    /**
     * Subcategory
     */
    private String subcategory;


    /**
     * Constructeur de notre élément de collection. Initialise une instance de l'élément présent
     * dans la base de données.
     *
     * @note Ce constructeur est privé (donc utilisable uniquement depuis cette classe). Cela permet
     * d'éviter d'avoir deux instances différentes d'un même élément dans la base de données, nous
     * utiliserons la méthode statique get(ciId) pour obtenir une instance d'un élément de notre
     * collection.
     */
    private Drink(int id_drink) {
        // On enregistre l'id dans la variable d'instance.
        this.id_drink = id_drink;
        // On enregistre l'instance de l'élément de collection courant dans la hashMap.
        Drink.drinkSparseArray.put(id_drink, this);
        // On charge les données depuis la base de données.
        loadData();
    }


    /**
     * Fournit l'id de l'élément de collection courant.
     */
    public int getId() {
        return id_drink;
    }

    /**
     * Fournit la note de l'élément de collection courant (comprise entre 0 et 5).
     */
    public float getRating() {
        return rating.get(User.getConnectedUser().getId());
    }

    /**
     * Fournit l'image Bitmap correspondant à l'élément de collection courant ou null s'il n'y en a
     * pas.
     */
    public Bitmap getPicture() {
        if (this.icon == null) {
            // S'il n'y a pas de nom de fichier, il n'y a pas d'image.
            return null;
        }

        try {
            /**
             *  @note Pour des questions de facilité, le choix a été fait de stocker les fichiers
             *  des photos dans la mémoire interne de l'application.
             *  Lisez https://developer.android.com/training/basics/data-storage/files.html afin de
             *  comprendre les différentes possibilités.
             */

            FileInputStream in = BarTenderApp.getContext().openFileInput(icon);
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            in.close();

            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName_drink() {
        return name_drink;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public int getId_drink() {
        return id_drink;
    }

    public float getPrice() {
        return price;
    }

    public int getAvailable_quantity() {
        return available_quantity;
    }

    public int getThreshold() {
        return threshold;
    }

    public int getMax_stock() {
        return max_stock;
    }

    public float getVolume() {
        return volume;
    }

    /**
     * Modifie la note de l'objet courant pour l'utilisateur actuellement connecté à l'application.
     *
     * @param newRating Nouvelle note pour l´objet courant.
     *
     * @return Retourne vrai (true) si l´opération s´est bien déroulée, faux (false) sinon.
     * @pre newRating doit être compris dans [0;5].
     * @post Modifie la newRating de l'objet courant dans la base de données.
     */
    public boolean setNote(float newRating) {

        // Vérification de la pré-condition.
        if (newRating < 0 || newRating > 5) {
            return false;
        }

        // Récupération de la base de données en mode "écriture".
        SQLiteDatabase db = MySQLiteHelper.get().getWritableDatabase();

        // Indique les valeurs à mettre à jour.
        ContentValues values = new ContentValues();
        values.put(DB_COL_VALUE, newRating);

        // Indique sur quelle ligne effectuer la mise à jour.
        String selection = DB_COL_LOGIN_CLIENT + " = ? AND " + DB_COL_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(User.getConnectedUser().getId()), String.valueOf(id_drink)};

        // Requête UPDATE sur la base de données.
        db.update(DB_TABLE_RATINGS, values, selection, selectionArgs);

        // Fermeture de la base de données.
        db.close();

        // Mise à jour de la note de l'élément courant pour l'utilisateur connecté.
        this.rating.put(User.getConnectedUser().getId(), newRating);

        return true;
    }

    /**
     * (Re)charge les informations depuis la base de données.
     *
     * @pre L'id de l'élément est indiqué dans this.id et l'élément existe dans la base de données.
     * @post Les informations de l'élément sont chargées dans les variables d'instance de la
     * classe.
     */
    private void loadData() {
        // Récupération de la base de données en mode "lecture".
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        // Colonnes pour lesquelles il nous faut les données.
        String[] columns = new String[]{DB_COL_DRINK_ID, DB_COL_NAME_DRINK, DB_COL_PRICE, DB_COL_DESCRIPTION, DB_COL_ICON, DB_COL_CATEGORY, DB_COL_SUBCATEGORY, DB_COL_VOLUME,
        DB_COL_AVAILABLE_QUANTITY, DB_COL_THRESHOLD, DB_COL_MAX_STOCK};

        // Critères de sélection de la ligne :
        String selection;// = DB_COL_ID + " = ? ";
        String[] selectionArgs;// = new String[]{String.valueOf(id_drink)};

        // Requête SELECT à la base de données.
        Cursor c = db.query(DB_TABLE_DRINKS, columns, null, null, null, null, null);

        // Placement du curseur sur le  premier résultat (ici le seul puisque l'objet est unique).
        c.moveToFirst();

        // Copie des données de la ligne vers les variables d'instance de l'objet courant.
        this.id_drink = c.getInt(0);
        this.name_drink = c.getString(1);
        this.price = c.getFloat(2);
        this.description = c.getString(3);
        this.icon = c.getString(4);
        this.category = c.getString(5);
        this.subcategory = c.getString(6);
        this.volume = c.getFloat(7);
        this.available_quantity = c.getInt(8);
        this.threshold = c.getInt(9);
        this.max_stock = c.getInt(10);

        // Fermeture du curseur.
        c.close();

        /* Récupération des différentes notes pour les différents utilisateurs. */

        this.rating = new SparseArray<Float>();

        // Colonnes à récupérérer.
        columns = new String[]{DB_COL_LOGIN_CLIENT, DB_COL_VALUE};

        // Critères de sélection de la ligne.
        selection = DB_COL_ID + " = ?";
        selectionArgs = new String[]{String.valueOf(id_drink)};

        // Requête SELECT à la base de données.
        c = db.query(DB_TABLE_RATINGS, columns, selection, selectionArgs, null, null, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            // On copie les résultats dans la variable d'instance rating.
            this.rating.put(c.getInt(0), c.getFloat(1));
            c.moveToNext();
        }

        // Fermeture du curseur et de la base de données.
        c.close();
        db.close();
    }

    /**
     * Fournit la liste de tous les éléments de la collection de l'utilisateur courant.
     *
     * @return Liste d'éléments.
     */
    public static ArrayList<Drink> getDrinks() {
        return getDrinks(null, null);
    }

    /******************************************************************************
     * Partie static de la classe.
     ******************************************************************************/

    /**
     * Contient les instances déjà existantes des objets afin d'éviter de créer deux instances du
     * même objet.
     */
    private static final SparseArray<Drink> drinkSparseArray = new SparseArray<Drink>();

    /**
     * Fournit la liste de tous les éléments de la collection de l'utilisateur courant dont le nom
     * contient searchQuery.
     *
     * @param searchQuery Requête de recherche.
     *
     * @return Liste d'éléments de collection répondant à la requête de recherche.
     */
    public static ArrayList<Drink> searchDrink(String searchQuery) {
        String selection = DB_COL_NAME_DRINK + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchQuery + "%"};

        // Les critères de selection sont passés à la sous-méthode de récupération des éléments.
        return getDrinks(selection, selectionArgs);
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
    private static ArrayList<Drink> getDrinks(String selection, String[] selectionArgs) {
        // Initialisation de la liste des songs.
        ArrayList<Drink> drinks = new ArrayList<Drink>();

        // Récupération du SQLiteHelper pour récupérer la base de données.
        SQLiteDatabase db = MySQLiteHelper.get().getReadableDatabase();

        // Colonnes à récupérer. Ici uniquement l'id de l'élément, le reste sera récupéré par
        // loadData() à la création de l'instance de l'élément. (choix de développement).
        String[] columns = new String[]{DB_COL_DRINK_ID};

        // Requête SELECT à la base de données.
        Cursor c = db.query(DB_TABLE_DRINKS, columns, selection, selectionArgs, null, null, Drink.order_by + " " + Drink.order);
        //Cursor c = db.query(DB_TABLES, columns, null, null, null, null, Drink.order_by + " " + Drink.order);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            // Id de l'élément.
            int drink_id = c.getInt(0);
            // L'instance de l'élément de collection est récupéré avec la méthode get(ciId)
            // (Si l'instance n'existe pas encore, elle est créée par la méthode get)
            Drink drink = Drink.get(drink_id);

            // Ajout de l'élément de collection à la liste.
            drinks.add(drink);
            c.moveToNext();
        }

        // Fermeture du curseur et de la base de données.
        c.close();
        db.close();

        return drinks;
    }


    /**
     * Fournit l'instance d'un élément de collection présent dans la base de données. Si l'élément
     * de collection n'est pas encore instancié, une instance est créée.
     *
     * @param ciId Id de l'élément de collection.
     *
     * @return L'instance de l'élément de collection.
     * @pre L'élément correspondant à l'id donné doit exister dans la base de données.
     */
    public static Drink get(int ciId) {
        Drink ci = Drink.drinkSparseArray.get(ciId);
        if (ci != null) {
            return ci;
        }
        return new Drink(ciId);
    }


    /**
     * Inverse l'ordre de tri actuel.
     *
     * @pre La valeur de Song.order est soit ASC soit DESC.
     * @post La valeur de Song.order a été modifiée et est soit ASC soit DESC.
     */
    public static void reverseOrder() {
        if (Drink.order.equals("ASC")) {
            Drink.order = "DESC";
        } else {
            Drink.order = "ASC";
        }
    }

    /**
     * Fournit une représentation sous forme de texte du morceau. Utilisé pour la liste dans
     * PlayerActivity.
     */
    public String toString() {
        return getName_drink() + " - " + getPrice() + "€";
    }
}