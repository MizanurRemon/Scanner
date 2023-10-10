package com.scanner.scanner.Views.Fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.scanner.scanner.Utils.Constants.IMAGE_REQ_CODE;
import static com.scanner.scanner.Utils.Helpers.getFileExtension;
import static com.scanner.scanner.Utils.Helpers.getFileName;
import static com.scanner.scanner.Utils.Helpers.saveImageToGallery;
import static com.scanner.scanner.Utils.Helpers.uriToBase64;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.scanner.scanner.Adapter.FileAdapter;
import com.scanner.scanner.Model.CommonResponse;
import com.scanner.scanner.Model.FileResponse;
import com.scanner.scanner.R;
import com.scanner.scanner.Remote.FileUpload.FileUploadViewModel;
import com.scanner.scanner.Sessions.SessionManagement;
import com.scanner.scanner.Utils.Constants;
import com.scanner.scanner.Views.Activity.ImageCropperActivity;
import com.scanner.scanner.Views.Activity.LoginActivity;
import com.scanner.scanner.databinding.FragmentFileUploadBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FileUploadFragment extends Fragment implements FileAdapter.OnImageItemClickListener, FileAdapter.OnImageDeleteClickListener {


    FragmentFileUploadBinding binding;

    private Uri filepath;
    ActivityResultLauncher<String> galleryContent;
    ActivityResultLauncher<Intent> cameraContent;

    ActivityResultLauncher<String> filePickContent;

    FileAdapter imageAdapter;

    List<FileResponse> fileList = new ArrayList<>();

    FileUploadViewModel fileUploadViewModel;
    SessionManagement sessionManagement;

    Dialog loader;

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK && requestCode == IMAGE_REQ_CODE) {

                Bundle bundle = data.getExtras();
                Bitmap imageBitmap = (Bitmap) bundle.get("data");

                Uri uri = saveImageToGallery(imageBitmap);

                if (uri != null) {
                    String str = uriToBase64(uri, getActivity());
                    String ext = getFileExtension(getActivity(), uri);
                    fileList.add(new FileResponse(getFileName(uri, getActivity()), str, ext));

                    setFileAdapter(fileList);

                    Toast.makeText(getActivity(), getFileName(uri, getActivity()) + " file picked", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), " file not picked", Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == RESULT_OK && requestCode == 12) {

                if (data != null) {
                    Uri uri = data.getData();

                    String str = uriToBase64(data.getData(), getActivity());
                    String ext = getFileExtension(getActivity(), uri);

                    fileList.add(new FileResponse(getFileName(uri, getActivity()), str, ext));

                    setFileAdapter(fileList);

                    Toast.makeText(getActivity(), getFileName(uri, getActivity()) + " file picked", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "file not picked", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    private void setImage(Uri uri) {

    }

    private void setFileAdapter(List<FileResponse> fileList) {
        imageAdapter = new FileAdapter(fileList);
        imageAdapter.setOnItemClickListener(FileUploadFragment.this::onImageItemClick, FileUploadFragment.this::onDeleteImageClick);
        binding.imageListView.setAdapter(imageAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFileUploadBinding.inflate(getLayoutInflater());
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
                    //  openPdfViewer(result);
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

                if (fileList.isEmpty() || binding.invoiceNoText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "missing parameter", Toast.LENGTH_SHORT).show();
                } else {

                    Map<String, Object> body = new HashMap<>();
                    body.put(Constants.MOBILE_NO, sessionManagement.getPhone());
                    body.put(Constants.INVOICE_DATE, binding.dateText.getText().toString().trim());
                    body.put(Constants.INVOICE_NO, binding.invoiceNoText.getText().toString().trim());
                    body.put(Constants.FILES, fileList);

                    loader.show();
                    fileUploadViewModel.uploadInvoice(body, getActivity()).observe(getViewLifecycleOwner(), new Observer<CommonResponse>() {
                        @Override
                        public void onChanged(CommonResponse commonResponse) {
                            loader.dismiss();

                            Toast.makeText(getActivity(), commonResponse.statusCode, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
        setCurrentDate(formatter.format(new Date()));

        binding.dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });


        return view;
    }

    public void setCurrentDate(String date) {
        binding.dateText.setText(date);
    }

    private void selectPdf() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), 12);
    }

    private void initView(View view) {
        sessionManagement = new SessionManagement(getActivity());
        fileUploadViewModel = new ViewModelProvider(getActivity()).get(FileUploadViewModel.class);
        binding.imageListView.setHasFixedSize(true);
        binding.imageListView.setLayoutManager(new GridLayoutManager(getActivity(), 1));


        loader = new Dialog(getActivity());
        loader.setContentView(R.layout.loader);
        loader.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader.setCancelable(false);

    }

    private void imageSelect() {
        //final CharSequence[] items = {"Camera", "Gallery", "File", "Cancel"};
        final CharSequence[] items = {"Camera", "File", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (items[i].equals("Camera")) {

                    /*ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                    filepath = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filepath);*/

                    // cameraContent.launch(cameraIntent);
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, IMAGE_REQ_CODE);

                } else if (items[i].equals("Gallery")) {

                    galleryContent.launch("image/*");

                } else if (items[i].equals("File")) {
                    // filePickContent.launch("application/*");
                    selectPdf();
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

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onDeleteImageClick(int position) {

    }

    private void pickDate() {
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);

                setCurrentDate(sdf.format(myCalendar.getTime()));

            }

        };

        new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}