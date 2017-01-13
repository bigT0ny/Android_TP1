package com.pam.abourassa.tp1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.pam.abourassa.tp1.enums.SortOrder;
import com.pam.abourassa.tp1.model.Objects.Country;
import com.pam.abourassa.tp1.model.adapters.CountryCursorAdapter;
import com.pam.abourassa.tp1.model.database.ForecastDBContracts;
import com.pam.abourassa.tp1.model.providers.Provider;

/**
 * Classe permettant de faire afficher une liste de pays trier selon 2 criteres choisis. Il faut
 * egalement trier les pays en ordre ascendant ou descendant. Il y a une actionBar avec un menu avec
 * des options permettant de trier les pays selon des criteres. Recuperation des pays a l'aide d'une
 * requete SQL dans la base de donnees pour les faire afficher dans un listview.
 */
public class CountryFragment extends Fragment {
    public static final String PREF_COUNTRY_CODE = "com.pam.abourassa.tp1.fragments.PREF_COUNTRY_CODE ";
    public static final String ARG_COUNTRY_CODE = "com.pam.abourassa.tp1.fragments.ARG_COUNTRY_CODE";
    public static final String KEY_SORT_ORDER = "com.pam.abourassa.tp1.fragments.KEY_SORT_ORDER";
    public static final String KEY_SORT_FIELD_1 = "com.pam.abourassa.tp1.fragments.KEY_SORT_FIELD_1";
    public static final String KEY_SORT_FIELD_2 = "com.pam.abourassa.tp1.fragments.KEY_SORT_FIELD_2";

    // Variables de CountryFragment
    private CountryCursorAdapter countryCursorAdapter;
    private Context context;
    private Spinner sortOrder_spinner;
    private ListView countriesList_listview;
    private SortOrder sortOrder;
    private String sortField1;
    private String sortField2;

    // Constructeur vide necessaire pour un fragment
    public CountryFragment () {
    }

