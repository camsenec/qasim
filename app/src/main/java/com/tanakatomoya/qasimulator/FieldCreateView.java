package com.tanakatomoya.qasimulator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tanakatomoya.qasimulator.DrawableObject.MyLine;
import com.tanakatomoya.qasimulator.DrawableObject.MyPointF;
import com.tanakatomoya.qasimulator.DrawableObject.MyTriangle;

import java.util.ArrayList;
import java.util.List;

public class FieldCreateView extends View{
    public final double INF = 1e5;
    public final float radius = 10;

    /**
     * params(object)
     * points : ユーザが入力した点のリスト
     * lines : 2点によって生成される線のリスト
     * triangles : 3点によって生成される三角形のリスト
     * iteration_num : 内部に三角形を生成する回数
     */
    private List<MyPointF> points = new ArrayList<>();
    private List<MyLine> lines = new ArrayList<>();
    private List<MyTriangle> triangles = new ArrayList<>();
    private int iteration_num = 3;

    /**
     * params(paint)
     * paintOfPoint : ユーザが入力した点のリスト
     * paintOfLine : 2点によって生成される線のリスト
     */
    private Paint paintOfPoint;
    private Paint paintOfLine;

    /**
     * params(UI)
     */
    private MyPointF dragCurrent;


    /**
     * params(windowsize)
     * width
     * height
     */
    private float width;
    private float height;

    /**
     * params(for spinGlass Model)
     */
    private int site_num;



    /**
     * コンストラクタ
     **/
    public FieldCreateView(Context context, @Nullable AttributeSet attrs) {
        super(context ,attrs);
        paintOfPoint = new Paint();
        paintOfLine = new Paint();
        paintOfLine.setStrokeWidth(5);
        paintOfPoint.setColor(Color.BLUE);
    }

    /**
     * 描画ルーチン
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        for(MyPointF point : this.points){
            canvas.drawCircle(point.getX(), point.getY(), radius, paintOfPoint);
        }

        for(MyLine line : this.lines){
            canvas.drawLine(line.getPoint1().getX(), line.getPoint1().getY(),
                    line.getPoint2().getX(), line.getPoint2().getY(), paintOfLine);
        }

        if(dragCurrent != null) {
            canvas.drawCircle(dragCurrent.getX(), dragCurrent.getY(), radius, paintOfPoint);
        }

    }


    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }



    private void createspinGlassCoupling(){
        site_num = (int)Math.sqrt(triangles.size()) + 1;
        for(int siteY = 1; siteY <= site_num; siteY++){
            for(int siteX = 1; siteX <= site_num; siteX++){
                if(siteY*site_num + siteX <= triangles.size()) {
                    triangles.get(siteY * site_num + siteX).setSiteX(siteX);
                    triangles.get(siteY * site_num + siteX).setSiteY(siteY);
                }
            }
        }
    }

    private void searchNextTriangles(){
        for(MyTriangle searchedTriangle: triangles){
            for(MyTriangle triangle : triangles){
                for(MyLine searchedLine : searchedTriangle.getIncludedLines()){
                  for(MyLine line : triangle.getIncludedLines()){
                      if(line.equals(searchedLine)){
                            searchedTriangle.getNextTriangles().add(triangle);
                        }
                    }
                }
            }
        }
    }

    public void createField(){
        initializeField();
        for(int i = 0 ; i < iteration_num; i++){
            paintOfLine.setARGB(255, 50, 90 - i*20, 255);
            createTrianglesinTriangle();
            invalidate();
        }
    }

    private void initializeField(){
        float minX = 20;
        float maxX = this.width - 20;
        float minY = 20;
        float maxY = this.height - 20;

        MyPointF pointUpperLeft = new MyPointF(minX, minY);
        MyPointF pointUpperRight = new MyPointF(maxX, minY);
        MyPointF pointLowerLeft = new MyPointF(minX, maxY);
        MyPointF pointLowerRight = new MyPointF(maxX, maxY);
        MyPointF pointCenter = new MyPointF((maxX - minX) / 2, (maxY - minY) / 2);

        MyLine line1 = new MyLine(pointUpperLeft, pointUpperRight);
        MyLine line2 = new MyLine(pointUpperLeft, pointLowerLeft);
        MyLine line3 = new MyLine(pointUpperRight, pointLowerRight);
        MyLine line4 = new MyLine(pointLowerLeft, pointLowerRight);
        MyLine line5 = new MyLine(pointUpperLeft, pointCenter);
        MyLine line6 = new MyLine(pointUpperRight, pointCenter);
        MyLine line7 = new MyLine(pointLowerLeft, pointCenter);
        MyLine line8 = new MyLine(pointUpperLeft, pointCenter);

        lines.add(line1);
        lines.add(line2);
        lines.add(line3);
        lines.add(line4);
        lines.add(line5);
        lines.add(line6);
        lines.add(line7);
        lines.add(line8);

        MyTriangle triangle1 = new MyTriangle(pointUpperLeft, pointUpperRight, pointCenter);
        MyTriangle triangle2 = new MyTriangle(pointUpperLeft, pointLowerLeft, pointCenter);
        MyTriangle triangle3 = new MyTriangle(pointUpperRight, pointLowerRight, pointCenter);
        MyTriangle triangle4 = new MyTriangle(pointLowerLeft, pointLowerRight, pointCenter);

        triangles.add(triangle1);
        triangles.add(triangle2);
        triangles.add(triangle3);
        triangles.add(triangle4);
    }

    private void createTrianglesinTriangle(){
        ArrayList<MyTriangle> tmpTriangles = new ArrayList<>();

        for(MyTriangle triangle : triangles){

            MyPointF center = triangle.calcCenter();
            MyPointF point1 = triangle.getPoint1();
            MyPointF point2 = triangle.getPoint2();
            MyPointF point3 = triangle.getPoint3();

            points.add(center);
            points.add(point1);
            points.add(point2);
            points.add(point3);

            MyLine line1 = new MyLine(center, point1);
            MyLine line2 = new MyLine(center, point2);
            MyLine line3 = new MyLine(center, point3);

            lines.add(line1);
            lines.add(line2);
            lines.add(line3);

            MyTriangle triangle1 = new MyTriangle(center, point1, point2);
            MyTriangle triangle2 = new MyTriangle(center, point2, point3);
            MyTriangle triangle3 = new MyTriangle(center, point1, point3);

            tmpTriangles.add(triangle1);
            tmpTriangles.add(triangle2);
            tmpTriangles.add(triangle3);

        }

        triangles.clear();
        for(MyTriangle triangle : tmpTriangles){
            triangles.add(triangle);
        }

    }

    public static MyPointF getViewSize(View View){
        MyPointF point = new MyPointF(View.getWidth(), View.getHeight());
        return point;
    }


    /**
     * resetボタンが押されたときの処理
     */
    public void reset(){
        points.clear();
        lines.clear();
        triangles.clear();
        invalidate();
    }


