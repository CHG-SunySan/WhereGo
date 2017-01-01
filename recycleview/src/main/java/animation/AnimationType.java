package animation;

/**
 * 动画的类型
 * Created by SunySan on 2016/12/21.
 */
public enum AnimationType {
    /**
     *     动画的各种效果类型
     *     CUSTOM：默认的动画效果，由大变小
     *     ALPHA： 淡入淡出的动画效果
     *     SCALE：由小变大的动画效果
     *     SLIDE_BOTTOM：由底部向上的动画效果
     *     SLIDE_TOP：由顶部向下的动画效果
     *     SLIDE_LEFT：由左到右的动画效果
     *     SLIDE_RIGHT：由右到左的动画效果
     *     SLIDE_LEFT_RIGHT：一左一右的动画效果
     *     SLIDE_BOTTOM_TOP：一上一下的动画效果
     */
    CUSTOM, ALPHA, SCALE, SLIDE_BOTTOM, SLIDE_TOP, SLIDE_LEFT, SLIDE_RIGHT, SLIDE_LEFT_RIGHT, SLIDE_BOTTOM_TOP
}
