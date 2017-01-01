package animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * 由左到右的动画效果
 * Created by 辉少s on 2016/12/21.
 */
public class SlideLeftToAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1f);
        return new Animator[]{
                translationX
        };
    }
}
