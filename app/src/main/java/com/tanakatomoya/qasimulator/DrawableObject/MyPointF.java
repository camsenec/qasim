package com.tanakatomoya.qasimulator.DrawableObject;

import java.io.Serializable;

import static java.lang.StrictMath.abs;

/**
 * Serializableを実装した点を表現する自作クラス.
 * (PointFを継承すると, 座標x,yがオブジェクト並列化されなかったため, PointFの継承は行っていない)
 */

public class MyPointF implements Serializable{

    private final double EPS = 1e-3;

    private float x;
    private float y;

    public MyPointF() {

    }

    public MyPointF(float x, float y) {
        this.setX(x);
        this.setY(y);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;

        MyPointF other = (MyPointF)obj;
        if(Math.abs(x - other.getX()) < EPS && Math.abs(y - other.getY()) < EPS){
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "x=" + x +
                ", y=" + y +
                '}';
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
}

