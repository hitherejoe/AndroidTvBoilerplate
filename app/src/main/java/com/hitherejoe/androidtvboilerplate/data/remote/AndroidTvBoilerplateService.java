package com.hitherejoe.androidtvboilerplate.data.remote;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public interface AndroidTvBoilerplateService {

    String ENDPOINT = "https://your.endpoint.com/";

    /********
     * Helper class that sets up a new services
     *******/
    class Creator {
        public static AndroidTvBoilerplateService newVineyardService() {
            OkHttpClient client = new OkHttpClient();
            client.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    // Catch unauthorised error
                    return response;
                }
            });

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AndroidTvBoilerplateService.ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(AndroidTvBoilerplateService.class);
        }
    }

}
