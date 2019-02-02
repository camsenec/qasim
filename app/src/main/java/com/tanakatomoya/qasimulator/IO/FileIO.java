package com.tanakatomoya.qasimulator.IO;

import android.content.Context;

import com.tanakatomoya.qasimulator.CreateModelActivity;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;
import com.tanakatomoya.qasimulator.CreateFieldView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

public class FileIO {

    final private double EPS = 1e5;

    /**
     * fileName : データの読み書きに用いるファイル
     * customView : モデルを参照する先のビュー
     */
    String fileName = "SG.csv";
    CreateModelActivity activity;

    public FileIO(CreateModelActivity activity){
        this.activity = activity;
    }


    /**
     * ファイルからのデータの読み込み
     */
    public void reader(){


        try {

            FileInputStream fileStream = activity.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileStream,"UTF-8"));

            System.out.println("Loading...");

            String line;
            MyTriangle[][] triangles2D = activity.getTriangles2D();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                int siteI = Integer.parseInt(data[0]);
                int siteJ = Integer.parseInt(data[1]);
                int siteK = Integer.parseInt(data[2]);
                double spin = Double.parseDouble(data[3]);

                if(Math.abs(spin - 1.0) < EPS) {
                    triangles2D[siteI - 1][siteJ - 1].setColor(siteK);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ファイルへのデータの書き込み
     */

    public void writer(){

        try {


            FileOutputStream fileStream = activity.openFileOutput(fileName,Context.MODE_PRIVATE);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(fileStream,"UTF-8"));

            //ファイルへのデータの書き込み
            List<MyTriangle> triangles = activity.getTriangles();

            System.out.println("Saving...");
            float JCoupling;

            for(MyTriangle triangleI : triangles){
                for(MyTriangle triangleJ : triangles){
                    if(triangleI.getNextTriangles().contains(triangleJ)) {
                        JCoupling = 1;
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

            pw.close();

        } catch(IOException e){
           e.printStackTrace();
        }
    }
}
