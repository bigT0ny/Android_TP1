package com.pam.abourassa.tp1.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Anthony on 30/12/2016.
 */

/**
 * Classe permettant de faire la conversion d'unite comme la temperature (celsius - fahrenheit), la
 * pressiont (hPa - kPa) et le temps (unix time - standard time).
 */
public class UnitConversionHelper {
    /**
     * Methode permettant de convertir la temperature en celsius en fahrenheit
     */
    public static float celsisuToFahrenheit(float celsiusTemperature) {
        return ((celsiusTemperature * (9/5f)) + 32.00f);
    }

    /**
     * Methode permettant de convertir la pression (hPa) en kPa
     */
    public static float hpaToKpa(float hpa) {
        return (hpa / 10);
    }

    /**
     * Methode permettant de convertir le temps (unixtime) en temps standard (heure:minute)
     */
    public static String unixTimeToStandardTime (long unixTimeStamp) {
        Date date = new Date(unixTimeStamp * 1000L);
        return new SimpleDateFormat("HH:mm", Locale.CANADA_FRENCH).format(date);
    }

}