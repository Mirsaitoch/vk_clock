package com.cucu.sup.clock;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;
public class ClockView extends View {

    private Paint mPaint;
    private int mWidth, mHeight;
    private int mPadding;
    private int mRadius;
    private boolean mInit;
    private Calendar mCalendar;
    private float mHourDegree, mMinuteDegree, mSecondDegree;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
            handler.postDelayed(this, 1000);
        }
    };


    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        handler.postDelayed(runnable, 1000);
    }

    private void init() {
        mPaint = new Paint();
        mCalendar = Calendar.getInstance();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mPadding = Math.max(Math.max(getPaddingBottom(), getPaddingTop()), Math.max(getPaddingLeft(), getPaddingRight()));
        mRadius = Math.min(mWidth, mHeight) / 2 - mPadding;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawScale(canvas);
        drawHour(canvas);
        drawMinute(canvas);
        drawSecond(canvas);
    }

    private void drawCircle(Canvas canvas) {
        if (!mInit) {
            mPaint.setAntiAlias(true);
            mInit = true;
        }
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaint);
    }

    private void drawScale(Canvas canvas) {
        canvas.save();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        for (int i = 0; i < 12; i++) {
            canvas.drawLine(mWidth / 2, mHeight / 2 - mRadius, mWidth / 2, mHeight / 2 - mRadius + 60, mPaint);
            canvas.rotate(30, mWidth / 2, mHeight / 2);
        }
        canvas.restore();
    }

    private void drawHour(Canvas canvas) {
        mHourDegree = (mCalendar.get(Calendar.HOUR) + mCalendar.get(Calendar.MINUTE) / 60f) * 30;
        canvas.save();
        canvas.rotate(mHourDegree, mWidth / 2, mHeight / 2);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(5);
        canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - mRadius / 2, mPaint);
        canvas.restore();
    }

    private void drawMinute(Canvas canvas) {
        mMinuteDegree = mCalendar.get(Calendar.MINUTE) * 6;
        canvas.save();
        canvas.rotate(mMinuteDegree, mWidth / 2, mHeight / 2);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        canvas.drawLine(mWidth / 2, mHeight / 2,
                mWidth / 2, mHeight / 2 - mRadius * 3 / 4, mPaint);
        canvas.restore();
    }

    private void drawSecond(Canvas canvas) {
        mSecondDegree = mCalendar.get(Calendar.SECOND) * 6;
        canvas.save();
        canvas.rotate(mSecondDegree, mWidth / 2, mHeight / 2);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(1);
        canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mHeight / 2 - mRadius + 30, mPaint);
        canvas.restore();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler.postDelayed(runnable, 1000);

    }

}

