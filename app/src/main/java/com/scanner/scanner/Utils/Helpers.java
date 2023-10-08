package com.scanner.scanner.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Helpers {
    public static String convertImageToString() {
        String imgToString = "";

        return imgToString;
    }

    public static Boolean validatePhoneNumber(String phone) {

        String REGEX = "^(?:\\+8801|8801|01)[^012]{1}[0-9]{8}$";

        return phone.matches(REGEX);
    }

    public static String fileUriToBase64(Uri imageUri, ContentResolver resolver) {
        try {
            final InputStream imageStream = resolver.openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
        }
        catch (Exception e1 ) {
            Log.d("dataxx", "getStringFile: "+ e1.toString());
        }

        lastVal = encodedFile;
        return lastVal;
    }

    public static String convertPdfToBase64(Uri pdfUri, Context context) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(pdfUri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
