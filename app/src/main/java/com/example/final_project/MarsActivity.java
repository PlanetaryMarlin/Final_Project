package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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
import com.example.final_project.Mars.*;
import com.example.final_project.databinding.ActivityMarsBinding;
import com.example.final_project.databinding.MarsResultBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/** Page for searcing NASA's Mars rover photos and displaying them in a recycler view
 * @author Ryan Wang
 * @version 1.0
 */
public class MarsActivity extends AppCompatActivity {
    /** holds variableBinding object */
    private ActivityMarsBinding binding;
    /** holds viewmodel object for this page*/
    private MarsViewModel marsModel;
    /** holds arraylist for results of JSON search*/
    private ArrayList<MarsResult> results;
    /** holds arraylist of saved photos*/
    private ArrayList<MarsFav> favs;
    /** holds adapter for recycler view*/
    private RecyclerView.Adapter adapter;
    /** holds date input for searching photos*/
    private String date;
    /** holds url for NASA API*/
    private String url;
    /** holds array position of current photo*/
    private int position;
    /** boolean for determining if recycler view shows search results or favourites*/
    boolean isFavList = false;
    /** holds database for storing favourites*/
    MarsDatabase db;

    /**
     * Toolbar menu item handling
     * @param item menu item clicked
     * @return true
     */
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
            case R.id.helpItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(MarsActivity.this);
                builder.setMessage("Enter a date into the field and click the button to search the NASA mars rover photos on that date. " +
                                "It may take a minute to fully load all images. " +
                                "Click the saved button to view your saved images.")
                        .setPositiveButton("Image list", (dialog, cl) -> {
                            builder.setMessage("Click an image in the list to get more details and save it.")
                                    .setPositiveButton(null, null)
                                    .show();
                        })
                        .setTitle("Mars Help")
                        .create().show();
        }
        return true;
    }

    /**
     * onCreate for toolbar menu
     * @param menu menu to be inflated
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * onCreate for activity
     * @param savedInstanceState Bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(), MarsDatabase.class, "mars-favs").build();
        MarsFavDAO mrDAO = db.mrDAO();

        binding = ActivityMarsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //ViewModel for rotation changes
        marsModel = new ViewModelProvider(this).get(MarsViewModel.class);
        isFavList = marsModel.isFavList;
        if(isFavList) { favs = marsModel.favs.getValue(); }
        else { results = marsModel.results.getValue(); }

        if (results == null){
            marsModel.results.setValue(results = new ArrayList<>());
        }

        EditText editTextDateNumber = binding.editTextDateNumber;
        Button searchButton = binding.searchButton;
        Button favButton = binding.favButton;
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //SharedPrefs for saving editText contents
        SharedPreferences prefs = getSharedPreferences("MarsData", Context.MODE_PRIVATE);
        int marsInput = prefs.getInt("marsDate", 0);
        editTextDateNumber.setText(String.valueOf(marsInput));

        RequestQueue queue = Volley.newRequestQueue(this);

        //Click listener for the search button
        searchButton.setOnClickListener( clk -> {
            isFavList = false;
            results = new ArrayList<>();
            adapter.notifyDataSetChanged();
            date = binding.editTextDateNumber.getText().toString();
            url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol="
                    + date
                    + "&api_key=C2gMbD7QTse7kTcfGcL5RlvbIsbBGd47ONCACTiG";

            //JSON request to NASA API
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            JSONArray photoArray = response.getJSONArray("photos");

                            //for looping through array of photos taken on date
                            for (int i=0; i < photoArray.length()-1; i++){

                                JSONObject currentResult = photoArray.getJSONObject(i);
                                JSONObject currentCamera = currentResult.getJSONObject("camera");
                                JSONObject currentRover = currentResult.getJSONObject("rover");

                                String imgID = currentResult.getString("id");
                                String imgSrc = currentResult.getString("img_src").replace("http://mars.jpl", "https://mars");
                                String camName = currentCamera.getString("name");
                                String roverName = currentRover.getString("name");
                                MarsResult result = new MarsResult(imgID, imgSrc, camName, roverName);

                                //Image request for getting photo as bitmap
                                ImageRequest imgReq = new ImageRequest(imgSrc, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        result.setBitmap(bitmap);
                                        results.add(result);
                                        adapter.notifyItemInserted(results.size()-1);
                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                        error  -> {
                                        });
                                queue.add(imgReq);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    error ->{

                    });
            queue.add(request);

            marsModel.results.postValue(results);
            marsModel.isFavList = isFavList;

            Toast.makeText(getApplicationContext(), "Searching for photos on date " + date, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("marsDate", Integer.parseInt(binding.editTextDateNumber.getText().toString()));
            editor.apply();
        });

        //Click listener for saved images button
        favButton.setOnClickListener( clk -> {
            favs = new ArrayList<>();
            adapter.notifyDataSetChanged();
            isFavList = true;
            Executor thread= Executors.newSingleThreadExecutor();
            thread.execute( () -> {
                favs.addAll(mrDAO.getAllFavs());
                runOnUiThread( () -> binding.recyclerView.setAdapter(adapter));
            });
            marsModel.favs.postValue(favs);
            marsModel.isFavList = isFavList;
        });

        binding.recyclerView.setAdapter(adapter = new RecyclerView.Adapter<ResultHolder>() {

            /**
             * onCreateViewHolder method for ResultHolder class
             * @param parent   The ViewGroup into which the new View will be added after it is bound to
             *                 an adapter position.
             * @param viewType The view type of the new View.
             * @return ResultHolder class
             */
            @NonNull
            @Override
            public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                MarsResultBinding binding = MarsResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ResultHolder(binding.getRoot());
            }

            /**
             * onBindViewHolder method for ResultHolder class
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
                if (isFavList) {
                    MarsFav obj = favs.get(position);

                    String dir = getApplicationContext().getFilesDir().getPath();
                    File imgFile = new File(dir, obj.getImgPath());
                    Bitmap img = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    obj.setBitmap(img);
                    holder.roverText.setText(obj.getRoverName());
                    holder.thumbnail.setImageBitmap(img);
                }
                else {
                    MarsResult obj = results.get(position);
                    holder.roverText.setText(obj.getRoverName());
                    holder.thumbnail.setImageBitmap(obj.getBitmap());
                }
            }

            /**
             * gets item count of arrays
             * @return size of array
             */
            @Override
            public int getItemCount() {
                int itemCount;
                if (isFavList){
                    itemCount = favs.size();
                }
                else {
                    itemCount = results.size();
                }
                return itemCount;
            }

            /**
             * gets item view type
             * @param position position to query
             * @return 0
             */
            @Override
            public int getItemViewType(int position){
                return 0;
            }
        });
    }

    /**
     * ResultHolder class extending ViewHolder for recycler view
     */
    class ResultHolder extends RecyclerView.ViewHolder {
        /** TextView for holding rover name*/
        TextView roverText;
        /** ImageView for holding photo taken*/
        ImageView thumbnail;
        public ResultHolder(View itemView){
            super(itemView);
            thumbnail = itemView.findViewById(R.id.roverImage);

            //Click listener for recyclerView items
            itemView.setOnClickListener(clk -> {
                position = getAbsoluteAdapterPosition();
                MarsDetailsFragment marsFragment;
                if (isFavList){
                    marsFragment = new MarsDetailsFragment(favs.get(position), db);
                }
                else {
                    marsFragment = new MarsDetailsFragment(results.get(position),db);
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.marsFragment, marsFragment)
                        .commit();
            });

            roverText = itemView.findViewById(R.id.roverText);
        }
    }
}