package com.scanner.scanner.Network.Public;

import com.scanner.scanner.Model.AuthResponse;
import com.scanner.scanner.Utils.URLConstants;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PublicApiService {

    @POST(URLConstants.LOGIN)
    Call<AuthResponse> adminLogin(@Body Map<String, Object> body);
}
