package com.tanakatomoya.qasimulator.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tanakatomoya.qasimulator.DrawableObject.MyLine;
import com.tanakatomoya.qasimulator.DrawableObject.MyPointF;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;

import java.util.ArrayList;

public class FieldView extends View{
    public final float radius = 10;

    /**
     * params(object)
     * points : ユーザが入力した点のリスト
     * lines : 2点によって生成される線のリスト
     * triangles : 3点によって生成される三角形のリスト
     * trianglesForFT : 3点によって生成される三角形のリスト(ファイル転送用)
     * iteration_num : フラクタル生成回数
     */

    private ArrayList<MyTriangle> triangles = new ArrayList<>();

    /**
     * params(paint)
     * paintOfPoint : ユーザが入力した点のリスト
     * paintOfLine : 2点によって生成される線のリスト
     */
    private Paint paintOfPoint;
    private Paint paintOfLine;
    private Path path;


    /**
     * params(windowsize)
     * width
     * height
     */
    private float width;
    private float height;


    /**
     * コンストラクタ
     **/
    public FieldView(Context context, @Nullable AttributeSet attrs) {
        super(context ,attrs);
        paintOfPoint = new Paint();
        paintOfLine = new Paint();
        paintOfLine.setStrokeWidth(5);
        paintOfLine.setARGB(255, 50, 90, 255);
        paintOfPoint.setColor(Color.BLACK);
    }

    /**
     * 描画ルーチン
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        for (MyTriangle triangle : this.triangles) {
            path = new Path();
            switch(triangle.getColor()){
                case 1:
                    paintOfLine.setColor(Color.BLUE);
                    break;
                case 2:
                    paintOfLine.setColor(Color.GREEN);
                    break;
                case 3:
                    paintOfLine.setColor(Color.RED);
                    break;
                case 4:
                    paintOfLine.setColor(Color.YELLOW);
                    break;
                default:
                    paintOfLine.setColor(Color.WHITE);
                    break;
            }
            path.moveTo(triangle.getPoint1().getX(), triangle.getPoint1().getY());
            path.lineTo(triangle.getPoint2().getX(), triangle.getPoint2().getY());
            path.lineTo(triangle.getPoint3().getX(), triangle.getPoint3().getY());
            path.lineTo(triangle.getPoint1().getX(), triangle.getPoint1().getY());
            canvas.drawPath(path, paintOfLine);
        }

        paintOfLine.setColor(Color.BLACK);
        paintOfPoint.setColor(Color.BLACK);

        for (MyTriangle triangle : this.triangles) {
            for (MyLine line : triangle.getIncludedLines()) {
                canvas.drawLine(line.getPoint1().getX(), line.getPoint1().getY(),
                        line.getPoint2().getX(), line.getPoint2().getY(), paintOfLine);
            }

            for(MyPointF point : triangle.getIncludedPoints()){
                canvas.drawCircle(point.getX(), point.getY(), radius, paintOfPoint);
            }
        }

    }

    /**
     * RESETボタンが押されたときの処理
     */
    public void reset(){
        triangles.clear();
        invalidate();
    }

    /**
     * CREATE FIELDボタンが押されたときの処理
     * 塗りつぶす三角形を生成
     * used : initializeField(), createFractal()
     */
    public void createField(int iterationNum){
        initializeField();
        System.out.println(iterationNum);
        for(int i = 0 ; i < iterationNum; i++){
            createFractal();
            invalidate();
        }

    }

