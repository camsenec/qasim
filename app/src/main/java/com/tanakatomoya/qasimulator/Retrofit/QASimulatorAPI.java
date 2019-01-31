package com.tanakatomoya.qasimulator.Retrofit;

import com.tanakatomoya.qasimulator.Model.SpinGlassModel;

import java.io.File;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

//multipart/form-data形式でPOST
public interface QASimulatorAPI {
    @Multipart
    @POST("solve/")
    Call<SpinGlassModel> createSpinGlassModel(@Part("name") String name,
                                              @Part("trotter_num") int trotter_num,
                                              @Part("site_num") int site_num,
                                              @Part("result") float result,
                                              @Part("data") File file);
}
