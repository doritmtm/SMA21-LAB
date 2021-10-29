package com.example.hellowebviewservices.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.example.hellowebviewservices.ImageActivity;
import com.example.hellowebviewservices.MyApplication;
import com.example.hellowebviewservices.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageIntentService extends IntentService {

    public ImageIntentService() {
        super("NAMETHREAD");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ImageIntentService(String name) {
        super(name);
    }

    //@Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d("MYAPPPPPP","I do something!");
        Uri uri=Uri.parse(intent.getStringExtra(Intent.EXTRA_TEXT));
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
        Intent intent1=new Intent(myapp, ImageActivity.class);
        myapp.startActivity(intent1);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("MYAPPPPPP","I do something!");
        Uri uri=Uri.parse(intent.getStringExtra(Intent.EXTRA_TEXT));
        MyApplication myapp= (MyApplication) getApplication();
        try {
            URL url=new URL(uri.toString());
            Log.d("MYAPPPPPP",url.toString());
            Bitmap image= BitmapFactory.decodeStream(url.openStream());
            image=BitmapFactory.decodeResource(myapp.getResources(),R.drawable.planetviewkaldr);
            myapp.setBitmap(image);
            Log.d("MYAPPPPPP",image.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent1=new Intent(myapp, ImageActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myapp.startActivity(intent1);
    }
}
