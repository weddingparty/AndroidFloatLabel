package com.micromobs.android.floatlabel;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.widget.EditText;
import android.widget.TextView;

public class FloatLabelViewHelper {

    public static final int SPACE_BETWEEN_HINT_AND_TEXT = -20;
    public static final float HINT_SIZE_SCALE = 0.75f;
    public int _fitScreenWidth;
    private float _inputTextSizeInSp;
    private int _focusedColor;
    private int _unFocusedColor;

    public void setFitScreenWidth(int fitScreenWidth) {
        _fitScreenWidth = fitScreenWidth;
    }

    public float getInputTextSizeInSp() {
        return _inputTextSizeInSp;
    }

    public void setInputTextSizeInSp(float inputTextSizeInSp) {
        _inputTextSizeInSp = inputTextSizeInSp;
    }

    public float getFloatHintSizeInSp() {
        return _inputTextSizeInSp * HINT_SIZE_SCALE;
    }

    public int getUnFocusedColor() {
        return _unFocusedColor;
    }

    public void setUnFocusedColor(int unFocusedColor) {
        _unFocusedColor = unFocusedColor;
    }

    public int getFocusedColor() {
        return _focusedColor;
    }

    public void setFocusedColor(int focusedColor) {
        _focusedColor = focusedColor;
    }

    public int getRightCoordinateForInputText(int parentWidth, int defaultInputTextWidth) {
        if (_fitScreenWidth == 1) {
            return parentWidth;
        }
        if (_fitScreenWidth == 2) {
            return (int) Math.round(parentWidth * 0.5);
        }
        return defaultInputTextWidth;
    }

    public void showOrHideFloatingLabel(EditText inputText, TextView floatHint) {
        boolean floatHintVisible = floatHint.getAlpha() != 0;

        if (!floatHintVisible && inputText.length() > 0) {
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 0);
            PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
            ObjectAnimator.ofPropertyValuesHolder(floatHint, pvhY, pvhAlpha).setDuration(350).start();
        }

        if (floatHintVisible && inputText.length() < 1) {
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y",
                                                                     (float) (0.2 *
                                                                              floatHint.getHeight()));
            PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 0f);
            ObjectAnimator.ofPropertyValuesHolder(floatHint, pvhY, pvhAlpha).setDuration(350).start();
        }
    }
}
