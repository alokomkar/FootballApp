package app.football.com.footballapp.utils;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cognitive on 10/24/16.
 */
public class ServiceGenerator {

    private static final String API_URL = "https://xobin.com/static/xobin_playground/";

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }
}
