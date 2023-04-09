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

    ArrayList<String> result = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;
    protected String cityName;
    RequestQueue queue = null;

    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_stack);
        setContentView(R.layout.weather_location_results);
        binding = ActivityWeatherStackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences prefs = getSharedPreferences("Weather_Location", Context.MODE_PRIVATE);
        String locationInput = prefs.getString("Location", "");


        // Weather
        EditText locationName = binding.locationName;
        Button search = binding.searchButton;
        locationName.setText(String.valueOf(locationInput));
        queue = Volley.newRequestQueue(this);

        binding.searchButton.setOnClickListener(click -> {
            cityName = binding.locationName.getText().toString();
            String stringURL = null;
            try {
                stringURL = new StringBuilder()
                        .append ("https://api.openweathermap.org/data/2.5/weather?q=")
                        .append (URLEncoder.encode(cityName, "UTF-8"))
                        .append("&appid=a6cad38314bac12aa304fd6e5d6a7172&units=metric").toString();
            } catch (UnsupportedEncodingException e) {e.printStackTrace();}

            WeatherLocationResultsBinding binding = WeatherLocationResultsBinding.inflate(getLayoutInflater());
            //this goes in the button click handler:
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            JSONObject coord = response.getJSONObject("coord");
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);
                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");
                            JSONObject mainObject = response.getJSONObject("main");
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");


                            runOnUiThread(() -> {
                                binding.temp.setText("The current temperature is " + current);
                                binding.temp.setVisibility(View.VISIBLE);
                                binding.minTemp.setText("The min temperature is " + min);
                                binding.minTemp.setVisibility(View.VISIBLE);
                                binding.maxTemp.setText("The max temperature is " + max);
                                binding.maxTemp.setVisibility(View.VISIBLE);
                                binding.humidity.setText("The humidity is " + humidity + "%");
                                binding.humidity.setVisibility(View.VISIBLE);
                                binding.icon.setImageBitmap(image);
                                binding.icon.setVisibility(View.VISIBLE);
                                binding.description.setText(description);
                                binding.description.setVisibility(View.VISIBLE);
                            });

                            try {
                                String pathname = getFilesDir() + "/" + iconName + ".png";
                                File file = new File(pathname);
                                if (file.exists()) {
                                    image = BitmapFactory.decodeFile(pathname);
                                } else {
                                    ImageRequest imgReq = new ImageRequest("https://openweathermap.org/img/w/" +
                                            iconName + ".png", new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            try {
                                                image = bitmap;
                                                image.compress(Bitmap.CompressFormat.PNG, 100,
                                                        Weather_Stack.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                                binding.icon.setImageBitmap(image);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                                        Toast.makeText(Weather_Stack.this, "" + error, Toast.LENGTH_SHORT).show();
                                    });
                                    queue.add(imgReq);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) -> {   });

            queue.add(request);

        });








        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView listLocation;

            public MyRowHolder(@NonNull View itemView) {
                super(itemView);


            }
        }


        binding.recyclerView.setAdapter(myAdapter=new RecyclerView.Adapter<MyRowHolder>() {
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

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }










    }

