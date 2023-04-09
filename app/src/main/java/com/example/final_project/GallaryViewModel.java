package com.example.final_project;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class GallaryViewModel extends ViewModel {

    public MutableLiveData<ArrayList<KittensEntity>> results = new MutableLiveData<>();

}
