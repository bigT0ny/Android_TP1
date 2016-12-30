package com.pam.abourassa.tp1.adapters;

import android.content.Context;
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
import com.pam.abourassa.tp1.flags.FlagsProvider;
import com.pam.abourassa.tp1.model.DownloadImageFromUrl;
import com.pam.abourassa.tp1.model.Objects.Country;
import com.pam.abourassa.tp1.model.Provider;
import com.pam.abourassa.tp1.networkConnection.NetworkConnection;

import java.io.File;

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
            if (cacheFile.exists()) {
                flag_imageview.setImageBitmap(FlagsProvider.getInstance().loadImageFromCacheFile(cacheFile));
            }else {
                if(NetworkConnection.verifyNetworkConnection(context)) {
                    new DownloadImageFromUrl(cacheFile).execute(urlString);
                    flag_imageview.setImageBitmap(FlagsProvider.getInstance().loadImageFromCacheFile(cacheFile));
                }else {
                    if (! noConnection) {
                        Toast.makeText(context, R.string.warning_network_connection_flags_toast,
                                Toast.LENGTH_LONG).show();
                        noConnection = true;
                    }
                }
            }

            /*
             *Si le bitmap retourne la valeur null, cela veut dire que le pays n'a pas de drapeau.
             *Donc, un mipmap representant un drapeau est afficher a la place.
             */
            if (FlagsProvider.getInstance().loadImageFromCacheFile(cacheFile) == null) {
                flag_imageview.setImageResource(R.mipmap.ic_flag);
            }

            // Les donnees sont envoyees dans les composantes graphiques pour les afficher a l'ecran.
            flag_imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            countryName_textview.setText(country.getName());
            continentName_textview.setText(country.getContinent());
            population_textview.setText(String.valueOf(country.getPopulation()));
        }
    }

}
