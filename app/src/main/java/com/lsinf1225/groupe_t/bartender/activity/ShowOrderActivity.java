package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.activity.adapter.MyOrdersListAdapter;
import com.lsinf1225.groupe_t.bartender.model.Order;

import java.util.ArrayList;

/**
 * Gère l'affichage sous forme de liste des éléments de la collection de l'utilisateur en cours. Si
 * une requête de recherche est passée dans l'Intent, la recherche est effectuée et la liste des
 * éléments affichés sera la liste des résultats.
 *
 * @author Damien Mercier
 * @version 1
 */
public class ShowOrderActivity extends Activity implements AdapterView.OnItemClickListener {

    private ArrayList<Order> collectedItems;
    private MyOrdersListAdapter myListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_order);

        // Chargement des éléments à afficher dans la variable de classe collectedItems
        loadCollectedItems();

        ListView myListView = (ListView) findViewById(R.id.show_DrinksListView);

        // Création de l'adapter pour faire la liaison entre les données (collectedItems) et
        // l'affichage de chaque ligne de la liste.
        myListViewAdapter = new MyOrdersListAdapter(this, collectedItems);
        myListView.setAdapter(myListViewAdapter);

        // Indique que le clic d'un élément de la liste doit appeler la méthode onItemClick d
        // cette classe (this).
        myListView.setOnItemClickListener(this);

        // Mise à jour des icones de tri afin de correspondre au tri actuel. (les options de tri
        // sont gardées en mémoire dans la classe CollectedItem tout au long de l'exécution de
        // l'application)
        updateDrawableOrder();
    }

    /**
     * Charge la liste des éléments de collection dans la variables de classe collectedItems.
     *
     * Charge la liste des éléments de la collection de l'utilisateur connecté et si une requête de
     * recherche est passée lors du lancement de l'activité, effectue la recherche et charge la
     * liste des résultats.
     */
    private void loadCollectedItems() {
        // Récupération de la requête de recherche.
        // Si aucune requête n'a été passée lors de la création de l'activité, searchQuery sera null.
        String searchQuery = getIntent().getStringExtra("searchQuery");

        if(searchQuery != null) {
            collectedItems = Order.searchDrink(searchQuery);
        } else {
            collectedItems = Order.getDrinks();
        }

        // S'il n'y a aucun éléments dans la liste, il faut afficher un message. Ce message est différent
        // s'il y avait une requête de recherche (message du type "Aucun résultat trouvé") ou si
        // l'utilisateur vient directement du menu principal et veut tout afficher (message du type
        // "Aucun élément n'est présent dans votre collection).
        if (collectedItems.isEmpty()) {
            if (searchQuery == null) {
                BarTenderApp.notifyShort(R.string.show_list_error_no_item);
            } else {
                BarTenderApp.notifyShort(R.string.show_list_no_result);
            }
            // Cloture de l'activité d'affichage de la liste (car liste vide). Retour à l'écran
            // précédent.
            finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // La liste des éléments est ici rechargées car en cas de modification d'un élément, l'ordre
        // a peut-être changé.

        loadCollectedItems();

        myListViewAdapter.setCollectedItems(collectedItems);
    }

    /**
     * Lance l'activité de vue des détails d'un élément de collection lors du clic sur un élément de
     * la liste.
     *
     * @param position Position de l'élément dans la liste.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ShowDrinkDetailsActivity.class);
        // L'id de l'élément de collection est passé en argument afin que la vue de détails puisse
        // récupérer celui-ci.
        intent.putExtra("s_id", collectedItems.get(position).getId_drink());
        startActivity(intent);
    }

    /**
     * Gère le changement du tri sur la liste.
     *
     * Cette méthode est appelée grâce à l'arttribut onClick présent dans le fichier xml de layout.
     *
     * @param view Vue sur laquelle l'utilisateur a cliqué.
     */
    public void change_order(View view) {
        // Détermine si le clic a été fait sur la colonne de nom (name) ou de note (rating).
        switch (view.getId()) {
            case R.id.show_list_name_title:
                if (Order.order_by.equals(Order.DB_COL_NAME_DRINK)) {
                    // Si le tri est déjà effectué sur les noms, il faut juste inverser l'ordre.
                    Order.reverseOrder();
                } else {
                    // Sinon il faut indiquer que le tri se fait sur les noms par ordre alphabétique (croissant)
                    Order.order_by = Order.DB_COL_NAME_DRINK;
                    Order.order = "ASC";
                }
                break;
            case R.id.show_list_price_title:
                if (Order.order_by.equals(Order.DB_COL_PRICE)) {
                    // Si le tri est déjà effectué sur les notes, il faut juste inverser l'ordre
                    Order.reverseOrder();
                } else {
                    // Sinon il faut indiquer que le tri se fait sur les notes par ordre décroissant
                    // (la meilleure note d'abord)
                    Order.order_by = Order.DB_COL_PRICE;
                    Order.order = "DESC";
                }
                break;
        }

        // Mise à jour des icônes de tri.
        updateDrawableOrder();

        // Re-chargement de la liste des éléments de collection pour prendre en compte le nouveau tri.
        loadCollectedItems();

        // Mise à jour de la liste des éléments dans l'adapter pour que l'affichage soit modifié.
        myListViewAdapter.setCollectedItems(collectedItems);
    }


    /**
     * Met à jour les icônes de tri afin qu'elles correspondent au tri actuellement en cours.
     *
     * @pre Les valeurs de CollectedItem.order et de CollectedItem.order_by sont correctement
     * définies.
     * @post Les icônes de tri sont mises à jour et correspondent aux valeurs de CollectedItem.order
     * et de CollectedItem.order_by.
     */
    private void updateDrawableOrder() {
        TextView priceTitle = (TextView) findViewById(R.id.show_list_price_title);
        TextView nameTitle = (TextView) findViewById(R.id.show_list_name_title);

        /**
         * Remise à zéro des images de tri.
         * @note : Attention, le tri par défaut pour les noms est croissant
         * (up) et celui pour les notes est décroissant (down). Il faut que cela correspondent dans
         * le comportement de la méthode change_order.
         */
        //nameTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up_inactive, 0, 0, 0);
        //priceTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down_inactive, 0, 0, 0);


        // Détermination de la colonne sur laquelle le tri est effectué.
        TextView orderTitle;
        boolean orderByRating = Order.order_by.equals(Order.DB_COL_PRICE);
        if (orderByRating) {
            orderTitle = priceTitle;
        } else {
            orderTitle = nameTitle;
        }

        // Détermination de l'ordre de tri.
        boolean orderDesc = Order.order.equals("DESC");

        // Placement de l'icône en fonction de l'ordre de tri.
        /*if (orderDesc) {
            orderTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down_active, 0, 0, 0);
        } else {
            orderTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up_active, 0, 0, 0);
        }*/
    }

}
