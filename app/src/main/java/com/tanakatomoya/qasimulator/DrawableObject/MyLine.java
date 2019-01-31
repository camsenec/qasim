package com.tanakatomoya.qasimulator.DrawableObject;

import android.os.Parcelable;

import java.io.Serializable;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MyLine implements Serializable{
    private final double EPS = 1e-1;
    private MyPointF point1;
    private MyPointF point2;
    double c1;
    double c2;
    double c3;

    //expression of line : c1*x + c2*y +c3 = 0
    public MyLine(MyPointF point1, MyPointF point2) {
        this.point1 = point1;
        this.point2 = point2;
        this.c1 = (point1.getY() - point2.getY()) / (point1.getX() - point2.getX());
        this.c2 = -1.0;
        this.c3 = point1.getY() - c1 * point1.getX();
    }


    //Default Constructor
    public MyLine() {}

    //calculate distance between point and line
    public double calcPointToLine(MyPointF point){
        double dist;

        dist = Math.abs((c1 * point.getX() + c2 * point.getY() + c3)) / Math.sqrt(c1*c1 + c2*c2);

        System.out.println(dist);
        return dist;

    }

    //相手の線(line)が自分の線と交わりを持つかを判断する
    public boolean isCross(MyLine line){
        double x;
        double y;

        x = -(line.c3 - this.c3) / (line.c1 - this.c1);
        y = line.c1 * x + line.c3;


        double minX = min(this.getPoint1().getX(), this.getPoint2().getX()) + EPS;
        double maxX = max(this.getPoint1().getX(), this.getPoint2().getX()) - EPS;
        double minY = min(this.getPoint1().getY(), this.getPoint2().getY()) + EPS;
        double maxY = max(this.getPoint1().getY(), this.getPoint2().getY()) - EPS;

        //System.out.println(minX +" "+ maxX +" "+ minY + " " + maxY);


        if((minX <= x && x <= maxX) && (minY < y && y < maxY)){
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "MyLine{" +
                "point1=" + point1.toString() +
                ", point2=" + point2.toString() +
                '}';
    }

    /**
     * getter and setter
     */
    public MyPointF getPoint1() {
        return point1;
    }

    public MyPointF getPoint2() {
        return point2;
    }



}
