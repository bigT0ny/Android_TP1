package com.pam.abourassa.tp1.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.actionBars.CustomActionBar;

import java.util.Locale;

/**
 * Created by Anthony on 04/01/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String PREFERENCES_SETTINGS = "preferences settings";
    public static final String SHOW_FLAGS = "show_flags";
    public static final String COUNTRY_NAME_LANGUAGE = "show_country_names_language";
    public static final String APP_LANGUAGE = "app_language";
    public static final String EXIT_SETTINGS = "exit_settings";
    public boolean flagsPreferenceCheck;
    public boolean countryNamesLanguagePreferenceCheck;

    public SettingsFragment() {

    }

    @Override
    public void onCreatePreferences (Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(PREFERENCES_SETTINGS);
        setPreferencesFromResource(R.xml.preferences, rootKey);
        setSettingsActionBar();

        CheckBoxPreference flagsPreference = (CheckBoxPreference) getPreferenceManager().findPreference(SHOW_FLAGS);
        CheckBoxPreference countryNameLanguagePreference = (CheckBoxPreference) getPreferenceManager()
                .findPreference(COUNTRY_NAME_LANGUAGE);
        CheckBoxPreference appLanguagePreference = (CheckBoxPreference) getPreferenceManager().findPreference(APP_LANGUAGE);
        Preference button = getPreferenceManager().findPreference(EXIT_SETTINGS);

        flagsPreferenceCheck = flagsPreference.isChecked();
        flagsPreference.setOnPreferenceChangeListener(flagsPreferenceClickListener);
        countryNameLanguagePreference.setOnPreferenceChangeListener(countryNamesLanguagePreferenceClickListener);
        appLanguagePreference.setOnPreferenceChangeListener(appLanguagePreferenceClickListener);
        button.setOnPreferenceClickListener(buttonOnPreferenceClickListener);
    }

    private void setSettingsActionBar() {
        String actionBarTitle = getResources().getString(R.string.preferences_action_bar_title);
        int resId = R.mipmap.ic_settings;

        CustomActionBar.setActionBar(getActivity(), actionBarTitle, resId);
    }

    private Preference.OnPreferenceChangeListener flagsPreferenceClickListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange (Preference preference, Object newValue) {
            if (newValue.toString().equals("true")) {
                flagsPreferenceCheck = true;
            }else {
                flagsPreferenceCheck = false;
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener countryNamesLanguagePreferenceClickListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange (Preference preference, Object newValue) {
            if (newValue.toString().equals("true")) {
                countryNamesLanguagePreferenceCheck = true;
            }else {
                countryNamesLanguagePreferenceCheck = false;
            }
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener appLanguagePreferenceClickListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange (Preference preference, Object newValue) {
            if (newValue.toString().equals("true")) {
                Configuration configuration = new Configuration();
                configuration.locale = Locale.FRENCH;
                getResources().updateConfiguration(configuration, null);
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
            }
            return true;
        }
    };

    private Preference.OnPreferenceClickListener buttonOnPreferenceClickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick (Preference preference) {
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
    };

}
