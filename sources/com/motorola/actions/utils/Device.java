package com.motorola.actions.utils;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.text.TextUtils;
import com.motorola.actions.reflect.SystemPropertiesProxy;
import java.util.Locale;

public class Device {
    private static final String FPS_POSITION_BACK = "1";
    private static final String FPS_POSITION_SIDE = "2";
    private static final String FPS_SENSOR_POSITION = "ro.fpsensor.position";
    private static final MALogger LOGGER = new MALogger(Device.class);
    private static final String PRODUCT_NAME_ADDISON = "addison";
    private static final String PRODUCT_NAME_ALBUS = "albus";
    private static final String PRODUCT_NAME_ALI = "ali";
    private static final String PRODUCT_NAME_ATHENE = "athene";
    private static final String PRODUCT_NAME_BECKHAM = "beckham";
    private static final String PRODUCT_NAME_CEDRIC = "cedric";
    private static final String PRODUCT_NAME_CHANNEL = "channel";
    private static final String PRODUCT_NAME_EVERT = "evert";
    private static final String PRODUCT_NAME_FOLES = "foles";
    private static final String PRODUCT_NAME_GOLF_LITE_GO = "pettyl";
    private static final String PRODUCT_NAME_GRIFFIN = "griffin";
    private static final String PRODUCT_NAME_HARPIA = "harpia";
    private static final String PRODUCT_NAME_KANE = "kane";
    private static final String PRODUCT_NAME_LAKE = "lake";
    private static final String PRODUCT_NAME_MESSI = "messi";
    private static final String PRODUCT_NAME_MONTANA = "montana";
    private static final String PRODUCT_NAME_NASH = "nash";
    private static final String PRODUCT_NAME_NICKLAUS = "nicklaus";
    private static final String PRODUCT_NAME_OCEAN = "ocean";
    private static final String PRODUCT_NAME_OLSON = "olson";
    private static final String PRODUCT_NAME_OWENS = "owens";
    private static final String PRODUCT_NAME_PAYTON = "payton";
    private static final String PRODUCT_NAME_PAYTON_FI = "payton_fi";
    private static final String PRODUCT_NAME_PERRY = "perry";
    private static final String PRODUCT_NAME_POTTER = "potter";
    private static final String PRODUCT_NAME_RIVER = "river";
    private static final String PRODUCT_NAME_ROBUSTA_NOTE = "chef";
    private static final String PRODUCT_NAME_ROBUSTA_S = "deen";
    private static final String PRODUCT_NAME_SANDERS = "sanders";
    private static final String PRODUCT_NAME_SPERRY = "sperry";
    private static final String PRODUCT_NAME_WOODS = "woods";
    private static final String RO_ANDROID_ONE = "ro.mot.android_one";
    private static final String RO_BUILD_VARS = "ro.boot.build_vars";
    private static final String RO_PRODUCT_NAME = "ro.product.name";
    private static final String UTAG_WHITE = "WHITE";

    public static boolean isVectorDevice() {
        return containsProductName(PRODUCT_NAME_GRIFFIN);
    }

    public static boolean isAffinityDevice() {
        return containsProductName(PRODUCT_NAME_ATHENE);
    }

    public static boolean isJudoDevice() {
        return containsProductName(PRODUCT_NAME_HARPIA);
    }

    public static boolean isVertexDevice() {
        return containsProductName(PRODUCT_NAME_ADDISON);
    }

    public static boolean isDanteDevice() {
        return containsProductName(PRODUCT_NAME_CEDRIC);
    }

    public static boolean isCopperfieldDevice() {
        return containsProductName(PRODUCT_NAME_POTTER);
    }

    public static boolean isAndyDevice() {
        return containsProductName(PRODUCT_NAME_WOODS) || isAndyNADevice();
    }

    public static boolean isAndyNADevice() {
        return containsProductName(PRODUCT_NAME_PERRY) || containsProductName(PRODUCT_NAME_SPERRY);
    }

    public static boolean isGeorgeDevice() {
        return containsProductName(PRODUCT_NAME_NICKLAUS) || containsProductName(PRODUCT_NAME_OWENS);
    }

    public static boolean isAndroidOne() {
        return propertyContains(RO_ANDROID_ONE, "true");
    }

