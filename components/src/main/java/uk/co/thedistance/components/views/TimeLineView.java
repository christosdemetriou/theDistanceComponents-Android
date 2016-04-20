package uk.co.thedistance.components.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import uk.co.thedistance.components.R;


public class TimeLineView extends View {

    private String startLabel;
    private String endLabel;
    private int mBarColor;
    private int mStartHour;
    private int mEndHour;
    private Paint mPaint;
    private Paint mSecondaryPaint;
    private final float mBarWidth;
    private boolean mShowMidPoints;
    private int mNumberOfSteps;
    private TextPaint mTextPaint;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private final Calendar calendar = Calendar.getInstance();
    private TimeZone timeZone = TimeZone.getTimeZone("UTC");
    private int mTextHeight;
    private int mTextPadding;

    public TimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TimeLineView,
                0, 0);

        String timeString = a.getString(R.styleable.TimeLineView_tl_timeZone);
        if (timeZone != null) {
            timeZone = TimeZone.getTimeZone(timeString);
        }

        TIME_FORMAT.setTimeZone(timeZone);
        calendar.setTimeZone(timeZone);

        mTextPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics());

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);


        mShowMidPoints = a.getBoolean(R.styleable.TimeLineView_tl_showMidPoints, false);
        mStartHour = a.getInt(R.styleable.TimeLineView_tl_startHour, 0);
        mEndHour = a.getInt(R.styleable.TimeLineView_tl_endHour, 24);

        if (mShowMidPoints) {
            mSecondaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mSecondaryPaint.setStyle(Paint.Style.FILL);
        }

        TypedValue value = new TypedValue();

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        int textSize = a.getDimensionPixelSize(R.styleable.TimeLineView_android_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, getResources().getDisplayMetrics()));
        mTextPaint.setTextSize(textSize);
        int textColor = a.getColor(R.styleable.TimeLineView_android_textColor, Color.BLACK);
        mTextPaint.setColor(textColor);

        updateTimes();

        mBarColor = a.getColor(R.styleable.TimeLineView_tl_barColor, Color.BLACK);
        setBarColor(mBarColor);

        a.getValue(R.styleable.TimeLineView_tl_barWidth, value);
        mBarWidth = value.getDimension(getResources().getDisplayMetrics());

        a.recycle();
    }

    public void setStartHour(int mStartHour) {
        this.mStartHour = mStartHour;
        updateTimes();
    }

    private void updateTimes() {
        mNumberOfSteps = mEndHour + 1 - mStartHour;
        calendar.set(Calendar.HOUR_OF_DAY, mStartHour);
        calendar.set(Calendar.MINUTE, 0);
        startLabel = TIME_FORMAT.format(calendar.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, mEndHour);
        endLabel = TIME_FORMAT.format(calendar.getTime());

        calculateTextSize();
    }

    public void setEndHour(int mEndHour) {
        this.mEndHour = mEndHour;
        updateTimes();
    }

    public int getNumberOfSteps() {
        return mNumberOfSteps - 1;
    }

    public void setTypeface(Typeface typeface) {
        mTextPaint.setTypeface(typeface);
        calculateTextSize();
    }

    private void calculateTextSize() {

        Rect bounds = new Rect();
        mTextPaint.getTextBounds(startLabel, 0, startLabel.length(), bounds);
        mTextHeight = bounds.height();
    }

    public int getBarColor() {
        return mBarColor;
    }

    public void setBarColor(@ColorInt int mBarColor) {
        this.mBarColor = mBarColor;
        mPaint.setColor(mBarColor);
        if (mSecondaryPaint != null) {
            mSecondaryPaint.setColor(mBarColor);
            mSecondaryPaint.setAlpha(77);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(width, height + mTextHeight + mTextPadding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mNumberOfSteps == 0) {
            return;
        }

        final float spacing = (float) (getWidth() - getPaddingLeft() - getPaddingRight() - mBarWidth) / (mNumberOfSteps - 1);
        final int height = getHeight() - mTextHeight - mTextPadding;

        for (int i = 0; i < mNumberOfSteps; i++) {
            canvas.drawRect(
                    getPaddingLeft() + (i * spacing),
                    0,
                    getPaddingLeft() + (i * spacing) + mBarWidth,
                    height,
                    mPaint
            );

            if (mShowMidPoints && i != mNumberOfSteps - 1) {
                canvas.drawRect(
                        getPaddingLeft() + (i * spacing) + (spacing / 2),
                        0,
                        getPaddingLeft() + (i * spacing) + (spacing / 2) + mBarWidth,
                        height / 2,
                        mSecondaryPaint
                );
            }
        }

        canvas.drawText(startLabel, getPaddingLeft(), getHeight(), mTextPaint);
        canvas.drawText(endLabel, getWidth() - mTextPaint.measureText(endLabel) - getPaddingRight(), getHeight(), mTextPaint);
    }

}
