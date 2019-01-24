package com.tanakatomoya.qasimulator.Model;

public class SpinGlassModel {
    String name;
    int trotter_num;
    int site_num;
    String error_msg;


    public SpinGlassModel(String name, int trotter_num, int site_num) {
        this.name = name;
        this.trotter_num = trotter_num;
        this.site_num = site_num;
    }

    public String getName() {
        return name;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTrotter_num() {
        return trotter_num;
    }

    public void setTrotter_num(int trotter_num) {
        this.trotter_num = trotter_num;
    }

    public int getSite_num() {
        return site_num;
    }

    public void setSite_num(int site_num) {
        this.site_num = site_num;
    }
}
