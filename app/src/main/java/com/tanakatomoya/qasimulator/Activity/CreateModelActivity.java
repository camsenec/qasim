package com.tanakatomoya.qasimulator.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;
import com.tanakatomoya.qasimulator.R;
import com.tanakatomoya.qasimulator.Retrofit.QASimulatorAPI;
import com.tanakatomoya.qasimulator.Thread.UploadFieldThread;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tanakatomoya.qasimulator.Activity.CreateFieldActivity.EXTRA_DATA_ITERATION;
import static com.tanakatomoya.qasimulator.Activity.CreateFieldActivity.EXTRA_DATA_TRIANGLES;

public class CreateModelActivity extends AppCompatActivity {

    public static String BASE_URL = "http://10.0.2.2:8000/";
    //public static String BASE_URL = "http://54.95.61.149/";

    public static final String EXTRA_DATA_SITE
            = "com.example.tanakatomoya.QASimulator.EXTRA_SITE";

    private ArrayList<MyTriangle> triangles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_model);

        /*--------define Resource-------*/

        //receive list of triangles from MainActivity
        Intent intent = getIntent();
        triangles = (ArrayList<MyTriangle>)intent.getSerializableExtra(EXTRA_DATA_TRIANGLES);
        final int iterationNum = intent.getIntExtra(EXTRA_DATA_ITERATION, 3);

        final MaterialEditText text_name = findViewById(R.id.nameField);
        final MaterialEditText text_trotter = findViewById(R.id.trotterNumField);
        final Button btn_register = findViewById(R.id.btn_register);


        /*--------create Retrofit Client-------*/

        //Build Retrofit
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000,TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS).build();

        Retrofit retro = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        //create api service
        final QASimulatorAPI service = retro.create(QASimulatorAPI.class);


        /*--------set button for POST-------*/


        //set onclick listener
        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                if(TextUtils.isEmpty(text_name.getText().toString())){
                    Toast.makeText(CreateModelActivity.this,
                            "Please enter your model name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(text_trotter.getText().toString())) {
                    Toast.makeText(CreateModelActivity.this,
                            "Please enter trotter_number", Toast.LENGTH_SHORT).show();
                    return;
                }

                //delegate upload to thread
                UploadFieldThread uploadFieldThread =
                        new UploadFieldThread(CreateModelActivity.this, service,
                                triangles,
                                iterationNum,
                                text_name.getText().toString(),
                                text_trotter.getText().toString()
                                );

                //After thread is done, callback and download thread is executed in UI thread.
                uploadFieldThread.execute();

            }
        });
    }

}
