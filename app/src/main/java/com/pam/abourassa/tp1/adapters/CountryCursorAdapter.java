package com.pam.abourassa.tp1.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pam.abourassa.tp1.Objects.Country;
import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.model.ForecastDBHelper;

/**
 * Created by Anthony on 09/12/2016.
 */

public class CountryCursorAdapter extends CursorAdapter {

    public CountryCursorAdapter (Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public void notifyDataSetChanged () {
        super.notifyDataSetChanged();
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_country_list_row, parent, false);
    }

    @Override
    public void bindView (View view, Context context, Cursor cursor) {
        Country country = ForecastDBHelper.getCountryFromCursor(cursor);

        if (country != null) {
            String countryCode = country.getCode();
            String path = "http://www.geognos.com/api/en/countries/flag/" + countryCode + ".png";

            ImageView flag_imageview = (ImageView) view.findViewById(R.id.country_listview_row_imageview_flag);
            //Picasso.with(context).load(path).into(flag_imageview);

            TextView countryName_textview = (TextView) view.findViewById(R.id.country_listview_row_textview_countryName);
            TextView continentName_textview = (TextView) view.findViewById(R.id.country_listview_row_textview_continentName);
            TextView population_textview = (TextView) view.findViewById(R.id.country_listview_row_textview_population);

            countryName_textview.setText(country.getName());
            continentName_textview.setText(country.getContinent());
            population_textview.setText(String.valueOf(country.getPopulation()));
        }
    }

}
