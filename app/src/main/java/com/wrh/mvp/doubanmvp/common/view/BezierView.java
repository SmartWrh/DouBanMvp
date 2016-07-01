package com.wrh.mvp.doubanmvp.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2016/6/30.
 */
public class BezierView extends View {

    private Paint mPaint;
    private Path mPath;
    private Point startPoint;
    private Point endPoint;
    // 辅助点
    private Point assistPoint;

    public BezierView(Context context) {
        super(context);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPath = new Path();
        startPoint = new Point(300, 600);
        endPoint = new Point(900, 600);
        assistPoint = new Point(600, 900);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画笔颜色
        mPaint.setColor(Color.BLACK);
// 笔宽
        mPaint.setStrokeWidth(5);
// 空心
        mPaint.setStyle(Paint.Style.STROKE);
// 重置路径
        mPath.reset();
// 起点
        mPath.moveTo(startPoint.x, startPoint.y);
// 重要的就是这句
        mPath.quadTo(assistPoint.x, assistPoint.y, endPoint.x, endPoint.y);
// 画路径
        canvas.drawPath(mPath, mPaint);
// 画辅助点
        canvas.drawPoint(assistPoint.x, assistPoint.y, mPaint);
    }
}
