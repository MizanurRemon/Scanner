package com.scanner.scanner.Network.Public;

public class PublicApiUtilize {

    public static PublicApiService publicApiService() {
        return PublicRetrofitConfig.getRetrofit().create(PublicApiService.class);
    }
}
