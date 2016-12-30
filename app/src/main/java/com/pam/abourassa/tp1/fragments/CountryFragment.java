package com.pam.abourassa.tp1.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.actionBars.CustomActionBar;
import com.pam.abourassa.tp1.adapters.CountryCursorAdapter;
import com.pam.abourassa.tp1.enums.SortOrder;
import com.pam.abourassa.tp1.model.Objects.Country;
import com.pam.abourassa.tp1.model.Provider;
import com.pam.abourassa.tp1.model.database.ForecastDBContracts;


public class CountryFragment extends Fragment {
    public static final String KEY_SORT_ORDER = "ca.cs.equipe3.forecast.activities.KEY_SORT_ORDER";
    public static final String KEY_SORT_FIELD_1 = "ca.cs.equipe3.forecast.activities.KEY_SORT_FIELD_1";
    public static final String KEY_SORT_FIELD_2 = "ca.cs.equipe3.forecast.activities.KEY_SORT_FIELD_2";

    // Variables de CountryFragment
    private Context context;
    private String sortField1;
    private String sortField2;
    private SortOrder sortOrder;
    private Spinner sortOrder_spinner;
    private ListView countriesList_listview;
    private CountryCursorAdapter countryCursorAdapter;
    private static String countryCode;

    // Setter permettant de reseter le countryCode lors du back lorsqu'une ville a ete sauvegardee.
    public static void setCountryCode (String countryCode) {
        CountryFragment.countryCode = countryCode;
    }

    public CountryFragment () {}

    // Permet de passer un countryCode pour lancer le countryFragment
    public static CountryFragment newInstance(String cityCountryCode) {
        countryCode = cityCountryCode;
        return new CountryFragment();
    }

