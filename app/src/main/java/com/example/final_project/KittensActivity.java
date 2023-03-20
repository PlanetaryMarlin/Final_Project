package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;


public class KittensActivity extends AppCompatActivity {
    String widthInput;
    String heightInput;
    String urlKitten = String.format("https://placekitten.com/%s/%s",widthInput,heightInput);
    ImageView image;
    Button button;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kittens);
        image = findViewById(R.id.imageView);
        EditText widthEditText = findViewById(R.id.widthEditText);
        EditText heightEditText= findViewById(R.id.heightEditText);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(clk -> {
            widthInput = widthEditText.getText().toString();
            heightInput = heightEditText.getText().toString();
            urlKitten = String.format("https://placekitten.com/%s/%s",widthInput,heightInput);
            new DownloadImage().execute(urlKitten);

        });
    }
    private class DownloadImage extends AsyncTask {
        @Override
        protected Bitmap doInBackground(Object[] URL) {
            String imageURL = (String)  URL[0];
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;        }


        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            image.setImageBitmap(result);
        }
    }
}