package com.micromobs.android.floatlabel;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.TextView;

public class FloatLabelAnimationHelper {


    public static View.OnFocusChangeListener getFocusChangeListener(final FloatLabelViewHelper fvh,
                                                                    final TextView floatHint) {
        return new View.OnFocusChangeListener() {


            ValueAnimator _focusToUnfocus, _unfocusToFocus;

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ValueAnimator lColorAnimation;

                if (hasFocus) {
                    lColorAnimation = getFocusToUnfocusAnimation();
                } else {
                    lColorAnimation = getUnfocusToFocusAnimation();
                }

                lColorAnimation.setDuration(700);
                lColorAnimation.start();
            }

            private ValueAnimator getFocusToUnfocusAnimation() {
                if (_focusToUnfocus == null) {
                    _focusToUnfocus = _getFocusAnimation(floatHint,
                                                         fvh.getUnFocusedColor(),
                                                         fvh.getFocusedColor());
                }
                return _focusToUnfocus;
            }

            private ValueAnimator getUnfocusToFocusAnimation() {
                if (_unfocusToFocus == null) {
                    _unfocusToFocus = _getFocusAnimation(floatHint,
                                                         fvh.getUnFocusedColor(),
                                                         fvh.getFocusedColor());
                }
                return _unfocusToFocus;
            }
        };
    }


    private static ValueAnimator _getFocusAnimation(final TextView floatHint,
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
