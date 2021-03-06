package com.pam.abourassa.tp1.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.model.providers.Provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Anthony on 09/12/2016.
 */

/**
 * Classe permettant l'importation des donnees provenant des fichiers csv (country et city), qui se
 * trouvent dans le fichier raw du projet (dans res), dans la base de donnees.
 */
public class DataImporter {

    /*
     * Methode permettant d'importer les pays du fichier country.csv et de les mettre dans la
     * table country de la base de donnees.
     */
    public static void importCountryCsvInDatabase(Context context) {
        SQLiteDatabase database = Provider.getInstance().helper(context).getReadableDatabase();
        InputStream countryInput = context.getResources().openRawResource(R.raw.country);

        // Sert a sauter la premiere ligne du fichier, qui n'est pas un pays.
        boolean skipFirstLine = true;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(countryInput));
            ContentValues values = new ContentValues();
            String line;

            database.beginTransaction();
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

                    database.insert(ForecastDBContracts.CountryTable.TABLE_NAME, null, values);
                }
                skipFirstLine = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    /*
     * Methode permettant d'importer les villes du fichier csv city.csv et de les mettre dans la
     * table city de la base de donnees.
     */
    public static void importCityCsvInDatabase(Context context) {
        SQLiteDatabase database = Provider.getInstance().helper(context).getReadableDatabase();
        InputStream cityInput = context.getResources().openRawResource(R.raw.city);
        boolean skipFirstLine = true;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cityInput));
            ContentValues values = new ContentValues();
            String line;

            database.beginTransaction();
            while ((line = bufferedReader.readLine()) != null) {
                if (! skipFirstLine) {
                    String[] columns = line.split(",");

                    String _id = columns[0].trim();
                    String name = columns[1].trim().toLowerCase();
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

                    database.insert(ForecastDBContracts.CityTable.TABLE_NAME, null, values);
                }
                skipFirstLine = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

}
