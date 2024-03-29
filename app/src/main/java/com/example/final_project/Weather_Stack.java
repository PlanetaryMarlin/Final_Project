    package com.example.final_project;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;

    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import androidx.room.Room;

    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.graphics.Bitmap;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;
    import android.widget.Toast;
    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.toolbox.JsonObjectRequest;
    import com.android.volley.toolbox.Volley;
    import com.example.final_project.Weather.WeatherDAO;
    import com.example.final_project.Weather.WeatherResult;
    import com.example.final_project.Weather.WeatherView;
    import com.example.final_project.Weather.Weather_Database;
    import com.example.final_project.databinding.ActivityWeatherStackBinding;
    import com.example.final_project.databinding.WeatherLocationResultsBinding;
    import com.google.android.material.snackbar.Snackbar;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.UnsupportedEncodingException;
    import java.net.URLEncoder;

    import java.text.BreakIterator;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.concurrent.Executor;
    import java.util.concurrent.Executors;

    public class Weather_Stack extends AppCompatActivity {

        ActivityWeatherStackBinding binding;

        private ArrayList<WeatherResult> results;

        private String weatherView;

        private RecyclerView.Adapter myAdapter;
        private String cityName;
        RequestQueue queue = null;

        Bitmap image;

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        /**
         *
         * @param item whcih contain the menu
         * @return
         */
        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            super.onOptionsItemSelected(item);
            switch (item.getItemId()){
                case R.id.weatherItem:
                    Toast menuToast = Toast.makeText(getApplicationContext(), "Currently In Weather.", Toast.LENGTH_SHORT);
                    menuToast.show();
                    break;
                case R.id.marsItem:
                    Intent nextPage = new Intent(Weather_Stack.this, MarsActivity.class);
                    startActivity(nextPage);
                    break;
                case R.id.kittensItem:
                    nextPage = new Intent(Weather_Stack.this, KittensActivity.class);
                    startActivity(nextPage);
                    break;
                case R.id.NYItem:
    //                Intent nextPage = new Intent(Weather_Stack.this,)
    //                startActivity(nextPage);
                    break;
                case R.id.helpItem:
                    AlertDialog.Builder builder = new AlertDialog.Builder(Weather_Stack.this);
                    builder.setMessage("Enter the location, city, that you want to see the weather of" +
                                    "Click the saved button to store the result in the database")
                            .setPositiveButton("Understood", (dialog, cl) -> {
                                Snackbar.make(binding.getRoot(), "Have Fun!", Snackbar.LENGTH_LONG).show();

                            })
                            .setTitle("Weather Help")
                            .create().show();
            }
            return true;
        }




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            queue = Volley.newRequestQueue(this);
            binding = ActivityWeatherStackBinding.inflate( getLayoutInflater());
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);


            SharedPreferences prefs = getSharedPreferences("Weather_Location", Context.MODE_PRIVATE);
            binding.cityTextField.setText(prefs.getString("Location", ""));

            setContentView(binding.getRoot());

            //Create Database
            Weather_Database db = Room.databaseBuilder(getApplicationContext(), Weather_Database.class, "weather-saves").build();


            /**
             * The Get Forecast button which will start the search on the weather site
             */

            binding.getForecast.setOnClickListener(click -> {
                cityName = binding.cityTextField.getText().toString();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Location" , cityName).apply();
                String stringURL = null;

                try {
                    stringURL = new StringBuilder()
                            .append("http://api.weatherstack.com/current?")
                            .append("access_key=cae54dfb61e45f9879c214ec35487b7a")
                            .append("&query=")
                            .append(URLEncoder.encode(cityName, "UTF-8")).toString();
                } catch (UnsupportedEncodingException e) {e.printStackTrace();}


                /**
                 * This is where we get the inofrmation from webiste, by using an api key and reading json.
                 */
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


                                });


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        (error) -> {   });
                queue.add(request);


                Toast.makeText(getApplicationContext(), "Receiving Weather Information on: " + cityName, Toast.LENGTH_SHORT).show();

            });

            /**
             * @param dity;
             */
            class MyRowHolder extends RecyclerView.ViewHolder {
                public BreakIterator weatherResult;
                TextView city;
                public MyRowHolder(@NonNull View itemView) {
                    super(itemView);
                    city = itemView.findViewById(R.id.cityData);

                }
            }




            /**
             * Contain RecyclerView
             */

            binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
                private List<WeatherResult> weatherList = new ArrayList<>();

                @NonNull
                @Override
                //It represents a single row in the list
                //MyRowHolder is an object for representing everything that goes on a row in the list.
                public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    WeatherLocationResultsBinding binding = WeatherLocationResultsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

                    return new MyRowHolder(binding.getRoot());
                }
                /**
                 *
                 * @param holder   The ViewHolder which should be updated to represent the contents of the
                 *                 item at the given position in the data set.
                 * @param position The position of the item within the adapter's data set.
                 */
                @Override
                public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                    WeatherResult weatherResult = weatherList.get(position);
                    holder.weatherResult.setText("");
                }

                /**
                 *
                 * @return nothing
                 */
                @Override
                public int getItemCount() {
                    return 0;
                }
            });
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));









        }


        }

