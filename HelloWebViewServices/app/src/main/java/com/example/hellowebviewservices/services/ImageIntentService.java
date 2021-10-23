package com.example.hellowebviewservices.services;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.hellowebviewservices.ImageActivity;
import com.example.hellowebviewservices.MyApplication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageIntentService extends JobIntentService {

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Uri uri=intent.getData();
        MyApplication myapp= (MyApplication) getApplication();
        try {
            URL url=new URL(uri.toString());
            Bitmap image= BitmapFactory.decodeStream(url.openStream());
            myapp.setBitmap(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent1=new Intent(this, ImageActivity.class);
        startActivity(intent1);
    }
}
