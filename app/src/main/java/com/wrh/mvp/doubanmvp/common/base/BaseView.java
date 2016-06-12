package com.wrh.mvp.doubanmvp.common.base;

import com.wrh.mvp.doubanmvp.common.ErrorLevel;

/**
 * Created by user on 2016/6/2.
 */
public interface BaseView<T> {
    void errorView(ErrorLevel level, String message);
}
