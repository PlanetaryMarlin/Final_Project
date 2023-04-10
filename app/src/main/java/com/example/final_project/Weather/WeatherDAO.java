package com.example.final_project.Weather;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert
    void insertSave (WeatherSave weather);

    @Query("Select * from WeatherSave")
    List<WeatherSave> getAllWeatherSave();
}
