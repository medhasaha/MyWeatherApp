package com.example.sonali.myweatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

/**
 * Created by Sonali on 30-06-2018.
 */



public class weather {
    // private static final String OPEN_WEATHER_APP_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_APP_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
     private static final String OPEN_WEATHER_APP_API = "6d20f4da42d6244327d923c856685e8d";

    public static String setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch(id) {
                case 2 : icon = "&#xf01e;";
                    break;
                case 3 : icon = "&#xf01c;";
                    break;
                case 7 : icon = "&#xf014;";
                    break;
                case 8 : icon = "&#xf013;";
                    break;
                case 6 : icon = "&#xf01b;";
                    break;
                case 5 : icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }

    public interface AsyncResponse {
        void processFinish(String output1, String output2, String output3, String output4, String output5, String output6,String output7);
    }

    public static class myAsync extends AsyncTask<String, Void, JSONObject> {
        public AsyncResponse delegate = null;

        public myAsync(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

           /* JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJson(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }


            return jsonWeather;*/

                URL url = null;
                HttpURLConnection httpURLConnection = null;
                JSONObject jsonObject = null;
                try {
                    //url = new URL(String.format(OPEN_WEATHER_APP_URL, params[0], params[1]));
                    url = new URL(String.format(OPEN_WEATHER_APP_URL, params[0]));
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.addRequestProperty("x-api-key", OPEN_WEATHER_APP_API);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer json = new StringBuffer(1024);
                    String line = "";
                    while (line != null) {
                        line = bufferedReader.readLine();
                        json.append(line);
                        json.append("\n");
                    }
                    bufferedReader.close();

                    jsonObject = new JSONObject(json.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d("Error", "Cannot process JSON results", e);
                }
                return jsonObject;
            }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                String description = null;
                int id=0;
                String name = jsonObject.getString("name");
               // String name="mimi";
                JSONArray weather = jsonObject.getJSONArray("weather");
                for (int i = 0; i < weather.length(); i++) {
                    JSONObject weather_object = weather.getJSONObject(i);
                    id=weather_object.getInt("id");
                    description = weather_object.getString("description");
                }
                JSONObject main = jsonObject.getJSONObject("main");
                String temp = main.getInt("temp")+" F";
                String pressure = main.getString("pressure")+ " hPa";
                String humidity = main.getString("humidity")+ " %";
                DateFormat df = DateFormat.getDateTimeInstance();
                String updatedOn = df.format(new Date(jsonObject.getLong("dt") * 1000));
                String iconText = setWeatherIcon(id, jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                        jsonObject.getJSONObject("sys").getLong("sunset") * 1000);

                delegate.processFinish(name, description, temp, humidity, pressure, updatedOn,iconText);

            } catch (JSONException e) {
                Log.d("Error", "Cannot process JSON results", e);
            }

        }
    }
}
