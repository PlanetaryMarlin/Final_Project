package com.example.final_project;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
//whenever there is a DAO class have to annotate it with DAO
//This class deals with CRUD operation

public interface KittensDAO {
    @Insert
        //ChatMessage is a database row, the @Insert does the insert operation
    void insertMessage(KittensEntity m);
    //@Query is whenever you want to make a SQL query
    @Query("Select * from KittensEntity")
    List<KittensEntity> getAllMessages();

    @Delete
    void deleteMessage(KittensEntity m);
}
