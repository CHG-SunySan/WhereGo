package com.sunysan.wherego.test.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sunysan.wherego.R;

/**
 * 片段基类
 * Created by SunySan on 2016/12/16.
 */
public class BaseFragment extends ListFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mItemClickListener = (OnListItemClickListener) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] items = getResources().getStringArray(R.array.base_fragment_item);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mItemClickListener.onListItemClick(position);
    }

    private OnListItemClickListener mItemClickListener;

    public interface OnListItemClickListener {
        void onListItemClick(int position);
    }
}
