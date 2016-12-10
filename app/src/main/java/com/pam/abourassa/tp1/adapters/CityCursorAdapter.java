package com.pam.abourassa.tp1.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CursorAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.pam.abourassa.tp1.Objects.City;
import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.model.ForecastDBContracts;
import com.pam.abourassa.tp1.model.ForecastDBHelper;

/**
 * Created by Anthony on 09/12/2016.
 */

public class CityCursorAdapter extends CursorAdapter implements SectionIndexer {
    private AlphabetIndexer alphaIndexer;

    public CityCursorAdapter (Context context, Cursor cursor) {
        super(context, cursor, 0);

        alphaIndexer = new AlphabetIndexer(cursor, cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_NAME),
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_city_list_row, parent, false);
    }

    @Override
    public void bindView (View view, Context context, Cursor cursor) {
        City city = ForecastDBHelper.getCityFromCursor(cursor);

        if (city != null) {
            TextView cityName_textview = (TextView) view.findViewById(R.id.city_list_row_textview_cityName);
            cityName_textview.setText(city.getName());
        }
    }

    @Override
    public Object[] getSections () {
        return alphaIndexer.getSections();
    }

    @Override
    public int getPositionForSection (int section) {
        return alphaIndexer.getPositionForSection(section);
    }

    @Override
    public int getSectionForPosition (int section) {
        return alphaIndexer.getSectionForPosition(section);
    }

    public Cursor swapCursor(Cursor cursor) {
        alphaIndexer = new AlphabetIndexer(cursor, cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_NAME),
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        return super.swapCursor(cursor);
    }

}
