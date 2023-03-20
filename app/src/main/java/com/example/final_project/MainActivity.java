package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.final_project.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
public class MainActivity extends AppCompatActivity {
    Bitmap bitmap;
    ImageView image;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Mars button
        Button marsButton = binding.marsButton;
        marsButton.setOnClickListener( marsClk -> {
            Intent nextPage = new Intent(MainActivity.this, MarsActivity.class);
            startActivity(nextPage);
        });


    }
}