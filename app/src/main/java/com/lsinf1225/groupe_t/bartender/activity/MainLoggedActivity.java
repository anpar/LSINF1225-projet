package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.User;

public class MainLoggedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logged);

        TextView welcomeTxt = (TextView) findViewById(R.id.welcome_text);
        welcomeTxt.setText(getString(R.string.welcome_text) + " " + User.getConnectedUser().getLogin());
    }

    public void logout(View v) {
        User.logout();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Désactive le bouton de retour. Désactive le retour à l'activité précédente (donc l'écran de
     * connexion dans ce cas-ci) et affiche un message indiquant qu'il faut se déconnecter.
     */
    @Override
    public void onBackPressed() {
        // On désactive le retour (car on se trouve au menu principal) en ne faisant
        // rien dans cette méthode si ce n'est afficher un message à l'utilisateur.
        BarTenderApp.notifyShort(R.string.main_back_button_disable);
    }
}
