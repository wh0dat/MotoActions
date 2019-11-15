package com.motorola.actions.debug;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.ActionsApplicationContext;
import com.motorola.actions.C0504R;
import com.motorola.actions.dagger.components.DaggerAppComponent;
import com.motorola.actions.dagger.modules.AppModule;
import com.motorola.actions.dagger.modules.SleepPatternModule;
import com.motorola.actions.debug.items.ADSensorDebugItem;
import com.motorola.actions.debug.items.ChangeFDNDelayDebugItem;
import com.motorola.actions.debug.items.ClearADNotificationDataDebugItem;
import com.motorola.actions.debug.items.ClearDataDebugItem;
import com.motorola.actions.debug.items.DebugItem;
import com.motorola.actions.debug.items.DeleteADPhotoDebugItem;
import com.motorola.actions.debug.items.DeleteSleepPatternDatabase;
import com.motorola.actions.debug.items.ForceChopChopAlgorithmDebugItem;
import com.motorola.actions.debug.items.InstrumentationClearDataDebugItem;
import com.motorola.actions.debug.items.InstrumentationSendDataDebugItem;
import com.motorola.actions.debug.items.ResetFDNShowNotificationsItem;
import com.motorola.actions.debug.items.ResetWelcomeActivitiesDebugItem;
import com.motorola.actions.debug.items.SaveADPhotoDebugItem;
import com.motorola.actions.debug.items.SendADPhotoDebugItem;
import com.motorola.actions.debug.items.SleepPatternSendDataDebugItem;
import com.motorola.actions.debug.items.SleepPatternToggleSourceDebugItem;
import com.motorola.actions.debug.items.StartLoggingToFileDebugItem;
import com.motorola.actions.debug.items.StopLoggingToFileDebugItem;
import com.motorola.actions.debug.items.TriggerAllFDNDebugItem;
import com.motorola.actions.foc.gesture.util.FlashOnChopAlgorithmUtil;
import com.motorola.actions.reflect.SystemPropertiesProxy;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import com.motorola.actions.utils.Constants;
import java.util.ArrayList;
import javax.inject.Inject;

public class DebugFragment extends Fragment {
    private static final String ACTIONS_DEBUG_PROPERTY = "debug.actions.debugmenu";
    private static final long DELAY_REFRESH_LIST_MS = 250;
    private DebugAdapter mAdapter;
    @Inject
    SleepPatternRepository mSleepPatternRepository;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ActionsApplication application = ActionsApplicationContext.getInstance().getApplication();
        DaggerAppComponent.builder().appModule(new AppModule(application)).sleepPatternModule(new SleepPatternModule(application)).build().inject(this);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new InstrumentationSendDataDebugItem());
        arrayList.add(new InstrumentationClearDataDebugItem());
        arrayList.add(new ResetFDNShowNotificationsItem());
        arrayList.add(new ChangeFDNDelayDebugItem());
        arrayList.add(new ResetWelcomeActivitiesDebugItem());
        arrayList.add(new ForceChopChopAlgorithmDebugItem(FlashOnChopAlgorithmUtil.KEY_TEST_USE_ONLY_FOC_ALGO_CAMERA_MANAGER, C0504R.string.debug_force_camera_manager_title, C0504R.string.debug_force_camera_manager_description, C0504R.string.debug_force_camera_manager_toast));
        arrayList.add(new ForceChopChopAlgorithmDebugItem(FlashOnChopAlgorithmUtil.KEY_TEST_USE_ONLY_FOC_ALGO_FLASHLIGHTCONTROLLER, C0504R.string.debug_force_flashlight_controller_title, C0504R.string.debug_force_flashlight_controller_description, C0504R.string.debug_force_flashlight_controller_toast));
        arrayList.add(new ForceChopChopAlgorithmDebugItem(FlashOnChopAlgorithmUtil.KEY_TEST_USE_ONLY_FOC_ALGO_UNKNOWN, C0504R.string.debug_force_default_algo_title, C0504R.string.debug_force_default_algo_description, C0504R.string.debug_force_default_algo_toast));
        arrayList.add(new ADSensorDebugItem(1, C0504R.string.debug_force_gyro_title, C0504R.string.debug_force_gyro_description, C0504R.string.debug_force_gyro_toast));
        arrayList.add(new ADSensorDebugItem(0, C0504R.string.debug_force_accel_title, C0504R.string.debug_force_accel_description, C0504R.string.debug_force_accel_toast));
        arrayList.add(new StartLoggingToFileDebugItem());
        arrayList.add(new StopLoggingToFileDebugItem());
        arrayList.add(new DeleteSleepPatternDatabase());
        arrayList.add(new SleepPatternSendDataDebugItem(this.mSleepPatternRepository));
        arrayList.add(new SleepPatternToggleSourceDebugItem());
        arrayList.add(new SaveADPhotoDebugItem());
        arrayList.add(new SendADPhotoDebugItem());
        arrayList.add(new DeleteADPhotoDebugItem());
        arrayList.add(new TriggerAllFDNDebugItem());
        arrayList.add(new ClearDataDebugItem());
        arrayList.add(new ClearADNotificationDataDebugItem());
        ListView listView = (ListView) layoutInflater.inflate(C0504R.layout.debug_list, viewGroup, false);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        this.mAdapter = new DebugAdapter(getActivity(), C0504R.layout.debug_item, arrayList);
        listView.setAdapter(this.mAdapter);
        listView.setOnItemClickListener(new DebugFragment$$Lambda$0(this, arrayList));
        return listView;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onCreateView$0$DebugFragment(ArrayList arrayList, AdapterView adapterView, View view, int i, long j) {
        ((DebugItem) arrayList.get(i)).getOnClickListener().onClick(view);
        new Handler().postDelayed(new DebugFragment$$Lambda$1(this), DELAY_REFRESH_LIST_MS);
    }

    public static boolean shouldShowDebugMenu() {
        return SystemPropertiesProxy.getBoolean(ACTIONS_DEBUG_PROPERTY, false) && Constants.DEBUG;
    }

    public static boolean shouldShowDebugMenu(@Nullable Intent intent) {
        return intent != null && ACTIONS_DEBUG_PROPERTY.equals(intent.getAction());
    }

    /* access modifiers changed from: private */
    /* renamed from: updateData */
    public void bridge$lambda$0$DebugFragment() {
        if (this.mAdapter != null) {
            for (int i = 0; i < this.mAdapter.getCount(); i++) {
                if (this.mAdapter.getItem(i) != null) {
                    ((DebugItem) this.mAdapter.getItem(i)).updateData();
                }
            }
        }
        this.mAdapter.notifyDataSetChanged();
    }
}
