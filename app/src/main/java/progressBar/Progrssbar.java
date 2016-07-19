package progressBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;

import first.progressbar.R;

/**
 * Created by dell on 2016/7/16.
 */
public class Progrssbar extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#FFDAB9");
    private static final int DEFAULT_UNREACH_COLOR = Color.parseColor("#00F5FF");
    private static final int DEFAULT_UNREACH_HEIGHT = 2;//dp
    private static final int DEFAULT_REACH_COLOR = Color.parseColor("#FFDAB9");
    private static final int DEFAULT_REACH_HEIGHT = 5;//dp
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp


    protected int mTextsize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mUnreachHeight = DEFAULT_UNREACH_HEIGHT;
    protected int mUnreachColor = DEFAULT_UNREACH_COLOR;
    protected int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    protected int mReachColor = DEFAULT_REACH_COLOR;
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
    protected Paint mPaint = new Paint();
    protected int mRealWidth;

    public Progrssbar(Context context) {
        this(context, null);
    }

    public Progrssbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public Progrssbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs);
        mPaint.setTextSize(mTextsize);
    }

    /**
     * 获取自定义属性
     * */
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.Progrssbar);
        mTextsize = (int) ta.getDimension(R.styleable.Progrssbar_progress_text_size,mTextsize);
        mTextColor = ta.getColor(R.styleable.Progrssbar_progress_text_color,mTextColor);
        mUnreachHeight = (int) ta.getDimension(R.styleable.Progrssbar_progress_unreach_height,mUnreachHeight);
        mUnreachColor = ta.getColor(R.styleable.Progrssbar_progress_unreach_color, mUnreachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.Progrssbar_progress_reach_height,mReachHeight);
        mReachColor = ta.getColor(R.styleable.Progrssbar_progress_reach_color,mReachColor);
        mTextOffset = (int) ta.getDimension(R.styleable.Progrssbar_progress_text_offset,mTextOffset);
        ta.recycle();
    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics());
    }

    public int sp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,dpVal,getResources().getDisplayMetrics());
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int mWidthMoble = MeasureSpec.getMode(widthMeasureSpec);
        Log.d("TAG",widthMeasureSpec+"------------");
        int mWidthValue = MeasureSpec.getSize(widthMeasureSpec);
        int height =  MeasureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidthValue,height);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);

        String text = getProgress()+"%";
        float textWidth = mPaint.measureText(text);

        boolean noNeedUnReach = false;
        float radius = getProgress() * 1.0f / getMax();
        float progressX = radius * mRealWidth;
        if(progressX + textWidth >mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnReach = true;
        }
        float endX = progressX - mTextOffset / 2;
        //draw reachProgress
        if(endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,endX,0,mPaint);

        }

        //draw text
        mPaint.setColor(mTextColor);
        int y = (int) -((mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, progressX, y, mPaint);

        //draw unReachProgress
        if(!noNeedUnReach) {
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachHeight);
            int mStartX = (int) (progressX + textWidth + mTextOffset / 2);
            canvas.drawLine(mStartX, 0, mRealWidth, 0, mPaint);

        }
        canvas.restore();

    }

    /**
     * 测量高度
     * */
    private int MeasureHeight(int heightMeasureSpec) {

        int mResult = 0;
        int mHeightMoble = MeasureSpec.getMode(heightMeasureSpec);
        int mHeightValue = MeasureSpec.getSize(heightMeasureSpec);

        //如果用户给的是精确值
        if(mHeightMoble == MeasureSpec.EXACTLY) {
            mResult = mHeightValue;
        }else {
            int mTextHeight = (int) (mPaint.descent() + mPaint.ascent());
            mResult = getPaddingTop() + getPaddingBottom() + Math.max(Math.abs(mTextHeight),Math.max(
                    mReachHeight,mUnreachHeight
            ));
            if(mHeightMoble == MeasureSpec.AT_MOST) {
               mResult = Math.min(mResult,mHeightValue);
            }

        }

        return mResult;
    }
}
