package com.motorola.actions.p013ui.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.motorola.actions.ui.settings.DropDownLayout */
public class DropDownLayout extends RelativeLayout {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(DropDownLayout.class);
    /* access modifiers changed from: private */
    public ActionListener mActionListener;
    private ImageView mDropdownArrow;
    private RelativeLayout mDropdownContainer;
    private final OnClickListener mDropdownContainerClickListener = new OnClickListener() {
        public void onClick(View view) {
            if (DropDownLayout.this.mDropdownMenu.getVisibility() == 8) {
                DropDownLayout.this.openDropdown();
            } else {
                DropDownLayout.this.closeDropdown();
            }
        }
    };
    /* access modifiers changed from: private */
    public LinearLayout mDropdownMenu;
    private InteractionListener mInteractionListener;
    /* access modifiers changed from: private */
    public View mSelectedSetting;
    /* access modifiers changed from: private */
    public TextView mSelectedSettingDescription;
    /* access modifiers changed from: private */
    public TextView mSelectedSettingTitle;
    private final ArrayList<DropDownSetting> mSettings = new ArrayList<>();
    private final OnClickListener mSettingsItemClickListener = new OnClickListener() {
        public void onClick(View view) {
            TextView textView = (TextView) view.findViewById(C0504R.C0506id.title);
            TextView textView2 = (TextView) view.findViewById(C0504R.C0506id.description);
            DropDownLayout.this.mSelectedSetting.setTag(view.getTag());
            DropDownLayout.this.mSelectedSettingTitle.setText(textView.getText());
            DropDownLayout.this.mSelectedSettingDescription.setText(textView2.getText());
            view.setVisibility(8);
            DropDownLayout.this.mSettingsOmittedView.setVisibility(0);
            DropDownLayout.this.mSettingsOmittedView = view;
            DropDownLayout.this.closeDropdown();
            DropDownLayout.LOGGER.mo11957d("Clicked item");
            if (DropDownLayout.this.mActionListener != null) {
                DropDownLayout.this.mActionListener.dropDownItemSelected(((Integer) view.getTag()).intValue());
                return;
            }
            throw new RuntimeException("ActionListener not set");
        }
    };
    /* access modifiers changed from: private */
    public View mSettingsOmittedView;

    /* renamed from: com.motorola.actions.ui.settings.DropDownLayout$ActionListener */
    public interface ActionListener {
        void dropDownItemSelected(int i);
    }

    /* renamed from: com.motorola.actions.ui.settings.DropDownLayout$InteractionListener */
    public interface InteractionListener {
        void dropDownClosed();

        void dropDownOpened();
    }

    public DropDownLayout(Context context) {
        super(context);
        View.inflate(context, C0504R.layout.drop_down_layout, this);
    }

