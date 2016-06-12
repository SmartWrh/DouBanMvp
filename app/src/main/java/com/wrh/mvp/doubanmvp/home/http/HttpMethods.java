package com.wrh.mvp.doubanmvp.home.http;

import com.wrh.mvp.doubanmvp.home.entity.MovieEntity;

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
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mMovieService = mRetrofit.create(MovieService.class);

    }

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getTopMovie(Subscriber<MovieEntity> subscriber, int start, int count) {
        mMovieService.getTopMovie(start, count)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
