package com.tanakatomoya.qasimulator.Thread;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


import com.tanakatomoya.qasimulator.Activity.ResultActivity;
import com.tanakatomoya.qasimulator.IO.FileIO;
import com.tanakatomoya.qasimulator.Activity.CreateModelActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.tanakatomoya.qasimulator.Activity.CreateFieldActivity.EXTRA_DATA_ITERATION;
import static com.tanakatomoya.qasimulator.Activity.CreateModelActivity.EXTRA_DATA_SITE;


public final class DownLoadFieldThread extends AsyncTask<URL, Void, Boolean> {

    private CreateModelActivity activity;
    private int siteNum;
    private int iterationNum;


    public DownLoadFieldThread(CreateModelActivity activity, int siteNum, int iterationNum){
        this.activity = activity;
        this.siteNum = siteNum;
        this.iterationNum = iterationNum;
    }

    @Override
    protected Boolean doInBackground(URL... urls) {
        HttpURLConnection con = null;

        try {
            // URL
            String resultUrl = urls[0].toString();
            final URL url = new URL(resultUrl);


            // establish connection
            con = (HttpURLConnection) url.openConnection();
            con.connect();

            // Code of http response
            final int status = con.getResponseCode();
            //if succeed in establish connection, download file
            if (status == HttpURLConnection.HTTP_OK) {

                //Input Stream
                final InputStream input = con.getInputStream();
                final DataInputStream dataInput = new DataInputStream(input);

                //Output Stream
                final FileOutputStream fileOutput = activity.openFileOutput(
                        FileIO.fileNameInput, Context.MODE_PRIVATE);
                final DataOutputStream dataOut = new DataOutputStream(fileOutput);
                final byte[] buffer = new byte[4096];
                int readByte = 0;


                // read downloaded file
                while((readByte = dataInput.read(buffer)) != -1) {
                    dataOut.write(buffer, 0, readByte);
                }

                //close Stream
                dataInput.close();
                fileOutput.close();
                dataInput.close();
                input.close();

                return true;
            }


        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Intent intent = new Intent(activity,
                ResultActivity.class);
        intent.putExtra(EXTRA_DATA_SITE, siteNum);
        intent.putExtra(EXTRA_DATA_ITERATION, iterationNum);
        activity.startActivity(intent);

    }
}