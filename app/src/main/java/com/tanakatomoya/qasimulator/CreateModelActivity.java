package com.tanakatomoya.qasimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tanakatomoya.qasimulator.DrawableObject.MyLine;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;
import com.tanakatomoya.qasimulator.IO.FileIO;
import com.tanakatomoya.qasimulator.Model.SpinGlassModel;
import com.tanakatomoya.qasimulator.Retrofit.QASimulatorAPI;

import java.io.File;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tanakatomoya.qasimulator.MainActivity.EXTRA_DATA;

public class CreateModelActivity extends AppCompatActivity {

    String BASE_URL = "http://10.0.2.2:8000/QASimulator/";

    ArrayList<MyTriangle> triangles;
    private MyTriangle[][] triangles2D = new MyTriangle[100][100];
    private int site_num;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        Gson gson = new Gson();

        Intent intent = getIntent();
        triangles = (ArrayList<MyTriangle>)intent.getSerializableExtra(EXTRA_DATA);

        site_num = (int)Math.sqrt(this.triangles.size()) + 1;

        final MaterialEditText text_name = findViewById(R.id.nameField);
        final MaterialEditText text_trotter = findViewById(R.id.trotterNumField);
        final Button btn_register = findViewById(R.id.btn_register);

        //Build Retrofit
        OkHttpClient client = new OkHttpClient();

        Retrofit retro = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        //create api service
        final QASimulatorAPI service = retro.create(QASimulatorAPI.class);


        //set onclick listener
        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                if(TextUtils.isEmpty(text_name.getText().toString())){
                    Toast.makeText(CreateModelActivity.this, "Please enter your model name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(text_trotter.getText().toString())) {
                    Toast.makeText(CreateModelActivity.this, "Please enter number of slices", Toast.LENGTH_SHORT).show();
                    return;
                }


                //convert trotter_num to String(2nd param)
                String trotter_num = text_trotter.getText().toString();

                //create spinGlassField and read file(4th param)
                searchNextTriangles();
                mappingTrianglesToSpinGlassField();

                System.out.println("write");
                FileIO fileFactory = new FileIO(CreateModelActivity.this);
                fileFactory.writer();
                String fileName = "SG.csv";
                File file = new File(fileName);

                //Debug
                System.out.println("name : " + text_name.getText().toString());
                System.out.println("trotter_num : " + trotter_num);


                final SpotsDialog waitingDialog = new SpotsDialog(CreateModelActivity.this);
                waitingDialog.show();
                waitingDialog.setMessage("Please waiting");


                //register to database(name,site_num,trotter_num,result,file) as multipart/form-data
                service.createSpinGlassModel(text_name.getText().toString(),
                        Integer.parseInt(trotter_num),
                        site_num,
                        0,
                        file)
                        .enqueue(new Callback<SpinGlassModel>(){
                            @Override
                            public void onResponse(Call<SpinGlassModel> call, Response<SpinGlassModel> response) {
                                //waitingDialog.dismiss();
                                SpinGlassModel spinGLassField = response.body();
                                Log.d("response", "get response");


                                if(TextUtils.isEmpty(spinGLassField.getError_msg())){
                                    Toast.makeText(CreateModelActivity.this, "Success!", Toast.LENGTH_SHORT);

                                }


                            }

                            @Override
                            public void onFailure(Call<SpinGlassModel> call, Throwable t) {
                                //waitingDialog.dismiss();
                                Log.d("error",t.getMessage());

                            }
                        });


            }
        });

    }

    private void mappingTrianglesToSpinGlassField(){
        MyTriangle triangle;
        for(int siteY = 0; siteY < site_num; siteY++){
            for(int siteX = 0; siteX < site_num; siteX++){
                if(siteY*site_num + siteX < triangles.size()){
                    triangle = triangles.get(siteY * site_num + siteX);
                    triangle.setSiteX(siteX);
                    triangle.setSiteY(siteY);
                    triangles2D[siteX + 1][siteY + 1] = triangle;
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
                        if(line.equals(searchedLine)){
                            searchedTriangle.getNextTriangles().add(triangle);
                        }
                    }
                }
            }
        }
    }

    public MyTriangle[][] getTriangles2D() {
        return triangles2D;
    }

    public ArrayList<MyTriangle> getTriangles() {
        return triangles;
    }
}
