package com.micromobs.android.floatlabel;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

@TargetApi(11)
public class FloatLabelEditText
    extends ViewGroup {

    private int _currentApiVersion = android.os.Build.VERSION.SDK_INT;
    private int _focusedColor;
    private int _unFocusedColor;
    private int _gravity;

    private String _hintText;
    private String _editText;

    private boolean _isPassword = false;

    private AttributeSet _attributes;
    private Context _context;
    private EditText _inputText;
    private TextView _floatHint;
    private FloatLabelViewHelper _fvh;

    // -----------------------------------------------------------------------
    // default constructors

    public FloatLabelEditText(Context context) {
        super(context);
        _context = context;
        initializeView();
    }

    public FloatLabelEditText(Context context, AttributeSet attributes) {
        super(context, attributes);
        _context = context;
        _attributes = attributes;
        initializeView();
    }

    public FloatLabelEditText(Context context, AttributeSet attributes, int defStyle) {
        super(context, attributes, defStyle);
        _context = context;
        _attributes = attributes;
        initializeView();
    }

    // -----------------------------------------------------------------------
    // Custom ViewGroup implementation

    /**
     * This method helps the app understand the precise dimensions of this view
     * including any child views.
     *
     * This is done by:
     * 1. specifying dimensions of each child view (measureChild)
     * 2. calculating the overall dimensions by adding up the child view dimensions + padding (as Android expects every individual view to control its padding)
     * 3. setting the overall dimensions (setMeasureDimension)
     *
     * @param widthMeasureSpec  packed data that gives us two attributes(mode & size) for the width of this whole view
     * @param heightMeasureSpec packed data that gives us two attributes(mode & size) for the height of this whole view
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 1. -------------------------------------------------------------------------------------------
        // measureChild measures the view thus allowing us to get individual heights/widths from the children
        measureChild(_inputText, widthMeasureSpec, heightMeasureSpec);
        measureChild(_floatHint, widthMeasureSpec, heightMeasureSpec);

        // 2. -------------------------------------------------------------------------------------------
        final int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int totalHeight = _inputText.getMeasuredHeight() +
                                _floatHint.getMeasuredHeight() +
                                getPaddingTop() +
                                getPaddingBottom();

        // 3. -------------------------------------------------------------------------------------------
        setMeasuredDimension(totalWidth, totalHeight);
    }


    /**
     * This method helps the app understand how to layout each child in the view
     * given the dimensions from {@link #onMeasure(int, int)}
     */
    @Override
    public void onLayout(boolean changed,
                         int parentLeftCoordinate,
                         int parentTopCoordinate,
                         int rightCoordinate,
                         int bottomCoordinate) {

        final int heightTakenByFloatHint = getPaddingTop() + _floatHint.getMeasuredHeight();

        // layout the Input Text
        _inputText.layout(getPaddingLeft(),
                          heightTakenByFloatHint + FloatLabelViewHelper.SPACE_BETWEEN_HINT_AND_TEXT,
                          _fvh.getRightCoordinateForInputText(getMeasuredWidth(),
                                                              _inputText.getMeasuredWidth()),
                          heightTakenByFloatHint +
                          FloatLabelViewHelper.SPACE_BETWEEN_HINT_AND_TEXT +
                          _inputText.getMeasuredHeight());

        // layout the Float Hint
        _floatHint.layout(getPaddingLeft() + _inputText.getPaddingLeft(),
                          getPaddingTop(),
                          _floatHint.getMeasuredWidth(),
                          heightTakenByFloatHint);


    }

    /**
     * The default is true and is only required if your container is scrollable
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    // -----------------------------------------------------------------------
    // public API

    public EditText getEditText() {
        return _inputText;
    }

    public String getText() {
        if (getEditTextString() != null &&
            getEditTextString().toString() != null &&
            getEditTextString().toString().length() > 0) {
            return getEditTextString().toString();
        }
        return "";
    }

    public void setHint(String hintText) {
        _hintText = hintText;
        _floatHint.setText(hintText);
        setupEditTextView();
    }

    // -----------------------------------------------------------------------
    // private helpers

    private void initializeView() {

        if (_context == null) {
            return;
        }

        LayoutInflater.from(_context).inflate(R.layout.com_micromobs_android_floatlabel, this, true);

        _floatHint = (TextView) findViewById(R.id.com_micromobs_android_floatlabel_float_hint);
        _inputText = (EditText) findViewById(R.id.com_micromobs_android_floatlabel_input_text);
        _fvh = new FloatLabelViewHelper();

        getAttributesFromXmlAndStoreLocally();
        setupEditTextView();
        setupFloatingLabel();
    }

    private void getAttributesFromXmlAndStoreLocally() {
        TypedArray attributesFromXmlLayout = _context.obtainStyledAttributes(_attributes,
                                                                             R.styleable.FloatLabelEditText);
        if (attributesFromXmlLayout == null) {
            return;
        }

        _hintText = attributesFromXmlLayout.getString(R.styleable.FloatLabelEditText_hint);
        _editText = attributesFromXmlLayout.getString(R.styleable.FloatLabelEditText_text);
        _gravity = attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_gravity, Gravity.LEFT);
        _isPassword = (attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_inputType, 0) == 1);

        _focusedColor = attributesFromXmlLayout.getColor(R.styleable.FloatLabelEditText_textColorHintFocused,
                                                         android.R.color.black);
        _unFocusedColor = attributesFromXmlLayout.getColor(R.styleable.FloatLabelEditText_textColorHintUnFocused,
                                                           android.R.color.darker_gray);

        _fvh.setInputTextSizeInSp(getScaledFontSize(attributesFromXmlLayout.getDimensionPixelSize(R.styleable.FloatLabelEditText_textSize,
                                                                                                  (int) _inputText
                                                                                                            .getTextSize())));
        _fvh.setFitScreenWidth(attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_fitScreenWidth,
                                                              0));
        attributesFromXmlLayout.recycle();
    }

    private void setupEditTextView() {

        if (_isPassword) {
            _inputText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            _inputText.setTypeface(Typeface.DEFAULT);
        }

        _inputText.setHint(_hintText);
        _inputText.setHintTextColor(_unFocusedColor);
        _inputText.setText(_editText);
        _inputText.setTextSize(_fvh.getInputTextSizeInSp());
        _inputText.addTextChangedListener(getTextWatcher());

        if (_currentApiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            _inputText.setOnFocusChangeListener(getFocusChangeListener());
        }
    }

    private void setupFloatingLabel() {
        _floatHint.setGravity(_gravity);
        _floatHint.setText(_hintText);
        _floatHint.setTextColor(_unFocusedColor);
        _floatHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, _fvh.getFloatHintSizeInSp());

        _fvh.showOrHideFloatingLabel(_inputText, _floatHint);
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                _fvh.showOrHideFloatingLabel(_inputText, _floatHint);
            }
        };
    }

    private OnFocusChangeListener getFocusChangeListener() {
        return new OnFocusChangeListener() {


            ValueAnimator _focusToUnfocus
                ,
                unfocusToFocus;

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
                    _focusToUnfocus = getFocusAnimation(_unFocusedColor, _focusedColor);
                }
                return _focusToUnfocus;
            }

            private ValueAnimator getUnfocusToFocusAnimation() {
                if (unfocusToFocus == null) {
                    unfocusToFocus = getFocusAnimation(_focusedColor, _unFocusedColor);
                }
                return unfocusToFocus;
            }
        };
    }

    private ValueAnimator getFocusAnimation(int fromColor, int toColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                _floatHint.setTextColor((Integer) animator.getAnimatedValue());
            }
        });
        return colorAnimation;
    }

    private Editable getEditTextString() {
        return _inputText.getText();
    }

    private float getScaledFontSize(float fontSizeFromAttributes) {
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return fontSizeFromAttributes / scaledDensity;
    }

}
