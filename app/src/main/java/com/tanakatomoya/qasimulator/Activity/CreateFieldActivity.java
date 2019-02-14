package com.tanakatomoya.qasimulator.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.tanakatomoya.qasimulator.DrawableObject.MyPointF;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;
import com.tanakatomoya.qasimulator.R;
import com.tanakatomoya.qasimulator.View.FieldView;

import java.util.ArrayList;

public class CreateFieldActivity extends AppCompatActivity {

    public static final String EXTRA_DATA_TRIANGLES
            = "com.example.tanakatomoya.QASimulator.EXTRA_DATA";
    public static final String EXTRA_DATA_ITERATION
            = "com.example.tanakatomoya.QASimulator.EXTRA_ITERATION";

    int iterationNum = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_field);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        FieldView r = findViewById(R.id.createFieldView);
        MyPointF view = FieldView.getViewSize(r);

        r.setWidth(view.getX());
        r.setHeight(view.getY());
    }

    public void onClickReset(View view){
        FieldView v = findViewById(R.id.createFieldView);
        v.reset();
    }

    public void onClickCreateField(View view){
        FieldView v = findViewById(R.id.createFieldView);
        v.createField(iterationNum);
    }

    public void onClickCreateModel(View view){
        // Do something in response to button
        Intent intent = new Intent(CreateFieldActivity.this, CreateModelActivity.class);
        FieldView v = findViewById(R.id.createFieldView);
        ArrayList<MyTriangle> triangles = v.getTriangles();
        intent.putExtra(EXTRA_DATA_TRIANGLES, triangles);
        startActivity(intent);
    }
}
