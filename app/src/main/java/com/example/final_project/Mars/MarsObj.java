package com.example.final_project.Mars;

import android.graphics.Bitmap;

public abstract class MarsObj {
//    protected String imgID;
//    protected String imgSrc;
//    protected String camName;
//    protected String roverName;
//
//
    public abstract String getImgID();
    public abstract String getImgSrc();
    public abstract String getCamName();
    public abstract String getRoverName();
    public abstract void setBitmap(Bitmap bitmap);
    public abstract Bitmap getBitmap();
    public abstract String getImgPath();
}
