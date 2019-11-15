package android.databinding;

import android.databinding.Observable.OnPropertyChangedCallback;
import android.support.annotation.NonNull;

public class BaseObservable implements Observable {
    private transient PropertyChangeRegistry mCallbacks;

    public void addOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback onPropertyChangedCallback) {
        synchronized (this) {
            if (this.mCallbacks == null) {
                this.mCallbacks = new PropertyChangeRegistry();
            }
        }
        this.mCallbacks.add(onPropertyChangedCallback);
    }

    public void removeOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback onPropertyChangedCallback) {
        synchronized (this) {
            if (this.mCallbacks != null) {
                this.mCallbacks.remove(onPropertyChangedCallback);
            }
        }
    }

    public void notifyChange() {
        synchronized (this) {
            if (this.mCallbacks != null) {
                this.mCallbacks.notifyCallbacks(this, 0, null);
            }
        }
    }

    public void notifyPropertyChanged(int i) {
        synchronized (this) {
            if (this.mCallbacks != null) {
                this.mCallbacks.notifyCallbacks(this, i, null);
            }
        }
    }
}
