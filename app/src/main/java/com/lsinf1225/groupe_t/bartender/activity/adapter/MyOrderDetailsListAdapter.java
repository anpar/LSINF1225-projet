package com.lsinf1225.groupe_t.bartender.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.OrderDetails;

import com.lsinf1225.groupe_t.bartender.model.Order;
import com.lsinf1225.groupe_t.bartender.model.Opinion;

import java.util.ArrayList;

public class MyOrderDetailsListAdapter extends BaseAdapter{
    /**
     * Permet d'instancier un fichier xml de layout dans une vue.
     */
    private final LayoutInflater mInflater;

    /**
     * Liste des éléments de collection à mettre dans la liste.
     */
    private ArrayList<OrderDetails> collectedItems;

    /**
     * Constructeur.
     *
     * @param context        Contexte de l'application.
     * @param collectedItems Liste des éléments de collection à placer dans la liste.
     */
    public MyOrderDetailsListAdapter(Context context, ArrayList<OrderDetails> collectedItems) {
        mInflater = LayoutInflater.from(context);
        this.collectedItems = collectedItems;
    }

    @Override
    public int getCount() {
        return collectedItems.size();
    }

    @Override
    public Object getItem(int position) {
        return collectedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return collectedItems.get(position).getId_order();
    }

    /**
     * Remplit chaque ligne de la liste avec un layout particulier.
     *
     * Cette méthode est appelée par Android pour construire la vue de la liste (lors de la
     * construction de listview).
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Si la vue n'a pas encore été créé (typiquement lors du première affichage de la liste).
        // Android recycle en effet les layout déjà chargés des éléments de la liste (par exemple
        // lors du changement de l'ordre dans la liste.)

        if (convertView == null) {
            // Création d'un nouvelle vue avec le layout correspondant au fichier xml
            convertView = mInflater.inflate(R.layout.collected_order_details_layout, parent, false);
        }

        // Récupération des deux éléments de notre vue dans le but d'y placer les données.
        TextView drink = (TextView) convertView.findViewById(R.id.order_details_row_drink);
        TextView quantity = (TextView) convertView.findViewById(R.id.order_details_row_price);
        TextView price = (TextView) convertView.findViewById(R.id.order_details_row_quantity);

        // Récupération et placement des données.
        OrderDetails collectedItem = collectedItems.get(position);
        drink.setText(collectedItem.getDrink().getName_drink());
        quantity.setText(Integer.toString(collectedItem.getQuantity()));
        price.setText(Float.toString(collectedItem.getDrink().getPrice()));

        return convertView;
    }

    /**
     * Change la liste des éléments de collection affichée.
     *
     * Permet de changer complètement la liste des éléments affichés dans la liste.
     *
     * @param newCollectedItems La nouvelle liste des éléments de collection à afficher.
     *
     * @post Les éléments de la liste ont été remplacés par les éléments passés en argument.
     */
    public void setCollectedItems(ArrayList<OrderDetails> newCollectedItems) {
        this.collectedItems = newCollectedItems;
        notifyDataSetChanged();
    }
}

