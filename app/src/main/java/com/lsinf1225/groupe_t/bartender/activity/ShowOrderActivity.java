package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.activity.adapter.MyOrdersListAdapter;
import com.lsinf1225.groupe_t.bartender.model.Bill;
import com.lsinf1225.groupe_t.bartender.model.Order;
import com.lsinf1225.groupe_t.bartender.model.User;

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

        ListView myListView = (ListView) findViewById(R.id.show_OrderView);

        int table_number = getIntent().getIntExtra("table_number", -1);
        Button closeBill = (Button) findViewById(R.id.button_close_bill);
        Button newOrder = (Button) findViewById(R.id.new_bill_table_number);
        EditText newOrderTable = (EditText) findViewById(R.id.show_order_table_number);

        if(table_number == -1) {
            closeBill.setVisibility(Button.INVISIBLE);
        } else {
            newOrder.setVisibility(Button.INVISIBLE);
            newOrderTable.setVisibility(Button.INVISIBLE);
        }

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
        int table_number = getIntent().getIntExtra("table_number", -1);

        if(table_number != -1) {
            String where = "table_number = ?";
            String[] whereArgs = new String[]{Integer.toString(table_number)};
            collectedItems = Order.getOrders(where, whereArgs);
        } else {
            collectedItems = Order.getOrders();
        }


        // S'il n'y a aucun éléments dans la liste, il faut afficher un message. Ce message est différent
        // s'il y avait une requête de recherche (message du type "Aucun résultat trouvé") ou si
        // l'utilisateur vient directement du menu principal et veut tout afficher (message du type
        // "Aucun élément n'est présent dans votre collection).
        if (collectedItems.isEmpty()) {
            if (table_number == -1) {
                BarTenderApp.notifyShort(R.string.show_list_no_order);
            }
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
        Intent intent = new Intent(this, ShowOrderDetailsActivity.class);
        // L'id de l'élément de collection est passé en argument afin que la vue de détails puisse
        // récupérer celui-ci.
        intent.putExtra("id_order", collectedItems.get(position).getId());
        startActivity(intent);
    }


    public void newOrder(View v) {

            Intent intent = new Intent(this, ShowMenuActivity.class);
            EditText table_numberText = (EditText) findViewById(R.id.show_order_table_number);

            String tableString = table_numberText.getText().toString();
            if (tableString.matches("")){
                BarTenderApp.notifyShort(R.string.no_table_number);
            }
            else {
                int table_number = Integer.parseInt(tableString);
                int id_order = Order.addOrder(table_number);

                if (table_number == -1) {
                    BarTenderApp.notifyShort(R.string.sorry_error);

                } else {
                    intent.putExtra("id_order", id_order);
                    startActivity(intent);
                }
            }
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
            case R.id.show_list_id:
                if (Order.order_by.equals(Order.DB_COL_ID)) {
                    // Si le tri est déjà effectué sur les noms, il faut juste inverser l'ordre.
                    Order.reverseOrder();
                } else {
                    // Sinon il faut indiquer que le tri se fait sur les noms par ordre alphabétique (croissant)
                    Order.order_by = Order.DB_COL_ID;
                    Order.order = "ASC";
                }
                break;
            case R.id.show_list_waiter:
                if (Order.order_by.equals(Order.DB_COL_LOGIN_WAITER)) {
                    // Si le tri est déjà effectué sur les notes, il faut juste inverser l'ordre
                    Order.reverseOrder();
                } else {
                    // Sinon il faut indiquer que le tri se fait sur les notes par ordre décroissant
                    // (la meilleure note d'abord)
                    Order.order_by = Order.DB_COL_LOGIN_WAITER;
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
        TextView priceTitle = (TextView) findViewById(R.id.show_list_waiter);
        TextView nameTitle = (TextView) findViewById(R.id.show_list_id);
    }

    public void closeBill(View v) {
        int table_number = getIntent().getIntExtra("table_number", -1);

        if(table_number != -1) {
            if(Bill.close(table_number)) {
                BarTenderApp.notifyShort(R.string.bill_closed);
                onResume();
            }
        } else {
            BarTenderApp.notifyShort(R.string.sorry_error);
        }
    }
}
