package com.lsinf1225.groupe_t.bartender.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.activity.adapter.MyOpinionsListAdapter;
import com.lsinf1225.groupe_t.bartender.model.Drink;
import com.lsinf1225.groupe_t.bartender.model.Opinion;
import com.lsinf1225.groupe_t.bartender.activity.adapter.MyOrderDetailsListAdapter
import com.lsinf1225.groupe_t.bartender.model.Order;
import com.lsinf1225.groupe_t.bartender.model.OrderDetails;
import com.lsinf1225.groupe_t.bartender.model.User;

import java.util.ArrayList;

public class ShowOrderDetailsActivity extends ActionBarActivity {
    private Order currentOrder;

        private ArrayList<OrderDetails> collectedItems;
        private MyOrderDetailsListAdapter myListViewAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_show_order_details);


            loadCollectedItems();

            ListView myListView = (ListView) findViewById(R.id.show_order_details_ListView);

            // Création de l'adapter pour faire la liaison entre les données (collectedItems) et
            // l'affichage de chaque ligne de la liste.

            myListViewAdapter = new MyOrderDetailsListAdapter(this, collectedItems);
            if(!collectedItems.isEmpty() ) {
                myListView.setAdapter(myListViewAdapter);
            }
    }

    /**
     * Charge la liste des éléments de collection dans la variables de classe collectedItems.
     *
     * Charge la liste des éléments de la collection de l'utilisateur connecté et si une requête de
     * recherche est passée lors du lancement de l'activité, effectue la recherche et charge la
     * liste des résultats.
     */
    private void loadCollectedItems() {
        int id_drink = getIntent().getIntExtra("id_order", -1);

        if(id_order != -1) {
            collectedItems = OrderDetails.getOrderDetails(id_order);
        } else {
            BarTenderApp.notifyShort(R.string.error_retrieving_order_details);
        }

        // S'il n'y a aucun éléments dans la liste, il faut afficher un message. Ce message est différent
        // s'il y avait une requête de recherche (message du type "Aucun résultat trouvé") ou si
        // l'utilisateur vient directement du menu principal et veut tout afficher (message du type
        // "Aucun élément n'est présent dans votre collection).
        if (collectedItems.isEmpty()) {
            BarTenderApp.notifyShort(R.string.show_now_order_details);
        }
    }
}