    public static boolean hasBackFPSSensor() {
        return propertyContains(FPS_SENSOR_POSITION, FPS_POSITION_BACK);
    }

    public static boolean hasSideFPSSensor() {
        return propertyContains(FPS_SENSOR_POSITION, FPS_POSITION_SIDE);
    }

    public static boolean isIbisDevice() {
        return containsProductName(PRODUCT_NAME_PAYTON);
    }

    public static boolean isIbisFiDevice() {
        return containsProductName(PRODUCT_NAME_PAYTON_FI);
    }

    public static boolean isHoudiniDevice() {
        return containsProductName(PRODUCT_NAME_ALBUS);
    }

    public static boolean isGoldenEagleDevice() {
        return containsProductName(PRODUCT_NAME_NASH);
    }

    public static boolean isD52Device() {
        return containsProductName(PRODUCT_NAME_MONTANA);
    }

    public static boolean isZachDevice() {
        return containsProductName(PRODUCT_NAME_SANDERS);
    }

    public static boolean isShelbyDevice() {
        return containsProductName(PRODUCT_NAME_BECKHAM);
    }

    public static boolean isRobustaSDevice() {
        return containsProductName(PRODUCT_NAME_ROBUSTA_S);
    }

    public static boolean isRobustaNoteDevice() {
        return containsProductName(PRODUCT_NAME_ROBUSTA_NOTE);
    }

    public static boolean isRobusta2Device() {
        return containsProductName(PRODUCT_NAME_KANE);
    }

    public static boolean isGolfLiteGoDevice() {
        return containsProductName(PRODUCT_NAME_GOLF_LITE_GO);
    }

    public static boolean isShelbyPlusDevice() {
        return containsProductName(PRODUCT_NAME_MESSI);
    }

    public static boolean isRioDevice() {
        return containsProductName(PRODUCT_NAME_RIVER) || containsProductName(PRODUCT_NAME_LAKE);
    }

    public static boolean isOsloDevice() {
        return containsProductName(PRODUCT_NAME_OCEAN) || containsProductName(PRODUCT_NAME_CHANNEL);
    }

    public static boolean isTellerDevice() {
        return containsProductName(PRODUCT_NAME_EVERT);
    }

    public static boolean isBlaineDevice() {
        return containsProductName(PRODUCT_NAME_ALI);
    }

    public static boolean isStingrayDevice() {
        return containsProductName(PRODUCT_NAME_FOLES);
    }

    public static boolean isVoyagerDevice() {
        return containsProductName(PRODUCT_NAME_OLSON);
    }

    private static boolean containsProductName(String str) {
        String str2 = SystemPropertiesProxy.get(RO_PRODUCT_NAME);
        if (str2 != null) {
            return str2.toLowerCase(Locale.US).startsWith(str);
        }
        return false;
    }

    private static boolean propertyContains(String str, String str2) {
        String str3 = SystemPropertiesProxy.get(str);
        if (str3 != null) {
            return str3.toLowerCase(Locale.US).startsWith(str2);
        }
        return false;
    }

    public static boolean isWhiteDevice() {
        String str = SystemPropertiesProxy.get(RO_BUILD_VARS);
        boolean z = false;
        if (str == null) {
            return false;
        }
        if (str.toUpperCase(Locale.US).indexOf(UTAG_WHITE) >= 0) {
            z = true;
        }
        return z;
    }

    public static boolean hasFingerprintSensor(Context context) {
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService("fingerprint");
        if (fingerprintManager != null) {
            try {
                return fingerprintManager.isHardwareDetected();
            } catch (SecurityException e) {
                LOGGER.mo11960e("Application does not have USE_FINGERPRINT permission", e);
            }
        } else {
            LOGGER.mo11959e("FingerprintService unavailable");
            return false;
        }
    }

    public static boolean isZuiDevice() {
        boolean z = !TextUtils.isEmpty(SystemPropertiesProxy.get("ro.com.zui.version"));
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("isZuiDevice: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }

    public static boolean is2017UIDevice() {
        return isGoldenEagleDevice() || isHoudiniDevice() || isIbisDevice() || isZachDevice() || isD52Device() || isAndyDevice() || isGeorgeDevice() || isCopperfieldDevice() || isDanteDevice();
    }

    public static boolean isNewMoto() {
        return isOsloDevice() || isRioDevice();
    }
}
