package com.lsinf1225.groupe_t.bartender.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.Order;

import java.util.ArrayList;

/**
 * Gère l'affichage personnalisé de notre liste.
 *
 * Cette classe permet de créer un Adapter personnalisé pour notre liste d'éléments de collection.
 * De cette manière il nous est possible d'utiliser un layout particulier (ici
 * collected_item_row.xml) pour chaque ligne reprenant le nom de l'élément et sa note (rating).
 *
 * @author Damien Mercier
 * @version 1
 * @see <a href="http://d.android.com/reference/android/widget/Adapter.html">Adapter</a>
 * @see <a href="http://d.android.com/reference/android/widget/BaseAdapter.html">BaseAdapter</a>
 */
public class MyOrdersListAdapter extends BaseAdapter {
    /**
     * Permet d'instancier un fichier xml de layout dans une vue.
     */
    private final LayoutInflater mInflater;

    /**
     * Liste des éléments de collection à mettre dans la liste.
     */
    private ArrayList<Order> collectedItems;

    /**
     * Constructeur.
     *
     * @param context        Contexte de l'application.
     * @param collectedItems Liste des éléments de collection à placer dans la liste.
     */
    public MyOrdersListAdapter(Context context, ArrayList<Order> collectedItems) {
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
        return collectedItems.get(position).getId_drink();
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
            convertView = mInflater.inflate(R.layout.collected_drink_layout, parent, false);
        }

        // Récupération des deux éléments de notre vue dans le but d'y placer les données.
        TextView nameTextView = (TextView) convertView.findViewById(R.id.show_row_name);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.show_row_price);

        // Récupération et placement des données.
        Order collectedItem = collectedItems.get(position);
        nameTextView.setText(collectedItem.getName_drink() + " (" + Float.toString(collectedItem.getVolume()) + "cl)");
        priceTextView.setText(Float.toString(collectedItem.getPrice()) + "€");

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
    public void setCollectedItems(ArrayList<Order> newCollectedItems) {
        this.collectedItems = newCollectedItems;
        notifyDataSetChanged();
    }
}
