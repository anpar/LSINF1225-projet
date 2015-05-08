package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.Drink;

import java.util.ArrayList;

public class SearchActivity extends Activity  implements TextView.OnEditorActionListener{

    private Spinner catSpinner;
    private Spinner subcatSpinner;
    ArrayList<String> catlist;
    ArrayList<String> subcatlist;
    ArrayAdapter<String> catadapter;
    ArrayAdapter<String> subcatadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText name = (EditText) findViewById(R.id.name_edit_text_search);
        name.setOnEditorActionListener(this);

        catSpinner = (Spinner) findViewById(R.id.spinner_cat);
        catlist = Drink.getCategories();
        catadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, catlist);
        catSpinner.setAdapter(catadapter);

        subcatSpinner= (Spinner) findViewById(R.id.spinner_subcat);
        subcatlist = Drink.getSubcategories();
        subcatadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, subcatlist);
        subcatSpinner.setAdapter(subcatadapter);

        EditText priceMin = (EditText) findViewById(R.id.editText_min_price);
        priceMin.setOnEditorActionListener(this);
        EditText priceMax = (EditText) findViewById(R.id.editText_max_price);
        priceMax.setOnEditorActionListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText name = (EditText) findViewById(R.id.name_edit_text_search);
        name.setText("");
        EditText priceMin = (EditText) findViewById(R.id.editText_min_price);
        priceMin.setText("");
        EditText priceMax = (EditText) findViewById(R.id.editText_max_price);
        priceMax.setText("");
    }

    public void search(View v) {
        Intent intent = new Intent(this, ShowMenuActivity.class);
        intent.putExtra("searchQuery", "ok");

        EditText nameText= (EditText) findViewById(R.id.name_edit_text_search);
        String name=nameText.getText().toString();
        if(!name.matches("")) {
            intent.putExtra("name", name);
        }
        String catText = (String)catSpinner.getSelectedItem();
        if(!catText.matches("")) {
            intent.putExtra("cat", catText);
        }
        String subcatText = (String)subcatSpinner.getSelectedItem();
        if(!subcatText.matches("")) {
            intent.putExtra("subcat", subcatText);
        }

        EditText pminText= (EditText) findViewById(R.id.editText_min_price);
        if(!pminText.getText().toString().matches("")) {
            Float pmin=Float.parseFloat(pminText.getText().toString());
            intent.putExtra("pmin", pmin);
        }

        EditText pmaxText= (EditText) findViewById(R.id.editText_max_price);
        if(!pmaxText.getText().toString().matches("")) {
            Float pmax=Float.parseFloat(pmaxText.getText().toString());
            intent.putExtra("pmax", pmax);
        }

        startActivity(intent);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            search(v);
            return true;
        }
        return false;
    }
}
