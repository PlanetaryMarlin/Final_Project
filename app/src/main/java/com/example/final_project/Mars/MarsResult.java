package com.example.final_project.Mars;

import android.graphics.Bitmap;

public class MarsResult {
    protected String imgSrc;
    protected String camName;
    protected String roverName;
    protected Bitmap bitmap;

    public MarsResult(String imgSrc, String camName, String roverName){
        this.imgSrc = imgSrc;
        this.camName = camName;
        this.roverName = roverName;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getCamName() {
        return camName;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
