package com.pam.abourassa.tp1.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.fragments.CountryFragment;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_COUNTRY_FRAGMENT = "ca.cs.equipe3.forecast.activities.KEY_COUNTRY_FRAGMENT";

    private CountryFragment countryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            countryFragment = CountryFragment.newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.activity_main_fragment, countryFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        getSupportFragmentManager().putFragment(outState, KEY_COUNTRY_FRAGMENT, countryFragment);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        countryFragment = (CountryFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_COUNTRY_FRAGMENT);
    }
}
