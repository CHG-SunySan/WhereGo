package com.sunysan.wherego.test.recycler_text;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sunysan.wherego.R;

import java.util.ArrayList;
import java.util.List;

import adapter.BaseRecyclerAdapter;
import adapter.BaseViewHolder;
import listener.OnRecyclerItemClickListener;
import tool.ToastTools;

/**
 * 网格布局的recyclerView
 * Created by SunySan on 2016/12/25.
 */
public class GridRecyclerViewAct extends AppCompatActivity {

    private Context mContext;

    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_add_head_foot);

        mContext = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//设置2列
        mRecyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<String>(this, /*getItemDatas()*/new ArrayList<String>(), R.layout.rv_item) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item);
            }
        });

//        mAdapter.openLoadAnimation(false);
        addNoData();
        addHeaderView();


        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastTools.s(mContext, "第" + position + "个item");
            }
        });

    }

    private void addNoData(){
        View emptyView = getLayoutInflater().inflate(R.layout.rv_empty,null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        mAdapter.addEmptyView(emptyView);
    }

    private void addHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.header_layout, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mAdapter.addHeaderView(headerView);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mList.clear();
//
//                mAdapter.notifyDataSetChanged();

                Snackbar.make(view, "点击了头部", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private static List<String> mList = new ArrayList<>();

    public static List<String> getItemDatas() {
        for (int i = 0; i < 20; i++) {
            mList.add("" + i);
        }
        return mList;
    }

}
