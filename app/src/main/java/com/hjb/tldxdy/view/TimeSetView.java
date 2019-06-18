package com.hjb.tldxdy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hjb.tldxdy.R;


public class TimeSetView extends View {


    /**
     * 时间
     */
    int time = 60 * 20;

    int stratTime = 0;

    int endTime = 0;

    int width,heigh;

    int margin;

    Bitmap mBitmap,mBitmap1,mBitmap2;
    Paint mPaint;


    private Paint textPaint,text1Paint;



    private float mRingWidth = 0;

    /**
     * 进度
     */
    private Paint prPaint;

    private int prColor;

    public TimeSetView(Context context) {
        this(context,null);
    }

    public TimeSetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TimeSetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sleeping_line);
        mBitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.sleeping_round);
        mBitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.play_music);
        mPaint = new Paint();


        prColor = Color.parseColor("#1A70FE");
        prPaint = new Paint();
        prPaint.setAntiAlias(true);// 抗锯齿效果
        prPaint.setStyle(Paint.Style.STROKE);
        prPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形笔头
        prPaint.setColor(prColor);

        textPaint  = new TextPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(sp2px(getContext(),16));
        textPaint.setColor(Color.parseColor("#FFFFFF"));

        text1Paint  = new TextPaint();
        text1Paint.setTextAlign(Paint.Align.CENTER);
        text1Paint.setAntiAlias(true);
        text1Paint.setTextSize(sp2px(getContext(),13));
        text1Paint.setColor(Color.parseColor("#FFFFFF"));


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        heigh = getHeight();
        margin = dp2px(getContext(),15);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        canvas.drawBitmap(mBitmap, null,new RectF(0, 0, width, heigh), mPaint);
        //画背景
        canvas.drawBitmap(mBitmap1, null,new RectF(margin, margin, width - margin, heigh - margin), mPaint);

        //获取圆心
        float x = width /2;
        float y = heigh /2;




        canvas.drawText("20:00",x,y - dp2px(getContext(),10),textPaint);

        canvas.drawText("设置时间",x,y + dp2px(getContext(),10),text1Paint);


        //如果未设置半径，则半径的值为view的宽、高一半的较小值
        float radius =  Math.min((width - 2*margin)/2,(heigh - 2*margin)/2) ;

        float ringWidth = mRingWidth == 0 ? dp2px(getContext(),4) : mRingWidth;
        //由于圆环本身有宽度，所以半径要减去圆环宽度的一半，不然一部分圆会在view外面
        radius = radius - ringWidth / 2;


        prPaint.setStrokeWidth(ringWidth);
        float left = x - radius;
        float top = y - radius;
        float right = x + radius;
        float bottom = y + radius;

        // 旋转画布90度+笔头半径转过的角度
        double radian = radianToAngle(radius);
        double degrees = Math.toDegrees(-2 * Math.PI / 360 * (90 + radian));// 90度+
        canvas.save();
        canvas.rotate((float) degrees, x, y);
        canvas.drawArc(new RectF(left, top, right, bottom), (float) radian, 360f*(endTime - stratTime)/time, false, prPaint);
        canvas.restore();

        canvas.save();
        double hudu = 2 * Math.PI / 360 * (360f*(endTime - stratTime)/time);
        double radians = Math.toDegrees(hudu);
        //canvas.drawBitmap(mBitmap2,x - radius -margin,y,mPaint);
        // 旋转画布
        canvas.rotate((float) radians, x, y);

        canvas.drawBitmap(mBitmap2,null,new RectF(x - margin/2, y - radius - margin/2, x + margin/2, y - radius + margin/2), mPaint);

        canvas.restore();
    }


    /**
     * 已知圆半径和切线长求弧长公式
     *
     * @param radios
     * @return
     */
    private double radianToAngle(float radios) {
        double aa = mRingWidth / 2 / radios;
        double asin = Math.asin(aa);
        double radian = Math.toDegrees(asin);
        return radian;
    }


    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStratTime() {
        return stratTime;
    }

    public void setStratTime(int stratTime) {
        this.stratTime = stratTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(int stratTime,int endTime) {
        this.stratTime = stratTime;
        this.endTime = endTime;
        invalidate();
    }
}
