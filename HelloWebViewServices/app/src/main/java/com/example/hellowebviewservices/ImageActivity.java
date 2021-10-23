package com.example.hellowebviewservices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {
    private ImageView imageGoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageGoogle=findViewById(R.id.imageGoogle);
        MyApplication myapp= (MyApplication) getApplication();
        imageGoogle.setImageBitmap(myapp.getBitmap());
    }
}