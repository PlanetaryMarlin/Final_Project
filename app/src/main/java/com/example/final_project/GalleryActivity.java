package com.example.final_project;

import static com.example.final_project.KittensActivity.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.final_project.databinding.ActivityGalleryBinding;
import com.example.final_project.databinding.KittensResultholderBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class GalleryActivity extends AppCompatActivity {

    private ActivityGalleryBinding bind;

    ArrayList<KittensEntity> results=new ArrayList<KittensEntity>();

    private RecyclerView.Adapter adapter;

    private GalleryViewModel viewModel;

    private MyDatabase db;
    private KittensDAO kittensDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        viewModel= new ViewModelProvider(this).get(GalleryViewModel.class);

        db= Room.databaseBuilder(getApplicationContext(),MyDatabase.class, DATABASE_NAME).build();
        kittensDAO = db.cmDAO();


        if (results == null){
            //backupResults is arraylist holds kittens entity
            //if we don't check for null, app will crash
            //nullpointer exception is runtime exception in java happens when variable is assigned when not point an object
            viewModel.backupResults.setValue(results = new ArrayList<>());
            //make a reference between class backupResults and viewModel backupResults
            //backupResults = new initizlies a new arralylist
            //backupResults holds the kittens data in viewmodel if the phone is rotated
            //need to initialize in order to use viewmodel
        }


        // this is an active observer on selectedKittensEntity
        // whenever a selectedKittensEntity is set, this block of code will be run
        //code for creating fragment
        viewModel.selectedKittensEntity.observe(this, kittensEntity -> {
            // show the detail fragment
            KittensDetailFragment fragment= new KittensDetailFragment(kittensEntity);
            getFragmentManager()
                    //can't make fragment without fragmentmanager
                    .beginTransaction()
                    .add(R.id.detailsLocation,fragment)
                    //add for one fragment, if want to use fragment over the other use replace to substitute
                    //add for stacking
                    .commit();
                    //is like build
        });


        Executor thread= Executors.newSingleThreadExecutor();
        //we need this thread because when we use the database it executes on a nother thread
        thread.execute(new Runnable() {
            @Override
            public void run() {
                results.addAll(kittensDAO.getAllKittens());
                runOnUiThread(()-> bind.recyclerview.setAdapter(adapter));
            }
        });

        bind.recyclerview.setAdapter(adapter = new RecyclerView.Adapter() {

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //assigns the layout of each row in the recyclerview to the adaptor
                //adaptor is responsible to render each row to UI
                KittensResultholderBinding holderItemBinding= KittensResultholderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new ResultHolder(holderItemBinding.getRoot());
            }

            /**
             *
             * @param holder The ViewHolder which should be updated to represent the contents of the
             *        item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            //for each row in recylcerview show height and width
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                //onBindViewHolder is responsible for setting UI elements of each row
                //int position is the nubmer of the row
                //holder is the ResultHolder
                //when the button is pressed this is called first,results is the list of kittens entity
                //get height and width from results
                TextView textView=holder.itemView.findViewById(R.id.rowHolderText);
                textView.setText(results.get(position).height +" X "+results.get(position).width);
            }

            @Override
            public int getItemCount() {
                //adpator needs to know number of items in the recycler view so return
                return results.size();
            }
        });
    }

    class ResultHolder extends RecyclerView.ViewHolder{
        //resultholder needs another class, have to create customzed and extend RecyclerView.ViewHolder

        TextView textView;
        Button button;

        /**
         *
         * @param itemView
         */

        public ResultHolder(@NonNull View itemView) {
            super(itemView);
            //result constructor creates a resultholder object
            //itemview is each row in the recyclerview, layout is kittens_resultholder
            //setOnClickListener has to be here, because ResultHolder is responsible for all UI functions of each row

            textView=itemView.findViewById(R.id.rowHolderText);
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

                                Executor thread=Executors.newSingleThreadExecutor();
                                thread.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        // remove item from database
                                        KittensEntity selectedRow=results.get(getAdapterPosition());
                                        //getAdaptorPosition returns integer of clicked item in the recylcler view
                                        //arraylist gets theat position
                                        kittensDAO.deleteKitten(selectedRow);
                                        //whenever we want to work with database use kittensdao

                                        // remove row from recyclerView
                                        results.remove(selectedRow);
                                        runOnUiThread(()->{adapter.notifyItemRemoved(getAdapterPosition());});
                                        //whenever you add or delete notify adaptor

                                        // remove image from local storage
                                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                                        // path to /data/data/yourapp/app_data/imageDir
                                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                        File file=new File(directory,selectedRow.id+".jpg");
                                        file.delete();
                                    }
                                });
                            })
                            .create().show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                //when user clicks on each row in the recyclerview
                //change the
                @Override
                public void onClick(View v) {
                    viewModel.selectedKittensEntity.setValue(results.get(getAdapterPosition()));
                    //set the selectedKittensEntity to fields that the user clicked

                }
            });
        }


    }
}