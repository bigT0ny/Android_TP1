package com.pam.abourassa.tp1.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pam.abourassa.tp1.Provider;
import com.pam.abourassa.tp1.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Anthony on 09/12/2016.
 */

public class DataImporter {

    public static void importCountryCsvInDatabase(Context context) {
        SQLiteDatabase db =  Provider.getInstance().db(context).getWritableDatabase();
        InputStream countryInput = context.getResources().openRawResource(R.raw.country);
        boolean skipFirstLine = true;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(countryInput));
            ContentValues values = new ContentValues();
            String line;

            db.beginTransaction();
            while ((line = bufferedReader.readLine()) != null) {
                if (! skipFirstLine) {
                    String[] columns = line.split(",");

                    String _id = columns[0].trim();
                    String name = columns[1].trim();
                    String localName = columns[2].trim();
                    String code = columns[3].trim();
                    String continent = columns[4].trim();
                    String population = columns[5].trim();

                    if (columns.length != 7) {
                        Log.d("Country CSVParser", "Skipping Bad CSV Row");
                        continue;
                    }

                    values.put(ForecastDBContracts.CountryTable.FIELD_ID, _id);
                    values.put(ForecastDBContracts.CountryTable.FIELD_NAME, name);
                    values.put(ForecastDBContracts.CountryTable.FIELD_LOCAL_NAME, localName);
                    values.put(ForecastDBContracts.CountryTable.FIELD_CODE, code);
                    values.put(ForecastDBContracts.CountryTable.FIELD_CONTINENT, continent);
                    values.put(ForecastDBContracts.CountryTable.FIELD_POPULATION, Integer.parseInt(population));

                    db.insert(ForecastDBContracts.CountryTable.TABLE_NAME, null, values);
                }

                skipFirstLine = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void importCityCsvInDatabase(Context context) {
        SQLiteDatabase db = Provider.getInstance().db(context).getWritableDatabase();
        InputStream cityInput = context.getResources().openRawResource(R.raw.city);
        boolean skipFirstLine = true;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cityInput));
            ContentValues values = new ContentValues();
            String line;

            db.beginTransaction();
            while ((line = bufferedReader.readLine()) != null) {
                if (! skipFirstLine) {
                    String[] columns = line.split(",");

                    String _id = columns[0].trim();
                    String name = columns[1].trim();
                    String latitude = columns[2].trim();
                    String longitude = columns[3].trim();
                    String countryCode = columns[4].trim();

                    if (columns.length != 5) {
                        Log.d("City CSVParser", "Skipping Bad CSV Row");
                        continue;
                    }

                    values.put(ForecastDBContracts.CityTable.FIELD_ID, _id);
                    values.put(ForecastDBContracts.CityTable.FIELD_NAME, name);
                    values.put(ForecastDBContracts.CityTable.FIELD_LATITUDE, latitude);
                    values.put(ForecastDBContracts.CityTable.FIELD_LONGITUDE, longitude);
                    values.put(ForecastDBContracts.CityTable.FIELD_COUNTRY_CODE, countryCode);

                    db.insert(ForecastDBContracts.CityTable.TABLE_NAME, null, values);
                }

                skipFirstLine = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

}
