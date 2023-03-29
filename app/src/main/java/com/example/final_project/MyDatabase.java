package com.example.final_project;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {KittensEntity.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract KittensDAO cmDAO();
    //ChatMessageDAO is interface
    //cmDAO() is method


}