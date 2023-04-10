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


    private ArrayList<WeatherResult> results;

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
        final String[] cityName = {prefs.getString("Location", "")};
        setContentView(binding.getRoot());

        Weather_Database db = Room.databaseBuilder(getApplicationContext(), Weather_Database.class, "weather-saves").build();
        WeatherDAO weatherDAO = db.weatherDAO();







        binding.getForecast.setOnClickListener(click -> {
            cityName[0] = binding.cityTextField.getText().toString();
            String stringURL = null;
            results = new ArrayList<>();
            try {
                stringURL = new StringBuilder()
                        .append ("https://api.openweathermap.org/data/2.5/weather?q=")
                        .append (URLEncoder.encode(cityName[0], "UTF-8"))
                        .append("&appid=a6cad38314bac12aa304fd6e5d6a7172&units=metric").toString();
            } catch (UnsupportedEncodingException e) {e.printStackTrace();}


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
                            int id = response.getInt("id");
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");
                            WeatherResult result = new WeatherResult(id,current,min,max,humidity,iconName,description);


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
                                                results.add(result);

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

            Toast.makeText(getApplicationContext(), "Receiving Weather Information on: " + cityName[0], Toast.LENGTH_SHORT).show();

        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() -> {
                WeatherDAO weatherDAO = db.weatherDAO();
                weatherDAO.insertSave(results);

                    weatherDAO.insertSave(results);
                });

                Toast.makeText(getApplicationContext(), "Result saved.", Toast.LENGTH_SHORT).show();
            }

        });











    }


    }

