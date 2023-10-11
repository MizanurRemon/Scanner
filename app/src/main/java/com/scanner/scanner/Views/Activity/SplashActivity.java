package com.scanner.scanner.Views.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.scanner.scanner.R;
import com.scanner.scanner.Sessions.SessionManagement;
import com.scanner.scanner.databinding.ActivityLoginBinding;
import com.scanner.scanner.databinding.ActivitySplashBinding;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1500;
    private ActivitySplashBinding binding;
    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initView();

        new Handler().postDelayed(() -> {
            //Toast.makeText(this, sessionManagement.getUserType()+" "+sessionManagement.getAccessToken(), Toast.LENGTH_SHORT).show();
            if (!sessionManagement.getAccessToken().equals("-1")) {

                if (sessionManagement.getUserType().equals("admin")) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {

                }




            } else {
               startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void initView() {
        sessionManagement = new SessionManagement(getApplicationContext());
    }
}