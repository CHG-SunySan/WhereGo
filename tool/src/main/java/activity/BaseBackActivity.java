package activity;

import android.content.Context;
import android.os.Bundle;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * 可以侧滑退出的activity
 * Created by SunySan on 2016/12/14.
 */
public class BaseBackActivity extends SwipeBackActivity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }
}
