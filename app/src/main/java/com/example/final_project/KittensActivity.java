package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.final_project.databinding.ActivityKittensBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class KittensActivity extends AppCompatActivity {
    String widthInput;
    String heightInput;
    String urlKitten = String.format("https://placekitten.com/%s/%s", widthInput, heightInput);
    ImageView image;
    SharedPreferences prefs;
    //SharedPreferences is telling the application that this is default and to save it, such that it's there you open it again
    Button button;
    ProgressDialog ProgressDialog;
    ActivityKittensBinding bind;

    KittensEntity entity=new KittensEntity();

    private KittensDAO mDAO;
    private static final String SHARED_PREF_NAME = "MyData";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_WIDTH = "width";

    //these are private final because they can only be accessed by this class and won't change
    //The binding class name is Activity + Classname, the main classname here would need Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityKittensBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        //need these two lines whenever we use binding
        //image = findViewById(R.id.imageView);
//        EditText heightEditText= findViewById(R.id.heightEditText);
//        Button button = findViewById(R.id.button);
        //no longer need to declare variables
        prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        //getSharedPreferences is an android method that looks for the sharedpreferences with the name given in the parameters
        //assigns mode to it, in this case private
        //all our SharedPreferences is stored in Android, and right now we just want those two to be stored in prefs

        String width = prefs.getString("width", null);
        String height = prefs.getString("height", null);
        bind.widthEditText.setText(width); // using String variable name
        bind.heightEditText.setText(height);

        bind.downloadImage.setOnClickListener(clk -> {
            widthInput = bind.widthEditText.getText().toString();
            //storing the content of the component
            //getText gets the text from input, toString converts text to String
            heightInput = bind.heightEditText.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            //declaring our editor
            //the editor edits the shared preferencces
            editor.putString(KEY_HEIGHT, heightInput);
            editor.putString(KEY_WIDTH, widthInput);
            //getting the editEmail that was already set to the email user inputed
            //editor.putString(yao@hotmail.com) that stores the email I put as a shared preference so that SecondActivity can access
            //Then in SecondActivity we will get the value from our previous activity from our shared preferences
            //only putString is related to SharedPreferencews
            editor.apply();
            //need to call apply to save putString
            urlKitten = String.format("https://placekitten.com/%s/%s", widthInput, heightInput);

            //inside prefs look for variable KEY_LOGIN which holds string value "email" in our case
            //value of variable email, which is the email that user inputted and storing it in email
            new DownloadImage().execute(urlKitten);
            Toast.makeText(getApplicationContext(), "The width = " + widthInput + " and height = " + heightInput + " .", Toast.LENGTH_LONG).show();


        });
        bind.saveFavourites.setOnClickListener(clk -> {

            widthInput = bind.widthEditText.getText().toString();
            //storing the content of the component
            heightInput = bind.heightEditText.getText().toString();
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
                            new SaveImageLocalStorage(getApplicationContext()).execute(entity);

                        }
                    })
                    .setNegativeButton("No", null)
                    .show();



        });
    }

    private void saveToDatabase() {

        // Open Database
        MyDatabase db = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "database-name").build();
        //create database instance, db is object of message database
        mDAO = db.cmDAO();
        //use mDAO to insert
        //use db to call cmDAO() method this will access kittens table, returns a ChatMessageDAO object that allows you to use CRUD on database

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            //entity is object of KittensEntity and height,width etc are attributes
            //taking inputs and saving in entity object
            entity.height=heightInput;
            entity.width=widthInput;
            entity.timeSaved=getDateTime();
            entity.imageUrl=urlKitten;
            mDAO.insertMessage(entity);
        });

    }

    private String getDateTime(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date=new Date();
        return dateFormat.format(date);
    }

    private class DownloadImage extends AsyncTask {
        @Override
        protected Bitmap doInBackground(Object[] URL) {
            String imageURL = (String) URL[0];
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            image.setImageBitmap(result);
        }
    }






    class SaveImageLocalStorage extends AsyncTask<KittensEntity, Void, Void> {
        //doesn't have access to import because nested
        private final Context mContext;
        //have to make context final because nested

        public SaveImageLocalStorage(final Context context)
        {
            mContext = context;
        }

        protected Void doInBackground(KittensEntity... kittensEntities) {
            try {

                // download image
                URL url = new URL(kittensEntities[0].imageUrl);
                Bitmap bitmapImage= BitmapFactory.decodeStream(url.openConnection().getInputStream());

                saveToInternalStorage(bitmapImage,kittensEntities[0]);

            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            // TODO: 3/27/2023 add snakbar
            Toast.makeText(mContext, "Image saved!", Toast.LENGTH_SHORT).show();
            //toast unlike Snackbar can take final

            View parentLayout=findViewById(android.R.id.content);
            //parentLayout is the parent layout of all activity in this application
            //can't pass the context because final so must pass parentLayout
            Snackbar.make(parentLayout,"Image saved!",Snackbar.LENGTH_LONG)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();
        }

        private void saveToInternalStorage(Bitmap bitmapImage, KittensEntity model) {

            ContextWrapper cw = new ContextWrapper(mContext);
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath = new File(directory, model.id + ".jpg");

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
    }
}

