package com.example.final_project.Mars;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Class for holding search results as objects
 */
@Entity
public class MarsResult {
    /** image ID from NASA API, primary key*/
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "imgID")
    protected String imgID;
    /** image url from NASA API*/
    @ColumnInfo(name = "imgSrc")
    protected String imgSrc;
    /** camera name from NASA API*/
    @ColumnInfo(name = "camName")
    protected String camName;
    /** rover name from NASA API*/
    @ColumnInfo(name = "roverName")
    protected String roverName;
    /** image filename for saving to local storage*/
    @ColumnInfo(name = "imgPath")
    protected String imgPath;
    /** holds photo as bitmap, ignored by database*/
    @Ignore
    protected Bitmap bitmap;

    /**
     * Class constructor for MarsResult
     * @param imgID image ID
     * @param imgSrc image url
     * @param camName camera name
     * @param roverName rover name
     */
    @Ignore
    public MarsResult(String imgID, String imgSrc, String camName, String roverName){
        this.imgID = imgID;
        this.imgSrc = imgSrc;
        this.camName = camName;
        this.roverName = roverName;
    }

    /**
     * Constructor for MarsResult with imgPath param
     * @param imgID image ID
     * @param imgSrc image url
     * @param camName camera name
     * @param roverName rover name
     * @param imgPath image filename
     */
    public MarsResult(String imgID, String imgSrc, String camName, String roverName, String imgPath){
        this.imgID = imgID;
        this.imgSrc = imgSrc;
        this.camName = camName;
        this.roverName = roverName;
        this.imgPath = imgPath;
    }


    /**
     * imgID getter
     * @return imgID
     */
    public String getImgID() {return imgID;}

    /**
     * imgSrc getter
     * @return imgSrc
     */
    public String getImgSrc() {return imgSrc;}

    /**
     * camName getter
     * @return camName
     */
    public String getCamName() {return camName;}

    /**
     * roverName getter
     * @return roverName
     */
    public String getRoverName() {return roverName;}

    /**
     * bitmap setter
     * @param bitmap photo as bitmap
     */
    public void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }

    /**
     * bitmap getter
     * @return bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * imgPath getter
     * @return imgPath
     */
    public String getImgPath() { return imgPath; }
}
