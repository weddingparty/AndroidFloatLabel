package com.micromobs.android.floatlabel;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
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


    public static ValueAnimator getFocusAnimation(final TextView floatHint,
                                                  final int fromColor,
                                                  final int toColor) {

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                floatHint.setTextColor((Integer) animator.getAnimatedValue());
            }
        });
        return colorAnimation;
    }
}
