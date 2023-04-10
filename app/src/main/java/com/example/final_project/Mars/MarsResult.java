package com.example.final_project.Mars;

import android.graphics.Bitmap;

/**
 * Class for holding search results as objects
 */
public class MarsResult extends MarsObj {
    /** image ID from NASA API */
    protected String imgID;
    /** image url from NASA API*/
    protected String imgSrc;
    /** camera name from NASA API*/
    protected String camName;
    /** rover name from NASA API*/
    protected String roverName;
    /** image filename for saving to local storage*/
    protected Bitmap bitmap;

    /**
     * Class constructor for MarsResult
     * @param imgID image ID
     * @param imgSrc image url
     * @param camName camera name
     * @param roverName rover name
     */
    public MarsResult(String imgID, String imgSrc, String camName, String roverName){
        this.imgID = imgID;
        this.imgSrc = imgSrc;
        this.camName = camName;
        this.roverName = roverName;
    }

    /**
     * imgID getter
     * @return imgID
     */
    @Override
    public String getImgID() {return imgID;}

    /**
     * imgSrc getter
     * @return imgSrc
     */
    @Override
    public String getImgSrc() {return imgSrc;}

    /**
     * camName getter
     * @return camName
     */
    @Override
    public String getCamName() {return camName;}

    /**
     * roverName getter
     * @return roverName
     */
    @Override
    public String getRoverName() {return roverName;}

    /**
     * bitmap setter
     * @param bitmap photo as bitmap
     */
    @Override
    public void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }

    /**
     * bitmap getter
     * @return bitmap
     */
    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * imgPath getter, necessary override of abstract method
     * @return null
     */
    @Override
    public String getImgPath() { return null; }
}
