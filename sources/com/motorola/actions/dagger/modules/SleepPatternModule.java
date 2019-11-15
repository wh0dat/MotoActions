package com.motorola.actions.dagger.modules;

import android.app.Application;
import com.motorola.actions.checkin.CheckinAlarm;
import com.motorola.actions.sleepPattern.AccelerometerReceiver;
import com.motorola.actions.sleepPattern.ActivityRecognitionManager;
import com.motorola.actions.sleepPattern.ProcessSleepEventThread;
import com.motorola.actions.sleepPattern.SleepPatternProcessing;
import com.motorola.actions.sleepPattern.SleepPatternSystemAccess;
import com.motorola.actions.sleepPattern.SleepPatternSystemAccessImpl;
import com.motorola.actions.sleepPattern.data.AccelDao;
import com.motorola.actions.sleepPattern.data.PreprocessingDao;
import com.motorola.actions.sleepPattern.data.PreviewDao;
import com.motorola.actions.sleepPattern.instrumentation.SleepPatternAnalytics;
import com.motorola.actions.sleepPattern.instrumentation.SleepPatternAnalyticsAccess;
import com.motorola.actions.sleepPattern.repository.SleepPatternDataSource;
import com.motorola.actions.sleepPattern.repository.SleepPatternDatabase;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class SleepPatternModule {
    private SleepPatternDatabase mSleepPatternDatabase;

    public SleepPatternModule(Application application) {
        this.mSleepPatternDatabase = SleepPatternDatabase.getInstance(application);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public SleepPatternSystemAccess providesSleepPatternSystemAccess(Application application) {
        return new SleepPatternSystemAccessImpl(application);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public SleepPatternDatabase providesSleepPatternDatabase() {
        return this.mSleepPatternDatabase;
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public AccelDao providesAccelDao(SleepPatternDatabase sleepPatternDatabase) {
        return sleepPatternDatabase.accelDao();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public PreprocessingDao providesPreprocessingDao(SleepPatternDatabase sleepPatternDatabase) {
        return sleepPatternDatabase.preprocessingDao();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public PreviewDao providesPreviewDao(SleepPatternDatabase sleepPatternDatabase) {
        return sleepPatternDatabase.previewDao();
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public SleepPatternRepository providesSleepPatternRepository(AccelDao accelDao, PreprocessingDao preprocessingDao, PreviewDao previewDao) {
        return new SleepPatternDataSource(previewDao, preprocessingDao, accelDao);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public SleepPatternAnalyticsAccess providesSleepPatternAnalyticsAccess() {
        return (SleepPatternAnalyticsAccess) CheckinAlarm.getInstance().getAnalytics(SleepPatternAnalytics.class);
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public ProcessSleepEventThread providesProcessSleepEventThread(SleepPatternProcessing sleepPatternProcessing, AccelerometerReceiver accelerometerReceiver, ActivityRecognitionManager activityRecognitionManager) {
        return new ProcessSleepEventThread(sleepPatternProcessing, accelerometerReceiver, activityRecognitionManager);
    }
}
