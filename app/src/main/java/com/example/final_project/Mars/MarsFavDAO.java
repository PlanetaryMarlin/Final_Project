package com.example.final_project.Mars;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO for MarsFav class
 */
@Dao
public interface MarsFavDAO {
    /**
     * Database Insert statement
     * @param r MarsFav object to be inserted
     * @return long
     */
    @Insert
    long insertFav(MarsFav r);

    /**
     * Query for selecting all items in database
     * @return List of MarsFav items
     */
    @Query("Select * from MarsFav")
    List<MarsFav> getAllFavs();

    /**
     * Query for selecting item by id
     * @param id id to search for
     * @return List of matching MarsFav items
     */
    @Query("Select * from MarsFav where imgID = :id")
    List<MarsFav> searchByID(String id);

    /**
     * Delete statement
     * @param r MarsFav item to be deleted
     */
    @Delete
    void deleteFav(MarsFav r);
}