    /**
     * Methode permettant de recuperer le countryCode afin de reconstruire le countryFragment dans
     * le cas ou il y a un countryCode d'enregistrer dans les shared preferences.
      */
    public static CountryFragment newInstance(String countryCode) {
        CountryFragment countryFragment = new CountryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COUNTRY_CODE, countryCode);
        countryFragment.setArguments(args);
        return countryFragment;
    }

    /**
     * Methode utilisee lors de la rotation de l'appareil pour recuperer les variables sauvegardees
     * du savedInstanceState et de les restaurer.
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
     * la vue personnalisee.
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
     * un pays. Il est possible de trier les pays selon differents parametres dans un menu dans
     * l'actionBar.
     */
    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Recupere le contexte du MainActivity
        context = getActivity();
        // Permet d'avoir un menu avec des options dans l'actionBar
        setHasOptionsMenu(true);
        // Permet de mettre une vue personnalisee dans l'actionBar
        setCountryActionBar();

        // Si tout est null, on met certaines valeurs a certains parametres au lancement du fragment.
        // On veut trier les pays par nom, puis par continent, et on trie les pays en ordre croissant.
        if (savedInstanceState == null && sortOrder == null && sortField1 == null && sortField2 == null) {
            sortField1 = ForecastDBContracts.CountryTable.FIELD_NAME;
            sortField2 = ForecastDBContracts.CountryTable.FIELD_CONTINENT;
            sortOrder = SortOrder.ASC;
        }

        // Permet de recuperer le code du pays en le recuperant dans le dossier des shared preferences.
        String countryCode = getSharedPreferences();

        /*
         * S'il y a un countryCode dans le fichier shared preferences, mais pas de id de ville, le
         * pays est recuperer a l'aide de son code et on le passe en parametre d'une methode pour
         * lancer le cityFragment. Il faut mettre la condition que le countryCode contienne une valeur
         * et que la preference du id de la ville ne soit pas null pour lancer le prochain fragment.
         */
        if (countryCode != null && CityFragment.PREF_CITY_ID != null) {
            Country country = Provider.getInstance().findCountryByCountryCode(context, countryCode);
            startCityFragment(country);
        }

        // Curseur permettant de recuperer tous les pays depuis la base de donnees afin de les faire
        // afficher dans un listview.
        Cursor countryCursor = Provider.getInstance().countryCursorOrdered(context, sortField1, sortField2, sortOrder);
        countryCursorAdapter = new CountryCursorAdapter(context, countryCursor, sortOrder);
        countriesList_listview.setAdapter(countryCursorAdapter);

        // Permet d'ecouter les clics sur le listview et le spinner
        sortOrder_spinner.setOnItemSelectedListener(sortOrderOnItemSelectedListener);
        countriesList_listview.setOnItemClickListener(countryRowOnItemClickListener);

        // Permet de mettre une barre a droite dans le listview pour naviguer plus rapidement
        countriesList_listview.setFastScrollEnabled(true);
    }

    /**
     * Methode permettant de lancer le cityFragment a l'aide du id du pays selectionner ou enregistrer.
     */
    public void startCityFragment(Country country) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_main_fragment, CityFragment.newInstance(country.getId()))
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

            // Permet d'enregistrer le id du pays selectionnee dans les shared preferences.
            setSharedPreferences(country.getCode());
            startCityFragment(country);
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
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    /**
     * Methode permettant d'obtenir le code du pays enregistrer dans le fichier shared preferences.
     */
    private String getSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_COUNTRY_CODE, null);
    }

    /**
     * Methode permettant d'enregistrer le code du pays dans le fichier shared preferences.
     */
    private void setSharedPreferences (String countryCode) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_COUNTRY_CODE, countryCode);
        editor.commit();
    }

    /**
     * Methode permettant de mettre un titre et une imae dans l'actionBar.
     */
    private void setCountryActionBar() {
        String actionBarTitle = getResources().getString(R.string.custom_action_bar_country_title);
        int mipmapResId = R.mipmap.ic_country_flags;

        CustomActionBar.setActionBar(getActivity(), actionBarTitle, mipmapResId);
    }

    /*
     * Methode mettant le curseur a jour afin de pouvoir mettre a jour le listview.
     */
    private void refreshCursor() {
        countryCursorAdapter.changeCursor(Provider.getInstance().countryCursorOrdered(context, sortField1, sortField2, sortOrder));
        countryCursorAdapter.notifyDataSetChanged();
    }

    /*
     * Methode permettant de mettre un menu avec des options dans la actionBar.
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
        // Triage des pays selon le nom du pays, puis selon le nom du continent
        switch (item.getItemId()) {
            case R.id.country_menu_sort_by_country_name:
                sortField1 = ForecastDBContracts.CountryTable.FIELD_NAME;
                sortField2 = ForecastDBContracts.CountryTable.FIELD_CONTINENT;
                break;
            // Triage des pays selon le nom du continent, puis selon le nom du pays
            case R.id.country_menu_sort_by_continent:
                sortField1 = ForecastDBContracts.CountryTable.FIELD_CONTINENT;
                sortField2 = ForecastDBContracts.CountryTable.FIELD_NAME;
                break;
            // Triage des pays selon la population, puis selon le nom du pays
            case R.id.country_menu_sort_by_population:
                sortField1 = ForecastDBContracts.CountryTable.FIELD_POPULATION;
                sortField2 = ForecastDBContracts.CountryTable.FIELD_NAME;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        // Permet de recuperer la liste des pays trier selon les 2 criteres, puis met a jour la
        // listview.
        Provider.getInstance().countryCursorOrdered(context, sortField1, sortField2, sortOrder);
        refreshCursor();

        return true;
    }

    /*
     * Methode permettant de sauvegarder les etats du countryFragment afin de pouvoir les restaurer
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