package com.pam.abourassa.tp1.model.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anthony on 30/12/2016.
 */

/**
 * Classe permettant de convertir les champs de la string JSON en objet Forecast. Association entre
 * chacun des objets du JSON en attributs de l'objet Forecast. Classe parceler afin de conserver
 * l'objet Forecast lors de la rotation de l'appareil.
 */
public class Forecast implements Parcelable {
    @SerializedName("coord")
    public Coord coord;
    @SerializedName("weather")
    public ArrayList<Weather> weather = null;
    @SerializedName("base")
    public String base;
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("dt")
    public int dt;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("id")
    public int cityId;
    @SerializedName("name")
    public String cityName;
    @SerializedName("cod")
    public int cityCod;

    protected Forecast (Parcel in) {
        base = in.readString();
        dt = in.readInt();
        cityId = in.readInt();
        cityName = in.readString();
        cityCod = in.readInt();
    }

    // Getters et setters
    public Coord getCoord () {
        return coord;
    }

    public void setCoord (Coord coord) {
        this.coord = coord;
    }

    public ArrayList<Weather> getWeather () {
        return weather;
    }

    public void setWeather (ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public String getBase () {
        return base;
    }

    public void setBase (String base) {
        this.base = base;
    }

    public Main getMain () {
        return main;
    }

    public void setMain (Main main) {
        this.main = main;
    }

    public Wind getWind () {
        return wind;
    }

    public void setWind (Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds () {
        return clouds;
    }

    public void setClouds (Clouds clouds) {
        this.clouds = clouds;
    }

    public int getDt () {
        return dt;
    }

    public void setDt (int dt) {
        this.dt = dt;
    }

    public Sys getSys () {
        return sys;
    }

    public void setSys (Sys sys) {
        this.sys = sys;
    }

    public int getCityId () {
        return cityId;
    }

    public void setCityId (int cityId) {
        this.cityId = cityId;
    }

    public String getCityName () {
        return cityName;
    }

    public void setCityName (String cityName) {
        this.cityName = cityName;
    }

    public int getCityCod () {
        return cityCod;
    }

    public void setCityCod (int cityCod) {
        this.cityCod = cityCod;
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel parcel, int i) {
        parcel.writeString(base);
        parcel.writeInt(dt);
        parcel.writeInt(cityId);
        parcel.writeString(cityName);
        parcel.writeInt(cityCod);
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        @Override
        public Forecast createFromParcel (Parcel in) {
            return new Forecast(in);
        }

        @Override
        public Forecast[] newArray (int size) {
            return new Forecast[size];
        }
    };

    // Permet d'avoir acces a la latitude et la longitude de la ville
    public class Coord {
        @SerializedName("lon")
        public float longitude;
        @SerializedName("lat")
        public float latitude;

        // Getters et setters
        public float getLongitude () {
            return longitude;
        }

        public void setLongitude (float longitude) {
            this.longitude = longitude;
        }

        public float getLatitude () {
            return latitude;
        }

        public void setLatitude (float latitude) {
            this.latitude = latitude;
        }
    }

    /**
     * Permet d'avoir acces a la description de la temperature ainsi qu'a l'icone de la ville
     */
    public class Weather {
        @SerializedName("id")
        public int id;
        @SerializedName("main")
        public String main;
        @SerializedName("description")
        public String description;
        @SerializedName("icon")
        public String icon;

        // Getters et setters
        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public String getMain () {
            return main;
        }

        public void setMain (String main) {
            this.main = main;
        }

        public String getDescription () {
            return description;
        }

        public void setDescription (String description) {
            this.description = description;
        }

        public void setIcon (String icon) {
            this.icon = icon;
        }

        public String getIcon () {
            return icon;
        }
    }

    /**
     * Permet d'avoir acces a la temperature (celsius), la pression (hPa), l'humidite, les temperatures
     * maximum et minimum, le niveau de la mer ainsi que le niveau de la terre.
     */
    public class Main {
        @SerializedName("temp")
        public String temperature;
        @SerializedName("pressure")
        public float pressure;
        @SerializedName("humidity")
        public float humidity;
        @SerializedName("temp_min")
        public float minimumTemperature;
        @SerializedName("temp_max")
        public float maximumTemperature;
        @SerializedName("sea_level")
        public float seaLevel;
        @SerializedName("grnd_level")
        public float groundLevel;

        // Getters et setters
        public String getTemperature () {
            return temperature;
        }

        public void setTemperature (String temperature) {
            this.temperature = temperature;
        }

        public float getPressure () {
            return pressure;
        }

        public void setPressure (float pressure) {
            this.pressure = pressure;
        }

        public float getHumidity () {
            return humidity;
        }

        public void setHumidity (float humidity) {
            this.humidity = humidity;
        }

        public float getMinimumTemperature () {
            return minimumTemperature;
        }

        public void setMinimumTemperature (float minimumTemperature) {
            this.minimumTemperature = minimumTemperature;
        }

        public float getMaximumTemperature () {
            return maximumTemperature;
        }

        public void setMaximumTemperature (float maximumTemperature) {
            this.maximumTemperature = maximumTemperature;
        }

        public float getSeaLevel () {
            return seaLevel;
        }

        public void setSeaLevel (float seaLevel) {
            this.seaLevel = seaLevel;
        }

        public float getGroundLevel () {
            return groundLevel;
        }

        public void setGroundLevel (float groundLevel) {
            this.groundLevel = groundLevel;
        }
    }

    /**
     * Permet d'avoir acces a la vitesse du vent
     */
    public class Wind {
        @SerializedName("speed")
        public float speed;
        @SerializedName("deg")
        public float deg;

        // Getters et setters
        public float getSpeed () {
            return speed;
        }

        public void setSpeed (float speed) {
            this.speed = speed;
        }

        public float getDeg () {
            return deg;
        }

        public void setDeg (float deg) {
            this.deg = deg;
        }
    }

    public class Clouds {
        @SerializedName("all")
        public int all;

        public int getAll () {
            return all;
        }

        public void setAll (int all) {
            this.all = all;
        }
    }

    /**
     * Permet d'avoir acces au message, au pays, au lever et au coucher du soleil
     */
    public class Sys{
        @SerializedName("message")
        public float message;
        @SerializedName("country")
        public String countryCode;
        @SerializedName("sunrise")
        public int sunrise;
        @SerializedName("sunset")
        public int sunset;

        // Getters et setters
        public float getMessage () {
            return message;
        }

        public void setMessage (float message) {
            this.message = message;
        }

        public String getCountryCode () {
            return countryCode;
        }

        public void setCountryCode (String countryCode) {
            this.countryCode = countryCode;
        }

        public int getSunrise () {
            return sunrise;
        }

        public void setSunrise (int sunrise) {
            this.sunrise = sunrise;
        }

        public int getSunset () {
            return sunset;
        }

        public void setSunset (int sunset) {
            this.sunset = sunset;
        }
    }

}