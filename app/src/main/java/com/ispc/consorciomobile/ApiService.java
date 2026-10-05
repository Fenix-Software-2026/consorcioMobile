package com.ispc.consorciomobile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface ApiService {
    @GET ("api/reclamos/")
    Call<Object> probarConexion();

    @POST ("api/login/")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("api/usuario/mi_perfil/")
    Call<PerfilResidente> obtenerMiPerfil();

    @GET("api/reclamos/")
    Call<List<Reclamo>> obtenerReclamos();

    @GET("api/comunicados/")
    Call<List<Comunicado>> obtenerComunicados();
}
