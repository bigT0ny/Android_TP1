package com.pam.abourassa.tp1.model.providers;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pam.abourassa.tp1.model.Objects.Forecast;
import com.pam.abourassa.tp1.networkConnection.DownloadImageFromUrl;
import com.pam.abourassa.tp1.networkConnection.DownloadJsonTask;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anthony on 30/12/2016.
 */

public class ForecastProvider {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?id=";
    public static final String API_KEY = "&units=metric&appid=2260c1efb70769d7c340dbc68dd980f5";
    public static final String WEATHER_ICON_URL = "http://openweathermap.org/img/w/";
    public static final String LANGUAGE_URL_ARG = "&lang=";

    private static ForecastProvider instance = new ForecastProvider();

    public static ForecastProvider getInstance () {
        return instance;
    }

    public ForecastProvider() {

    }

    public Forecast getJsonString(int cityId, Activity activity) {
        Forecast forecast = null;
        Locale current = activity.getResources().getConfiguration().locale;
        String language = LANGUAGE_URL_ARG + current;
        String urlJsonString = BASE_URL + cityId + language + API_KEY;

        try {
            String jsonString = new DownloadJsonTask().execute(urlJsonString).get();
            forecast = fromJSON(jsonString);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return forecast;
    }

    /**
     * Create a Forecast object from a JSON string
     */
    public Forecast fromJSON(String jsonString) {
        Gson gson = new GsonBuilder().create();
        Forecast forecast = gson.fromJson(jsonString, Forecast.class);
        return forecast;
    }

    public Bitmap getWeatherIcon(String iconCode) {
        Bitmap weatherIcon = null;
        String urlWeatherIcon = WEATHER_ICON_URL + iconCode + ".png";

        try {
            weatherIcon = new DownloadImageFromUrl().execute(urlWeatherIcon).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return weatherIcon;
    }

}
