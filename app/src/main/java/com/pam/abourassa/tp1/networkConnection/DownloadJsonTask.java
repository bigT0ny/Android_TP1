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

public class DownloadJsonTask extends AsyncTask<String, Void, String> {

    protected void onPreExecute() {

    }

    private String downloadJsonString (String jsonString) {
        String result = null;

        try {
            URL url = new URL(jsonString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            result = stringBuilder.toString();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected String doInBackground(String... params) {
        return downloadJsonString(params[0]);
    }

    protected void onPostExecute() {

    }

}
