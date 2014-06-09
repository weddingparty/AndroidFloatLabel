package com.micromobs.android.floatlabel;

import com.google.common.base.Strings;

public class FloatLabelViewModel {

    public static final double HINT_SIZE_SCALE = 0.75;
    public static final int SPACE_BETWEEN_HINT_AND_TEXT = 8;
    private String _inputText = "";
    private String _floatHintText = "";
    private boolean _isPassword = false;

    private int _floatHintColor = android.R.color.black;
    private int _inputTextColor = android.R.color.darker_gray;


    public String getInputText() {
        return _inputText;
    }

    public void setText(String text) {
        _inputText = text;
    }

    public void setFloatHintText(String text) {
        _floatHintText = text;
    }

    public boolean showFloatHint() {
        return Strings.isNullOrEmpty(_inputText);
    }

}
