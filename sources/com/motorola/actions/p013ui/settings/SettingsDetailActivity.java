package com.motorola.actions.p013ui.settings;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.debug.DebugFragment;
import com.motorola.actions.onenav.OneNavHelper;
import com.motorola.actions.p013ui.tutorial.nightdisplay.NightDisplaySleepPatternTutorialActivity;
import com.motorola.actions.p013ui.tutorial.onenav.OneNavWelcomeActivity;
import com.motorola.actions.p013ui.tutorial.onenav.soft.SoftOneNavWelcomeActivity;
import com.motorola.actions.p013ui.tutorial.quickScreenshot.welcome.QuickScreenshotWelcomeActivity;
import com.motorola.actions.quickscreenshot.QuickScreenshotHelper;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.settings.SettingsDetailActivity */
public class SettingsDetailActivity extends BaseSettingsActivity {
    private static final String ACTION_MOTO_PRIVACY = "com.motorola.intent.action.MOTOROLA_FULL_PRIVACY_POLICY";
    public static final String EXTRA_SET_STATUS = "EXTRA_SET_STATUS";
    public static final String KEY_SETTINGS = "settings";
    private static final String LICENSE_DLG_TAG = "LicenseDialog";
    private static final MALogger LOGGER = new MALogger(SettingsDetailActivity.class);
    private static final String PRIVACY_EXTRA_TOPIC_ID = "topic_id";
    private static final String PRIVACY_ID_MOTO = "umoto";

    /* renamed from: com.motorola.actions.ui.settings.SettingsDetailActivity$LicenseDialog */
    public static class LicenseDialog extends DialogFragment {
        private static final String LICENCES_FILE = "file:///android_asset/licenses.html";

        public Dialog onCreateDialog(Bundle bundle) {
            WebView webView = new WebView(getActivity());
            webView.loadUrl(LICENCES_FILE);
            return new Builder(getActivity()).setTitle(getString(C0504R.string.licenses_title)).setView(webView).setPositiveButton(17039370, SettingsDetailActivity$LicenseDialog$$Lambda$0.$instance).create();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        LOGGER.mo11957d("onCreate");
        super.onCreate(bundle);
        setContentView((int) C0504R.layout.activity_settings);
        openFragment(getIntent().getIntExtra(KEY_SETTINGS, FeatureKey.NOT_VALID.ordinal()), getIntent());
    }

    private void openFragment(int i, Intent intent) {
        SettingsDetailFragment fragmentToOpen = getFragmentToOpen(i, FeatureKey.getFeatureKey(i), intent);
        executeFragmentTransaction(fragmentToOpen, intent);
        setupToolbar(fragmentToOpen != null ? fragmentToOpen.getTitleTextId() : -1);
    }

    private SettingsDetailFragment getFragmentToOpen(int i, FeatureKey featureKey, Intent intent) {
        switch (featureKey) {
            case APPROACH:
                return new ApproachSettingsFragment();
            case QUICK_CAPTURE:
                return new QuickDrawSettingsFragment();
            case FLASH_ON_CHOP:
                return new FlashOnChopSettingsFragment();
            case ATTENTIVE_DISPLAY:
                return new AttentiveDisplaySettingsFragment();
            case MICROSCREEN:
                return new MicroScreenSettingsFragment();
            case FLIP_TO_DND:
                return new FlipToMuteSettingsFragment();
            case PICKUP_TO_STOP_RINGING:
                return new LiftToSilenceSettingsFragment();
            case NIGHT_DISPLAY:
            case SLEEP_PATTERN:
                if (NightDisplaySleepPatternTutorialActivity.shouldOpenTutorial()) {
                    NightDisplaySleepPatternTutorialActivity.openActivity(this, intent.getExtras());
                    redirectPendingTransition();
                    break;
                } else {
                    return new NightDisplaySettingsFragment();
                }
            case ONE_NAV:
                if (OneNavHelper.shouldShowWelcomeScreen()) {
                    if (OneNavHelper.isSoftOneNav()) {
                        SoftOneNavWelcomeActivity.openWelcomeActivity(this, intent.getExtras());
                    } else {
                        OneNavWelcomeActivity.openWelcomeActivity(this, intent.getExtras());
                    }
                    redirectPendingTransition();
                    break;
                } else {
                    return new OneNavSettingsFragment();
                }
            case QUICK_SCREENSHOT:
                if (QuickScreenshotHelper.shouldShowWelcomeScreen()) {
                    QuickScreenshotWelcomeActivity.openWelcomeActivity(this, intent.getExtras());
                    redirectPendingTransition();
                    break;
                } else {
                    return new QuickScreenshotSettingsFragment();
                }
            case ENHANCED_SCREENSHOT:
                return new EnhancedScreenshotSettingsFragment();
            case MEDIA_CONTROL:
                return new MediaControlSettingsFragment();
            case FPS_SLIDE_HOME:
                return new FPSSlideHomeSettingsFragment();
            case FPS_SLIDE_APP:
                return new FPSSlideAppSettingsFragment();
            case LIFT_TO_UNLOCK:
                return new LiftToUnlockSettingsFragment();
            default:
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("Tried to launch a settings fragment that does not exist: ");
                sb.append(i);
                sb.append(", settingKey: ");
                sb.append(featureKey);
                mALogger.mo11959e(sb.toString());
                break;
        }
        return null;
    }

