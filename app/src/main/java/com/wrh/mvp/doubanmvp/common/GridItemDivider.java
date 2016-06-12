package com.wrh.mvp.doubanmvp.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class GridItemDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };


    private Drawable mDivider;


    public GridItemDivider(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();

    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    private int getTop(int position, int span, int width) {
        if (position < span) {
            return width;
        }
        return width / 2;
    }

    private int getBottom(int position, int span, int childCount, int width) {
        if (position < childCount && position >= childCount - span) {
            return width;
        }
        return width / 2;
    }

    private int getLeft(int position, int span, int width) {
        if (position % span == 0) {
            return width;
        }
        return width / 2;
    }

    private int getRight(int position, int span, int width) {
        if ((position + 1) % span == 0) {
            return width;
        }
        return width / 2;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int left = getLeft(parent.getChildAdapterPosition(view), getSpanCount(parent), mDivider.getIntrinsicWidth());
        int top = getTop(parent.getChildAdapterPosition(view), getSpanCount(parent), mDivider.getIntrinsicWidth());
        int right = getRight(parent.getChildAdapterPosition(view), getSpanCount(parent), mDivider.getIntrinsicWidth());
        int bottom = getBottom(parent.getChildAdapterPosition(view), getSpanCount(parent), state.getItemCount(), mDivider.getIntrinsicWidth());
        outRect.set(left, top, right, bottom);

    }
}