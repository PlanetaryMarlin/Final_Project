package com.example.final_project.Weather;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class WeatherResult {

    @PrimaryKey @NonNull
    @ColumnInfo(name = "ID")
    public int id;
    @ColumnInfo(name = "current")
    protected double current;

    @ColumnInfo(name = "max")
    protected double max;

    @ColumnInfo(name = "min")
    protected double min;

    @ColumnInfo(name = "humidity")
    protected int humidity;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "iconName")
    public String iconName;

    @Ignore
    protected Bitmap bitmap;

    public WeatherResult(int id, double current, double max, double min, int humidity, String iconName, String description){
        this.id = id;
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
