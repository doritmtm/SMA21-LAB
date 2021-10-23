package com.example.hellowebviewservices.services;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.hellowebviewservices.ImageActivity;
import com.example.hellowebviewservices.MyApplication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImageThread extends Thread{
    private Intent intent;
    private MyApplication myapp;

    public DownloadImageThread(Intent intent, MyApplication myApplication) {
        this.intent = intent;
        this.myapp = myApplication;
    }

    @Override
    public void run() {
        super.run();
        Uri uri=intent.getData();
        try {
            URL url=new URL(uri.toString());
            Bitmap image= BitmapFactory.decodeStream(url.openStream());
            myapp.setBitmap(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent1=new Intent(myapp, ForegroundImageService.class);
        intent1.setAction("myact.stopforegroundsvc");
        myapp.startService(intent1);
    }
}