    //windowの角のうちの2点と中央を頂点とする, 4つの三角形を生成
    private void initializeField(){
        triangles.clear();

        float minX = 20;
        float maxX = this.width - 20;
        float minY = 20;
        float maxY = this.height - 20;

        MyPointF pointUpperLeft = new MyPointF(minX, minY);
        MyPointF pointUpperRight = new MyPointF(maxX, minY);
        MyPointF pointLowerLeft = new MyPointF(minX, maxY);
        MyPointF pointLowerRight = new MyPointF(maxX, maxY);
        MyPointF pointCenter = new MyPointF((maxX - minX) / 2, (maxY - minY) / 2);

        MyTriangle triangle1 = new MyTriangle(pointUpperLeft, pointUpperRight, pointCenter);
        MyTriangle triangle2 = new MyTriangle(pointUpperLeft, pointLowerLeft, pointCenter);
        MyTriangle triangle3 = new MyTriangle(pointUpperRight, pointLowerRight, pointCenter);
        MyTriangle triangle4 = new MyTriangle(pointLowerLeft, pointLowerRight, pointCenter);

        triangles.add(triangle1);
        triangles.add(triangle2);
        triangles.add(triangle3);
        triangles.add(triangle4);
    }

    //フラクタルを生成
    private void createFractal(){
        ArrayList<MyTriangle> tmpTriangles = new ArrayList<>();

        for(MyTriangle triangle : this.triangles){

            MyPointF center = triangle.calcCenter();
            MyPointF point1 = triangle.getPoint1();
            MyPointF point2 = triangle.getPoint2();
            MyPointF point3 = triangle.getPoint3();

            MyTriangle triangle1 = new MyTriangle(center, point1, point2);
            MyTriangle triangle2 = new MyTriangle(center, point2, point3);
            MyTriangle triangle3 = new MyTriangle(center, point1, point3);

            tmpTriangles.add(triangle1);
            tmpTriangles.add(triangle2);
            tmpTriangles.add(triangle3);
        }

        triangles.clear();
        triangles.addAll(tmpTriangles);


    }

    //windowサイズを取得
    public static MyPointF getViewSize(View View){
        MyPointF point = new MyPointF(View.getWidth(), View.getHeight());
        return point;
    }

    /**
     * static functions
     */


    /*正解率の計算*/
    public static double calcAccuracy(ArrayList<MyTriangle> triangles){

        int accuracy = 0;
        int sum = 0;
        searchNextTriangles(triangles);
        for(MyTriangle triangleI: triangles){
            for(MyTriangle triangleJ : triangles){
                if(!triangleI.equals(triangleJ) &&
                        triangleI.getNextTriangles().contains(triangleJ)) {
                    if (triangleI.getColor() != triangleJ.getColor()){
                        accuracy = accuracy + 1;
                    }
                    sum = sum + 1;
                }
            }
        }
        System.out.println("accurate count: " + accuracy);
        System.out.println("sum : " + sum);
        System.out.println("accuracy" + (float)accuracy / (float)sum);
        return (float)accuracy / (float)sum;
    }

    //O(9*N^2) = O(N^2)
    public static void searchNextTriangles(ArrayList<MyTriangle> triangles){

        for(MyTriangle triangleI: triangles){
            for(MyTriangle triangleJ : triangles){
                for(MyLine lineI : triangleI.getIncludedLines()){ //iteration : 3
                    for(MyLine lineJ : triangleJ.getIncludedLines()){ //iteration : 3
                        if(lineJ.equals(lineI) && !triangleI.equals(triangleJ)){
                            triangleI.getNextTriangles().add(triangleJ);
                        }
                    }
                }
            }
        }
    }

    public static void mappingTrianglesToSpinGlassField(int siteNum, ArrayList<MyTriangle> triangles){

        MyTriangle triangle;
        for(int siteY = 0; siteY < siteNum; siteY++){
            for(int siteX = 0; siteX < siteNum; siteX++){
                if(siteY*siteNum + siteX < triangles.size()){
                    triangle = triangles.get(siteY * siteNum + siteX);
                    triangle.setSiteX(siteX + 1);
                    triangle.setSiteY(siteY + 1);
                }
            }
        }
    }


    /**
     * getter and setter
     */

    public void setWidth(float width) { this.width = width; }

    public void setHeight(float height) { this.height = height; }

    public ArrayList<MyTriangle> getTriangles() { return this.triangles; }

    public void setTriangles(ArrayList<MyTriangle> triangles) {
        this.triangles = triangles;
        invalidate();
    }

}
