package helper;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Interface to notify an item ViewHolder of relevant callbacks from
 * {@link ItemTouchHelper.Callback}
 * 接口，当适配器刷新时回调
 *
 * Created by SunySan on 2016/12/16.
 */
public interface HelperViewHolderInterface {

    /**
     * 当item被移动时，调用
     */
    void onItemSelected();

    /**
     * 当item移动完毕，调用
     */
    void onItemClear();

}
