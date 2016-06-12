package com.wrh.mvp.doubanmvp.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wrh.mvp.doubanmvp.R;
import com.wrh.mvp.doubanmvp.common.view.LabelView;
import com.wrh.mvp.doubanmvp.home.entity.MovieEntity;
import com.wrh.mvp.recyclerviewlayout.base.RecyclerViewLayout;

import java.util.List;

/**
 * Created by user on 2016/6/3.
 */
public class HomeAdapter extends RecyclerViewLayout.Adapter<HomeAdapter.ItemHolder> {

    private Context mContext;
    private List<MovieEntity.SubjectsBean> mSubjectsBean;

    public HomeAdapter(Context context, List<MovieEntity.SubjectsBean> subjectsBean) {
        mContext = context;
        mSubjectsBean = subjectsBean;
    }

    @Override
    protected ItemHolder onAdapterCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_feed, null);
        return new ItemHolder(view);
    }

    @Override
    protected void onAdapterBindViewHolder(ItemHolder viewHolder, int position) {
        Glide.with(mContext).load(mSubjectsBean.get(position).getImages().getLarge()).into(viewHolder.imgFeed);
        viewHolder.labelView.setText(mSubjectsBean.get(position).getGenres().get(0));
    }

    @Override
    protected View onLoadMoreCreateView(ViewGroup viewGroup) {
        return LayoutInflater.from(mContext).inflate(R.layout.view_common_loadmore, viewGroup, false);
    }


    @Override
    public int getAdapterItemCount() {
        return mSubjectsBean.size();
    }

    @Override
    public boolean hasHeader() {
        return false;
    }


    static class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView imgFeed;
        private LabelView labelView;

        public ItemHolder(View itemView) {
            super(itemView);
            imgFeed = (ImageView) itemView.findViewById(R.id.home_img_feed);
            labelView = (LabelView) itemView.findViewById(R.id.home_label);
        }
    }
}
