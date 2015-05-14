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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            login(v);
            return true;
        }
        return false;
    }
}
