package com.example.final_project.Mars;

import android.graphics.Bitmap;

public class MarsResult extends MarsObj {
    protected String imgID;
    protected String imgSrc;
    protected String camName;
    protected String roverName;
    protected Bitmap bitmap;

    public MarsResult(String imgID, String imgSrc, String camName, String roverName){
        this.imgID = imgID;
        this.imgSrc = imgSrc;
        this.camName = camName;
        this.roverName = roverName;
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
    public void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public String getImgPath() { return null; }
}
