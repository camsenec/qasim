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

public class MainActivity extends AppCompatActivity {

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
        v.reset();
    }

    public void createField(View view){
        CreateFieldView v = findViewById(R.id.createFieldView);
        v.createField();
    }

    public void createQASimulatorInstance(View view){
        // Do something in response to button
        Intent intent = new Intent(MainActivity.this, CreateModelActivity.class);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        CreateFieldView r = (CreateFieldView) findViewById(R.id.createFieldView);
        MyPointF view = CreateFieldView.getViewSize(r);

        r.setWidth(view.getX());
        r.setHeight(view.getY());
    }
}
