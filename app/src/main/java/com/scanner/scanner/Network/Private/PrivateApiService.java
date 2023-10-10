package com.scanner.scanner.Network.Private;


import com.scanner.scanner.Model.CommonResponse;
import com.scanner.scanner.Utils.URLConstants;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PrivateApiService {

    @POST(URLConstants.UPLOAD)
    Call<CommonResponse> uploadFiles(@Body Map<String, Object> body);
}
