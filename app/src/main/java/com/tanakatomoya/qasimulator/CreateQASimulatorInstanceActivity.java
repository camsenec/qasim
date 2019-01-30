package com.tanakatomoya.qasimulator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.tanakatomoya.qasimulator.Model.SpinGlassModel;
import com.tanakatomoya.qasimulator.Retrofit.QASimulatorAPI;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateQASimulatorInstanceActivity extends AppCompatActivity {

    String BASE_URL = "http://10.0.2.2:8888/QASimulator/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        final MaterialEditText text_name = findViewById(R.id.nameField);
        final MaterialEditText text_site = findViewById(R.id.siteNumField);
        final MaterialEditText text_trotter = findViewById(R.id.trotterNumField);

        Button btn_register = (Button)findViewById(R.id.btn_register);

        OkHttpClient client = new OkHttpClient();

        Retrofit retro = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        //create api service
        final QASimulatorAPI service = retro.create(QASimulatorAPI.class);

        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(text_name.getText().toString())){
                    Toast.makeText(CreateQASimulatorInstanceActivity.this, "Please enter your model name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(text_site.getText().toString())){
                    Toast.makeText(CreateQASimulatorInstanceActivity.this, "Please enter number of sites", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(text_trotter.getText().toString())) {
                    Toast.makeText(CreateQASimulatorInstanceActivity.this, "Please enter number of slices", Toast.LENGTH_SHORT).show();
                    return;
                }

                //convert to String
                String site_num = text_site.getText().toString();
                String trotter_num = text_trotter.getText().toString();

                //Debug
                System.out.println("name : " + text_name.getText().toString());
                System.out.println("trotter_num : " + trotter_num);
                System.out.println("site_num : " + site_num);

                //display dialog
                final SpotsDialog waitingDialog = new SpotsDialog(CreateQASimulatorInstanceActivity.this);
                waitingDialog.show();
                waitingDialog.setMessage("Please waiting");

                //register to database
                service.createSpinGlassModel(text_name.getText().toString(),
                        Integer.parseInt(trotter_num),
                        Integer.parseInt(site_num))
                        .enqueue(new Callback<SpinGlassModel>(){
                            @Override
                            public void onResponse(Call<SpinGlassModel> call, Response<SpinGlassModel> response) {
                                waitingDialog.dismiss();
                                SpinGlassModel spinGLassField = response.body();
                                Log.d("response", "get response");

                                if(TextUtils.isEmpty(spinGLassField.getError_msg())){
                                    Toast.makeText(CreateQASimulatorInstanceActivity.this, "Success!", Toast.LENGTH_SHORT);

                                }

                            }

                            @Override
                            public void onFailure(Call<SpinGlassModel> call, Throwable t) {
                                waitingDialog.dismiss();
                                Log.d("error",t.getMessage());

                            }
                        });


            }
        });

    }
}
