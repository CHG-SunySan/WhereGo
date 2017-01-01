package animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * 由小变大的动画效果
 * Created by SunySan on 2016/12/21.
 */
public class ScaleToAnimation implements BaseAnimation {

    private static final float DEFAULT_SCALE_FROM = 0.5f;
    private final float mFrom;

    public ScaleToAnimation(float from) {
        mFrom = from;
    }
    public ScaleToAnimation() {
        this(DEFAULT_SCALE_FROM);
    }

    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f);
        return new Animator[]{scaleX, scaleY};
    }
}
