package com.example.final_project.Mars;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class MarsFav extends MarsObj {
    @PrimaryKey @NonNull
    @ColumnInfo(name = "imgID")
    protected String imgID;
    @ColumnInfo(name = "imgSrc")
    protected String imgSrc;
    @ColumnInfo(name = "camName")
    protected String camName;
    @ColumnInfo(name = "roverName")
    protected String roverName;
    @ColumnInfo(name = "imgPath")
    protected String imgPath;
    @Ignore
    protected Bitmap bitmap;

    public MarsFav(String imgID, String imgSrc, String camName, String roverName, String imgPath){
        this.imgID = imgID;
        this.imgSrc = imgSrc;
        this.camName = camName;
        this.roverName = roverName;
        this.imgPath = imgPath;
    }

    @Override
    public String getImgID() {return imgID;}

    @Override
    public String getImgSrc() {return imgSrc;}

    @Override
    public String getCamName() {return camName;}

    @Override
    public String getRoverName() {return roverName;}

    @Override
    public void setBitmap(Bitmap bitmap) { this.bitmap = bitmap;}

    @Override
    public Bitmap getBitmap() { return bitmap; }

    @Override
    public String getImgPath() {return imgPath;}
}