    /**
     * getter and setter
     */
    public List<MyPointF> getPoints(){ return this.points; }

    public void setWidth(float width) { this.width = width; }

    public void setHeight(float height) { this.height = height; }

    public List<MyTriangle> getTriangles() { return triangles; }

    /**
     * Method
     * registerLineAndTriangle
     * searchMinLine
     * searchTriangleIncludingLine
     */


    /* -----------for custom input-----------
    //タッチに対するイベントハンドラ
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MyPointF currentPoint = new MyPointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dragCurrent = currentPoint;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                createField();
                points.add(currentPoint);
                invalidate();
                dragCurrent = null;
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                dragCurrent = currentPoint;
                invalidate();
                break;
        }

        return true;
    }

    //register line and triangle to list
    private void registerLineAndTriangle(MyPointF currentPoint) {
        Map<Double, MyLine> distMap = new HashMap<>();
        MyLine minLine = new MyLine();
        MyLine line1 = new MyLine();
        MyLine line2 = new MyLine();

        if (this.lines.isEmpty()) {
            assert this.points.size() == 1;
            MyLine line = new MyLine(currentPoint, this.points.get(0));
            lines.add(line);
        } else {
            int iteration_num = 3;
            distMap = createDistMap(currentPoint);
            Object[] mapKeys = distMap.keySet().toArray(); //参照を取得
            Arrays.sort(mapKeys);

            for (Double key : distMap.keySet()) {
                minLine = distMap.get(key);
                line1 = new MyLine(currentPoint, minLine.getPoint1());
                line2 = new MyLine(currentPoint, minLine.getPoint2());

                if (!isCrossLineExist(line1) && !isCrossLineExist(line2)) {
                    //register new line to list
                    this.lines.add(line1);
                    this.lines.add(line2);

                    //register new triangle to list
                    MyTriangle newTriangle = new MyTriangle(
                            currentPoint, minLine.getPoint1(), minLine.getPoint2()
                    );
                    this.triangles.add(newTriangle);

                    //search triangle including minLine
                    MyTriangle triangle = searchTriangleIncludeLine(minLine);

                    //register triangle and newTriangle to nextTriangles each other
                    if (triangle != null) {
                        triangle.getNextTriangles().add(newTriangle);
                        newTriangle.getNextTriangles().add(triangle);
                    }
                }

                counter = counter + 1;
                if (counter == distMap.size()) {
                    return;
                }

            }
        }
    }

    //search nearest line from current point
    private Map<Double, MyLine> createDistMap(MyPointF currentPoint){
        double dist;
        double minDist = INF;
        Map<Double,MyLine> distMap = new HashMap<>();

        for(MyLine line : this.lines) {
            dist = line.calcPointToLine(currentPoint);
            distMap.put(dist, line);
        }

        return distMap;

    }

    //search triangle including a line
    private MyTriangle searchTriangleIncludeLine(MyLine line){

        for(MyTriangle triangle : this.triangles){
            if(triangle.isExistLine(line) == true){
                return triangle;
            }
        }

        return null;
    }

    private boolean isCrossLineExist(MyLine judgedLine){
        for(MyLine line: this.lines){
            if(judgedLine.isCross(line)){
                return true;
            }
        }
        return false;
    }
    */


}
