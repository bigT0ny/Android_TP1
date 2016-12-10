package com.pam.abourassa.tp1.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pam.abourassa.tp1.Objects.Country;
import com.pam.abourassa.tp1.Provider;
import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.adapters.CityCursorAdapter;

public class CityFragment extends Fragment {
// parametres d'initialisation de CityFragment
private static final String ARG_COUNTRY_ID = "ca.cs.equipe3.forecast.fragments.ARG_COUNTRY_ID";

private CityCursorAdapter cityCursorAdapter;
private Country country;
private SearchView searchBar;
private ListView citiesList_listview;
private String criteria;
private int countryId;


public CityFragment () {}

/**
 * @param countryId Parameter 1 : le id du pays selectionner.
 * @return Une instance de CityFragment
 */
public static CityFragment newInstance (int countryId) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNTRY_ID, countryId);
        fragment.setArguments(args);
        return fragment;
        }

@Override
public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        countryId = getArguments().getInt(ARG_COUNTRY_ID);
        country = Provider.getInstance().db(getContext()).findCountryById(countryId);
        }
        }

@Override
public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        citiesList_listview = (ListView) view.findViewById(R.id.city_fragment_listview_citiesList);
        searchBar = (SearchView) view.findViewById(R.id.city_fragment_searchview_searchBar);

        return view;
        }

@Override
public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
/*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);

        View customActionBar = inflater.inflate(R.layout.action_bar, null);
        TextView countryName_textiew = (TextView) customActionBar.findViewById(R.id.action_bar_textview_countryName);
        countryName_textiew.setText(country.getName() + "  -  Cities");
*/
        citiesList_listview.setOnItemClickListener(cityRowOnItemClickListener);
        citiesList_listview.setFastScrollEnabled(true);

        searchBar.setOnQueryTextListener(searchBarOnQueryTextListener);
        criteria = "";

        Cursor cityCursor = Provider.getInstance().getCityOrdered(getActivity(), criteria, country.getCode());
        cityCursorAdapter = new CityCursorAdapter(getContext(), cityCursor);
        citiesList_listview.setAdapter(cityCursorAdapter);
        }

public AdapterView.OnItemClickListener cityRowOnItemClickListener = new AdapterView.OnItemClickListener() {
@Override
public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

        }
        };

public SearchView.OnQueryTextListener searchBarOnQueryTextListener = new SearchView.OnQueryTextListener() {
@Override
public boolean onQueryTextSubmit (String query) {
        return false;
        }

@Override
public boolean onQueryTextChange (String newText) {
        criteria = newText;
        refreshCursor();

        return true;
        }
        };

@Override
public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        searchBar.clearFocus();
        menu.clear();
        }

public void refreshCursor() {
        cityCursorAdapter.changeCursor(Provider.getInstance().getCityOrderedByCriteria(getActivity(), criteria, country.getCode()));
        cityCursorAdapter.notifyDataSetChanged();
        }
}
