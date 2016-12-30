package com.pam.abourassa.tp1.model.database;

import android.provider.BaseColumns;

/**
 * Created by Anthony on 09/12/2016.
 */

public class ForecastDBContracts {

    // Constructeur vide permettant d'eviter l'instanciation d'un objet ForecastDBContract
    public ForecastDBContracts() {
    }

    public static abstract class CountryTable implements BaseColumns {
        // Noms et champs des tables de la base de donnees
        public static final String TABLE_NAME = "country";
        public static final String FIELD_ID = "_id";
        public static final String FIELD_CODE = "code";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_LOCAL_NAME = "local_name";
        public static final String FIELD_CONTINENT = "continent";
        public static final String FIELD_POPULATION = "population";
        public static final String[] ALL_FIELDS = {FIELD_ID, FIELD_CODE, FIELD_NAME,
                FIELD_LOCAL_NAME, FIELD_CONTINENT, FIELD_POPULATION};

        // Requete SQL de creation de la table
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                FIELD_ID +              " INTEGER PRIMARY KEY," +
                FIELD_CODE +            " TEXT," +
                FIELD_NAME +            " TEXT," +
                FIELD_LOCAL_NAME +      " TEXT," +
                FIELD_CONTINENT +       " TEXT," +
                FIELD_POPULATION +      " INTEGER)";

        // Requete SQL de suppression de la table
        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class CityTable implements BaseColumns {
        // Noms et champs des tables de la base de donnees
        public static final String TABLE_NAME = "city";
        public static final String FIELD_ID = "_id";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_LATITUDE = "latitude";
        public static final String FIELD_LONGITUDE = "longitude";
        public static final String FIELD_COUNTRY_CODE = "country_code";
        public static final String[] ALL_FIELDS = {FIELD_ID, FIELD_NAME, FIELD_LATITUDE,
                FIELD_LONGITUDE, FIELD_COUNTRY_CODE};

        // Requete SQL de creation de la table
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                FIELD_ID +              " INTEGER PRIMARY KEY," +
                FIELD_NAME +            " TEXT," +
                FIELD_LATITUDE +        " TEXT," +
                FIELD_LONGITUDE +       " TEXT," +
                FIELD_COUNTRY_CODE +    " TEXT)";

        // Requete SQL de suppression de la table
        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
