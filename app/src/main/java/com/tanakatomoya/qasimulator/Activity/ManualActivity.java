package com.tanakatomoya.qasimulator.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tanakatomoya.qasimulator.Activity.CreateModelActivity;
import com.tanakatomoya.qasimulator.R;

public class ManualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual);
        WebView myWebView = findViewById(R.id.manualView);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl(CreateModelActivity.BASE_URL + "qa_simulator/manual/");
    }
}
