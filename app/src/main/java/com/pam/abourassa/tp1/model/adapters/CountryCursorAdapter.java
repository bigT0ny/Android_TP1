package com.pam.abourassa.tp1.model.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.enums.SortOrder;
import com.pam.abourassa.tp1.fragments.SettingsFragment;
import com.pam.abourassa.tp1.model.providers.FlagsProvider;
import com.pam.abourassa.tp1.networkConnection.DownloadImageFromUrl;
import com.pam.abourassa.tp1.model.Objects.Country;
import com.pam.abourassa.tp1.model.providers.Provider;
import com.pam.abourassa.tp1.networkConnection.NetworkConnection;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anthony on 09/12/2016.
 */

public class CountryCursorAdapter extends CursorAdapter {
    private static boolean noConnection = false;

    /*
     * Constructeur de CountryCursorAdapter
     */
    public CountryCursorAdapter (Context context, Cursor cursor, SortOrder sortOrder) {
        super(context, cursor, 0);
    }

    @Override
    public void notifyDataSetChanged () {
        super.notifyDataSetChanged();
    }

    /*
     * Methode permettant de faire afficher un layout a l'ecran.
      */
    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_country_list_row, parent, false);
    }

    /**
     * Methode permettant de faire afficher le nom du pays, le nom du continent ainsi que la
     * population. Le drapeau du pays est afficher dans un imageview.
     */
    @Override
    public void bindView (View view, Context context, Cursor cursor) {
        // Recuperation de l'objet country
        Country country = Provider.getInstance().getCountryFromCursor(cursor);
        /*
         * Permet de verifier la preference afin de savoir s'il faut afficher les drapeau des pays.
         * Si la preference est a true, les drapeaux sont affiches, sinon, ils n'apparaissent pas.
         */
        SharedPreferences preferences = context.getSharedPreferences(SettingsFragment.PREFERENCES_SETTINGS, Context.MODE_PRIVATE);
        boolean showFlags = preferences.getBoolean(SettingsFragment.SHOW_FLAGS, false);

        if (country != null) {
            // Nom de l'image qui sera sauvegardee
            String imageName = country.getCode() + ".png";

            // URL permettant d'aller telecharger l'image sur Internet
            String urlString = "http://www.geognos.com/api/en/countries/flag/" + country.getCode() + ".png";
            // Fichier cache ou sera enregistrer les drapeaux.
            File cacheFile = new File(context.getCacheDir(), imageName);

            // Association des variables du programme avec les variables de la vue.
            TextView countryName_textview = (TextView) view.findViewById(R.id.country_listview_row_textview_countryName);
            TextView continentName_textview = (TextView) view.findViewById(R.id.country_listview_row_textview_continentName);
            TextView population_textview = (TextView) view.findViewById(R.id.country_listview_row_textview_population);
            ImageView flag_imageview = (ImageView) view.findViewById(R.id.country_listview_row_imageview_flag);

            /*
             * Si l'image est enregistree dans la cache, on telecharge l'image depuis la cache et elle
             * est affichee dans un imageview. Sinon, l'image est telechargee depuis Internet a l'aide
             * d'une URL et elle est sauvegardee dans la cache. Une fois sauvegardee, on telecharge
             * l'image depuis la cache. Pour pouvoir telecharger les images depuis Internet, il y a
             * une verification de la connection faite. S'il n'y a pas de connection, un message
             * apparait pour indiquer a l'utilisateur qu'il ne peut pas telecharger les images depuis
             * Internet.
             */
            if (! showFlags) {
                flag_imageview.setVisibility(View.GONE);
            }else {

                if (cacheFile.exists()) {
                    flag_imageview.setImageBitmap(FlagsProvider.getInstance().loadImageFromCacheFile(cacheFile));
                } else {
                    if (NetworkConnection.verifyNetworkConnection(context)) {
                        //new DownloadImageFromUrl(cacheFile).execute(urlString);

                        try {
                            FlagsProvider.getInstance().saveImageInCacheFile(new DownloadImageFromUrl(cacheFile).execute(urlString).get(), cacheFile);
                            flag_imageview.setImageBitmap(FlagsProvider.getInstance().loadImageFromCacheFile(cacheFile));
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (! noConnection) {
                            Toast.makeText(context, R.string.warning_network_connection_flags_toast,
                                    Toast.LENGTH_LONG).show();
                            noConnection = true;
                        }
                    }
                }

                /*
             * Si le bitmap retourne la valeur null, cela veut dire que le pays n'a pas de drapeau.
             * Donc, un mipmap representant un drapeau est afficher a la place.
             */
                if (FlagsProvider.getInstance().loadImageFromCacheFile(cacheFile) == null) {
                    flag_imageview.setImageResource(R.mipmap.ic_flag);
                }

                flag_imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            /*
             * Permet de verifier la preference afin de savoir s'il faut afficher les noms des pays
             * en anglais ou dans la langue locale du pays et est afficher a l'ecran.
             */
            boolean countryNameLanguage = preferences.getBoolean(SettingsFragment.COUNTRY_NAME_LANGUAGE, false);

            if (countryNameLanguage) {
                countryName_textview.setText(country.getName());
            }else {
                countryName_textview.setText(country.getLocalName());
            }
            // Affiche a l'ecran le nom du continent et la population de chaque pays
            continentName_textview.setText(country.getContinent());
            population_textview.setText(String.valueOf(country.getPopulation()));
        }
    }

}
