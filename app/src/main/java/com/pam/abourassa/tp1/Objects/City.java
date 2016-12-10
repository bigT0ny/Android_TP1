package com.pam.abourassa.tp1.Objects;

/**
 * Created by Anthony on 09/12/2016.
 */

public class City {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String countryCode;

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public double getLatitude () {
        return latitude;
    }

    public void setLatitude (double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude () {
        return longitude;
    }

    public void setLongitude (double longitude) {
        this.longitude = longitude;
    }

    public String getCountryCode () {
        return countryCode;
    }

    public void setCountryCode (String countryCode) {
        this.countryCode = countryCode;
    }

    public City(int id, String name, double latitude, double longitude, String countryCode) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryCode = countryCode;
    }

    public City(String name, double latitude, double longitude, String countryCode) {
        this(-1, name, latitude, longitude, countryCode);
    }

}
