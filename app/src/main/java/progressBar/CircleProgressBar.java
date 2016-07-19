package progressBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import first.progressbar.R;

/**
 * Created by dell on 2016/7/18.
 */
public class CircleProgressBar extends Progrssbar{

    private int mRadius = dp2px(40);
    private int mMaxPaintWidth;
    private RectF mRectf = null;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);

        mReachHeight = (int) (mUnreachHeight * 2.5f);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mRadius = (int) ta.getDimension(R.styleable.CircleProgressBar_radius,mRadius);

        ta.recycle();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);


    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth = Math.max(mReachHeight,mUnreachHeight);
        int except = mMaxPaintWidth * 2 + mRadius * 2 + getPaddingLeft()+getPaddingRight();
        int mWidth = resolveSize(except,widthMeasureSpec);
        int mHeight = resolveSize(except,heightMeasureSpec);

        int mRealWidth = Math.min(mWidth,mHeight);

        mRadius = (mRealWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth * 2) / 2;

        setMeasuredDimension(mRealWidth,mRealWidth);
        Log.d("TAG",heightMeasureSpec+"");

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        canvas.save();
        String text = getProgress() + "%";
        float mTextWidth = mPaint.measureText(text);
        float mTextHeight = (mPaint.descent() + mPaint.ascent()) ;
        canvas.translate(getPaddingLeft(), getPaddingTop());

        //paint unReach
        mPaint.setColor(mUnreachColor);
        mPaint.setStrokeWidth(mUnreachHeight);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        //paint Reach
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;


        if(mRectf == null) {
            mRectf = new RectF(0,0,mRadius*2,mRadius*2);
        }
        canvas.drawArc(mRectf, 0, sweepAngle, false, mPaint);

        /**
         * paint text
         * */

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, mRadius - mTextWidth / 2, mRadius - mTextHeight / 2, mPaint);
//        //paint unReach
//        mPaint.setColor(mUnreachColor);
//        mPaint.setStrokeWidth(mUnreachHeight);
//        canvas.drawCircle(y,360,mRadius + mUnreachHeight / 2,mPaint);
//        //paint text
//
//        mPaint.setColor(mTextColor);
//        mPaint.setTextSize(mTextsize);
//        canvas.translate(mRadius,mRadius);
//        canvas.drawText(text, mRadius, mRadius, mPaint);
//        canvas.restore();
    }
}
