package com.example.final_project.Mars;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarsFavDAO {
    @Insert
    long insertFav(MarsFav r);

    @Query("Select * from MarsFav")
    List<MarsFav> getAllFavs();

    @Query("Select * from MarsFav where imgID = :id")
    List<MarsFav> searchByID(String id);

    @Delete
    void deleteFav(MarsFav r);
}
