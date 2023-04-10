package com.example.final_project.Weather;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert
    void insertSave (WeatherResult weather);

    @Query("Select * from WeatherResult")
    List<WeatherResult> getAllWeatherSave();

    @Delete
    void deleteFav(WeatherResult weather);
}
