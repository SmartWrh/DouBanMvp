package com.wrh.mvp.recyclerviewlayout.base.recyclerviewlayout;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kate on 2015/6/1
 */
public class CustomRecyclerView extends RecyclerView {
    private List<OnScrollListener> mOnScrollListenerList;
    private List<OnChangeLayoutManagerListener> mOnChangeLayoutManagerListenerList;

    public CustomRecyclerView(Context context) {
        super(context);

        initialize();
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initialize();
    }

    private void initialize() {
        mOnScrollListenerList = new ArrayList<OnScrollListener>();
        addOnScrollListener(null);
        mOnChangeLayoutManagerListenerList = new ArrayList<OnChangeLayoutManagerListener>();
        addOnChangeLayoutManagerListener(null);

        setOnScrollListenerInternal(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                for (OnScrollListener onScrollListener : mOnScrollListenerList) {

                    if (onScrollListener != null) {
                        onScrollListener.onScrollStateChanged(recyclerView, newState);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                for (OnScrollListener onScrollListener : mOnScrollListenerList) {

                    if (onScrollListener != null) {
                        onScrollListener.onScrolled(recyclerView, dx, dy);
                    }
                }
            }
        });
    }

    @Override
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        if (getAdapter() != null && getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutAnimationController.AnimationParameters animationParams =
                    (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;
            if (animationParams == null) {
                animationParams = new GridLayoutAnimationController.AnimationParameters();
                params.layoutAnimationParameters = animationParams;
            }

            int columns = ((GridLayoutManager) getLayoutManager()).getSpanCount();

            animationParams.count = count;
            animationParams.index = index;
            animationParams.columnsCount = columns;
            animationParams.rowsCount = count / columns;

            final int invertedIndex = count - 1 - index;
            animationParams.column = columns - 1 - (invertedIndex % columns);
            animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns;

        } else {
            super.attachLayoutAnimationParameters(child, params, index, count);
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        mOnScrollListenerList.set(0, listener);
    }

    private void setOnScrollListenerInternal(OnScrollListener listener) {
        super.setOnScrollListener(listener);
    }

    public void addOnScrollListener(OnScrollListener listener) {
        mOnScrollListenerList.add(listener);
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        removeOnScrollListener(mOnScrollListenerList.indexOf(listener));
    }

    private void removeOnScrollListener(int index) {
        if (index < 0 || index >= mOnScrollListenerList.size()) {
            return;
        }
        if (index == 0) {
            setOnScrollListener(null);
            return;
        }
        mOnScrollListenerList.remove(index);
    }

    /**
     * @deprecated use {@link #setAdapter(CustomAdapter)} instead
     */
    @Override
    public void setAdapter(Adapter adapter) {

    }

    public void setAdapter(CustomAdapter adapter) {
        super.setAdapter(adapter);
    }

    public CustomAdapter getAdapter() {
        return (CustomAdapter) super.getAdapter();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        for (OnChangeLayoutManagerListener onChangeLayoutManagerListener : mOnChangeLayoutManagerListenerList) {

            if (onChangeLayoutManagerListener != null) {
                onChangeLayoutManagerListener.onChange(layout);
            }
        }
    }

    public void setOnChangeLayoutManagerListener(OnChangeLayoutManagerListener listener) {
        mOnChangeLayoutManagerListenerList.set(0, listener);
    }

    public void addOnChangeLayoutManagerListener(OnChangeLayoutManagerListener listener) {
        mOnChangeLayoutManagerListenerList.add(listener);
    }

    public void removeOnChangeLayoutManagerListener(OnChangeLayoutManagerListener listener) {
        removeOnChangeLayoutManagerListener(mOnChangeLayoutManagerListenerList.indexOf(listener));
    }

    private void removeOnChangeLayoutManagerListener(int index) {
        if (index < 0 || index >= mOnChangeLayoutManagerListenerList.size()) {
            return;
        }
        if (index == 0) {
            setOnChangeLayoutManagerListener(null);
            return;
        }
        mOnChangeLayoutManagerListenerList.remove(index);
    }

    public interface OnChangeLayoutManagerListener {
        void onChange(LayoutManager layout);
    }
}
