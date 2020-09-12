package com.weirm.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class BoatView extends View {
    //一个波长的长度
    private final int INT_WAVE_LENGTH = 1000;
    private Paint mPaint;
    private Path mPath;
    private Integer move = 0;
    private Bitmap mBitMap;
    private Matrix mMatrix;
    private PathMeasure mPathMeasure;

    public BoatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.RED);
        mPath = new Path();
        mMatrix = new Matrix();
        BitmapFactory.Options options = new BitmapFactory.Options();
        mBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_boat, options);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        int orginy = getHeight() / 2;
        mPath.moveTo(-INT_WAVE_LENGTH + move, orginy);
        int half = INT_WAVE_LENGTH / 2;
        for (int i = -INT_WAVE_LENGTH; i < getWidth() + INT_WAVE_LENGTH; i += INT_WAVE_LENGTH) {
            mPath.rQuadTo(half / 2, 80, half, 0);
            mPath.rQuadTo(half / 2, -80, half, 0);
        }
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        mPathMeasure = new PathMeasure(mPath, false);
        float length = mPathMeasure.getLength();
        mMatrix.reset();
        mPathMeasure.getMatrix(getWidth() + 500 - move, mMatrix, PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);
        mMatrix.preTranslate(-mBitMap.getWidth() / 2, -mBitMap.getHeight());
        canvas.drawBitmap(mBitMap, mMatrix, mPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startAnimation();
    }

    public void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, INT_WAVE_LENGTH);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                move = (Integer) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.start();
    }
}
