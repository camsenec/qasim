package com.tanakatomoya.qasimulator.IO;

import android.content.Context;

import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Math.abs;


public class FileIO {

    private static double EPS = 1e-5;

    /**
     * fileName : データの読み書きに用いるファイル
     * customView : モデルを参照する先のビュー
     */

    public static String fileNameOutput = "SG.csv";
    public static String fileNameInput = "SGResult.csv";

    /**
     * read from local file
     */
    public static void reader(Context context,int siteNum, ArrayList<MyTriangle> triangles){

        /*---------read from local file---------*/
        try {
            System.out.println(context.getFilesDir().getPath());
            FileInputStream fileStream = context.openFileInput(fileNameInput);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileStream,"UTF-8"));

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                //in fortran siteX = 1 ~ site_num , but in java siteX = 0 ~ site_num - 1
                int siteX = Integer.parseInt(data[0]) - 1;
                int siteY = Integer.parseInt(data[1]) - 1;
                int color = Integer.parseInt(data[2]);
                int spin = Integer.parseInt(data[3]);

                //For Debug
                /*
                System.out.println(String.format(Locale.US,
                        "%d %d %d %d", siteX, siteY, color, spin));
                        */


                if (spin == 1 && siteY*siteNum + siteX < triangles.size()) {
                    triangles.get(siteY * siteNum + siteX).setColor(color);
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
     * write to local file
     */

    public static void writer(Context context, ArrayList<MyTriangle> triangles){

        try {

            System.out.println(context.getFilesDir().getPath());
            FileOutputStream fileStream = context.openFileOutput(
                     fileNameOutput, Context.MODE_PRIVATE);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(fileStream,"UTF-8"));

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
                                "%d,%d,%d,%d,%f\n",
                                triangleI.getSiteX(),
                                triangleI.getSiteY(),
                                triangleJ.getSiteX(),
                                triangleJ.getSiteY(),
                                JCoupling
                        ));

                        //For Debug
                        /*
                        if(Math.abs(JCoupling - (-1)) < EPS){
                            System.out.println(String.format(Locale.US,
                                    "%d,%d,%d,%d,%f",
                                    triangleI.getSiteX(),
                                    triangleI.getSiteY(),
                                    triangleJ.getSiteX(),
                                    triangleJ.getSiteY(),
                                    JCoupling));
                        }
                        */
                    }
                }
            }
            System.out.println("WRITE DONE");

            pw.close();

        } catch(IOException e){
           e.printStackTrace();
        }
    }

}
