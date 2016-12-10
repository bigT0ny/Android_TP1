package com.pam.abourassa.tp1.Objects;

/**
 * Created by Anthony on 09/12/2016.
 */

public class Country {
    private int id;
    private String name;
    private String localName;
    private String code;
    private String continent;
    private int population;

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

    public String getLocalName () {
        return localName;
    }

    public void setLocalName (String localName) {
        this.localName = localName;
    }

    public String getCode () {
        return code;
    }

    public void setCode (String code) {
        this.code = code;
    }

    public String getContinent () {
        return continent;
    }

    public int getPopulation () {
        return population;
    }

    public void setPopulation (int population) {
        this.population = population;
    }

    public void setContinent (String continent) {
        this.continent = continent;
    }

    public Country(int id, String name, String localName, String code, String continent, int population) {
        this.id = id;
        this.name = name;
        this.localName = localName;
        this.code = code;
        this.continent = continent;
        this.population = population;
    }

    public Country(String name,String localName, String code, String continent, int population) {
        this(-1, name, localName, code, continent, population);
    }

}
