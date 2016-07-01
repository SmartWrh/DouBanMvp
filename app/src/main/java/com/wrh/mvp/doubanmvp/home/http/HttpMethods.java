package com.wrh.mvp.doubanmvp.home.http;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wrh.mvp.doubanmvp.home.entity.MovieEntity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by user on 2016/6/2.
 */
public class HttpMethods {
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";
    private Retrofit mRetrofit;
    private MovieService mMovieService;

    private HttpMethods() {


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(mTokenInterceptor)
                .build();

        mRetrofit = new Retrofit.Builder().client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mMovieService = mRetrofit.create(MovieService.class);
    }

    Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request authorised = originalRequest.newBuilder()
                    .header("Authorization", "")
                    .build();
            return chain.proceed(authorised);
        }
    };

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getTopMovie(RxAppCompatActivity activity, Subscriber<MovieEntity> subscriber, int start, int count) {
        mMovieService.getTopMovie(start, count).compose(activity.<MovieEntity>bindUntilEvent(ActivityEvent.STOP))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
