package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.example.final_project.databinding.ActivityMarsBinding;

public class MarsActivity extends AppCompatActivity {
    ActivityMarsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMarsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText editTextDateNumber = binding.editTextDateNumber;

        SharedPreferences prefs = getSharedPreferences("MarsData", Context.MODE_PRIVATE);
        int marsInput = prefs.getInt("marsDate", 0);
        editTextDateNumber.setText(String.valueOf(marsInput));

    }
}