package com.motorola.actions.modaccess.modanalyzer;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.modaccess.ModAccessManager.ModStateListener;
import com.motorola.actions.utils.MALogger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ModAnalyzer {
    private static final MALogger LOGGER = new MALogger(ModAnalyzer.class);
    private final Map<FeatureKey, ModStateListener> mConnectedFeatures = new HashMap();

    public void addModStateListener(FeatureKey featureKey, ModStateListener modStateListener) {
        if (modStateListener != null) {
            this.mConnectedFeatures.put(featureKey, modStateListener);
        }
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("addModStateListener, mConnectedFeatures = ");
        sb.append(getConnectedFeaturesCount());
        sb.append(", featureKey = ");
        sb.append(featureKey.name());
        mALogger.mo11957d(sb.toString());
    }

    public void removeModStateListener(FeatureKey featureKey) {
        this.mConnectedFeatures.remove(featureKey);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("removeModStateListener, mConnectedFeatures = ");
        sb.append(getConnectedFeaturesCount());
        sb.append(", featureKey = ");
        sb.append(featureKey.name());
        mALogger.mo11957d(sb.toString());
    }

    public int getConnectedFeaturesCount() {
        return this.mConnectedFeatures.size();
    }

    public void analyze(int i, boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("analyzing mod, productFamilyId: ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        for (Entry value : this.mConnectedFeatures.entrySet()) {
            ((ModStateListener) value.getValue()).onModStateChanged(i, z);
        }
    }
}
