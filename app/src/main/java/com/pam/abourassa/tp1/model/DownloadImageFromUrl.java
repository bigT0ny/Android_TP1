package com.pam.abourassa.tp1.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.pam.abourassa.tp1.flags.FlagsProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anthony on 13/12/2016.
 */

public class DownloadImageFromUrl extends AsyncTask<String, Void, Bitmap> {
    private File cacheFile;

    public DownloadImageFromUrl(File cacheFile) {
        this.cacheFile = cacheFile;
    }

    /**
     * Methode permettant de telecharger une image depuis un lien sur un site Internet.
     */
    private Bitmap downloadImageBitmap(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Bitmap doInBackground(String... urlString) {
        return downloadImageBitmap(urlString[0]);
    }

    /**
     * L'image est sauvegardee apres le telechargement des images.
     */
    protected void onPostExecute(Bitmap result) {
        FlagsProvider.getInstance().saveImageInCacheFile(result, cacheFile);
    }

}
