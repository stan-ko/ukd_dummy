package ua.ukd.dummy.net;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://reqres.in/api/"; // Base URL of the API

    private static Retrofit client;

    public static Retrofit getInstance() {
        if (client == null)
            init();
        return client;
    }

    private static void init(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // Add logging interceptor for debugging
        httpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static UserService getUserService() {
        return getInstance().create(UserService.class);
    }
}
