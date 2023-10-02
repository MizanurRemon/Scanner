package com.scanner.scanner.Views.Fragments;

import static com.scanner.scanner.Utils.Constants.IMAGE_REQ_CODE;
import static com.scanner.scanner.Utils.Constants.RESULT;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scanner.scanner.Adapter.ImageAdapter;
import com.scanner.scanner.Utils.Constants;
import com.scanner.scanner.Views.Activity.ImageCropperActivity;
import com.scanner.scanner.databinding.FragmentImageUploadBinding;

import java.util.ArrayList;
import java.util.List;

public class ImageUploadFragment extends Fragment {


    FragmentImageUploadBinding binding;
    Bitmap bitmap;
    private Uri filepath;
    ActivityResultLauncher<String> mGetContent;
    ActivityResultLauncher<Intent> cameraContent;

    String yourCameraOutputFilePath;

    List<Uri> imageList = new ArrayList<>();
    ImageAdapter imageAdapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1 && requestCode == IMAGE_REQ_CODE) {
            String result = data.getStringExtra(RESULT);
            if (result != null) {
                filepath = Uri.parse(result);
            }

            imageList.add(filepath);

            setImageAdapter(imageList);

            binding.imageView.setImageURI(imageList.get(imageList.size() - 1));
        }

    }

    private void setImageAdapter(List<Uri> imageList) {
        imageAdapter = new ImageAdapter(imageList);
        binding.imageListView.setAdapter(imageAdapter);
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
            if (result != null) {
                Intent intent = new Intent(getActivity(), ImageCropperActivity.class);
                intent.putExtra(Constants.DATA, result.toString());
                startActivityForResult(intent, IMAGE_REQ_CODE);
            }
        });

        cameraContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (filepath != null) {
                        Intent intent = new Intent(getActivity(), ImageCropperActivity.class);
                        intent.putExtra(Constants.DATA, filepath.toString());
                        startActivityForResult(intent, IMAGE_REQ_CODE);
                    }
                }
        );


        return view;
    }

    private void clearData() {
        filepath = null;
        binding.imageView.setImageURI(null);
    }

    private void initView(View view) {
        binding.imageListView.setHasFixedSize(true);
        binding.imageListView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("dataxx", "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("dataxx", "onDetach: ");
    }
}