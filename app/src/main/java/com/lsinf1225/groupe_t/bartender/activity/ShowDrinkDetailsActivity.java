package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.Drink;
import com.lsinf1225.groupe_t.bartender.model.User;

import org.w3c.dom.Text;

public class ShowDrinkDetailsActivity extends Activity {
    private Drink currentDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(User.isWaiter()) {
            setContentView(R.layout.activity_show_drink_details_waiter);
        } else {
            setContentView(R.layout.activity_show_drink_details_customer);
        }
        // Récupération de l'id de la boisson ou si rien n'est trouvé, -1 est la valeur par défaut.
        int id = getIntent().getIntExtra("id_drink", -1);

        if (id == -1) {
            throw new RuntimeException("Aucun id de morceau n'a été spécifié.");
        }

        // Récupération de la boisson
        currentDrink = Drink.get(id);

        if(User.isWaiter()) {

        } else {
            // Complétition des différents champs avec les donnée.
            TextView name = (TextView) findViewById(R.id.textview_drink_detail_name);
            name.setText(currentDrink.getName_drink());

            TextView description = (TextView) findViewById(R.id.textview_drink_detail_description);
            description.setText(currentDrink.getDescription());

            TextView price = (TextView) findViewById(R.id.textview_drink_detail_price);
            price.setText(Float.toString(currentDrink.getPrice()) + "€");

            TextView volume = (TextView) findViewById(R.id.textView_drink_detail_volume);
            volume.setText(Float.toString(currentDrink.getVolume()) + "cl");

            RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
            rating.setRating(currentDrink.getRating());
        }
    }
}
