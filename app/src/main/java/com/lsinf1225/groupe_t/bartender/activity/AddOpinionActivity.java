package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.MySQLiteHelper;
import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.Opinion;
import com.lsinf1225.groupe_t.bartender.model.User;

/*
     TODO : ajouter les fonctions onResume (ou onRestart, ou je sais pas quoi)
     pour que les commentaires se rechargent directement quand on en ajoute un.
 */

public class AddOpinionActivity extends Activity implements TextView.OnEditorActionListener, RatingBar.OnRatingBarChangeListener{
    private float note = -1;
    private String comment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_opinion);

        EditText comment = (EditText) findViewById(R.id.addCommentText);
        comment.setOnEditorActionListener(this);
        RatingBar note = (RatingBar) findViewById(R.id.addOpinionRatingBar);
        note.setOnRatingBarChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText comment = (EditText) findViewById(R.id.addCommentText);
        comment.setText("");
        RatingBar note = (RatingBar) findViewById(R.id.addOpinionRatingBar);
        note.setRating(0);
    }

    public void addOpinion(View v) {
        EditText comment = (EditText) findViewById(R.id.addCommentText);
        this.comment = comment.getText().toString();
        if(note != -1) {
            int id_drink = getIntent().getIntExtra("id_drink", -1);
            if(id_drink != -1) {
                if(Opinion.add(id_drink, User.getConnectedUser().getLogin(), note, this.comment)) {
                    BarTenderApp.notifyShort(R.string.opinion_added);
                    finish(); // Go back to the previous activity
                } else {
                    BarTenderApp.notifyShort(R.string.error_adding_opinion);
                }

            } else {
                BarTenderApp.notifyShort(R.string.sorry_error);
            }
        } else {
            BarTenderApp.notifyShort(R.string.please_specify_a_note_value);
        }
    }

    /**
     * Enregistre les changements de la note (rating).
     *
     * @param ratingBar La RatingBar concernée (ici il n'y en a qu'une dont l'id est
     *                  show_details_rating).
     * @param rating    La valeur de la nouvelle note (rating).
     * @param fromUser  Indique si le changement de note (rating) est effectué par l'utilisateur ou
     *                  par le programme (par exemple par appel de la méthode
     *                  ratingBar.setRating(x)).
     */
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (fromUser) {
            ratingBar.setRating(rating);
            note = rating;
        }
    }

    /**
     * Récupère les actions faites depuis le clavier.
     *
     * Récupère les actions faites depuis le clavier lors de l'édition du champ du mot de passe afin
     * de permettre de se connecter depuis le bouton "Terminer" du clavier. (Cela évite à
     * l'utilisateur de devoir fermer le clavier et de cliquer sur le bouton se connecter).
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            return true;
        }
        return false;
    }
}
