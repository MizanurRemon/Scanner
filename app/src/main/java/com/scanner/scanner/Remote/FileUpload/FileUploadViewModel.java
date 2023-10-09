package com.scanner.scanner.Remote.FileUpload;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.scanner.scanner.Model.CommonResponse;

import java.util.Map;

public class FileUploadViewModel extends ViewModel {

    public LiveData<CommonResponse> uploadInvoice(Map<String, Object> body) {
        return FileUploadRepositories.getInstance().uploadInvoice(body);
    }
}
