package com.scanner.scanner.Utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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

    public static String fileUriToBase64(Uri imageUri, ContentResolver resolver){
        try {
            final InputStream imageStream = resolver.openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(b, Base64.DEFAULT);
        }catch (Exception e){
            Log.d("dataxx", "fileUriToBase64: "+e.getMessage());
            return "";
        }
    }
}
