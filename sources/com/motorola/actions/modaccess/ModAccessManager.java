package com.motorola.actions.modaccess;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.modaccess.modanalyzer.ModAnalyzer;
import com.motorola.actions.utils.MALogger;
import com.motorola.mod.IModManager;
import com.motorola.mod.IModManager.Stub;
import com.motorola.mod.ModManager;

public final class ModAccessManager {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(ModAccessManager.class);
    private boolean mConnected;
    /* access modifiers changed from: private */
    public HotPlugModListener mHotPlugModListener;
    /* access modifiers changed from: private */
    public ModAnalyzer mModAnalyzer;
    /* access modifiers changed from: private */
    public ModManager mModManager;
    /* access modifiers changed from: private */
    public IModManager mModManagerIf;
    /* access modifiers changed from: private */
    public ServiceConnection mModManagerServiceConnection;

    private class ModManagerServiceConnection implements ServiceConnection {
        private ModManagerServiceConnection() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ModAccessManager.LOGGER.mo11957d("Connected to ModManager Service");
            synchronized (ModAccessManager.this) {
                ModAccessManager.this.mModManagerIf = Stub.asInterface(iBinder);
                ModAccessManager.this.mModManager = new ModManager(ActionsApplication.getAppContext(), ModAccessManager.this.mModManagerIf);
                ModAccessManager.this.mHotPlugModListener = new HotPlugModListener(ModAccessManager.this.mModAnalyzer);
                ModAccessManager.this.mModManager.registerModListener(ModAccessManager.this.mHotPlugModListener, new int[]{256});
                ModAccessManager.this.analyzeModDevices();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ModAccessManager.LOGGER.mo11957d("Disconnected from ModManager Service");
            ModAccessManager.this.mModManagerServiceConnection = null;
            ModAccessManager.this.reset();
        }
    }

    public interface ModStateListener {
        void onModStateChanged(int i, boolean z);
    }

    private static class SingletonHolder {
        static final ModAccessManager INSTANCE = new ModAccessManager();

        private SingletonHolder() {
        }
    }

    private ModAccessManager() {
    }

