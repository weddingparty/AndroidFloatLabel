package com.micromobs.android.floatlabel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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

public class FloatLabelEditText
    extends ViewGroup {

    private int _gravity;

    private String _floatHintString;
    private String _inputTextString;

    private boolean _isPassword = false;

    private AttributeSet _attributes;
    private Context _context;
    private EditText _inputTextView;
    private TextView _floatHintView;
    private FloatLabelViewHelper _fvh;

    // -----------------------------------------------------------------------
    // default constructors

    public FloatLabelEditText(Context context) {
        super(context);
        _context = context;
        _initializeView();
    }

    public FloatLabelEditText(Context context, AttributeSet attributes) {
        super(context, attributes);
        _context = context;
        _attributes = attributes;
        _initializeView();
    }

    public FloatLabelEditText(Context context, AttributeSet attributes, int defStyle) {
        super(context, attributes, defStyle);
        _context = context;
        _attributes = attributes;
        _initializeView();
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
        measureChild(_inputTextView, widthMeasureSpec, heightMeasureSpec);
        measureChild(_floatHintView, widthMeasureSpec, heightMeasureSpec);

        // 2. -------------------------------------------------------------------------------------------
        final int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int totalHeight = _inputTextView.getMeasuredHeight() +
                                _floatHintView.getMeasuredHeight() +
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

        final int heightTakenByFloatHint = getPaddingTop() + _floatHintView.getMeasuredHeight();

        // layout the Input Text
        _inputTextView.layout(getPaddingLeft(),
                              heightTakenByFloatHint + FloatLabelViewHelper.SPACE_BETWEEN_HINT_AND_TEXT,
                              _fvh.getRightCoordinateForInputText(getMeasuredWidth(),
                                                                  _inputTextView.getMeasuredWidth()),
                              heightTakenByFloatHint +
                              FloatLabelViewHelper.SPACE_BETWEEN_HINT_AND_TEXT +
                              _inputTextView.getMeasuredHeight());

        // layout the Float Hint
        _floatHintView.layout(getPaddingLeft() + _inputTextView.getPaddingLeft(),
                              getPaddingTop(),
                              _floatHintView.getMeasuredWidth(),
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

    public EditText getInputTextString() {
        return _inputTextView;
    }

    public String getText() {
        if (_getEditTextString() != null &&
            _getEditTextString().toString() != null &&
            _getEditTextString().toString().length() > 0) {
            return _getEditTextString().toString();
        }
        return "";
    }

    public void setHint(String hintText) {
        _floatHintString = hintText;
        _floatHintView.setText(hintText);
        _setupEditTextView();
    }

    // -----------------------------------------------------------------------
    // private helpers

    private void _initializeView() {
        if (_context == null) {
            return;
        }

        LayoutInflater.from(_context).inflate(R.layout.com_micromobs_android_floatlabel, this, true);

        _floatHintView = (TextView) findViewById(R.id.com_micromobs_android_floatlabel_float_hint);
        _inputTextView = (EditText) findViewById(R.id.com_micromobs_android_floatlabel_input_text);
        _fvh = new FloatLabelViewHelper();

        _getAttributesFromXmlAndStoreLocally();
        _setupEditTextView();
        _setupFloatingLabel();
    }

    private void _getAttributesFromXmlAndStoreLocally() {
        TypedArray attributesFromXmlLayout = _context.obtainStyledAttributes(_attributes,
                                                                             R.styleable.FloatLabelEditText);
        if (attributesFromXmlLayout == null) {
            return;
        }

        _floatHintString = attributesFromXmlLayout.getString(R.styleable.FloatLabelEditText_hint);
        _inputTextString = attributesFromXmlLayout.getString(R.styleable.FloatLabelEditText_text);
        _gravity = attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_gravity, Gravity.LEFT);
        _isPassword = (attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_inputType, 0) == 1);

        _fvh.setFocusedColor(attributesFromXmlLayout.getColor(R.styleable.FloatLabelEditText_textColorHintFocused,
                                                              Color.parseColor("#ff0099cc")));
        _fvh.setUnFocusedColor(attributesFromXmlLayout.getColor(R.styleable.FloatLabelEditText_textColorHintUnFocused,
                                                                Color.parseColor("#8D8D8D")));
        _fvh.setInputTextSizeInSp(_getScaledFontSize(attributesFromXmlLayout.getDimensionPixelSize(R.styleable.FloatLabelEditText_textSize,
                                                                                                   (int) _inputTextView
                                                                                                             .getTextSize())));
        _fvh.setFitScreenWidth(attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_fitScreenWidth,
                                                              0));
        attributesFromXmlLayout.recycle();
    }

    private void _setupEditTextView() {

        if (_isPassword) {
            _inputTextView.setInputType(InputType.TYPE_CLASS_TEXT |
                                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            _inputTextView.setTypeface(Typeface.DEFAULT);
        }

        _inputTextView.setHint(_floatHintString);
        _inputTextView.setHintTextColor(_fvh.getUnFocusedColor());
        _inputTextView.setText(_inputTextString);
        _inputTextView.setTextSize(_fvh.getInputTextSizeInSp());
        _inputTextView.addTextChangedListener(_getTextWatcher());

        _inputTextView.setOnFocusChangeListener(_getFocusChangeListener());
    }

    private void _setupFloatingLabel() {
        _floatHintView.setGravity(_gravity); // TODO: onLayout should take care of this

        _floatHintView.setText(_floatHintString);
        _floatHintView.setTextColor(_fvh.getUnFocusedColor());
        _floatHintView.setTextSize(TypedValue.COMPLEX_UNIT_SP, _fvh.getFloatHintSizeInSp());

        _fvh.showOrHideFloatingLabel(_inputTextView, _floatHintView);
    }

    private Editable _getEditTextString() {
        return _inputTextView.getText();
    }

    private float _getScaledFontSize(float fontSizeFromAttributes) {
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return fontSizeFromAttributes / scaledDensity;
    }

    private TextWatcher _getTextWatcher() {
        return new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                _fvh.showOrHideFloatingLabel(_inputTextView, _floatHintView);
            }
        };
    }

    private OnFocusChangeListener _getFocusChangeListener() {
        return new OnFocusChangeListener() {


            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                FloatLabelAnimationHelper.getFloatHintColorChangeAnimation(_floatHintView,
                                                                           _fvh,
                                                                           hasFocus).start();
            }
        };
    }
}
