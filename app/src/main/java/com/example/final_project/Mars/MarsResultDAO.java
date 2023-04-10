package com.example.final_project.Mars;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO for MarsResult class
 */
@Dao
public interface MarsResultDAO {
    /**
     * Database Insert statement
     * @param r MarsResult object to be inserted
     * @return long
     */
    @Insert
    long insertFav(MarsResult r);

    /**
     * Query for selecting all items in database
     * @return List of MarsResult items
     */
    @Query("Select * from MarsResult")
    List<MarsResult> getAllFavs();

    /**
     * Query for selecting item by id
     * @param id id to search for
     * @return List of matching MarsResult items
     */
    @Query("Select * from MarsResult where imgID = :id")
    List<MarsResult> searchByID(String id);

    /**
     * Delete statement
     * @param r MarsResult item to be deleted
     */
    @Delete
    void deleteFav(MarsResult r);
}
