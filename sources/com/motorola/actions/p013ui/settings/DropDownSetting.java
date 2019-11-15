package com.motorola.actions.p013ui.settings;

/* renamed from: com.motorola.actions.ui.settings.DropDownSetting */
public class DropDownSetting {
    private final String mDescription;
    private final int mId;
    private final boolean mIsDefault;
    private final String mTitle;

    DropDownSetting(int i, String str, String str2, boolean z) {
        this.mId = i;
        this.mTitle = str;
        this.mDescription = str2;
        this.mIsDefault = z;
    }

    public int getId() {
        return this.mId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public boolean isDefault() {
        return this.mIsDefault;
    }
}
