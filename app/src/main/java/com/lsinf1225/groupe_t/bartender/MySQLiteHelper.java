package com.lsinf1225.groupe_t.bartender;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_SQL_FILENAME = "database.sql";
    private static final String DATABASE_NAME = "database.sqlite";
    private static final int DATABASE_VERSION = 3;

    private static MySQLiteHelper instance;

    /**
     * Constructeur. Instancie l'utilitaire de gestion de la base de données.
     *
     * @param context Contexte de l'application.
     */
    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        instance = this;
    }

    /**
     * Fournit une instance de notre com.lsinf1225.groupe_t.bartender.activity.MySQLiteHelper.
     *
     * @return com.lsinf1225.groupe_t.bartender.activity.MySQLiteHelper
     */
    public static MySQLiteHelper get() {
        if (instance == null) {
            return new MySQLiteHelper(BarTenderApp.getContext());
        }
        return instance;
    }

    /**
     * Méthode d'initialisation appelée lors de la création de la base de données.
     *
     * @param db Base de données à initialiser
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        initDatabase(db);
    }

    /**
     * Méthode de mise à jour lors du changement de version de la base de données.
     *
     * @param db         Base de données à mettre à jour.
     * @param oldVersion Numéro de l'ancienne version.
     * @param newVersion Numéro de la nouvelle version.
     *
     * @pre La base de données est dans la version oldVersion.
     * @post La base de données a été mise à jour vers la version newVersion.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * @note : Ici on se contente juste de supprimer toutes les données et de les re-créer par
         * après. Dans une vraie application en production (par ex. sur le Play Store), il faudra
         * faire en sorte que les données enregistrées par l'utilisateur ne soient pas complètement
         * effacées lorsqu'on veut mettre à jour la structure de la base de données.
         */
        deleteDatabase(db);
        onCreate(db);
    }

    /**
     * Crée les tables de la base de données et les remplit.
     *
     * @param db Base de données à initialiser.
     *
     * @note À l'avenir on peut imaginer aller chercher les requêtes à effectuer dans un fichier
     * local (dans le dossier assets) ou sur internet (sur un server distant), au lieu de les
     * encoder en dur ici. (En fait c’est une mauvaise pratique de les encoder en dur comme on a
     * fait ici, mais on a voulu simplifier le code pour des raisons didactiques.) Vous trouverez en
     * commentaires dans cette méthode le code permettant de charger la base de données depuis un
     * fichier SQL placé dans le dossier assets/.
     * @post Les tables nécessaires à l'application sont créées et les données initiales y sont
     * enregistrées.
     */
    private void initDatabase(SQLiteDatabase db) {
        try {
            // Ouverture du fichier sql.
            BufferedReader in = new BufferedReader(new InputStreamReader(BarTenderApp.getContext().getAssets().open(DATABASE_SQL_FILENAME)));

            String line;
            // Parcourt du fichier ligne par ligne.
            while ((line = in.readLine()) != null) {
                /**
                 * @note : Pour des raisons de facilité, on ne prend en charge ici que les fichiers
                 * contenant une instruction par ligne. Si des instructions SQL se trouvent sur deux
                 * lignes, cela produira des erreurs (car l'instruction sera coupée)
                 */
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    Log.d("MySQL query", line);
                    db.execSQL(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier " + DATABASE_SQL_FILENAME + " : " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la création de la base de données." +
                    "Vérifiez que chaque instruction SQL est au plus sur une ligne." +
                    "L'erreur est : " + e.getMessage(), e);
        }

    }


    /**
     * Supprime toutes les tables dans la base de données.
     *
     * @param db Base de données.
     *
     * @post Les tables de la base de données passées en argument sont effacées.
     */
    private void deleteDatabase(SQLiteDatabase db) {
        Cursor c = db.query("sqlite_master", new String[]{"name"}, "type = 'table' AND name NOT LIKE '%sqlite_%'", null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            db.execSQL("DROP TABLE IF EXISTS " + c.getString(0));
            c.moveToNext();
        }
    }
}
