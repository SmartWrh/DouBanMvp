package com.wrh.mvp.doubanmvp.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wrh.mvp.doubanmvp.R;
import com.wrh.mvp.doubanmvp.common.ErrorLevel;
import com.wrh.mvp.doubanmvp.common.base.BaseActivity;
import com.wrh.mvp.doubanmvp.common.listener.OnRecyclerItemClick;
import com.wrh.mvp.doubanmvp.home.adapter.HomeAdapter;
import com.wrh.mvp.doubanmvp.home.entity.MovieEntity;
import com.wrh.mvp.doubanmvp.home.presenter.HomeContract;
import com.wrh.mvp.doubanmvp.home.presenter.HomePresenter;
import com.wrh.mvp.recyclerviewlayout.base.RecyclerViewLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements HomeContract.View, OnRecyclerItemClick {

    @BindView(R.id.home_content)
    LinearLayout mHomeContent;

    @BindView(R.id.home_toolbar)
    Toolbar mHomeToolbar;

    @BindView(R.id.home_recycler)
    RecyclerViewLayout mHomeRecycler;

    private HomePresenter mHomePresenter;

    private HomeAdapter mHomeAdapter;

    private List<MovieEntity.SubjectsBean> mSubjectsBeen = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initState();
    }

    private void initState() {
        mHomePresenter = new HomePresenter(this);
        mHomeAdapter = new HomeAdapter(this, mSubjectsBeen);
        mHomePresenter.setRecyclerView(this, mHomeRecycler, mHomeAdapter);
        mHomeRecycler.getRecyclerView().setAdapter(mHomeAdapter);
        mHomeAdapter.setOnRecyclerItemClick(this);
        mHomePresenter.loadTask();
    }

    @Override
    public void setTopMovie(MovieEntity entities) {
        mHomeRecycler.setRefreshing(false);
        mSubjectsBeen.clear();
        mSubjectsBeen.addAll(entities.getSubjects());
        mHomeAdapter.notifyDataSetChanged();
        mHomeRecycler.getRecyclerView().scheduleLayoutAnimation();
    }

    @Override
    public void setLoadMoreMovie(MovieEntity entities) {
        mHomeAdapter.setLoadingMore(false);
        Observable.from(entities.getSubjects()).compose(this.bindToLifecycle()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object subjectsBean) {
                mSubjectsBeen.add((MovieEntity.SubjectsBean) subjectsBean);
                mHomeAdapter.notifyAdapterItemInserted(mSubjectsBeen.size());
            }
        });
    }

    @Override
    public void errorView(ErrorLevel level, String message) {
        switch (level) {
            case INFO:
                mHomeRecycler.setRefreshing(false);
                mHomeAdapter.setLoadingMore(false);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder view, int position) {
        mHomePresenter.toMovieDetail(view, mSubjectsBeen.get(position));
    }
}
