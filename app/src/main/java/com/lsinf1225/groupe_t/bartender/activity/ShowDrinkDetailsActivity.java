package com.lsinf1225.groupe_t.bartender.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lsinf1225.groupe_t.bartender.BarTenderApp;
import com.lsinf1225.groupe_t.bartender.R;
import com.lsinf1225.groupe_t.bartender.model.Drink;
import com.lsinf1225.groupe_t.bartender.model.OrderDetails;
import com.lsinf1225.groupe_t.bartender.model.User;

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
            throw new RuntimeException("Aucun id de boisson n'a été spécifié.");
        }

        int id_order = getIntent().getIntExtra("id_order",-1);
        if (id_order == -1){
            Button AddButton=(Button) findViewById(R.id.add_drink);
            AddButton.setVisibility(View.INVISIBLE);
            TextView X=(TextView) findViewById(R.id.multiplicator);
            X.setVisibility(View.INVISIBLE);

        }

        // Récupération de la boisson
        currentDrink = Drink.get(id);

        if(User.isWaiter()) {
            TextView name = (TextView) findViewById(R.id.textview_drink_detail_name_c);
            name.setText(currentDrink.getName_drink());

            TextView price = (TextView) findViewById(R.id.textview_drink_detail_price_c);
            price.setText(Float.toString(currentDrink.getPrice()) + "€");

            TextView volume = (TextView) findViewById(R.id.textview_drink_detail_volume_c);
            volume.setText(Float.toString(currentDrink.getVolume()) + "cl");

            TextView stockmax = (TextView) findViewById(R.id.textview_drink_detail_stock_max);
            stockmax.setText(Float.toString(currentDrink.getMax_stock()));

            TextView stock = (TextView) findViewById(R.id.textview_drink_detail_stock);
            stock.setText(Float.toString(currentDrink.getAvailable_quantity()));

            TextView threshold = (TextView) findViewById(R.id.textview_drink_detail_threshold);
            threshold.setText(Float.toString(currentDrink.getThreshold()));

            RatingBar rating = (RatingBar) findViewById(R.id.ratingBar2);
            rating.setRating(currentDrink.getRating());

            ProgressBar stockbar = (ProgressBar) findViewById(R.id.progressBar);
            stockbar.setMax(currentDrink.getMax_stock());
            stockbar.setProgress(currentDrink.getAvailable_quantity());

            if(currentDrink.getThreshold() >= currentDrink.getAvailable_quantity()) {
                stockbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            } else {
                stockbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            }

        } else {
            // Complétition des différents champs avec les donnée.
            TextView name = (TextView) findViewById(R.id.textview_drink_detail_name_c);
            name.setText(currentDrink.getName_drink());

            TextView description = (TextView) findViewById(R.id.textview_drink_detail_description);
            description.setText(currentDrink.getDescription());

            TextView price = (TextView) findViewById(R.id.textview_drink_detail_price_c);
            price.setText(Float.toString(currentDrink.getPrice()) + "€");

            TextView volume = (TextView) findViewById(R.id.textView_drink_detail_volume);
            volume.setText(Float.toString(currentDrink.getVolume()) + "cl");

            RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
            rating.setRating(currentDrink.getRating());
        }
    }

    public void showComment(View v) {
        Intent intent = new Intent(this, ShowCommentsActivity.class);
        intent.putExtra("id_drink", currentDrink.getId_drink());
        startActivity(intent);
    }

    public void addDrinkToOrder (View v){


        EditText quantityText = (EditText) findViewById(R.id.drink_quantity);
        String quantityString = quantityText.getText().toString();
        if (quantityString.matches("")){
            BarTenderApp.notifyShort(R.string.no_quantity);
        }
        else {
            int quantity = Integer.parseInt(quantityText.getText().toString());
            if (quantity <= 0){
                BarTenderApp.notifyShort(R.string.nonvalid_quantity);
            }
            if (!Drink.upDateStock(currentDrink.getId_drink(), quantity)) {
                BarTenderApp.notifyShort(R.string.not_enough_drinks);
            } else {
                OrderDetails.addDrink(getIntent().getIntExtra("id_order", -1), quantity, currentDrink.getId_drink());
                BarTenderApp.notifyShort(R.string.add__drink_succeed);
                finish();
            }
        }
    }
}
