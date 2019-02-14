package com.tanakatomoya.qasimulator.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tanakatomoya.qasimulator.DrawableObject.MyPointF;
import com.tanakatomoya.qasimulator.IO.FileIO;
import com.tanakatomoya.qasimulator.R;
import com.tanakatomoya.qasimulator.View.FieldView;


import java.util.Locale;

import static com.tanakatomoya.qasimulator.Activity.CreateFieldActivity.EXTRA_DATA_ITERATION;
import static com.tanakatomoya.qasimulator.Activity.CreateModelActivity.EXTRA_DATA_SITE;

public class ResultActivity extends AppCompatActivity {

    int iterationNum;
    int siteNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        this.siteNum = intent.getIntExtra(EXTRA_DATA_SITE, 11);
        this.iterationNum = intent.getIntExtra(EXTRA_DATA_ITERATION, 3);

    }

    public void onClickShowResult(View view) {//receive list of triangles from MainActivity
        FieldView v = findViewById(R.id.createFieldView);
        v.createField(iterationNum);

        //read from local file
        FileIO.reader(ResultActivity.this, siteNum, v.getTriangles());

        double accuracy = FieldView.calcAccuracy(v.getTriangles());
        String accuracyStr = String.format(Locale.US, "%.2f", accuracy * 100);
        Toast.makeText(ResultActivity.this,
                "Accuracy is " + accuracyStr + " %"
                , Toast.LENGTH_LONG).show();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        FieldView v = findViewById(R.id.createFieldView);
        MyPointF view = FieldView.getViewSize(v);
        v.setWidth(view.getX());
        v.setHeight(view.getY());
    }

}
