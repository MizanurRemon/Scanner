package com.scanner.scanner.Views.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.scanner.scanner.R;
import com.scanner.scanner.Utils.Constants;
import com.scanner.scanner.Utils.Helpers;
import com.scanner.scanner.databinding.ActivityLoginBinding;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.logInButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));

        toggleController();

        binding.logInButton.setOnClickListener(v -> {
            if (userType.equals(getApplicationContext().getResources().getString(R.string.user))) {
                userLogin();
            } else {
                adminLogin();
            }

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
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

    private void adminLogin() {
        if (TextUtils.isEmpty(binding.phoneEditText.getText().toString().trim()) || TextUtils.isEmpty(binding.passwordEditText.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT).show();

        } else {
            Map<String, Object> body = new HashMap<>();
            body.put(Constants.EMAIL, binding.phoneEditText.getText().toString().trim());
            body.put(Constants.PASSWORD, binding.passwordEditText.getText().toString().trim());
            body.put(Constants.USERTYPE, userType);
            Log.d("dataxx", "ADMIN: " + body);
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