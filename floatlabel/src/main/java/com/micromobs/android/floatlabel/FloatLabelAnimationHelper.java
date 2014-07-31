package com.micromobs.android.floatlabel;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.widget.TextView;

public class FloatLabelAnimationHelper {

    public static ObjectAnimator getFloatHintShowAnimation(TextView floatHint) {
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 0);
        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        return ObjectAnimator.ofPropertyValuesHolder(floatHint, pvhY, pvhAlpha).setDuration(350);
    }

    public static ObjectAnimator getFloatHintHideAnimation(TextView floatHint) {
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y",
                                                                 (float) (0.2 * floatHint.getHeight()));
        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 0f);
        return ObjectAnimator.ofPropertyValuesHolder(floatHint, pvhY, pvhAlpha).setDuration(350);
    }

    public static ObjectAnimator getFloatHintColorChangeAnimation(final TextView floatHint,
                                                                  final FloatLabelViewHelper fvh,
                                                                  final boolean hasFocus) {
        ObjectAnimator colorAnimation;
        if (hasFocus) {
            colorAnimation = ObjectAnimator.ofInt(floatHint,
                                                  "textColor",
                                                  fvh.getUnFocusedColor(),
                                                  fvh.getFocusedColor());

        } else {
            colorAnimation = ObjectAnimator.ofInt(floatHint,
                                                  "textColor",
                                                  fvh.getFocusedColor(),
                                                  fvh.getUnFocusedColor());

        }
        colorAnimation.setEvaluator(new ArgbEvaluator());
        return colorAnimation;
    }
}
