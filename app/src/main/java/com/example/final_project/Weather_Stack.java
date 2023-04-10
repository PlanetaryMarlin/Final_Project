package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project.Weather.WeatherDAO;
import com.example.final_project.Weather.WeatherResult;
import com.example.final_project.Weather.Weather_Database;
import com.example.final_project.databinding.ActivityMainBinding;
import com.example.final_project.databinding.ActivityWeatherStackBinding;
import com.example.final_project.databinding.WeatherLocationResultsBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Weather_Stack extends AppCompatActivity {

    ActivityWeatherStackBinding binding;


    private WeatherResult results;

    private RecyclerView.Adapter myAdapter;
    private String cityName;
    RequestQueue queue = null;

    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        binding = ActivityWeatherStackBinding.inflate( getLayoutInflater() );
        SharedPreferences prefs = getSharedPreferences("Weather_Location", Context.MODE_PRIVATE);
        cityName = prefs.getString("Location", "");
        setContentView(binding.getRoot());

        Weather_Database db = Room.databaseBuilder(getApplicationContext(), Weather_Database.class, "weather-saves").build();
        WeatherDAO weatherDAO = db.weatherDAO();







        binding.getForecast.setOnClickListener(click -> {
            cityName = binding.cityTextField.getText().toString();
            String stringURL = null;

            try {
                stringURL = new StringBuilder()
                        .append("http://api.weatherstack.com/current?")
                        .append("access_key=cae54dfb61e45f9879c214ec35487b7a")
                        .append("&query=")
                        .append(URLEncoder.encode(cityName, "UTF-8")).toString();
            } catch (UnsupportedEncodingException e) {e.printStackTrace();}


            //this goes in the button click handler:
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            JSONObject locationObject = response.getJSONObject("location");
                            String city_name = locationObject.getString("name");
                            String country_name = locationObject.getString("country");
                            String time = locationObject.getString("localtime");
                            JSONObject temp = response.getJSONObject("current");
                            double current = temp.getDouble("temperature");
                            JSONObject description = response.getJSONObject("current");
                            double visibility = description.getDouble("visibility");
                            double feelslike = description.getDouble("feelslike");
                            int humidity = description.getInt("humidity");

                            WeatherResult results = new WeatherResult(city_name,country_name,time,current,humidity,feelslike, visibility);

                            weatherDAO.insertSave(results);

                            runOnUiThread(() -> {
                                binding.cityName.setText("City is: " + city_name);
                                binding.cityName.setVisibility(View.VISIBLE);
                                binding.countryName.setText("Country: " + country_name);
                                binding.countryName.setVisibility(View.VISIBLE);
                                binding.temp.setText("The current temperature is: " + current + "Degrees");
                                binding.temp.setVisibility(View.VISIBLE);
                                binding.time.setText("Current time is: " + time);
                                binding.time.setVisibility(View.VISIBLE);
                                binding.visibility.setText("Current Visibility: " + visibility);
                                binding.visibility.setVisibility(View.VISIBLE);
                                binding.feelslike.setText("Current Visibility: " + feelslike);
                                binding.feelslike.setVisibility(View.VISIBLE);
                                binding.humidity.setText("The humidity is " + humidity + "%");
                                binding.humidity.setVisibility(View.VISIBLE);
                                binding.icon.setImageBitmap(image);
                                binding.icon.setVisibility(View.VISIBLE);

                                //results.add(result);

                            });


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) -> {   });
            queue.add(request);

            Toast.makeText(getApplicationContext(), "Receiving Weather Information on: " + cityName, Toast.LENGTH_SHORT).show();

        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() -> {
                WeatherDAO weatherDAO = db.weatherDAO();
                    weatherDAO.insertSave(results);
                });

                Toast.makeText(getApplicationContext(), "Result saved.", Toast.LENGTH_SHORT).show();
            }

        });











    }


    }

