package com.scanner.scanner.Views.Fragments;

import static com.scanner.scanner.Utils.Constants.IMAGE_REQ_CODE;
import static com.scanner.scanner.Utils.Constants.RESULT;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.scanner.scanner.Adapter.ImageAdapter;
import com.scanner.scanner.R;
import com.scanner.scanner.Utils.Constants;
import com.scanner.scanner.Utils.Helpers;
import com.scanner.scanner.Views.Activity.ImageCropperActivity;
import com.scanner.scanner.Views.Activity.PdfViewerActivity;
import com.scanner.scanner.databinding.FragmentImageUploadBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageUploadFragment extends Fragment implements ImageAdapter.OnImageItemClickListener, ImageAdapter.OnImageDeleteClickListener {


    FragmentImageUploadBinding binding;
    Bitmap bitmap;
    private Uri filepath;
    ActivityResultLauncher<String> galleryContent;
    ActivityResultLauncher<Intent> cameraContent;

    ActivityResultLauncher<String> filePickContent;

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

            String ext = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(filepath.getPath())).toString());
            Log.d("dataxx", "onActivityResult: "+ext);

            imageList.add(filepath);

            setImageAdapter(imageList);

            setImage(imageList.get(imageList.size() - 1));

        }

    }

    private void setImage(Uri uri) {
        binding.imageView.setImageURI(uri);
    }

    private void setImageAdapter(List<Uri> imageList) {
        imageAdapter = new ImageAdapter(imageList);
        imageAdapter.setOnItemClickListener(ImageUploadFragment.this::onImageItemClick, ImageUploadFragment.this::onDeleteImageClick);
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

        filePickContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    openPdfViewer(result);

                }
            }
        });

        galleryContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
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

        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageList.isEmpty()) {
                    Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < imageList.size(); i++) {

                        Log.d("dataxx", "onClick: " + Helpers.fileUriToBase64(imageList.get(i), getActivity().getContentResolver()));

                    }
                }
            }
        });


        return view;
    }

    private void openPdfViewer(Uri result) {
       // Log.d("filexx", "openPdfViewer: "+Helpers.pdfToString(new File(result.getPath())));
        imageList.add(result);
        setImageAdapter(imageList);
    }

    private void clearData() {
        filepath = null;
        binding.imageView.setImageURI(null);
    }

    private void initView(View view) {
        binding.imageListView.setHasFixedSize(true);
        binding.imageListView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
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

                    galleryContent.launch("image/*");

                } else if (items[i].equals("PDF")) {
                    filePickContent.launch("application/*");
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

    @Override
    public void onImageItemClick(int position) {
        Uri uri = imageList.get(position);
        setImage(uri);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onDeleteImageClick(int position) {
        imageList.remove(position);

        if (imageList.size() > 0) {
            setImage(imageList.get(imageList.size() - 1));
        } else {
            binding.imageView.setImageDrawable(getActivity().getDrawable(R.drawable.ic_no_image));
        }

        setImageAdapter(imageList);
    }
}