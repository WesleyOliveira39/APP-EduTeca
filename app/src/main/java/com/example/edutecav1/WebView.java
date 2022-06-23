package com.example.edutecav1;

import android.os.Bundle;
import android.webkit.WebViewClient;

import com.example.edutecav1.databinding.ActivityWebViewBinding;

public class WebView extends Base {


    private static String endereco;

    ActivityWebViewBinding activityWebViewBinding;
    private android.webkit.WebView myWebView;

    public static void setEndereco(String endereco) {
        WebView.endereco = endereco;
    }

    @Override
    public void onBackPressed(){
        if(myWebView.canGoBack()){
            myWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWebViewBinding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(activityWebViewBinding.getRoot());
        alocarTitulo("Web");


        myWebView = findViewById(R.id.webViewUrl);
        myWebView.loadUrl(endereco);

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
    }
}