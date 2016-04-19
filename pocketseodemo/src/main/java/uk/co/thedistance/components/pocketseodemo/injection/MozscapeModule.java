/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.pocketseodemo.injection;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.thedistance.components.pocketseodemo.BuildConfig;
import uk.co.thedistance.components.pocketseodemo.mozscape.MSHelper;
import uk.co.thedistance.components.pocketseodemo.mozscape.MSWebService;

/**
 * Created by pharris on 17/02/16.
 */
@Module
public class MozscapeModule {

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        return builder.build();
    }

    @Provides
    @Singleton
    public MSHelper.Authenticator provideAuthenticator(){
        return new MSHelper.Authenticator(BuildConfig.MOZSCAPE_ACCESS_KEY, BuildConfig.MOZSCAPE_SECRET_KEY, 300);
    }

    @Provides
    @Singleton
    public MSWebService provideWebService(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MSWebService.URL_PREFIX)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(MSWebService.class);
    }

}
