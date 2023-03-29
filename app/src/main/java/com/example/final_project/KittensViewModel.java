package com.example.final_project;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class KittensViewModel extends ViewModel {
    public MutableLiveData<ArrayList<KittensEntity>> messages = new MutableLiveData<>();
    public MutableLiveData<KittensEntity> selectedMessage = new MutableLiveData< >();
    //contains value set data into it take data out of it
    //container of chat messages
    //use it to update the view into showing the fragment when user clicks on recycler view


}
