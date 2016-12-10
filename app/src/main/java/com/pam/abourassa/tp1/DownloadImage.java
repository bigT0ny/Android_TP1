package com.pam.abourassa.tp1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Anthony on 09/12/2016.
 */

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    private String TAG = "DownloadImage";
    private Context context;
    private String imageName;

    public DownloadImage(Context context, String imageName) {
        this.context = context;
        this.imageName = imageName;
    }

    public static void saveImage(Context context, Bitmap bitmap, String imageName){
        FileOutputStream fos;

        if (bitmap != null) {
            try {
                fos = context.openFileOutput(imageName, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("Save image", "Something went wrong !");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream = context.openFileInput(imageName);
            bitmap = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap downloadImageBitmap(String url) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(url).openStream();    // Download Image from URL
            bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
            inputStream.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception 1, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = downloadImageBitmap(params[0]);

        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        saveImage(context, result, imageName);
    }

}
