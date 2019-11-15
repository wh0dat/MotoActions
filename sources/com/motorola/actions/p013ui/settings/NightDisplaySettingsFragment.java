package com.motorola.actions.p013ui.settings;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.p001v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.nightdisplay.common.SleepPatternAccess;
import com.motorola.actions.nightdisplay.p008pd.NightDisplayUpdateReceiver;
import com.motorola.actions.nightdisplay.p008pd.TwilightAccess;
import com.motorola.actions.p013ui.settings.DropDownLayout.ActionListener;
import com.motorola.actions.p013ui.tutorial.nightdisplay.NightDisplayDefaultTutorialActivity;
import com.motorola.actions.p013ui.tutorial.nightdisplay.NightDisplaySleepPatternTutorialActivity;
import com.motorola.actions.settings.updater.NDSettingsUpdater;
import com.motorola.actions.sleepPattern.SleepPatternService;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.KotlinFunctionsKt;
import com.motorola.actions.utils.MALogger;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u0000 J2\u00020\u00012\u00020\u0002:\u0001JB\u0005¢\u0006\u0002\u0010\u0003J\u0010\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0012H\u0014J\u0010\u0010\u001c\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\tH\u0016J\b\u0010\u001e\u001a\u00020\tH\u0014J\b\u0010\u001f\u001a\u00020 H\u0014J\b\u0010!\u001a\u00020\"H\u0014J\u0010\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020\tH\u0002J\b\u0010%\u001a\u00020\tH\u0014J\u0010\u0010&\u001a\u00020\t2\u0006\u0010'\u001a\u00020\tH\u0002J\b\u0010(\u001a\u00020\tH\u0002J\b\u0010)\u001a\u00020\tH\u0014J\b\u0010*\u001a\u00020\tH\u0014J\b\u0010+\u001a\u00020\u0007H\u0002J\u0010\u0010,\u001a\u00020\u00072\u0006\u0010-\u001a\u00020\u0012H\u0002J\b\u0010.\u001a\u00020\u0007H\u0016J \u0010/\u001a\u00020\u00072\u0006\u00100\u001a\u00020\u00122\u0006\u00101\u001a\u00020\t2\u0006\u00102\u001a\u00020\tH\u0002J\u001a\u00103\u001a\u00020\u00072\u0006\u00104\u001a\u00020\u00062\b\u00105\u001a\u0004\u0018\u000106H\u0016J\u0018\u00107\u001a\u00020\u00072\u0006\u00100\u001a\u00020\u00122\u0006\u00108\u001a\u00020\tH\u0002J\b\u00109\u001a\u00020\u0007H\u0002J\b\u0010:\u001a\u00020\u0007H\u0002J\b\u0010;\u001a\u00020\u0007H\u0002J\b\u0010<\u001a\u00020\u0007H\u0002J\u0010\u0010=\u001a\u00020\u00072\u0006\u0010'\u001a\u00020\tH\u0002J\b\u0010>\u001a\u00020\u0007H\u0002J\f\u0010?\u001a\u0006\u0012\u0002\b\u00030@H\u0014J\b\u0010A\u001a\u00020\u0007H\u0002J\u0010\u0010B\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\tH\u0002J\b\u0010C\u001a\u00020\u0007H\u0002J\b\u0010D\u001a\u00020\u0007H\u0002J\u0010\u0010E\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0012H\u0002J\b\u0010F\u001a\u00020\u0007H\u0002J\u0010\u0010G\u001a\u00020\u00072\u0006\u0010-\u001a\u00020\u0012H\u0002J\b\u0010H\u001a\u00020\u0007H\u0002J\u0010\u0010I\u001a\u00020\u00072\u0006\u0010-\u001a\u00020\u0012H\u0002R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0017X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006K"}, mo14495d2 = {"Lcom/motorola/actions/ui/settings/NightDisplaySettingsFragment;", "Lcom/motorola/actions/ui/settings/SettingsDetailFragment;", "Lcom/motorola/actions/ui/settings/DropDownLayout$ActionListener;", "()V", "endClickListener", "Lkotlin/Function1;", "Landroid/view/View;", "", "mColorTemperatureMax", "", "mColorTemperatureMin", "mEndPreviewRunnable", "Ljava/lang/Runnable;", "mFallbackIndicatorEnd", "mFallbackIndicatorStart", "mHandler", "Landroid/os/Handler;", "mIsFallback", "", "mIsPreviewMode", "mPreviewSnackbar", "Landroid/support/design/widget/Snackbar;", "mTextEndCustom", "Landroid/widget/TextView;", "mTextStartCustom", "startClickListener", "changeStatus", "status", "dropDownItemSelected", "mode", "getDetailTextId", "getEnabledStatus", "Lcom/motorola/actions/ui/settings/SettingsDetailFragment$SettingStatus;", "getFeatureKey", "Lcom/motorola/actions/FeatureKey;", "getIntensity", "progress", "getLayoutId", "getProgress", "intensity", "getTimeTextColor", "getTitleTextId", "getVideoId", "loadColorTemperatures", "loadSettingsDropdown", "isEnabled", "onResume", "onTimeSet", "isStartTime", "selectedHour", "selectedMinute", "onViewCreated", "view", "savedInstanceState", "Landroid/os/Bundle;", "openTimePicker", "minute", "setupEndTime", "setupFilterType", "setupNightDisplayUi", "setupStartTime", "showNightDisplayPreview", "stopPreviewMode", "tutorialClass", "Ljava/lang/Class;", "updateEndText", "updateFallbackStatus", "updateFallbackText", "updateNightDisplayUi", "updateOptions", "updateStartText", "updateStatus", "updateTexts", "updateVisibility", "Companion", "MotoActions_release"}, mo14496k = 1, mo14497mv = {1, 1, 11})
/* renamed from: com.motorola.actions.ui.settings.NightDisplaySettingsFragment */
/* compiled from: NightDisplaySettingsFragment.kt */
public final class NightDisplaySettingsFragment extends SettingsDetailFragment implements ActionListener {
    public static final Companion Companion = new Companion(null);
    private static final long DELAY_EXIT_PREVIEW = 3000;
    private static final int[][] DROPDOWN_OPTIONS;
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(NightDisplaySettingsFragment.class);
    private static final int MAX_INTENSITY_SEEK_VALUE = 20;
    private HashMap _$_findViewCache;
    private final Function1<View, Unit> endClickListener = new NightDisplaySettingsFragment$endClickListener$1(this);
    private int mColorTemperatureMax;
    private int mColorTemperatureMin;
    /* access modifiers changed from: private */
    public final Runnable mEndPreviewRunnable = new NightDisplaySettingsFragment$mEndPreviewRunnable$1(this);
    private View mFallbackIndicatorEnd;
    private View mFallbackIndicatorStart;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    private boolean mIsFallback;
    /* access modifiers changed from: private */
    public boolean mIsPreviewMode;
    /* access modifiers changed from: private */
    public Snackbar mPreviewSnackbar;
    private TextView mTextEndCustom;
    private TextView mTextStartCustom;
    private final Function1<View, Unit> startClickListener = new NightDisplaySettingsFragment$startClickListener$1(this);

