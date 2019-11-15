package android.support.p001v4.hardware.fingerprint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

@RequiresApi(23)
@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23 */
public final class FingerprintManagerCompatApi23 {

    /* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23$AuthenticationCallback */
    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int i, CharSequence charSequence) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int i, CharSequence charSequence) {
        }

        public void onAuthenticationSucceeded(AuthenticationResultInternal authenticationResultInternal) {
        }
    }

    /* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23$AuthenticationResultInternal */
    public static final class AuthenticationResultInternal {
        private CryptoObject mCryptoObject;

        public AuthenticationResultInternal(CryptoObject cryptoObject) {
            this.mCryptoObject = cryptoObject;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }
    }

    /* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23$CryptoObject */
    public static class CryptoObject {
        private final Cipher mCipher;
        private final Mac mMac;
        private final Signature mSignature;

        public CryptoObject(Signature signature) {
            this.mSignature = signature;
            this.mCipher = null;
            this.mMac = null;
        }

        public CryptoObject(Cipher cipher) {
            this.mCipher = cipher;
            this.mSignature = null;
            this.mMac = null;
        }

        public CryptoObject(Mac mac) {
            this.mMac = mac;
            this.mCipher = null;
            this.mSignature = null;
        }

        public Signature getSignature() {
            return this.mSignature;
        }

        public Cipher getCipher() {
            return this.mCipher;
        }

        public Mac getMac() {
            return this.mMac;
        }
    }

    private static FingerprintManager getFingerprintManagerOrNull(Context context) {
        if (context.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            return (FingerprintManager) context.getSystemService(FingerprintManager.class);
        }
        return null;
    }

    @SuppressLint({"MissingPermission"})
    static boolean hasEnrolledFingerprints(Context context) {
        FingerprintManager fingerprintManagerOrNull = getFingerprintManagerOrNull(context);
        return fingerprintManagerOrNull != null && fingerprintManagerOrNull.hasEnrolledFingerprints();
    }

    @SuppressLint({"MissingPermission"})
    static boolean isHardwareDetected(Context context) {
        FingerprintManager fingerprintManagerOrNull = getFingerprintManagerOrNull(context);
        return fingerprintManagerOrNull != null && fingerprintManagerOrNull.isHardwareDetected();
    }

    @SuppressLint({"MissingPermission"})
    static void authenticate(Context context, CryptoObject cryptoObject, int i, Object obj, AuthenticationCallback authenticationCallback, Handler handler) {
        FingerprintManager fingerprintManagerOrNull = getFingerprintManagerOrNull(context);
        if (fingerprintManagerOrNull != null) {
            fingerprintManagerOrNull.authenticate(wrapCryptoObject(cryptoObject), (CancellationSignal) obj, i, wrapCallback(authenticationCallback), handler);
        }
    }

    private static android.hardware.fingerprint.FingerprintManager.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        if (cryptoObject.getCipher() != null) {
            return new android.hardware.fingerprint.FingerprintManager.CryptoObject(cryptoObject.getCipher());
        }
        if (cryptoObject.getSignature() != null) {
            return new android.hardware.fingerprint.FingerprintManager.CryptoObject(cryptoObject.getSignature());
        }
        if (cryptoObject.getMac() != null) {
            return new android.hardware.fingerprint.FingerprintManager.CryptoObject(cryptoObject.getMac());
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static CryptoObject unwrapCryptoObject(android.hardware.fingerprint.FingerprintManager.CryptoObject cryptoObject) {
        if (cryptoObject == null) {
            return null;
        }
        if (cryptoObject.getCipher() != null) {
            return new CryptoObject(cryptoObject.getCipher());
        }
        if (cryptoObject.getSignature() != null) {
            return new CryptoObject(cryptoObject.getSignature());
        }
        if (cryptoObject.getMac() != null) {
            return new CryptoObject(cryptoObject.getMac());
        }
        return null;
    }

    private static android.hardware.fingerprint.FingerprintManager.AuthenticationCallback wrapCallback(final AuthenticationCallback authenticationCallback) {
        return new android.hardware.fingerprint.FingerprintManager.AuthenticationCallback() {
            public void onAuthenticationError(int i, CharSequence charSequence) {
                authenticationCallback.onAuthenticationError(i, charSequence);
            }

            public void onAuthenticationHelp(int i, CharSequence charSequence) {
                authenticationCallback.onAuthenticationHelp(i, charSequence);
            }

            public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
                authenticationCallback.onAuthenticationSucceeded(new AuthenticationResultInternal(FingerprintManagerCompatApi23.unwrapCryptoObject(authenticationResult.getCryptoObject())));
            }

            public void onAuthenticationFailed() {
                authenticationCallback.onAuthenticationFailed();
            }
        };
    }
}
