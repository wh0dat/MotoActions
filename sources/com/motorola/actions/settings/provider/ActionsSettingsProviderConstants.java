package com.motorola.actions.settings.provider;

import com.motorola.actions.settings.provider.contract.Column;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionsSettingsProviderConstants {
    public static final String AUTHORITY_NAME = "com.motorola.actions.settings.provider";
    static final String CALL_FACTORY_RESET = "factory_reset";
    public static final List<String> CONTAINER_COLUMNS = Collections.unmodifiableList(Arrays.asList(new String[]{Column.ENABLED, Column.TITLE, Column.DESCRIPTION, Column.ICON, Column.TYPE, Column.VALUE, Column.FAMILY_IMAGE, Column.FAMILY_HEADER_TEXT, Column.FAMILY_SUPPORT_TEXT, Column.DRAWER_FAMILY_ICON}));
    public static final String CONTENT_SCHEMA = "content://";
    public static final List<String> SETTINGS_COLUMNS_V2 = Collections.unmodifiableList(Arrays.asList(new String[]{Column.ENABLED, Column.TITLE, Column.DESCRIPTION, Column.ICON, Column.TYPE, Column.VALUE, Column.URI, Column.LINK_ACTION, Column.DISCOVERY_STATUS, Column.FEATURE_CARD_PRIORITY, Column.FEATURE_CARD_ICON, Column.DISCOVERY_ICON, Column.DISCOVERY_HEADER_TEXT, Column.DISCOVERY_SUPPORT_TEXT, Column.DISCOVERY_CTA_TEXT, Column.FEATURE_ICON}));
    public static final String URI_PREFIX = "content://com.motorola.actions.settings.provider";

    public enum TableConstants {
        ATTENTIVE_DISPLAY_ID,
        ACTIONS_CONTAINER_ID,
        ACTIONS_CONTAINER_ALL_SETTINGS,
        DISPLAY_CONTAINER_ALL_SETTINGS,
        ACTIONS_CONTAINER_FOC,
        ACTIONS_CONTAINER_FTM,
        ACTIONS_CONTAINER_QC,
        ACTIONS_CONTAINER_ONENAV,
        ACTIONS_CONTAINER_MICROSCREEN,
        ACTIONS_CONTAINER_LTS,
        ACTIONS_CONTAINER_APPROACH,
        ACTIONS_CONTAINER_SCREENSHOT,
        DISPLAY_CONTAINER_ATTENTIVE_DISPLAY,
        DISPLAY_CONTAINER_NIGHT_DISPLAY,
        ACTIONS_CONTAINER_ENHANCED_SCREENSHOT,
        ACTIONS_CONTAINER_MEDIA_CONTROL,
        ACTIONS_CONTAINER_FPS_SLIDE_HOME,
        ACTIONS_CONTAINER_FPS_SLIDE_APP,
        ACTIONS_CONTAINER_LIFT_TO_UNLOCK
    }
}
