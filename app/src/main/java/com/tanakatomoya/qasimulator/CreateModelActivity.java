package com.tanakatomoya.qasimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tanakatomoya.qasimulator.DrawableObject.MyLine;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;
import com.tanakatomoya.qasimulator.IO.FileIO;
import com.tanakatomoya.qasimulator.Model.SpinGlassModel;
import com.tanakatomoya.qasimulator.Retrofit.QASimulatorAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tanakatomoya.qasimulator.MainActivity.EXTRA_DATA;

public class CreateModelActivity extends AppCompatActivity {

    String BASE_URL = "http://10.0.2.2:8000/QASimulator/";

    public static final String EXTRA_DATA_RETURNED
            = "com.example.tanakatomoya.QASimulator.EXTRA_RETURN";;

    ArrayList<MyTriangle> triangles;
    private int siteNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //receive list of triangles from MainActivity
        Intent intent = getIntent();
        triangles = (ArrayList<MyTriangle>)intent.getSerializableExtra(EXTRA_DATA);

        final MaterialEditText text_name = findViewById(R.id.nameField);
        final MaterialEditText text_trotter = findViewById(R.id.trotterNumField);
        final Button btn_register = findViewById(R.id.btn_register);


        /*--------create Retrofit Client-------*/

        //Build Retrofit
        final OkHttpClient client = new OkHttpClient();

        Retrofit retro = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
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
                            "Please enter number of slices", Toast.LENGTH_SHORT).show();
                    return;
                }


                /*--------create POST data(trotterNum, siteNum)-------*/
                //get trotterNum for POST
                String trotterNum = text_trotter.getText().toString();
                //get siteNum for POST
                siteNum = (int)Math.sqrt(triangles.size()) + 1;

                //Debug
                System.out.println("name : " + text_name.getText().toString());
                System.out.println("trotter_num : " + trotterNum);
                System.out.println("site_num : " + siteNum);

                /*--------create POST data(spinGlassField)-------*/
                final SpotsDialog waitingDialog = new SpotsDialog(
                        CreateModelActivity.this);
                waitingDialog.show();
                waitingDialog.setMessage("creating data...");

                //convert triangles into SpinGlassField
                searchNextTriangles();
                mappingTrianglesToSpinGlassField();

                //create file for POST using created SpinGlassField
                System.out.println("write to file");
                final FileIO fileFactory = new FileIO(CreateModelActivity.this);
                fileFactory.writer();

                waitingDialog.dismiss();

                /*------create request body------*/
                RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"),
                        text_name.getText().toString());
                RequestBody trotterNumBody = RequestBody.create(MediaType.parse("text/plain"),
                        trotterNum);
                RequestBody siteNumBody = RequestBody.create(MediaType.parse("text/plain"),
                        String.valueOf(siteNum));
                RequestBody resultBody = RequestBody.create(MediaType.parse("text/plain"),
                        "0");
                File file = new File(fileFactory.getFileNameOutput());
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"),
                        file);

                /*
                MultipartBody.Part namePart = MultipartBody.Part.createFormData(
                        "name", text_name.getText().toString());

                MultipartBody.Part trotterNumPart = MultipartBody.Part.createFormData(
                        "trotter_num", trotterNum);

                MultipartBody.Part siteNumPart = MultipartBody.Part.createFormData(
                        "site_num",String.valueOf(siteNum));

                MultipartBody.Part resultPart = MultipartBody.Part.createFormData(
                        "result", "0");
                */

                MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                        "data", file.getName(), fileBody);

                //inform user that data is sent
                Toast.makeText(CreateModelActivity.this,
                        "Your data is sent! Please wait until result is displayed"
                         , Toast.LENGTH_LONG);


                /*-------POST and get Response------*/
                Call<SpinGlassModel> call = service.createSpinGlassModel(
                        nameBody,
                        trotterNumBody,
                        siteNumBody,
                        resultBody,
                        filePart);

                call.enqueue(new Callback<SpinGlassModel>(){
                    @Override
                    public void onResponse(Call<SpinGlassModel> call,
                                           Response<SpinGlassModel> response) {

                        Log.d("response", "get response");
                        SpinGlassModel result = response.body();

                        /*
                        final SpotsDialog waitingDialog = new SpotsDialog(
                                CreateModelActivity.this);

                        //show dialog
                        waitingDialog.show();
                        waitingDialog.setMessage("Loading file...");


                        //file read
                        String resultUrl = result.getResultUrl();

                        if(resultUrl != null) {
                          fileFactory.reader(siteNum, "SGResult.csv");
                        }

                        waitingDialog.dismiss();

                        Intent intent = new Intent(CreateModelActivity.this,
                                MainActivity.class);
                        intent.putExtra(EXTRA_DATA_RETURNED, triangles);
                        startActivity(intent);
                        */

                    }

                    @Override
                    public void onFailure(Call<SpinGlassModel> call, Throwable t) {
                        Toast.makeText(CreateModelActivity.this,
                                "sorry. error is occurred"
                                , Toast.LENGTH_SHORT);

                    }
                });
            }
        });
    }

    private void mappingTrianglesToSpinGlassField(){
        MyTriangle triangle;
        for(int siteY = 0; siteY < siteNum; siteY++){
            for(int siteX = 0; siteX < siteNum; siteX++){
                if(siteY*siteNum + siteX < triangles.size()){
                    triangle = triangles.get(siteY * siteNum + siteX);
                    triangle.setSiteX(siteX + 1);
                    triangle.setSiteY(siteY + 1);
                    System.out.println(triangle);
                }
            }
        }
    }

    //O(9*N^2) = O(N^2)
    private void searchNextTriangles(){
        for(MyTriangle searchedTriangle: this.triangles){
            for(MyTriangle triangle : this.triangles){
                for(MyLine searchedLine : searchedTriangle.getIncludedLines()){ //iteration : 3
                    for(MyLine line : triangle.getIncludedLines()){ //iteration : 3
                        if(line.equals(searchedLine) && !searchedTriangle.equals(triangle)){
                            searchedTriangle.getNextTriangles().add(triangle);
                        }
                    }
                }
            }
        }
    }

    /**
     * getter and setter
     */

    public ArrayList<MyTriangle> getTriangles() {
        return triangles;
    }

    public int getSiteNum() {
        return siteNum;
    }
}
