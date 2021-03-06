package com.wrh.mvp.recyclerviewlayout.base.recyclerviewlayout;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wrh.mvp.recyclerviewlayout.R;

/**
 * Created by Kate on 2015/4/30
 */
abstract public class AdvanceAdapter<VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_HEADER = 11;
    protected static final int TYPE_FOOTER = 12;
    protected static final int TYPE_LOAD_MORE = 13;

    private OnLoadMoreListener mOnLoadMoreListener;
    private static boolean isLoadingMore = false;
    private static boolean enableLoadMore = false;

    protected int getOrientation() {
        return OrientationHelper.VERTICAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                DefaultViewHolder header = new DefaultViewHolder(getDefaultView(viewGroup));
                view = onHeaderCreateView(header.rootView);
                if (view != null) {
                    header.addView(view);
                }
                return new AdvanceHolder(header.itemView);

            case TYPE_FOOTER:
                view = onFooterCreateView(viewGroup);
                return new AdvanceHolder(view != null ? view : getDefaultView(viewGroup));

            case TYPE_LOAD_MORE:
                view = onLoadMoreCreateView(viewGroup);
                return new AdvanceHolder(view != null ? view : getDefaultView(viewGroup));

            default:
                return onAdapterCreateViewHolder(viewGroup, viewType);
        }
    }

    protected View onLoadMoreCreateView(ViewGroup viewGroup) {
        return null;
    }

    protected View onFooterCreateView(ViewGroup viewGroup) {
        return null;
    }

    protected View onHeaderCreateView(ViewGroup viewGroup) {
        return null;
    }

    private class DefaultViewHolder extends RecyclerView.ViewHolder {

        private final ViewGroup rootView;

        public DefaultViewHolder(View itemView) {
            super(itemView);

            rootView = (ViewGroup) itemView.findViewById(R.id.recyclerviewlayout_default);
        }

        public void addView(View view) {
            rootView.addView(view);
        }
    }

    private View getDefaultView(ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_recyclerviewlayout_default,
                viewGroup, false);
    }

    protected abstract VH onAdapterCreateViewHolder(ViewGroup viewGroup, int viewType);

    @SuppressWarnings("unchecked viewHolder type")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_HEADER:
                onHeaderBindViewHolder(viewHolder);
                break;

            case TYPE_FOOTER:
                onFooterBindViewHolder(viewHolder);
                break;

            case TYPE_LOAD_MORE:
                onLoadMoreBindViewHolder(viewHolder);
                break;

            default:
                onAdapterBindViewHolder((VH) viewHolder, position - getHeaderCount());
                break;
        }

        if (viewHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(isFullSpan(position));
        }
    }

    protected void onFooterBindViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    protected void onHeaderBindViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    protected void onLoadMoreBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        ViewGroup.LayoutParams vlp = viewHolder.itemView.getLayoutParams();
        if (getOrientation() == OrientationHelper.VERTICAL) {
            vlp.width = enableLoadMore ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
            vlp.height = enableLoadMore ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;
        } else {
            vlp.width = enableLoadMore ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;
            vlp.height = enableLoadMore ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
        }
        viewHolder.itemView.setLayoutParams(vlp);

        if (enableLoadMore && mOnLoadMoreListener != null && !isLoadingMore) {
            mOnLoadMoreListener.loadMore();
            isLoadingMore = true;
        }
    }

    protected abstract void onAdapterBindViewHolder(VH viewHolder, int position);

    @Override
    public int getItemViewType(int position) {
        if (position == getHeaderPosition() && getHeaderCount() != 0) {
            return TYPE_HEADER;
        }
        if (position == getFooterPosition()) {
            return TYPE_FOOTER;
        }
        if (position == getLoadMorePosition()) {
            return TYPE_LOAD_MORE;
        }
        int viewType = getAdapterItemViewType(position - getHeaderCount());
        if (viewType == TYPE_HEADER || viewType == TYPE_FOOTER || viewType == TYPE_LOAD_MORE) {
            throw new IllegalArgumentException("These viewTypes is for special case!");
        }
        return viewType;
    }

    protected int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getAdapterItemCount() + getFooterCount();
    }

    abstract public int getAdapterItemCount();

    abstract public boolean hasHeader();

    public int getHeaderCount() {
        return hasHeader() ? 1 : 0;
    }

    public int getFooterCount() {
        return 2;
    }

    /**
     * cause it will invalid view, do not use this method in bindView
     */
    public void disableLoadMore() {
        if (!enableLoadMore) {
            return;
        }
        enableLoadMore = false;
        notifyLoadMoreViewChanged();
    }

    /**
     * cause it will invalid view, do not use this method in bindView
     */
    public void enableLoadMore() {
        if (enableLoadMore) {
            return;
        }
        enableLoadMore = true;
        notifyLoadMoreViewChanged();
    }

    public boolean getLoadMoreEnable() {
        return enableLoadMore;
    }

    public boolean isFullSpan(int position) {
        return (position == getHeaderPosition() && getHeaderCount() != 0)
                || position == getFooterPosition() || position == getLoadMorePosition();
    }

    public void notifyAdapterItemRangeInserted(int position, int size) {
        notifyItemRangeInserted(getHeaderCount() + position, size);
    }

    public void notifyAdapterItemRangeRemoved(int position, int size) {
        notifyItemRangeRemoved(getHeaderCount() + position, size);
    }

    public void notifyAdapterItemRangeChanged(int position, int size) {
        notifyItemRangeChanged(getHeaderCount() + position, size);
    }

    public void notifyAdapterItemInserted(int position) {
        notifyItemInserted(getHeaderCount() + position);
    }

    public void notifyAdapterItemRemoved(int position) {
        notifyItemRemoved(getHeaderCount() + position);
    }

    /**
     * @deprecated Use {@link #notifyAdapterItemChanged(int)} instead
     */
    public void notifyAdapterItemRangeChanged(int position) {
        notifyAdapterItemChanged(position);
    }

    public void notifyAdapterItemChanged(int position) {
        notifyItemChanged(getHeaderCount() + position);
    }

    public static class AdvanceHolder extends RecyclerView.ViewHolder {
        View hold_view;

        public AdvanceHolder(View itemView) {
            super(itemView);

            hold_view = itemView.findViewById(R.id.hold_view);
        }

        public void setHoldHeight(int height) {
            if (hold_view != null) {
                ViewGroup.LayoutParams vlp = hold_view.getLayoutParams();
                vlp.height = height;
                hold_view.setLayoutParams(vlp);
            }
        }

        public void setHoldWidth(int width) {
            if (hold_view != null) {
                ViewGroup.LayoutParams vlp = hold_view.getLayoutParams();
                vlp.width = width;
                hold_view.setLayoutParams(vlp);
            }
        }
    }

    public void notifyHeaderViewChanged() {
        if (getHeaderCount() != 0) {
            notifyItemChanged(getHeaderPosition());
        }
    }

    public void notifyFooterViewChanged() {
        notifyItemChanged(getFooterPosition());
    }

    public void notifyLoadMoreViewChanged() {
        notifyItemChanged(getLoadMorePosition());
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }

    /**
     * cause it will invalid view, do not use this method in bindView
     *
     * @param flag LoadingMoreStatus
     */
    public void setLoadingMore(boolean flag) {
        if (isLoadingMore == flag) {
            return;
        }
        isLoadingMore = flag;
        notifyLoadMoreViewChanged();
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    int getHeaderPosition() {
        return 0;
    }

    int getFooterPosition() {
        return getHeaderCount() + getAdapterItemCount();
    }

    int getLoadMorePosition() {
        return getItemCount() - 1;
    }
}
