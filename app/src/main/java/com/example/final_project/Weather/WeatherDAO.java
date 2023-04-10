package com.example.final_project.Weather;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface WeatherDAO {

    /**
     * Insert into the database
     * @param weather information
     */
    @Insert
    void insertSave (WeatherResult weather);

    /**
     * A query to get all data
     * @return all items in the database
     */
    @Query("Select * from WeatherResult")
    List<WeatherResult> getAllWeatherSave();

    /**
     * Delete data
     * @param weather
     */
    @Delete
    void deleteFav(WeatherResult weather);
}
