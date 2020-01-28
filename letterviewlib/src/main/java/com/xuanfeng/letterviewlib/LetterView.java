package com.xuanfeng.letterviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

public class LetterView extends View {

    private static final String[] DEFAULT_LETTERS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    private int mSelectedPosition = -1;//选中的位置
    private Context mContext;
    private float mLetterHeight;//字母高度
    private Paint mPaint;
    private List<String> mList;
    private int mCommonColor;//未选中字母颜色
    private int mSelectedColor;//选中字母颜色
    private LetterChangeListener mListener;

    public LetterView(Context context) {
        this(context, null);
    }

    public LetterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.LetterView, defStyleAttr, 0);
        float letterSize = typedArray.getDimension(R.styleable.LetterView_letter_size, sp2px(12));
        mLetterHeight = typedArray.getDimension(R.styleable.LetterView_letter_height, dp2px(12));
        mCommonColor = typedArray.getColor(R.styleable.LetterView_common_color, Color.parseColor("#000000"));
        mSelectedColor = typedArray.getColor(R.styleable.LetterView_selected_color, Color.parseColor("#FF0000"));

        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(letterSize);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mList = Arrays.asList(DEFAULT_LETTERS);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width;
        int height;

        //处理宽
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = (int) (getPaddingLeft() + getPaddingRight() + getMaxWidth());
        }

        //处理高
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
            mLetterHeight = height * 1.0f / mList.size();//总高度固定的话，每个字母高度自适应
        } else {
            height = (int) (getPaddingTop() + getPaddingBottom() + mLetterHeight * mList.size());
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mList.size(); i++) {
            if (i == mSelectedPosition) {
                mPaint.setColor(mSelectedColor);
            } else {
                mPaint.setColor(mCommonColor);
            }
            float baseLine = mLetterHeight / 2.0f + (mPaint.descent() - mPaint.ascent()) / 2 - mPaint.descent();
            canvas.drawText(mList.get(i), (getWidth() - mPaint.measureText(mList.get(i))) / 2, getPaddingTop() + baseLine + i * mLetterHeight, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        int touchPos = (int) (y / mLetterHeight);
        if (touchPos < 0 || touchPos > mList.size() - 1) {//滑动超出范围
            return true;
        }
        String content = mList.get(touchPos);
        if (mListener != null) {
            mListener.onLetterChange(content);
        }
        mSelectedPosition = touchPos;
        invalidate();
        return true;
    }

    private int dp2px(int dp) {
        return (int) (mContext.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    private int sp2px(int sp) {
        return (int) (mContext.getResources().getDisplayMetrics().scaledDensity * sp + 0.5f);
    }

    public interface LetterChangeListener {
        void onLetterChange(String letter);
    }

    public void setListener(LetterChangeListener listener) {
        mListener = listener;
    }

    public void setData(List<String> list) {
        mList = list;
    }

    //从所有字符串中，找出最大的宽度
    private float getMaxWidth() {
        float result = 0;
        if (mList != null && !mList.isEmpty()) {
            for (String ss : mList) {
                float temp = mPaint.measureText(ss);
                if (temp > result) {
                    result = temp;
                }
            }
        }
        return result;
    }
}
