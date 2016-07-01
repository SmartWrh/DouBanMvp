package com.wrh.mvp.doubanmvp.common.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by user on 2016/6/13.
 */
public interface OnRecyclerItemClick {
    void onItemClick(RecyclerView.ViewHolder view, int position);
}
