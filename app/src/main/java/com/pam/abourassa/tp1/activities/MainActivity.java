package com.pam.abourassa.tp1.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.fragments.CityFragment;
import com.pam.abourassa.tp1.fragments.CountryFragment;
import com.pam.abourassa.tp1.fragments.PreferencesFragment;
import com.pam.abourassa.tp1.model.Objects.City;
import com.pam.abourassa.tp1.model.database.DataImporter;
import com.pam.abourassa.tp1.model.providers.Provider;
import com.pam.abourassa.tp1.utils.PreferencesManager;

import java.util.List;

/**
 * Classe permettant de gerer les differents fragments de l'application. On importe les donnees des
 * fichiers csv (coutry et city) dans la base de donnees au lancement de l'application. Grace a la
 * classe PreferencesManager, cette importation n'est fait qu'une seule fois, tant et aussi longtemps
 * que l'application n'a pas ete supprimer. Ensuite, on va verifier dans le dossier shared preferences
 * s'il contient un id d'une ville enregistree. Si c'est le cas, on recreer les tous les fragments
 * dans la backstack et c'est le weatherFragment qui apparait au demarrage de l'application. Sinon,
 * c'est le countryFragment qui est lancer pour faire apparaitre la liste des pays.
 *
 * Le MainActivity gere egalement le menu implementer dans chaque layout. Puisque le menu des
 * preferences doit etre dans chaque layout, c'est donc le main qui l'affiche et qui gere la selection
 * du clic sur ce menu. De plus, le MainActivity gere l'action de revenir au fragment precedent lors
 * du clic sur le bouton back dans chaque fragment.
 */
public class MainActivity extends AppCompatActivity {
    // Variable des shared preferences
    private SharedPreferences sharedPreferences;

    /**
     * Methode permettant le lancement du telechargement des fichiers csv dans la bse de donnees a
     * une seule occasion, lors de la creation de l'application. Par la suite, on verifie si le fichier
     * shared preferences contient le id d'une ville enregistree par l'utilisateur. Cette etape se
     * fait lorsque l'utilisateur selectionne une ville dans le cityFragment, la ville s'enregistre
     * dans le fichier des shared preferences. Lorsque l'utilisteur enleve l'application de la pile
     * d'applications sur son appareil, lors de la prochaine ouverture de cette application, grace a
     * ce id, les 3 fragments sont recrees et le weatherFragment lui fait apparaitre la meme ville
     * que lorsque l'utilisateur a quitter l'application. Sinon, le countryFragment est lancer pour
     * faire apparaitre la liste des pays.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Telechargement des fichiers csv dans la base de donnees
        firstLaunchApplication();
        int cityId = getSharedPreferences();

        /*
         * Verification du fichier des shared preferences pour voir s'il contient un id d'une ville
         * enregistree. La condition savedInstanceState == null permet d'eviter de recreer 3 fragments
         * a chaque fois qu'une rotation est faite dans le weatherFragment, si celui-ci est afficher
         * au demarrage de l'application
         */
        if (sharedPreferences.contains(CityFragment.PREF_CITY_ID) && savedInstanceState == null) {
            City city = Provider.getInstance().findCityById(this, cityId);
            startCountryFragment(city.getCountryCode());
        }else if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_fragment, new PreferencesFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Methode permettant de lancer le CountryFragment a l'aide du countryCode de la ville
     * selectionnee.
     */
    private void startCountryFragment(String countryCode) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment, CountryFragment.newInstance(countryCode))
                .addToBackStack(null)
                .commit();
    }

    /**
     * Methode permettant de lancer le telechargement des fichiers csv une seule fois lors de la
     * creation de l'application, a l'aide de la classe PreferencesManager.
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
    private int getSharedPreferences() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CityFragment.PREF_CITY_ID,0);
    }

    /**
     * Methode permettant de lancer le PreferencesFragment pour gerer les preferences de l'application.
     */
    public void getApplicationPreferences() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment, new PreferencesFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     *
     */
    @Override
    public void onBackPressed () {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int currentFragment = getSupportFragmentManager().getBackStackEntryCount();

        List<Fragment> fragmentsList = getSupportFragmentManager().getFragments();
        Fragment fragmentOnTop = fragmentsList.get(currentFragment - 1);

        String fragmentName = null;
        if (fragmentOnTop != null) {
            fragmentName = fragmentOnTop.getClass().getSimpleName();
        }

        switch (fragmentName) {
            case "CountryFragment":
                Toast.makeText(this, "You cannot back from this screen", Toast.LENGTH_LONG).show();
                break;
            case "CityFragment":
                editor.remove(CountryFragment.PREF_COUNTRY_CODE)
                        .commit();
                super.onBackPressed();
                break;
            case "WeatherFragment":
                editor.remove(CityFragment.PREF_CITY_ID)
                        .commit();
                super.onBackPressed();
                break;
            case "PreferencesFragment":
                if (fragmentsList.size() == 1) {
                    Toast.makeText(this, "Cannot go back", Toast.LENGTH_LONG).show();
                    break;
                }else {
                    super.onBackPressed();
                    break;
                }
            default:
                super.onBackPressed();
                break;
        }
    }

    /**
     * Methode permettant de pouvoir implementer un menu avec des options dans la actionBar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.preferences_menu, menu);
        return true;
    }

    /**
     * Methode permettant de gerer les clics sur les menus dans l'actionBar. Le clic sur le menu des
     * preferences
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                getApplicationPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}