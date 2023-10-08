package com.scanner.scanner.Views.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static androidx.navigation.Navigation.findNavController;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.scanner.scanner.R;
import com.scanner.scanner.Sessions.SessionManagement;
import com.scanner.scanner.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initView();

        configureMenu();

        getPermission();

        setAppVersion();

    }

    @SuppressLint("SetTextI18n")
    private void setAppVersion() {

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            binding.versionText.setText(getResources().getString(R.string.version) + ": " + packageInfo.versionName + "+" + String.valueOf(packageInfo.versionCode));
        } catch (Exception ignored) {

        }

    }

    private void getPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //  Manifest.permission.POST_NOTIFICATIONS,
                //Manifest.permission.READ_MEDIA_IMAGES
        }, 1);

    }

    private void configureMenu() {
        binding.navigationView.getMenu().getItem(0).setChecked(true);
        binding.navigationView.getMenu().getItem(1).setVisible(false);
    }

    private void initView() {

        sessionManagement = new SessionManagement(getApplicationContext());
        binding.navigationView.bringToFront();
        binding.navigationView.setNavigationItemSelectedListener(this);
        binding.navigationView.inflateHeaderView(R.layout.menu_header);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //  Toast.makeText(this, sessionManagement.getAccessToken()+" "+sessionManagement.getUserType(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {
            findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.fragment_home);
        } else if (item.getItemId() == R.id.nav_profile) {
            findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.fragment_profile);
        } else if (item.getItemId() == R.id.nav_logout) {

            sessionManagement.removeUser();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else if (item.getItemId() == R.id.nav_reset_password) {
            findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.fragment_reset_password);
        } else if (item.getItemId() == R.id.nav_upload) {
            findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.fragment_image_upload);
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}