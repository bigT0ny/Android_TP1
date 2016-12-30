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

import com.pam.abourassa.tp1.model.Objects.City;
import com.pam.abourassa.tp1.model.Provider;
import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.model.database.ForecastDBContracts;

/**
 * Created by Anthony on 09/12/2016.
 */

public class CityCursorAdapter extends CursorAdapter implements SectionIndexer {
    private AlphabetIndexer alphaIndexer;

    /**
     * Constructeur de la classe CityCursorAdapter permettant d'afficher les lettres de
     * l'alphabet a droite dans le listview.
     */
    public CityCursorAdapter (Context context, Cursor cursor) {
        super(context, cursor, 0);

        alphaIndexer = new AlphabetIndexer(cursor, cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_NAME),
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    @Override
    public void notifyDataSetChanged () {
        super.notifyDataSetChanged();
    }

    // Methode permettant de faire afficher un layout a l'ecran.
    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_city_list_row, parent, false);
    }

    /**
     * Methode permettant de faire afficher le nom de la ville dans un listview. A l'aide du curseur,
     * l'objet ville est recree afin de pouvoir recuperer son nom et de pouvoir l'afficher dans le
     * listview.
     */
    @Override
    public void bindView (View view, Context context, Cursor cursor) {
        City city = Provider.getInstance().getCityFromCursor(cursor);
        String cityName = city.getName();
        String cityNameFirstLetterCapitalise = "";
        TextView cityName_textview = (TextView) view.findViewById(R.id.city_list_row_textview_cityName);

        if (city != null) {
            /*
             * Sert a prendre le nom de chaque ville et de mettre la premiere lettre de la ville
             * en majuscule, puisqu'il y a au moins une ville en lettre minuscule dans le fichier
             * csv alors que les autres villes commencent par une majuscule.
             */
            if (! city.getName().isEmpty()) {
                cityNameFirstLetterCapitalise = cityName.substring(0, 1).toUpperCase() + cityName.substring(1);
                cityName_textview.setText(cityNameFirstLetterCapitalise);
            }else {
                cityName_textview.setText(cityName);
            }
        }
    }

    // Methodes permettant d'implementer un indexeur alphabetique dans le listview
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
