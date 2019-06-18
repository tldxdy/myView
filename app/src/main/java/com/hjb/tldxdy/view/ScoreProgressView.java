package com.hjb.tldxdy.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

public class ScoreProgressView extends View {

    int[] resIds_waves;
    private int phaseAngle;

    int[] resIds_bubble;

    /**
     * 进度
     */
    private Paint prPaint;
    int prColor;
    private Paint prPaint1,prPaint2,prPaint3;
    int prColor1,prColor2;

    private int left, top, right, bottom;

    private float rippleWidth;

    private Paint textPaint,text1Paint;

    private int margin,margin1;

    private int mRingWidth;


    private float score = 0;


    public ScoreProgressView(Context context) {
        this(context, null);
    }

    public ScoreProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScoreProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();


    }

    private void init() {
        prPaint = new Paint();
        prPaint.setAntiAlias(true);// 抗锯齿效果
        prPaint.setStyle(Paint.Style.STROKE);
        prPaint.setStrokeCap(Paint.Cap.ROUND);// 圆形笔头

        prColor = Color.parseColor("#2E53A7");
        prPaint1 = new Paint();
        prPaint1.setAntiAlias(true);// 抗锯齿效果
        prPaint1.setStyle(Paint.Style.STROKE);
        prPaint1.setStrokeCap(Paint.Cap.ROUND);// 圆形笔头
        prPaint1.setColor(prColor);

        prColor1 = Color.parseColor("#142651");
        prPaint2 = new Paint();
        prPaint2.setAntiAlias(true);// 抗锯齿效果
        prPaint2.setStyle(Paint.Style.FILL);
        prPaint2.setColor(prColor1);

        prColor2 = Color.parseColor("#2E53A7");
        prPaint3 = new Paint();
        prPaint3.setAntiAlias(true);// 抗锯齿效果
        prPaint3.setStyle(Paint.Style.STROKE);
        prPaint3.setColor(prColor2);



        textPaint  = new TextPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(sp2px(getContext(),17));
        textPaint.setColor(Color.parseColor("#FFFFFF"));

        text1Paint  = new TextPaint();
        text1Paint.setTextAlign(Paint.Align.CENTER);
        text1Paint.setAntiAlias(true);
        text1Paint.setTextSize(sp2px(getContext(),9));
        text1Paint.setColor(Color.parseColor("#FFFFFF"));
    }
    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.left = 0;
        this.top = 0;

        this.right = getWidth();
        this.bottom = getHeight();
        rippleWidth = dp2px(getContext(),100);
        margin = dp2px(getContext(),22);
        margin1 = dp2px(getContext(),12);

        LinearGradient linearGradient = new LinearGradient(
                right - margin,bottom - margin,right + margin,bottom + margin,
                new int[]{Color.parseColor("#159FF1"), Color.parseColor("#00F4F4")},null, Shader.TileMode.MIRROR);
        prPaint.setShader(linearGradient);
    }
    Bitmap bitmap,bitmap2;
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (resIds_waves != null) {
            //获取圆心
            float x = right /2;
            float y = bottom /2;

            //如果未设置半径，则半径的值为view的宽、高一半的较小值
            float radius2 =  Math.min((this.right - 2*margin1)/2,(this.bottom - 2*margin1)/2) ;
            float ringWidth2 = dp2px(getContext(),6);
            //由于圆环本身有宽度，所以半径要减去圆环宽度的一半，不然一部分圆会在view外面
            radius2 = radius2 - ringWidth2 / 2;

            canvas.drawRoundRect(new RectF(x - radius2,y - radius2,x + radius2, x + radius2),x,y,prPaint2);
            canvas.drawRoundRect(new RectF(x - radius2-1,y - radius2-1,x + radius2 + 1, x + radius2 + 1),x,y,prPaint3);



            bitmap = BitmapFactory.decodeResource(getResources(), resIds_waves[phaseAngle%151]);
            canvas.drawBitmap(bitmap,null,new RectF((right - rippleWidth)/2,(bottom - rippleWidth)/2,(right + rippleWidth)/2,(bottom + rippleWidth)/2),prPaint);
            bitmap = null;

            canvas.drawText( (int)score + "分",x,y - dp2px(getContext(),14),textPaint);

            canvas.drawText("最新睡眠评分",x,y + dp2px(getContext(),2),text1Paint);


            //如果未设置半径，则半径的值为view的宽、高一半的较小值
            float radius =  Math.min((right - 2*margin)/2,(bottom - 2*margin)/2) ;

            float ringWidth = mRingWidth == 0 ? dp2px(getContext(),4) : mRingWidth;
            //由于圆环本身有宽度，所以半径要减去圆环宽度的一半，不然一部分圆会在view外面
            radius = radius - ringWidth / 2;


            prPaint.setStrokeWidth(ringWidth);
            float left = x - radius;
            float top = y - radius;
            float right = x + radius;
            float bottom = y + radius;

            // 旋转画布90度+笔头半径转过的角度
            double radian = radianToAngle(radius,ringWidth);
            double degrees = Math.toDegrees(-2 * Math.PI / 360 * ((score>0 && score < 90)?85:90 + radian));// 90度+
            canvas.save();
            canvas.rotate((float) degrees, x, y);
            if(phaseAngle < score){
                canvas.drawArc(new RectF(left, top, right, bottom), (float) radian, 360f*phaseAngle/100, false, prPaint);
            }else {
                canvas.drawArc(new RectF(left, top, right, bottom), (float) radian, 360f*score/100, false, prPaint);
            }

            canvas.restore();





            //如果未设置半径，则半径的值为view的宽、高一半的较小值
            float radius1 =  Math.min((this.right - 2*margin1)/2,(this.bottom - 2*margin1)/2) ;

            float ringWidth1 = dp2px(getContext(),6);
            //由于圆环本身有宽度，所以半径要减去圆环宽度的一半，不然一部分圆会在view外面
            radius1 = radius1 - ringWidth1 / 2;


            prPaint1.setStrokeWidth(ringWidth1);

            float left1 = x - radius1;
            float top1 = y - radius1;
            float right1 = x + radius1;
            float bottom1 = y + radius1;

            // 旋转画布90度+笔头半径转过的角度
            double radian1 = radianToAngle(radius1,ringWidth1);
            double degrees1 = Math.toDegrees(-2 * Math.PI / 360 * ((score>0 && score < 90)?85:90 + radian));// 90度+
            canvas.save();
            canvas.rotate((float) degrees1, x, y);

            if(phaseAngle < score){
                canvas.drawArc(new RectF(left1, top1, right1, bottom1), (float) radian1, 360f*phaseAngle/100, false, prPaint1);
            }else {
                canvas.drawArc(new RectF(left1, top1, right1, bottom1), (float) radian1, 360f*score/100, false, prPaint1);
            }

            canvas.restore();






            canvas.save();
            double hudu = 2 * Math.PI / 360 * (360f*((score > 0 && score < 90)?score + 1:score)/100f);
            // 旋转画布
            //canvas.rotate((float) degrees, x, y);
            if(phaseAngle > score){
                int num = (phaseAngle - (int)score)/2;
                if(num >= 50){
                    //bitmap2 = BitmapFactory.decodeResource(getResources(), resIds_bubble[resIds_bubble.length - 1]);
                }else {
                    bitmap2 = BitmapFactory.decodeResource(getResources(), resIds_bubble[num%50]);
                    // Matrix类进行图片处理（缩小或者旋转）
                    Matrix matrix = new Matrix();
                    // 缩放原图

                    matrix.postScale(0.5f, 0.5f);
                    // 生成新的图片
                    // 添加到canvas
                    bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(),

                            bitmap2.getHeight(), matrix, true);
                }







                canvas.drawBitmap(bitmap2, x + radius1*(float) Math.cos(hudu - Math.PI/2)  - bitmap2.getWidth()/2f,  y + radius1*(float) Math.sin(hudu - Math.PI/2)  - bitmap2.getHeight()/2f, null);

                //canvas.drawBitmap(dstbmp1, x - bitmap2.getWidth()*(score/100f)  - bitmap2.getWidth()/4,  y - radius2 - (ringWidth1/2+bitmap2.getWidth())*(score/100f) - bitmap2.getHeight()/4, null);
               //canvas.drawBitmap(dstbmp1,null,new RectF(x - 18f/2, y - radius1 - ringWidth1 - 54f/2, x + 18f/2, y - radius1 - ringWidth1 + 54f/2), prPaint1);
                //bitmap2 = null;

            }


            canvas.restore();




        }



    }



    /**
     * 已知圆半径和切线长求弧长公式
     *
     * @param radios
     * @return
     */
    private double radianToAngle(float radios,float mRingWidth) {
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





    ValueAnimator animator;

    //开始动画
    public void deployAnimation() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        animator = ValueAnimator.ofInt(0, metrics.widthPixels);
        animator.setDuration(50000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if((Integer) animation.getAnimatedValue() >= phaseAngle){
                    setPhaseAngle((Integer) animation.getAnimatedValue());
                }
            }
        });

    }

    public void startAnimation(){
        if(null != animator){
            animator.start();
        }
    }

    public void pauseAnimation(){
        if(null != animator){
            animator.pause();
        }
    }

    public void setResIds_waves(int[] resIds_waves) {
        this.resIds_waves = resIds_waves;
    }


    public void setResIds_bubble(int[] resIds_bubble) {
        this.resIds_bubble = resIds_bubble;
    }

    public void setPhaseAngle(Integer phaseAngle) {
        this.phaseAngle = phaseAngle;

        invalidate();
    }

    public void setScore(float score) {
        this.score = score;
    }
}
