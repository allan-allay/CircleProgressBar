package com.emredavarci.circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 02.08.2017.
 */

public class CircleProgressBar extends View {

    private Paint progressBarPaint;
    private Paint bacgroundPaint;
    private Paint textPaint;

    private float mRadius;
    private RectF mArcBounds = new RectF();

    float drawUpto = 0;

    public CircleProgressBar(Context context) {
        super(context);

        // create the Paint and set its color

    }

    private int progressColor;
    private int backgroundColor;
    private float strokeWidth;
    private boolean roundedCorners;
    private float maxValue;

    private int textColor = Color.BLACK;
    private float textSize = 18;
    private String text = "";
    private String unit = "";

    int defStyleAttr;

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.defStyleAttr = defStyleAttr;
        initPaints(context, attrs);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initPaints(context, attrs);
    }

    private void initPaints(Context context, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);

        progressColor = ta.getColor(R.styleable.CircleProgressBar_progressColor, Color.BLUE);
        backgroundColor = ta.getColor(R.styleable.CircleProgressBar_backgroundColor, Color.GRAY);
        strokeWidth = ta.getFloat(R.styleable.CircleProgressBar_strokeWidth, 10);
        roundedCorners = ta.getBoolean(R.styleable.CircleProgressBar_roundedCorners, false);
        maxValue = ta.getFloat(R.styleable.CircleProgressBar_maxValue, 100);
        textColor = ta.getColor(R.styleable.CircleProgressBar_textColor, Color.BLACK);
        textSize = ta.getDimension(R.styleable.CircleProgressBar_textSize, 18);
        unit = ta.getString(R.styleable.CircleProgressBar_unit);

        progressBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressBarPaint.setStyle(Paint.Style.FILL);
        progressBarPaint.setColor(progressColor);
        progressBarPaint.setStyle(Paint.Style.STROKE);
        progressBarPaint.setStrokeWidth(strokeWidth * getResources().getDisplayMetrics().density);
        if(roundedCorners){
            progressBarPaint.setStrokeCap(Paint.Cap.ROUND);
        }else{
            progressBarPaint.setStrokeCap(Paint.Cap.BUTT);
        }
        String pc = String.format("#%06X", (0xFFFFFF & progressColor));
        progressBarPaint.setColor(Color.parseColor(pc));

        bacgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bacgroundPaint.setStyle(Paint.Style.FILL);
        bacgroundPaint.setColor(backgroundColor);
        bacgroundPaint.setStyle(Paint.Style.STROKE);
        bacgroundPaint.setStrokeWidth(strokeWidth * getResources().getDisplayMetrics().density);
        bacgroundPaint.setStrokeCap(Paint.Cap.SQUARE);
        String bc = String.format("#%06X", (0xFFFFFF & backgroundColor));
        bacgroundPaint.setColor(Color.parseColor(bc));

        ta.recycle();

        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        String c = String.format("#%06X", (0xFFFFFF & textColor));
        textPaint.setColor(Color.parseColor(c));
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        //paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h) / 2f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        float yHeight = getProgress() / (float) getMaxValue() * getHeight();
//        float radius = getWidth() / 2f;
//        float angle = (float) (Math.acos((radius - yHeight) / radius) * 180 / Math.PI);
//        float startAngle = 90 + angle;
//        float sweepAngle = 360 - angle * 2;
//        canvas.drawArc(mArcBounds, startAngle, sweepAngle, false, bacgroundPaint);
//        canvas.drawArc(mArcBounds, 270 - angle, angle * 2, false, progressBarPaint);


        float mouthInset = mRadius / 3;
        mArcBounds.set(mouthInset, mouthInset, mRadius * 2 - mouthInset, mRadius * 2 - mouthInset);
        canvas.drawArc(mArcBounds, 0f, 360f, false, bacgroundPaint);
        canvas.drawArc(mArcBounds, 270f, drawUpto / getMaxValue() * 360, false, progressBarPaint);

        if(TextUtils.isEmpty(unit)){
            unit = "";
        }

        String drawnText = unit + text;

        if (!TextUtils.isEmpty(text)) {
            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(drawnText, (getWidth() - textPaint.measureText(drawnText)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
        }

    }

    public void setProgress(float f){
        drawUpto = f;
        invalidate();
    }

    public float getProgress(){
        return drawUpto;
    }

    public float getProgressPercentage(){
        return drawUpto/getMaxValue() * 100;
    }

    public void setProgressColor(int color){
        progressColor = color;
        invalidate();
    }

    public void setBackgroundColor(int color){
        backgroundColor = color;
        invalidate();
    }

    public float getMaxValue(){
        return maxValue;
    }

    public void setMaxValue(float max){
        maxValue = max;
        invalidate();
    }

    public void setStrokeWidth(float width){
        strokeWidth = width;
        invalidate();
    }

    public void setText(String progressText){
        text = progressText;
        invalidate();
    }

    public String getText(){
        return text;
    }

    public void setTextColor(int color){
        textColor = color;
        textPaint.setColor(color);
        invalidate();
    }

    public void setTextColor(String color){
        String c = String.format("#%06X", (0xFFFFFF & Integer.valueOf(color)));
        textPaint.setColor(Color.parseColor(c));
        invalidate();
    }

    public int getTextColor(){
        return textColor;
    }

    public void setUnit(String unit){
        this.unit = unit;
        invalidate();
    }

    public String getUnit(){
        return unit;
    }
}

