package com.sunysan.wherego.test.recycler_text;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.sunysan.wherego.R;
import com.sunysan.wherego.test.recycler_text.bean.TestUserBean;

import java.util.ArrayList;
import java.util.List;

import adapter.BaseRecyclerAdapter;
import adapter.BaseViewHolder;
import tool.ToastTools;

/**
 * 添加头部和底部
 * Created by SunySan on 2016/12/25.
 */
public class AddHearAndFootAct extends AppCompatActivity {
    private Context mContext;

    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<TestUserBean> mAdapter;

    private TestUserBean mBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_add_head_foot);

        mContext = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<TestUserBean>(this, getmList(), R.layout.bean_test) {
            @Override
            protected void convert(BaseViewHolder helper, final TestUserBean item) {
                mBean = item;

                helper.setText(R.id.name, item.getName());
                helper.setText(R.id.phone,item.getPhone());

                helper.setOnClickListener(R.id.name, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastTools.s(AddHearAndFootAct.this, item.getName());
                    }
                });
            }


//            @Override
//            protected void convert(BaseViewHolder helper, final String item) {
//                helper.setText(R.id.tv_item_text, item);
//                helper.setOnClickListener(R.id.tv_item_text, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ToastTools.s(AddHearAndFootAct.this, item);
//                    }
//                });
//            }
        });
        mAdapter.openLoadAnimation(false);//不要默认动画效果的话设置这个
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
                ToastTools.s(AddHearAndFootAct.this, "我是头部");
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
                ToastTools.s(AddHearAndFootAct.this, "我是底部");
            }
        });
    }


    private List<String> getItemDatas() {
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mList.add("测试添加头部底部");
        }
        return mList;
    }

    private String[] names = new String[]{"小米","小马","小狗","小鸭","小人","小屏"
            ,"小胖","小猪","小猫","小小","小道"};
    private String[] phones = new String[]{"1111","2222","3333","4444","5555","6666"
            ,"7777","8888","9999","0000","1212"};

    private List<TestUserBean> getmList() {
        List<TestUserBean> mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestUserBean mBean = new TestUserBean();
            mBean.setName(names[i]);
            mBean.setPhone(phones[i]);
            mList.add(mBean);
        }
        return mList;
    }



}
