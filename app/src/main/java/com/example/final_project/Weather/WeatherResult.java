package com.example.final_project.Weather;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class WeatherResult {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "ID")
    public int id;
    @ColumnInfo(name = "city_name")
    protected String city_name;

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


    public String getCityName() {
        return city_name;
    }

    public String getCountryName() {
        return country_name;
    }

    public String getTime() {
        return time;
    }

    public double getTemp() {
        return temp;
    }

    public double getVis() {
        return visibility;
    }

    public double getFeel() {
        return feelslike;
    }

}
