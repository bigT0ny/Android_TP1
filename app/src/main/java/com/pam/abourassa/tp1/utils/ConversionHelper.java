package com.pam.abourassa.tp1.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Anthony on 30/12/2016.
 */

public class ConversionHelper {
    /**
     * Return a float that represent a temperature in Fahrenheit
     */
    public static float celsisuToFahrenheit(float celsiusTemperature) {
        return ((celsiusTemperature * (9/5f)) + 32.00f);
    }

    /**
     * Return pressure in kPa
     */
    public static float hpaToKpa(float hpa) {
        return (hpa / 10);
    }

    /**
     * Convert unix time to standard time
     */
    public static String unixTimeToStandardTime (long unixTimeStamp) {
        Date date = new Date(unixTimeStamp * 1000L);
        String result = new SimpleDateFormat("HH:mm", Locale.CANADA_FRENCH).format(date);
        return result;
    }

}
