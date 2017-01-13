package com.pam.abourassa.tp1.networkConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anthony on 13/12/2016.
 */

/**
 * Classe permettant d'effectuer le telechargement d'une image, sous forme de bitmap, a partir d'une
 * url et d'une requete HTTP.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    /**
     * Contructeur vide utiliser dans le forecastProvider pour effectuer le telechargement de l'icone
     * du temps en cours de la ville.
     */
    public DownloadImageTask () {

    }

    /**
     * Methode permettant le telechargement d'une image (bitmap) a l'aide d'une url et retourne un
     * bitmap qui servira a faire afficher l'image recuperee depuis la requete HTTP.
     */
    private Bitmap downloadFlagBitmap(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            return BitmapFactory.decodeStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Methode executee en background qui recupere une image sous forme de bitmap.
     */
    @Override
    protected Bitmap doInBackground(String... urlString) {
        return downloadFlagBitmap(urlString[0]);
    }

    /**
     * Methode etant executee apres le telechargement en background.
     */
    protected void onPostExecute(Bitmap result) {

    }

}
