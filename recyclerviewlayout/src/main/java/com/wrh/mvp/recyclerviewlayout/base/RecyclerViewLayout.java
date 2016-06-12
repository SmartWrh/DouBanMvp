package com.wrh.mvp.recyclerviewlayout.base;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;

import com.wrh.mvp.recyclerviewlayout.R;
import com.wrh.mvp.recyclerviewlayout.base.recyclerviewlayout.AutoHidingHeaderListener;
import com.wrh.mvp.recyclerviewlayout.base.recyclerviewlayout.CustomAdapter;
import com.wrh.mvp.recyclerviewlayout.base.recyclerviewlayout.CustomRecyclerView;
import com.wrh.mvp.recyclerviewlayout.base.recyclerviewlayout.ParallaxScrollingHeaderListener;

/**
 * Created by Kate on 2015/5/7
 */
public class RecyclerViewLayout extends SwipeRefreshLayout {
    private FrameLayout mFrameLayout;
    private FrameLayout mParallaxScrollingLayout;
    private final CustomRecyclerView mRecyclerView;
    private View mAutoHidingHeader;
    private View mParallaxScrollingHeader;
    private AutoHidingHeaderListener mAutoHidingHeaderListener;
    private ParallaxScrollingHeaderListener mParallaxScrollingListener;
    private float mParallaxScrollingVelocity = 1;

    public RecyclerViewLayout(Context context) {
        this(context, null);
    }

    public RecyclerViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFrameLayout = new FrameLayout(context);
        mFrameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mFrameLayout);

        mRecyclerView = new CustomRecyclerView(context);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
        GridLayoutAnimationController animationController = new GridLayoutAnimationController(animation, 0.15f, 0.15f);
        animationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        animationController.setDirection(GridLayoutAnimationController.DIRECTION_TOP_TO_BOTTOM | GridLayoutAnimationController.DIRECTION_LEFT_TO_RIGHT);
        mRecyclerView.setLayoutAnimation(animationController);
        mRecyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mFrameLayout.addView(mRecyclerView);

        mParallaxScrollingLayout = new FrameLayout(context);
        mParallaxScrollingLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        mFrameLayout.addView(mParallaxScrollingLayout);

        initialize();
    }

    private void initialize() {
        mAutoHidingHeaderListener = new AutoHidingHeaderListener(mRecyclerView) {
            @Override
            public View getHeaderView() {
                return mAutoHidingHeader;
            }
        };
        mParallaxScrollingListener = new ParallaxScrollingHeaderListener() {
            @Override
            public View getSceneryView() {
                return mParallaxScrollingHeader;
            }

            @Override
            public View getWindowView() {
                return mParallaxScrollingLayout;
            }

            @Override
            public float getVelocity() {
                return mParallaxScrollingVelocity;
            }
        };
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mAutoHidingHeader != null) {
                    mAutoHidingHeaderListener.onScrollStateChanged(recyclerView, newState);
                }
                if (mParallaxScrollingHeader != null) {
                    mParallaxScrollingListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mAutoHidingHeader != null) {
                    mAutoHidingHeaderListener.onScrolled(recyclerView, dx, dy);
                }
                if (mParallaxScrollingHeader != null) {
                    mParallaxScrollingListener.onScrolled(recyclerView, dx, dy);
                }
            }
        });
        mRecyclerView.addOnChangeLayoutManagerListener(new CustomRecyclerView.OnChangeLayoutManagerListener() {
            @Override
            public void onChange(RecyclerView.LayoutManager layout) {

                if (layout instanceof GridLayoutManager) {
                    final GridLayoutManager gridLayoutManager = ((GridLayoutManager) layout);
                    ((GridLayoutManager) layout).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return mRecyclerView.getAdapter().isFullSpan(position) ?
                                    gridLayoutManager.getSpanCount() : 1;
                        }
                    });
                }
                if (mParallaxScrollingLayout != null) {
                    mParallaxScrollingLayout.setTranslationY(0);
                }
                if (mParallaxScrollingHeader != null) {
                    mParallaxScrollingHeader.setTranslationY(0);
                }
            }
        });
    }

    @Override
    public boolean canChildScrollUp() {
        return ViewCompat.canScrollVertically(mRecyclerView, -1);// &&
//                !(mRecyclerView.getChildAt(0) != null
//                        && mRecyclerView.getChildAdapterPosition(mRecyclerView.getChildAt(0)) == 0
//                        && mRecyclerView.getChildAt(0).getScrollY() == 0);
    }


    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mRecyclerView.setOnScrollListener(listener);
    }

    public CustomRecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    public void setAdapter(Adapter<? extends RecyclerView.ViewHolder> adapter) {
        mRecyclerView.setAdapter(adapter);
    }


    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }


    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    /**
     * not recommend to Use {@link #setParallaxScrollingHeaderView} at the same time.
     *
     * @param view
     */
    public void setAutoHidingHeaderView(View view) {
        if (mAutoHidingHeader != null) {
            mFrameLayout.removeView(mAutoHidingHeader);
        }
        mFrameLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mAutoHidingHeader = view;
        mRecyclerView.getAdapter().setAutoHidingHeaderView(view);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollToPosition(0);
            }
        }, 100);
    }

    public void removeAutoHidingHeaderView() {
        if (mAutoHidingHeader != null) {
            mFrameLayout.removeView(mAutoHidingHeader);
        }

        mAutoHidingHeader = null;
        mRecyclerView.getAdapter().setAutoHidingHeaderView(null);
        mRecyclerView.getAdapter().notifyHeaderViewChanged();
    }

    public View getAutoHidingHeaderView() {
        return mAutoHidingHeader;
    }

    /**
     * Not recommend to Use {@link #setAutoHidingHeaderView} at the same time.
     *
     * @param view The ParallaxScrollingHeaderView
     */
    public void setParallaxScrollingHeaderView(View view) {
        if (mParallaxScrollingHeader != null) {
            mParallaxScrollingLayout.removeView(mParallaxScrollingHeader);
        }
        mParallaxScrollingLayout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mParallaxScrollingHeader = view;
        mRecyclerView.getAdapter().setParallaxScrollingHeaderView(view);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollToPosition(0);
            }
        }, 100);
    }

    public void removeParallaxScrollingHeaderView() {
        if (mParallaxScrollingHeader != null) {
            mFrameLayout.removeView(mParallaxScrollingHeader);
        }

        mParallaxScrollingHeader = null;
        mRecyclerView.getAdapter().setParallaxScrollingHeaderView(null);
        mRecyclerView.getAdapter().notifyHeaderViewChanged();
    }

    public View getParallaxScrollingHeaderView() {
        return mParallaxScrollingHeader;
    }

    /**
     * set header velocity relative proportion with scroll
     *
     * @param velocity recommend 0 to 1
     */
    public void setParallaxScrollingVelocity(float velocity) {
        mParallaxScrollingVelocity = velocity;
    }


    public void setRecyclerViewCilpChildren(boolean clipChildren) {
        mRecyclerView.setClipChildren(clipChildren);
    }


    public void setRecyclerViewClipToPadding(boolean clipToPadding) {
        mRecyclerView.setClipToPadding(clipToPadding);
    }


    public void setRecyclerViewPadding(int left, int top, int right, int bottom) {
        mRecyclerView.setPadding(left, top, right, bottom);
    }


    public void setRecyclerViewItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }


    public void setRecyclerViewItemAnimator(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }


    public void setRecyclerViewItemAnimator(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }

    public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends CustomAdapter<VH> {
    }
}