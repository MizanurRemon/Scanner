package com.scanner.scanner.Views.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.scanner.scanner.R;
import com.scanner.scanner.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        toggleController();


    }

    private void toggleController() {
        if (binding.toggleGroup.getCheckedButtonId() == R.id.userButton) {
            binding.passwordLayout.setVisibility(View.GONE);
            userType = getApplicationContext().getResources().getString(R.string.user);
        } else if (binding.toggleGroup.getCheckedButtonId() == R.id.adminButton) {
            binding.passwordLayout.setVisibility(View.VISIBLE);
            userType = getApplicationContext().getResources().getString(R.string.admin);
        }
        Toast.makeText(LoginActivity.this, userType, Toast.LENGTH_SHORT).show();
        binding.toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

               if(isChecked){
                   if (group.getCheckedButtonId() == R.id.userButton) {
                       userType = getApplicationContext().getResources().getString(R.string.user);
                       binding.passwordLayout.setVisibility(View.GONE);
                       Toast.makeText(LoginActivity.this, userType, Toast.LENGTH_SHORT).show();

                   } else if (group.getCheckedButtonId() == R.id.adminButton) {
                       binding.passwordLayout.setVisibility(View.VISIBLE);
                       userType = getApplicationContext().getResources().getString(R.string.admin);
                       Toast.makeText(LoginActivity.this, userType, Toast.LENGTH_SHORT).show();
                   }
               }



            }
        });

    }
}