package com.micromobs.android.floatlabel;

//import org.hamcrest.Matcher;

import android.view.View;
import android.widget.EditText;

import com.google.android.apps.common.testing.ui.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class FloatLabelMatchers {

    /**
     * @param floatLabelHint
     * @return View Matcher for the child EditText
     */
    public static Matcher<View> withFloatLabelHint(final String floatLabelHint) {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("has floatlabel hint ");
                description.appendValue(floatLabelHint);

                if (null != floatLabelHint) {
                    description.appendText("[");
                    description.appendText(floatLabelHint);
                    description.appendText("]");
                }

                if (null != floatLabelHint) {
                    description.appendText(" value: ");
                    description.appendText(floatLabelHint);
                }
            }

            @Override
            protected boolean matchesSafely(EditText editText) {
                if (!(editText instanceof EditText)) {
                    return false;
                }

                String hint = ((EditText) editText).getHint().toString();

                return floatLabelHint.equals(hint);
            }
        };
    }
}
