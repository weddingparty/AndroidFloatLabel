package com.micromobs.android.floatlabel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.EditText;

import com.google.common.base.Strings;

public class FloatLabelViewModel {

    public static final double HINT_SIZE_SCALE = 0.75;
    public static final int SPACE_BETWEEN_HINT_AND_TEXT = -10;
    private static final int HOLO_BLUE = Color.parseColor("#ff33b5e5");
    private static final int BLACK = android.R.color.black;
    private static final int GRAY = android.R.color.darker_gray;

    private float _inputTextSize;
    private float _floatHintSize;

    private int _floatHintColor;
    private int _inputTextColor;
    private int _highlightColor;

    private String _inputText = "";
    private String _floatHintText = "";

    private int _gravity;
    private boolean _isPassword = false;

    public FloatLabelViewModel(EditText editText) {
        setTextSizes(editText.getTextSize());

        _highlightColor = HOLO_BLUE;
        _inputTextColor = GRAY;

        _gravity = editText.getGravity();
    }

    public void setAttributes(TypedArray attributesFromXmlLayout, Context context) {
        setTextSizes(_getScaledFontSize(attributesFromXmlLayout, context));

        _floatHintText = attributesFromXmlLayout.getString(R.styleable.FloatLabelEditText_hint);
        _inputText = attributesFromXmlLayout.getString(R.styleable.FloatLabelEditText_text);

        _gravity = attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_gravity,
                                                  Gravity.LEFT);

    }

    public boolean showFloatHint() {
        return Strings.isNullOrEmpty(_inputText);
    }

    public void setTextSizes(float inputTextSize) {
        _inputTextSize = inputTextSize;
        _floatHintSize = Math.round(HINT_SIZE_SCALE * _inputTextSize);
    }

    private float _getScaledFontSize(TypedArray attributesFromXmlLayout, Context context) {
        int fontSizeFromAttributes = attributesFromXmlLayout.getDimensionPixelSize(R.styleable.FloatLabelEditText_textSize,
                                                                                   Math.round(_inputTextSize));

        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return fontSizeFromAttributes / scaledDensity;
    }


}
