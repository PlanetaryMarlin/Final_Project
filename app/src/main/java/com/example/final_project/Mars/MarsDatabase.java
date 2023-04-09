package com.example.final_project.Mars;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MarsFav.class}, version = 1)
public abstract class MarsDatabase extends RoomDatabase {
    public abstract MarsFavDAO mrDAO();
}
