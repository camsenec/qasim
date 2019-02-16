package com.tanakatomoya.qasimulator.Thread;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tanakatomoya.qasimulator.Model.SpinGlassModel;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;
import com.tanakatomoya.qasimulator.IO.FileIO;
import com.tanakatomoya.qasimulator.Activity.CreateModelActivity;
import com.tanakatomoya.qasimulator.Retrofit.QASimulatorAPI;
import com.tanakatomoya.qasimulator.View.FieldView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * UploadFieldThread thread used by CreateModelActivity
 */

public class UploadFieldThread extends AsyncTask<Void, Void, Boolean> {

    private CreateModelActivity activity;
    private QASimulatorAPI service;
    private ArrayList<MyTriangle> triangles;
    private int iterationNum;
    private String name;
    private String trotterNum;
    private String siteNum;
    private SpotsDialog waitingDialog;


    public UploadFieldThread(CreateModelActivity activity, QASimulatorAPI service,
                             ArrayList<MyTriangle> triangles, int iterationNum,
                             String name, String trotterNum) {
        this.activity = activity;
        this.service = service;
        this.triangles = triangles;
        this.iterationNum = iterationNum;
        this.name = name;
        this.trotterNum = trotterNum;
        this.siteNum = String.valueOf((int) Math.sqrt(triangles.size()) + 1);
        this.waitingDialog = new SpotsDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        waitingDialog.show();
        waitingDialog.setMessage("Creating Data...");

    }


    @Override
    protected Boolean doInBackground(Void... voids) {

        /*--------create POST data(trotterNum, siteNum)-------*/

        //convert triangles into SpinGlassField
        FieldView.searchNextTriangles(this.triangles);
        FieldView.mappingTrianglesToSpinGlassField(Integer.parseInt(this.siteNum), this.triangles);


        FileIO.writer(activity, this.triangles);


        return true;
    }


    //execute at UI Thread
    @Override
    protected void onPostExecute(Boolean aBoolean) {

        waitingDialog.dismiss();

        /*------create request body------*/
        RequestBody nameBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                name);

        RequestBody trotterNumBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                trotterNum);

        RequestBody siteNumBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                siteNum);

        RequestBody resultBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                "0");

        File file = new File(activity.getFilesDir().getPath() +"/"+ FileIO.fileNameOutput);
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                file);


        /*-------POST and get Response------*/
        Call<SpinGlassModel> call = service.createSpinGlassModel(
                nameBody,
                trotterNumBody,
                siteNumBody,
                resultBody,
                fileBody);

        Toast.makeText(activity,
                "Data is posted! Please wait until results are returned.", Toast.LENGTH_LONG).show();


        call.enqueue(new Callback<SpinGlassModel>() {
            @Override
            public void onResponse(Call<SpinGlassModel> call,
                                   Response<SpinGlassModel> response) {

                Log.d("response", "get response");

                //delegate download to thread
                DownLoadFieldThread downloadFieldThread =
                        new DownLoadFieldThread(activity, Integer.parseInt(siteNum), iterationNum);
                try {
                    downloadFieldThread.execute(
                            new URL(CreateModelActivity.BASE_URL + "data/SGResult.csv"));
                }catch(MalformedURLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SpinGlassModel> call, Throwable t) {
                t.printStackTrace();

                Toast.makeText(activity,
                        "Sorry. Error is occurred"
                        , Toast.LENGTH_SHORT).show();

            }
        });
    }

}