    public DropDownLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View.inflate(context, C0504R.layout.drop_down_layout, this);
    }

    public DropDownLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        View.inflate(context, C0504R.layout.drop_down_layout, this);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mDropdownContainer = (RelativeLayout) findViewById(C0504R.C0506id.selected_setting_container);
        this.mDropdownMenu = (LinearLayout) findViewById(C0504R.C0506id.dropdown_settings_menu);
        if (this.mDropdownContainer != null) {
            this.mSelectedSetting = this.mDropdownContainer.findViewById(C0504R.C0506id.selected_setting);
            this.mSelectedSettingTitle = (TextView) this.mDropdownContainer.findViewById(C0504R.C0506id.selected_setting_title);
            this.mSelectedSettingDescription = (TextView) this.mDropdownContainer.findViewById(C0504R.C0506id.selected_setting_description);
            this.mDropdownArrow = (ImageView) this.mDropdownContainer.findViewById(C0504R.C0506id.dropdown_settings_arrow);
        }
    }

    public void clear() {
        this.mSettings.clear();
    }

    public void setActionListener(ActionListener actionListener) {
        this.mActionListener = actionListener;
    }

    public void setInteractionListener(InteractionListener interactionListener) {
        this.mInteractionListener = interactionListener;
    }

    /* access modifiers changed from: 0000 */
    public void addSettingsOption(DropDownSetting dropDownSetting) {
        this.mSettings.add(dropDownSetting);
    }

    public void setSelectedSettings(int i) {
        if (this.mSelectedSetting != null) {
            this.mSelectedSetting.setTag(Integer.valueOf(i));
        }
        if (this.mSelectedSettingTitle != null && this.mSelectedSettingDescription != null) {
            Iterator it = this.mSettings.iterator();
            while (it.hasNext()) {
                DropDownSetting dropDownSetting = (DropDownSetting) it.next();
                if (dropDownSetting.getId() == i) {
                    this.mSelectedSettingTitle.setText(dropDownSetting.getTitle());
                    this.mSelectedSettingDescription.setText(dropDownSetting.getDescription());
                    return;
                }
            }
        }
    }

    public void setEnabled() {
        this.mDropdownContainer.setAlpha(1.0f);
        if (this.mDropdownArrow != null) {
            this.mDropdownArrow.setImageResource(C0504R.C0505drawable.dropdown_arrow_enabled);
        }
        setSettingsDropdown();
    }

    public void setDisabled() {
        this.mDropdownContainer.setAlpha(0.5f);
        if (this.mDropdownArrow != null) {
            this.mDropdownArrow.setImageResource(C0504R.C0505drawable.dropdown_arrow_disabled);
        }
        closeDropdown();
        this.mDropdownContainer.setOnClickListener(null);
    }

    private void setSettingsDropdown() {
        Context appContext = ActionsApplication.getAppContext();
        if (this.mDropdownContainer != null && this.mDropdownMenu != null) {
            this.mDropdownContainer.setOnClickListener(this.mDropdownContainerClickListener);
            if (this.mDropdownMenu.getChildCount() == 0) {
                DropDownSettingsAdapter dropDownSettingsAdapter = new DropDownSettingsAdapter(appContext, new ArrayList());
                Iterator it = this.mSettings.iterator();
                while (it.hasNext()) {
                    dropDownSettingsAdapter.add((DropDownSetting) it.next());
                }
                int count = dropDownSettingsAdapter.getCount();
                for (int i = 0; i < count; i++) {
                    View view = dropDownSettingsAdapter.getView(i, null, null);
                    DropDownSetting dropDownSetting = (DropDownSetting) dropDownSettingsAdapter.getItem(i);
                    if (dropDownSetting != null) {
                        addSettingOptionToMenu(view, dropDownSetting.isDefault());
                    }
                }
            }
        }
    }

    private void addSettingOptionToMenu(View view, boolean z) {
        if (this.mSelectedSetting != null && this.mSelectedSettingTitle != null && this.mSelectedSettingDescription != null) {
            if (z) {
                view.setVisibility(8);
                this.mSettingsOmittedView = view;
            }
            view.setOnClickListener(this.mSettingsItemClickListener);
            this.mDropdownMenu.addView(view);
        }
    }

    /* access modifiers changed from: private */
    public void openDropdown() {
        if (this.mDropdownMenu != null && this.mDropdownMenu.getVisibility() != 0) {
            this.mDropdownMenu.setVisibility(0);
            if (this.mDropdownArrow != null) {
                this.mDropdownArrow.setVisibility(8);
            }
            if (this.mInteractionListener != null) {
                this.mInteractionListener.dropDownOpened();
            }
        }
    }

    public void closeDropdown() {
        if (this.mDropdownMenu != null && this.mDropdownMenu.getVisibility() == 0) {
            this.mDropdownMenu.setVisibility(8);
            if (this.mDropdownArrow != null) {
                this.mDropdownArrow.setVisibility(0);
            }
            if (this.mInteractionListener != null) {
                this.mInteractionListener.dropDownClosed();
            }
        }
    }
}
