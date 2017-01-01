package tool;

import android.content.Context;

/**
 * recyclerview工具类
 * Created by SunySan on 2016/12/21.
 */
public class RecyclerUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context mContext,float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
