package com.wrh.mvp.doubanmvp.home.presenter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import com.wrh.mvp.doubanmvp.common.ErrorLevel;
import com.wrh.mvp.doubanmvp.common.RefreshRunnable;
import com.wrh.mvp.doubanmvp.common.base.BaseConstant;
import com.wrh.mvp.doubanmvp.home.MainActivity;
import com.wrh.mvp.doubanmvp.home.entity.MovieEntity;
import com.wrh.mvp.doubanmvp.home.http.HttpMethods;
import com.wrh.mvp.doubanmvp.home.subscriber.MovieEntitySubscriber;
import com.wrh.mvp.recyclerviewlayout.base.RecyclerViewLayout;
import com.wrh.mvp.recyclerviewlayout.base.recyclerviewlayout.AdvanceAdapter;

/**
 * Created by user on 2016/6/2.
 */
public class HomePresenter implements HomeContract.Presenter, SwipeRefreshLayout.OnRefreshListener, AdvanceAdapter.OnLoadMoreListener {

    private int start = BaseConstant.start;

    private final HomeContract.View mView;

    private MainActivity mActivity;

    private RecyclerViewLayout mBaseRecyclerView;

    private RecyclerViewLayout.Adapter mAdapter;

    private HttpMethods mHttpMethods;


    private MovieEntitySubscriber mEntitySubscriber;

    public HomePresenter(HomeContract.View view, MainActivity activity) {
        mView = view;
        mActivity = activity;
        mHttpMethods = HttpMethods.getInstance();
    }

    @Override
    public void loadTask() {
        openTask();
    }


    @Override
    public void errorTask(String message) {
        mView.errorView(ErrorLevel.INFO, message);
    }

    private void openTask() {
        if (mView == null) {
            return;
        }
        if (mEntitySubscriber != null && !mEntitySubscriber.isUnsubscribed()) {
            mEntitySubscriber.unsubscribe();
        }
        mEntitySubscriber = new MovieEntitySubscriber(this);
        mHttpMethods.getTopMovie(mActivity, mEntitySubscriber, start, BaseConstant.count);
    }

    @Override
    public void onRefresh() {
        start = 0;
        mAdapter.disableLoadMore();
        mAdapter.setLoadingMore(false);
        openTask();
    }

    @Override
    public void setRecyclerView(Context mContext, RecyclerViewLayout recyclerView, RecyclerViewLayout.Adapter adapter) {
        mBaseRecyclerView = recyclerView;
        mAdapter = adapter;
        adapter.setOnLoadMoreListener(this);
        adapter.disableLoadMore();
        adapter.setLoadingMore(false);
        recyclerView.getRecyclerView().setHasFixedSize(true);
        recyclerView.getRecyclerView().post(new RefreshRunnable(recyclerView));
        recyclerView.setOnRefreshListener(this);
        recyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(mContext, 2));
    }

    @Override
    public void setRecyclerViewData(MovieEntity entity) {
        if (entity.getSubjects().size() >= BaseConstant.count) {
            mAdapter.enableLoadMore();
        } else {
            mAdapter.disableLoadMore();
        }
        if (mBaseRecyclerView.isRefreshing()) {
            mView.setTopMovie(entity);
        } else if (mAdapter.isLoadingMore()) {
            mView.setLoadMoreMovie(entity);
        }
    }


    @Override
    public void loadMore() {
        start += BaseConstant.count;
        mBaseRecyclerView.setRefreshing(false);
        openTask();
    }
}
