package com.scanner.scanner.Remote.Login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.scanner.scanner.Model.AuthResponse;

public class LoginViewModel extends ViewModel {
    public LiveData<AuthResponse> adminLogin(String phone, String password) {
        return LoginRepositories.getInstance().adminLogin(phone, password);
    }
}
