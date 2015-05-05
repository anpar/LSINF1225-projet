package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.User;

public class LoginActivity extends Activity implements TextView.OnEditorActionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText loginEditText = (EditText) findViewById(R.id.login_field_value);
        loginEditText.setOnEditorActionListener(this);
        EditText passwordEditText = (EditText) findViewById(R.id.password_field_value);
        passwordEditText.setOnEditorActionListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // On efface le mot de passe qui était écrit quand on se déconnecte.
        EditText loginEditText = (EditText) findViewById(R.id.login_field_value);
        loginEditText.setText("");
        EditText passwordEditText = (EditText) findViewById(R.id.password_field_value);
        passwordEditText.setText("");
    }

    /**
     * Vérifie le mot de passe et connecte l'utilisateur.
     *
     * Cette méthode vérifie le mot de passe saisi. Si celui-ci est bon, connecte l'utilisateur et
     * affiche le menu principal, sinon un message est affiché à l'utilisateur.
     *
     * Cette méthode est appelée grâce à l'attribut onClick indiqué dans le fichier xml de layout
     * sur le bouton de connexion. Elle peut également être appelée depuis la méthode
     * "onEditorAction" de cette classe.
     *
     * @param v Une vue quelconque (n'est pas utilisé ici, mais requis par le onClick)
     */
    public void login(View v) {
        EditText loginEditText = (EditText) findViewById(R.id.login_field_value);
        EditText passwordEditText = (EditText) findViewById(R.id.password_field_value);
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        User user = new User(0, login, password, "");

        if (user.login()) {
            Intent intent = new Intent(this, MainLoggedActivity.class);
            startActivity(intent);
        } else {
            BarTenderApp.notifyShort(R.string.login_wrong_password_msg);
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
            login(v);
            return true;
        }
        return false;
    }


}
