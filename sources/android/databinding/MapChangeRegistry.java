package android.databinding;

import android.databinding.CallbackRegistry.NotifierCallback;
import android.databinding.ObservableMap.OnMapChangedCallback;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MapChangeRegistry extends CallbackRegistry<OnMapChangedCallback, ObservableMap, Object> {
    private static NotifierCallback<OnMapChangedCallback, ObservableMap, Object> NOTIFIER_CALLBACK = new NotifierCallback<OnMapChangedCallback, ObservableMap, Object>() {
        public void onNotifyCallback(OnMapChangedCallback onMapChangedCallback, ObservableMap observableMap, int i, Object obj) {
            onMapChangedCallback.onMapChanged(observableMap, obj);
        }
    };

    public MapChangeRegistry() {
        super(NOTIFIER_CALLBACK);
    }

    public void notifyChange(@NonNull ObservableMap observableMap, @Nullable Object obj) {
        notifyCallbacks(observableMap, 0, obj);
    }
}
