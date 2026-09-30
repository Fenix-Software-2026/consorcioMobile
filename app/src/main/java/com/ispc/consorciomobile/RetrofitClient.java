package com.ispc.consorciomobile;

import android.content.Context;
import android.content.SharedPreferences;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.Request;

public class RetrofitClient {

    private static Retrofit retrofit;

    private static final String BASE_URL = "https://gaston.alwaysdata.net/" ;
    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
             OkHttpClient client = new OkHttpClient.Builder()
                     .addInterceptor(chain -> {
                         SharedPreferences preferences = context.getApplicationContext()
                                 .getSharedPreferences("ConsorcioPrefs", Context.MODE_PRIVATE);
                         String token = preferences.getString("access_token", "");
                         Request originalRequest = chain.request();
                                 Request.Builder builder = originalRequest.newBuilder();
                         if (!token.isEmpty()){
                             builder.header("Authorization", "Bearer " + token);
                         }
                         return chain.proceed(builder.build());
                     }).build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    )
                    .build();
        }
        return retrofit;
    }


}
