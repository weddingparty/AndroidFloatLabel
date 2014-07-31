package com.micromobs.android.floatlabel;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import static android.view.View.VISIBLE;

public class FloatLabelViewHelper {

    public static final int SPACE_BETWEEN_HINT_AND_TEXT = -20;
    public static final float HINT_SIZE_SCALE = 0.75f;
    public int _fitScreenWidth;
    private float _inputTextSizeInSp;

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
        boolean currentlyShowing = floatHint.getVisibility() == VISIBLE;

        if (!currentlyShowing && inputText.length() > 0) {
            floatHint.setVisibility(VISIBLE);
            floatHint.startAnimation(AnimationUtils.loadAnimation(floatHint.getContext(),
                                                                  R.anim.weddingparty_floatlabel_slide_from_bottom));
        }
        if (currentlyShowing && inputText.length() < 1) {
            floatHint.setVisibility(View.INVISIBLE);
            floatHint.startAnimation(AnimationUtils.loadAnimation(floatHint.getContext(),
                                                                  R.anim.weddingparty_floatlabel_slide_to_bottom));
        }
    }
}
