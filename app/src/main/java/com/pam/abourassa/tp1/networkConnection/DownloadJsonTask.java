package com.pam.abourassa.tp1.networkConnection;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anthony on 30/12/2016.
 */

/**
 * Classe permettant de faire le telechargment, en asynchrone, d'une string JSON a l'aide d'une
 * requete HTTP.
 */
public class DownloadJsonTask extends AsyncTask<String, Void, String> {

    /**
     * Methode etant executee avant le telechargement en background.
     */
    protected void onPreExecute() {

    }

    /**
     * Methode permettant de telecharger une string JSON a partir d'une url et d'une requete HTTP
     * et retourne une string representant le JSON recuperer sur le site web.
     */
    private String downloadJsonString (String urlJsonString) {
        String result = null;

        // Requete HTTP pour recuperer le JSON sous forme de string
        try {
            URL url = new URL(urlJsonString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            // Lecture du JSON qui est stocker dans une variable string
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result = line;
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Methode executee en background qui recupere le JSON sous forme de string.
     */
    @Override
    protected String doInBackground(String... params) {
        return downloadJsonString(params[0]);
    }

    /**
     * Methode etant executee apres le telechargement en background.
     */
    protected void onPostExecute() {

    }

}
