package helper;

import android.support.v7.widget.RecyclerView;

/**
 * 开始移动监听器
 * Created by SunySan on 2016/12/16.
 */
public interface OnStartDragListener {

    /**
     * 开始移动时监听移动状态
     * @param viewHolder
     */
    void onStratDrag(RecyclerView.ViewHolder viewHolder);
}
