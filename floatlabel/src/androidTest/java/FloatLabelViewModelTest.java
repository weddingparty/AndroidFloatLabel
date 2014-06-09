import android.test.AndroidTestCase;

import com.micromobs.android.floatlabel.FloatLabelViewModel;

import static org.fest.assertions.api.Assertions.assertThat;

public class FloatLabelViewModelTest
    extends AndroidTestCase {

    FloatLabelViewModel _fvm;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _fvm = new FloatLabelViewModel();
    }

    public void test_ShouldShowFloatHint_WhenInputTextEmpty() {
        assertThat(_fvm.showFloatHint()).isTrue();
    }

    public void test_ShouldNotShowFloatHint_WhenInputTextSet() {
        _fvm.setText("Some random input text");
        assertThat(_fvm.showFloatHint()).isFalse();
    }

    public void test_ShouldHaveFocusedColor_WhenFocus() { }

    public void test_ShouldHaveUnFocusedColor_WhenNotInFocus(){}


}
