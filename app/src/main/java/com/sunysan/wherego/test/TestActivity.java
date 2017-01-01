package com.sunysan.wherego.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sunysan.wherego.R;
import com.sunysan.wherego.test.fragment.BaseFragment;
import com.sunysan.wherego.test.recycler_text.RecyclerViewTestAct;

import tool.ToastTools;

/**
 * 测试的Activity
 * Created by SunySan on 2016/12/16.
 */
public class TestActivity extends AppCompatActivity implements BaseFragment.OnListItemClickListener{
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mContext = this;

        if (savedInstanceState == null){
            BaseFragment fragment = new BaseFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.test_main_fragment
            ,fragment).commit();
        }
    }


    @Override
    public void onListItemClick(int position) {
        BaseFragment fragment = null;
        switch (position){
            case 0:
                Intent o = new Intent(this,RecyclerViewTestAct.class);
                startActivity(o);
                ToastTools.s(mContext,"第"+position+"个item");
                break;
            case 1:
                ToastTools.s(mContext,"第"+position+"个item");
        }

//        getSupportFragmentManager().beginTransaction().replace(R.id.test_main_fragment
//                , fragment).addToBackStack(null).commit();
    }
}
