package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
public class MainActivity extends AppCompatActivity {
    Bitmap bitmap;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.iMageView);
        new GetImageFromUrl(image).execute(urlKitten);
        EditText widthEditText = findViewById(R.id.widthEditText);
        EditText heightEditText= findViewById(R.id.heightEditText);
        Button logInButton = findViewById(R.id.logInButton);
        logInButton.setOnClickListener(clk -> {
            String widthInput = widthEditText.getText().toString();
            String heightInput = heightEditText.getText().toString();
            String urlKitten = String.format("https://placekitten.com/%s/%s",widthInput,heightInput);

        });
    }
    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;
        public GetImageFromUrl(ImageView img){
            this.imageView = img;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}