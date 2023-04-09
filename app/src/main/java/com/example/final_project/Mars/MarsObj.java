package com.example.final_project.Mars;

import android.graphics.Bitmap;

/**
 * Abstract parent class for MarsResult and MarsFav, exists to cast between MarsFav, MarsResult
 */
public abstract class MarsObj {
    public abstract String getImgID();
    public abstract String getImgSrc();
    public abstract String getCamName();
    public abstract String getRoverName();
    public abstract void setBitmap(Bitmap bitmap);
    public abstract Bitmap getBitmap();
    public abstract String getImgPath();
}
