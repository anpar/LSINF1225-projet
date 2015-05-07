package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.activity.adapter.MyDrinksListAdapter;
import com.lsinf1225.groupe_t.bartender.activity.adapter.MyOpinionsListAdapter;
import com.lsinf1225.groupe_t.bartender.model.Drink;
import com.lsinf1225.groupe_t.bartender.model.Opinion;
import com.lsinf1225.groupe_t.bartender.model.User;

import java.util.ArrayList;

public class ShowCommentsActivity extends Activity {
    private ArrayList<Opinion> collectedItems;
    private MyOpinionsListAdapter myListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

        Button addComment = (Button) findViewById(R.id.button_add_opinion_activity);
        if(User.isWaiter()) {
            addComment.setEnabled(false);
        }

        loadCollectedItems();

        ListView myListView = (ListView) findViewById(R.id.show_OpinionsListView);

        // Création de l'adapter pour faire la liaison entre les données (collectedItems) et
        // l'affichage de chaque ligne de la liste.

        myListViewAdapter = new MyOpinionsListAdapter(this, collectedItems);
        if(!collectedItems.isEmpty() ) {
            myListView.setAdapter(myListViewAdapter);
        }
    }

    public void addComment(View v) {
        if(User.getConnectedUser() == null) {
            BarTenderApp.notifyShort(R.string.you_have_to_be_connected);
        } else {
            Opinion comp = new Opinion(1,User.getConnectedUser().getLogin(), 0,"");
            if (!collectedItems.contains(comp)) {
                Intent intent = new Intent(this, AddOpinionActivity.class);
                int id_drink = getIntent().getIntExtra("id_drink", -1);
                if(id_drink != -1) {
                    intent.putExtra("id_drink", id_drink);
                    startActivity(intent);
                } else {
                    BarTenderApp.notifyShort(R.string.sorry_error);
                }
            } else {
                BarTenderApp.notifyShort(R.string.show_msg_error_already_commented);
            }
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
        int id_drink = getIntent().getIntExtra("id_drink", -1);

        if(id_drink != -1) {
            collectedItems = Opinion.getComments(id_drink);
        } else {
            BarTenderApp.notifyShort(R.string.error_retrieving_opinions);
        }

        // S'il n'y a aucun éléments dans la liste, il faut afficher un message. Ce message est différent
        // s'il y avait une requête de recherche (message du type "Aucun résultat trouvé") ou si
        // l'utilisateur vient directement du menu principal et veut tout afficher (message du type
        // "Aucun élément n'est présent dans votre collection).
        if (collectedItems.isEmpty()) {
            BarTenderApp.notifyShort(R.string.show_now_comment);
        }
    }
}
