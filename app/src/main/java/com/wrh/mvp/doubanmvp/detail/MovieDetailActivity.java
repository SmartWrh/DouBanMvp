package com.wrh.mvp.doubanmvp.detail;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wrh.mvp.doubanmvp.R;
import com.wrh.mvp.doubanmvp.common.base.BaseActivity;
import com.wrh.mvp.doubanmvp.home.entity.MovieEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends BaseActivity {

    public static final String MOVIE_ENTITY = MovieDetailActivity.class.getSimpleName() + "movie_entity";
    public static final String BITMAP = MovieDetailActivity.class.getSimpleName() + "bitmap";
    @BindView(R.id.test)
    ImageView mTest;


    private MovieEntity.SubjectsBean mSubjectsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.test);
        ButterKnife.bind(this);

        mSubjectsBean = getIntent().getExtras().getParcelable(MOVIE_ENTITY);
        //  mCollapsingToolbarLayout.setTitle(mSubjectsBean.getTitle());

        //    setTitle(mSubjectsBean.getTitle());

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mByteArray != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(mByteArray, 0, mByteArray.length);
//            mDetailImg.setImageBitmap(bitmap);
//            ViewCompat.setTransitionName(mDetailImg, "feedView");
//        } else {
        Glide.with(this).load(mSubjectsBean.getImages().getLarge()).asBitmap().into(mTest);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //   finishAfterTransition();
        }
    }
}
