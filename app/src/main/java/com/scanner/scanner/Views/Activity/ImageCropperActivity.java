package com.scanner.scanner.Views.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.scanner.scanner.Utils.Constants;
import com.scanner.scanner.databinding.ActivityImageCropperBinding;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;


public class ImageCropperActivity extends AppCompatActivity {

    ActivityImageCropperBinding binding;
    String result;
    Uri fileUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.RESULT, resultUri + "");
            setResult(-1, returnIntent);

            finish();
        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable throwable = UCrop.getError(data);
            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageCropperBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        readIntent();

        String dest_uri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

        UCrop.Options options = new UCrop.Options();
        UCrop.of(fileUri, Uri.fromFile(new File(getCacheDir(), dest_uri)))
                .withOptions(options)
                .withAspectRatio(0, 0)
                .useSourceImageAspectRatio()
                .withMaxResultSize(2000, 2000)
                .start(ImageCropperActivity.this);

    }

    private void readIntent() {

        result = getIntent().getStringExtra(Constants.DATA);
        fileUri = Uri.parse(result);
    }
}