package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.Bill;
import com.lsinf1225.groupe_t.bartender.model.Order;

import java.util.ArrayList;

public class AddBillActivity extends Activity implements TextView.OnEditorActionListener{

    private Spinner userSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        /**
         * @note La liste des utilisateurs est affichées dans un Spinner, pour en savoir plus lisez
         * http://d.android.com/guide/topics/ui/controls/spinner.html
         */

        userSpinner = (Spinner) findViewById(R.id.spinner_add_bill);
        Button newBill = (Button) findViewById(R.id.button_new_bill);

        // Obtention de la liste des utilisateurs.
        ArrayList<Integer> tables = Order.getAllTable();
        if(tables.isEmpty()) {
            newBill.setEnabled(false);
        }

        // Création d'un ArrayAdapter en utilisant la liste des utilisateurs et un layout pour le spinner existant dans Android.
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, tables);
        // On lie l'adapter au spinner.
        userSpinner.setAdapter(adapter);
    }

    public void newBill(View v) {
        int table_number = (Integer) userSpinner.getSelectedItem();

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
