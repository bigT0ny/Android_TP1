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
import com.pam.abourassa.tp1.fragments.PreferencesFragment;
import com.pam.abourassa.tp1.model.Objects.Country;
import com.pam.abourassa.tp1.model.providers.FlagsProvider;
import com.pam.abourassa.tp1.model.providers.Provider;
import com.pam.abourassa.tp1.networkConnection.DownloadImageTask;
import com.pam.abourassa.tp1.networkConnection.NetworkConnection;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anthony on 09/12/2016.
 */

public class CountryCursorAdapter extends CursorAdapter {
    // Url de base permettant de telecharger les drapeaux des pays
    public static final String URL_FLAG_IMAGE = "http://www.geognos.com/api/en/countries/flag/";
    // Variables associees a celles de la vue
    private TextView countryName_textview;
    private TextView continentName_textview;
    private TextView population_textview;
    private ImageView flag_imageview;
    // Variable permettant de garder l'etat de la connection tout au long de l'application
    private static boolean noConnection = false;

    /*
     * Constructeur de CountryCursorAdapter
     */
    public CountryCursorAdapter (Context context, Cursor cursor, SortOrder sortOrder) {
        super(context, cursor, 0);
    }

    /**
     * Permet de mettre a jour le countryCursorAdapter
     */
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
            // initialisation des composantes de la vue
            initialization(view);
            // Nom de l'image qui sera sauvegardee
            String imageName = country.getCode() + ".png";
            // Url permettant d'aller telecharger l'image sur Internet
            String urlString = URL_FLAG_IMAGE + country.getCode() + ".png";
            // Fichier cache ou sera enregistrer les drapeaux.
            File cacheFile = new File(context.getCacheDir(), imageName);
            // Permet de recuperer la valeur de la preference show_flags choisi par l'utilisateur
            SharedPreferences preferences = context.getSharedPreferences(PreferencesFragment.PREFERENCES_SETTINGS,
                    Context.MODE_PRIVATE);
            boolean showFlags = preferences.getBoolean(PreferencesFragment.SHOW_FLAGS, false);

            // Si la variable showFlags, recuperee du fichier des preferences est a false, on n'affiche
            // pas les drapeaux a l'ecran.
            if (! showFlags) {
                flag_imageview.setVisibility(View.GONE);
            }else {
                // Si le dossier cache ou est enregistree l'image, on recuper l'image depuis ce
                // fichier et on la fait afficher dans un imagview.
                if (cacheFile.exists()) {
                    flag_imageview.setImageBitmap(FlagsProvider.getInstance().loadImageFromCacheFile(cacheFile));
                } else {
                    // Verification de la connexion internet pour telecharger les drapeaux
                    if (NetworkConnection.verifyNetworkConnection(context)) {
                        //new DownloadImageTask(cacheFile).execute(urlString);
                        /*
                         * Si la connexion internet est disponible, on lance le telechargement sur
                         * internet en asynchrone et on fait afficher l'image dans un imageview.
                         */
                        try {
                            FlagsProvider.getInstance().saveImageInCacheFile(new DownloadImageTask()
                                    .execute(urlString).get(), cacheFile);
                            flag_imageview.setImageBitmap(FlagsProvider.getInstance().loadImageFromCacheFile(cacheFile));
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        /*
                         * Si la connexion est indisponible, un message est afficher a l'ecran et la
                         * variable noConnection est mise a true pour afficher le Toast une seule
                         * fois, car sinon le message apparait en tout temps dans le listview.
                         */
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
                // Permet de scaler l'image
                flag_imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            /*
             * Permet de verifier la preference afin de savoir s'il faut afficher les noms des pays
             * en anglais ou dans la langue locale du pays et est afficher a l'ecran.
             */
            boolean countryNameLanguage = preferences.getBoolean(PreferencesFragment.COUNTRY_NAME_LANGUAGE, false);

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

    /**
     * Association des variables du programme avec les variables de la vue.
     */
    private void initialization(View view) {
        countryName_textview = (TextView) view.findViewById(R.id.country_listview_row_textview_countryName);
        continentName_textview = (TextView) view.findViewById(R.id.country_listview_row_textview_continentName);
        population_textview = (TextView) view.findViewById(R.id.country_listview_row_textview_population);
        flag_imageview = (ImageView) view.findViewById(R.id.country_listview_row_imageview_flag);
    }

}