package animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * 淡入淡出的动画效果
 * Created by SunySan on 2016/12/21.
 */
public class AlphaToAnimation implements BaseAnimation{
    private static final float DEFAULT_ALPHA_FROM = 0f;
    private final float mFrom;

    public AlphaToAnimation() {
        this(DEFAULT_ALPHA_FROM);
    }

    public AlphaToAnimation(float from) {
        mFrom = from;
    }

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", mFrom, 1.0f)};
    }
}
