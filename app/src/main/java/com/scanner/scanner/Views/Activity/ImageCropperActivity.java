package com.scanner.scanner.Views.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.scanner.scanner.R;
import com.scanner.scanner.Utils.Constants;
import com.scanner.scanner.databinding.ActivityImageCropperBinding;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.view.CropImageView;

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
            Toast.makeText(getApplicationContext(), getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show();
            finish();
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
        options.setFreeStyleCropEnabled(true);
        options.setAspectRatioOptions(2,
                new AspectRatio("WOW", 1, 2),
                new AspectRatio("MUCH", 3, 4),
                new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
                new AspectRatio("SO", 16, 9),
                new AspectRatio("ASPECT", 1, 1));
        options.withAspectRatio(CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO);
        UCrop.of(fileUri, Uri.fromFile(new File(getCacheDir(), dest_uri)))
                .withOptions(options)
                .useSourceImageAspectRatio()
                .withMaxResultSize(2000, 2000)
                .start(ImageCropperActivity.this);

    }

    private void readIntent() {

        result = getIntent().getStringExtra(Constants.DATA);
        fileUri = Uri.parse(result);
    }
}