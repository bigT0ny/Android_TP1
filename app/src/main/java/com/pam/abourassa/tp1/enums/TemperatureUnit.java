package com.pam.abourassa.tp1.enums;

/**
 * Created by Anthony on 01/01/2017.
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

}
