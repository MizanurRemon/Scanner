package com.scanner.scanner.Utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Helpers {

    public static Boolean validatePhoneNumber(String phone) {

        String REGEX = "^(?:\\+8801|8801|01)[^012]{1}[0-9]{8}$";

        return phone.matches(REGEX);
    }

    public static String fileImageUriToBase64(Uri imageUri, ContentResolver resolver) {
        Log.d("dataxx", "fileUriToBase64: ");
        try {
            final InputStream imageStream = resolver.openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();

            Log.d("dataxx", "fileUriToBase64: " + Base64.encodeToString(b, Base64.DEFAULT));
            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            Log.d("dataxx", "ERORXX: " + e.getMessage());
            return "";
        }
    }


    public static String uriToBase64(Uri uri, Context context) {

        if (getFileExtension(context, uri).equals(Constants.PDF)) {
            return filePdfToBase64(uri, context);
        } else {
            return fileImageUriToBase64(uri, context.getContentResolver());
        }
    }

    private static String filePdfToBase64(Uri uri, Context context) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getFileExtension(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Get the file extension from the URI
        String extension;

        if ("content".equals(uri.getScheme())) {
            // If the URI is a content URI (e.g., from the Gallery or Downloads), use ContentResolver
            extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        } else {
            // If the URI is a file URI, extract the extension from the file path
            extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        }

        return extension;
    }

    public static Uri saveImageToGallery(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(imageFile);
    }

    @SuppressLint("Range")
    public static String getFileName(Uri uri, Context context) {
        String displayName = null;
        String uriString = uri.toString();
        File myFile = new File(uriString);

        if (uriString.startsWith("content://")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                Log.d("dataxx", "ERRORXX: " + e.getMessage());
            }
        } else if (uriString.startsWith("file://")) {
            displayName = myFile.getName();
        }

        return displayName;
    }

}
