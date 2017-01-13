package com.pam.abourassa.tp1.networkConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Anthony on 28/12/2016.
 */

/**
 * Classe permettant de verifiant la connection internet de l'appareil retournant un boolean
 * indiquant la connection.
 */
public class NetworkConnection {

    /**
     * Methode permettant de verifier si l'appareil a une connection Internet. Retourne un boleen
     * pour indiquer si l'appareil a la onnection internet ou pas.
     */
    public static boolean verifyNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connection = false;

        if (networkInfo != null) {
            connection = networkInfo.isConnected();
        }

        return connection;
    }

}
