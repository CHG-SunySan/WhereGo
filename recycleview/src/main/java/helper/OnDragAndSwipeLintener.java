package helper;

import android.support.v7.widget.RecyclerView;

/**
 * 拖动监听
 * Created by SunySan on 2016/12/16.
 */
public interface OnDragAndSwipeLintener {
    boolean onItemDrag(int fromPosition, int toPosition);

    void onItemSwipe(int position);

    void onItemSelected(RecyclerView.ViewHolder viewHolder);

    void onItemClear(RecyclerView.ViewHolder viewHolder);

    /**
     * @param viewHolder
     * @param dX         Side slip distance
     */
    void onItemSwipeAlpha(RecyclerView.ViewHolder viewHolder, float dX);

}
