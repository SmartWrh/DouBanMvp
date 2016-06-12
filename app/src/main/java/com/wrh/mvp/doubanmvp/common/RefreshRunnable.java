package com.wrh.mvp.doubanmvp.common;

import com.wrh.mvp.recyclerviewlayout.base.RecyclerViewLayout;

/**
 * Created by user on 2016/6/3.
 */
public class RefreshRunnable implements Runnable {
    private RecyclerViewLayout mSwipeRefreshLayout;

    public RefreshRunnable(RecyclerViewLayout swipeRefreshLayout) {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void run() {
        mSwipeRefreshLayout.setRefreshing(true);
    }
}
