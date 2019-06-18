package com.hjb.tldxdy.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 *
 * 1，使用paint绘制正弦函数（调用Math.sin(x)的方法）
 * 2，使用逐帧动画来实现
 * 3，使用贝塞尔三阶来实现波浪效果
 */
public class RippleView extends View {


    private int left, top, right, bottom;

    //画笔
    private Paint mPaint;
    //路径
    private Path mPath;

    int mColor;

    /**
     * 移动距离
     */
    private int dx = 0;
    /**
     * 波浪起伏距离
     */
    private int waveWidth;
    private int waveHeight;

    private int amplitude;


    public RippleView(Context context) {
        this(context,null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mColor = Color.parseColor("#ff0000");
        waveWidth = getWidth();
        waveHeight = getHeight();
        amplitude = getHeight()/2;
        init();
    }

    private void init() {

        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //初始化路径
        mPath = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.left = 0;
        this.right = getWidth();
        this.top = 0;
        this.bottom = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //不断的计算波浪的路径
        calculatePath();
        //绘制水部分
        canvas.drawPath(mPath, mPaint);
    }
    //角频率， 控制周期
    private double angularFrequency = 8 * 1.0f / 4;
    //初次相位角,
    private double phaseAngle = 0 * Math.PI / 180 + Math.PI / 2 * -1;
    private void calculatePath() {

        mPath.moveTo(0, bottom);
        for (int i = 0; i < right; i++) {
            double angle = i * 1F / right * 2 * Math.PI;
            double y = amplitude * Math.sin(angle * angularFrequency + phaseAngle);
            mPath.lineTo(i, (float) (y + bottom / 2));
        }
        mPath.lineTo(right, bottom + 1);

    }
    ValueAnimator animator;
    //开始动画
    public void startAnimation() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                dx = (int) (waveWidth * fraction);
                postInvalidate();
            }
        });
        animator.start();
    }
}
