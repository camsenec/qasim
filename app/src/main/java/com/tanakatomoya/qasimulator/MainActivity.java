package com.tanakatomoya.qasimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.tanakatomoya.qasimulator.DrawableObject.MyPointF;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;
import com.tanakatomoya.qasimulator.IO.FileIO;
import com.tanakatomoya.qasimulator.IO.NetworkIO;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.tanakatomoya.qasimulator.CreateModelActivity.EXTRA_DATA_RETURNED;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_DATA
            = "com.example.tanakatomoya.QASimulator.EXTRA_DATA";

    ArrayList<MyTriangle> triangles;
    int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void reset(View view){
        CreateFieldView v = findViewById(R.id.createFieldView);
        ArrayList<MyTriangle> triangles = v.getTriangles();
        this.setTriangles(triangles);
        try {
            new NetworkIO(MainActivity.this)
                    .execute(new URL("http://10.0.2.2:8000/data/SGResult.csv"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    public void createField(View view){
        CreateFieldView v = findViewById(R.id.createFieldView);
        v.createField();
    }

    public void createQASimulatorInstance(View view){
        // Do something in response to button
        //Intent intent = new Intent(MainActivity.this, CreateModelActivity.class);
        CreateFieldView v = findViewById(R.id.createFieldView);
        //ArrayList<MyTriangle> triangles = v.getTriangles();
        v.setTriangles(this.triangles);
        v.setCompleteFlag(1);
        v.calcAccuracy();
        //intent.putExtra(EXTRA_DATA, triangles);
        //startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CreateFieldView r = (CreateFieldView) findViewById(R.id.createFieldView);
        MyPointF view = CreateFieldView.getViewSize(r);

        r.setWidth(view.getX());
        r.setHeight(view.getY());
    }

    public ArrayList<MyTriangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(ArrayList<MyTriangle> triangles) {
        this.triangles = triangles;
    }
}
