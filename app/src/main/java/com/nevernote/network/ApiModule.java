package com.nevernote.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nevernote.Config;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ali on 28.02.18.
 */

@Module
public class ApiModule {

    @Provides
    @Singleton
    public ApiService apiService() {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if(Config.RETROFIT_LOG){
            builder = builder.addInterceptor(interceptor);
        }

        builder = builder.readTimeout(60, TimeUnit.SECONDS);
        builder = builder.connectTimeout(60, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.build();


        Gson gson = new GsonBuilder()
                .create();

        GsonConverterFactory factory = GsonConverterFactory.create(gson);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.API_ROOT)
                .addConverterFactory(factory)
                .client(okHttpClient)
                .build();
        return retrofit.create(ApiService.class);

    }

}
