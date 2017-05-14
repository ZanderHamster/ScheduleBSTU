package ru.lekveishvili.david.schedulebstu.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.lekveishvili.david.schedulebstu.network.service.MainApiService;

public class RetrofitClient {
    private static final String BASE_URL = "http://95.213.251.173:3000";

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static MainApiService getMainApiService() {
        return getRetrofitInstance().create(MainApiService.class);
    }
}
