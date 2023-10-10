package com.scanner.scanner.Remote.FileUpload;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.scanner.scanner.Model.CommonResponse;

import java.util.Map;

public class FileUploadViewModel extends ViewModel {

    public LiveData<CommonResponse> uploadInvoice(Map<String, Object> body, Context context) {
        return FileUploadRepositories.getInstance().uploadInvoice(body, context);
    }
}
