package com.example.final_project.Mars;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * Class for viewmodel for MarsActivity, protects data from rotation changes
 */
public class MarsViewModel extends ViewModel {
    /** ArrayList to hold search results */
    public MutableLiveData<ArrayList<MarsResult>> results = new MutableLiveData<>();
    /** ArrayList to hold saved photos*/
    public MutableLiveData<ArrayList<MarsResult>> favs = new MutableLiveData<>();
    /** boolean to determine if saved list should be displayed*/
    public boolean isFavList;
}
