package com.tanakatomoya.qasimulator.Retrofit;

import com.tanakatomoya.qasimulator.Model.SpinGlassModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface QASimulatorAPI {
    @FormUrlEncoded
    @POST("register.php")
    Call<SpinGlassModel> createSpinGlassModel(@Field("name") String name,
                                              @Field("trotter_num") int trotter_num,
                                              @Field("site_num") int site_num);
}
