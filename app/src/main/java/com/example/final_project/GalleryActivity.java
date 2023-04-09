package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.final_project.databinding.ActivityGalleryBinding;
import com.example.final_project.databinding.HolderItemBinding;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class GalleryActivity extends AppCompatActivity {

    private ActivityGalleryBinding bind;

    ArrayList<KittensEntity> results=new ArrayList<KittensEntity>();

    private RecyclerView.Adapter adapter;

    private GallaryViewModel viewModel;

    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel= new ViewModelProvider(this).get(GallaryViewModel.class);

        db= Room.databaseBuilder(getApplicationContext(),MyDatabase.class,"kittens").build();
        KittensDAO kittensDAO=db.cmDAO();

        if (results == null){
            viewModel.results.setValue(results = new ArrayList<>());
        }

        Executor thread= Executors.newSingleThreadExecutor();
        thread.execute(new Runnable() {
            @Override
            public void run() {
                results.addAll(kittensDAO.getAllKittens());
                runOnUiThread(()-> bind.recyclerView.setAdapter(adapter));
            }
        });

        bind.recyclerView.setAdapter(adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                HolderItemBinding holderItemBinding= HolderItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new ResultHolder(holderItemBinding.getRoot());
            }

            @Override
            //for each row in recylcerview show height and width
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView textView=holder.itemView.findViewById(R.id.titleTv);
                textView.setText(results.get(position).height +" X "+results.get(position).width);
            }

            @Override
            public int getItemCount() {
                return results.size();
            }
        });
    }

    class ResultHolder extends RecyclerView.ViewHolder{

        TextView textView;
        Button button;

        public ResultHolder(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.titleTv);
            button=itemView.findViewById(R.id.deleteBtn);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(GalleryActivity.this);
                    builder.setMessage("Do you want to delete this Item?")
                            .setTitle("Warning")
                            .setNegativeButton("No", (dialog, which) -> {})
                            .setPositiveButton("Yes",(dialog, which) -> {
                                // TODO: 4/9/2023 implement delete item
                            })
                            .create().show();
                }
            });
        }


    }
}