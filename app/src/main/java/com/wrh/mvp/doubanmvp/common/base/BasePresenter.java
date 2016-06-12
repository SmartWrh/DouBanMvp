package com.wrh.mvp.doubanmvp.common.base;

/**
 * Created by user on 2016/6/2.
 */
public interface BasePresenter {
    void loadTask();

    void errorTask(String message);
}
