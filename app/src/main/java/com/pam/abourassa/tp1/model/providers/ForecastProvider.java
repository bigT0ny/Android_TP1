package com.pam.abourassa.tp1.model.providers;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pam.abourassa.tp1.model.Objects.Forecast;
import com.pam.abourassa.tp1.networkConnection.DownloadImageTask;
import com.pam.abourassa.tp1.networkConnection.DownloadJsonTask;

import java.util.concurrent.ExecutionException;

/**
 * Created by Anthony on 30/12/2016.
 */

/**
 * ForecastProvider permettant de gerer le JSON provenant d'une url. Un objet JSON est recuperer,
 * sous forme de string, a l'aide d'une url et d'une requete HTTP. Une fois le string JSON recuperer,
 * il est utiliser afin de le convertir en un objet Forecast a l'aide de la libraire GSON de google.
 */
public class ForecastProvider {
    // Url de base de l'adresse du site web
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?id=";
    // Cle API unique permettant de recuperer des donnees sur le site web
    public static final String API_KEY = "&units=metric&appid=2260c1efb70769d7c340dbc68dd980f5";
    // Url de base permettant de recuper l'icone de la temperature en cours de la ville
    public static final String WEATHER_ICON_URL = "http://openweathermap.org/img/w/";
    // Partie de l'url permettant de choisir la langue des informations retournees par le JSON
    public static final String LANGUAGE_URL_ARG = "&lang=";
    private static ForecastProvider instance = new ForecastProvider();

    /**
     * Retourne une instance de FlagsProvider afin d'avoir acces aux methodes du flagProvider.
     */
    public static ForecastProvider getInstance () {
        return instance;
    }

    // Constructeur vide
    public ForecastProvider() {

    }

    /**
     *
     * Methode permettant de construire une url complete avec differents parametres afin de recuperer
     * un JSON sous forme de string a l'aide de l'url. Le cityId permet d'indiquer la ville dont on
     * veut recuperer les previsions et la langue permet d'avoir ces previsions selon la langue desiree.
     */
    public Forecast getJsonString(Activity activity, int cityId, String applicationLanguage) {
        Forecast forecast = null;
        // Partie de l'url indiquant la langue choisie pour recuperer les informations
        String argLanguage = LANGUAGE_URL_ARG + applicationLanguage;
        // Url complete avec toutes les informations desirees
        String urlJsonString = BASE_URL + cityId + argLanguage + API_KEY;

        try {
            // Telechargerment asynchrone de la string JSON a l'aide d'une url
            String jsonString = new DownloadJsonTask().execute(urlJsonString).get();
            // Conversion de la string JSON sous forme d'objet Forecast
            forecast = fromJSON(jsonString);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return forecast;
    }

    /**
     * Methode permettant de passer en parametre une string JSON et de la convertir en objet Forecast
     * a l'aide de la librairie GSON de google. L'objet forecast contient alors toutes les previsions
     * meteo de la ville.
     */
    public Forecast fromJSON(String jsonString) {
        Gson gson = new GsonBuilder().create();
        Forecast forecast = gson.fromJson(jsonString, Forecast.class);
        return forecast;
    }

    /**
     * Methode permettant de recuperer l'icone du temps en cours de la ville a l'aide d'une url. Le
     * telechargement de l'image est fait en asychrone.
     */
    public Bitmap getWeatherIcon(String iconCode) {
        String urlWeatherIcon = WEATHER_ICON_URL + iconCode + ".png";
        Bitmap weatherIcon = null;

        try {
            weatherIcon = new DownloadImageTask().execute(urlWeatherIcon).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return weatherIcon;
    }

}
