package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.final_project.databinding.ActivityGalleryBinding;
import com.example.final_project.databinding.ActivityKittensBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Page for downloading Kittens image from URL using Volley
 * @author
 */

 public class KittensActivity extends AppCompatActivity {
    public static final String IMAGE_DIR = "imageDir";
    String widthInput;
    String heightInput;
    String urlKitten = String.format("https://placekitten.com/%s/%s", widthInput, heightInput);
    ImageView image;
    SharedPreferences prefs;
    //SharedPreferences is telling the application that this is default and to save it, such that it's there you open it again
    Button button;
    ProgressDialog ProgressDialog;
    ActivityKittensBinding binding;

    KittensEntity entity = new KittensEntity();

    private KittensDAO mDAO;
    private Bitmap bitmap;
    private static final String SHARED_PREF_NAME = "MyData";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_WIDTH = "width";
    private RecyclerView.Adapter adapter;

    public static final String DATABASE_NAME = "application-database";
    private long insertedEntityId;
    //database name when we use ROOM database

    //these are private final because they can only be accessed by this class and won't change
    //The binding class name is Activity + Classname, the main classname here would need Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKittensBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //need these two lines whenever we use binding
        //image = findViewById(R.id.imageView);
//        EditText heightEditText= findViewById(R.id.heightEditText);
//        Button button = findViewById(R.id.button);
        //no longer need to declare variables
        prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        //getSharedPreferences is an android method that looks for the sharedpreferences with the name given in the parameters
        //assigns mode to it, in this case private
        //all our SharedPreferences is stored in Android, and right now we just want those two to be stored in prefs

        String width = prefs.getString(KEY_WIDTH, null);
        String height = prefs.getString(KEY_HEIGHT, null);
        binding.widthEditText.setText(width); // using String variable name
        binding.heightEditText.setText(height);
        //reading data from sharedpref and putting it into the edittext

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        RequestQueue queue = Volley.newRequestQueue(this);

        binding.downloadImage.setOnClickListener(clk -> {
            widthInput = binding.widthEditText.getText().toString();
            //storing the content of the component
            //getText gets the text from input, toString converts text to String
            heightInput = binding.heightEditText.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            //declaring our editor
            //the editor edits the shared preferencces
            editor.putString(KEY_HEIGHT, heightInput);
            editor.putString(KEY_WIDTH, widthInput);
            //writing data to sharedprefs
            //getting the editEmail that was already set to the email user inputed
            //editor.putString(yao@hotmail.com) that stores the email I put as a shared preference so that SecondActivity can access
            //Then in SecondActivity we will get the value from our previous activity from our shared preferences
            //only putString is related to SharedPreferencews
            editor.apply();
            //need to call apply to save putString
            urlKitten = String.format("https://placekitten.com/%s/%s", widthInput, heightInput);
            ImageRequest imgReq = new ImageRequest(urlKitten, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap mybitmap) {
                    bitmap = mybitmap;
                    binding.imageView.setImageBitmap(bitmap);

                }
            }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                    error -> {
                    });
            queue.add(imgReq);

            //inside prefs look for variable KEY_LOGIN which holds string value "email" in our case
            //value of variable email, which is the email that user inputted and storing it in email
            //new DownloadImage().execute(urlKitten);
            Toast.makeText(getApplicationContext(), "The width = " + widthInput + " and height = " + heightInput + " .", Toast.LENGTH_LONG).show();


        });
        binding.saveFavourites.setOnClickListener(clk -> {

            widthInput = binding.widthEditText.getText().toString();
            //storing the content of the component
            heightInput = binding.heightEditText.getText().toString();
            urlKitten = String.format("https://placekitten.com/%s/%s", widthInput, heightInput);


            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Do you want to save this image?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            // Save data in the database
                            saveToDatabase();


                            // Save Image to local storage
                            //new SaveImageLocalStorage(getApplicationContext()).execute(entity);
                            saveToInternalStorage(bitmap, entity);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();


        });
        ActivityGalleryBinding gallerybinding;

        gallerybinding = ActivityGalleryBinding.inflate(getLayoutInflater());

//        gallerybinding.recyclerview.setAdapter(adapter = new RecyclerView.Adapter<GalleryActivity.ResultHolder>() {
//
//            /**
//             * onCreateViewHolder method for ResultHolder class
//             *
//             * @param parent   The ViewGroup into which the new View will be added after it is bound to
//             *                 an adapter position.
//             * @param viewType The view type of the new View.
//             * @return ResultHolder class
//             */
//            @NonNull
//            @Override
//            public GalleryActivity.ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                KittensResultBinding binding = KittensResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//                return new ResultHolder(binding.getRoot());
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull GalleryActivity.ResultHolder holder, int position) {
//
//            }
//
//            @Override
//            public int getItemCount() {
//                return 0;
//            }
//        });
    }

    private void saveToDatabase() {
        //when we open ROOM database, we need to give it a name

        // Open Database
        MyDatabase db = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, DATABASE_NAME).build();
        //create database instance, db is object of message database
        mDAO = db.cmDAO();
        //use mDAO to insert
        //use db to call cmDAO() method this will access kittens table, returns a ChatMessageDAO object that allows you to use CRUD on database

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            //entity is object of KittensEntity and height,width etc are attributes
            //taking inputs and saving in entity object
            entity.height = heightInput;
            entity.width = widthInput;
            entity.timeSaved = getDateTime();
            entity.imageUrl = urlKitten;
            insertedEntityId = mDAO.insertKitten(entity);
        });

    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    private void saveToInternalStorage(Bitmap bitmapImage, KittensEntity model) {

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(IMAGE_DIR, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, insertedEntityId + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Menu for KittensActivity
     *
     * @param item The menu item that was selected.
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.kittensItem:
                Toast menuToast = Toast.makeText(getApplicationContext(), "Already in Kittens activity!", Toast.LENGTH_SHORT);
                menuToast.show();
                break;
            case R.id.galleryItem:
                Intent nextPage = new Intent(KittensActivity.this, GalleryActivity.class);
                startActivity(nextPage);
                break;
            case R.id.helpItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(KittensActivity.this);
                builder.setMessage("In this activity you can search for kittens images of specified dimensions and save it if you want!")
                        .setPositiveButton("OK", (dialog, cl) -> {
                        })
                        .setTitle("Kittens Help")
                        .create().show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}



