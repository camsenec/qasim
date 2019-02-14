package com.tanakatomoya.qasimulator.IO;
import android.content.Context;
import android.os.AsyncTask;

import com.tanakatomoya.qasimulator.CreateFieldView;
import com.tanakatomoya.qasimulator.CreateModelActivity;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;
import com.tanakatomoya.qasimulator.MainActivity;
import com.tanakatomoya.qasimulator.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class NetworkIO extends AsyncTask<URL, Void, Boolean> {

    private MainActivity activity;

    public NetworkIO(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(URL... urls) {
        HttpURLConnection con = null;
        try {
            // アクセス先URL
            String resultUrl = urls[0].toString();
            final URL url = new URL(resultUrl);
            // 出力ファイルフルパス
            final String filePath = "SGResult.csv";

            // ローカル処理
            // コネクション取得
            con = (HttpURLConnection) url.openConnection();
            con.connect();

            // HTTPレスポンスコード
            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // 通信に成功した
                // ファイルのダウンロード処理を実行
                // 読み込み用ストリーム
                final InputStream input = con.getInputStream();
                final DataInputStream dataInput = new DataInputStream(input);
                // 書き込み用ストリーム
                final FileOutputStream fileOutput = activity.openFileOutput(filePath,
                        Context.MODE_PRIVATE);
                final DataOutputStream dataOut = new DataOutputStream(fileOutput);
                // 読み込みデータ単位
                final byte[] buffer = new byte[4096];
                // 読み込んだデータを一時的に格納しておく変数
                int readByte = 0;


                // ファイルを読み込む
                while((readByte = dataInput.read(buffer)) != -1) {
                    System.out.println(readByte);
                    dataOut.write(buffer, 0, readByte);
                }
                // 各ストリームを閉じる
                dataInput.close();
                fileOutput.close();
                dataInput.close();
                input.close();


                FileInputStream fileStream = activity.openFileInput(filePath);
                BufferedReader br = new BufferedReader(new InputStreamReader(fileStream,"UTF-8"));
                System.out.println("Loading...");

                String line;
                ArrayList<MyTriangle> triangles = activity.getTriangles();

                System.out.println("size : " + triangles.size());

                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    String[] data = line.split(",", -1);
                    //in fortran siteX = 1 ~ site_num , but in java siteX = 0 ~ site_num - 1
                    int siteX = Integer.parseInt(data[0]) - 1;
                    int siteY = Integer.parseInt(data[1]) - 1;
                    int color = Integer.parseInt(data[2]);
                    System.out.println(color);
                    double spin = Double.parseDouble(data[3]);

                    //need modification
                    if (Math.abs(spin - 1.0) < 1e-5 && siteY * 11 + siteX < triangles.size()) {
                        triangles.get(siteY * 11 + siteX).setColor(color);
                    }
                }

                activity.setTriangles(triangles);

                //close stream
                fileStream.close();
                br.close();
                System.out.println("DONE");

                // 処理成功
                return true;
            }


        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (con != null) {
                // コネクションを切断
                con.disconnect();
            }
        }
        return false;
    }
}
