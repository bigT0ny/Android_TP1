package com.pam.abourassa.tp1;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Anthony on 09/12/2016.
 */

public class PreferencesManager {
    private final String PREFERENCES_FILE_NAME = "com.pam.abourassa.preferences_file_name";
    private final String FIRST_LAUNCH = "com.pam.abourassa.preferences.first_launch";

    private SharedPreferences preferences;

    public PreferencesManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public boolean isFisrtLaunch() {
        return preferences.getBoolean(FIRST_LAUNCH, true);
    }

    public void setIsFirstLaunch(boolean firstLaunch) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(FIRST_LAUNCH, firstLaunch);
        editor.commit();
    }

}
