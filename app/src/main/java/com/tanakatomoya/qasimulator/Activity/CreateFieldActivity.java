package com.tanakatomoya.qasimulator.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


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

    private int iterationNum = 4;

    String spinnerItems[] = {
            "1",
            "2",
            "3"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_field);

        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerItems
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
                iterationNum = Integer.parseInt(item);
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
                iterationNum = 3;
            }
        });

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
        intent.putExtra(EXTRA_DATA_ITERATION, iterationNum);
        startActivity(intent);
    }
}
