package com.pam.abourassa.tp1.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.Menu;
import android.view.MenuInflater;

import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.actionBars.CustomActionBar;

import java.util.List;
import java.util.Locale;

/**
 * Created by Anthony on 04/01/2017.
 */

/**
 * Classe permettant de gerer le preferences (settings) de l'application et de les sauvegardees dans
 * le fichiers com.pam.abourassa.tp1.fragments.PREFERENCES_SETTINGS.
 */
public class PreferencesFragment extends PreferenceFragmentCompat {
    // Nom du fichier des preferences
    public static final String PREFERENCES_SETTINGS = "com.pam.abourassa.tp1.fragments.PREFERENCES_SETTINGS";
    // Preference permettant d'affichee ou pas le drapeaux des pays
    public static final String SHOW_FLAGS = "show_flags";
    // Preference permettant d'affichee le nom du pays en anlais ou dans la langue locale du pays
    public static final String COUNTRY_NAME_LANGUAGE = "show_country_names_language";
    // Preference permettant d'affichee l'application en francais ou en anglais
    public static final String APP_LANGUAGE = "app_language";
    // Preference permettant de quitter le menu des preferences
    public static final String EXIT_SETTINGS = "exit_settings";

    public PreferencesFragment () {

    }

    /**
     * Methode permettant d'avoir une actionBar avec un titre et une icone et de faire afficher les
     * preferences pouvant etre modifiees par l'utilisateur. Celui-ci peut entre autre modifier
     * l'affichage des drapeaux des pays, l'affichage du langage piur les noms
     */
    @Override
    public void onCreatePreferences (Bundle savedInstanceState, String rootKey) {
        // Cration du layout des preferences
        getPreferenceManager().setSharedPreferencesName(PREFERENCES_SETTINGS);
        setPreferencesFromResource(R.xml.preferences, rootKey);
        // Permet de pouvoir implementer un menu avec des options dans la actionBar
        setHasOptionsMenu(true);
        setSettingsActionBar();

        // Association des variables du programme a celles de la vue des preferences
        CheckBoxPreference appLanguagePreference = (CheckBoxPreference) getPreferenceManager().findPreference(APP_LANGUAGE);
        Preference exitOption = getPreferenceManager().findPreference(EXIT_SETTINGS);

        // Permet d'ecouter le clic sur les preferences
        appLanguagePreference.setOnPreferenceChangeListener(appLanguagePreferenceClickListener);
        exitOption.setOnPreferenceClickListener(exitOptionOnPreferenceClickListener);
    }

    /**
     * Methode permettant d'afficher un titre et une icone dans l'actionBar.
     */
    private void setSettingsActionBar() {
        String actionBarTitle = getResources().getString(R.string.preferences_action_bar_title);
        int resId = R.mipmap.ic_settings;

        CustomActionBar.setActionBar(getActivity(), actionBarTitle, resId);
    }

    /**
     * Methode permettant d'ecouter le changement de la preference du langage de l'application. Si
     * la preference est cochee, l'application sera en francais, sinon, elle sera en anglais.
     */
    private Preference.OnPreferenceChangeListener appLanguagePreferenceClickListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange (Preference preference, Object newValue) {
            if (newValue.toString().equals("true")) {
                Configuration configuration = new Configuration();
                configuration.locale = Locale.FRENCH;
                getResources().updateConfiguration(configuration, null);
                setSettingsActionBar();
            }else {
                /*
                Configuration config = getActivity().getBaseContext().getResources().getConfiguration();
                Locale locale = Locale.ENGLISH;
                config.setLocale(locale);
                getActivity().createConfigurationContext(config);
                */

                Configuration configuration = new Configuration();
                configuration.locale = Locale.ENGLISH;
                getResources().updateConfiguration(configuration, null);
                setSettingsActionBar();
            }
            return true;
        }
    };

    /**
     * Methode permettant de quitter les preferences de l'application. Ce menu est afficher au
     * lancement de l'application. Donc, si c'est le cas, cela veut dire qu'il y a 1 seul fragment
     * dans la backstack, donc, le countryFragment est lancer pour faire afficher la lsite des pays.
     * Sinon, si l'utilisateur a selectionner l'icone dans l'actionBar, on retourne au fragment
     * precedent.
     */
    private Preference.OnPreferenceClickListener exitOptionOnPreferenceClickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick (Preference preference) {
            // Recuperation de la liste des fragments dans la backstack
            List<Fragment> fragmentsList = getActivity().getSupportFragmentManager().getFragments();

            if (fragmentsList.size() == 1) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main_fragment, CountryFragment.newInstance(""))
                        .addToBackStack(null)
                        .commit();
            }else {
                getActivity().getSupportFragmentManager().popBackStack();
            }

            return true;
        }
    };

    /**
     * Methode permettant d'enelver le menu que le MainActivity affiche par defaut.
     */
    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

}