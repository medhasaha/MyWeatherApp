package com.example.sonali.myweatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    Typeface weatherFont;
    String city_name="London";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");

        //SharedPreferences sharedPref=getSharedPreferences("city_preference", Context.MODE_PRIVATE);
       // String city_name=sharedPref.getString("city_name","London");

        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        humidity_field = (TextView)findViewById(R.id.humidity_field);
        pressure_field = (TextView)findViewById(R.id.pressure_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

      Bundle bundle=getIntent().getExtras();
      if(bundle!=null)
        city_name = bundle.getString("city_name","London");


        weather.myAsync asyncTask =new weather.myAsync(new weather.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature,
                                      String weather_humidity, String weather_pressure, String weather_updatedOn,
                                      String weather_iconText) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                humidity_field.setText(weather_humidity);
                pressure_field.setText(weather_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

            }
        });
       // SharedPreferences sharedPref=getSharedPreferences("city_preference", Context.MODE_PRIVATE);
       // SharedPreferences.Editor editor=sharedPref.edit();
       // editor.putString("city_name",city_name);
        //editor.apply();
        asyncTask.execute(city_name);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menu_inflator=getMenuInflater();
        menu_inflator.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==(R.id.list_item))
        {
            Intent intent=new Intent(MainActivity.this,autoComplete.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
