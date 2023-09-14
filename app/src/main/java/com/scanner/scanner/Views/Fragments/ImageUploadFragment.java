package com.scanner.scanner.Views.Fragments;

import static com.scanner.scanner.Utils.Constants.CAMERA_REQUEST;
import static com.scanner.scanner.Utils.Constants.GALLERY_IMAGE_REQ_CODE;
import static com.scanner.scanner.Utils.Constants.RESULT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.constant.ImageProvider;
import com.github.dhaval2404.imagepicker.listener.DismissListener;
import com.scanner.scanner.R;
import com.scanner.scanner.Utils.Constants;
import com.scanner.scanner.Views.Activity.ImageCropperActivity;
import com.scanner.scanner.databinding.FragmentImageUploadBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ImageUploadFragment extends Fragment {


    FragmentImageUploadBinding binding;
    Bitmap bitmap;
    private Uri filepath;
    ActivityResultLauncher<String> mGetContent;
    ActivityResultLauncher<Intent> cameraContent;

    String yourCameraOutputFilePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1 && requestCode == GALLERY_IMAGE_REQ_CODE) {
            String result = data.getStringExtra(RESULT);
            if (result != null) {
                filepath = Uri.parse(result);
            }

            binding.imageView.setImageURI(filepath);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentImageUploadBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        initView(view);


        binding.addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageSelect();
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(getActivity(), ImageCropperActivity.class);
            intent.putExtra(Constants.DATA, result.toString());
            startActivityForResult(intent, GALLERY_IMAGE_REQ_CODE);
        });

        cameraContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    Intent intent = new Intent(getActivity(), ImageCropperActivity.class);
                    intent.putExtra(Constants.DATA, filepath.toString());
                    startActivityForResult(intent, GALLERY_IMAGE_REQ_CODE);
                }
        );


        return view;
    }

    private void initView(View view) {
    }

    private void imageSelect() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (items[i].equals("Camera")) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                    filepath = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filepath);

                    cameraContent.launch(cameraIntent);

                } else if (items[i].equals("Gallery")) {

                    mGetContent.launch("image/*");

                } else if (items[i].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create a file to save the image
            File photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(requireActivity(),
                        "your.file.provider.authority", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                cameraContent.launch(cameraIntent);
            }
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            yourCameraOutputFilePath = imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }

}