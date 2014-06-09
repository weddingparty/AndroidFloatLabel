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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import static com.micromobs.android.floatlabel.FloatLabelViewModel.SPACE_BETWEEN_HINT_AND_TEXT;

@TargetApi(11)
public class FloatLabelEditText
    extends ViewGroup {

    private int _currentApiVersion = android.os.Build.VERSION.SDK_INT;
    private int _focusedColor;
    private int _unFocusedColor;
    private int _fitScreenWidth;
    private int _gravity;
    private float _textSizeInSp;

    private String _hintText;
    private String _editText;

    private boolean _isPassword = false;

    private FloatLabelViewModel _fvm;

    private AttributeSet _attributes;
    private Context _context;
    private EditText _inputText;
    private TextView _floatHint;

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

    @Override
    public void onMeasure(int parentWidthMeasureSpec, int parentHeightMeasureSpec) {

        int heightUsed = 0;

        measureChild(_inputText, parentWidthMeasureSpec, parentHeightMeasureSpec);
        heightUsed += _inputText.getMeasuredHeight();

        measureChild(_floatHint, parentWidthMeasureSpec, parentHeightMeasureSpec);
        heightUsed += _floatHint.getMeasuredHeight();

        final int totalWidth = MeasureSpec.getSize(parentWidthMeasureSpec);
        final int totalHeight = heightUsed +
                                getPaddingTop() +
                                getPaddingBottom();

        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        int currentTop = paddingTop;

        _floatHint.layout(paddingLeft,
                          currentTop,
                          _floatHint.getMeasuredWidth(),
                          currentTop + _floatHint.getMeasuredHeight());

        currentTop += _floatHint.getHeight() + SPACE_BETWEEN_HINT_AND_TEXT;

        _inputText.layout(paddingLeft,
                          currentTop,
                          _inputText.getMeasuredWidth(),
                          currentTop + _inputText.getMeasuredHeight());
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    // -----------------------------------------------------------------------
    // public interface

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

        LayoutInflater.from(_context)
                      .inflate(R.layout.com_micromobs_android_floatlabel, this, true);

        _floatHint = (TextView) findViewById(R.id.com_micromobs_android_floatlabel_float_hint);
        _inputText = (EditText) findViewById(R.id.com_micromobs_android_floatlabel_input_text);

        getAttributesFromXmlAndStoreLocally();
        setupEditTextView();
        setupFloatingLabel();
    }

    private void getAttributesFromXmlAndStoreLocally() {

        _fvm = new FloatLabelViewModel(_inputText);

        TypedArray attributesFromXmlLayout = _context.obtainStyledAttributes(_attributes,
                                                                             R.styleable.FloatLabelEditText);
        if (attributesFromXmlLayout != null) {
            _fvm.setAttributes(attributesFromXmlLayout, getContext());
        }

        _focusedColor = attributesFromXmlLayout.getColor(R.styleable.FloatLabelEditText_textColorHintFocused,
                                                         android.R.color.black);
        _unFocusedColor = attributesFromXmlLayout.getColor(R.styleable.FloatLabelEditText_textColorHintUnFocused,
                                                           android.R.color.darker_gray);
        _fitScreenWidth = attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_fitScreenWidth,
                                                         0);
        _isPassword = (attributesFromXmlLayout.getInt(R.styleable.FloatLabelEditText_inputType,
                                                      0) == 1);
        attributesFromXmlLayout.recycle();
    }

    private void setupEditTextView() {

        if (_isPassword) {
            _inputText.setInputType(InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            _inputText.setTypeface(Typeface.DEFAULT);
        }

        _inputText.setHint(_hintText);
        _inputText.setHintTextColor(_unFocusedColor);
        _inputText.setText(_editText);
        _inputText.setTextSize(TypedValue.COMPLEX_UNIT_SP, _textSizeInSp);
        _inputText.addTextChangedListener(getTextWatcher());

        if (_fitScreenWidth > 0) {
            _inputText.setWidth(getSpecialWidth());
        }

        if (_currentApiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            _inputText.setOnFocusChangeListener(getFocusChangeListener());
        }
    }

    private void setupFloatingLabel() {
        _floatHint.setText(_hintText);
        _floatHint.setTextColor(_unFocusedColor);
        _floatHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (_textSizeInSp / 1.3));
        _floatHint.setGravity(_gravity);
        _floatHint.setPadding(_inputText.getPaddingLeft(), 0, 0, 0);

        //        if (getText().length() > 0) {
        showFloatingLabel();
        //        }
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                /*if (s.length() > 0 && _floatHint.getVisibility() == INVISIBLE) {
                    showFloatingLabel();
                } else if (s.length() == 0 && _floatHint.getVisibility() == VISIBLE) {
                    hideFloatingLabel();
                }*/
            }
        };
    }

    private void showFloatingLabel() {
        _floatHint.setVisibility(VISIBLE);
        _floatHint.startAnimation(AnimationUtils.loadAnimation(getContext(),
                                                               R.anim.weddingparty_floatlabel_slide_from_bottom));
    }

    private void hideFloatingLabel() {
        _floatHint.setVisibility(INVISIBLE);
        _floatHint.startAnimation(AnimationUtils.loadAnimation(getContext(),
                                                               R.anim.weddingparty_floatlabel_slide_to_bottom));
    }

    private OnFocusChangeListener getFocusChangeListener() {
        return new OnFocusChangeListener() {

            ValueAnimator mFocusToUnfocusAnimation
                ,
                mUnfocusToFocusAnimation;

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
                if (mFocusToUnfocusAnimation == null) {
                    mFocusToUnfocusAnimation = getFocusAnimation(_unFocusedColor, _focusedColor);
                }
                return mFocusToUnfocusAnimation;
            }

            private ValueAnimator getUnfocusToFocusAnimation() {
                if (mUnfocusToFocusAnimation == null) {
                    mUnfocusToFocusAnimation = getFocusAnimation(_focusedColor, _unFocusedColor);
                }
                return mUnfocusToFocusAnimation;
            }
        };
    }

    private ValueAnimator getFocusAnimation(int fromColor, int toColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                                                              fromColor,
                                                              toColor);
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

    private int getSpecialWidth() {
        float screenWidth = ((WindowManager) _context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                                                                                               .getWidth();
        int prevWidth = _inputText.getWidth();

        switch (_fitScreenWidth) {
            case 2:
                return (int) Math.round(screenWidth * 0.5);
            default:
                return Math.round(screenWidth);
        }
    }


}
