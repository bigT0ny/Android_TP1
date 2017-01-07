package com.pam.abourassa.tp1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.actionBars.CustomActionBar;
import com.pam.abourassa.tp1.model.Objects.City;
import com.pam.abourassa.tp1.model.Objects.Country;
import com.pam.abourassa.tp1.model.adapters.CityCursorAdapter;
import com.pam.abourassa.tp1.model.providers.FlagsProvider;
import com.pam.abourassa.tp1.model.providers.Provider;

import java.io.File;

public class CityFragment extends Fragment {
    public static final String PREF_CITY_ID = "com.pam.abourassa.tp1.fragments.PREF_CITY_ID";
    // Variable permettant de sauvegarder le id du pays selectionner ou enregistrer.
    public static final String ARG_COUNTRY_ID = "com.pam.abourassa.tp1.fragments.ARG_COUNTRY_ID";
    // Variable permettant de sauvegarder l'etat du cityFragment lors de la rotation de l'appareil.
    public static final String KEY_SEARCH_CRITERIA = "com.pam.abourassa.tp1.fragments.KEY_SEARCH_CRITERIA";

    // Variables de cityFragment
    private CityCursorAdapter cityCursorAdapter;
    private Country country;
    private Context context;
    private SearchView citySearchBar;
    private ListView citiesList_listview;
    private String searchCriteria;

    public CityFragment () {
    }

    /**
     * @param countryId Parameter 1 : selected country ID.
     * @return an instance of CityFragment
     */
    public static CityFragment newInstance (int countryId) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNTRY_ID, countryId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Methode utilisee lors de la rotation de l'appareil pour recuperer les variables sauvegardees
     * du savedInstanceState et de les restaurees.
     */
    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            searchCriteria = savedInstanceState.getString(KEY_SEARCH_CRITERIA);
        }
    }

    /**
     * Methode permettant d'associer les variables du programme aux variables de la vue et retourne
     * une vue.
     */
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        citiesList_listview = (ListView) view.findViewById(R.id.city_fragment_listview_citiesList);
        citySearchBar = (SearchView) view.findViewById(R.id.city_fragment_searchview_searchBar);

        return view;
    }

    /**
     * Methode permettant de recuperer le id du pays selectionner afin de recuperer notre objet
     * country afin d'aller chercher le countryCode de ce pays pour recuperer la liste des villes
     * de ce pays et de les faire afficher dans un listview. Si une ville a ete enregistree dans les
     * shared preferences, le id de cette ville est recuperer et le weatherFragment est lancer.
     */
    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Permet de recuperer le id d'une ville si elle a ete enregistree dans les shared preferences.
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int cityId = sharedPreferences.getInt(CityFragment.PREF_CITY_ID, 0);
        context = getActivity();

        if (getArguments() != null) {
            int countryId = getArguments().getInt(ARG_COUNTRY_ID);
            country = Provider.getInstance().findCountryById(context, countryId);
        }

        // Le critere de recherche de la barre de recherche est vide au depart
        if (savedInstanceState == null) {
            searchCriteria = "";
        }

        // Permet d'avoir un menu avec des options dans l'actionBar
        setHasOptionsMenu(true);
        // Permet de mettre une vue personnalisee dans l'actionBar
        setCityActionBar();

        Cursor cityCursor = Provider.getInstance().cityCursorOrdered(context, country.getCode(), searchCriteria);
        cityCursorAdapter = new CityCursorAdapter(context, cityCursor);
        citiesList_listview.setAdapter(cityCursorAdapter);

        // Permet d'ecouter les clics du listview et de la barre de recherche
        citySearchBar.setOnQueryTextListener(citySearchBarOnQueryTextListener);
        citiesList_listview.setOnItemClickListener(cityRowOnItemClickListener);

        // Permet de mettre une barre a droite dans le listview pour naviguer plus rapidement
        citiesList_listview.setFastScrollEnabled(true);

        if (cityId != 0) {
            startWeatherFragment(cityId);
        }
    }

    /**
     * Methode permettant de lancer weatherFragment a l'aide du id de la ville selectionne ou
     * enresgistree.
     */
    private void startWeatherFragment (int cityId) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        WeatherFragment weatherFragment = WeatherFragment.newInstance(cityId);
        fragmentTransaction.replace(R.id.activity_main_fragment, weatherFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Methode permettant de lancer weatheFragment lors du clic sur une ville du listview. La ville
     * est recuperee a l'aide du cureseur et on utilise le id de celle-ci pour aller au prochain
     * fragent.
     */
    public AdapterView.OnItemClickListener cityRowOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
            Cursor cityCursor = cityCursorAdapter.getCursor();
            cityCursor.moveToPosition(position);
            City city = Provider.getInstance().getCityFromCursor(cityCursor);

            // Permet d'enregistrer le id de la ville selectionnee dans les shared preferences.
            setSharedPreferences(city.getId());
            startWeatherFragment(city.getId());
        }
    };

    private void setSharedPreferences (int cityId) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_CITY_ID, cityId);
        editor.commit();
    }

    /**
     * Methode permettant de faire une recherche a l'aide d'une barre de recherche. Lorsque du texte
     * est entrer dans la barre de recherche, seulement les villes contenant ces caracteres sont
     * affichees dans le listview.
     */
    public SearchView.OnQueryTextListener citySearchBarOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit (String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange (String newText) {
            searchCriteria = newText;
            refreshCursor();

            return true;
        }
    };

    /**
     * Methode permettant d'enregistrer le id de la ville selectionner dans les shared preferences
     * afin de pouvoir lancer le weatherFragment lors de l'ouverture de l'application si une ville
     * a ete enregistree dans ce fichier.
     */

    private void setCityActionBar () {
        String actionBarTitle = country.getName() + " - " + getActivity().getResources()
                .getString(R.string.custom_action_bar_city_title);
        String imageName = country.getCode() + ".png";
        File cacheFile = new File(context.getCacheDir(), imageName);
        Bitmap bitmap = FlagsProvider.getInstance().loadImageFromCacheFile(cacheFile);

        CustomActionBar.setActionBar(getActivity(), actionBarTitle, bitmap);
    }

    /*
     * Methode mettant le curseur a jour afin de pouvoir mettre a jour le listview.
     */
    public void refreshCursor () {
        cityCursorAdapter.changeCursor(Provider.getInstance().cityCursorOrdered(context, country.getCode(), searchCriteria));
        cityCursorAdapter.notifyDataSetChanged();
    }

    /*
     * Methode permettant de sauvegarder les etats du countryFragment afin de pouvoir les restaures
     * lors de la rotation de l'appareil.
     */
    @Override
    public void onSaveInstanceState (Bundle outState) {
        outState.putString(KEY_SEARCH_CRITERIA, searchCriteria);

        super.onSaveInstanceState(outState);
    }

}
