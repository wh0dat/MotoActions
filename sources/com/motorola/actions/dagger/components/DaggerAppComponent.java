package com.motorola.actions.dagger.components;

import android.app.Application;
import com.motorola.actions.dagger.modules.AppModule;
import com.motorola.actions.dagger.modules.AppModule_ProvidesApplicationFactory;
import com.motorola.actions.dagger.modules.SleepPatternModule;
import com.motorola.actions.dagger.modules.SleepPatternModule_ProvidesAccelDaoFactory;
import com.motorola.actions.dagger.modules.SleepPatternModule_ProvidesPreprocessingDaoFactory;
import com.motorola.actions.dagger.modules.SleepPatternModule_ProvidesPreviewDaoFactory;
import com.motorola.actions.dagger.modules.SleepPatternModule_ProvidesProcessSleepEventThreadFactory;
import com.motorola.actions.dagger.modules.SleepPatternModule_ProvidesSleepPatternAnalyticsAccessFactory;
import com.motorola.actions.dagger.modules.SleepPatternModule_ProvidesSleepPatternDatabaseFactory;
import com.motorola.actions.dagger.modules.SleepPatternModule_ProvidesSleepPatternRepositoryFactory;
import com.motorola.actions.dagger.modules.SleepPatternModule_ProvidesSleepPatternSystemAccessFactory;
import com.motorola.actions.debug.DebugFragment;
import com.motorola.actions.debug.DebugFragment_MembersInjector;
import com.motorola.actions.sleepPattern.AccelerometerReceiver;
import com.motorola.actions.sleepPattern.AccelerometerReceiver_Factory;
import com.motorola.actions.sleepPattern.ActivityRecognitionManager;
import com.motorola.actions.sleepPattern.ActivityRecognitionManager_Factory;
import com.motorola.actions.sleepPattern.ProcessSleepEventThread;
import com.motorola.actions.sleepPattern.SleepEventsAlarmReceiver_Factory;
import com.motorola.actions.sleepPattern.SleepPatternProcessing;
import com.motorola.actions.sleepPattern.SleepPatternProcessing_Factory;
import com.motorola.actions.sleepPattern.SleepPatternService;
import com.motorola.actions.sleepPattern.SleepPatternService_MembersInjector;
import com.motorola.actions.sleepPattern.SleepPatternSystemAccess;
import com.motorola.actions.sleepPattern.data.AccelDao;
import com.motorola.actions.sleepPattern.data.PreprocessingDao;
import com.motorola.actions.sleepPattern.data.PreviewDao;
import com.motorola.actions.sleepPattern.instrumentation.SleepPatternAnalyticsAccess;
import com.motorola.actions.sleepPattern.repository.SleepPatternDatabase;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DaggerAppComponent implements AppComponent {
    private Provider<AccelerometerReceiver> accelerometerReceiverProvider;
    private Provider<ActivityRecognitionManager> activityRecognitionManagerProvider;
    private Provider<AccelDao> providesAccelDaoProvider;
    private Provider<Application> providesApplicationProvider;
    private Provider<PreprocessingDao> providesPreprocessingDaoProvider;
    private Provider<PreviewDao> providesPreviewDaoProvider;
    private Provider<ProcessSleepEventThread> providesProcessSleepEventThreadProvider;
    private Provider<SleepPatternAnalyticsAccess> providesSleepPatternAnalyticsAccessProvider;
    private Provider<SleepPatternDatabase> providesSleepPatternDatabaseProvider;
    private Provider<SleepPatternRepository> providesSleepPatternRepositoryProvider;
    private Provider<SleepPatternSystemAccess> providesSleepPatternSystemAccessProvider;
    private Provider<SleepPatternProcessing> sleepPatternProcessingProvider;

    public static final class Builder {
        /* access modifiers changed from: private */
        public AppModule appModule;
        /* access modifiers changed from: private */
        public SleepPatternModule sleepPatternModule;

        private Builder() {
        }

        public AppComponent build() {
            if (this.sleepPatternModule == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(SleepPatternModule.class.getCanonicalName());
                sb.append(" must be set");
                throw new IllegalStateException(sb.toString());
            } else if (this.appModule != null) {
                return new DaggerAppComponent(this);
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(AppModule.class.getCanonicalName());
                sb2.append(" must be set");
                throw new IllegalStateException(sb2.toString());
            }
        }

        public Builder appModule(AppModule appModule2) {
            this.appModule = (AppModule) Preconditions.checkNotNull(appModule2);
            return this;
        }

        public Builder sleepPatternModule(SleepPatternModule sleepPatternModule2) {
            this.sleepPatternModule = (SleepPatternModule) Preconditions.checkNotNull(sleepPatternModule2);
            return this;
        }
    }

    private DaggerAppComponent(Builder builder) {
        initialize(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    private void initialize(Builder builder) {
        this.providesSleepPatternDatabaseProvider = DoubleCheck.provider(SleepPatternModule_ProvidesSleepPatternDatabaseFactory.create(builder.sleepPatternModule));
        this.providesAccelDaoProvider = DoubleCheck.provider(SleepPatternModule_ProvidesAccelDaoFactory.create(builder.sleepPatternModule, this.providesSleepPatternDatabaseProvider));
        this.providesPreprocessingDaoProvider = DoubleCheck.provider(SleepPatternModule_ProvidesPreprocessingDaoFactory.create(builder.sleepPatternModule, this.providesSleepPatternDatabaseProvider));
        this.providesPreviewDaoProvider = DoubleCheck.provider(SleepPatternModule_ProvidesPreviewDaoFactory.create(builder.sleepPatternModule, this.providesSleepPatternDatabaseProvider));
        this.providesSleepPatternRepositoryProvider = DoubleCheck.provider(SleepPatternModule_ProvidesSleepPatternRepositoryFactory.create(builder.sleepPatternModule, this.providesAccelDaoProvider, this.providesPreprocessingDaoProvider, this.providesPreviewDaoProvider));
        this.providesApplicationProvider = DoubleCheck.provider(AppModule_ProvidesApplicationFactory.create(builder.appModule));
        this.providesSleepPatternSystemAccessProvider = DoubleCheck.provider(SleepPatternModule_ProvidesSleepPatternSystemAccessFactory.create(builder.sleepPatternModule, this.providesApplicationProvider));
        this.providesSleepPatternAnalyticsAccessProvider = DoubleCheck.provider(SleepPatternModule_ProvidesSleepPatternAnalyticsAccessFactory.create(builder.sleepPatternModule));
        this.sleepPatternProcessingProvider = SleepPatternProcessing_Factory.create(this.providesSleepPatternRepositoryProvider, this.providesSleepPatternSystemAccessProvider, this.providesSleepPatternAnalyticsAccessProvider);
        this.accelerometerReceiverProvider = AccelerometerReceiver_Factory.create(this.providesApplicationProvider, this.providesSleepPatternRepositoryProvider);
        this.activityRecognitionManagerProvider = ActivityRecognitionManager_Factory.create(this.providesApplicationProvider, this.providesSleepPatternRepositoryProvider);
        this.providesProcessSleepEventThreadProvider = DoubleCheck.provider(SleepPatternModule_ProvidesProcessSleepEventThreadFactory.create(builder.sleepPatternModule, this.sleepPatternProcessingProvider, this.accelerometerReceiverProvider, this.activityRecognitionManagerProvider));
    }

    public void inject(SleepPatternService sleepPatternService) {
        injectSleepPatternService(sleepPatternService);
    }

    public void inject(DebugFragment debugFragment) {
        injectDebugFragment(debugFragment);
    }

    private SleepPatternService injectSleepPatternService(SleepPatternService sleepPatternService) {
        SleepPatternService_MembersInjector.injectMSleepEventsAlarmReceiver(sleepPatternService, SleepEventsAlarmReceiver_Factory.newSleepEventsAlarmReceiver((ProcessSleepEventThread) this.providesProcessSleepEventThreadProvider.get()));
        SleepPatternService_MembersInjector.injectMProcessSleepEventThread(sleepPatternService, (ProcessSleepEventThread) this.providesProcessSleepEventThreadProvider.get());
        return sleepPatternService;
    }

    private DebugFragment injectDebugFragment(DebugFragment debugFragment) {
        DebugFragment_MembersInjector.injectMSleepPatternRepository(debugFragment, (SleepPatternRepository) this.providesSleepPatternRepositoryProvider.get());
        return debugFragment;
    }
}
