package android.support.p001v4.p003os;

/* renamed from: android.support.v4.os.OperationCanceledException */
public class OperationCanceledException extends RuntimeException {
    public OperationCanceledException() {
        this(null);
    }

    public OperationCanceledException(String str) {
        if (str == null) {
            str = "The operation has been canceled.";
        }
        super(str);
    }
}
