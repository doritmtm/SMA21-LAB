package com.example.hellowebviewservices;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.hellowebviewservices.services.ImageIntentService;

public class WebsearchActivity extends AppCompatActivity {
    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview=findViewById(R.id.webview);
        webview.loadUrl("https://google.com/imghp");
        WebSettings webSettings=webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void clicked(View view)
    {
        ClipboardManager clipboard=getSystemService(ClipboardManager.class);
        ClipData clip=clipboard.getPrimaryClip();
        ClipData.Item item=clip.getItemAt(0);
        String url=item.getText().toString();
        if(!url.contains("https://images.app.goo.gl/"))
        {
            Toast.makeText(getApplicationContext(),"Invalid link. Google images link required!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent;
            switch(view.getId())
            {
                case R.id.butBackground:
                    intent=new Intent(this, ImageIntentService.class);
                    intent.putExtra(Intent.EXTRA_TEXT,url);
                    startService(intent);
                    break;
                case R.id.butForeground:
                    intent=new Intent(this, ImageIntentService.class);
                    intent.putExtra(Intent.EXTRA_TEXT,url);
                    intent.setAction("myact.startforegroundsvc");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    }
                    else
                    {
                        startService(intent);
                    }
                    break;
            }
        }
    }
}