package android.databinding;

import android.databinding.CallbackRegistry.NotifierCallback;
import android.databinding.Observable.OnPropertyChangedCallback;
import android.support.annotation.NonNull;

public class PropertyChangeRegistry extends CallbackRegistry<OnPropertyChangedCallback, Observable, Void> {
    private static final NotifierCallback<OnPropertyChangedCallback, Observable, Void> NOTIFIER_CALLBACK = new NotifierCallback<OnPropertyChangedCallback, Observable, Void>() {
        public void onNotifyCallback(OnPropertyChangedCallback onPropertyChangedCallback, Observable observable, int i, Void voidR) {
            onPropertyChangedCallback.onPropertyChanged(observable, i);
        }
    };

    public PropertyChangeRegistry() {
        super(NOTIFIER_CALLBACK);
    }

    public void notifyChange(@NonNull Observable observable, int i) {
        notifyCallbacks(observable, i, null);
    }
}
