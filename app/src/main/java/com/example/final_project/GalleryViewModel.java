package com.example.final_project;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class GalleryViewModel extends ViewModel {

    public MutableLiveData<ArrayList<KittensEntity>> backupResults = new MutableLiveData<>();
    //backupResults holds data in the database (list of kittens) when the application is rotated or application is in the background and bring it to the foreground

    public MutableLiveData<KittensEntity> selectedKittensEntity= new MutableLiveData<>();

}
