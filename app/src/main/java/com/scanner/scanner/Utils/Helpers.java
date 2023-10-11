package com.scanner.scanner.Utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.scanner.scanner.Model.GetFilePathAndStatus;
import com.scanner.scanner.R;

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
        // Log.d("dataxx", "fileUriToBase64: ");
        try {
            final InputStream imageStream = resolver.openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();

            // Log.d("dataxx", "fileUriToBase64: " + Base64.encodeToString(b, Base64.DEFAULT));
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

    public static Bitmap getImageFromString(String imgString) {

        byte[] imageBytes = Base64.decode(imgString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

    }

    public static void convertPdfStringToPDF(String imgString, Context context, String fileName) {
        try {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);

            if (!file.exists()) {
                byte[] pdfAsBytes = Base64.decode(imgString, 0);
                FileOutputStream os = new FileOutputStream(file, false);
                os.write(pdfAsBytes);
                os.flush();
                os.close();

                Toast.makeText(context, "file  saved at " + Environment.DIRECTORY_DOCUMENTS, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "file already  saved at " + Environment.DIRECTORY_DOCUMENTS, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("dataxx", "convertStringToPDF: " + e.getMessage());
        }
    }

    public static GetFilePathAndStatus getFileFromBase64AndSaveInSDCard(String base64, String filename, String extension) {
        filename = filename.replace(".", "");
        GetFilePathAndStatus getFilePathAndStatus = new GetFilePathAndStatus();
        try {
            byte[] pdfAsBytes = Base64.decode(base64, 0);
            FileOutputStream os = new FileOutputStream(getReportPath(filename, extension), false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
            getFilePathAndStatus.filStatus = true;
            getFilePathAndStatus.filePath = getReportPath(filename, extension);
            return getFilePathAndStatus;
        } catch (Exception e) {
            getFilePathAndStatus.filStatus = false;
            getFilePathAndStatus.filePath = getReportPath(filename, extension);
            return getFilePathAndStatus;
        }
    }

    public static String getReportPath(String filename, String extension) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "ParentFolder/Report");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + filename + "." + extension);
        return uriSting;
    }


    public static void convertImageStringToPDF(String imgString, Context context, String fileName) {
        try {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), removeImageExtension(fileName));

            if (!file.exists()) {
                byte[] imageBytes = Base64.decode(imgString, Base64.DEFAULT);
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                PdfDocument document = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(imageBitmap.getWidth(), imageBitmap.getHeight(), 1).create();

                PdfDocument.Page page = document.startPage(pageInfo);

                imageBitmap = Bitmap.createScaledBitmap(imageBitmap, pageInfo.getPageWidth(), pageInfo.getPageHeight(), true);
                page.getCanvas().drawBitmap(imageBitmap, 0, 0, null);

                document.finishPage(page);


                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    document.writeTo(fos);
                    document.close();
                    fos.close();
                    Toast.makeText(context, "file  saved at " + Environment.DIRECTORY_DOCUMENTS, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d("dataxx", "convertStringToPDF: " + e.getMessage());
                }


            } else {
                Toast.makeText(context, "file already  saved at " + Environment.DIRECTORY_DOCUMENTS, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("dataxx", "convertStringToPDF: " + e.getMessage());
        }
    }


    private static String removeImageExtension(String originalString) {
        int dotIndex = originalString.lastIndexOf('.');
        String newExtension = ".pdf";

        if (dotIndex != -1) {
            // Extract the part of the string before the dot
            String prefix = originalString.substring(0, dotIndex);

            // Create the new string by concatenating the prefix and the new extension
            String newString = prefix + newExtension;

            // Output the new string
            return newString;
        } else {
            // Handle the case where there is no dot in the original string
            System.out.println("Invalid file name");

            return "";
        }
    }


}
