package listener;

import android.view.View;

/**
 * item长按点击事件监听
 * Created by SunySan on 2016/12/21.
 */
public interface OnRecyclerItemLongClickListener {
    boolean onItemLongClick(View view, int position);
}
