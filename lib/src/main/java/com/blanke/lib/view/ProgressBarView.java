package com.blanke.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.blanke.lib.R;


/**
 * by 12406 on 2016/5/24.
 */
public class ProgressBarView extends View {

    //空心
    public static final int STROKE = 1;
    //实心
    public static final int FILL = 0;


    private Paint mPaint;
    private int style;

    //圆环的宽度
    private float circlesWidth;
    //圆环的颜色
    private int circlesColor;
    //进度字体的粗细程度
    private float textCrude;
    //字体颜色
    private int textColor;
    //字体大小
    private float textSize;
    //设置字体
    private Typeface mTypeface;
    //当前进度
    private int currentProgress;
    //进度颜色
    private int currentProgressColor;
    //进度圆环的宽度
    private float currentScheduleWidth;
    //是否显示百分比
    private boolean isPercent;
    private RectF rectF;
    private int wholeSize;
    private int contentSize;
    private int paddingSize;


    public ProgressBarView(Context context) {
        super(context);
    }

    public ProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mPaint = new Paint();
        rectF = new RectF();
        mPaint.setAntiAlias(true);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclesProgressBar);


        //初始化圆环变量
        circlesWidth = mTypedArray.getDimension(R.styleable.CirclesProgressBar_circlesWidth, ProgressDefaults.CIRCLES_WIDTH);
        circlesColor = mTypedArray.getColor(R.styleable.CirclesProgressBar_circlesColor, ProgressDefaults.CIRCLES_COLOR);
        textCrude = mTypedArray.getDimension(R.styleable.CirclesProgressBar_textCrude, ProgressDefaults.TEXT_CRUDE);
        textColor = mTypedArray.getColor(R.styleable.CirclesProgressBar_textColor, ProgressDefaults.TEXT_COLOR);
        textSize = mTypedArray.getDimension(R.styleable.CirclesProgressBar_textSize, ProgressDefaults.TEXT_SIZE);
        currentProgress = mTypedArray.getInt(R.styleable.CirclesProgressBar_currentProgress, ProgressDefaults.CURRENT_PROGRESS);
        currentProgressColor = mTypedArray.getColor(R.styleable.CirclesProgressBar_currentProgressColor, ProgressDefaults.CURRENT_PROGRESS_COLOR);
        isPercent = mTypedArray.getBoolean(R.styleable.CirclesProgressBar_isPercent, ProgressDefaults.IS_PERCENT);
        style = mTypedArray.getInt(R.styleable.CirclesProgressBar_style, ProgressDefaults.STYLE);
        currentScheduleWidth = mTypedArray.getDimension(R.styleable.CirclesProgressBar_currentScheduleWidth, ProgressDefaults.CURRENT_SCHEDULE_WIDTH);

        mTypedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = getDefaultSize(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec);
        int h = getDefaultSize(MeasureSpec.getSize(heightMeasureSpec), heightMeasureSpec);
        wholeSize = Math.min(w, h);
        setMeasuredDimension(wholeSize, wholeSize);
        paddingSize = Math.max(Math.max(Math.max(getPaddingLeft(), getPaddingTop()), getPaddingRight()), getPaddingBottom());
        contentSize = wholeSize - 2 * paddingSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int circlesRadius = (int) (contentSize / 2 - (currentScheduleWidth - circlesWidth) - circlesWidth / 2);//半径


        //圆环
        mPaint.setColor(circlesColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(circlesWidth);
        canvas.drawCircle(paddingSize + contentSize / 2, paddingSize + contentSize / 2, circlesRadius, mPaint);

        //百分比
        if (isPercent && style == STROKE) {
            mPaint.setStrokeWidth(textCrude);
            mPaint.setColor(textColor);
            mPaint.setTextSize(textSize);
            mPaint.setTypeface(mTypeface);
            int percent = (int) (((float) currentProgress / (float) ProgressDefaults.PROGRESS_BAR_MAX) * 100);
            float textWidth = mPaint.measureText(percent + "%");
            canvas.drawText(percent + "%", contentSize / 2 - textWidth / 2, contentSize / 2 + textSize / 2, mPaint);
        }


        //进度的圆环
        mPaint.setColor(currentProgressColor);
        mPaint.setStrokeWidth(currentScheduleWidth);
        rectF.set(currentScheduleWidth / 2 + paddingSize, currentScheduleWidth / 2 + paddingSize,
                wholeSize - paddingSize - currentScheduleWidth / 2, wholeSize - paddingSize - currentScheduleWidth / 2);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //选择风格
        switch (style) {

            case STROKE:
                if (currentProgress != 0) {
                    mPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(rectF, -90, 360 * currentProgress / ProgressDefaults.PROGRESS_BAR_MAX, false, mPaint);
                }
                break;
            case FILL:
                if (currentProgress != 0) {
                    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    canvas.drawArc(rectF, -90, 360 * currentProgress / ProgressDefaults.PROGRESS_BAR_MAX, true, mPaint);
                }
                break;
        }
    }


    public synchronized void setCurrentProgress(int currentProgress) {
        if (currentProgress < ProgressDefaults.CURRENT_PROGRESS) {
            currentProgress = ProgressDefaults.CURRENT_PROGRESS;
        }
        if (currentProgress > ProgressDefaults.PROGRESS_BAR_MAX) {
            currentProgress = ProgressDefaults.PROGRESS_BAR_MAX;
        }
        if (currentProgress <= ProgressDefaults.PROGRESS_BAR_MAX) {
            this.currentProgress = currentProgress;
            postInvalidate();
        }
    }

    public float getCurrentProgress() {
        return currentProgress;
    }


    public void setScheduleWidth(float currentScheduleWidth) {
        this.currentScheduleWidth = currentScheduleWidth;
    }

    public float getScheduleWidth() {
        return currentScheduleWidth;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getStyle() {
        return style;
    }

    public void setPercent(boolean isPercent) {
        this.isPercent = isPercent;
    }

    public boolean getPercent() {
        return isPercent;
    }

    public void setCurrentProgressColor(int currentProgressColor) {
        if (currentProgressColor < ProgressDefaults.MIN) {
            currentProgressColor = ProgressDefaults.CURRENT_PROGRESS_COLOR;
        }
        this.currentProgressColor = currentProgressColor;
    }

    public int getCurrentProgressColor() {
        return currentProgressColor;
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }

    public void setCirclesWidth(float circlesWidth) {
        if (circlesWidth < ProgressDefaults.MIN) {
            circlesWidth = ProgressDefaults.CIRCLES_WIDTH;
        }
        this.circlesWidth = circlesWidth;
    }

    public float getCirclesWidth() {
        return circlesWidth;
    }

    public void setCirclesColor(int circlesColor) {
        if (circlesColor < ProgressDefaults.MIN) {
            circlesColor = ProgressDefaults.CIRCLES_COLOR;
        }
        this.circlesColor = circlesColor;
    }

    public int getCirclesColor() {
        return circlesColor;
    }

    public void setTextCrude(float textCrude) {
        if (textCrude < ProgressDefaults.MIN) {
            textCrude = ProgressDefaults.TEXT_CRUDE;
        }
        this.textCrude = textCrude;
    }

    public float getTextCrude() {
        return textCrude;
    }

    public void setTextColor(int textColor) {
        if (textColor < ProgressDefaults.MIN) {
            textColor = ProgressDefaults.TEXT_COLOR;
        }
        this.textColor = textColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextSize(float textSize) {
        if (textSize < ProgressDefaults.MIN) {
            textSize = ProgressDefaults.TEXT_SIZE;
        }
        this.textSize = textSize;
    }
}
