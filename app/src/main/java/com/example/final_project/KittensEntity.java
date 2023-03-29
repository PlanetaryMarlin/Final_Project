package com.example.final_project;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
//tells the database what to store

@Entity
public class KittensEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "height")
    protected String height;

    @ColumnInfo(name = "width")
    protected String width;

    @ColumnInfo(name = "TimeSaved")
    protected String timeSaved;

    @ColumnInfo(name = "ImageUrl")
    protected String imageUrl;
}
