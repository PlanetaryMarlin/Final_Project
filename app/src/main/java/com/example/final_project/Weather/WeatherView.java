package com.example.final_project.Weather;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class WeatherView extends ViewModel {
    /**
     * Contain the view
     */
    public static MutableLiveData<String> weatherView = new MutableLiveData<>();
}
