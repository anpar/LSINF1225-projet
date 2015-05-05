package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
        finish();
    }
}
