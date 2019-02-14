package com.tanakatomoya.qasimulator.IO;

import android.content.Context;

import com.tanakatomoya.qasimulator.CreateModelActivity;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;
import com.tanakatomoya.qasimulator.CreateFieldView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class FileIO {

    final private double EPS = 1e5;

    /**
     * fileName : データの読み書きに用いるファイル
     * customView : モデルを参照する先のビュー
     */
    String fileNameOutput = "SG.csv";
    String fileNameInput = "SGResult.csv";
    CreateModelActivity activity;

    public FileIO(CreateModelActivity activity){
        this.activity = activity;
    }


    /**
     * ネットワークからのデータの読み込み
     */
    public void reader(int site_num){

        /*---------read from local file---------*/
        try {
            FileInputStream fileStream = activity.openFileInput(fileNameInput);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileStream,"UTF-8"));
            System.out.println("Loading...");

            String line;
            List<MyTriangle> triangles = activity.getTriangles();

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] data = line.split(",", -1);
                //in fortran siteX = 1 ~ site_num , but in java siteX = 0 ~ site_num - 1
                int siteX = Integer.parseInt(data[0]) - 1;
                int siteY = Integer.parseInt(data[1]) - 1;
                int color = Integer.parseInt(data[2]);
                double spin = Double.parseDouble(data[3]);

                if (Math.abs(spin - 1.0) < EPS) {
                    triangles.get(siteY * site_num + siteX).setColor(color);
                }
            }

            //close stream
            fileStream.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * ファイルへのデータの書き込み
     */

    public void writer(){

        try {


            FileOutputStream fileStream = activity.openFileOutput(fileNameOutput,Context.MODE_PRIVATE);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(fileStream,"UTF-8"));

            //ファイルへのデータの書き込み
            List<MyTriangle> triangles = activity.getTriangles();

            System.out.println("Saving...");
            float JCoupling;

            for(MyTriangle triangleI : triangles){
                for(MyTriangle triangleJ : triangles){
                    if(triangleI.getNextTriangles().contains(triangleJ)) {
                        JCoupling = -1;
                    }else {
                        JCoupling = 0;
                    }

                    if(!triangleI.equals(triangleJ)) {

                        pw.write(String.format(Locale.US,
                                "%d,%d,%d,%d,%f",
                                triangleI.getSiteX(),
                                triangleI.getSiteY(),
                                triangleJ.getSiteX(),
                                triangleJ.getSiteY(),
                                JCoupling
                        ));
                        System.out.println(String.format(Locale.US,
                                "%d,%d,%d,%d,%f",
                                triangleI.getSiteX(),
                                triangleI.getSiteY(),
                                triangleJ.getSiteX(),
                                triangleJ.getSiteY(),
                                JCoupling

                        ));

                    }
                }
            }
            System.out.println("WRITE DONE");

            pw.close();

        } catch(IOException e){
           e.printStackTrace();
        }
    }

    public String getFileNameOutput() {
        return fileNameOutput;
    }
}
