package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project.Mars.*;
import com.example.final_project.databinding.ActivityMarsBinding;
import com.example.final_project.databinding.MarsResultBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MarsActivity extends AppCompatActivity {
    ActivityMarsBinding binding;
    ArrayList<MarsResult> results = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private String date;
    private String url;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.marsItem:
                Toast menuToast = Toast.makeText(getApplicationContext(), "Already in Mars", Toast.LENGTH_SHORT);
                menuToast.show();
                break;
            case R.id.weatherItem:
                Intent nextPage = new Intent(MarsActivity.this, Weather_Stack.class);
                startActivity(nextPage);
                break;
            case R.id.kittensItem:
                nextPage = new Intent(MarsActivity.this, KittensActivity.class);
                startActivity(nextPage);
                break;
            case R.id.NYItem:
//                nextPage = new Intent(MarsActivity.this,)
//                startActivity(nextPage);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMarsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        EditText editTextDateNumber = binding.editTextDateNumber;
        Button searchButton = binding.searchButton;
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences prefs = getSharedPreferences("MarsData", Context.MODE_PRIVATE);
        int marsInput = prefs.getInt("marsDate", 0);
        editTextDateNumber.setText(String.valueOf(marsInput));

        RequestQueue queue = Volley.newRequestQueue(this);
        searchButton.setOnClickListener( clk -> {
                date = binding.editTextDateNumber.getText().toString();
                url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol="
                        + date
                        + "&api_key=C2gMbD7QTse7kTcfGcL5RlvbIsbBGd47ONCACTiG";
            System.out.println(url);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            JSONArray photoArray = response.getJSONArray("photos");

                            for (int i=0; i < photoArray.length()-1; i++){
                                JSONObject currentResult = photoArray.getJSONObject(i);
                                JSONObject currentCamera = currentResult.getJSONObject("camera");
                                JSONObject currentRover = currentResult.getJSONObject("rover");

                                String imgSrc = currentResult.getString("img_src");
                                String camName = currentCamera.getString("name");
                                String roverName = currentRover.getString("name");

                                MarsResult result = new MarsResult(imgSrc, camName, roverName);
                                results.add(result);
                                adapter.notifyItemInserted(results.size()-1);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    error ->{

                    });
            queue.add(request);
//            Toast toast = Toast.makeText(getApplicationContext(), "toast text", Toast.LENGTH_SHORT);
//            toast.show();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("marsDate", Integer.parseInt(binding.editTextDateNumber.getText().toString()));
            editor.apply();
        });

        binding.recyclerView.setAdapter(adapter = new RecyclerView.Adapter<ResultHolder>() {
            @NonNull
            @Override
            public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                MarsResultBinding binding = MarsResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ResultHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
                MarsResult obj = results.get(position);
                holder.roverText.setText(obj.getRoverName());
                try {
                    URL imageURL = new URL(obj.getImgSrc());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                holder.thumbnail.set
            }

            @Override
            public int getItemCount() {
                return results.size();
            }

            @Override
            public int getItemViewType(int position){
                return 0;
            }
        });
    }

    class ResultHolder extends RecyclerView.ViewHolder {
        TextView roverText;
        ImageView thumbnail;
        public ResultHolder(View itemView){
            super(itemView);

            itemView.setOnClickListener(clk -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MarsActivity.this);
                builder.setMessage("show snackbar?")
                        .setTitle("title placeholder")
                        .setNegativeButton("No", (dialog, cl) -> {})
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            Snackbar.make(roverText, "snackbar placeholder", Snackbar.LENGTH_LONG).show();
                        })
                        .create().show();
            });

            roverText = itemView.findViewById(R.id.roverText);
        }
    }
}