    public void connect(FeatureKey featureKey, ModStateListener modStateListener) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("connect, mConnected = ");
        sb.append(this.mConnected);
        sb.append(", featureKey = ");
        sb.append(featureKey.name());
        mALogger.mo11957d(sb.toString());
        if (!this.mConnected) {
            init();
            this.mModAnalyzer.addModStateListener(featureKey, modStateListener);
            Intent intent = new Intent(ModManager.ACTION_BIND_MANAGER);
            intent.setComponent(ModManager.MOD_SERVICE_NAME);
            LOGGER.mo11957d("Bind to the ModManager Service");
            ActionsApplication.getAppContext().bindService(intent, this.mModManagerServiceConnection, 1);
            this.mConnected = true;
            return;
        }
        this.mModAnalyzer.addModStateListener(featureKey, modStateListener);
        analyzeModDevices();
    }

    private void init() {
        this.mModAnalyzer = new ModAnalyzer();
        this.mModManagerServiceConnection = new ModManagerServiceConnection();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x004a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        LOGGER.mo11959e("Service not registered");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0058, code lost:
        r3.mConnected = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005a, code lost:
        throw r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x004c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void disconnect(com.motorola.actions.FeatureKey r4) {
        /*
            r3 = this;
            com.motorola.actions.utils.MALogger r0 = LOGGER
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "disconnect, mConnected = "
            r1.append(r2)
            boolean r2 = r3.mConnected
            r1.append(r2)
            java.lang.String r2 = ", featureKey = "
            r1.append(r2)
            java.lang.String r2 = r4.name()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.mo11957d(r1)
            com.motorola.actions.modaccess.modanalyzer.ModAnalyzer r0 = r3.mModAnalyzer
            if (r0 == 0) goto L_0x002d
            com.motorola.actions.modaccess.modanalyzer.ModAnalyzer r0 = r3.mModAnalyzer
            r0.removeModStateListener(r4)
        L_0x002d:
            boolean r4 = r3.mConnected
            if (r4 == 0) goto L_0x005b
            com.motorola.actions.modaccess.modanalyzer.ModAnalyzer r4 = r3.mModAnalyzer
            if (r4 == 0) goto L_0x005b
            com.motorola.actions.modaccess.modanalyzer.ModAnalyzer r4 = r3.mModAnalyzer
            int r4 = r4.getConnectedFeaturesCount()
            if (r4 != 0) goto L_0x005b
            r4 = 0
            android.content.Context r0 = com.motorola.actions.ActionsApplication.getAppContext()     // Catch:{ IllegalArgumentException -> 0x004c }
            android.content.ServiceConnection r1 = r3.mModManagerServiceConnection     // Catch:{ IllegalArgumentException -> 0x004c }
            r0.unbindService(r1)     // Catch:{ IllegalArgumentException -> 0x004c }
        L_0x0047:
            r3.mConnected = r4
            goto L_0x0054
        L_0x004a:
            r0 = move-exception
            goto L_0x0058
        L_0x004c:
            com.motorola.actions.utils.MALogger r0 = LOGGER     // Catch:{ all -> 0x004a }
            java.lang.String r1 = "Service not registered"
            r0.mo11959e(r1)     // Catch:{ all -> 0x004a }
            goto L_0x0047
        L_0x0054:
            r3.reset()
            goto L_0x005b
        L_0x0058:
            r3.mConnected = r4
            throw r0
        L_0x005b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.modaccess.ModAccessManager.disconnect(com.motorola.actions.FeatureKey):void");
    }

    /* access modifiers changed from: private */
    public void reset() {
        synchronized (this) {
            this.mModManagerServiceConnection = null;
            if (this.mModManager != null) {
                this.mModManager.registerModListener(this.mHotPlugModListener, null);
            }
            this.mModManager = null;
            this.mModManagerIf = null;
            this.mHotPlugModListener = null;
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:12|13) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        LOGGER.mo11959e("Error retrieving mod device list from modlib");
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0032 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void analyzeModDevices() {
        /*
            r4 = this;
            monitor-enter(r4)
            com.motorola.mod.ModManager r0 = r4.mModManager     // Catch:{ all -> 0x003b }
            if (r0 == 0) goto L_0x0039
            com.motorola.mod.ModManager r0 = r4.mModManager     // Catch:{ RemoteException -> 0x0032 }
            r1 = 1
            java.util.List r0 = r0.getModList(r1)     // Catch:{ RemoteException -> 0x0032 }
            if (r0 == 0) goto L_0x002a
            java.util.Iterator r0 = r0.iterator()     // Catch:{ RemoteException -> 0x0032 }
        L_0x0012:
            boolean r2 = r0.hasNext()     // Catch:{ RemoteException -> 0x0032 }
            if (r2 == 0) goto L_0x0039
            java.lang.Object r2 = r0.next()     // Catch:{ RemoteException -> 0x0032 }
            com.motorola.mod.ModDevice r2 = (com.motorola.mod.ModDevice) r2     // Catch:{ RemoteException -> 0x0032 }
            com.motorola.actions.modaccess.modanalyzer.ModAnalyzer r3 = r4.mModAnalyzer     // Catch:{ RemoteException -> 0x0032 }
            int r2 = r2.getProductId()     // Catch:{ RemoteException -> 0x0032 }
            int r2 = r2 >> 4
            r3.analyze(r2, r1)     // Catch:{ RemoteException -> 0x0032 }
            goto L_0x0012
        L_0x002a:
            com.motorola.actions.utils.MALogger r0 = LOGGER     // Catch:{ RemoteException -> 0x0032 }
            java.lang.String r1 = "getModList returned null pointer."
            r0.mo11963w(r1)     // Catch:{ RemoteException -> 0x0032 }
            goto L_0x0039
        L_0x0032:
            com.motorola.actions.utils.MALogger r0 = LOGGER     // Catch:{ all -> 0x003b }
            java.lang.String r1 = "Error retrieving mod device list from modlib"
            r0.mo11959e(r1)     // Catch:{ all -> 0x003b }
        L_0x0039:
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            return
        L_0x003b:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.modaccess.ModAccessManager.analyzeModDevices():void");
    }

    public static ModAccessManager getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
