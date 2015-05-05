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

public class RegisterActivity extends Activity implements TextView.OnEditorActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText loginEditText = (EditText) findViewById(R.id.register_login_field_value);
        loginEditText.setOnEditorActionListener(this);
        EditText passwordEditText = (EditText) findViewById(R.id.register_password_field_value);
        passwordEditText.setOnEditorActionListener(this);
        EditText passwordConfirmedEditText = (EditText) findViewById(R.id.register_password_confirmed_field_value);
        passwordConfirmedEditText.setOnEditorActionListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // On efface le mot de passe qui était écrit quand on se déconnecte.
        EditText loginEditText = (EditText) findViewById(R.id.register_login_field_value);
        loginEditText.setText("");
        EditText passwordEditText = (EditText) findViewById(R.id.register_password_field_value);
        passwordEditText.setText("");
        EditText passwordConfirmedEditText = (EditText) findViewById(R.id.register_password_confirmed_field_value);
        passwordConfirmedEditText.setText("");
    }

    public void register(View v) {
        EditText loginEditText = (EditText) findViewById(R.id.register_login_field_value);
        EditText passwordEditText = (EditText) findViewById(R.id.register_password_field_value);
        EditText passwordConfirmedEditText = (EditText) findViewById(R.id.register_password_confirmed_field_value);
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirmed = passwordConfirmedEditText.getText().toString();

        if(password.equals(passwordConfirmed)) {
            if(User.isNew(login)) {
                User user = new User(0, login, password, "");
                User.add(user);
                if(user.login()) {
                    Intent intent = new Intent(this, MainLoggedActivity.class);
                    startActivity(intent);
                } else {
                    BarTenderApp.notifyShort(R.string.sorry_error);
                }
            }
            else {
                BarTenderApp.notifyShort(R.string.register_username_already_exists);
            }
        } else {
            BarTenderApp.notifyShort(R.string.register_wrong_password_msg);
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
            register(v);
            return true;
        }
        return false;
    }
}