package com.pam.abourassa.tp1;

import android.content.Context;
import android.database.Cursor;

import com.pam.abourassa.tp1.enums.SortOrder;
import com.pam.abourassa.tp1.model.ForecastDBHelper;

/**
 * Created by Anthony on 09/12/2016.
 */

public class Provider {
    private static Provider instance = new Provider();

    public static Provider getInstance () {
        return instance;
    }

    public ForecastDBHelper db (Context context) {
        return new ForecastDBHelper(context);
    }

    public Cursor getCountryOrdered(Context context, String sortField1, String sortField2, SortOrder sortOrder) {
        return db(context).countryCursorOrdered(sortField1, sortField2, sortOrder);
    }

    public Cursor getCityOrdered(Context context, String criteria, String countryCode) {
        return db(context).cityCursorOrdered(countryCode);
    }

    public Cursor getCityOrderedByCriteria(Context context, String criteria, String countryCode) {
        return db(context).cursorOrderedByCriteria(criteria, countryCode);
    }

}
