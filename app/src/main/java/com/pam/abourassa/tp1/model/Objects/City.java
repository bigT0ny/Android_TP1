package com.pam.abourassa.tp1.model.Objects;

/**
 * Created by Anthony on 09/12/2016.
 */

public class City {
    // Variables membres de l'objet City
    private int id;
    private String name;
    private float latitude;
    private float longitude;
    private String countryCode;

    // Getters et setters
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

    public float getLatitude () {
        return latitude;
    }

    public void setLatitude (float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude () {
        return longitude;
    }

    public void setLongitude (float longitude) {
        this.longitude = longitude;
    }

    public String getCountryCode () {
        return countryCode;
    }

    public void setCountryCode (String countryCode) {
        this.countryCode = countryCode;
    }

    // Constructeurs de City
    public City(int id, String name, float latitude, float longitude, String countryCode) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryCode = countryCode;
    }

    public City(String name, float latitude, float longitude, String countryCode) {
        this(-1, name, latitude, longitude, countryCode);
    }

}
