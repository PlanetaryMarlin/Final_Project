package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_project.databinding.ActivityMarsBinding;
import com.example.final_project.databinding.ActivityWeatherStackBinding;
import com.example.final_project.databinding.WeatherLocationResultsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Weather_Stack extends AppCompatActivity {

    ActivityWeatherStackBinding binding;
    ArrayList<String> result = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_stack);
        binding = ActivityWeatherStackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        EditText locationName = binding.locationName;
        Button search = binding.search;

        SharedPreferences prefs = getSharedPreferences("Weather_Location", Context.MODE_PRIVATE);
        String locationInput = prefs.getString("Location", "");
        locationName.setText(String.valueOf(locationInput));

        search.setOnClickListener( clk -> {
            Toast toast = Toast.makeText(getApplicationContext(), "Search Button Used", Toast.LENGTH_SHORT);
            toast.show();

            result.add(binding.locationName.getText().toString());
            myAdapter.notifyItemInserted(result.size()-1);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Location", binding.locationName.getText().toString());
            editor.apply();
        });



        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView listLocation;

            public MyRowHolder(@NonNull View itemView) {
                super(itemView);

                itemView.setOnClickListener(clk -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Weather_Stack.this);
                    builder.setMessage("Do you want to delete the message: " + listLocation.getText());
                    builder.setTitle("Add Same Location:");
                    builder.setNegativeButton("No", (dialog, cl) -> {});
                    builder.setPositiveButton("Yes", (dialog, cl) -> {
                        Snackbar.make(listLocation, "Added ", Snackbar.LENGTH_LONG)
                                .setAction("Undo", click->{
                                })
                                .show();


                    }).create().show();
                });

                listLocation = itemView.findViewById(R.id.listLocation);

            }
        }


        binding.recycle.setAdapter(myAdapter=new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            //It represents a single row in the list
            //MyRowHolder is an object for representing everything that goes on a row in the list.
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                WeatherLocationResultsBinding binding = WeatherLocationResultsBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            //What are the text view set to.
            //This initializes a ViewHolder to go at the row specified by the position parameter.
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return result.size();
            }

            public int getItemViewType(int position){
                return 0;
            }
        });

        binding.recycle.setLayoutManager(new LinearLayoutManager(this));


    }










    }

