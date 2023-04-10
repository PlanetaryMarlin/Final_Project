package com.example.final_project.Mars;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database class for MarsResult objects saved as favourites
 */
@Database(entities = {MarsResult.class}, version = 1)
public abstract class MarsDatabase extends RoomDatabase {
    public abstract MarsResultDAO mrDAO();
}
