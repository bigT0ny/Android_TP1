package com.pam.abourassa.tp1.networkConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anthony on 13/12/2016.
 */

public class DownloadImageFromUrl extends AsyncTask<String, Void, Bitmap> {
    private File cacheFile;

    public DownloadImageFromUrl() {

    }

    public DownloadImageFromUrl(File cacheFile) {
        this.cacheFile = cacheFile;
    }

    /**
     * Methode permettant de telecharger une image depuis un lien sur un site Internet.
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

    @Override
    protected Bitmap doInBackground(String... urlString) {
        return downloadFlagBitmap(urlString[0]);
    }

    protected void onPostExecute(Bitmap result) {

    }

}
