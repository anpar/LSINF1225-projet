package com.lsinf1225.groupe_t.bartender.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.Opinion;

import java.util.ArrayList;

public class MyOpinionsListAdapter extends BaseAdapter {
    /**
     * Permet d'instancier un fichier xml de layout dans une vue.
     */
    private final LayoutInflater mInflater;

    /**
     * Liste des éléments de collection à mettre dans la liste.
     */
    private ArrayList<Opinion> collectedItems;

    /**
     * Constructeur.
     *
     * @param context        Contexte de l'application.
     * @param collectedItems Liste des éléments de collection à placer dans la liste.
     */
    public MyOpinionsListAdapter(Context context, ArrayList<Opinion> collectedItems) {
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
            convertView = mInflater.inflate(R.layout.collected_comment_layout, parent, false);
        }

        // Récupération des deux éléments de notre vue dans le but d'y placer les données.
        TextView comment = (TextView) convertView.findViewById(R.id.textview_show_comments_comment);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.show_comment_rating_bar);

        // Récupération et placement des données.
        Opinion collectedItem = collectedItems.get(position);
        comment.setText(collectedItem.getComment());
        ratingBar.setRating(collectedItem.getNote());

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
    public void setCollectedItems(ArrayList<Opinion> newCollectedItems) {
        this.collectedItems = newCollectedItems;
        notifyDataSetChanged();
    }
}
