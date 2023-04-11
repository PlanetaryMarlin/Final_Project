package com.example.final_project.Weather;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class WeatherResult {
    /**
     * The Primary Key of the Database
     * Auto generated
     */
    @PrimaryKey (autoGenerate = true) @NonNull
    @ColumnInfo(name = "ID")
    public int id;

    /**
     * City name from the search
     * A column in the table
     */
    @ColumnInfo(name = "city_name")
    protected String city_name;

    /**
     * Country name from the search
     * A column in the table
     */
    @ColumnInfo(name = "country_name")
    protected String country_name;

    @ColumnInfo(name = "time")
    protected String time;

    @ColumnInfo(name = "temp")
    protected double temp;

    @ColumnInfo(name = "humidity")
    protected int humidity;

    @ColumnInfo(name = "visibility")
    public double visibility;

    @ColumnInfo(name = "feelslike")
    protected double feelslike;


    @Ignore
    protected Bitmap bitmap;

    /**
     * This class will contain the user search, and place it into the database
     * @param city_name contains the city name
     * @param country_name contains the country
     * @param time contains the time the seach was made
     * @param temp contains the temperature
     * @param humidity contains the humidity
     * @param feelslike contains the what the weather feels like
     * @param visibility contains the visibility
     */
    public WeatherResult(String city_name, String country_name, String time, double temp, int humidity, double feelslike, double visibility) {
       // this.id = id;
        this.city_name = city_name;
        this.country_name = country_name;
        this.time = time;
        this.temp = temp;
        this.humidity = humidity;
        this.visibility = visibility;
        this.feelslike = feelslike;
    }


    /**
     *
     * @return city name
     */
    public String getCityName() {
        return city_name;
    }

    /**
     *
     * @return country name
     */
    public String getCountryName() {
        return country_name;
    }

    /**
     *
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @return temperature
     */
    public double getTemp() {
        return temp;
    }

    /**
     *
     * @return visibility
     */
    public double getVis() {
        return visibility;
    }

    /**
     *
     * @return feelslike
     */
    public double getFeel() {
        return feelslike;
    }

}
