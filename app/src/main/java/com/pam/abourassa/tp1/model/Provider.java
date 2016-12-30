package com.pam.abourassa.tp1.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pam.abourassa.tp1.model.Objects.City;
import com.pam.abourassa.tp1.model.Objects.Country;
import com.pam.abourassa.tp1.enums.SortOrder;
import com.pam.abourassa.tp1.model.database.ForecastDBContracts;
import com.pam.abourassa.tp1.model.database.ForecastDBHelper;

/**
 * Created by Anthony on 09/12/2016.
 */

public class Provider {
    private static Provider instance = new Provider();

    // Permet de retourner une instance du Provider
    public static Provider getInstance () {
        return instance;
    }

    /*
     * Permet de creer un objet helper afin d'avoir acces au methode de SQLOpenHelper
     */
    public ForecastDBHelper helper (Context context) {
        return new ForecastDBHelper(context);
    }

    /**
     * Methode permettant de faire une requete a la table country de la base de donnees afin de
     * trier les pays selon 2 criteres et un ordre (ascendant/descendant).
     */
    public Cursor countryCursorOrdered(Context context, String field, String field2, SortOrder sortOrder) {
        SQLiteDatabase database = helper(context).getReadableDatabase();

        String[] projection = ForecastDBContracts.CountryTable.ALL_FIELDS;
        String sort = String.format("%s %s, %s %s", field, sortOrder, field2, sortOrder);

        Cursor cursor = database.query(
                ForecastDBContracts.CountryTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sort
        );
        return cursor;
    }

    /**
     * Methode permettant de faire une requete a la table city de la base de donnees afin de
     * trier les villes selon le contryCode de son pays et de ce qui a ete inscrit dans la barre
     * de recherche (critere de recherche).
     */
    public Cursor cityCursorOrdered(Context context, String countryCode, String searchCriteria) {
        SQLiteDatabase database = helper(context).getReadableDatabase();

        String[] projection = ForecastDBContracts.CityTable.ALL_FIELDS;
        String selection = ForecastDBContracts.CityTable.FIELD_COUNTRY_CODE + " = ? AND " +
                ForecastDBContracts.CityTable.FIELD_NAME + " LIKE ?";
        String[] selectionArgs = {countryCode, "%" + searchCriteria + "%"};
        String sort = ForecastDBContracts.CityTable.FIELD_NAME;

        Cursor cursor = database.query(
                ForecastDBContracts.CityTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sort
        );
        return cursor;
    }

    /**
     * Methode permettant de trouver une ville a l'aide de son id.
     */
    public City findCityById(Context context, int id) {
        SQLiteDatabase database = helper(context).getReadableDatabase();

        String[] projection = ForecastDBContracts.CityTable.ALL_FIELDS;
        String selection = ForecastDBContracts.CityTable.FIELD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = database.query(
                ForecastDBContracts.CityTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.moveToFirst() == true) {
            // country found
            return getCityFromCursor(cursor);
        }else {
            return null;
        }
    }

    /**
     * Methode permettant de trouver un pays a l'aide de son id.
     */
    public Country findCountryById(Context context, int id) {
        SQLiteDatabase database = helper(context).getReadableDatabase();

        String[] projection = ForecastDBContracts.CountryTable.ALL_FIELDS;
        String selection = ForecastDBContracts.CountryTable.FIELD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = database.query(
                ForecastDBContracts.CountryTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.moveToFirst() == true) {
            // country found
            return getCountryFromCursor(cursor);
        }else {
            return null;
        }
    }

    /**
     * Methode permettant de trouver un pays a l'aide du contryCode.
     */
    public Country findCountryByCountryCode(Context context, String countryCode) {
        SQLiteDatabase database = helper(context).getReadableDatabase();

        String[] projection = ForecastDBContracts.CountryTable.ALL_FIELDS;
        String selection = ForecastDBContracts.CountryTable.FIELD_CODE + " = ?";
        String[] selectionArgs = {String.valueOf(countryCode)};

        Cursor cursor = database.query(
                ForecastDBContracts.CountryTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.moveToFirst() == true) {
            // country found
            return getCountryFromCursor(cursor);
        }else {
            return null;
        }
    }

    /**
     * Methode permettant de recuperer un objet country dans la table country de la base de donnees
     * a l'aide d'un curseur. Retourne un objet country avec ses attributs.
     */
    public Country getCountryFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_ID));
        String name = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_NAME));
        String localName = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_LOCAL_NAME));
        String code = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_CODE));
        String continent = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_CONTINENT));
        int population = cursor.getInt(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_POPULATION));

        return new Country(id, name, localName, code, continent, population);
    }

    /**
     * Methode permettant de recuperer un objet city dans la table city de la base de donnees
     * a l'aide d'un curseur. Retourne un objet city avec ses attributs.
     */
    public City getCityFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(ForecastDBContracts.CityTable.FIELD_ID));
        String name = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CityTable.FIELD_NAME));
        float latitude = cursor.getFloat(cursor.getColumnIndex(ForecastDBContracts.CityTable.FIELD_LATITUDE));
        float longitude = cursor.getFloat(cursor.getColumnIndex(ForecastDBContracts.CityTable.FIELD_LONGITUDE));
        String codeCountry = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CityTable.FIELD_COUNTRY_CODE));

        return new City(id, name, latitude, longitude, codeCountry);
    }
}
