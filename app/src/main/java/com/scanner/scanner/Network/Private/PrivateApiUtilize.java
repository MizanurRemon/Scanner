package com.scanner.scanner.Network.Private;

public class PrivateApiUtilize {
    public static PrivateApiService privateApiService() {
        return PrivateRetrofitConfig.getRetrofit().create(PrivateApiService.class);
    }
}
