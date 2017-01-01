package yizx.createshortcut;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by yizx on 8/7/15.
 */
public class DragListView extends ListView {
    private final float mAlpha = 0.9f;
    private ImageView mDragView;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private int mStartPos;
    private int mCurrentPos;
    private int mScaledTouchSlop;
    private int mDragOffsetX, mDragOffsetY;
    private int mDragPointX, mDragPointY;
    private int mUpperBound, mLowerBound;
    private DropViewListener mDropViewListener;

    public DragListView(Context context) {
        super(context);
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    public DragListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                final int itemPos = pointToPosition(x, y);
                if (itemPos == AdapterView.INVALID_POSITION) {
                    break;
                }
                final ViewGroup item = (ViewGroup) getChildAt(itemPos - getFirstVisiblePosition());
                mDragPointX = x - item.getLeft();
                mDragPointY = y - item.getTop();
                mDragOffsetX = ((int) ev.getRawX()) - x;
                mDragOffsetY = ((int) ev.getRawY()) - y;
                item.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final int height = getHeight();
                        mUpperBound = Math.min(y - mScaledTouchSlop, height / 3);
                        mLowerBound = Math.max(y + mScaledTouchSlop, height * 2 / 3);
                        mCurrentPos = mStartPos = itemPos;
                        item.setDrawingCacheEnabled(true);
                        Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
                        startDragging(bitmap, x, y);
                        return true;
                    }
                });
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void startDragging(Bitmap bitmap, int x, int y) {
        stopDragging();
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.x = x - mDragPointX + mDragOffsetX;
        mLayoutParams.y = y - mDragPointY + mDragOffsetY;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.windowAnimations = 0;

        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(bitmap);
        imageView.setPadding(0, 0, 0, 0);
        mWindowManager.addView(imageView, mLayoutParams);
        mDragView = imageView;
    }

    public void stopDragging() {
        if (mDragView != null) {
            mWindowManager.removeView(mDragView);
            mDragView.setImageDrawable(null);
            mDragView = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mDragView != null && mCurrentPos != INVALID_POSITION && mDropViewListener != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                    stopDragging();
                    if (mCurrentPos >= 0 && mCurrentPos < getCount()) {
                        mDropViewListener.drop(mStartPos, mCurrentPos);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    dragView(x, y);
                    if (y >= getHeight() / 3) {
                        mUpperBound = getHeight() / 3;
                    }
                    if (y <= getHeight() * 2 / 3) {
                        mLowerBound = getHeight() * 2 / 3;
                    }
                    int speed = 0;
                    if (y > mLowerBound) {
                        if (getLastVisiblePosition() < getCount() - 1) {
                            speed = y > (getHeight() + mLowerBound) / 2 ? 16 : 4;
                        } else {
                            speed = 1;
                        }
                    } else if(y < mUpperBound){
                        speed = y < mUpperBound/2 ? -16 : -4;
                        if(getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= getPaddingTop()){
                            speed = 0;
                        }
                    }
                    if(speed != 0){
                        smoothScrollBy(speed, 30);
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void dragView(int x, int y) {
        if(mDragView != null){
            mLayoutParams.alpha = mAlpha;
            mLayoutParams.x = x - mDragPointX + mDragOffsetX;
            mLayoutParams.y = y - mDragPointY + mDragOffsetY;
            mWindowManager.updateViewLayout(mDragView, mLayoutParams);
        }
        int tempPos = pointToPosition(0, y);
        if(tempPos != INVALID_POSITION){
            mCurrentPos = tempPos;
        }
        int scrollY = 0;
        if(y < mUpperBound){
            scrollY = 8;
        } else {
            scrollY = -8;
        }
        if(scrollY != 0){
            int top = getChildAt(mCurrentPos - getFirstVisiblePosition()).getTop();
            setSelectionFromTop(mCurrentPos, top+scrollY);
        }
    }

    public void setDropViewListener(DropViewListener dropViewListener) {
        this.mDropViewListener = dropViewListener;
    }

    public interface DropViewListener {
        void drop(int from, int to);
    }

}
