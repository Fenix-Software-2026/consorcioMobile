package com.ispc.consorciomobile;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET ("api/reclamos/")
    Call<Object> probarConexion();


}
