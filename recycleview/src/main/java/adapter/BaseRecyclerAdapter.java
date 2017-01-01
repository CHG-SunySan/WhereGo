package adapter;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import animation.AlphaToAnimation;
import animation.AnimationType;
import animation.BaseAnimation;
import animation.CustomAnimation;
import animation.ScaleToAnimation;
import animation.SlideBottomToAnimation;
import animation.SlideLeftToAnimation;
import animation.SlideRightToAnimation;
import animation.SlideTopToAnimation;
import helper.OnDragAndSwipeLintener;
import listener.OnRecyclerItemClickListener;
import listener.OnRecyclerItemLongClickListener;
import listener.RequestLoadMoreListener;
import animation.LoadType;
import view.FooterView;

/**
 * recycleview适配器基类
 * Created by SunySan on 2016/12/16.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnDragAndSwipeLintener {
    protected Context mContext;

    protected List<T> data;
    protected LayoutInflater layoutInflater;
    protected int mLayoutResId;

    private static final int VIEW_TYPE_HEADER = 0x00001111;//header
    private static final int VIEW_TYPE_CONTENT = 0x00002222;//content
    private static final int VIEW_TYPE_FOOTER = 0x00003333;//footer
    private static final int VIEW_TYPE_EMPTY = 0x00004444;//empty
    private static final int VIEW_TYPE_LOADING = 0x00005555;//loading

    private int mLastPosition = -1;
    private int mViewType = -1;
    private int pageSize = -1;
    private int mItemHeight;

    private boolean mLoadingMoreEnable = false;
    private boolean mNextLoadingEnable = false;
    private boolean mHeadAndEmptyEnable; // headerView and emptyView
    private boolean mFootAndEmptyEnable;// footerView and emptyView
    private boolean mEmptyEnable;
    private boolean mMultiItemTypeEnable;

    private static final int DEFAULT_DURATION = 300;
    private static final int DEFAULT_DRAG_VIEW = 0;
    private static final float ALPHA_255 = 1.0f;

    private ItemTouchHelper mItemTouchHelper;
    private int mDragViewId = DEFAULT_DRAG_VIEW;
    private int mSelectedColor = Color.LTGRAY;
    private Drawable mBackgroundDrawable;

    private RequestLoadMoreListener mRequestLoadMoreListener;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    private OnRecyclerItemLongClickListener onRecyclerItemLongClickListener;

    private LoadType mLoadType;
    private BaseAnimation[] mBaseAnimation;
    private Interpolator mInterpolator = new LinearInterpolator();
    private boolean mOpenAnimationEnable = true;
    private boolean mFirstOnlyAnimationEnable = false;//动画第一次展现
    private int mDuration = DEFAULT_DURATION;//动画的时间

    private LinearLayout mHeaderLayout;
    private View mFooterView;
    private View mEmptyView;
    private View mLoadView;
    private View mContentView;


    /**
     * recycleview基类构造器
     *
     * @param mContext    上下文
     * @param data        数据源
     * @param layoutResId 布局文件
     */
    public BaseRecyclerAdapter(Context mContext, List<T> data, int layoutResId) {
        this.mContext = mContext;
        this.data = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
        this.layoutInflater = LayoutInflater.from(mContext);
        //默认动画
        this.mBaseAnimation = new BaseAnimation[]{new CustomAnimation()};
        this.mLoadType = LoadType.CUSTOM;
    }

    /**
     * recycleview基类构造器
     *
     * @param mContext 上下文
     * @param data     数据源
     */
    public BaseRecyclerAdapter(Context mContext, List<T> data) {
        this(mContext, data, 0);
    }


    /**
     * 根据position位置获取item
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return data.get(position);
    }

    /**
     * 获取item的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int loadMoreCount = isLoadMore() ? 1 : 0;
        int count = data.size() + getHeadCount() + getFooterCount() + loadMoreCount;
        if (data.size() == 0 && mEmptyView != null) {
            if (count == 0 && (!mHeadAndEmptyEnable || !mFootAndEmptyEnable)) {
                count += getEmptyViewCount();
            } else if (mHeadAndEmptyEnable || mFootAndEmptyEnable) {
                count += getEmptyViewCount();
            }
            if ((mHeadAndEmptyEnable && getHeadCount() == 1 && count == 1) || count == 0) {
                mEmptyEnable = true;
                count += getEmptyViewCount();
            }
        }
        return count;
    }

    /**
     * 根据不同布局设置不同类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderLayout != null && position == 0)
            return VIEW_TYPE_HEADER;
        //position <= 2的情况四：
        //{@link #setEmptyView(header + empty + footer,empty,empty +footer,header + empty)}
        if (data.size() == 0 && mEmptyView != null && mEmptyEnable && position <= 2) {
            // position == 1的情况三:
            // {@link #setEmptyView(header + empty + footer,header + empty,empty+ footer)}
            if ((mHeadAndEmptyEnable || mFootAndEmptyEnable) && position == 1) {
                if (mHeaderLayout == null && mFooterView != null) {
                    //empty + footer
                    return VIEW_TYPE_FOOTER;
                } else if (mHeaderLayout != null && mEmptyView != null) {
                    //header + empty + footer ,header + empty
                    return VIEW_TYPE_EMPTY;
                }
            } else if (position == 0) {//position == 0 的情况二：
                if (mHeaderLayout == null) {
                    return VIEW_TYPE_EMPTY;
                } else {
                    return VIEW_TYPE_HEADER;
                }
            } else if (position == 2 && mHeaderLayout != null && mFooterView != null) {
                return VIEW_TYPE_FOOTER;
            } else if (position == 1) {
                if (mHeaderLayout != null) {
                    return VIEW_TYPE_EMPTY;
                }
                return VIEW_TYPE_FOOTER;
            }
        } else if (position == data.size() + getHeadCount()) {
            if (mNextLoadingEnable) {
                return VIEW_TYPE_LOADING;
            }
            return VIEW_TYPE_FOOTER;
        } else if (position - getHeadCount() >= 0) {
            if (mMultiItemTypeEnable) return getMultiItemViewType(position - getHeadCount());
            return VIEW_TYPE_CONTENT;
        }

        return super.getItemViewType(position - getHeadCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                baseViewHolder = new BaseViewHolder(mHeaderLayout);
                break;
            default:
            case VIEW_TYPE_CONTENT:
                baseViewHolder = onBaseViewHolder(parent, viewType);
                initItemClickListener(baseViewHolder);
                // get default background drawable
                mBackgroundDrawable = baseViewHolder.getmView().getBackground();
                break;
            case VIEW_TYPE_FOOTER:
                baseViewHolder = new BaseViewHolder(mFooterView);
                break;
            case VIEW_TYPE_EMPTY:
                baseViewHolder = new BaseViewHolder(mEmptyView);
                break;
            case VIEW_TYPE_LOADING:
                baseViewHolder = addLoadingView(mLoadType);
                break;
        }
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        mViewType = holder.getItemViewType();
        switch (mViewType) {
            case VIEW_TYPE_HEADER:
                break;
            case VIEW_TYPE_EMPTY:
                break;
            case VIEW_TYPE_FOOTER:
                break;
            default:
            case VIEW_TYPE_CONTENT:
                convert((BaseViewHolder) holder, data.get(holder.getLayoutPosition() - getHeadCount()));
                // addAnimation(holder, holder.getLayoutPosition());
                break;
            case VIEW_TYPE_LOADING:
                addLoadMore();
                break;
        }

        if (mItemTouchHelper != null && mViewType == VIEW_TYPE_CONTENT) {
            if (mDragViewId != DEFAULT_DRAG_VIEW) {
                View dragView = ((BaseViewHolder) holder).getView(mDragViewId);
                if (dragView != null) {
                    dragView.setTag(holder);
                    dragView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                                if (mItemTouchHelper != null) {
                                    mItemTouchHelper.startDrag((RecyclerView.ViewHolder) view.getTag());
                                }
                                return true;
                            }
                            return false;
                        }
                    });
                }
            }
        }

    }

    /**
     * @param parent
     */
    protected BaseViewHolder onBaseViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mLayoutResId);
    }

    /**
     * @param parent
     * @param layoutResId
     * @return
     */
    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        if (mContentView == null) {
            return new BaseViewHolder(getItemView(layoutResId, parent));
        }
        return new BaseViewHolder(mContentView);
    }

    /**
     * @param layoutResId
     * @param parent
     * @return
     */
    protected View getItemView(int layoutResId, ViewGroup parent) {
        View view = layoutInflater.inflate(layoutResId, parent, false);
        if (mItemHeight != 0) {
            view.getLayoutParams().height = mItemHeight;
        }
        return view;
    }

    /**
     * @param baseViewHolder
     */
    private void initItemClickListener(final BaseViewHolder baseViewHolder) {
        if (onRecyclerItemClickListener != null) {
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerItemClickListener.onItemClick(v
                            , baseViewHolder.getLayoutPosition() - getHeadCount());
                }
            });
        }
        if (onRecyclerItemLongClickListener != null) {
            baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onRecyclerItemLongClickListener.onItemLongClick(v
                            , baseViewHolder.getLayoutPosition() - getHeadCount());
                }
            });
        }
    }

    /**
     * @param helper
     * @param item
     */
    protected abstract void convert(BaseViewHolder helper, T item);


    /**
     * 添加动画
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder, int position) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyAnimationEnable || holder.getLayoutPosition() > mLastPosition) {
                if (mBaseAnimation.length == 1) {//一种动画
                    for (Animator anim : mBaseAnimation[0].getAnimators(holder.itemView)) {
                        startAnim(anim);
                    }
                } else {//多种动画
                    if (position % 2 == 0) {
                        for (Animator anim : mBaseAnimation[0].getAnimators(holder.itemView)) {
                            startAnim(anim);
                        }
                    } else {
                        for (Animator anim : mBaseAnimation[1].getAnimators(holder.itemView)) {
                            startAnim(anim);
                        }
                    }
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * 加载的时候设置动画
     * 【知识普及：什么是Interpolator?
     * Interpolator定义了动画变化的速率，在Animations框架当中定义了一下几种Interpolator
     * 1、AccelerateDecelerateInterpolator:在动画开始与结束的地方速率改变比较慢，在中间的时候加速
     * 2、AccelerateInterpolator: 在动画开始的地方速率改变比较慢，然后开始加速
     * 3、CycleInterpolator： 动画循环播放特定的次数，速率改变沿着正弦曲线
     * 4、DecelerateInterpolator: 在动画开始的地方速率改变的比较慢，然后开始减速
     * 5、LinerInterpolator：动画以均匀的速率改变
     * 】
     *
     * @param anim
     */
    protected void startAnim(Animator anim) {
        anim.setInterpolator(mInterpolator);//设置动画均匀的速率改变
        anim.setDuration(mDuration).start();//设置动画的时间
    }

    /**
     * itemView设置animation
     *
     * @param animationType One of {ALPHA}, {SCALE}, {SLIDE_LEFT}, {SLIDE_RIGHT}, {SLIDE_BOTTOM},{SLIDE_TOP}.
     */
    public void openLoadAnimation(AnimationType animationType) {
        this.mOpenAnimationEnable = true;
        mBaseAnimation = null;
        switch (animationType) {
            case CUSTOM:
                mBaseAnimation = new BaseAnimation[]{new CustomAnimation()};
                break;
            case ALPHA:
                mBaseAnimation = new BaseAnimation[]{new AlphaToAnimation()};
                break;
            case SCALE:
                mBaseAnimation = new BaseAnimation[]{new ScaleToAnimation()};
                break;
            case SLIDE_LEFT:
                mBaseAnimation = new BaseAnimation[]{new SlideLeftToAnimation()};
                break;
            case SLIDE_RIGHT:
                mBaseAnimation = new BaseAnimation[]{new SlideRightToAnimation()};
                break;
            case SLIDE_BOTTOM:
                mBaseAnimation = new BaseAnimation[]{new SlideBottomToAnimation()};
                break;
            case SLIDE_TOP:
                mBaseAnimation = new BaseAnimation[]{new SlideTopToAnimation()};
                break;
            case SLIDE_LEFT_RIGHT:
                mBaseAnimation = new BaseAnimation[]{new SlideLeftToAnimation(), new SlideRightToAnimation()};
                break;
            case SLIDE_BOTTOM_TOP:
                mBaseAnimation = new BaseAnimation[]{new SlideBottomToAnimation(), new SlideTopToAnimation()};
                break;
            default:
                break;
        }
    }


    /**
     * 开启加载时的动画，自定义动画
     *
     * @param animation
     */
    public void openLoadAnimation(BaseAnimation[] animation) {
        this.mBaseAnimation = animation;
        this.mOpenAnimationEnable = true;
    }

    /**
     * 开启加载时的动画，默认动画效果——CUSTOM；
     */
    public void openLoadAnimation() {
        openLoadAnimation(true);
    }

    /**
     * @param openAnimationEnable
     */
    public void openLoadAnimation(boolean openAnimationEnable) {
        this.mOpenAnimationEnable = openAnimationEnable;
    }

    /**
     * 开启加载更多
     *
     * @param enable
     */
    public void openLoadingMore(boolean enable) {
        this.mNextLoadingEnable = enable;
    }

    /**
     * 加载更多
     *
     * @param pageSize 页数
     * @param enable   是否开启
     */
    public void openLoadingMore(int pageSize, boolean enable) {
        this.pageSize = pageSize;
        this.mNextLoadingEnable = enable;
    }

    /**
     * 开启多类型布局模式
     *
     * @param multiItemTypeEnable
     */
    public void openMultiItemType(boolean multiItemTypeEnable) {
        this.mMultiItemTypeEnable = multiItemTypeEnable;
    }

    /**
     * 获取多种视图类型
     *
     * @param position
     * @return
     */
    protected int getMultiItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 添加加载中的布局
     *
     * @param loadingView
     */
    public void addLoadingView(View loadingView) {
        this.mLoadView = loadingView;
    }

    /**
     * @param loadType
     * @return
     */
    public BaseViewHolder addLoadingView(LoadType loadType) {
        mLoadView = new FooterView(mContext);
        if (mLoadView instanceof FooterView) {
            ((FooterView) mLoadView).setLoadView(loadType);
        }
        return new BaseViewHolder(mLoadView);
    }

    /**
     * 添加加载更多的接口
     */
    public void addLoadMore() {
        if (isLoadMore() && !mLoadingMoreEnable) {
            mLoadingMoreEnable = true;
            mRequestLoadMoreListener.onLoadMore();
        }
    }

    /**
     * 是否加载更多
     *
     * @return
     */
    private boolean isLoadMore() {
        return mNextLoadingEnable && mRequestLoadMoreListener != null && data.size() > 0;
    }

    /**
     * 是否第一次启动动画
     *
     * @param firstOnlyAnimation
     */
    public void isFirstOnlyAnimation(boolean firstOnlyAnimation) {
        this.mFirstOnlyAnimationEnable = firstOnlyAnimation;
    }

    /**
     * 移除置顶位置的item
     *
     * @param position
     */
    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position + getHeadCount());
    }

    /**
     * 指定位置添加item
     *
     * @param position
     * @param item
     */
    public void add(int position, T item) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 添加数据源
     *
     * @param datas
     */
    public void addAll(List<T> datas) {
        if (datas != null) {
            data.addAll(datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 直接在最后的position添加item
     *
     * @param item
     */
    public void add(T item) {
        add(data.size(), item);
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.data = data;
        if (mRequestLoadMoreListener != null) {
            mNextLoadingEnable = true;
            mFooterView = null;
        }
        mLastPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * 设置item的高
     *
     * @param itemHeight
     */
    public void setItemHeight(int itemHeight) {
        this.mItemHeight = itemHeight;
    }

    /**
     * 在原有数据源的基础上添加数据（累加数据）
     *
     * @param data
     */
    public void addData(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 获取当前数据源
     *
     * @return
     */
    public List getData() {
        return data;
    }

    /**
     * 设置动画的时间
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    /**
     * 是否有头部
     *
     * @return 有返回1，没有返回0
     */
    public int getHeadCount() {
        return mHeaderLayout == null ? 0 : 1;
    }

    /**
     * 是否有底部
     *
     * @return 有返回1，没有返回0
     */
    public int getFooterCount() {
        return mFooterView == null ? 0 : 1;
    }

    public int getEmptyViewCount() {
        return mEmptyView == null ? 0 : 1;
    }


    /////////////////////////////http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1120/3705.html//////////////////////////////////////////////////////////
    //以下重写的两个方法，目的在于处理当recyclerview是网格布局的时候添加头部的问题

    /**
     * 当Item进入这个页面的时候调用
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == VIEW_TYPE_EMPTY || type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER
                || type == VIEW_TYPE_LOADING) {
            setFullSpan(holder);
        } else {
            addAnimation(holder, holder.getLayoutPosition());
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            //SpanSizeLookup是一个抽象类，仅有一个抽象方法，getSpanSize,这个方法的返回值决定
            //每个position上的item占据的单元格个数
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    return (type == VIEW_TYPE_EMPTY || type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER
                            || type == VIEW_TYPE_LOADING) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }


    /////////////////////////////http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1120/3705.html//////////////////////////////////////////////////////////

    /**
     * @param holder
     */
    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);//设置占领全部空间
        }
    }

    /**
     * 添加头部
     *
     * @param header
     */
    public void addHeaderView(View header) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(mContext);
            mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
            mHeaderLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (header != null) {
            try {
                this.mHeaderLayout.addView(header);
            } catch (RuntimeException e) {
                this.mHeaderLayout.removeAllViews();
                this.mHeaderLayout.addView(header);
            }
            this.notifyDataSetChanged();
        }
    }


    /**
     * 添加底部
     *
     * @param footer
     */
    public void addFooterView(View footer) {
        mNextLoadingEnable = false;
        this.mFooterView = footer;
        this.notifyDataSetChanged();
    }

    /**
     * 添加没有更多的视图
     */
    public void addNoMoreView() {
        mNextLoadingEnable = false;
        mFooterView = new FooterView(mContext);
        ((FooterView) mFooterView).setNoMoreView();
        this.notifyDataSetChanged();
    }

    /**
     * 设置没有数据或是没有网络之后的视图
     *
     * @param emptyView
     */
    public void addEmptyView(View emptyView) {
        addEmptyView(false, false, emptyView);
    }

    /**
     * @param isHeadAndEmpty
     * @param isFootAndEmpty
     * @param emptyView
     */
    public void addEmptyView(boolean isHeadAndEmpty, boolean isFootAndEmpty, View emptyView) {
        mHeadAndEmptyEnable = isHeadAndEmpty;
        mFootAndEmptyEnable = isFootAndEmpty;
        mEmptyView = emptyView;
        mEmptyEnable = true;
    }

    /**
     * @param isHeadAndEmpty
     * @param emptyView
     */
    public void addEmpty(boolean isHeadAndEmpty, View emptyView) {
        addEmptyView(isHeadAndEmpty, false, emptyView);
    }

    /**
     * 设置加载更多的类型
     *
     * @param loadType
     */
    public void setLoadMoreType(LoadType loadType) {
        this.mLoadType = loadType;
    }

    /**
     * @param itemTouchHelper
     */
    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.mItemTouchHelper = itemTouchHelper;
    }

    /**
     * @param viewId
     */
    public void setDragViewId(int viewId) {
        this.mDragViewId = viewId;
    }

    /**
     * @param selectedColor
     */
    public void setSelectedColor(int selectedColor) {
        this.mSelectedColor = selectedColor;
    }

    /**
     * @param isNextLoad
     */
    public void notifyDataChangeAfterLoadMore(boolean isNextLoad) {
        mNextLoadingEnable = isNextLoad;
        mLoadingMoreEnable = false;
        notifyDataSetChanged();
    }

    /**
     * @param data
     * @param isNextLoad
     */
    public void notifyDataChangeAfterLoadMore(List<T> data, boolean isNextLoad) {
        data.addAll(data);
        notifyDataChangeAfterLoadMore(isNextLoad);
    }


    /**
     * @return
     */
    public LinearLayout getHeaderLayout() {
        return mHeaderLayout;
    }

    /**
     * @return
     */
    public View getFooterView() {
        return mFooterView;
    }

    /**
     * @return
     */
    public View getEmptyView() {
        return mEmptyView;
    }


    ////////////////////////////////////接口回调方法/////////////////////////////////////////////

    @Override
    public void onItemSwipe(int position) {
        int type = getItemViewType(position);
        if (!(type == VIEW_TYPE_EMPTY || type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER
                || type == VIEW_TYPE_LOADING)) {
            //删除mData中数据
            data.remove(position - getHeadCount());
            //删除RecyclerView列表对应item
            notifyItemRemoved(position);
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean onItemDrag(int fromPosition, int toPosition) {
        //交换mData中数据的位置
        Collections.swap(data, fromPosition - getHeadCount(), toPosition - getHeadCount());
        //交换RecyclerView列表中item的位置
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    @Override
    public void onItemSelected(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundColor(mSelectedColor);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundDrawable(mBackgroundDrawable);
    }

    @Override
    public void onItemSwipeAlpha(RecyclerView.ViewHolder viewHolder, float dX) {
        final float alpha = ALPHA_255 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
        viewHolder.itemView.setAlpha(alpha);
        viewHolder.itemView.setTranslationX(dX);
    }

    //////////////////////接口申明/////////////////////////////////

    /**
     * @param onRecyclerViewItemClickListener
     */
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerViewItemClickListener;
    }

    /**
     * @param onRecyclerViewItemLongClickListener
     */
    public void setOnRecyclerItemLongClickListener(OnRecyclerItemLongClickListener onRecyclerViewItemLongClickListener) {
        this.onRecyclerItemLongClickListener = onRecyclerViewItemLongClickListener;
    }

    /**
     * @param requestLoadMoreListener
     */
    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
    }
}
