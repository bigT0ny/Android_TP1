package com.pam.abourassa.tp1.flags;

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

public class FlagsProvider {

    private static FlagsProvider instance = new FlagsProvider();

    /*
     * Retourne une instance de FlagsProvider afin de pouvoir utiliser ses methodes.
     */
    public static FlagsProvider getInstance () {
        return instance;
    }

    /*
     * Methode permettant de sauvegarder un bitmap dans le dossier cache de l'application.
     */
    public void saveImageInCacheFile(Bitmap bitmap, File cacheFile) {
        if (bitmap != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                Log.i("Save image", "An error occured during the backup !");
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
            Log.d("Load image", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }

}
