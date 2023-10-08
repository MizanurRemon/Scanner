package com.scanner.scanner.Views.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.scanner.scanner.Model.AuthResponse;
import com.scanner.scanner.R;
import com.scanner.scanner.Remote.Login.LoginViewModel;
import com.scanner.scanner.Sessions.SessionManagement;
import com.scanner.scanner.Utils.Constants;
import com.scanner.scanner.Utils.Helpers;
import com.scanner.scanner.databinding.ActivityLoginBinding;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String userType;
    private LoginViewModel loginViewModel;
    SessionManagement sessionManagement;
    Dialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initView();

        binding.logInButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));

        toggleController();

        binding.logInButton.setOnClickListener(v -> {
            if (userType.equals(getApplicationContext().getResources().getString(R.string.user))) {
                userLogin();
            } else {
                adminLogin();
            }

        });

        binding.phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Helpers.validatePhoneNumber(s.toString().trim()) || s.toString().trim().length() == 0) {
                    binding.phoneError.setVisibility(View.VISIBLE);
                    binding.logInButton.setEnabled(false);
                    binding.logInButton.setTextColor(getResources().getColor(R.color.gray));
                } else {
                    binding.phoneError.setVisibility(View.GONE);
                    binding.logInButton.setEnabled(true);
                    binding.logInButton.setTextColor(getResources().getColor(R.color.White));
                }
            }
        });


    }

    private void initView() {
        sessionManagement = new SessionManagement(getApplicationContext());
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        loader = new Dialog(LoginActivity.this);
        loader.setContentView(R.layout.loader);
        loader.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader.setCancelable(false);

    }

    private void adminLogin() {
        if (TextUtils.isEmpty(binding.phoneEditText.getText().toString().trim()) || TextUtils.isEmpty(binding.passwordEditText.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT).show();

        } else {
            loader.show();
            loginViewModel.adminLogin(binding.phoneEditText.getText().toString().trim(), binding.passwordEditText.getText().toString().trim()).observe(this, new Observer<AuthResponse>() {
                @Override
                public void onChanged(AuthResponse authResponse) {
                    loader.dismiss();
                    if (!authResponse.accessToken.isEmpty()) {
                        sessionManagement.saveToken(authResponse, "admin");

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "invalid credentials", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

    private void userLogin() {
        if (TextUtils.isEmpty(binding.phoneEditText.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT).show();

        } else {
            Map<String, Object> body = new HashMap<>();
            body.put(Constants.EMAIL, binding.phoneEditText.getText().toString().trim());
            body.put(Constants.USERTYPE, userType);
            Log.d("dataxx", "USER: " + body);
        }
    }

    private void toggleController() {
        if (binding.toggleGroup.getCheckedButtonId() == R.id.userButton) {
            binding.passwordLayout.setVisibility(View.GONE);
            userType = getApplicationContext().getResources().getString(R.string.user);
        } else if (binding.toggleGroup.getCheckedButtonId() == R.id.adminButton) {
            binding.passwordLayout.setVisibility(View.VISIBLE);
            userType = getApplicationContext().getResources().getString(R.string.admin);
        }
        binding.toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

            if (isChecked) {
                if (group.getCheckedButtonId() == R.id.userButton) {
                    userType = getApplicationContext().getResources().getString(R.string.user);
                    binding.passwordLayout.setVisibility(View.GONE);

                } else if (group.getCheckedButtonId() == R.id.adminButton) {
                    binding.passwordLayout.setVisibility(View.VISIBLE);
                    userType = getApplicationContext().getResources().getString(R.string.admin);
                }
            }


        });

    }
}