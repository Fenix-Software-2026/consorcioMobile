package com.ispc.consorciomobile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET ("api/reclamos/")
    Call<Object> probarConexion();

    @POST ("api/login/")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("api/usuario/mi_perfil/")
    Call<PerfilResidente> obtenerMiPerfil();
}
