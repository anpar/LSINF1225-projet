package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.Bill;

public class AddBillActivity extends Activity implements TextView.OnEditorActionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        EditText tableNumber = (EditText) findViewById(R.id.new_bill_table_number);
        tableNumber.setOnEditorActionListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // On efface le mot de passe qui était écrit quand on se déconnecte.
        EditText tableNumber = (EditText) findViewById(R.id.new_bill_table_number);
        tableNumber.setText("");
    }

    public void newBill(View v) {
        EditText tableNumber = (EditText) findViewById(R.id.new_bill_table_number);
        int table_number = Integer.parseInt(tableNumber.getText().toString());

        Log.d("newBill", "table_number = " + table_number);
        if(Bill.addBill(table_number)) {
            BarTenderApp.notifyShort(R.string.add_bill_success);
            finish();
        } else {
            BarTenderApp.notifyShort(R.string.error_adding_bill);
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
            newBill(v);
            return true;
        }
        return false;
    }
}
