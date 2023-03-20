package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.final_project.databinding.ActivityMarsBinding;
import com.example.final_project.databinding.ActivityWeatherStackBinding;

import java.util.ArrayList;

public class Weather_Stack extends AppCompatActivity {

    ActivityWeatherStackBinding binding;
    ArrayList<String> result = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_stack);
        binding = ActivityWeatherStackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        EditText locationName = binding.locationName;
        Button search = binding.search;

        SharedPreferences prefs = getSharedPreferences("Weather_Date", Context.MODE_PRIVATE);
        String locationInput = prefs.getString("Location", "");

        search.setOnClickListener( clk -> {
            Toast toast = Toast.makeText(getApplicationContext(), "Search Button Used", Toast.LENGTH_SHORT);
            toast.show();

            result.add(binding.locationName.getText().toString());
            adapter.notifyItemInserted(result.size()-1);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Location", binding.locationName.getText().toString());
            editor.apply();
        });
    }

}