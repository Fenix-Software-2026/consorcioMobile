package com.ispc.consorciomobile.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = TokenManager.obtenerToken();

        // El login no necesita token; las demás llamadas sí lo reciben.
        if (token == null || token.isEmpty()) {
            return chain.proceed(chain.request());
        }

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(request);
    }
}