    private void redirectPendingTransition() {
        finish();
        overridePendingTransition(C0504R.anim.splash_slide_in_anim_set, C0504R.anim.splash_slide_out_anim_set);
    }

    private void executeFragmentTransaction(SettingsDetailFragment settingsDetailFragment, Intent intent) {
        if (settingsDetailFragment != null) {
            getFragmentManager().beginTransaction().replace(C0504R.C0506id.layout_menu, settingsDetailFragment).commit();
            if (intent != null && intent.hasExtra(EXTRA_SET_STATUS)) {
                boolean booleanExtra = intent.getBooleanExtra(EXTRA_SET_STATUS, false);
                intent.removeExtra(EXTRA_SET_STATUS);
                settingsDetailFragment.setUpdateStatusOnStart(true);
                settingsDetailFragment.setStatusToBeSet(booleanExtra);
            }
        } else if (!Constants.PRODUCTION_MODE && DebugFragment.shouldShowDebugMenu(intent)) {
            getFragmentManager().beginTransaction().replace(C0504R.C0506id.layout_menu, new DebugFragment()).commit();
        }
    }

    public boolean onNavigateUp() {
        finish();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        openFragment(intent.getIntExtra(KEY_SETTINGS, FeatureKey.NOT_VALID.ordinal()), intent);
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C0504R.anim.splash_slide_in_anim_reverse_set, C0504R.anim.splash_slide_out_anim_reverse_set);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0504R.C0507menu.menu, menu);
        MenuItem findItem = menu.findItem(C0504R.C0506id.debug_settings_menu_entry);
        if (DebugFragment.shouldShowDebugMenu() && findItem != null) {
            findItem.setVisible(true);
        }
        return true;
    }

    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem != null) {
            int itemId = menuItem.getItemId();
            if (itemId == C0504R.C0506id.license_menu_entry) {
                new LicenseDialog().show(getFragmentManager(), LICENSE_DLG_TAG);
                return true;
            } else if (itemId == C0504R.C0506id.privacy_menu_entry) {
                openPrivacyLink();
            } else if (itemId == C0504R.C0506id.debug_settings_menu_entry) {
                final FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().add(C0504R.C0506id.layout_menu, new DebugFragment()).addToBackStack(DebugFragment.class.getSimpleName()).commit();
                fragmentManager.addOnBackStackChangedListener(new OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        if (menuItem.isVisible()) {
                            menuItem.setVisible(false);
                            return;
                        }
                        menuItem.setVisible(true);
                        fragmentManager.removeOnBackStackChangedListener(this);
                    }
                });
                return true;
            }
        }
        return false;
    }

    private void openPrivacyLink() {
        try {
            Intent intent = new Intent(ACTION_MOTO_PRIVACY);
            intent.putExtra(PRIVACY_EXTRA_TOPIC_ID, PRIVACY_ID_MOTO);
            intent.setFlags(67108864);
            startActivity(intent);
            overridePendingTransition(C0504R.anim.splash_slide_in_anim_set, C0504R.anim.splash_slide_out_anim_set);
        } catch (ActivityNotFoundException unused) {
            LOGGER.mo11959e("Moto privacy app is not installed");
        }
    }
}
