package com.hjb.tldxdy.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

public class SinusoidalWaveView extends View {

    private int width;
    private int height;

    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path linePath;

    private double amplitude;

    //角频率， 控制周期
    private double angularFrequency = 8 * 1.0f / 6;
    //初次相位角,
    private double phaseAngle = 0 * Math.PI / 180 + Math.PI / 2 * -1;

    public SinusoidalWaveView(Context context,  AttributeSet attrs) {
        super(context, attrs);

        linePaint.setStyle(Paint.Style.FILL);

        linePath = new Path();





    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.width = w;
        this.height = h;

        amplitude = height / 8;

        LinearGradient linearGradient = new LinearGradient(
                0, 0,
                0, getHeight(),
                new int[]{Color.parseColor("#6627345A"), Color.parseColor("#00000000")},null, Shader.TileMode.MIRROR);
        linePaint.setShader(linearGradient);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        linePath.reset();
        linePath.moveTo(0, height);
        for (int i = 0; i < width; i++) {
            double angle = i * 1F / width * 2 * Math.PI;
            double y = amplitude * Math.sin(angle * angularFrequency + phaseAngle);
            linePath.lineTo(i, (float) (y + height / 2));
        }
        linePath.lineTo(width, height + 1);
        canvas.drawPath(linePath, linePaint);



        linePath.reset();
        linePath.moveTo(0, height);
        for (int i = 0; i < width; i++) {
            double angle = i * 1F / width * 2 * Math.PI;
            double y = amplitude * Math.cos(angle * angularFrequency - phaseAngle);
            linePath.lineTo(i, (float) (y + height / 2));
        }
        linePath.lineTo(width, height + 1);
        canvas.drawPath(linePath, linePaint);


        linePath.reset();
        linePath.moveTo(0, height);
        for (int i = 0; i < width; i++) {
            double angle = i * 1F / width * 2 * Math.PI;
            double y = amplitude * Math.cos(angle * angularFrequency - phaseAngle + Math.PI / 2 * -1);
            linePath.lineTo(i, (float) (y + height / 2));
        }
        linePath.lineTo(width, height + 1);
        canvas.drawPath(linePath, linePaint);

    }

    public void setAmplitude(float progress) {
        amplitude = progress * 1.0F / 100 * height / 2;
        invalidate();
    }

    public void setAngularFrequency(float progress) {
        angularFrequency = progress * 1.0F / 4;
        invalidate();
    }

    public void setPhaseAngle(float progress) {
        phaseAngle = progress * Math.PI / 180 + Math.PI / 2 * -1;
        invalidate();
    }

    ValueAnimator animator;
    //开始动画
    public void startAnimation() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        animator = ValueAnimator.ofInt(0, metrics.widthPixels);
        animator.setDuration(8000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setPhaseAngle((Integer) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
