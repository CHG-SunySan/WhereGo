package animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * 由底部向上的动画效果
 * Created by SunySan on 2016/12/21.
 */
public class SlideBottomToAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", view.getRootView().getHeight(), 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1f);
        return new Animator[]{
                translationY, alpha
        };
    }
}
