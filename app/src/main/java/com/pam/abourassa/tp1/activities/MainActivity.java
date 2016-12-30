package com.pam.abourassa.tp1.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.pam.abourassa.tp1.model.Objects.City;
import com.pam.abourassa.tp1.model.PreferencesManager;
import com.pam.abourassa.tp1.model.Provider;
import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.fragments.CityFragment;
import com.pam.abourassa.tp1.fragments.CountryFragment;
import com.pam.abourassa.tp1.model.database.DataImporter;

public class MainActivity extends AppCompatActivity {
    // Variable des shared preferences
    private SharedPreferences sharedPreferences;
    // Variable contenant le id de la ville qui a ete selectionne et enregistree
    private int cityId;

    /**
     * Methode permettant d'appeler la methode pour telecharger des fichiers csv, ainsi que celle
     * permettant de verifier si le fichier des shared preferences contient une variable avec le id
     * de la ville selectionnee. Lancement du ContryFragment pour afficher la liste des pays a
     * l'ecran.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstLaunchApplication();
        getSharedPreferences();

        if (sharedPreferences.contains(CityFragment.PREF_CITY_ID)) {
            City city = Provider.getInstance().findCityById(this, cityId);
            startCountryFragment(city.getCountryCode());
        }else if (savedInstanceState == null){
            String countryCode = "";
            startCountryFragment(countryCode);
        }
    }

    /**
     * Methode permettant de lancer le CountryFragment a l'aide du countryCode de la ville
     * selectionne. Si le countryCode est vide, la liste des pays sera affichee immediatement.
     */
    private void startCountryFragment(String countryCode) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CountryFragment countryFragment = CountryFragment.newInstance(countryCode);
        fragmentTransaction.replace(R.id.activity_main_fragment, countryFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Methode permettant de lancer le telechargement des fichiers csv une seule fois lors de la
     * cration de l'application, a l'aide de la classe PreferencesManager.
     */
    private void firstLaunchApplication() {
        PreferencesManager preferenceManager = new PreferencesManager(this);

        if (preferenceManager.isFisrtLaunch()) {
            DataImporter.importCountryCsvInDatabase(this);
            DataImporter.importCityCsvInDatabase(this);

            // Setter permettant de passer une seule fois dans cette methode.
            preferenceManager.setIsFirstLaunch(false);
        }
    }

    /**
     * Methode permettant de verifier si le fichier shared preference contient le id d'une ville qui
     * a ete selectionne.
     */
    private void getSharedPreferences() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        cityId = sharedPreferences.getInt(CityFragment.PREF_CITY_ID,0);
    }

    /**
     * Methode permettant de gerer le clic sur le bouton back, ce qui permet de revenir au fragment
     * precedent tout en executant les actions qu'il est necessaire de faire pour le bon
     * fonctionnement de l'application.
     */
    @Override
    public void onBackPressed () {
        int currentFragment = getSupportFragmentManager().getBackStackEntryCount();

        switch (currentFragment) {

            case 1:
                startCountryFragment("");
                break;
            case 2:
                CountryFragment.setCountryCode("");
                break;
            case 3:
                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(CityFragment.PREF_CITY_ID);
                editor.apply();
                break;
        }
        super.onBackPressed();
    }

}
