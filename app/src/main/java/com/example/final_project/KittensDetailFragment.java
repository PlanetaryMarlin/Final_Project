package com.example.final_project;

import static com.example.final_project.KittensActivity.IMAGE_DIR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.databinding.FragmentKittensDetailBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class KittensDetailFragment extends android.app.Fragment {

    private KittensEntity mKittensEntity;

    public KittensDetailFragment(KittensEntity kittensEntity) {
        // Required empty public constructor
        //passing kitensentity data from outside of the class into this constructor
        //so we can keep using it
        mKittensEntity=kittensEntity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //to render layout to screen need inflator
        FragmentKittensDetailBinding binding=FragmentKittensDetailBinding.inflate(inflater);


        binding.imageView.setImageBitmap(getBitmapFromInternalStorage());
        //set the image
        binding.heightTextView.setText(mKittensEntity.height);
        binding.widthTextView.setText(mKittensEntity.width);
        binding.dateTextView.setText(mKittensEntity.timeSaved);
        //set the text

        return binding.getRoot();
    }

    private Bitmap getBitmapFromInternalStorage(){
        try {
            ContextWrapper cw = new ContextWrapper(getActivity());
            File directory = cw.getDir(IMAGE_DIR, Context.MODE_PRIVATE);
            // Create imageDir
            File file= new File(directory,mKittensEntity.id+".jpg");
            Bitmap bitmap= BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}