    /**
     * Methode utilisee lors de la rotation de l'appareil pour recuperer les variables sauvegardees
     * du savedInstanceState et de les restaurees.
     */
    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            sortOrder = SortOrder.createFromInt(savedInstanceState.getInt(KEY_SORT_ORDER));
            sortField1 = savedInstanceState.getString(KEY_SORT_FIELD_1);
            sortField2 = savedInstanceState.getString(KEY_SORT_FIELD_2);
        }
    }

    /**
     *Methode permettant d'associer les variables du programme aux variables de la vue et retourne
     * une vue.
     */
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);
        sortOrder_spinner = (Spinner) view.findViewById(R.id.activity_main_spinner_sortOrder);
        countriesList_listview = (ListView) view.findViewById(R.id.activity_main_listview_countries);
        return view;
    }

    /**
     * Methode permettant de faire afficher un layout personnaliser dans l'actionBar, de faire
     * afficher la liste des pays dans un listview a l'aide d'un curseur afin de pouvoir selectionner
     * un pays.
     */
    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && sortOrder == null && sortField1 == null && sortField2 == null) {
            sortField1 = ForecastDBContracts.CountryTable.FIELD_NAME;
            sortField2 = ForecastDBContracts.CountryTable.FIELD_CONTINENT;
            sortOrder = SortOrder.ASC;
        }

        // Recupere le contexte du MainActivity
        context = getActivity();

        // Permet d'avoir un menu avec des options dans l'actionBar
        setHasOptionsMenu(true);

        // Permet de mettre une vue personnalisee dans l'actionBar
        setCountryActionBar();

        Cursor countryCursor = Provider.getInstance().countryCursorOrdered(context, sortField1, sortField2, sortOrder);
        countryCursorAdapter = new CountryCursorAdapter(context, countryCursor, sortOrder);
        countriesList_listview.setAdapter(countryCursorAdapter);

        // Permet d'ecouter les clics sur le listview et le spinner
        sortOrder_spinner.setOnItemSelectedListener(sortOrderOnItemSelectedListener);
        countriesList_listview.setOnItemClickListener(countryRowOnItemClickListener);

        // Permet de mettre une barre a droite dans le listview pour naviguer plus rapidement
        countriesList_listview.setFastScrollEnabled(true);

        if (! countryCode.isEmpty()) {
            Country country = Provider.getInstance().findCountryByCountryCode(context, countryCode);
            startCountryFragment(country);
        }
    }

    // Methode permettant de lancer le cityFragment a l'aide du id du pays selectionner ou enregistrer.
    public void startCountryFragment(Country country) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        CityFragment cityFragment = CityFragment.newInstance(country.getId());
        fragmentTransaction.replace(R.id.activity_main_fragment, cityFragment)
                .addToBackStack(null)
                .commit();
    }

    /*
     * Lors du clic sur un pays dans le listview, le pays selectionner est recuperer par le curseur
     * afin d'aller chercher son id pour lancer le cityFragment.
     */
    public AdapterView.OnItemClickListener countryRowOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
            Cursor countryCursor = countryCursorAdapter.getCursor();
            countryCursor.moveToPosition(position);
            Country country = Provider.getInstance().getCountryFromCursor(countryCursor);

            startCountryFragment(country);
        }
    };

    /**
     * Methode permettant d'ecouter les clics du spinner. La position du spinner est recuperee et mis
     * dans la variable sortOrder afin de pouvoir mettre le listview a jour selon un ordre ascendant
     * ou descendant
     */
    public AdapterView.OnItemSelectedListener sortOrderOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
            sortOrder = SortOrder.createFromInt(position);
            refreshCursor();
        }

        @Override
        public void onNothingSelected (AdapterView<?> adapterView) {

        }
    };

    private void setCountryActionBar() {
        String actionBarTitle = getResources().getString(R.string.custom_action_bar_country_title);
        int resId = R.mipmap.ic_flag;

        CustomActionBar.setActionBar(getActivity(), actionBarTitle, resId);
    }

    /*
     * Methode mettant le curseur a jour afin de pouvoir mettre a jour le listview.
     */
    private void refreshCursor() {
        countryCursorAdapter.changeCursor(Provider.getInstance().countryCursorOrdered(context, sortField1, sortField2, sortOrder));
        countryCursorAdapter.notifyDataSetChanged();
    }

    /*
     * Methode permettant de mettre un menu avec des options dans le actionBar.
     */
    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.country_menu, menu);
    }

    /**
     * Methode permettant d'effectuer le triage des pays selon 2 criteres. S'il y a plusieurs pays
     * avec le meme nom de continent ou la meme population, le 2e critere sert a trier ces pays avec
     * leur nom de pays pour qu'il soient affiches dans le bon ordre.
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Triage des pays selon le nom du pays
        if (item.getItemId() == R.id.country_menu_sort_by_country_name) {
            sortField1 = ForecastDBContracts.CountryTable.FIELD_NAME;
            sortField2 = ForecastDBContracts.CountryTable.FIELD_CONTINENT;
            // Triage des pays selon le nom du continent
        }else if (item.getItemId() == R.id.country_menu_sort_by_continent) {
            sortField1 = ForecastDBContracts.CountryTable.FIELD_CONTINENT;
            sortField2 = ForecastDBContracts.CountryTable.FIELD_NAME;
            // Triage des pays selon la population du pays
        }else if (item.getItemId() == R.id.country_menu_sort_by_population) {
            sortField1 = ForecastDBContracts.CountryTable.FIELD_POPULATION;
            sortField2 = ForecastDBContracts.CountryTable.FIELD_NAME;
        } else {
            return super.onOptionsItemSelected(item);
        }

        Provider.getInstance().countryCursorOrdered(context, sortField1, sortField2, sortOrder);
        refreshCursor();
        return true;
    }

    /*
     * Methode permettant de sauvegarder les etats du countryFragment afin de pouvoir les restaures
     * lors de la rotation de l'appareil.
     */
    @Override
    public void onSaveInstanceState (Bundle outState) {
        if (outState != null) {
            outState.putInt(KEY_SORT_ORDER, sortOrder.getValue());
            outState.putString(KEY_SORT_FIELD_1, sortField1);
            outState.putString(KEY_SORT_FIELD_2, sortField2);
        }

        super.onSaveInstanceState(outState);
    }

}
