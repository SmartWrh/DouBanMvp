package com.wrh.mvp.doubanmvp.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.wrh.mvp.doubanmvp.R;

/**
 * Created by user on 2016/6/3.
 */
public class LabelView extends View {

    private int mWidth;
    private int mHeight;
    private int mTriangleHeight;
    private int mColor;
    private int mTxtColor;
    private String mText;

    private Paint mPaint;
    private Paint mTxtPaint;

    public LabelView(Context context) {
        super(context);
        init();
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.LabelView);
        mColor = a.getColor(R.styleable.LabelView_labelColor, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mTxtColor = a.getColor(R.styleable.LabelView_textColor, Color.WHITE);
        mText = a.getString(R.styleable.LabelView_text);
        a.recycle();
        init();
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        mTxtPaint = new Paint();
        mTxtPaint.setColor(mTxtColor);
        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setStyle(Paint.Style.STROKE);
        mTxtPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setText(String text) {
        this.mText = text;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mTriangleHeight = Math.min(mWidth, mHeight) / 2;
        mTxtPaint.setTextSize(mTriangleHeight / 8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.moveTo(mWidth, mHeight);
        path.lineTo(mWidth, mHeight - mTriangleHeight);
        path.lineTo(mWidth - mTriangleHeight, mHeight);
        path.close();
        canvas.drawPath(path, mPaint);

        Path textPath = new Path();
        textPath.moveTo(mWidth - mTriangleHeight / 2, mHeight);
        textPath.lineTo(mWidth, mHeight - mTriangleHeight / 2);
        canvas.drawTextOnPath(mText, textPath, 0, 0, mTxtPaint);
    }

}
