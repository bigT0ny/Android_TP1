package com.pam.abourassa.tp1.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pam.abourassa.tp1.Objects.City;
import com.pam.abourassa.tp1.Objects.Country;
import com.pam.abourassa.tp1.enums.SortOrder;

/**
 * Created by Anthony on 09/12/2016.
 */

public class ForecastDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "com.pam.abourassa.forecast.db";
    private static final int DATABASE_VERSION = 1;

    public ForecastDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(ForecastDBContracts.CountryTable.SQL_CREATE_TABLE);
        db.execSQL(ForecastDBContracts.CityTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ForecastDBContracts.CountryTable.SQL_DELETE_TABLE);
        db.execSQL(ForecastDBContracts.CityTable.SQL_DELETE_TABLE);
        onCreate(db);
    }

    public Cursor countryCursorOrdered(String field, String field2, SortOrder sortOrder) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = ForecastDBContracts.CountryTable.ALL_FIELDS;
        String sort = field + " " + sortOrder + ", " + field2 + " " + sortOrder;
        String selection = field + " LIKE ? OR " + field2 + " LIKE ?";
        String[] selectionArgs = {"%", "%"};

        Cursor cursor = db.query(
                ForecastDBContracts.CountryTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sort
        );

        return cursor;
    }

    public static Country getCountryFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_ID));
        String name = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_NAME));
        String localName = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_LOCAL_NAME));
        String code = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_CODE));
        String continent = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_CONTINENT));
        int population = cursor.getInt(cursor.getColumnIndex(ForecastDBContracts.CountryTable.FIELD_POPULATION));

        return new Country(id, name, localName, code, continent, population);
    }

    public ContentValues getCountryValues(Country country) {
        ContentValues countryValues = new ContentValues();

        countryValues.put(ForecastDBContracts.CountryTable.FIELD_ID, country.getId());
        countryValues.put(ForecastDBContracts.CountryTable.FIELD_CODE, country.getCode());
        countryValues.put(ForecastDBContracts.CountryTable.FIELD_NAME, country.getName());
        countryValues.put(ForecastDBContracts.CountryTable.FIELD_LOCAL_NAME, country.getLocalName());
        countryValues.put(ForecastDBContracts.CountryTable.FIELD_CONTINENT, country.getContinent());
        countryValues.put(ForecastDBContracts.CountryTable.FIELD_POPULATION, country.getPopulation());

        return countryValues;
    }

    public Country findCountryById(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = ForecastDBContracts.CountryTable.ALL_FIELDS;
        String selection = ForecastDBContracts.CountryTable.FIELD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
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

    public Cursor cityCursorOrdered(String countryCode) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = ForecastDBContracts.CityTable.ALL_FIELDS;
        String selection = ForecastDBContracts.CityTable.FIELD_COUNTRY_CODE + " LIKE ?";
        String[] selectionArgs = {countryCode + "%"};
        String sort = ForecastDBContracts.CityTable.FIELD_NAME;

        Cursor cursor = db.query(
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

    public static City getCityFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(ForecastDBContracts.CityTable.FIELD_ID));
        String name = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CityTable.FIELD_NAME));
        double latitude = cursor.getDouble(cursor.getColumnIndex(ForecastDBContracts.CityTable.FIELD_LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndex(ForecastDBContracts.CityTable.FIELD_LONGITUDE));
        String codeCountry = cursor.getString(cursor.getColumnIndex(ForecastDBContracts.CityTable.FIELD_COUNTRY_CODE));

        return new City(id, name, latitude, longitude, codeCountry);
    }

    public ContentValues getCityValues(City city) {
        ContentValues cityValues = new ContentValues();

        cityValues.put(ForecastDBContracts.CityTable.FIELD_ID, city.getId());
        cityValues.put(ForecastDBContracts.CityTable.FIELD_NAME, city.getName());
        cityValues.put(ForecastDBContracts.CityTable.FIELD_LATITUDE, city.getLatitude());
        cityValues.put(ForecastDBContracts.CityTable.FIELD_LONGITUDE, city.getLongitude());
        cityValues.put(ForecastDBContracts.CityTable.FIELD_COUNTRY_CODE, city.getCountryCode());

        return cityValues;
    }

    public City findCityById(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = ForecastDBContracts.CityTable.ALL_FIELDS;
        String selection = ForecastDBContracts.CityTable.FIELD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                ForecastDBContracts.CityTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.moveToFirst() == true) {
            // city found
            return getCityFromCursor(cursor);
        }else {
            return null;
        }
    }

    public Cursor cursorOrderedByCriteria(String criteria, String countryCode) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = ForecastDBContracts.CityTable.ALL_FIELDS;
        String sort = "lower(" + ForecastDBContracts.CityTable.FIELD_NAME + ")";

        String lowerCaseCriteria = "%"+criteria.toLowerCase()+"%";
        String selection = String.format("lower(%s) = ? AND lower(%s) LIKE ?"
                , ForecastDBContracts.CityTable.FIELD_COUNTRY_CODE, ForecastDBContracts.CityTable.FIELD_NAME);

        String[] selectionArgs = {countryCode, lowerCaseCriteria};

        Cursor cursor = db.query(
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
}
