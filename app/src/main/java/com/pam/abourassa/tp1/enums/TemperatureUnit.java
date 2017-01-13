package com.pam.abourassa.tp1.enums;

/**
 * Created by Anthony on 01/01/2017.
 */

/**
 * Enum permettant de convertir des strings en unite de temperature et vice-versa.
 */
public enum TemperatureUnit {
    CELSIUS ("Celsius"),
    FAHRENHEIT ("Fahrenheit");

    String temperatureUnit = "";

    TemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String toString() {
        return temperatureUnit;
    }

    /**
     * Methode permettant de convertir un string en unite de temperature.
     */
    public static TemperatureUnit stringToTemperatureUnit(String temp_unit) {
        switch (temp_unit) {
            case "Celsius":
                return TemperatureUnit.CELSIUS;
            case "Fahrenheit":
                return TemperatureUnit.FAHRENHEIT;
            default:
                return null;
        }
    }

}
