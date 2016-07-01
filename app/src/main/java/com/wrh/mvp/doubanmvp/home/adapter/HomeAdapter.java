package com.wrh.mvp.doubanmvp.home.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.wrh.mvp.doubanmvp.R;
import com.wrh.mvp.doubanmvp.common.listener.OnRecyclerItemClick;
import com.wrh.mvp.doubanmvp.common.view.LabelView;
import com.wrh.mvp.doubanmvp.home.entity.MovieEntity;
import com.wrh.mvp.recyclerviewlayout.base.RecyclerViewLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by user on 2016/6/3.
 */
public class HomeAdapter extends RecyclerViewLayout.Adapter<HomeAdapter.ItemHolder> {

    private Context mContext;
    private List<MovieEntity.SubjectsBean> mSubjectsBean;
    private int resourceColor[] = {R.color.colorAccent, R.color.colorBlue,
            R.color.colorOrange, R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorRed};

    private OnRecyclerItemClick mItemClick;

    public HomeAdapter(Context context, List<MovieEntity.SubjectsBean> subjectsBean) {
        mContext = context;
        mSubjectsBean = subjectsBean;
    }

    public void setOnRecyclerItemClick(OnRecyclerItemClick itemClick) {
        this.mItemClick = itemClick;
    }

    @Override
    protected ItemHolder onAdapterCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_feed, null);
        return new ItemHolder(view);
    }

    @Override
    protected void onAdapterBindViewHolder(final ItemHolder viewHolder, final int position) {
        Glide.with(mContext).load(mSubjectsBean.get(position).getImages().getLarge()).asBitmap().into(viewHolder.imgFeed);
        int num = (int) (Math.random() * resourceColor.length);
        viewHolder.labelView.setColor(ContextCompat.getColor(mContext, resourceColor[num]));
        viewHolder.labelView.setText(mSubjectsBean.get(position).getGenres().get(0));
        RxView.clicks(viewHolder.labelView).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mItemClick != null) {
                    mItemClick.onItemClick(viewHolder, position);
                }
            }
        });
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
