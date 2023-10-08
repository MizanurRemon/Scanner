package com.scanner.scanner.Remote.Login;

import androidx.lifecycle.MutableLiveData;

import com.scanner.scanner.Model.AuthResponse;
import com.scanner.scanner.Network.Public.PublicApiService;
import com.scanner.scanner.Network.Public.PublicApiUtilize;
import com.scanner.scanner.Utils.Constants;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepositories {
    private static LoginRepositories loginRepositories;
    private PublicApiService apiService;

    MutableLiveData<AuthResponse> authResponse;


    public LoginRepositories() {
        this.apiService = PublicApiUtilize.publicApiService();
        authResponse = new MutableLiveData<>();
    }

    public synchronized static LoginRepositories getInstance() {
        if (loginRepositories == null) {
            return new LoginRepositories();
        }

        return loginRepositories;
    }

    MutableLiveData<AuthResponse> adminLogin(String phone, String password) {

        Map<String, Object> body = new HashMap<>();
        body.put(Constants.MOBILE_NO, phone);
        body.put(Constants.PASSWORD, password);

        Call<AuthResponse> call = apiService.adminLogin(body);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    authResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

            }
        });

        return authResponse;
    }
}
