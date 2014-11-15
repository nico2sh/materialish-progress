package com.pnikosis.materialishprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * An indicator of progress, similar to Android's ProgressBar.
 * Can be used in 'spin mode' or 'increment mode'
 *
 * @author Todd Davies
 *         <p/>
 *         Licensed under the Creative Commons Attribution 3.0 license see:
 *         http://creativecommons.org/licenses/by/3.0/
 */
public class ProgressWheel extends View {
    private static final String TAG = ProgressWheel.class.getSimpleName();

    //Sizes (with defaults)
    private int circleRadius = 80;
    private boolean fillRadius = false;

    private final int barLength = 40;
    private final int barMaxLength = 270;
    private double timeStartGrowing = 0;
    private double barSpinCycleTime = 1000;
    private float barExtraLength = 0;
    private boolean barGrowingFromFront = true;

    private long pausedTimeWithoutGrowing = 0;
    private final long pauseGrowingTime = 300;
    private int barWidth = 5;
    private int rimWidth = 5;

    //Colors (with defaults)
    private int barColor = 0xAA000000;
    private int rimColor = 0x00FFFFFF;

    //Paints
    private Paint barPaint = new Paint();
    private Paint rimPaint = new Paint();

    //Rectangles
    private RectF circleBounds = new RectF();

    //Animation
    //The amount of full circles per second
    private float spinSpeed = 0.75f;
    // The last time the spinner was animated
    private long lastTimeAnimated = 0;

    private float mProgress = 0.0f;
    private float mTargetProgress = 0.0f;
    private boolean isSpinning = false;

