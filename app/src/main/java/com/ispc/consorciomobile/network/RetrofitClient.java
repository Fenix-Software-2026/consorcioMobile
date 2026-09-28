package com.ispc.consorciomobile;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://gaston.alwaysdata.net/" ;
    // PEGA AQUÍ TU TOKEN DE POSTMAN
    private static final String TOKEN_TEMPORAL ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzkwNjQwMzYxLCJpYXQiOjE3OTA2MzY3NjEsImp0aSI6IjZiN2IyYjliNzljZDRhYzJiYTY1NTJkZjk0NDllODcxIiwidXNlcl9pZCI6IjQiLCJyb2wiOiJhZG1pbmlzdHJhZG9yIiwidXNlcm5hbWUiOiJhZG1pbkNvbnNvciJ9.IF8bR1LC8X8mhIW_H2c2S-QU2iFiqL8H_5HEGQu__1E";
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Este interceptor inyecta el token en el Header "Authorization"
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + TOKEN_TEMPORAL)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
