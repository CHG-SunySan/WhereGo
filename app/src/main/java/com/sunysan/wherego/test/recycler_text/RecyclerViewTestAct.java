package com.sunysan.wherego.test.recycler_text;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sunysan.wherego.R;

import java.util.ArrayList;
import java.util.List;

import adapter.BaseRecyclerAdapter;
import adapter.BaseViewHolder;
import entity.MultiItemEntity;
import listener.OnRecyclerItemClickListener;
import tool.ToastTools;

/**
 * recyclerView测试类
 * Created by SunySan on 2016/12/21.
 */
public class RecyclerViewTestAct extends Activity {

    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_test);
        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<String>(this
                , getItemDatas(), R.layout.rv_item) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item_text, item);
            }
        });

        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(mContext, AddHearAndFootAct.class);
                        break;
                    case 1:
                        intent.setClass(mContext,GridRecyclerViewAct.class);
                        break;
                    case 2:
                        intent.setClass(mContext,EmptyActivity.class);
                        break;

                    case 3:
                        intent.setClass(mContext, MultiTypeAct.class);
                        break;
                    default:
                        ToastTools.s(RecyclerViewTestAct.this, "你的位置是" + position);
                        break;

                }
                startActivity(intent);
            }
        });

    }

    public static List<String> getItemDatas() {
        List<String> mList = new ArrayList<>();
        mList.add("测试添加头部底部");
        mList.add("测试网格布局+头部");
        mList.add("没有数据的时候");
        mList.add("测试多种布局");
        mList.add("PullToRefresh Adapter");
        mList.add("Empty Adapter");
        mList.add("Drag And Swipe Adapter");
        mList.add("Multi Item Adapter");
        mList.add("Navigation Adapter");
        mList.add("Custom Refresh Adapter");
        mList.add("简单测试");
        mList.add("HeaderAndFooter Adapter");
        mList.add("Animation Adapter");
        mList.add("ItemAnimation Adapter");
        mList.add("PullToRefresh Adapter");
        mList.add("Empty Adapter");
        mList.add("Drag And Swipe Adapter");
        mList.add("Multi Item Adapter");
        mList.add("Navigation Adapter");
        mList.add("Custom Refresh Adapter");
        mList.add("简单测试");
        mList.add("HeaderAndFooter Adapter");
        mList.add("Animation Adapter");
        mList.add("ItemAnimation Adapter");
        mList.add("PullToRefresh Adapter");
        mList.add("Empty Adapter");
        mList.add("Drag And Swipe Adapter");
        mList.add("Multi Item Adapter");
        mList.add("Navigation Adapter");
        mList.add("Custom Refresh Adapter");
        mList.add("简单测试");
        mList.add("HeaderAndFooter Adapter");
        mList.add("Animation Adapter");
        mList.add("ItemAnimation Adapter");
        mList.add("PullToRefresh Adapter");
        mList.add("Empty Adapter");
        mList.add("Drag And Swipe Adapter");
        mList.add("Multi Item Adapter");
        mList.add("Navigation Adapter");
        mList.add("Custom Refresh Adapter");
        mList.add("简单测试");
        mList.add("HeaderAndFooter Adapter");
        mList.add("Animation Adapter");
        mList.add("ItemAnimation Adapter");
        mList.add("PullToRefresh Adapter");
        mList.add("Empty Adapter");
        mList.add("Drag And Swipe Adapter");
        mList.add("Multi Item Adapter");
        mList.add("Navigation Adapter");
        mList.add("Custom Refresh Adapter");
        return mList;
    }
}
