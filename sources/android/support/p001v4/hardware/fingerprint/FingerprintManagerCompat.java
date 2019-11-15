package android.support.p001v4.hardware.fingerprint;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.p001v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationResultInternal;
import android.support.p001v4.p003os.CancellationSignal;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

/* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompat */
public final class FingerprintManagerCompat {
    static final FingerprintManagerCompatImpl IMPL;
    private Context mContext;

    @RequiresApi(23)
    /* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompat$Api23FingerprintManagerCompatImpl */
    private static class Api23FingerprintManagerCompatImpl implements FingerprintManagerCompatImpl {
        public boolean hasEnrolledFingerprints(Context context) {
            return FingerprintManagerCompatApi23.hasEnrolledFingerprints(context);
        }

        public boolean isHardwareDetected(Context context) {
            return FingerprintManagerCompatApi23.isHardwareDetected(context);
        }

        public void authenticate(Context context, CryptoObject cryptoObject, int i, CancellationSignal cancellationSignal, AuthenticationCallback authenticationCallback, Handler handler) {
            FingerprintManagerCompatApi23.authenticate(context, wrapCryptoObject(cryptoObject), i, cancellationSignal != null ? cancellationSignal.getCancellationSignalObject() : null, wrapCallback(authenticationCallback), handler);
        }

        private static android.support.p001v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
            if (cryptoObject == null) {
                return null;
            }
            if (cryptoObject.getCipher() != null) {
                return new android.support.p001v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getCipher());
            }
            if (cryptoObject.getSignature() != null) {
                return new android.support.p001v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getSignature());
            }
            if (cryptoObject.getMac() != null) {
                return new android.support.p001v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getMac());
            }
            return null;
        }

        static CryptoObject unwrapCryptoObject(android.support.p001v4.hardware.fingerprint.FingerprintManagerCompatApi23.CryptoObject cryptoObject) {
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

        private static android.support.p001v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationCallback wrapCallback(final AuthenticationCallback authenticationCallback) {
            return new android.support.p001v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationCallback() {
                public void onAuthenticationError(int i, CharSequence charSequence) {
                    authenticationCallback.onAuthenticationError(i, charSequence);
                }

                public void onAuthenticationHelp(int i, CharSequence charSequence) {
                    authenticationCallback.onAuthenticationHelp(i, charSequence);
                }

                public void onAuthenticationSucceeded(AuthenticationResultInternal authenticationResultInternal) {
                    authenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(Api23FingerprintManagerCompatImpl.unwrapCryptoObject(authenticationResultInternal.getCryptoObject())));
                }

                public void onAuthenticationFailed() {
                    authenticationCallback.onAuthenticationFailed();
                }
            };
        }
    }

    /* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompat$AuthenticationCallback */
    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int i, CharSequence charSequence) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int i, CharSequence charSequence) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
        }
    }

    /* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompat$AuthenticationResult */
    public static final class AuthenticationResult {
        private CryptoObject mCryptoObject;

        public AuthenticationResult(CryptoObject cryptoObject) {
            this.mCryptoObject = cryptoObject;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }
    }

    /* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompat$CryptoObject */
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

    /* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompat$FingerprintManagerCompatImpl */
    private interface FingerprintManagerCompatImpl {
        void authenticate(Context context, CryptoObject cryptoObject, int i, CancellationSignal cancellationSignal, AuthenticationCallback authenticationCallback, Handler handler);

        boolean hasEnrolledFingerprints(Context context);

        boolean isHardwareDetected(Context context);
    }

    /* renamed from: android.support.v4.hardware.fingerprint.FingerprintManagerCompat$LegacyFingerprintManagerCompatImpl */
    private static class LegacyFingerprintManagerCompatImpl implements FingerprintManagerCompatImpl {
        public void authenticate(Context context, CryptoObject cryptoObject, int i, CancellationSignal cancellationSignal, AuthenticationCallback authenticationCallback, Handler handler) {
        }

        public boolean hasEnrolledFingerprints(Context context) {
            return false;
        }

        public boolean isHardwareDetected(Context context) {
            return false;
        }
    }

    public static FingerprintManagerCompat from(Context context) {
        return new FingerprintManagerCompat(context);
    }

    private FingerprintManagerCompat(Context context) {
        this.mContext = context;
    }

    static {
        if (VERSION.SDK_INT >= 23) {
            IMPL = new Api23FingerprintManagerCompatImpl();
        } else {
            IMPL = new LegacyFingerprintManagerCompatImpl();
        }
    }

    public boolean hasEnrolledFingerprints() {
        return IMPL.hasEnrolledFingerprints(this.mContext);
    }

    public boolean isHardwareDetected() {
        return IMPL.isHardwareDetected(this.mContext);
    }

    public void authenticate(@Nullable CryptoObject cryptoObject, int i, @Nullable CancellationSignal cancellationSignal, @NonNull AuthenticationCallback authenticationCallback, @Nullable Handler handler) {
        IMPL.authenticate(this.mContext, cryptoObject, i, cancellationSignal, authenticationCallback, handler);
    }
}
