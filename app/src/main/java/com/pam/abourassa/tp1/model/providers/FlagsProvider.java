package com.pam.abourassa.tp1.model.providers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Anthony on 28/12/2016.
 */

/**
 * Provider permettant la gestion des drapeaux des pays. Celui-ci gere la sauvegarde de l'image (bitmap)
 * dans la cache de l'application et le telechargement de celle-ci depuis cette cache.
 */
public class FlagsProvider {
    public static final String SAVE_IMAGE_ERROR = "Save image error";
    public static final String LOAD_IMAGE_ERROR = "Load image error";

    private static FlagsProvider instance = new FlagsProvider();

    /*
     * Retourne une instance de FlagsProvider afin d'avoir acces aux methodes du flagProvider.
     */
    public static FlagsProvider getInstance () {
        return instance;
    }

    /*
     * Methode permettant de sauvegarder une image (bitmap) dans le dossier cache de l'application.
     */
    public void saveImageInCacheFile(Bitmap bitmap, File cacheFile) {
        if (bitmap != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                Log.i(SAVE_IMAGE_ERROR, "An error occurred during image backup !");
                e.printStackTrace();
            }
        }
    }

    /*
     * Methode permettant de telecharger un bitmap depuis le dossier cache de l'application.
     */
    public Bitmap loadImageFromCacheFile(File cacheFile) {
        Bitmap bitmap = null;
        String cacheFilePath = String.valueOf(cacheFile);

        try {
            FileInputStream fileInputStream = new FileInputStream(cacheFile);
            bitmap = BitmapFactory.decodeFile(cacheFilePath);
            fileInputStream.close();
        } catch (Exception e) {
            Log.d(LOAD_IMAGE_ERROR, "An error occurred during image loading !");
            e.printStackTrace();
        }
        return bitmap;
    }

}