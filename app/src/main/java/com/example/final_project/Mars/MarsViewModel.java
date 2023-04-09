package com.example.final_project.Mars;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MarsViewModel extends ViewModel {
    public MutableLiveData<ArrayList<MarsResult>> results = new MutableLiveData<>();
    public MutableLiveData<ArrayList<MarsFav>> favs = new MutableLiveData<>();
    public boolean isFavList;
}
