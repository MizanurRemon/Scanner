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
import com.scanner.scanner.databinding.ActivityMainBinding;
import com.scanner.scanner.databinding.ActivityPdfViewerBinding;
import com.yalantis.ucrop.UCrop;

public class PdfViewerActivity extends AppCompatActivity {

    ActivityPdfViewerBinding binding;
    Uri fileUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.RESULT, fileUri + "");
            setResult(-1, returnIntent);

            finish();
        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable throwable = UCrop.getError(data);
            Toast.makeText(getApplicationContext(), getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfViewerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fileUri = getIntent().getData();
        binding.pdfView.fromUri(fileUri).load();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}