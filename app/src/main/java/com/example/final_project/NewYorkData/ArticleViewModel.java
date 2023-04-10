package com.example.final_project.NewYorkData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ArticleViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ArticleItem>> articleItems = new MutableLiveData<>();

    public MutableLiveData<ArticleItem> selectedArticleItem = new MutableLiveData< >();




}
