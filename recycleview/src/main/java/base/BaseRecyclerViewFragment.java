package base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import helper.OnStartDragListener;

/**
 * recycleview基类
 * Created by SunySan on 2016/12/16.
 */
public class BaseRecyclerViewFragment extends Fragment implements OnStartDragListener{
    private ItemTouchHelper itemTouchHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new RecyclerView(container.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        .....
//        这里需要初始化适配器
    }

    @Override
    public void onStratDrag(RecyclerView.ViewHolder viewHolder) {

    }
}
