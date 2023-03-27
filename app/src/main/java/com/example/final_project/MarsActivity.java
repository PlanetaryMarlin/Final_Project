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
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_project.databinding.ActivityMarsBinding;
import com.example.final_project.databinding.MarsResultBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MarsActivity extends AppCompatActivity {
    ActivityMarsBinding binding;
    ArrayList<String> results = new ArrayList<>();
    private RecyclerView.Adapter adapter;

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

        searchButton.setOnClickListener( clk -> {
            Toast toast = Toast.makeText(getApplicationContext(), "toast text", Toast.LENGTH_SHORT);
            toast.show();

            results.add(binding.editTextDateNumber.getText().toString());
            adapter.notifyItemInserted(results.size()-1);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("marsDate", Integer.parseInt(binding.editTextDateNumber.getText().toString()));
            editor.apply();
        });

        binding.recyclerView.setAdapter(adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                MarsResultBinding binding= MarsResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ResultHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

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
        TextView text;
        public ResultHolder(View itemView){
            super(itemView);

            itemView.setOnClickListener(clk -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MarsActivity.this);
                builder.setMessage("show snackbar?")
                        .setTitle("title placeholder")
                        .setNegativeButton("No", (dialog, cl) -> {})
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            Snackbar.make(text, "snackbar placeholder", Snackbar.LENGTH_LONG).show();
                        })
                        .create().show();
            });

            text = itemView.findViewById(R.id.testText);
        }
    }
}