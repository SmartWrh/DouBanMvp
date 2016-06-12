package com.wrh.mvp.doubanmvp.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wrh.mvp.doubanmvp.R;
import com.wrh.mvp.doubanmvp.common.ErrorLevel;

/**
 * Created by user on 2016/6/2.
 */
public class BaseActivity extends RxAppCompatActivity {

    private static final String TAG_CREATE = "onCreate";



    private Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.d(TAG_CREATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void setContentWithToolbarView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        View totalView = LayoutInflater.from(this).inflate(R.layout.activity_base, null);
        mToolbar = (Toolbar) totalView.findViewById(R.id.base_toolbar);
        FrameLayout contentLayout = (FrameLayout) totalView.findViewById(R.id.base_content);
        contentLayout.addView(view);
        super.setContentView(totalView);
        setToolBar();
    }

    /**
     * 设置toolBar代替ActionBar
     */
    private void setToolBar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        titleRunnable.run();
    }

    private Runnable titleRunnable = new Runnable() {
        @Override
        public void run() {
            if (mToolbar != null) {
                mToolbar.setTitle(getTitle());
            }
        }
    };


    /**
     * 错误提示View
     *
     * @return
     */
    protected View onErrorView() {
        TextView txtErrorView = new TextView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        txtErrorView.setLayoutParams(layoutParams);
        txtErrorView.setId(R.id.base_error);
        txtErrorView.setText(getResources().getString(R.string.error_info));
        return txtErrorView;
    }

    /**
     * 对error处理
     *
     * @param level
     * @param view    父控件用来添加错误提示View
     * @param message 错误消息
     */
    protected void onError(ErrorLevel level, ViewGroup view, String message) {
        switch (level) {
            case ERROR:
                view.addView(onErrorView());
                break;
            case WARN:
                break;
            case INFO:
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
