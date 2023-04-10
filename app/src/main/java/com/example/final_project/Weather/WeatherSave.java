package com.example.final_project.Weather;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class WeatherSave {

    @PrimaryKey @NonNull
    @ColumnInfo(name = "imgID")
    public int id;
    @ColumnInfo(name = "current")
    protected double current;

    @ColumnInfo(name = "current")
    protected double max;

    @ColumnInfo(name = "current")
    protected double min;

    @ColumnInfo(name = "current")
    protected int humidity;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "iconName")
    public String iconName;

    public WeatherSave(double current, double max, double min, int humidity, String iconName, String description){
        this.current = current;
        this.max = max;
        this.min = min;
        this.humidity = humidity;
        this.iconName = iconName;
        this.description = description;
    }


    public double getCurrent() {
        return current;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getIconName() {
        return iconName;}

    public String getDescription() {
        return description;}

}
