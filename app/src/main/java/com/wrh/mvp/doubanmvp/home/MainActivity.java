package com.wrh.mvp.doubanmvp.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wrh.mvp.doubanmvp.R;
import com.wrh.mvp.doubanmvp.common.base.BaseActivity;
import com.wrh.mvp.doubanmvp.common.ErrorLevel;
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

public class MainActivity extends BaseActivity implements HomeContract.View {

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
        Observable.from(entities.getSubjects()).subscribe(new Action1<MovieEntity.SubjectsBean>() {
            @Override
            public void call(MovieEntity.SubjectsBean subjectsBean) {
                mSubjectsBeen.add(subjectsBean);
                mHomeAdapter.notifyAdapterItemInserted(mSubjectsBeen.size());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubjectsBeen = null;
        if (mHomePresenter != null) {
            mHomePresenter.stopTask();
        }
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
}