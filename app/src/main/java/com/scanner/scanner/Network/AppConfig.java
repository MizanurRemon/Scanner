package com.scanner.scanner.Network;

import com.scanner.scanner.Utils.URLConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig {
    public static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build();

    public static Retrofit getRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(URLConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
