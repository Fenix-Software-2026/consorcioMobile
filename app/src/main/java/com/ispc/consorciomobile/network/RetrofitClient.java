package com.ispc.consorciomobile.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://gaston.alwaysdata.net/";
    // El token se actualiza después de iniciar sesión correctamente.
    private static String tokenActual = "";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Este interceptor agrega el token a las llamadas protegidas de la API.
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            // El login no necesita token; las demás llamadas sí lo reciben.
                            if (tokenActual.isEmpty()) {
                                return chain.proceed(chain.request());
                            }

                            Request request = chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer " + tokenActual)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void guardarToken(String token) {
        // Todas las llamadas futuras usarán el access token del usuario autenticado.
        tokenActual = token == null ? "" : token;
    }
}
