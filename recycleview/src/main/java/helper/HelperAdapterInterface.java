package helper;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Interface to listen for a move or dismissal event from a {@link ItemTouchHelper.Callback}
 * 接口，监听移除或是从ItemTouchHelper.Callback回调
 * Created by SunySan on 2016/12/16.
 */
public interface HelperAdapterInterface {

    /**
     * 每一次item移动时调用
     * @param fromPosition 开始移动的位置
     * @param toPosition 移动到的位置
     * @return
     */
    boolean onItemMove(int fromPosition,int toPosition);

    /**
     * 当item被移除时调用
     * @param position
     */
    void onItemDismiss(int position);
}
