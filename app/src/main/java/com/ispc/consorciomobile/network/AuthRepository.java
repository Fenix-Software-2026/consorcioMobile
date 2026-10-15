package com.ispc.consorciomobile.network;

import android.util.Base64;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final ApiService apiService;

    public interface LoginCallback {
        void onSuccess(String rol, String username);
        void onErrorCredenciales();
        void onErrorTokenInvalido();
        void onErrorConexion();
    }

    public AuthRepository() {
        this.apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);
    }

    public void login(String username, String password, LoginCallback callback) {
        apiService.iniciarSesion(new LoginRequest(username, password))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (!response.isSuccessful() || response.body() == null) {
                            callback.onErrorCredenciales();
                            return;
                        }

                        try {
                            LoginResponse loginResponse = response.body();

                            JSONObject datosToken = decodificarPayload(loginResponse.getAccess());
                            String usernameToken = datosToken.optString("username");
                            String rol = datosToken.optString("rol");

                            // Guardar el access token
                            TokenManager.guardarToken(loginResponse.getAccess());
                            TokenManager.guardarUsername(username)
                            ;
                            callback.onSuccess(rol, usernameToken);

                        } catch (Exception exception) {
                            callback.onErrorTokenInvalido();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        callback.onErrorConexion();
                    }
                });
    }

    private JSONObject decodificarPayload(String token) throws Exception {
        String[] partes = token.split("\\.");

        if (partes.length != 3) {
            throw new IllegalArgumentException("JWT inválido");
        }

        byte[] payload = Base64.decode(
                partes[1],
                Base64.URL_SAFE | Base64.NO_WRAP
        );

        return new JSONObject(
                new String(payload, StandardCharsets.UTF_8)
        );
    }
}