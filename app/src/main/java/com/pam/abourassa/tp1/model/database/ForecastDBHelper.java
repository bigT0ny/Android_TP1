package com.pam.abourassa.tp1.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anthony on 09/12/2016.
 */

public class ForecastDBHelper extends SQLiteOpenHelper {
    // Nom de la base de donnees et son numero de version
    private static final String DATABASE_NAME = "ca.cs.equipe3.forecast.tp1.db";
    private static final int DATABASE_VERSION = 1;

    public ForecastDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
     * Creation des tables country et city dans la base de donnees.
     */
    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(ForecastDBContracts.CountryTable.SQL_CREATE_TABLE);
        db.execSQL(ForecastDBContracts.CityTable.SQL_CREATE_TABLE);
    }

    /*
     * Mis a jour des tables de la base de donnees
     */
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ForecastDBContracts.CountryTable.SQL_DELETE_TABLE);
        db.execSQL(ForecastDBContracts.CityTable.SQL_DELETE_TABLE);
        onCreate(db);
    }

}
