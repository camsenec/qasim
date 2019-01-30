package com.tanakatomoya.qasimulator.IO;

import android.content.Context;

import com.tanakatomoya.qasimulator.DrawableObject.MyPointF;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;
import com.tanakatomoya.qasimulator.FieldCreateView;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class FileIO {

    /**
     * fileName : データの読み書きに用いるファイル
     * customView : モデルを参照する先のビュー
     */
    String fileName = "SG.dat";
    FieldCreateView view;

    public FileIO(FieldCreateView customView){
        this.view = customView;
    }


    /**
     * ファイルからのデータの読み込み
     * @param context
     */
    /*
    public void reader(Context context){


        try {

            FileInputStream fileStream = context.openFileInput(fileName);
            //FileInputStreamを経由してObjectInputStreamを作成
            ObjectInputStream in = new ObjectInputStream(fileStream);

            //list<MyPointF> is in in.readObject()
            System.out.println("Loading...");

            //ファイルからのデータの読み込み
            ArrayList<MyPointF> points = (ArrayList<MyPointF>)in.readObject();

            System.out.println(points);
            view.setPoints(points);

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException c){
            c.getCause();
        }
    }
    */

    /**
     * ファイルへのデータの書き込み
     * @param context
     */

    public void writer(Context context){

        try {

            FileOutputStream fileStream = context.openFileOutput(fileName,MODE_PRIVATE);
            //FileOutputStreamを経由して, ObjectOutputStreamを作成
            ObjectOutputStream out = new ObjectOutputStream(fileStream);

            System.out.println("Saving...");

            //ファイルへのデータの書き込み
            List<MyTriangle> triangles = view.getTriangles();

            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));


            float JCoupling;
            for(MyTriangle triangleI : triangles){
                for(MyTriangle triangleJ : triangles){
                    if(triangleI.getNextTriangles().contains(triangleJ)) {
                        JCoupling = 1;
                    }else {
                        JCoupling = 0;
                    }

                    pw.write(String.format(Locale.US,
                            "%d\t%d\t%d\t%d\t%f",
                            triangleI.getSiteX(),
                            triangleI.getSiteY(),
                            triangleJ.getSiteX(),
                            triangleJ.getSiteY(),
                            JCoupling
                    ));
                }
            }

            out.close();

        } catch(IOException e){
           e.printStackTrace();
        }
    }
}
