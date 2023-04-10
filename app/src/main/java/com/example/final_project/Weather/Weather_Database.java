package com.example.final_project.Weather;
import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database class for Weather Database
 */
@Database(entities = {WeatherResult.class}, version = 1)
public abstract class Weather_Database extends RoomDatabase {
    public abstract WeatherDAO weatherDAO();
}
