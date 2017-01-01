package com.sunysan.wherego.test.recycler_text;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sunysan.wherego.R;
import com.sunysan.wherego.test.recycler_text.bean.MultiBean;

import java.util.ArrayList;
import java.util.List;

import adapter.BaseMultiItemAdapter;
import adapter.BaseRecyclerAdapter;
import adapter.BaseViewHolder;
import tool.ToastTools;

/**
 * 多种布局测试
 * Created by SunySan on 2016/12/25.
 */
public class MultiTypeAct extends AppCompatActivity{

    private Context mContext;

    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_layout);

        mContext = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter = new BaseMultiItemAdapter<MultiBean>(this, getMultiItemDatas()) {
            @Override
            protected void convert(BaseViewHolder helper, MultiBean item) {
                switch (helper.getItemViewType()) {
                    case MultiBean.SEND:
                        helper.setText(R.id.multi1, item.content);
                        //helper.setImageBitmap(R.id.chat_from_icon,getRoundCornerBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.chat_head), 16));
                        break;
                    case MultiBean.FROM:
                        helper.setText(R.id.multi2, item.content);
                        //helper.setImageBitmap(R.id.chat_send_icon,getRoundCornerBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.from_head), 16));
                        break;
                }
            }

            @Override
            protected void addItemLayout() {
                addItemType(MultiBean.SEND, R.layout.multi_layout1);
                addItemType(MultiBean.FROM, R.layout.multi_layout2);
            }
        });

        addHeaderView();
        addFooterView();
    }


    private void addHeaderView() {
        View headView = getLayoutInflater().inflate(R.layout.header_layout, null);
        headView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mAdapter.addHeaderView(headView);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastTools.s(mContext, "我是头部");
            }
        });
    }

    private void addFooterView() {
        View footerView = getLayoutInflater().inflate(R.layout.footer_layout, null);
        footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mAdapter.addFooterView(footerView);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastTools.s(mContext, "我是底部");
            }
        });
    }

    public static List<MultiBean> getMultiItemDatas() {
        List<MultiBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            MultiBean multiItem = new MultiBean();
            if (i % 2 == 0) {
                multiItem.itemType = MultiBean.SEND;
                multiItem.content = "圣诞了";
            } else {
                multiItem.itemType = MultiBean.FROM;
                multiItem.content = "中秋了";
            }
            list.add(multiItem);
        }
        return list;
    }
}
