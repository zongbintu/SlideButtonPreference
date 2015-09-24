package com.tu.preference;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 通用滑动开关Button 1、背景图片，滑块组成 2、两种状态背景图片、滑块组成
 *
 * @author Tu
 * @email atlas.tufei@gmail.com
 * Created by atlas on 15/9/24.
 */
public class SlideButtonPreference extends View implements View.OnClickListener {
    private Bitmap mBgOffBitmap, mBgOnBitmap, mSlideBitmap;
    private Paint mPaint;
    private float firstX, lastX;
    private boolean isDrag;
    private boolean isOn;
    private String key;
    private StateChanageListener listenerCallback;

    public SlideButtonPreference(Context context) {
        super(context);
    }

    public SlideButtonPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SlideButtonPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context.obtainStyledAttributes(attrs, R.styleable.SlideButtonPreference));
    }

    private void initView(TypedArray typedArray) {
        int backgroundId = typedArray.getResourceId(
                R.styleable.SlideButtonPreference_background_off, 0);
        int slideBarId = typedArray.getResourceId(
                R.styleable.SlideButtonPreference_slidebar, 0);
        int onBackgroundId = typedArray.getResourceId(
                R.styleable.SlideButtonPreference_background_on, 0);
        if (0 == backgroundId || 0 == slideBarId) {
            throw new IllegalArgumentException("background_off slidebar must be set");
        }
        Resources resources = getResources();
        if (0 != onBackgroundId) {
            mBgOnBitmap = BitmapFactory.decodeResource(resources,
                    onBackgroundId);

        }
        key = typedArray.getString(R.styleable.SlideButtonPreference_key);
        if (TextUtils.isEmpty(key))
            key = SlideButtonPreference.class.getName()+getId();
        mBgOffBitmap = BitmapFactory.decodeResource(resources, backgroundId);
        mSlideBitmap = BitmapFactory.decodeResource(resources, slideBarId);
        isOn = typedArray.getBoolean(R.styleable.SlideButtonPreference_on, false);
        SharedPreferences preferences = getContext().getSharedPreferences(SlideButtonPreference.class.getName(), Context.MODE_PRIVATE);
        isOn = preferences.getBoolean(key, isOn);
        lastX = isOn ? mBgOffBitmap.getWidth() - mSlideBitmap.getWidth() : 0;
        setOnClickListener(this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(mBgOffBitmap.getWidth(), mBgOffBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != mBgOnBitmap && isOn)
            canvas.drawBitmap(mBgOnBitmap, 0, 0, mPaint);
        else {
            canvas.drawBitmap(mBgOffBitmap, 0, 0, mPaint);
        }

        canvas.drawBitmap(mSlideBitmap, lastX, 0, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        // 滑动
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = lastX;
                lastX = event.getX();
                isDrag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - lastX) > 5) {
                    isDrag = true;
                    lastX = event.getX();
                    firstX = lastX = lastX > mBgOffBitmap.getWidth()
                            - mSlideBitmap.getWidth() ? mBgOffBitmap.getWidth()
                            - mSlideBitmap.getWidth() : lastX;
                    firstX = lastX = lastX < 0 ? 0 : lastX;
                    flushView();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    if (lastX * 2 + mSlideBitmap.getWidth() / 2 > mBgOffBitmap
                            .getWidth()) {
                        lastX = mBgOffBitmap.getWidth() - mSlideBitmap.getWidth();
                        flushState(true);
                    } else {
                        lastX = 0;
                        flushState(false);
                    }

                }
                flushView();
                break;
            default:
                break;
        }
        return true;
    }

    private void flushState(boolean newState) {
        if (isOn != newState) {
            isOn = newState;
            if (null != listenerCallback)
                listenerCallback.stateChange(this);
        }
        SharedPreferences preferences = getContext().getSharedPreferences(SlideButtonPreference.class.getName(), Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, isOn).commit();
    }

    @Override
    public void onClick(View v) {
        if (!isDrag) {
            firstX = lastX = mBgOffBitmap.getWidth() - mSlideBitmap.getWidth()
                    - firstX;
            flushState(!isOn);
            flushView();
        }
    }

    private void flushView() {
        invalidate();
    }

    public boolean isOn() {
        return isOn;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBgOffBitmap.recycle();
        mBgOnBitmap.recycle();
        mSlideBitmap.recycle();
        mBgOffBitmap = mBgOnBitmap = mSlideBitmap = null;
        mPaint = null;
    }

    public void setStateChanageListener(StateChanageListener listener) {
        listenerCallback = listener;
    }

    public interface StateChanageListener {
        void stateChange(SlideButtonPreference sb);
    }
}
