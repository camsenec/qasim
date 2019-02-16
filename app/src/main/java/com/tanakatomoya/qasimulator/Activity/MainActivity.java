package com.tanakatomoya.qasimulator.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tanakatomoya.qasimulator.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void onClickStart(View view){
        Intent intent = new Intent(MainActivity.this, CreateFieldActivity.class);
        startActivity(intent);
    }

    public void onClickManual(View view){
        Intent intent = new Intent(MainActivity.this, ManualActivity.class);
        startActivity(intent);
    }


}
