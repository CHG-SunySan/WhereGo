package animation;

import android.animation.Animator;
import android.view.View;

/**
 * 基类，动画接口
 * Created by SunySan on 2016/12/21.
 */
public interface BaseAnimation {
    Animator[] getAnimators(View view);
}
