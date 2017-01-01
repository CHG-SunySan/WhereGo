package view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import refresh.RefreshLoadView;

/**
 * 普通大众的recyclerView
 * Created by SunySan on 2016/12/21.
 */
public class CommonRecyclerView extends RecyclerView{
    private RefreshLoadView mRefreshLoadView;
    private OnRefreshCompleteListener mCompleteListener;

    private float mLastY = -1;
    private boolean mScrollTop;



    public CommonRecyclerView(Context context) {
        super(context,null);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mRefreshLoadView = new RefreshLoadView(context);
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScrollTop() && mRefreshLoadView != null) {
                    float deltaY = e.getRawY() - mLastY;
                    mRefreshLoadView.onMove(deltaY / 3);
                    mLastY = e.getRawY();
                    if (mRefreshLoadView.getVisibleHeight() > 0 &&
                            mRefreshLoadView.getState() < mRefreshLoadView.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                mLastY = -1; // reset
                if (mRefreshLoadView != null && mRefreshLoadView.releaseAction()) {
                    if (mCompleteListener != null) {
                        mCompleteListener.onRefreshComplete();
                    }
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    public RefreshLoadView getRefreshView() {
        return mRefreshLoadView == null ? null : mRefreshLoadView;
    }

    public void refreshComplete() {
        mRefreshLoadView.refreshComplete();
    }

    public boolean isScrollTop() {
        if (getLayoutManager() instanceof LinearLayoutManager &&
                ((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() <= 1) {
            mScrollTop = true;
        } else {
            mScrollTop = false;
        }
        return mScrollTop;
    }

    public interface OnRefreshCompleteListener {
        void onRefreshComplete();
    }

    public void setOnRefreshCompleteListener(OnRefreshCompleteListener listener) {
        mCompleteListener = listener;
    }
}
