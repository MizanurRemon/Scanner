package com.scanner.scanner.Remote.FileUpload;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.scanner.scanner.Model.CommonResponse;
import com.scanner.scanner.Model.FileListResponse;

import java.util.List;
import java.util.Map;

public class FileViewModel extends ViewModel {

    public LiveData<CommonResponse> uploadInvoice(Map<String, Object> body, Context context) {
        return FileUploadRepositories.getInstance().uploadInvoice(body, context);
    }

    public LiveData<List<FileListResponse>> getFileList(Context context) {
        return FileUploadRepositories.getInstance().getFileList(context);
    }
}
