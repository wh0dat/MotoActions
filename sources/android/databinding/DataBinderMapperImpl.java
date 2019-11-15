package android.databinding;

import android.view.View;
import com.motorola.actions.C0504R;
import com.motorola.actions.databinding.ActivityWelcomeQuickscreenshotBinding;
import com.motorola.actions.databinding.ComponentBottomBarBorderAlignedTwoButtons2018Binding;
import com.motorola.actions.databinding.ComponentBottomBarBorderAlignedTwoButtonsBinding;

class DataBinderMapperImpl extends DataBinderMapper {

    private static class InnerBrLookup {
        static String[] sKeys = {"_all", "leftText", "onLeftClick", "onRightClick", "presenter", "rightText"};

        private InnerBrLookup() {
        }
    }

    public ViewDataBinding getDataBinder(DataBindingComponent dataBindingComponent, View[] viewArr, int i) {
        return null;
    }

    public ViewDataBinding getDataBinder(DataBindingComponent dataBindingComponent, View view, int i) {
        if (i != C0504R.layout.activity_welcome_quickscreenshot) {
            switch (i) {
                case C0504R.layout.component_bottom_bar_border_aligned_two_buttons /*2131427371*/:
                    Object tag = view.getTag();
                    if (tag == null) {
                        throw new RuntimeException("view must have a tag");
                    } else if ("layout/component_bottom_bar_border_aligned_two_buttons_0".equals(tag)) {
                        return new ComponentBottomBarBorderAlignedTwoButtonsBinding(dataBindingComponent, view);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("The tag for component_bottom_bar_border_aligned_two_buttons is invalid. Received: ");
                        sb.append(tag);
                        throw new IllegalArgumentException(sb.toString());
                    }
                case C0504R.layout.component_bottom_bar_border_aligned_two_buttons_2018 /*2131427372*/:
                    Object tag2 = view.getTag();
                    if (tag2 == null) {
                        throw new RuntimeException("view must have a tag");
                    } else if ("layout/component_bottom_bar_border_aligned_two_buttons_2018_0".equals(tag2)) {
                        return new ComponentBottomBarBorderAlignedTwoButtons2018Binding(dataBindingComponent, view);
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("The tag for component_bottom_bar_border_aligned_two_buttons_2018 is invalid. Received: ");
                        sb2.append(tag2);
                        throw new IllegalArgumentException(sb2.toString());
                    }
                default:
                    return null;
            }
        } else {
            Object tag3 = view.getTag();
            if (tag3 == null) {
                throw new RuntimeException("view must have a tag");
            } else if ("layout/activity_welcome_quickscreenshot_0".equals(tag3)) {
                return new ActivityWelcomeQuickscreenshotBinding(dataBindingComponent, view);
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("The tag for activity_welcome_quickscreenshot is invalid. Received: ");
                sb3.append(tag3);
                throw new IllegalArgumentException(sb3.toString());
            }
        }
    }

    public int getLayoutId(String str) {
        if (str == null) {
            return 0;
        }
        int hashCode = str.hashCode();
        if (hashCode != -688807396) {
            if (hashCode != 528960060) {
                if (hashCode == 794858922 && str.equals("layout/component_bottom_bar_border_aligned_two_buttons_0")) {
                    return C0504R.layout.component_bottom_bar_border_aligned_two_buttons;
                }
            } else if (str.equals("layout/activity_welcome_quickscreenshot_0")) {
                return C0504R.layout.activity_welcome_quickscreenshot;
            }
        } else if (str.equals("layout/component_bottom_bar_border_aligned_two_buttons_2018_0")) {
            return C0504R.layout.component_bottom_bar_border_aligned_two_buttons_2018;
        }
        return 0;
    }

    public String convertBrIdToString(int i) {
        if (i < 0 || i >= InnerBrLookup.sKeys.length) {
            return null;
        }
        return InnerBrLookup.sKeys[i];
    }
}
