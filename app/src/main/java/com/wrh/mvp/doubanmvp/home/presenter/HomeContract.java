package com.wrh.mvp.doubanmvp.home.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.wrh.mvp.doubanmvp.common.base.BasePresenter;
import com.wrh.mvp.doubanmvp.common.base.BaseView;
import com.wrh.mvp.doubanmvp.home.entity.MovieEntity;
import com.wrh.mvp.recyclerviewlayout.base.RecyclerViewLayout;

/**
 * Created by user on 2016/6/2.
 */
public class HomeContract {
    public interface View extends BaseView {
        void setTopMovie(MovieEntity entities);

        void setLoadMoreMovie(MovieEntity entities);
    }

    public interface Presenter extends BasePresenter {
        //用来给recycleview复制加监听等操作
        void setRecyclerView(Context context, RecyclerViewLayout recyclerView, RecyclerViewLayout.Adapter adapter);

        //用来判断是否可以加载更多等关于数据业务操作
        void setRecyclerViewData(MovieEntity entity);

        //跳转至电影详情
        void toMovieDetail(RecyclerView.ViewHolder holder, MovieEntity.SubjectsBean subjectsBean);
    }
}
