package com.tanakatomoya.qasimulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tanakatomoya.qasimulator.Model.SpinGlassModel;
import com.tanakatomoya.qasimulator.Retrofit.QASimulatorAPI;

import java.io.File;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateModelActivity extends AppCompatActivity {

    String BASE_URL = "http://10.0.2.2:8000/QASimulator/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //Resource
        final MaterialEditText text_name = findViewById(R.id.nameField);
        final MaterialEditText text_trotter = findViewById(R.id.trotterNumField);
        final Button btn_register = (Button) findViewById(R.id.btn_register);

        final CreateFieldView view = findViewById(R.id.createFieldView);


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
                view.searchNextTriangles();
                view.mappingTrianglesToSpinGlassField();
                String fileName = "SG.dat";
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
                        view.getSite_num(),
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
}
