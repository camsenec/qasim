package com.tanakatomoya.qasimulator.Model;

public class SpinGlassModel {
    String name;
    int trotter_num;
    int site_num;
    float result;
    String resultUrl;


    public SpinGlassModel(String name, int trotter_num, int site_num, float result, String resultUrl) {
        this.name = name;
        this.trotter_num = trotter_num;
        this.site_num = site_num;
        this.result = result;
        this.resultUrl = resultUrl;
    }

    public String getName() {
        return name;
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

    public float getResult() { return result; }

    public void setResult(float result) { this.result = result; }

    public String getResultUrl() { return resultUrl; }

    public void setResultUrl(String resultUrl) { this.resultUrl = resultUrl; }
}
