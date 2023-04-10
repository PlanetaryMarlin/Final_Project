package com.example.final_project;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.final_project.NewYorkData.ArticleItem;
import com.example.final_project.databinding.ArticleDetailsFragmentBinding;


public class ArticleDetailsFragment extends Fragment {


    ArticleItem selected;

    public ArticleDetailsFragment(ArticleItem a) {

        selected = a;


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);


        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        ArticleDetailsFragmentBinding binding = ArticleDetailsFragmentBinding.inflate(inflater);


        binding.getRoot().setBackgroundColor(Color.WHITE);
        binding.articleDetailsId.setText("Id = " + selected.id);
        binding.articleDetailsHeadline.setText("Headline : " + selected.getHeadline());
        binding.articleDetailsPubDate.setText("Publication date : " + selected.getPublication_date());
        binding.articleDetailsUrl.setText("Article url : " + selected.getArticle_url());



        return binding.getRoot();


    }


}
