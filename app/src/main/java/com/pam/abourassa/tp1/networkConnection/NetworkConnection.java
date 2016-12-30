package com.pam.abourassa.tp1.networkConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Anthony on 28/12/2016.
 */

public class NetworkConnection {

    /**
     * Methode permettant de verifier si l'appareil a une connection Internet. Retourne un boleen
     * pour que le message de connection perdue soit afficher une seule fois.
     */
    public static boolean verifyNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = false;

        if (networkInfo != null) {
            connected = networkInfo.isConnected();
        }

        return connected;
    }

}
