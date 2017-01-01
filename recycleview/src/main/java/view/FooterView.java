package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import sunysan.com.recycleview.R;
import tool.RecyclerUtils;
import animation.LoadType;

/**
 * recycleview底部加载更多
 * Created by SunySan on 2016/12/21.
 */
public class FooterView extends LinearLayout{

    private Context mContext;

    private int mPadding;
    private int mLoadType = -1;

    private View mLoadView;
    private TextView mTextView;

    private LayoutParams mLayoutParams;

    private static final int LOAD_TYPE_CUSTOM = 0x00000001;
    private static final int LOAD_TYPE_CUBES = 0x00000002;
    private static final int LOAD_TYPE_SWAP = 0x00000003;
    private static final int LOAD_TYPE_EAT_BEANS = 0x00000004;
    private static final int NO_MORE = 0x00000005;


    public FooterView(Context context) {
        super(context);
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public FooterView(Context context, AttributeSet attrs,int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.mContext = context;
        this.mPadding = RecyclerUtils.dip2px(mContext,8);
    }

    public void addView(){
        this.removeAllViews();

        mTextView = new TextView(mContext);
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = RecyclerUtils.dip2px(mContext,8);
        mTextView.setLayoutParams(mLayoutParams);
        mTextView.setText(R.string.loading);

        switch (mLoadType){
            case LOAD_TYPE_CUBES:
                mLoadView.setLayoutParams(new LayoutParams(RecyclerUtils.dip2px(mContext,48)
                        ,RecyclerUtils.dip2px(mContext,8)));
                addView(mLoadView);
                addView(mTextView);
                break;
            default:
            case LOAD_TYPE_CUSTOM:
                setPadding(0, mPadding, 0, mPadding);
                mLoadView.setLayoutParams(new LayoutParams(RecyclerUtils.dip2px(mContext,36)
                        , RecyclerUtils.dip2px(mContext,36)));
                addView(mLoadView);
                addView(mTextView);
                break;
            case LOAD_TYPE_SWAP:
                mLoadView.setLayoutParams(new LayoutParams(RecyclerUtils.dip2px(mContext,100)
                        ,RecyclerUtils.dip2px(mContext,100)));
                addView(mLoadView);
                //addView(mTextView);
                break;
            case LOAD_TYPE_EAT_BEANS:
                mLoadView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, RecyclerUtils.dip2px(mContext,48)));
                addView(mLoadView);
                //addView(mTextView);
                break;
            case NO_MORE:
                mTextView.setText(R.string.no_more);
                addView(mTextView);
                break;
        }
    }

    /**
     * 设置加载的view视图
     * @param loadView
     */
    public void setLoadView(LoadType loadView) {
        switch (loadView) {
            case CUBES:
                mLoadType = LOAD_TYPE_CUBES;
                mLoadView = new CubesLoadView(mContext);
                break;
            default:
            case CUSTOM:
                mLoadType = LOAD_TYPE_CUSTOM;
                mLoadView = new ProgressBar(mContext);
                break;
            case SWAP:
                mLoadType = LOAD_TYPE_SWAP;
                mLoadView = new SwapLoadView(mContext);
                break;
            case EAT_BEANS:
                mLoadType = LOAD_TYPE_EAT_BEANS;
                mLoadView = new EatBeansLoadView(mContext);
                break;
        }
        // 添加视图
        addView();
        // 开启动画
        startAnimator();
    }

    /**
     * 设置没有更多
     */
    public void setNoMoreView() {
        mLoadType = NO_MORE;
        addView();
    }


    /**
     * 开启动画
     */
    public void startAnimator() {
        switch (mLoadType) {
            case LOAD_TYPE_CUBES:
                if (mLoadView instanceof CubesLoadView) {
                    ((CubesLoadView) mLoadView).startAnimator();
                }
                break;
            default:
            case LOAD_TYPE_CUSTOM:
                break;
            case LOAD_TYPE_SWAP:
                if (mLoadView instanceof SwapLoadView) {
                    ((SwapLoadView) mLoadView).startAnimator();
                }
                break;
            case LOAD_TYPE_EAT_BEANS:
                if (mLoadView instanceof EatBeansLoadView) {
                    ((EatBeansLoadView) mLoadView).startAnimator();
                }
                break;
        }
    }


}
