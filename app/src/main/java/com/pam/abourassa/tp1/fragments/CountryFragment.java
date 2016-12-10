package com.pam.abourassa.tp1.fragments;

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

import com.pam.abourassa.tp1.Objects.Country;
import com.pam.abourassa.tp1.PreferencesManager;
import com.pam.abourassa.tp1.Provider;
import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.adapters.CountryCursorAdapter;
import com.pam.abourassa.tp1.enums.SortOrder;
import com.pam.abourassa.tp1.model.DataImporter;
import com.pam.abourassa.tp1.model.ForecastDBContracts;
import com.pam.abourassa.tp1.model.ForecastDBHelper;


public class CountryFragment extends Fragment {
    private static final String KEY_SORT_ORDER = "ca.cs.equipe3.forecast.activities.KEY_SORT_ORDER";
    private static final String KEY_SORT_FIELD_1 = "ca.cs.equipe3.forecast.activities.KEY_SORT_FIELD_1";
    private static final String KEY_SORT_FIELD_2 = "ca.cs.equipe3.forecast.activities.KEY_SORT_FIELD_2";

    private SortOrder sortOrder;
    private String sortField1;
    private String sortField2;
    private Spinner sortOrder_spinner;
    private ListView countriesList_listview;
    private CountryCursorAdapter countryCursorAdapter;

    public CountryFragment () {}

    public static CountryFragment newInstance () {
        return new CountryFragment();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);
        sortOrder_spinner = (Spinner) view.findViewById(R.id.activity_main_spinner_sortOrder);
        countriesList_listview = (ListView) view.findViewById(R.id.activity_main_listview_countries);
        return view;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        getPreferencesManager();

        if (savedInstanceState != null) {
            sortOrder = SortOrder.createFromInt(savedInstanceState.getInt(KEY_SORT_ORDER));
            sortField1 = savedInstanceState.getString(KEY_SORT_FIELD_1);
            sortField2 = savedInstanceState.getString(KEY_SORT_FIELD_2);
        }else {
            sortField1 = ForecastDBContracts.CountryTable.FIELD_NAME;
            sortField2 = ForecastDBContracts.CountryTable.FIELD_CONTINENT;
            sortOrder = SortOrder.ASC;
        }

        sortOrder_spinner.setOnItemSelectedListener(sortOrderOnItemSelectedListener);

        countriesList_listview.setOnItemClickListener(countryRowOnItemClickListener);
        countriesList_listview.setFastScrollEnabled(true);

        Cursor countryCursor = Provider.getInstance().getCountryOrdered(getActivity(), sortField1, sortField2, sortOrder);
        countryCursorAdapter = new CountryCursorAdapter(getActivity(), countryCursor);
        countriesList_listview.setAdapter(countryCursorAdapter);
    }

    public AdapterView.OnItemClickListener countryRowOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
            Cursor countryCursor = countryCursorAdapter.getCursor();
            countryCursor.moveToPosition(position);
            Country country = ForecastDBHelper.getCountryFromCursor(countryCursor);

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            CityFragment cityFragment = CityFragment.newInstance(country.getId());
            fragmentTransaction.replace(R.id.activity_main_fragment, cityFragment)
                    .addToBackStack(null)
                    .commit();
        }
    };

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

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.country_sort_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.country_menu_sort_by_country_name:
                sortField1 = ForecastDBContracts.CountryTable.FIELD_NAME;
                sortField2 = ForecastDBContracts.CountryTable.FIELD_CONTINENT;

                Provider.getInstance().db(getActivity()).countryCursorOrdered(sortField1, sortField2, sortOrder);
                refreshCursor();

                return true;

            case R.id.country_menu_sort_by_continent:
                sortField1 = ForecastDBContracts.CountryTable.FIELD_CONTINENT;
                sortField2 = ForecastDBContracts.CountryTable.FIELD_NAME;

                Provider.getInstance().db(getActivity()).countryCursorOrdered(sortField1, sortField2, sortOrder);
                refreshCursor();

                return true;

            case R.id.country_menu_sort_by_population:
                sortField1 = ForecastDBContracts.CountryTable.FIELD_POPULATION;
                sortField2 = ForecastDBContracts.CountryTable.FIELD_NAME;

                Provider.getInstance().db(getActivity()).countryCursorOrdered(sortField1, sortField2, sortOrder);
                refreshCursor();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPreferencesManager() {
        PreferencesManager preferenceManager = new PreferencesManager(getActivity());

        if (preferenceManager.isFisrtLaunch()) {
            getActivity().setTitle(R.string.country_listview_row_textView_country_name);

            DataImporter.importCountryCsvInDatabase(getActivity());
            DataImporter.importCityCsvInDatabase(getActivity());

            preferenceManager.setIsFirstLaunch(false);
        }
    }

    private void refreshCursor() {
        countryCursorAdapter.changeCursor(Provider.getInstance().getCountryOrdered(getActivity(), sortField1, sortField2, sortOrder));
        countryCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        outState.putInt(KEY_SORT_ORDER, sortOrder.getValue());
        outState.putString(KEY_SORT_FIELD_1, sortField1);
        outState.putString(KEY_SORT_FIELD_2, sortField2);

        super.onSaveInstanceState(outState);
    }

}
