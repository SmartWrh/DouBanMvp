package com.wrh.mvp.doubanmvp.home.subscriber;

import com.orhanobut.logger.Logger;
import com.wrh.mvp.doubanmvp.home.entity.MovieEntity;
import com.wrh.mvp.doubanmvp.home.presenter.HomeContract;

import rx.Subscriber;

/**
 * Created by user on 2016/6/4.
 */
public class MovieEntitySubscriber extends Subscriber<MovieEntity> {

    private HomeContract.Presenter mPresenter;

    public MovieEntitySubscriber(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        Logger.d("Error:" + e.getMessage());
        mPresenter.errorTask(e.getMessage());
    }

    @Override
    public void onNext(MovieEntity movieEntity) {
        mPresenter.setRecyclerViewData(movieEntity);
    }
}
