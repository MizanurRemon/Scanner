package com.scanner.scanner.Network.Private;

import static com.scanner.scanner.Sessions.SessionManagement.getAccessToken;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scanner.scanner.Sessions.SessionManagement;
import com.scanner.scanner.Utils.URLConstants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrivateRetrofitConfig {
    public static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .addInterceptor(chain -> {

                Request newRequest = chain.request().newBuilder()
                        .header("Authorization", "Bearer "+getAccessToken())
                        .build();

                return chain.proceed(newRequest);
            })
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
