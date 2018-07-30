package com.example.sonali.myweatherapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class autoComplete extends AppCompatActivity {
    PlaceAutocompleteFragment places;
    String city=null;
    Button btn;
    Typeface custom_font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_auto_complete);

        custom_font = Typeface.createFromAsset(getAssets(),  "fonts/leaguespartan-bold.ttf");
        btn=(Button)findViewById(R.id.Button);
        btn.setTypeface(custom_font);

        places=(PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        places.setFilter(typeFilter);

        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                //Toast.makeText(autoComplete.this, place.getName(), Toast.LENGTH_SHORT).show();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1=new Intent(autoComplete.this,MainActivity.class);
                        //Toast.makeText(autoComplete.this, city, Toast.LENGTH_SHORT).show();
                        intent1.putExtra("city_name",place.getName().toString());
                        startActivity(intent1);
                    }
                });
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(autoComplete.this, status.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
