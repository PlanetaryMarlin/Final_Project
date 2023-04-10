package com.example.final_project.Weather;

public class WeatherResult {


    protected double current;
    protected double max;
    protected double min;
    protected int humidity;
    public String description;
    public String iconName;

    public WeatherResult(double current, double max, double min, int humidity, String iconName, String description){
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


    public String getIconName() {
        return iconName;}

    public String getDescription() {
        return description;}

}
