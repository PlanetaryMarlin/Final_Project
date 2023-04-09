package com.example.final_project.Mars;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * Class for viewmodel for MarsActivity, protects data from rotation changes
 */
public class MarsViewModel extends ViewModel {
    public MutableLiveData<ArrayList<MarsResult>> results = new MutableLiveData<>();
    public MutableLiveData<ArrayList<MarsFav>> favs = new MutableLiveData<>();
    public boolean isFavList;
}
