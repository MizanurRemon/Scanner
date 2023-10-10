package com.scanner.scanner.Remote.FileUpload;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.scanner.scanner.Model.CommonResponse;
import com.scanner.scanner.Network.Private.PrivateApiService;
import com.scanner.scanner.Network.Private.PrivateApiUtilize;
import com.scanner.scanner.Network.Public.PublicApiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadRepositories {
    private static FileUploadRepositories fileUploadRepositories;
    MutableLiveData<CommonResponse> commonResponse;

    PrivateApiService privateApiService;
    PublicApiService publicApiService;

    public FileUploadRepositories() {
        privateApiService = PrivateApiUtilize.privateApiService();
        commonResponse = new MutableLiveData<>();
    }

    public synchronized static FileUploadRepositories getInstance() {
        if (fileUploadRepositories == null) {
            return new FileUploadRepositories();
        }

        return fileUploadRepositories;
    }

    MutableLiveData<CommonResponse> uploadInvoice(Map<String, Object> body, Context context) {

        Call<CommonResponse> call = privateApiService.uploadFiles(body);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                if (response.isSuccessful()) {
                    Log.d("dataxx", "onResponse: " + response.body().toString());
                    commonResponse.postValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.d("dataxx", "failed: " + t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return commonResponse;
    }
}
