package com.ispc.consorciomobile.network;

import com.ispc.consorciomobile.Comunicado;
import com.ispc.consorciomobile.PerfilResidente;
import com.ispc.consorciomobile.model.Reclamo;
import com.ispc.consorciomobile.model.Residente;
import com.ispc.consorciomobile.model.Unidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import java.util.Map;
import retrofit2.http.Header;

public interface ApiService {

    @GET("api/reclamos/")
    Call<List<Reclamo>> probarConexion();

    @POST("api/reclamos/")
    Call<Reclamo> crearReclamo(@Body Reclamo reclamo);

    @PATCH("api/reclamos/{id}/")
    Call<Reclamo> actualizarEstadoReclamo(
            @Path("id") int id,
            @Body Map<String, String> body
    );

    @POST("api/login/")
    Call<LoginResponse> iniciarSesion(@Body LoginRequest request);

    @GET("api/usuario/mi_perfil/")
    Call<PerfilResidente> obtenerMiPerfil();

    @GET("api/usuario/")
    Call<List<Residente>> obtenerResidentes();

    @GET("api/unidad/")
    Call<List<Unidad>> obtenerUnidades();

    @POST("api/usuario/")
    Call<Residente> crearResidente(@Body Residente residente);

    @GET("api/reclamos/")
    Call<List<Reclamo>> obtenerReclamos();

    @GET("api/comunicados/")
    Call<List<Comunicado>> obtenerComunicados();
}