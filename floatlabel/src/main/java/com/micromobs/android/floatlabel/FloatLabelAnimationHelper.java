package com.micromobs.android.floatlabel;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.widget.TextView;

public class FloatLabelAnimationHelper {

    public static ObjectAnimator getFloatHintShowHideAnimation(TextView floatHint, boolean showHint) {

        final float y = showHint ? 0 : (float) (0.2 * floatHint.getHeight());
        final float alpha = showHint ? 1f : 0f;

        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", y);
        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha", alpha);

        return ObjectAnimator.ofPropertyValuesHolder(floatHint, pvhY, pvhAlpha).setDuration(350);
    }

    public static ObjectAnimator getFloatHintColorChangeAnimation(final TextView floatHint,
                                                                  final FloatLabelViewHelper fvh,
                                                                  final boolean hasFocus) {
        final int fromColor = hasFocus ? fvh.getUnFocusedColor() : fvh.getFocusedColor();
        final int toColor = hasFocus ? fvh.getFocusedColor() : fvh.getUnFocusedColor();

        ObjectAnimator colorAnimation = ObjectAnimator.ofInt(floatHint, "textColor", fromColor, toColor);
        colorAnimation.setEvaluator(new ArgbEvaluator());
        return colorAnimation;
    }
}