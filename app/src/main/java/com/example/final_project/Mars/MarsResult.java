package com.example.final_project.Mars;
public class MarsResult {
    protected String imgSrc;
    protected String camName;
    protected String roverName;

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
}