    /**
     * The constructor for the ProgressWheel
     *
     * @param context
     * @param attrs
     */
    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.ProgressWheel));
    }

    /**
     * The constructor for the ProgressWheel
     *
     * @param context
     */
    public ProgressWheel(Context context) {
        super(context);
    }

    //----------------------------------
    //Setting up stuff
    //----------------------------------

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = circleRadius + this.getPaddingLeft() + this.getPaddingRight();
        int viewHeight = circleRadius + this.getPaddingTop() + this.getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(viewWidth, widthSize);
        } else {
            //Be whatever you want
            width = viewWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(viewHeight, heightSize);
        } else {
            //Be whatever you want
            height = viewHeight;
        }

        setMeasuredDimension(width, height);
    }

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setupBounds(w, h);
        setupPaints();
        invalidate();
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds(int layout_width, int layout_height) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        if (!fillRadius) {
            // Width should equal to Height, find the min value to setup the circle
            int minValue = Math.min(layout_width - paddingLeft - paddingRight,
                    layout_height - paddingBottom - paddingTop);

            int circleDiameter = Math.min(minValue, circleRadius * 2 - barWidth * 2);

            // Calc the Offset if needed for centering the wheel in the available space
            int xOffset = (layout_width - paddingLeft - paddingRight - circleDiameter) / 2 + paddingLeft;
            int yOffset = (layout_height - paddingTop - paddingBottom - circleDiameter) / 2 + paddingTop;

            circleBounds = new RectF(xOffset + barWidth,
                    yOffset + barWidth,
                    xOffset + circleDiameter - barWidth,
                    yOffset + circleDiameter - barWidth);
        } else {
            circleBounds = new RectF(paddingLeft + barWidth,
                    paddingTop + barWidth,
                    layout_width - paddingRight - barWidth,
                    layout_height - paddingBottom - barWidth);
        }
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        barWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, barWidth, metrics);
        rimWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rimWidth, metrics);

        circleRadius = (int) a.getDimension(R.styleable.ProgressWheel_circleRadius, circleRadius);

        fillRadius = a.getBoolean(R.styleable.ProgressWheel_fillRadius, false);

        barWidth = (int) a.getDimension(R.styleable.ProgressWheel_barWidth, barWidth);

        rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_rimWidth, rimWidth);

        spinSpeed = a.getFloat(R.styleable.ProgressWheel_spinSpeed, spinSpeed);

        barSpinCycleTime = a.getInt(R.styleable.ProgressWheel_barSpinCycleTime, (int) barSpinCycleTime);

        barColor = a.getColor(R.styleable.ProgressWheel_barColor, barColor);

        rimColor = a.getColor(R.styleable.ProgressWheel_rimColor, rimColor);

        if (a.getBoolean(R.styleable.ProgressWheel_progressIndeterminate, false)) {
            spin();
        }

        // Recycle
        a.recycle();
    }

    //----------------------------------
    //Animation stuff
    //----------------------------------

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);

        boolean mustInvalidate = false;

        if (isSpinning) {
            //Draw the spinning bar
            mustInvalidate = true;

            long deltaTime = (SystemClock.uptimeMillis() - lastTimeAnimated);
            float deltaNormalized = deltaTime * spinSpeed * 360.0f / 1000.0f;

            updateBarLength(deltaTime);

            mProgress += deltaNormalized;
            if (mProgress > 360) {
                mProgress -= 360f;
            }
            lastTimeAnimated = SystemClock.uptimeMillis();

            float from = mProgress - 90;
            float length = barLength + barExtraLength;

            canvas.drawArc(circleBounds, from, length, false,
                    barPaint);
        } else {
            if (mProgress != mTargetProgress) {
                //We smoothly increase the progress bar
                mustInvalidate = true;

                float deltaTime = (float) (SystemClock.uptimeMillis() - lastTimeAnimated) / 1000;
                float deltaNormalized = deltaTime * spinSpeed * 360.0f;

                mProgress = Math.min(mProgress + deltaNormalized, mTargetProgress);
                lastTimeAnimated = SystemClock.uptimeMillis();
            }

            canvas.drawArc(circleBounds, -90, mProgress, false, barPaint);
        }

        if (mustInvalidate) {
            invalidate();
        }
    }

    private void updateBarLength(long deltaTimeInMilliSeconds) {
        if (pausedTimeWithoutGrowing >= pauseGrowingTime) {
            timeStartGrowing += deltaTimeInMilliSeconds;

            if (timeStartGrowing > barSpinCycleTime) {
                // We completed a size change cycle
                // (growing or shrinking)
                timeStartGrowing -= barSpinCycleTime;
                timeStartGrowing = 0;
                if(!barGrowingFromFront) {
                    pausedTimeWithoutGrowing = 0;
                }
                barGrowingFromFront = !barGrowingFromFront;
            }

            float distance = (float) Math.cos((timeStartGrowing / barSpinCycleTime + 1) * Math.PI) / 2 + 0.5f;
            float destLength = (barMaxLength - barLength);

            if (barGrowingFromFront) {
                barExtraLength = distance * destLength;
            } else {
                float newLength = destLength * (1 - distance);
                mProgress += (barExtraLength - newLength);
                barExtraLength = newLength;
            }
        } else {
            pausedTimeWithoutGrowing += deltaTimeInMilliSeconds;
        }
    }

    /**
     * Check if the wheel is currently spinning
     */

    public boolean isSpinning() {
        return isSpinning;
    }

    /**
     * Reset the count (in increment mode)
     */
    public void resetCount() {
        mProgress = 0.0f;
        mTargetProgress = 0.0f;
        invalidate();
    }

    /**
     * Turn off spin mode
     */
    public void stopSpinning() {
        isSpinning = false;
        mProgress = 0.0f;
        mTargetProgress = 0.0f;
        invalidate();
    }

    /**
     * Puts the view on spin mode
     */
    public void spin() {
        lastTimeAnimated = SystemClock.uptimeMillis();
        isSpinning = true;
        invalidate();
    }

    /**
     * Set the progress to a specific value
     */
    public void setProgress(float progress) {
        if (isSpinning) {
            mProgress = 0.0f;
            isSpinning = false;
        }

        if (progress == mTargetProgress) {
            return;
        }

        // If we are currently in the right position
        // we set again the last time animated so the
        // animation starts smooth from here
        if (mProgress == mTargetProgress) {
            lastTimeAnimated = SystemClock.uptimeMillis();
        }

        mTargetProgress = Math.min(progress * 360.0f, 360.0f);

        invalidate();
    }

    public float getProgress() {
        return mProgress / 360.0f;
    }

    //----------------------------------
    //Getters + setters
    //----------------------------------
    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public int getRimColor() {
        return rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
    }

    public float getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(float spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
    }
}
