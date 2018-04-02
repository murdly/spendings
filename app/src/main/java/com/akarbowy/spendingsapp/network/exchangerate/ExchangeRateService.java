package com.akarbowy.spendingsapp.network.exchangerate;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ExchangeRateService {

    private static Api serviceInstance = null;

    public static Api getApi() {
        return serviceInstance == null ? createService() : serviceInstance;
    }

    private static Api createService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();

        return retrofit.create(Api.class);
    }

    public interface Api {
        String BASE_URL = "https://api.fixer.io/";

        @GET("latest")
        Call<ExchangeRateResponse> getExchangeRates(@Query("base") String base);
    }

}
