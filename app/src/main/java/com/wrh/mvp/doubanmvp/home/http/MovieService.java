package com.wrh.mvp.doubanmvp.home.http;

import com.wrh.mvp.doubanmvp.home.entity.MovieEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by user on 2016/6/2.
 */
public interface MovieService {
    @GET("top250")
    Observable<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
}
