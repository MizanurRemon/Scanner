package com.scanner.scanner.Remote.FileUpload;

import androidx.lifecycle.MutableLiveData;

import com.scanner.scanner.Model.CommonResponse;
import com.scanner.scanner.Network.Private.PrivateApiService;
import com.scanner.scanner.Network.Public.PublicApiService;

import java.util.Map;

public class FileUploadRepositories {
    private static FileUploadRepositories fileUploadRepositories;
    MutableLiveData<CommonResponse> commonResponse;

    PrivateApiService privateApiService;
    PublicApiService publicApiService;

    public FileUploadRepositories() {
        commonResponse = new MutableLiveData<>();
    }

    public synchronized static FileUploadRepositories getInstance() {
        if (fileUploadRepositories == null) {
            return new FileUploadRepositories();
        }

        return fileUploadRepositories;
    }

    MutableLiveData<CommonResponse> uploadInvoice(Map<String, Object> body) {


        return commonResponse;
    }
}
