package adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;


import java.util.List;

import entity.MultiItemEntity;

/**
 * 多种布局的recyclerView适配器
 * Created by SunySan on 2016/12/25.
 */
public abstract class BaseMultiItemAdapter<T extends MultiItemEntity> extends BaseRecyclerAdapter {

    private SparseArray<Integer> layouts;

    public BaseMultiItemAdapter(Context context, List data) {
        super(context, data);
        //添加不同item布局
        addItemLayout();
        //开启多类型布局模式
        openMultiItemType(true);
    }

    /**
     * @param type
     * @param layoutResId
     */
    protected void addItemType(int type, int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, layoutResId);
    }

    /**
     * @param viewType
     */
    private int getItemLayoutId(int viewType) {
        return layouts == null ? 0 : layouts.get(viewType);
    }

    @Override
    protected int getMultiItemViewType(int position) {
        return ((MultiItemEntity) data.get(position)).itemType;
    }

    @Override
    protected BaseViewHolder onBaseViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getItemLayoutId(viewType));
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        convert(helper, (T) item);
    }

    protected abstract void convert(BaseViewHolder helper, T item);

    protected abstract void addItemLayout();

}