    @Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fXT¢\u0006\u0002\n\u0000¨\u0006\r"}, mo14495d2 = {"Lcom/motorola/actions/ui/settings/NightDisplaySettingsFragment$Companion;", "", "()V", "DELAY_EXIT_PREVIEW", "", "DROPDOWN_OPTIONS", "", "", "[[I", "LOGGER", "Lcom/motorola/actions/utils/MALogger;", "MAX_INTENSITY_SEEK_VALUE", "", "MotoActions_release"}, mo14496k = 1, mo14497mv = {1, 1, 11})
    /* renamed from: com.motorola.actions.ui.settings.NightDisplaySettingsFragment$Companion */
    /* compiled from: NightDisplaySettingsFragment.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view == null) {
            View view2 = getView();
            if (view2 == null) {
                return null;
            }
            view = view2.findViewById(i);
            this._$_findViewCache.put(Integer.valueOf(i), view);
        }
        return view;
    }

    /* access modifiers changed from: protected */
    public int getDetailTextId() {
        return C0504R.string.night_shade_checkbox_summary;
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C0504R.layout.fragment_settings_night_display;
    }

    /* access modifiers changed from: protected */
    public int getTitleTextId() {
        return C0504R.string.night_shade_enabled;
    }

    /* access modifiers changed from: protected */
    public int getVideoId() {
        return C0504R.raw.night_display_settings;
    }

    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    /* access modifiers changed from: protected */
    @NotNull
    public Class<?> tutorialClass() {
        if (SleepPatternService.isFeatureSupported()) {
            return NightDisplaySleepPatternTutorialActivity.class;
        }
        return NightDisplayDefaultTutorialActivity.class;
    }

    /* access modifiers changed from: protected */
    @NotNull
    public FeatureKey getFeatureKey() {
        return FeatureKey.NIGHT_DISPLAY;
    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        loadColorTemperatures();
        setupNightDisplayUi();
        setDisplayFamilyColor();
    }

    /* access modifiers changed from: protected */
    @NotNull
    public SettingStatus getEnabledStatus() {
        if (Persistence.isFeatureEnabled()) {
            return SettingStatus.ENABLED;
        }
        return SettingStatus.DISABLED;
    }

    /* access modifiers changed from: protected */
    public void changeStatus(boolean z) {
        SettingStatus settingStatus;
        NDSettingsUpdater.getInstance().toggleStatus(z, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS);
        if (z) {
            settingStatus = SettingStatus.ENABLED;
        } else {
            settingStatus = SettingStatus.DISABLED;
        }
        updateStatus(settingStatus);
        updateNightDisplayUi();
        KotlinFunctionsKt.ifElse(z, new NightDisplaySettingsFragment$changeStatus$1(this), new NightDisplaySettingsFragment$changeStatus$2(this));
    }

    public void onResume() {
        super.onResume();
        updateNightDisplayUi();
        updateTexts();
    }

    private final void loadColorTemperatures() {
        this.mColorTemperatureMax = TwilightAccess.getMaxColorTemperature();
        this.mColorTemperatureMin = TwilightAccess.getMinColorTemperature();
    }

    /* access modifiers changed from: private */
    public final void setupNightDisplayUi() {
        Snackbar snackbar;
        setupStartTime();
        setupEndTime();
        setupFilterType();
        View findViewById = getActivity().findViewById(16908290);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "activity.findViewById(android.R.id.content)");
        try {
            snackbar = Snackbar.make(findViewById, (int) C0504R.string.preview, -2);
        } catch (IllegalArgumentException e) {
            LOGGER.mo11960e("Error making snackbar", e);
            snackbar = null;
        }
        this.mPreviewSnackbar = snackbar;
        ((TextView) _$_findCachedViewById(C0504R.C0506id.fallbackInfo)).setText(C0504R.string.try_it_now_tutorial);
        updateNightDisplayUi();
        loadSettingsDropdown(getEnabledStatus() == SettingStatus.ENABLED);
    }

    private final void setupFilterType() {
        View _$_findCachedViewById = _$_findCachedViewById(C0504R.C0506id.componentIntensityFilter);
        if (_$_findCachedViewById != null) {
            SeekBar seekBar = (SeekBar) _$_findCachedViewById.findViewById(C0504R.C0506id.seekbarIntensity);
            if (seekBar != null) {
                seekBar.setMax(20);
            }
        }
        View _$_findCachedViewById2 = _$_findCachedViewById(C0504R.C0506id.componentIntensityFilter);
        if (_$_findCachedViewById2 != null) {
            SeekBar seekBar2 = (SeekBar) _$_findCachedViewById2.findViewById(C0504R.C0506id.seekbarIntensity);
            if (seekBar2 != null) {
                seekBar2.setProgress(getProgress(TwilightAccess.getNightDisplayIntensity()));
            }
        }
        View _$_findCachedViewById3 = _$_findCachedViewById(C0504R.C0506id.componentIntensityFilter);
        if (_$_findCachedViewById3 != null) {
            SeekBar seekBar3 = (SeekBar) _$_findCachedViewById3.findViewById(C0504R.C0506id.seekbarIntensity);
            if (seekBar3 != null) {
                seekBar3.setOnSeekBarChangeListener(new NightDisplaySettingsFragment$setupFilterType$1(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public final void showNightDisplayPreview(int i) {
        Intent intent = new Intent(NightDisplayUpdateReceiver.ACTION_NIGHT_DISPLAY_PREVIEW);
        intent.putExtra(NightDisplayUpdateReceiver.EXTRA_INTENSITY, getIntensity(i));
        LocalBroadcastManager.getInstance(ActionsApplication.getAppContext()).sendBroadcast(intent);
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [kotlin.jvm.functions.Function1<android.view.View, kotlin.Unit>, kotlin.jvm.functions.Function1] */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r4v0, types: [com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$i$android_view_View_OnClickListener$0] */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void setupStartTime() {
        /*
            r5 = this;
            int r0 = com.motorola.actions.C0504R.C0506id.componentStart
            android.view.View r0 = r5._$_findCachedViewById(r0)
            if (r0 == 0) goto L_0x0062
            int r1 = com.motorola.actions.C0504R.C0506id.nightDisplayIcon
            android.view.View r1 = r0.findViewById(r1)
            android.widget.ImageView r1 = (android.widget.ImageView) r1
            if (r1 == 0) goto L_0x0018
            r2 = 2131230926(0x7f0800ce, float:1.8077919E38)
            r1.setImageResource(r2)
        L_0x0018:
            int r1 = com.motorola.actions.C0504R.C0506id.textPeriod
            android.view.View r1 = r0.findViewById(r1)
            android.widget.TextView r1 = (android.widget.TextView) r1
            if (r1 == 0) goto L_0x0028
            r2 = 2131624259(0x7f0e0143, float:1.8875693E38)
            r1.setText(r2)
        L_0x0028:
            int r1 = com.motorola.actions.C0504R.C0506id.textCustom
            android.view.View r1 = r0.findViewById(r1)
            r2 = 0
            if (r1 == 0) goto L_0x003b
            r3 = 2131296499(0x7f0900f3, float:1.8210916E38)
            android.view.View r1 = r1.findViewById(r3)
            android.widget.TextView r1 = (android.widget.TextView) r1
            goto L_0x003c
        L_0x003b:
            r1 = r2
        L_0x003c:
            r5.mTextStartCustom = r1
            android.widget.TextView r1 = r5.mTextStartCustom
            if (r1 == 0) goto L_0x0051
            kotlin.jvm.functions.Function1<android.view.View, kotlin.Unit> r3 = r5.startClickListener
            if (r3 == 0) goto L_0x004c
            com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$i$android_view_View_OnClickListener$0 r4 = new com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$i$android_view_View_OnClickListener$0
            r4.<init>(r3)
            r3 = r4
        L_0x004c:
            android.view.View$OnClickListener r3 = (android.view.View.OnClickListener) r3
            r1.setOnClickListener(r3)
        L_0x0051:
            int r1 = com.motorola.actions.C0504R.C0506id.textCustom
            android.view.View r0 = r0.findViewById(r1)
            if (r0 == 0) goto L_0x0060
            r1 = 2131296498(0x7f0900f2, float:1.8210914E38)
            android.view.View r2 = r0.findViewById(r1)
        L_0x0060:
            r5.mFallbackIndicatorStart = r2
        L_0x0062:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.p013ui.settings.NightDisplaySettingsFragment.setupStartTime():void");
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [kotlin.jvm.functions.Function1<android.view.View, kotlin.Unit>, kotlin.jvm.functions.Function1] */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r4v0, types: [com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$i$android_view_View_OnClickListener$0] */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void setupEndTime() {
        /*
            r5 = this;
            int r0 = com.motorola.actions.C0504R.C0506id.componentEnd
            android.view.View r0 = r5._$_findCachedViewById(r0)
            if (r0 == 0) goto L_0x0062
            int r1 = com.motorola.actions.C0504R.C0506id.nightDisplayIcon
            android.view.View r1 = r0.findViewById(r1)
            android.widget.ImageView r1 = (android.widget.ImageView) r1
            if (r1 == 0) goto L_0x0018
            r2 = 2131230950(0x7f0800e6, float:1.8077967E38)
            r1.setImageResource(r2)
        L_0x0018:
            int r1 = com.motorola.actions.C0504R.C0506id.textPeriod
            android.view.View r1 = r0.findViewById(r1)
            android.widget.TextView r1 = (android.widget.TextView) r1
            if (r1 == 0) goto L_0x0028
            r2 = 2131624251(0x7f0e013b, float:1.8875676E38)
            r1.setText(r2)
        L_0x0028:
            int r1 = com.motorola.actions.C0504R.C0506id.textCustom
            android.view.View r1 = r0.findViewById(r1)
            r2 = 0
            if (r1 == 0) goto L_0x003b
            r3 = 2131296499(0x7f0900f3, float:1.8210916E38)
            android.view.View r1 = r1.findViewById(r3)
            android.widget.TextView r1 = (android.widget.TextView) r1
            goto L_0x003c
        L_0x003b:
            r1 = r2
        L_0x003c:
            r5.mTextEndCustom = r1
            android.widget.TextView r1 = r5.mTextEndCustom
            if (r1 == 0) goto L_0x0051
            kotlin.jvm.functions.Function1<android.view.View, kotlin.Unit> r3 = r5.endClickListener
            if (r3 == 0) goto L_0x004c
            com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$i$android_view_View_OnClickListener$0 r4 = new com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$i$android_view_View_OnClickListener$0
            r4.<init>(r3)
            r3 = r4
        L_0x004c:
            android.view.View$OnClickListener r3 = (android.view.View.OnClickListener) r3
            r1.setOnClickListener(r3)
        L_0x0051:
            int r1 = com.motorola.actions.C0504R.C0506id.textCustom
            android.view.View r0 = r0.findViewById(r1)
            if (r0 == 0) goto L_0x0060
            r1 = 2131296498(0x7f0900f2, float:1.8210914E38)
            android.view.View r2 = r0.findViewById(r1)
        L_0x0060:
            r5.mFallbackIndicatorEnd = r2
        L_0x0062:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.p013ui.settings.NightDisplaySettingsFragment.setupEndTime():void");
    }

    private final void updateNightDisplayUi() {
        boolean isFeatureEnabled = Persistence.isFeatureEnabled();
        if (isFeatureEnabled) {
            this.mVideoListener.stopVideo();
        } else {
            this.mVideoListener.startVideo();
        }
        View _$_findCachedViewById = _$_findCachedViewById(C0504R.C0506id.componentStart);
        float f = 0.5f;
        if (_$_findCachedViewById != null) {
            View findViewById = _$_findCachedViewById.findViewById(C0504R.C0506id.textCustom);
            if (findViewById != null) {
                findViewById.setAlpha(isFeatureEnabled ? 1.0f : 0.5f);
            }
        }
        View _$_findCachedViewById2 = _$_findCachedViewById(C0504R.C0506id.componentEnd);
        if (_$_findCachedViewById2 != null) {
            View findViewById2 = _$_findCachedViewById2.findViewById(C0504R.C0506id.textCustom);
            if (findViewById2 != null) {
                if (isFeatureEnabled) {
                    f = 1.0f;
                }
                findViewById2.setAlpha(f);
            }
        }
        updateStatus(isFeatureEnabled);
        updateVisibility(isFeatureEnabled);
        updateOptions(isFeatureEnabled);
    }

    private final void updateStatus(boolean z) {
        View _$_findCachedViewById = _$_findCachedViewById(C0504R.C0506id.componentStart);
        if (_$_findCachedViewById != null) {
            View findViewById = _$_findCachedViewById.findViewById(C0504R.C0506id.textCustom);
            if (findViewById != null) {
                findViewById.setEnabled(z);
            }
        }
        View _$_findCachedViewById2 = _$_findCachedViewById(C0504R.C0506id.componentEnd);
        if (_$_findCachedViewById2 != null) {
            View findViewById2 = _$_findCachedViewById2.findViewById(C0504R.C0506id.textCustom);
            if (findViewById2 != null) {
                findViewById2.setEnabled(z);
            }
        }
        DropDownLayout dropDownLayout = this.mDropdownContainer;
        if (dropDownLayout != null) {
            dropDownLayout.setEnabled(z);
        }
        TextView textView = this.mTextStartCustom;
        if (textView != null) {
            textView.setEnabled(z);
        }
        TextView textView2 = this.mTextEndCustom;
        if (textView2 != null) {
            textView2.setEnabled(z);
        }
        View _$_findCachedViewById3 = _$_findCachedViewById(C0504R.C0506id.componentIntensityFilter);
        if (_$_findCachedViewById3 != null) {
            SeekBar seekBar = (SeekBar) _$_findCachedViewById3.findViewById(C0504R.C0506id.seekbarIntensity);
            if (seekBar != null) {
                seekBar.setEnabled(z);
            }
        }
    }

    private final void updateVisibility(boolean z) {
        if (z) {
            View _$_findCachedViewById = _$_findCachedViewById(C0504R.C0506id.componentStart);
            if (_$_findCachedViewById != null) {
                _$_findCachedViewById.setVisibility(0);
            }
            View _$_findCachedViewById2 = _$_findCachedViewById(C0504R.C0506id.componentEnd);
            if (_$_findCachedViewById2 != null) {
                _$_findCachedViewById2.setVisibility(0);
            }
            DropDownLayout dropDownLayout = this.mDropdownContainer;
            if (dropDownLayout != null) {
                dropDownLayout.setVisibility(0);
            }
            View _$_findCachedViewById3 = _$_findCachedViewById(C0504R.C0506id.videoContainer);
            if (_$_findCachedViewById3 != null) {
                _$_findCachedViewById3.setVisibility(8);
            }
            TextView textView = (TextView) _$_findCachedViewById(C0504R.C0506id.fallbackInfo);
            if (textView != null) {
                textView.setVisibility(0);
            }
            View _$_findCachedViewById4 = _$_findCachedViewById(C0504R.C0506id.componentIntensityFilter);
            if (_$_findCachedViewById4 != null) {
                _$_findCachedViewById4.setVisibility(0);
                return;
            }
            return;
        }
        View _$_findCachedViewById5 = _$_findCachedViewById(C0504R.C0506id.componentStart);
        if (_$_findCachedViewById5 != null) {
            _$_findCachedViewById5.setVisibility(8);
        }
        View _$_findCachedViewById6 = _$_findCachedViewById(C0504R.C0506id.componentEnd);
        if (_$_findCachedViewById6 != null) {
            _$_findCachedViewById6.setVisibility(8);
        }
        DropDownLayout dropDownLayout2 = this.mDropdownContainer;
        if (dropDownLayout2 != null) {
            dropDownLayout2.setVisibility(8);
        }
        View _$_findCachedViewById7 = _$_findCachedViewById(C0504R.C0506id.videoContainer);
        if (_$_findCachedViewById7 != null) {
            _$_findCachedViewById7.setVisibility(0);
        }
        TextView textView2 = (TextView) _$_findCachedViewById(C0504R.C0506id.fallbackInfo);
        if (textView2 != null) {
            textView2.setVisibility(8);
        }
        View _$_findCachedViewById8 = _$_findCachedViewById(C0504R.C0506id.componentIntensityFilter);
        if (_$_findCachedViewById8 != null) {
            _$_findCachedViewById8.setVisibility(8);
        }
    }

    private final void updateTexts() {
        updateStartText();
        updateEndText();
    }

    /* JADX WARNING: type inference failed for: r1v2, types: [kotlin.jvm.functions.Function1] */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r2v1, types: [com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$android_view_View_OnClickListener$0] */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r1v7, types: [kotlin.jvm.functions.Function1<android.view.View, kotlin.Unit>] */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void updateStartText() {
        /*
            r5 = this;
            int r0 = com.motorola.actions.sleepPattern.SleepPatternService.getDefaultState()
            int r0 = com.motorola.actions.nightdisplay.common.Persistence.getMode(r0)
            java.util.Calendar r1 = java.util.Calendar.getInstance()
            java.lang.String r1 = com.motorola.actions.p013ui.settings.NightDisplayTextGetter.getStartText(r1, r0)
            r5.updateFallbackStatus(r0)
            boolean r2 = r5.mIsFallback
            r3 = 0
            r4 = 1
            if (r2 != 0) goto L_0x001d
            if (r0 != r4) goto L_0x001c
            goto L_0x001d
        L_0x001c:
            r4 = r3
        L_0x001d:
            android.view.View r0 = r5.mFallbackIndicatorStart
            if (r0 == 0) goto L_0x002b
            boolean r2 = r5.mIsFallback
            if (r2 == 0) goto L_0x0026
            goto L_0x0028
        L_0x0026:
            r3 = 8
        L_0x0028:
            r0.setVisibility(r3)
        L_0x002b:
            android.widget.TextView r0 = r5.mTextStartCustom
            if (r0 == 0) goto L_0x0042
            if (r4 == 0) goto L_0x0036
            int r2 = r5.getTimeTextColor()
            goto L_0x0039
        L_0x0036:
            r2 = 17170433(0x1060001, float:2.4611916E-38)
        L_0x0039:
            android.text.Spanned r1 = com.motorola.actions.p013ui.settings.NightDisplayTextGetter.getCustomTimeSpannedText(r1, r2)
            java.lang.CharSequence r1 = (java.lang.CharSequence) r1
            r0.setText(r1)
        L_0x0042:
            android.widget.TextView r0 = r5.mTextStartCustom
            if (r0 == 0) goto L_0x0059
            if (r4 == 0) goto L_0x004b
            kotlin.jvm.functions.Function1<android.view.View, kotlin.Unit> r1 = r5.startClickListener
            goto L_0x004c
        L_0x004b:
            r1 = 0
        L_0x004c:
            if (r1 == 0) goto L_0x0054
            com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$android_view_View_OnClickListener$0 r2 = new com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$android_view_View_OnClickListener$0
            r2.<init>(r1)
            r1 = r2
        L_0x0054:
            android.view.View$OnClickListener r1 = (android.view.View.OnClickListener) r1
            r0.setOnClickListener(r1)
        L_0x0059:
            r5.updateFallbackText()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.p013ui.settings.NightDisplaySettingsFragment.updateStartText():void");
    }

    /* JADX WARNING: type inference failed for: r1v2, types: [kotlin.jvm.functions.Function1] */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r2v1, types: [com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$android_view_View_OnClickListener$0] */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r1v7, types: [kotlin.jvm.functions.Function1<android.view.View, kotlin.Unit>] */
    /* JADX WARNING: type inference failed for: r1v10 */
    /* JADX WARNING: type inference failed for: r1v11 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void updateEndText() {
        /*
            r5 = this;
            int r0 = com.motorola.actions.sleepPattern.SleepPatternService.getDefaultState()
            int r0 = com.motorola.actions.nightdisplay.common.Persistence.getMode(r0)
            java.util.Calendar r1 = java.util.Calendar.getInstance()
            java.lang.String r1 = com.motorola.actions.p013ui.settings.NightDisplayTextGetter.getEndText(r1, r0)
            r5.updateFallbackStatus(r0)
            boolean r2 = r5.mIsFallback
            r3 = 0
            r4 = 1
            if (r2 != 0) goto L_0x001d
            if (r0 != r4) goto L_0x001c
            goto L_0x001d
        L_0x001c:
            r4 = r3
        L_0x001d:
            android.view.View r0 = r5.mFallbackIndicatorEnd
            if (r0 == 0) goto L_0x002b
            boolean r2 = r5.mIsFallback
            if (r2 == 0) goto L_0x0026
            goto L_0x0028
        L_0x0026:
            r3 = 8
        L_0x0028:
            r0.setVisibility(r3)
        L_0x002b:
            android.widget.TextView r0 = r5.mTextEndCustom
            if (r0 == 0) goto L_0x0042
            if (r4 == 0) goto L_0x0036
            int r2 = r5.getTimeTextColor()
            goto L_0x0039
        L_0x0036:
            r2 = 17170433(0x1060001, float:2.4611916E-38)
        L_0x0039:
            android.text.Spanned r1 = com.motorola.actions.p013ui.settings.NightDisplayTextGetter.getCustomTimeSpannedText(r1, r2)
            java.lang.CharSequence r1 = (java.lang.CharSequence) r1
            r0.setText(r1)
        L_0x0042:
            android.widget.TextView r0 = r5.mTextEndCustom
            if (r0 == 0) goto L_0x0059
            if (r4 == 0) goto L_0x004b
            kotlin.jvm.functions.Function1<android.view.View, kotlin.Unit> r1 = r5.endClickListener
            goto L_0x004c
        L_0x004b:
            r1 = 0
        L_0x004c:
            if (r1 == 0) goto L_0x0054
            com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$android_view_View_OnClickListener$0 r2 = new com.motorola.actions.ui.settings.NightDisplaySettingsFragment$sam$android_view_View_OnClickListener$0
            r2.<init>(r1)
            r1 = r2
        L_0x0054:
            android.view.View$OnClickListener r1 = (android.view.View.OnClickListener) r1
            r0.setOnClickListener(r1)
        L_0x0059:
            r5.updateFallbackText()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.p013ui.settings.NightDisplaySettingsFragment.updateEndText():void");
    }

    private final int getTimeTextColor() {
        return Device.isNewMoto() ? C0504R.color.wave : C0504R.color.moto_primary_color_light;
    }

    private final void updateFallbackStatus(int i) {
        this.mIsFallback = false;
        if (i == 4) {
            this.mIsFallback = !SleepPatternAccess.isReady();
        }
    }

    /* access modifiers changed from: private */
    public final void openTimePicker(boolean z, int i) {
        int hours = (int) TimeUnit.MINUTES.toHours((long) i);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new NightDisplaySettingsFragment$openTimePicker$timePickerDialog$1(this, z), hours, i - ((int) TimeUnit.HOURS.toMinutes((long) hours)), DateFormat.is24HourFormat(ActionsApplication.getAppContext()));
        timePickerDialog.show();
    }

    /* access modifiers changed from: private */
    public final void onTimeSet(boolean z, int i, int i2) {
        int minutes = ((int) TimeUnit.HOURS.toMinutes((long) i)) + i2;
        stopPreviewMode();
        if (z) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Set custom initial time: ");
            sb.append(minutes);
            mALogger.mo11957d(sb.toString());
            Persistence.saveInitialTimeInMinutes(minutes);
            updateStartText();
            return;
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Set custom termination time: ");
        sb2.append(minutes);
        mALogger2.mo11957d(sb2.toString());
        Persistence.saveTerminationTimeInMinutes(minutes);
        updateEndText();
    }

    /* access modifiers changed from: private */
    public final int getIntensity(int i) {
        float f = ((float) i) / ((float) 20);
        int i2 = this.mColorTemperatureMax - ((int) (((float) (this.mColorTemperatureMax - this.mColorTemperatureMin)) * f));
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getIntensity: ");
        sb.append(i2);
        sb.append(", progress: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        return i2;
    }

    private final int getProgress(int i) {
        int i2 = (int) ((((float) (this.mColorTemperatureMax - i)) / ((float) (this.mColorTemperatureMax - this.mColorTemperatureMin))) * ((float) 20));
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("getProgress: ");
        sb.append(i2);
        sb.append(", intensity: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        return i2;
    }

    /* access modifiers changed from: private */
    public final void stopPreviewMode() {
        if (this.mIsPreviewMode) {
            LOGGER.mo11957d("stopping preview");
            this.mIsPreviewMode = false;
            this.mHandler.removeCallbacks(this.mEndPreviewRunnable);
            Snackbar snackbar = this.mPreviewSnackbar;
            if (snackbar != null) {
                snackbar.dismiss();
            }
            Intent intent = new Intent(NightDisplayUpdateReceiver.ACTION_NIGHT_DISPLAY_PREVIEW);
            intent.putExtra(NightDisplayUpdateReceiver.EXTRA_INTENSITY, 0);
            LocalBroadcastManager.getInstance(ActionsApplication.getAppContext()).sendBroadcast(intent);
        }
    }

    private final void updateFallbackText() {
        TextView textView = (TextView) _$_findCachedViewById(C0504R.C0506id.fallbackInfo);
        if (textView != null) {
            textView.setText(NightDisplayTextGetter.getFallbackText(this.mIsFallback));
        }
    }

    private final void loadSettingsDropdown(boolean z) {
        int[][] iArr;
        int mode = Persistence.getMode(SleepPatternService.getDefaultState());
        if (this.mDropdownContainer != null) {
            this.mDropdownContainer.setActionListener(this);
            this.mDropdownContainer.setInteractionListener(this);
            for (int[] iArr2 : DROPDOWN_OPTIONS) {
                int i = iArr2[0];
                boolean z2 = true;
                String string = getResources().getString(iArr2[1]);
                String string2 = getResources().getString(iArr2[2]);
                if (iArr2[0] != mode) {
                    z2 = false;
                }
                this.mDropdownContainer.addSettingsOption(new DropDownSetting(i, string, string2, z2));
            }
            DropDownLayout dropDownLayout = this.mDropdownContainer;
            Intrinsics.checkExpressionValueIsNotNull(dropDownLayout, "mDropdownContainer");
            dropDownLayout.setVisibility(0);
            this.mDropdownContainer.setSelectedSettings(mode);
            dropDownItemSelected(mode);
            updateOptions(z);
        }
    }

    private final void updateOptions(boolean z) {
        if (z) {
            DropDownLayout dropDownLayout = this.mDropdownContainer;
            if (dropDownLayout != null) {
                dropDownLayout.setEnabled();
                return;
            }
            return;
        }
        DropDownLayout dropDownLayout2 = this.mDropdownContainer;
        if (dropDownLayout2 != null) {
            dropDownLayout2.setDisabled();
        }
    }

    public void dropDownItemSelected(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Set mode to: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        Persistence.saveMode(i);
        stopPreviewMode();
        updateNightDisplayUi();
        updateTexts();
    }

    static {
        int[][] iArr;
        if (SleepPatternService.isFeatureSupported()) {
            iArr = new int[][]{new int[]{4, C0504R.string.night_display_automatic_title, C0504R.string.night_display_automatic_description}, new int[]{1, C0504R.string.night_display_manual_title, C0504R.string.night_display_manual_description}};
        } else {
            iArr = new int[][]{new int[]{1, C0504R.string.night_display_manual_title, C0504R.string.night_display_manual_description}};
        }
        DROPDOWN_OPTIONS = iArr;
    }
}
