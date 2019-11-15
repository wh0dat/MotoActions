package android.support.p001v4.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.p001v4.app.Fragment.SavedState;
import android.support.p001v4.app.FragmentManager.BackStackEntry;
import android.support.p001v4.app.FragmentManager.FragmentLifecycleCallbacks;
import android.support.p001v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.p001v4.util.ArraySet;
import android.support.p001v4.util.DebugUtils;
import android.support.p001v4.util.LogWriter;
import android.support.p001v4.util.Pair;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater.Factory2;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* renamed from: android.support.v4.app.FragmentManagerImpl */
/* compiled from: FragmentManager */
final class FragmentManagerImpl extends FragmentManager implements Factory2 {
    static final Interpolator ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    static final Interpolator ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
    static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    SparseArray<Fragment> mActive;
    final ArrayList<Fragment> mAdded = new ArrayList<>();
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState = 0;
    boolean mDestroyed;
    Runnable mExecCommit = new Runnable() {
        public void run() {
            FragmentManagerImpl.this.execPendingActions();
        }
    };
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private final CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks = new CopyOnWriteArrayList<>();
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex = 0;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    SparseArray<Parcelable> mStateArray = null;
    Bundle mStateBundle = null;
    boolean mStateSaved;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;

    /* renamed from: android.support.v4.app.FragmentManagerImpl$AnimateOnHWLayerIfNeededListener */
    /* compiled from: FragmentManager */
    private static class AnimateOnHWLayerIfNeededListener extends AnimationListenerWrapper {
        View mView;

        AnimateOnHWLayerIfNeededListener(View view, AnimationListener animationListener) {
            super(animationListener);
            this.mView = view;
        }

        @CallSuper
        public void onAnimationEnd(Animation animation) {
            if (ViewCompat.isAttachedToWindow(this.mView) || VERSION.SDK_INT >= 24) {
                this.mView.post(new Runnable() {
                    public void run() {
                        AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, null);
                    }
                });
            } else {
                this.mView.setLayerType(0, null);
            }
            super.onAnimationEnd(animation);
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$AnimationListenerWrapper */
    /* compiled from: FragmentManager */
    private static class AnimationListenerWrapper implements AnimationListener {
        private final AnimationListener mWrapped;

        private AnimationListenerWrapper(AnimationListener animationListener) {
            this.mWrapped = animationListener;
        }

        @CallSuper
        public void onAnimationStart(Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationStart(animation);
            }
        }

        @CallSuper
        public void onAnimationEnd(Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationEnd(animation);
            }
        }

        @CallSuper
        public void onAnimationRepeat(Animation animation) {
            if (this.mWrapped != null) {
                this.mWrapped.onAnimationRepeat(animation);
            }
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$AnimationOrAnimator */
    /* compiled from: FragmentManager */
    private static class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        private AnimationOrAnimator(Animation animation2) {
            this.animation = animation2;
            this.animator = null;
            if (animation2 == null) {
                throw new IllegalStateException("Animation cannot be null");
            }
        }

        private AnimationOrAnimator(Animator animator2) {
            this.animation = null;
            this.animator = animator2;
            if (animator2 == null) {
                throw new IllegalStateException("Animator cannot be null");
            }
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$AnimatorOnHWLayerIfNeededListener */
    /* compiled from: FragmentManager */
    private static class AnimatorOnHWLayerIfNeededListener extends AnimatorListenerAdapter {
        View mView;

        AnimatorOnHWLayerIfNeededListener(View view) {
            this.mView = view;
        }

        public void onAnimationStart(Animator animator) {
            this.mView.setLayerType(2, null);
        }

        public void onAnimationEnd(Animator animator) {
            this.mView.setLayerType(0, null);
            animator.removeListener(this);
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$FragmentTag */
    /* compiled from: FragmentManager */
    static class FragmentTag {
        public static final int[] Fragment = {16842755, 16842960, 16842961};
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        FragmentTag() {
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$OpGenerator */
    /* compiled from: FragmentManager */
    interface OpGenerator {
        boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$PopBackStackState */
    /* compiled from: FragmentManager */
    private class PopBackStackState implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        PopBackStackState(String str, int i, int i2) {
            this.mName = str;
            this.mId = i;
            this.mFlags = i2;
        }

        public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
                FragmentManager peekChildFragmentManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager();
                if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                    return false;
                }
            }
            return FragmentManagerImpl.this.popBackStackState(arrayList, arrayList2, this.mName, this.mId, this.mFlags);
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl$StartEnterTransitionListener */
    /* compiled from: FragmentManager */
    static class StartEnterTransitionListener implements OnStartEnterTransitionListener {
        /* access modifiers changed from: private */
        public final boolean mIsBack;
        private int mNumPostponed;
        /* access modifiers changed from: private */
        public final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord backStackRecord, boolean z) {
            this.mIsBack = z;
            this.mRecord = backStackRecord;
        }

        public void onStartEnterTransition() {
            this.mNumPostponed--;
            if (this.mNumPostponed == 0) {
                this.mRecord.mManager.scheduleCommit();
            }
        }

        public void startListening() {
            this.mNumPostponed++;
        }

        public boolean isReady() {
            return this.mNumPostponed == 0;
        }

        public void completeTransaction() {
            boolean z = this.mNumPostponed > 0;
            FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
            int size = fragmentManagerImpl.mAdded.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = (Fragment) fragmentManagerImpl.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener(null);
                if (z && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, !z, true);
        }

        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }
    }

    public static int reverseTransit(int i) {
        if (i == 4097) {
            return 8194;
        }
        if (i == 4099) {
            return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
        }
        if (i != 8194) {
            return 0;
        }
        return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
    }

    public static int transitToStyleIndex(int i, boolean z) {
        if (i == 4097) {
            return z ? 1 : 2;
        }
        if (i == 4099) {
            return z ? 5 : 6;
        }
        if (i != 8194) {
            return -1;
        }
        return z ? 3 : 4;
    }

    /* access modifiers changed from: 0000 */
    public Factory2 getLayoutInflaterFactory() {
        return this;
    }

    FragmentManagerImpl() {
    }

    static boolean modifiesAlpha(AnimationOrAnimator animationOrAnimator) {
        if (animationOrAnimator.animation instanceof AlphaAnimation) {
            return true;
        }
        if (!(animationOrAnimator.animation instanceof AnimationSet)) {
            return modifiesAlpha(animationOrAnimator.animator);
        }
        List animations = ((AnimationSet) animationOrAnimator.animation).getAnimations();
        for (int i = 0; i < animations.size(); i++) {
            if (animations.get(i) instanceof AlphaAnimation) {
                return true;
            }
        }
        return false;
    }

    static boolean modifiesAlpha(Animator animator) {
        if (animator == null) {
            return false;
        }
        if (animator instanceof ValueAnimator) {
            PropertyValuesHolder[] values = ((ValueAnimator) animator).getValues();
            for (PropertyValuesHolder propertyName : values) {
                if ("alpha".equals(propertyName.getPropertyName())) {
                    return true;
                }
            }
        } else if (animator instanceof AnimatorSet) {
            ArrayList childAnimations = ((AnimatorSet) animator).getChildAnimations();
            for (int i = 0; i < childAnimations.size(); i++) {
                if (modifiesAlpha((Animator) childAnimations.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean shouldRunOnHWLayer(View view, AnimationOrAnimator animationOrAnimator) {
        boolean z = false;
        if (view == null || animationOrAnimator == null) {
            return false;
        }
        if (VERSION.SDK_INT >= 19 && view.getLayerType() == 0 && ViewCompat.hasOverlappingRendering(view) && modifiesAlpha(animationOrAnimator)) {
            z = true;
        }
        return z;
    }

    private void throwException(RuntimeException runtimeException) {
        Log.e(TAG, runtimeException.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
        if (this.mHost != null) {
            try {
                this.mHost.onDump("  ", null, printWriter, new String[0]);
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", null, printWriter, new String[0]);
            } catch (Exception e2) {
                Log.e(TAG, "Failed dumping state", e2);
            }
        }
        throw runtimeException;
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public boolean executePendingTransactions() {
        boolean execPendingActions = execPendingActions();
        forcePostponedTransactions();
        return execPendingActions;
    }

    public void popBackStack() {
        enqueueAction(new PopBackStackState(null, -1, 0), false);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        return popBackStackImmediate(null, -1, 0);
    }

    public void popBackStack(String str, int i) {
        enqueueAction(new PopBackStackState(str, -1, i), false);
    }

    public boolean popBackStackImmediate(String str, int i) {
        checkStateLoss();
        return popBackStackImmediate(str, -1, i);
    }

    public void popBackStack(int i, int i2) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bad id: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        enqueueAction(new PopBackStackState(null, i, i2), false);
    }

    public boolean popBackStackImmediate(int i, int i2) {
        checkStateLoss();
        execPendingActions();
        if (i >= 0) {
            return popBackStackImmediate(null, i, i2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Bad id: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    private boolean popBackStackImmediate(String str, int i, int i2) {
        execPendingActions();
        ensureExecReady(true);
        if (this.mPrimaryNav != null && i < 0 && str == null) {
            FragmentManager peekChildFragmentManager = this.mPrimaryNav.peekChildFragmentManager();
            if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                return true;
            }
        }
        boolean popBackStackState = popBackStackState(this.mTmpRecords, this.mTmpIsPop, str, i, i2);
        if (popBackStackState) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        doPendingDeferredStart();
        burpActive();
        return popBackStackState;
    }

    public int getBackStackEntryCount() {
        if (this.mBackStack != null) {
            return this.mBackStack.size();
        }
        return 0;
    }

    public BackStackEntry getBackStackEntryAt(int i) {
        return (BackStackEntry) this.mBackStack.get(i);
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<>();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(onBackStackChangedListener);
        }
    }

    public void putFragment(Bundle bundle, String str, Fragment fragment) {
        if (fragment.mIndex < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(fragment);
            sb.append(" is not currently in the FragmentManager");
            throwException(new IllegalStateException(sb.toString()));
        }
        bundle.putInt(str, fragment.mIndex);
    }

    public Fragment getFragment(Bundle bundle, String str) {
        int i = bundle.getInt(str, -1);
        if (i == -1) {
            return null;
        }
        Fragment fragment = (Fragment) this.mActive.get(i);
        if (fragment == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment no longer exists for key ");
            sb.append(str);
            sb.append(": index ");
            sb.append(i);
            throwException(new IllegalStateException(sb.toString()));
        }
        return fragment;
    }

    public List<Fragment> getFragments() {
        List<Fragment> list;
        if (this.mAdded.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        synchronized (this.mAdded) {
            list = (List) this.mAdded.clone();
        }
        return list;
    }

    /* access modifiers changed from: 0000 */
    public List<Fragment> getActiveFragments() {
        if (this.mActive == null) {
            return null;
        }
        int size = this.mActive.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(this.mActive.valueAt(i));
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public int getActiveFragmentCount() {
        if (this.mActive == null) {
            return 0;
        }
        return this.mActive.size();
    }

    public SavedState saveFragmentInstanceState(Fragment fragment) {
        if (fragment.mIndex < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(fragment);
            sb.append(" is not currently in the FragmentManager");
            throwException(new IllegalStateException(sb.toString()));
        }
        SavedState savedState = null;
        if (fragment.mState <= 0) {
            return null;
        }
        Bundle saveFragmentBasicState = saveFragmentBasicState(fragment);
        if (saveFragmentBasicState != null) {
            savedState = new SavedState(saveFragmentBasicState);
        }
        return savedState;
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("    ");
        String sb2 = sb.toString();
        if (this.mActive != null) {
            int size = this.mActive.size();
            if (size > 0) {
                printWriter.print(str);
                printWriter.print("Active Fragments in ");
                printWriter.print(Integer.toHexString(System.identityHashCode(this)));
                printWriter.println(":");
                for (int i = 0; i < size; i++) {
                    Fragment fragment = (Fragment) this.mActive.valueAt(i);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i);
                    printWriter.print(": ");
                    printWriter.println(fragment);
                    if (fragment != null) {
                        fragment.dump(sb2, fileDescriptor, printWriter, strArr);
                    }
                }
            }
        }
        int size2 = this.mAdded.size();
        if (size2 > 0) {
            printWriter.print(str);
            printWriter.println("Added Fragments:");
            for (int i2 = 0; i2 < size2; i2++) {
                Fragment fragment2 = (Fragment) this.mAdded.get(i2);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i2);
                printWriter.print(": ");
                printWriter.println(fragment2.toString());
            }
        }
        if (this.mCreatedMenus != null) {
            int size3 = this.mCreatedMenus.size();
            if (size3 > 0) {
                printWriter.print(str);
                printWriter.println("Fragments Created Menus:");
                for (int i3 = 0; i3 < size3; i3++) {
                    Fragment fragment3 = (Fragment) this.mCreatedMenus.get(i3);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i3);
                    printWriter.print(": ");
                    printWriter.println(fragment3.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            int size4 = this.mBackStack.size();
            if (size4 > 0) {
                printWriter.print(str);
                printWriter.println("Back Stack:");
                for (int i4 = 0; i4 < size4; i4++) {
                    BackStackRecord backStackRecord = (BackStackRecord) this.mBackStack.get(i4);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i4);
                    printWriter.print(": ");
                    printWriter.println(backStackRecord.toString());
                    backStackRecord.dump(sb2, fileDescriptor, printWriter, strArr);
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                int size5 = this.mBackStackIndices.size();
                if (size5 > 0) {
                    printWriter.print(str);
                    printWriter.println("Back Stack Indices:");
                    for (int i5 = 0; i5 < size5; i5++) {
                        BackStackRecord backStackRecord2 = (BackStackRecord) this.mBackStackIndices.get(i5);
                        printWriter.print(str);
                        printWriter.print("  #");
                        printWriter.print(i5);
                        printWriter.print(": ");
                        printWriter.println(backStackRecord2);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(str);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        if (this.mPendingActions != null) {
            int size6 = this.mPendingActions.size();
            if (size6 > 0) {
                printWriter.print(str);
                printWriter.println("Pending Actions:");
                for (int i6 = 0; i6 < size6; i6++) {
                    OpGenerator opGenerator = (OpGenerator) this.mPendingActions.get(i6);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i6);
                    printWriter.print(": ");
                    printWriter.println(opGenerator);
                }
            }
        }
        printWriter.print(str);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(str);
        printWriter.print("  mHost=");
        printWriter.println(this.mHost);
        printWriter.print(str);
        printWriter.print("  mContainer=");
        printWriter.println(this.mContainer);
        if (this.mParent != null) {
            printWriter.print(str);
            printWriter.print("  mParent=");
            printWriter.println(this.mParent);
        }
        printWriter.print(str);
        printWriter.print("  mCurState=");
        printWriter.print(this.mCurState);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.mStateSaved);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            printWriter.print(str);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            printWriter.print(str);
            printWriter.print("  mNoTransactionsBecause=");
            printWriter.println(this.mNoTransactionsBecause);
        }
    }

    static AnimationOrAnimator makeOpenCloseAnimation(Context context, float f, float f2, float f3, float f4) {
        AnimationSet animationSet = new AnimationSet(false);
        ScaleAnimation scaleAnimation = new ScaleAnimation(f, f2, f, f2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(DECELERATE_QUINT);
        scaleAnimation.setDuration(220);
        animationSet.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(f3, f4);
        alphaAnimation.setInterpolator(DECELERATE_CUBIC);
        alphaAnimation.setDuration(220);
        animationSet.addAnimation(alphaAnimation);
        return new AnimationOrAnimator((Animation) animationSet);
    }

    static AnimationOrAnimator makeFadeAnimation(Context context, float f, float f2) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(f, f2);
        alphaAnimation.setInterpolator(DECELERATE_CUBIC);
        alphaAnimation.setDuration(220);
        return new AnimationOrAnimator((Animation) alphaAnimation);
    }

    /* access modifiers changed from: 0000 */
    public AnimationOrAnimator loadAnimation(Fragment fragment, int i, boolean z, int i2) {
        int nextAnim = fragment.getNextAnim();
        Animation onCreateAnimation = fragment.onCreateAnimation(i, z, nextAnim);
        if (onCreateAnimation != null) {
            return new AnimationOrAnimator(onCreateAnimation);
        }
        Animator onCreateAnimator = fragment.onCreateAnimator(i, z, nextAnim);
        if (onCreateAnimator != null) {
            return new AnimationOrAnimator(onCreateAnimator);
        }
        if (nextAnim != 0) {
            boolean equals = "anim".equals(this.mHost.getContext().getResources().getResourceTypeName(nextAnim));
            boolean z2 = false;
            if (equals) {
                try {
                    Animation loadAnimation = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (loadAnimation != null) {
                        return new AnimationOrAnimator(loadAnimation);
                    }
                    z2 = true;
                } catch (NotFoundException e) {
                    throw e;
                } catch (RuntimeException unused) {
                }
            }
            if (!z2) {
                try {
                    Animator loadAnimator = AnimatorInflater.loadAnimator(this.mHost.getContext(), nextAnim);
                    if (loadAnimator != null) {
                        return new AnimationOrAnimator(loadAnimator);
                    }
                } catch (RuntimeException e2) {
                    if (equals) {
                        throw e2;
                    }
                    Animation loadAnimation2 = AnimationUtils.loadAnimation(this.mHost.getContext(), nextAnim);
                    if (loadAnimation2 != null) {
                        return new AnimationOrAnimator(loadAnimation2);
                    }
                }
            }
        }
        if (i == 0) {
            return null;
        }
        int transitToStyleIndex = transitToStyleIndex(i, z);
        if (transitToStyleIndex < 0) {
            return null;
        }
        switch (transitToStyleIndex) {
            case 1:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            case 2:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            case 3:
                return makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            case 4:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            case 5:
                return makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            case 6:
                return makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            default:
                if (i2 == 0 && this.mHost.onHasWindowAnimations()) {
                    i2 = this.mHost.onGetWindowAnimations();
                }
                return i2 == 0 ? null : null;
        }
    }

    public void performPendingDeferredStart(Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            fragment.mDeferStart = false;
            moveToState(fragment, this.mCurState, 0, 0, false);
        }
    }

    private static void setHWLayerAnimListenerIfAlpha(View view, AnimationOrAnimator animationOrAnimator) {
        if (!(view == null || animationOrAnimator == null || !shouldRunOnHWLayer(view, animationOrAnimator))) {
            if (animationOrAnimator.animator != null) {
                animationOrAnimator.animator.addListener(new AnimatorOnHWLayerIfNeededListener(view));
            } else {
                AnimationListener animationListener = getAnimationListener(animationOrAnimator.animation);
                view.setLayerType(2, null);
                animationOrAnimator.animation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(view, animationListener));
            }
        }
    }

    private static AnimationListener getAnimationListener(Animation animation) {
        try {
            if (sAnimationListenerField == null) {
                sAnimationListenerField = Animation.class.getDeclaredField("mListener");
                sAnimationListenerField.setAccessible(true);
            }
            return (AnimationListener) sAnimationListenerField.get(animation);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "No field with the name mListener is found in Animation class", e);
            return null;
        } catch (IllegalAccessException e2) {
            Log.e(TAG, "Cannot access Animation's mListener field", e2);
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isStateAtLeast(int i) {
        return this.mCurState >= i;
    }

    /* JADX WARNING: type inference failed for: r8v0 */
    /* JADX WARNING: type inference failed for: r8v1, types: [int] */
    /* JADX WARNING: type inference failed for: r11v1 */
    /* JADX WARNING: type inference failed for: r8v2 */
    /* JADX WARNING: type inference failed for: r11v2 */
    /* JADX WARNING: type inference failed for: r11v3 */
    /* JADX WARNING: type inference failed for: r11v4 */
    /* JADX WARNING: type inference failed for: r11v5 */
    /* JADX WARNING: type inference failed for: r8v3, types: [boolean] */
    /* JADX WARNING: type inference failed for: r8v4 */
    /* JADX WARNING: type inference failed for: r11v6 */
    /* JADX WARNING: type inference failed for: r11v7 */
    /* JADX WARNING: type inference failed for: r8v5 */
    /* JADX WARNING: type inference failed for: r11v10 */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0234, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x0316, code lost:
        if (r11 >= 4) goto L_0x0338;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x031a, code lost:
        if (DEBUG == false) goto L_0x0332;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:0x031c, code lost:
        r0 = TAG;
        r1 = new java.lang.StringBuilder();
        r1.append("movefrom STARTED: ");
        r1.append(r7);
        android.util.Log.v(r0, r1.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x0332, code lost:
        r16.performStop();
        dispatchOnFragmentStopped(r7, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x0338, code lost:
        if (r11 >= 3) goto L_0x0357;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x033c, code lost:
        if (DEBUG == false) goto L_0x0354;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x033e, code lost:
        r0 = TAG;
        r1 = new java.lang.StringBuilder();
        r1.append("movefrom STOPPED: ");
        r1.append(r7);
        android.util.Log.v(r0, r1.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x0354, code lost:
        r16.performReallyStop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x0357, code lost:
        if (r11 >= 2) goto L_0x03d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x035b, code lost:
        if (DEBUG == false) goto L_0x0373;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x035d, code lost:
        r0 = TAG;
        r1 = new java.lang.StringBuilder();
        r1.append("movefrom ACTIVITY_CREATED: ");
        r1.append(r7);
        android.util.Log.v(r0, r1.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:157:0x0375, code lost:
        if (r7.mView == null) goto L_0x0386;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:159:0x037d, code lost:
        if (r6.mHost.onShouldSaveFragmentState(r7) == false) goto L_0x0386;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:161:0x0381, code lost:
        if (r7.mSavedViewState != null) goto L_0x0386;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x0383, code lost:
        saveFragmentViewState(r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x0386, code lost:
        r16.performDestroyView();
        dispatchOnFragmentViewDestroyed(r7, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x038e, code lost:
        if (r7.mView == null) goto L_0x03cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x0392, code lost:
        if (r7.mContainer == null) goto L_0x03cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x0394, code lost:
        r7.mView.clearAnimation();
        r7.mContainer.endViewTransition(r7.mView);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x03a3, code lost:
        if (r6.mCurState <= 0) goto L_0x03c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x03a7, code lost:
        if (r6.mDestroyed != false) goto L_0x03c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:0x03af, code lost:
        if (r7.mView.getVisibility() != 0) goto L_0x03c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:0x03b5, code lost:
        if (r7.mPostponedAlpha < 0.0f) goto L_0x03c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x03b7, code lost:
        r0 = loadAnimation(r7, r18, false, r19);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x03c0, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:0x03c1, code lost:
        r7.mPostponedAlpha = 0.0f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:178:0x03c3, code lost:
        if (r0 == null) goto L_0x03c8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:179:0x03c5, code lost:
        animateRemoveFragment(r7, r0, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:180:0x03c8, code lost:
        r7.mContainer.removeView(r7.mView);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:181:0x03cf, code lost:
        r7.mContainer = null;
        r7.mView = null;
        r7.mInnerView = null;
        r7.mInLayout = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x03d7, code lost:
        if (r11 >= 1) goto L_0x044d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:184:0x03db, code lost:
        if (r6.mDestroyed == false) goto L_0x03fe;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:186:0x03e1, code lost:
        if (r16.getAnimatingAway() == null) goto L_0x03ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:0x03e3, code lost:
        r0 = r16.getAnimatingAway();
        r7.setAnimatingAway(null);
        r0.clearAnimation();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:0x03f2, code lost:
        if (r16.getAnimator() == null) goto L_0x03fe;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x03f4, code lost:
        r0 = r16.getAnimator();
        r7.setAnimator(null);
        r0.cancel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:192:0x0402, code lost:
        if (r16.getAnimatingAway() != null) goto L_0x0449;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:194:0x0408, code lost:
        if (r16.getAnimator() == null) goto L_0x040b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:196:0x040d, code lost:
        if (DEBUG == false) goto L_0x0425;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:197:0x040f, code lost:
        r0 = TAG;
        r1 = new java.lang.StringBuilder();
        r1.append("movefrom CREATED: ");
        r1.append(r7);
        android.util.Log.v(r0, r1.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x0427, code lost:
        if (r7.mRetaining != false) goto L_0x0430;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:200:0x0429, code lost:
        r16.performDestroy();
        dispatchOnFragmentDestroyed(r7, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:201:0x0430, code lost:
        r7.mState = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:202:0x0432, code lost:
        r16.performDetach();
        dispatchOnFragmentDetached(r7, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:203:0x0438, code lost:
        if (r20 != false) goto L_0x044d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:205:0x043c, code lost:
        if (r7.mRetaining != false) goto L_0x0442;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:206:0x043e, code lost:
        makeInactive(r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:207:0x0442, code lost:
        r7.mHost = null;
        r7.mParentFragment = null;
        r7.mFragmentManager = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:208:0x0449, code lost:
        r7.setStateAfterAnimating(r11);
        r8 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x019d, code lost:
        ensureInflatedFragmentView(r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x01a0, code lost:
        if (r11 <= 1) goto L_0x029a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x01a4, code lost:
        if (DEBUG == false) goto L_0x01bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x01a6, code lost:
        r0 = TAG;
        r1 = new java.lang.StringBuilder();
        r1.append("moveto ACTIVITY_CREATED: ");
        r1.append(r7);
        android.util.Log.v(r0, r1.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x01be, code lost:
        if (r7.mFromLayout != false) goto L_0x0285;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x01c2, code lost:
        if (r7.mContainerId == 0) goto L_0x0234;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x01c7, code lost:
        if (r7.mContainerId != -1) goto L_0x01e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x01c9, code lost:
        r1 = new java.lang.StringBuilder();
        r1.append("Cannot create fragment ");
        r1.append(r7);
        r1.append(" for a container view with no id");
        throwException(new java.lang.IllegalArgumentException(r1.toString()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x01e7, code lost:
        r0 = (android.view.ViewGroup) r6.mContainer.onFindViewById(r7.mContainerId);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x01f1, code lost:
        if (r0 != null) goto L_0x0235;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x01f5, code lost:
        if (r7.mRestored != false) goto L_0x0235;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:?, code lost:
        r1 = r16.getResources().getResourceName(r7.mContainerId);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0202, code lost:
        r1 = "unknown";
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r11v6
      assigns: []
      uses: []
      mth insns count: 444
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:212:0x0452  */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void moveToState(android.support.p001v4.app.Fragment r16, int r17, int r18, int r19, boolean r20) {
        /*
            r15 = this;
            r6 = r15
            r7 = r16
            boolean r0 = r7.mAdded
            r8 = 1
            if (r0 == 0) goto L_0x0010
            boolean r0 = r7.mDetached
            if (r0 == 0) goto L_0x000d
            goto L_0x0010
        L_0x000d:
            r0 = r17
            goto L_0x0015
        L_0x0010:
            r0 = r17
            if (r0 <= r8) goto L_0x0015
            r0 = r8
        L_0x0015:
            boolean r1 = r7.mRemoving
            if (r1 == 0) goto L_0x002b
            int r1 = r7.mState
            if (r0 <= r1) goto L_0x002b
            int r0 = r7.mState
            if (r0 != 0) goto L_0x0029
            boolean r0 = r16.isInBackStack()
            if (r0 == 0) goto L_0x0029
            r0 = r8
            goto L_0x002b
        L_0x0029:
            int r0 = r7.mState
        L_0x002b:
            boolean r1 = r7.mDeferStart
            r9 = 4
            r10 = 3
            if (r1 == 0) goto L_0x0039
            int r1 = r7.mState
            if (r1 >= r9) goto L_0x0039
            if (r0 <= r10) goto L_0x0039
            r11 = r10
            goto L_0x003a
        L_0x0039:
            r11 = r0
        L_0x003a:
            int r0 = r7.mState
            r12 = 2
            r13 = 0
            r14 = 0
            if (r0 > r11) goto L_0x02e8
            boolean r0 = r7.mFromLayout
            if (r0 == 0) goto L_0x004a
            boolean r0 = r7.mInLayout
            if (r0 != 0) goto L_0x004a
            return
        L_0x004a:
            android.view.View r0 = r16.getAnimatingAway()
            if (r0 != 0) goto L_0x0056
            android.animation.Animator r0 = r16.getAnimator()
            if (r0 == 0) goto L_0x0068
        L_0x0056:
            r7.setAnimatingAway(r13)
            r7.setAnimator(r13)
            int r2 = r16.getStateAfterAnimating()
            r3 = 0
            r4 = 0
            r5 = 1
            r0 = r6
            r1 = r7
            r0.moveToState(r1, r2, r3, r4, r5)
        L_0x0068:
            int r0 = r7.mState
            switch(r0) {
                case 0: goto L_0x006f;
                case 1: goto L_0x019d;
                case 2: goto L_0x029a;
                case 3: goto L_0x029e;
                case 4: goto L_0x02c0;
                default: goto L_0x006d;
            }
        L_0x006d:
            goto L_0x044d
        L_0x006f:
            if (r11 <= 0) goto L_0x019d
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x008b
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto CREATED: "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x008b:
            android.os.Bundle r0 = r7.mSavedFragmentState
            if (r0 == 0) goto L_0x00d3
            android.os.Bundle r0 = r7.mSavedFragmentState
            android.support.v4.app.FragmentHostCallback r1 = r6.mHost
            android.content.Context r1 = r1.getContext()
            java.lang.ClassLoader r1 = r1.getClassLoader()
            r0.setClassLoader(r1)
            android.os.Bundle r0 = r7.mSavedFragmentState
            java.lang.String r1 = "android:view_state"
            android.util.SparseArray r0 = r0.getSparseParcelableArray(r1)
            r7.mSavedViewState = r0
            android.os.Bundle r0 = r7.mSavedFragmentState
            java.lang.String r1 = "android:target_state"
            android.support.v4.app.Fragment r0 = r6.getFragment(r0, r1)
            r7.mTarget = r0
            android.support.v4.app.Fragment r0 = r7.mTarget
            if (r0 == 0) goto L_0x00c0
            android.os.Bundle r0 = r7.mSavedFragmentState
            java.lang.String r1 = "android:target_req_state"
            int r0 = r0.getInt(r1, r14)
            r7.mTargetRequestCode = r0
        L_0x00c0:
            android.os.Bundle r0 = r7.mSavedFragmentState
            java.lang.String r1 = "android:user_visible_hint"
            boolean r0 = r0.getBoolean(r1, r8)
            r7.mUserVisibleHint = r0
            boolean r0 = r7.mUserVisibleHint
            if (r0 != 0) goto L_0x00d3
            r7.mDeferStart = r8
            if (r11 <= r10) goto L_0x00d3
            r11 = r10
        L_0x00d3:
            android.support.v4.app.FragmentHostCallback r0 = r6.mHost
            r7.mHost = r0
            android.support.v4.app.Fragment r0 = r6.mParent
            r7.mParentFragment = r0
            android.support.v4.app.Fragment r0 = r6.mParent
            if (r0 == 0) goto L_0x00e4
            android.support.v4.app.Fragment r0 = r6.mParent
            android.support.v4.app.FragmentManagerImpl r0 = r0.mChildFragmentManager
            goto L_0x00ea
        L_0x00e4:
            android.support.v4.app.FragmentHostCallback r0 = r6.mHost
            android.support.v4.app.FragmentManagerImpl r0 = r0.getFragmentManagerImpl()
        L_0x00ea:
            r7.mFragmentManager = r0
            android.support.v4.app.Fragment r0 = r7.mTarget
            if (r0 == 0) goto L_0x0134
            android.util.SparseArray<android.support.v4.app.Fragment> r0 = r6.mActive
            android.support.v4.app.Fragment r1 = r7.mTarget
            int r1 = r1.mIndex
            java.lang.Object r0 = r0.get(r1)
            android.support.v4.app.Fragment r1 = r7.mTarget
            if (r0 == r1) goto L_0x0124
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Fragment "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r2 = " declared target fragment "
            r1.append(r2)
            android.support.v4.app.Fragment r2 = r7.mTarget
            r1.append(r2)
            java.lang.String r2 = " that does not belong to this FragmentManager!"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0124:
            android.support.v4.app.Fragment r0 = r7.mTarget
            int r0 = r0.mState
            if (r0 >= r8) goto L_0x0134
            android.support.v4.app.Fragment r1 = r7.mTarget
            r2 = 1
            r3 = 0
            r4 = 0
            r5 = 1
            r0 = r6
            r0.moveToState(r1, r2, r3, r4, r5)
        L_0x0134:
            android.support.v4.app.FragmentHostCallback r0 = r6.mHost
            android.content.Context r0 = r0.getContext()
            r6.dispatchOnFragmentPreAttached(r7, r0, r14)
            r7.mCalled = r14
            android.support.v4.app.FragmentHostCallback r0 = r6.mHost
            android.content.Context r0 = r0.getContext()
            r7.onAttach(r0)
            boolean r0 = r7.mCalled
            if (r0 != 0) goto L_0x0168
            android.support.v4.app.SuperNotCalledException r0 = new android.support.v4.app.SuperNotCalledException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Fragment "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r2 = " did not call through to super.onAttach()"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L_0x0168:
            android.support.v4.app.Fragment r0 = r7.mParentFragment
            if (r0 != 0) goto L_0x0172
            android.support.v4.app.FragmentHostCallback r0 = r6.mHost
            r0.onAttachFragment(r7)
            goto L_0x0177
        L_0x0172:
            android.support.v4.app.Fragment r0 = r7.mParentFragment
            r0.onAttachFragment(r7)
        L_0x0177:
            android.support.v4.app.FragmentHostCallback r0 = r6.mHost
            android.content.Context r0 = r0.getContext()
            r6.dispatchOnFragmentAttached(r7, r0, r14)
            boolean r0 = r7.mIsCreated
            if (r0 != 0) goto L_0x0194
            android.os.Bundle r0 = r7.mSavedFragmentState
            r6.dispatchOnFragmentPreCreated(r7, r0, r14)
            android.os.Bundle r0 = r7.mSavedFragmentState
            r7.performCreate(r0)
            android.os.Bundle r0 = r7.mSavedFragmentState
            r6.dispatchOnFragmentCreated(r7, r0, r14)
            goto L_0x019b
        L_0x0194:
            android.os.Bundle r0 = r7.mSavedFragmentState
            r7.restoreChildFragmentState(r0)
            r7.mState = r8
        L_0x019b:
            r7.mRetaining = r14
        L_0x019d:
            r15.ensureInflatedFragmentView(r16)
            if (r11 <= r8) goto L_0x029a
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x01bc
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto ACTIVITY_CREATED: "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x01bc:
            boolean r0 = r7.mFromLayout
            if (r0 != 0) goto L_0x0285
            int r0 = r7.mContainerId
            if (r0 == 0) goto L_0x0234
            int r0 = r7.mContainerId
            r1 = -1
            if (r0 != r1) goto L_0x01e7
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Cannot create fragment "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r2 = " for a container view with no id"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            r6.throwException(r0)
        L_0x01e7:
            android.support.v4.app.FragmentContainer r0 = r6.mContainer
            int r1 = r7.mContainerId
            android.view.View r0 = r0.onFindViewById(r1)
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            if (r0 != 0) goto L_0x0235
            boolean r1 = r7.mRestored
            if (r1 != 0) goto L_0x0235
            android.content.res.Resources r1 = r16.getResources()     // Catch:{ NotFoundException -> 0x0202 }
            int r2 = r7.mContainerId     // Catch:{ NotFoundException -> 0x0202 }
            java.lang.String r1 = r1.getResourceName(r2)     // Catch:{ NotFoundException -> 0x0202 }
            goto L_0x0204
        L_0x0202:
            java.lang.String r1 = "unknown"
        L_0x0204:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "No view found for id 0x"
            r3.append(r4)
            int r4 = r7.mContainerId
            java.lang.String r4 = java.lang.Integer.toHexString(r4)
            r3.append(r4)
            java.lang.String r4 = " ("
            r3.append(r4)
            r3.append(r1)
            java.lang.String r1 = ") for fragment "
            r3.append(r1)
            r3.append(r7)
            java.lang.String r1 = r3.toString()
            r2.<init>(r1)
            r6.throwException(r2)
            goto L_0x0235
        L_0x0234:
            r0 = r13
        L_0x0235:
            r7.mContainer = r0
            android.os.Bundle r1 = r7.mSavedFragmentState
            android.view.LayoutInflater r1 = r7.performGetLayoutInflater(r1)
            android.os.Bundle r2 = r7.mSavedFragmentState
            android.view.View r1 = r7.performCreateView(r1, r0, r2)
            r7.mView = r1
            android.view.View r1 = r7.mView
            if (r1 == 0) goto L_0x0283
            android.view.View r1 = r7.mView
            r7.mInnerView = r1
            android.view.View r1 = r7.mView
            r1.setSaveFromParentEnabled(r14)
            if (r0 == 0) goto L_0x0259
            android.view.View r1 = r7.mView
            r0.addView(r1)
        L_0x0259:
            boolean r0 = r7.mHidden
            if (r0 == 0) goto L_0x0264
            android.view.View r0 = r7.mView
            r1 = 8
            r0.setVisibility(r1)
        L_0x0264:
            android.view.View r0 = r7.mView
            android.os.Bundle r1 = r7.mSavedFragmentState
            r7.onViewCreated(r0, r1)
            android.view.View r0 = r7.mView
            android.os.Bundle r1 = r7.mSavedFragmentState
            r6.dispatchOnFragmentViewCreated(r7, r0, r1, r14)
            android.view.View r0 = r7.mView
            int r0 = r0.getVisibility()
            if (r0 != 0) goto L_0x027f
            android.view.ViewGroup r0 = r7.mContainer
            if (r0 == 0) goto L_0x027f
            goto L_0x0280
        L_0x027f:
            r8 = r14
        L_0x0280:
            r7.mIsNewlyAdded = r8
            goto L_0x0285
        L_0x0283:
            r7.mInnerView = r13
        L_0x0285:
            android.os.Bundle r0 = r7.mSavedFragmentState
            r7.performActivityCreated(r0)
            android.os.Bundle r0 = r7.mSavedFragmentState
            r6.dispatchOnFragmentActivityCreated(r7, r0, r14)
            android.view.View r0 = r7.mView
            if (r0 == 0) goto L_0x0298
            android.os.Bundle r0 = r7.mSavedFragmentState
            r7.restoreViewState(r0)
        L_0x0298:
            r7.mSavedFragmentState = r13
        L_0x029a:
            if (r11 <= r12) goto L_0x029e
            r7.mState = r10
        L_0x029e:
            if (r11 <= r10) goto L_0x02c0
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x02ba
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto STARTED: "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x02ba:
            r16.performStart()
            r6.dispatchOnFragmentStarted(r7, r14)
        L_0x02c0:
            if (r11 <= r9) goto L_0x044d
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x02dc
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveto RESUMED: "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x02dc:
            r16.performResume()
            r6.dispatchOnFragmentResumed(r7, r14)
            r7.mSavedFragmentState = r13
            r7.mSavedViewState = r13
            goto L_0x044d
        L_0x02e8:
            int r0 = r7.mState
            if (r0 <= r11) goto L_0x044d
            int r0 = r7.mState
            switch(r0) {
                case 1: goto L_0x03d7;
                case 2: goto L_0x0357;
                case 3: goto L_0x0338;
                case 4: goto L_0x0316;
                case 5: goto L_0x02f3;
                default: goto L_0x02f1;
            }
        L_0x02f1:
            goto L_0x044d
        L_0x02f3:
            r0 = 5
            if (r11 >= r0) goto L_0x0316
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0310
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom RESUMED: "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x0310:
            r16.performPause()
            r6.dispatchOnFragmentPaused(r7, r14)
        L_0x0316:
            if (r11 >= r9) goto L_0x0338
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0332
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom STARTED: "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x0332:
            r16.performStop()
            r6.dispatchOnFragmentStopped(r7, r14)
        L_0x0338:
            if (r11 >= r10) goto L_0x0357
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0354
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom STOPPED: "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x0354:
            r16.performReallyStop()
        L_0x0357:
            if (r11 >= r12) goto L_0x03d7
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0373
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom ACTIVITY_CREATED: "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x0373:
            android.view.View r0 = r7.mView
            if (r0 == 0) goto L_0x0386
            android.support.v4.app.FragmentHostCallback r0 = r6.mHost
            boolean r0 = r0.onShouldSaveFragmentState(r7)
            if (r0 == 0) goto L_0x0386
            android.util.SparseArray<android.os.Parcelable> r0 = r7.mSavedViewState
            if (r0 != 0) goto L_0x0386
            r15.saveFragmentViewState(r16)
        L_0x0386:
            r16.performDestroyView()
            r6.dispatchOnFragmentViewDestroyed(r7, r14)
            android.view.View r0 = r7.mView
            if (r0 == 0) goto L_0x03cf
            android.view.ViewGroup r0 = r7.mContainer
            if (r0 == 0) goto L_0x03cf
            android.view.View r0 = r7.mView
            r0.clearAnimation()
            android.view.ViewGroup r0 = r7.mContainer
            android.view.View r1 = r7.mView
            r0.endViewTransition(r1)
            int r0 = r6.mCurState
            r1 = 0
            if (r0 <= 0) goto L_0x03c0
            boolean r0 = r6.mDestroyed
            if (r0 != 0) goto L_0x03c0
            android.view.View r0 = r7.mView
            int r0 = r0.getVisibility()
            if (r0 != 0) goto L_0x03c0
            float r0 = r7.mPostponedAlpha
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto L_0x03c0
            r0 = r18
            r2 = r19
            android.support.v4.app.FragmentManagerImpl$AnimationOrAnimator r0 = r6.loadAnimation(r7, r0, r14, r2)
            goto L_0x03c1
        L_0x03c0:
            r0 = r13
        L_0x03c1:
            r7.mPostponedAlpha = r1
            if (r0 == 0) goto L_0x03c8
            r6.animateRemoveFragment(r7, r0, r11)
        L_0x03c8:
            android.view.ViewGroup r0 = r7.mContainer
            android.view.View r1 = r7.mView
            r0.removeView(r1)
        L_0x03cf:
            r7.mContainer = r13
            r7.mView = r13
            r7.mInnerView = r13
            r7.mInLayout = r14
        L_0x03d7:
            if (r11 >= r8) goto L_0x044d
            boolean r0 = r6.mDestroyed
            if (r0 == 0) goto L_0x03fe
            android.view.View r0 = r16.getAnimatingAway()
            if (r0 == 0) goto L_0x03ee
            android.view.View r0 = r16.getAnimatingAway()
            r7.setAnimatingAway(r13)
            r0.clearAnimation()
            goto L_0x03fe
        L_0x03ee:
            android.animation.Animator r0 = r16.getAnimator()
            if (r0 == 0) goto L_0x03fe
            android.animation.Animator r0 = r16.getAnimator()
            r7.setAnimator(r13)
            r0.cancel()
        L_0x03fe:
            android.view.View r0 = r16.getAnimatingAway()
            if (r0 != 0) goto L_0x0449
            android.animation.Animator r0 = r16.getAnimator()
            if (r0 == 0) goto L_0x040b
            goto L_0x0449
        L_0x040b:
            boolean r0 = DEBUG
            if (r0 == 0) goto L_0x0425
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "movefrom CREATED: "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
        L_0x0425:
            boolean r0 = r7.mRetaining
            if (r0 != 0) goto L_0x0430
            r16.performDestroy()
            r6.dispatchOnFragmentDestroyed(r7, r14)
            goto L_0x0432
        L_0x0430:
            r7.mState = r14
        L_0x0432:
            r16.performDetach()
            r6.dispatchOnFragmentDetached(r7, r14)
            if (r20 != 0) goto L_0x044d
            boolean r0 = r7.mRetaining
            if (r0 != 0) goto L_0x0442
            r15.makeInactive(r16)
            goto L_0x044d
        L_0x0442:
            r7.mHost = r13
            r7.mParentFragment = r13
            r7.mFragmentManager = r13
            goto L_0x044d
        L_0x0449:
            r7.setStateAfterAnimating(r11)
            goto L_0x044e
        L_0x044d:
            r8 = r11
        L_0x044e:
            int r0 = r7.mState
            if (r0 == r8) goto L_0x0481
            java.lang.String r0 = "FragmentManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "moveToState: Fragment state for "
            r1.append(r2)
            r1.append(r7)
            java.lang.String r2 = " not updated inline; "
            r1.append(r2)
            java.lang.String r2 = "expected state "
            r1.append(r2)
            r1.append(r8)
            java.lang.String r2 = " found "
            r1.append(r2)
            int r2 = r7.mState
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.w(r0, r1)
            r7.mState = r8
        L_0x0481:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.app.FragmentManagerImpl.moveToState(android.support.v4.app.Fragment, int, int, int, boolean):void");
    }

    private void animateRemoveFragment(@NonNull final Fragment fragment, @NonNull AnimationOrAnimator animationOrAnimator, int i) {
        final View view = fragment.mView;
        fragment.setStateAfterAnimating(i);
        if (animationOrAnimator.animation != null) {
            Animation animation = animationOrAnimator.animation;
            fragment.setAnimatingAway(fragment.mView);
            animation.setAnimationListener(new AnimationListenerWrapper(getAnimationListener(animation)) {
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway(null);
                        FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                    }
                }
            });
            setHWLayerAnimListenerIfAlpha(view, animationOrAnimator);
            fragment.mView.startAnimation(animation);
            return;
        }
        Animator animator = animationOrAnimator.animator;
        fragment.setAnimator(animationOrAnimator.animator);
        final ViewGroup viewGroup = fragment.mContainer;
        if (viewGroup != null) {
            viewGroup.startViewTransition(view);
        }
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                if (viewGroup != null) {
                    viewGroup.endViewTransition(view);
                }
                if (fragment.getAnimator() != null) {
                    fragment.setAnimator(null);
                    FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                }
            }
        });
        animator.setTarget(fragment.mView);
        setHWLayerAnimListenerIfAlpha(fragment.mView, animationOrAnimator);
        animator.start();
    }

    /* access modifiers changed from: 0000 */
    public void moveToState(Fragment fragment) {
        moveToState(fragment, this.mCurState, 0, 0, false);
    }

    /* access modifiers changed from: 0000 */
    public void ensureInflatedFragmentView(Fragment fragment) {
        if (fragment.mFromLayout && !fragment.mPerformedCreateView) {
            fragment.mView = fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), null, fragment.mSavedFragmentState);
            if (fragment.mView != null) {
                fragment.mInnerView = fragment.mView;
                fragment.mView.setSaveFromParentEnabled(false);
                if (fragment.mHidden) {
                    fragment.mView.setVisibility(8);
                }
                fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
                return;
            }
            fragment.mInnerView = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            AnimationOrAnimator loadAnimation = loadAnimation(fragment, fragment.getNextTransition(), !fragment.mHidden, fragment.getNextTransitionStyle());
            if (loadAnimation == null || loadAnimation.animator == null) {
                if (loadAnimation != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                    fragment.mView.startAnimation(loadAnimation.animation);
                    loadAnimation.animation.start();
                }
                fragment.mView.setVisibility((!fragment.mHidden || fragment.isHideReplaced()) ? 0 : 8);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            } else {
                loadAnimation.animator.setTarget(fragment.mView);
                if (!fragment.mHidden) {
                    fragment.mView.setVisibility(0);
                } else if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                } else {
                    final ViewGroup viewGroup = fragment.mContainer;
                    final View view = fragment.mView;
                    viewGroup.startViewTransition(view);
                    loadAnimation.animator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            viewGroup.endViewTransition(view);
                            animator.removeListener(this);
                            if (fragment.mView != null) {
                                fragment.mView.setVisibility(8);
                            }
                        }
                    });
                }
                setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                loadAnimation.animator.start();
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    /* access modifiers changed from: 0000 */
    public void moveFragmentToExpectedState(Fragment fragment) {
        if (fragment != null) {
            int i = this.mCurState;
            if (fragment.mRemoving) {
                if (fragment.isInBackStack()) {
                    i = Math.min(i, 1);
                } else {
                    i = Math.min(i, 0);
                }
            }
            moveToState(fragment, i, fragment.getNextTransition(), fragment.getNextTransitionStyle(), false);
            if (fragment.mView != null) {
                Fragment findFragmentUnder = findFragmentUnder(fragment);
                if (findFragmentUnder != null) {
                    View view = findFragmentUnder.mView;
                    ViewGroup viewGroup = fragment.mContainer;
                    int indexOfChild = viewGroup.indexOfChild(view);
                    int indexOfChild2 = viewGroup.indexOfChild(fragment.mView);
                    if (indexOfChild2 < indexOfChild) {
                        viewGroup.removeViewAt(indexOfChild2);
                        viewGroup.addView(fragment.mView, indexOfChild);
                    }
                }
                if (fragment.mIsNewlyAdded && fragment.mContainer != null) {
                    if (fragment.mPostponedAlpha > 0.0f) {
                        fragment.mView.setAlpha(fragment.mPostponedAlpha);
                    }
                    fragment.mPostponedAlpha = 0.0f;
                    fragment.mIsNewlyAdded = false;
                    AnimationOrAnimator loadAnimation = loadAnimation(fragment, fragment.getNextTransition(), true, fragment.getNextTransitionStyle());
                    if (loadAnimation != null) {
                        setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                        if (loadAnimation.animation != null) {
                            fragment.mView.startAnimation(loadAnimation.animation);
                        } else {
                            loadAnimation.animator.setTarget(fragment.mView);
                            loadAnimation.animator.start();
                        }
                    }
                }
            }
            if (fragment.mHiddenChanged) {
                completeShowHideFragment(fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void moveToState(int i, boolean z) {
        if (this.mHost == null && i != 0) {
            throw new IllegalStateException("No activity");
        } else if (z || i != this.mCurState) {
            this.mCurState = i;
            if (this.mActive != null) {
                int size = this.mAdded.size();
                boolean z2 = false;
                for (int i2 = 0; i2 < size; i2++) {
                    Fragment fragment = (Fragment) this.mAdded.get(i2);
                    moveFragmentToExpectedState(fragment);
                    if (fragment.mLoaderManager != null) {
                        z2 |= fragment.mLoaderManager.hasRunningLoaders();
                    }
                }
                int size2 = this.mActive.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    Fragment fragment2 = (Fragment) this.mActive.valueAt(i3);
                    if (fragment2 != null && ((fragment2.mRemoving || fragment2.mDetached) && !fragment2.mIsNewlyAdded)) {
                        moveFragmentToExpectedState(fragment2);
                        if (fragment2.mLoaderManager != null) {
                            z2 |= fragment2.mLoaderManager.hasRunningLoaders();
                        }
                    }
                }
                if (!z2) {
                    startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
                    this.mHost.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void startPendingDeferredFragments() {
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment fragment = (Fragment) this.mActive.valueAt(i);
                if (fragment != null) {
                    performPendingDeferredStart(fragment);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void makeActive(Fragment fragment) {
        if (fragment.mIndex < 0) {
            int i = this.mNextFragmentIndex;
            this.mNextFragmentIndex = i + 1;
            fragment.setIndex(i, this.mParent);
            if (this.mActive == null) {
                this.mActive = new SparseArray<>();
            }
            this.mActive.put(fragment.mIndex, fragment);
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Allocated fragment index ");
                sb.append(fragment);
                Log.v(str, sb.toString());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void makeInactive(Fragment fragment) {
        if (fragment.mIndex >= 0) {
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Freeing fragment index ");
                sb.append(fragment);
                Log.v(str, sb.toString());
            }
            this.mActive.put(fragment.mIndex, null);
            this.mHost.inactivateFragment(fragment.mWho);
            fragment.initState();
        }
    }

    public void addFragment(Fragment fragment, boolean z) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("add: ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
        makeActive(fragment);
        if (fragment.mDetached) {
            return;
        }
        if (this.mAdded.contains(fragment)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Fragment already added: ");
            sb2.append(fragment);
            throw new IllegalStateException(sb2.toString());
        }
        synchronized (this.mAdded) {
            this.mAdded.add(fragment);
        }
        fragment.mAdded = true;
        fragment.mRemoving = false;
        if (fragment.mView == null) {
            fragment.mHiddenChanged = false;
        }
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        if (z) {
            moveToState(fragment);
        }
    }

    public void removeFragment(Fragment fragment) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("remove: ");
            sb.append(fragment);
            sb.append(" nesting=");
            sb.append(fragment.mBackStackNesting);
            Log.v(str, sb.toString());
        }
        boolean z = !fragment.isInBackStack();
        if (!fragment.mDetached || z) {
            synchronized (this.mAdded) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
        }
    }

    public void hideFragment(Fragment fragment) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("hide: ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
        }
    }

    public void showFragment(Fragment fragment) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("show: ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    public void detachFragment(Fragment fragment) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("detach: ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (DEBUG) {
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("remove from detach: ");
                    sb2.append(fragment);
                    Log.v(str2, sb2.toString());
                }
                synchronized (this.mAdded) {
                    this.mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = false;
            }
        }
    }

    public void attachFragment(Fragment fragment) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("attach: ");
            sb.append(fragment);
            Log.v(str, sb.toString());
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (fragment.mAdded) {
                return;
            }
            if (this.mAdded.contains(fragment)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Fragment already added: ");
                sb2.append(fragment);
                throw new IllegalStateException(sb2.toString());
            }
            if (DEBUG) {
                String str2 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("add from attach: ");
                sb3.append(fragment);
                Log.v(str2, sb3.toString());
            }
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
        }
    }

    public Fragment findFragmentById(int i) {
        for (int size = this.mAdded.size() - 1; size >= 0; size--) {
            Fragment fragment = (Fragment) this.mAdded.get(size);
            if (fragment != null && fragment.mFragmentId == i) {
                return fragment;
            }
        }
        if (this.mActive != null) {
            for (int size2 = this.mActive.size() - 1; size2 >= 0; size2--) {
                Fragment fragment2 = (Fragment) this.mActive.valueAt(size2);
                if (fragment2 != null && fragment2.mFragmentId == i) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByTag(String str) {
        if (str != null) {
            for (int size = this.mAdded.size() - 1; size >= 0; size--) {
                Fragment fragment = (Fragment) this.mAdded.get(size);
                if (fragment != null && str.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        if (!(this.mActive == null || str == null)) {
            for (int size2 = this.mActive.size() - 1; size2 >= 0; size2--) {
                Fragment fragment2 = (Fragment) this.mActive.valueAt(size2);
                if (fragment2 != null && str.equals(fragment2.mTag)) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String str) {
        if (!(this.mActive == null || str == null)) {
            for (int size = this.mActive.size() - 1; size >= 0; size--) {
                Fragment fragment = (Fragment) this.mActive.valueAt(size);
                if (fragment != null) {
                    Fragment findFragmentByWho = fragment.findFragmentByWho(str);
                    if (findFragmentByWho != null) {
                        return findFragmentByWho;
                    }
                }
            }
        }
        return null;
    }

    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        } else if (this.mNoTransactionsBecause != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can not perform this action inside of ");
            sb.append(this.mNoTransactionsBecause);
            throw new IllegalStateException(sb.toString());
        }
    }

    public boolean isStateSaved() {
        return this.mStateSaved;
    }

    public void enqueueAction(OpGenerator opGenerator, boolean z) {
        if (!z) {
            checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed) {
                if (this.mHost != null) {
                    if (this.mPendingActions == null) {
                        this.mPendingActions = new ArrayList<>();
                    }
                    this.mPendingActions.add(opGenerator);
                    scheduleCommit();
                    return;
                }
            }
            if (!z) {
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
    }

    /* access modifiers changed from: private */
    public void scheduleCommit() {
        synchronized (this) {
            boolean z = false;
            boolean z2 = this.mPostponedTransactions != null && !this.mPostponedTransactions.isEmpty();
            if (this.mPendingActions != null && this.mPendingActions.size() == 1) {
                z = true;
            }
            if (z2 || z) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
        }
    }

    public int allocBackStackIndex(BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mAvailBackStackIndices != null) {
                if (this.mAvailBackStackIndices.size() > 0) {
                    int intValue = ((Integer) this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1)).intValue();
                    if (DEBUG) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Adding back stack index ");
                        sb.append(intValue);
                        sb.append(" with ");
                        sb.append(backStackRecord);
                        Log.v(str, sb.toString());
                    }
                    this.mBackStackIndices.set(intValue, backStackRecord);
                    return intValue;
                }
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<>();
            }
            int size = this.mBackStackIndices.size();
            if (DEBUG) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Setting back stack index ");
                sb2.append(size);
                sb2.append(" to ");
                sb2.append(backStackRecord);
                Log.v(str2, sb2.toString());
            }
            this.mBackStackIndices.add(backStackRecord);
            return size;
        }
    }

    public void setBackStackIndex(int i, BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<>();
            }
            int size = this.mBackStackIndices.size();
            if (i < size) {
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Setting back stack index ");
                    sb.append(i);
                    sb.append(" to ");
                    sb.append(backStackRecord);
                    Log.v(str, sb.toString());
                }
                this.mBackStackIndices.set(i, backStackRecord);
            } else {
                while (size < i) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList<>();
                    }
                    if (DEBUG) {
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Adding available back stack index ");
                        sb2.append(size);
                        Log.v(str2, sb2.toString());
                    }
                    this.mAvailBackStackIndices.add(Integer.valueOf(size));
                    size++;
                }
                if (DEBUG) {
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Adding back stack index ");
                    sb3.append(i);
                    sb3.append(" with ");
                    sb3.append(backStackRecord);
                    Log.v(str3, sb3.toString());
                }
                this.mBackStackIndices.add(backStackRecord);
            }
        }
    }

    public void freeBackStackIndex(int i) {
        synchronized (this) {
            this.mBackStackIndices.set(i, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<>();
            }
            if (DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Freeing back stack index ");
                sb.append(i);
                Log.v(str, sb.toString());
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(i));
        }
    }

    private void ensureExecReady(boolean z) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        } else {
            if (!z) {
                checkStateLoss();
            }
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new ArrayList<>();
                this.mTmpIsPop = new ArrayList<>();
            }
            this.mExecutingActions = true;
            try {
                executePostponedTransaction(null, null);
            } finally {
                this.mExecutingActions = false;
            }
        }
    }

    public void execSingleAction(OpGenerator opGenerator, boolean z) {
        if (!z || (this.mHost != null && !this.mDestroyed)) {
            ensureExecReady(z);
            if (opGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
                this.mExecutingActions = true;
                try {
                    removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                } finally {
                    cleanupExec();
                }
            }
            doPendingDeferredStart();
            burpActive();
        }
    }

    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    /* JADX INFO: finally extract failed */
    public boolean execPendingActions() {
        ensureExecReady(true);
        boolean z = false;
        while (generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                removeRedundantOperationsAndExecute(this.mTmpRecords, this.mTmpIsPop);
                cleanupExec();
                z = true;
            } catch (Throwable th) {
                cleanupExec();
                throw th;
            }
        }
        doPendingDeferredStart();
        burpActive();
        return z;
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        int size = this.mPostponedTransactions == null ? 0 : this.mPostponedTransactions.size();
        int i = 0;
        while (i < size) {
            StartEnterTransitionListener startEnterTransitionListener = (StartEnterTransitionListener) this.mPostponedTransactions.get(i);
            if (arrayList != null && !startEnterTransitionListener.mIsBack) {
                int indexOf = arrayList.indexOf(startEnterTransitionListener.mRecord);
                if (indexOf != -1 && ((Boolean) arrayList2.get(indexOf)).booleanValue()) {
                    startEnterTransitionListener.cancelTransaction();
                    i++;
                }
            }
            if (startEnterTransitionListener.isReady() || (arrayList != null && startEnterTransitionListener.mRecord.interactsWith(arrayList, 0, arrayList.size()))) {
                this.mPostponedTransactions.remove(i);
                i--;
                size--;
                if (arrayList != null && !startEnterTransitionListener.mIsBack) {
                    int indexOf2 = arrayList.indexOf(startEnterTransitionListener.mRecord);
                    if (indexOf2 != -1 && ((Boolean) arrayList2.get(indexOf2)).booleanValue()) {
                        startEnterTransitionListener.cancelTransaction();
                    }
                }
                startEnterTransitionListener.completeTransaction();
            }
            i++;
        }
    }

    private void removeRedundantOperationsAndExecute(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (arrayList != null && !arrayList.isEmpty()) {
            if (arrayList2 == null || arrayList.size() != arrayList2.size()) {
                throw new IllegalStateException("Internal error with the back stack records");
            }
            executePostponedTransaction(arrayList, arrayList2);
            int size = arrayList.size();
            int i = 0;
            int i2 = 0;
            while (i < size) {
                if (!((BackStackRecord) arrayList.get(i)).mReorderingAllowed) {
                    if (i2 != i) {
                        executeOpsTogether(arrayList, arrayList2, i2, i);
                    }
                    i2 = i + 1;
                    if (((Boolean) arrayList2.get(i)).booleanValue()) {
                        while (i2 < size && ((Boolean) arrayList2.get(i2)).booleanValue() && !((BackStackRecord) arrayList.get(i2)).mReorderingAllowed) {
                            i2++;
                        }
                    }
                    executeOpsTogether(arrayList, arrayList2, i, i2);
                    i = i2 - 1;
                }
                i++;
            }
            if (i2 != size) {
                executeOpsTogether(arrayList, arrayList2, i2, size);
            }
        }
    }

    private void executeOpsTogether(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i, int i2) {
        int i3;
        ArrayList<BackStackRecord> arrayList3 = arrayList;
        ArrayList<Boolean> arrayList4 = arrayList2;
        int i4 = i;
        int i5 = i2;
        boolean z = ((BackStackRecord) arrayList3.get(i4)).mReorderingAllowed;
        if (this.mTmpAddedFragments == null) {
            this.mTmpAddedFragments = new ArrayList<>();
        } else {
            this.mTmpAddedFragments.clear();
        }
        this.mTmpAddedFragments.addAll(this.mAdded);
        Fragment primaryNavigationFragment = getPrimaryNavigationFragment();
        boolean z2 = false;
        for (int i6 = i4; i6 < i5; i6++) {
            BackStackRecord backStackRecord = (BackStackRecord) arrayList3.get(i6);
            if (!((Boolean) arrayList4.get(i6)).booleanValue()) {
                primaryNavigationFragment = backStackRecord.expandOps(this.mTmpAddedFragments, primaryNavigationFragment);
            } else {
                primaryNavigationFragment = backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, primaryNavigationFragment);
            }
            z2 = z2 || backStackRecord.mAddToBackStack;
        }
        this.mTmpAddedFragments.clear();
        if (!z) {
            FragmentTransition.startTransitions(this, arrayList3, arrayList4, i4, i5, false);
        }
        executeOps(arrayList, arrayList2, i, i2);
        if (z) {
            ArraySet arraySet = new ArraySet();
            addAddedFragments(arraySet);
            int postponePostponableTransactions = postponePostponableTransactions(arrayList3, arrayList4, i4, i5, arraySet);
            makeRemovedFragmentsInvisible(arraySet);
            i3 = postponePostponableTransactions;
        } else {
            i3 = i5;
        }
        if (i3 != i4 && z) {
            FragmentTransition.startTransitions(this, arrayList3, arrayList4, i4, i3, true);
            moveToState(this.mCurState, true);
        }
        while (i4 < i5) {
            BackStackRecord backStackRecord2 = (BackStackRecord) arrayList3.get(i4);
            if (((Boolean) arrayList4.get(i4)).booleanValue() && backStackRecord2.mIndex >= 0) {
                freeBackStackIndex(backStackRecord2.mIndex);
                backStackRecord2.mIndex = -1;
            }
            backStackRecord2.runOnCommitRunnables();
            i4++;
        }
        if (z2) {
            reportBackStackChanged();
        }
    }

    private void makeRemovedFragmentsInvisible(ArraySet<Fragment> arraySet) {
        int size = arraySet.size();
        for (int i = 0; i < size; i++) {
            Fragment fragment = (Fragment) arraySet.valueAt(i);
            if (!fragment.mAdded) {
                View view = fragment.getView();
                fragment.mPostponedAlpha = view.getAlpha();
                view.setAlpha(0.0f);
            }
        }
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i, int i2, ArraySet<Fragment> arraySet) {
        int i3 = i2;
        for (int i4 = i2 - 1; i4 >= i; i4--) {
            BackStackRecord backStackRecord = (BackStackRecord) arrayList.get(i4);
            boolean booleanValue = ((Boolean) arrayList2.get(i4)).booleanValue();
            if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(arrayList, i4 + 1, i2)) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<>();
                }
                StartEnterTransitionListener startEnterTransitionListener = new StartEnterTransitionListener(backStackRecord, booleanValue);
                this.mPostponedTransactions.add(startEnterTransitionListener);
                backStackRecord.setOnStartPostponedListener(startEnterTransitionListener);
                if (booleanValue) {
                    backStackRecord.executeOps();
                } else {
                    backStackRecord.executePopOps(false);
                }
                i3--;
                if (i4 != i3) {
                    arrayList.remove(i4);
                    arrayList.add(i3, backStackRecord);
                }
                addAddedFragments(arraySet);
            }
        }
        return i3;
    }

    /* access modifiers changed from: private */
    public void completeExecute(BackStackRecord backStackRecord, boolean z, boolean z2, boolean z3) {
        if (z) {
            backStackRecord.executePopOps(z3);
        } else {
            backStackRecord.executeOps();
        }
        ArrayList arrayList = new ArrayList(1);
        ArrayList arrayList2 = new ArrayList(1);
        arrayList.add(backStackRecord);
        arrayList2.add(Boolean.valueOf(z));
        if (z2) {
            FragmentTransition.startTransitions(this, arrayList, arrayList2, 0, 1, true);
        }
        if (z3) {
            moveToState(this.mCurState, true);
        }
        if (this.mActive != null) {
            int size = this.mActive.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = (Fragment) this.mActive.valueAt(i);
                if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && backStackRecord.interactsWith(fragment.mContainerId)) {
                    if (fragment.mPostponedAlpha > 0.0f) {
                        fragment.mView.setAlpha(fragment.mPostponedAlpha);
                    }
                    if (z3) {
                        fragment.mPostponedAlpha = 0.0f;
                    } else {
                        fragment.mPostponedAlpha = -1.0f;
                        fragment.mIsNewlyAdded = false;
                    }
                }
            }
        }
    }

    private Fragment findFragmentUnder(Fragment fragment) {
        ViewGroup viewGroup = fragment.mContainer;
        View view = fragment.mView;
        if (viewGroup == null || view == null) {
            return null;
        }
        for (int indexOf = this.mAdded.indexOf(fragment) - 1; indexOf >= 0; indexOf--) {
            Fragment fragment2 = (Fragment) this.mAdded.get(indexOf);
            if (fragment2.mContainer == viewGroup && fragment2.mView != null) {
                return fragment2;
            }
        }
        return null;
    }

    private static void executeOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i, int i2) {
        while (i < i2) {
            BackStackRecord backStackRecord = (BackStackRecord) arrayList.get(i);
            boolean z = true;
            if (((Boolean) arrayList2.get(i)).booleanValue()) {
                backStackRecord.bumpBackStackNesting(-1);
                if (i != i2 - 1) {
                    z = false;
                }
                backStackRecord.executePopOps(z);
            } else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
            i++;
        }
    }

    private void addAddedFragments(ArraySet<Fragment> arraySet) {
        if (this.mCurState >= 1) {
            int min = Math.min(this.mCurState, 4);
            int size = this.mAdded.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment.mState < min) {
                    moveToState(fragment, min, fragment.getNextAnim(), fragment.getNextTransition(), false);
                    if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                        arraySet.add(fragment);
                    }
                }
            }
        }
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                ((StartEnterTransitionListener) this.mPostponedTransactions.remove(0)).completeTransaction();
            }
        }
    }

    private void endAnimatingAwayFragments() {
        int size = this.mActive == null ? 0 : this.mActive.size();
        for (int i = 0; i < size; i++) {
            Fragment fragment = (Fragment) this.mActive.valueAt(i);
            if (fragment != null) {
                if (fragment.getAnimatingAway() != null) {
                    int stateAfterAnimating = fragment.getStateAfterAnimating();
                    View animatingAway = fragment.getAnimatingAway();
                    fragment.setAnimatingAway(null);
                    Animation animation = animatingAway.getAnimation();
                    if (animation != null) {
                        animation.cancel();
                        animatingAway.clearAnimation();
                    }
                    moveToState(fragment, stateAfterAnimating, 0, 0, false);
                } else if (fragment.getAnimator() != null) {
                    fragment.getAnimator().end();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003b, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean generateOpsForPendingActions(java.util.ArrayList<android.support.p001v4.app.BackStackRecord> r5, java.util.ArrayList<java.lang.Boolean> r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r0 = r4.mPendingActions     // Catch:{ all -> 0x003c }
            r1 = 0
            if (r0 == 0) goto L_0x003a
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r0 = r4.mPendingActions     // Catch:{ all -> 0x003c }
            int r0 = r0.size()     // Catch:{ all -> 0x003c }
            if (r0 != 0) goto L_0x000f
            goto L_0x003a
        L_0x000f:
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r0 = r4.mPendingActions     // Catch:{ all -> 0x003c }
            int r0 = r0.size()     // Catch:{ all -> 0x003c }
            r2 = r1
        L_0x0016:
            if (r1 >= r0) goto L_0x0028
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r3 = r4.mPendingActions     // Catch:{ all -> 0x003c }
            java.lang.Object r3 = r3.get(r1)     // Catch:{ all -> 0x003c }
            android.support.v4.app.FragmentManagerImpl$OpGenerator r3 = (android.support.p001v4.app.FragmentManagerImpl.OpGenerator) r3     // Catch:{ all -> 0x003c }
            boolean r3 = r3.generateOps(r5, r6)     // Catch:{ all -> 0x003c }
            r2 = r2 | r3
            int r1 = r1 + 1
            goto L_0x0016
        L_0x0028:
            java.util.ArrayList<android.support.v4.app.FragmentManagerImpl$OpGenerator> r5 = r4.mPendingActions     // Catch:{ all -> 0x003c }
            r5.clear()     // Catch:{ all -> 0x003c }
            android.support.v4.app.FragmentHostCallback r5 = r4.mHost     // Catch:{ all -> 0x003c }
            android.os.Handler r5 = r5.getHandler()     // Catch:{ all -> 0x003c }
            java.lang.Runnable r6 = r4.mExecCommit     // Catch:{ all -> 0x003c }
            r5.removeCallbacks(r6)     // Catch:{ all -> 0x003c }
            monitor-exit(r4)     // Catch:{ all -> 0x003c }
            return r2
        L_0x003a:
            monitor-exit(r4)     // Catch:{ all -> 0x003c }
            return r1
        L_0x003c:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003c }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.app.FragmentManagerImpl.generateOpsForPendingActions(java.util.ArrayList, java.util.ArrayList):boolean");
    }

    /* access modifiers changed from: 0000 */
    public void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            boolean z = false;
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment fragment = (Fragment) this.mActive.valueAt(i);
                if (!(fragment == null || fragment.mLoaderManager == null)) {
                    z |= fragment.mLoaderManager.hasRunningLoaders();
                }
            }
            if (!z) {
                this.mHavePendingDeferredStart = false;
                startPendingDeferredFragments();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i++) {
                ((OnBackStackChangedListener) this.mBackStackChangeListeners.get(i)).onBackStackChanged();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void addBackStackState(BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<>();
        }
        this.mBackStack.add(backStackRecord);
    }

    /* access modifiers changed from: 0000 */
    public boolean popBackStackState(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, String str, int i, int i2) {
        int i3;
        if (this.mBackStack == null) {
            return false;
        }
        if (str == null && i < 0 && (i2 & 1) == 0) {
            int size = this.mBackStack.size() - 1;
            if (size < 0) {
                return false;
            }
            arrayList.add(this.mBackStack.remove(size));
            arrayList2.add(Boolean.valueOf(true));
        } else {
            if (str != null || i >= 0) {
                int size2 = this.mBackStack.size() - 1;
                while (i3 >= 0) {
                    BackStackRecord backStackRecord = (BackStackRecord) this.mBackStack.get(i3);
                    if ((str != null && str.equals(backStackRecord.getName())) || (i >= 0 && i == backStackRecord.mIndex)) {
                        break;
                    }
                    size2 = i3 - 1;
                }
                if (i3 < 0) {
                    return false;
                }
                if ((i2 & 1) != 0) {
                    i3--;
                    while (i3 >= 0) {
                        BackStackRecord backStackRecord2 = (BackStackRecord) this.mBackStack.get(i3);
                        if ((str == null || !str.equals(backStackRecord2.getName())) && (i < 0 || i != backStackRecord2.mIndex)) {
                            break;
                        }
                        i3--;
                    }
                }
            } else {
                i3 = -1;
            }
            if (i3 == this.mBackStack.size() - 1) {
                return false;
            }
            for (int size3 = this.mBackStack.size() - 1; size3 > i3; size3--) {
                arrayList.add(this.mBackStack.remove(size3));
                arrayList2.add(Boolean.valueOf(true));
            }
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public FragmentManagerNonConfig retainNonConfig() {
        setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }

    private static void setRetaining(FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (fragmentManagerNonConfig != null) {
            List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    fragment.mRetaining = true;
                }
            }
            List<FragmentManagerNonConfig> childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
            if (childNonConfigs != null) {
                for (FragmentManagerNonConfig retaining : childNonConfigs) {
                    setRetaining(retaining);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void saveNonConfig() {
        ArrayList arrayList;
        ArrayList arrayList2;
        FragmentManagerNonConfig fragmentManagerNonConfig;
        if (this.mActive != null) {
            arrayList2 = null;
            arrayList = null;
            for (int i = 0; i < this.mActive.size(); i++) {
                Fragment fragment = (Fragment) this.mActive.valueAt(i);
                if (fragment != null) {
                    if (fragment.mRetainInstance) {
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList();
                        }
                        arrayList2.add(fragment);
                        fragment.mTargetIndex = fragment.mTarget != null ? fragment.mTarget.mIndex : -1;
                        if (DEBUG) {
                            String str = TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("retainNonConfig: keeping retained ");
                            sb.append(fragment);
                            Log.v(str, sb.toString());
                        }
                    }
                    if (fragment.mChildFragmentManager != null) {
                        fragment.mChildFragmentManager.saveNonConfig();
                        fragmentManagerNonConfig = fragment.mChildFragmentManager.mSavedNonConfig;
                    } else {
                        fragmentManagerNonConfig = fragment.mChildNonConfig;
                    }
                    if (arrayList == null && fragmentManagerNonConfig != null) {
                        arrayList = new ArrayList(this.mActive.size());
                        for (int i2 = 0; i2 < i; i2++) {
                            arrayList.add(null);
                        }
                    }
                    if (arrayList != null) {
                        arrayList.add(fragmentManagerNonConfig);
                    }
                }
            }
        } else {
            arrayList2 = null;
            arrayList = null;
        }
        if (arrayList2 == null && arrayList == null) {
            this.mSavedNonConfig = null;
        } else {
            this.mSavedNonConfig = new FragmentManagerNonConfig(arrayList2, arrayList);
        }
    }

    /* access modifiers changed from: 0000 */
    public void saveFragmentViewState(Fragment fragment) {
        if (fragment.mInnerView != null) {
            if (this.mStateArray == null) {
                this.mStateArray = new SparseArray<>();
            } else {
                this.mStateArray.clear();
            }
            fragment.mInnerView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                fragment.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public Bundle saveFragmentBasicState(Fragment fragment) {
        Bundle bundle;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        dispatchOnFragmentSaveInstanceState(fragment, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            bundle = this.mStateBundle;
            this.mStateBundle = null;
        } else {
            bundle = null;
        }
        if (fragment.mView != null) {
            saveFragmentViewState(fragment);
        }
        if (fragment.mSavedViewState != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray(VIEW_STATE_TAG, fragment.mSavedViewState);
        }
        if (!fragment.mUserVisibleHint) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean(USER_VISIBLE_HINT_TAG, fragment.mUserVisibleHint);
        }
        return bundle;
    }

    /* access modifiers changed from: 0000 */
    public Parcelable saveAllState() {
        int[] iArr;
        forcePostponedTransactions();
        endAnimatingAwayFragments();
        execPendingActions();
        this.mStateSaved = true;
        BackStackState[] backStackStateArr = null;
        this.mSavedNonConfig = null;
        if (this.mActive == null || this.mActive.size() <= 0) {
            return null;
        }
        int size = this.mActive.size();
        FragmentState[] fragmentStateArr = new FragmentState[size];
        boolean z = false;
        for (int i = 0; i < size; i++) {
            Fragment fragment = (Fragment) this.mActive.valueAt(i);
            if (fragment != null) {
                if (fragment.mIndex < 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failure saving state: active ");
                    sb.append(fragment);
                    sb.append(" has cleared index: ");
                    sb.append(fragment.mIndex);
                    throwException(new IllegalStateException(sb.toString()));
                }
                FragmentState fragmentState = new FragmentState(fragment);
                fragmentStateArr[i] = fragmentState;
                if (fragment.mState <= 0 || fragmentState.mSavedFragmentState != null) {
                    fragmentState.mSavedFragmentState = fragment.mSavedFragmentState;
                } else {
                    fragmentState.mSavedFragmentState = saveFragmentBasicState(fragment);
                    if (fragment.mTarget != null) {
                        if (fragment.mTarget.mIndex < 0) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Failure saving state: ");
                            sb2.append(fragment);
                            sb2.append(" has target not in fragment manager: ");
                            sb2.append(fragment.mTarget);
                            throwException(new IllegalStateException(sb2.toString()));
                        }
                        if (fragmentState.mSavedFragmentState == null) {
                            fragmentState.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fragmentState.mSavedFragmentState, TARGET_STATE_TAG, fragment.mTarget);
                        if (fragment.mTargetRequestCode != 0) {
                            fragmentState.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, fragment.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Saved state of ");
                    sb3.append(fragment);
                    sb3.append(": ");
                    sb3.append(fragmentState.mSavedFragmentState);
                    Log.v(str, sb3.toString());
                }
                z = true;
            }
        }
        if (!z) {
            if (DEBUG) {
                Log.v(TAG, "saveAllState: no fragments!");
            }
            return null;
        }
        int size2 = this.mAdded.size();
        if (size2 > 0) {
            iArr = new int[size2];
            for (int i2 = 0; i2 < size2; i2++) {
                iArr[i2] = ((Fragment) this.mAdded.get(i2)).mIndex;
                if (iArr[i2] < 0) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Failure saving state: active ");
                    sb4.append(this.mAdded.get(i2));
                    sb4.append(" has cleared index: ");
                    sb4.append(iArr[i2]);
                    throwException(new IllegalStateException(sb4.toString()));
                }
                if (DEBUG) {
                    String str2 = TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("saveAllState: adding fragment #");
                    sb5.append(i2);
                    sb5.append(": ");
                    sb5.append(this.mAdded.get(i2));
                    Log.v(str2, sb5.toString());
                }
            }
        } else {
            iArr = null;
        }
        if (this.mBackStack != null) {
            int size3 = this.mBackStack.size();
            if (size3 > 0) {
                backStackStateArr = new BackStackState[size3];
                for (int i3 = 0; i3 < size3; i3++) {
                    backStackStateArr[i3] = new BackStackState((BackStackRecord) this.mBackStack.get(i3));
                    if (DEBUG) {
                        String str3 = TAG;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("saveAllState: adding back stack #");
                        sb6.append(i3);
                        sb6.append(": ");
                        sb6.append(this.mBackStack.get(i3));
                        Log.v(str3, sb6.toString());
                    }
                }
            }
        }
        FragmentManagerState fragmentManagerState = new FragmentManagerState();
        fragmentManagerState.mActive = fragmentStateArr;
        fragmentManagerState.mAdded = iArr;
        fragmentManagerState.mBackStack = backStackStateArr;
        if (this.mPrimaryNav != null) {
            fragmentManagerState.mPrimaryNavActiveIndex = this.mPrimaryNav.mIndex;
        }
        fragmentManagerState.mNextFragmentIndex = this.mNextFragmentIndex;
        saveNonConfig();
        return fragmentManagerState;
    }

    /* access modifiers changed from: 0000 */
    public void restoreAllState(Parcelable parcelable, FragmentManagerNonConfig fragmentManagerNonConfig) {
        List list;
        if (parcelable != null) {
            FragmentManagerState fragmentManagerState = (FragmentManagerState) parcelable;
            if (fragmentManagerState.mActive != null) {
                if (fragmentManagerNonConfig != null) {
                    List fragments = fragmentManagerNonConfig.getFragments();
                    list = fragmentManagerNonConfig.getChildNonConfigs();
                    int size = fragments != null ? fragments.size() : 0;
                    for (int i = 0; i < size; i++) {
                        Fragment fragment = (Fragment) fragments.get(i);
                        if (DEBUG) {
                            String str = TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("restoreAllState: re-attaching retained ");
                            sb.append(fragment);
                            Log.v(str, sb.toString());
                        }
                        int i2 = 0;
                        while (i2 < fragmentManagerState.mActive.length && fragmentManagerState.mActive[i2].mIndex != fragment.mIndex) {
                            i2++;
                        }
                        if (i2 == fragmentManagerState.mActive.length) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Could not find active fragment with index ");
                            sb2.append(fragment.mIndex);
                            throwException(new IllegalStateException(sb2.toString()));
                        }
                        FragmentState fragmentState = fragmentManagerState.mActive[i2];
                        fragmentState.mInstance = fragment;
                        fragment.mSavedViewState = null;
                        fragment.mBackStackNesting = 0;
                        fragment.mInLayout = false;
                        fragment.mAdded = false;
                        fragment.mTarget = null;
                        if (fragmentState.mSavedFragmentState != null) {
                            fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            fragment.mSavedViewState = fragmentState.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                            fragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
                        }
                    }
                } else {
                    list = null;
                }
                this.mActive = new SparseArray<>(fragmentManagerState.mActive.length);
                int i3 = 0;
                while (i3 < fragmentManagerState.mActive.length) {
                    FragmentState fragmentState2 = fragmentManagerState.mActive[i3];
                    if (fragmentState2 != null) {
                        Fragment instantiate = fragmentState2.instantiate(this.mHost, this.mContainer, this.mParent, (list == null || i3 >= list.size()) ? null : (FragmentManagerNonConfig) list.get(i3));
                        if (DEBUG) {
                            String str2 = TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("restoreAllState: active #");
                            sb3.append(i3);
                            sb3.append(": ");
                            sb3.append(instantiate);
                            Log.v(str2, sb3.toString());
                        }
                        this.mActive.put(instantiate.mIndex, instantiate);
                        fragmentState2.mInstance = null;
                    }
                    i3++;
                }
                if (fragmentManagerNonConfig != null) {
                    List fragments2 = fragmentManagerNonConfig.getFragments();
                    int size2 = fragments2 != null ? fragments2.size() : 0;
                    for (int i4 = 0; i4 < size2; i4++) {
                        Fragment fragment2 = (Fragment) fragments2.get(i4);
                        if (fragment2.mTargetIndex >= 0) {
                            fragment2.mTarget = (Fragment) this.mActive.get(fragment2.mTargetIndex);
                            if (fragment2.mTarget == null) {
                                String str3 = TAG;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("Re-attaching retained fragment ");
                                sb4.append(fragment2);
                                sb4.append(" target no longer exists: ");
                                sb4.append(fragment2.mTargetIndex);
                                Log.w(str3, sb4.toString());
                            }
                        }
                    }
                }
                this.mAdded.clear();
                if (fragmentManagerState.mAdded != null) {
                    for (int i5 = 0; i5 < fragmentManagerState.mAdded.length; i5++) {
                        Fragment fragment3 = (Fragment) this.mActive.get(fragmentManagerState.mAdded[i5]);
                        if (fragment3 == null) {
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("No instantiated fragment for index #");
                            sb5.append(fragmentManagerState.mAdded[i5]);
                            throwException(new IllegalStateException(sb5.toString()));
                        }
                        fragment3.mAdded = true;
                        if (DEBUG) {
                            String str4 = TAG;
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("restoreAllState: added #");
                            sb6.append(i5);
                            sb6.append(": ");
                            sb6.append(fragment3);
                            Log.v(str4, sb6.toString());
                        }
                        if (this.mAdded.contains(fragment3)) {
                            throw new IllegalStateException("Already added!");
                        }
                        synchronized (this.mAdded) {
                            this.mAdded.add(fragment3);
                        }
                    }
                }
                if (fragmentManagerState.mBackStack != null) {
                    this.mBackStack = new ArrayList<>(fragmentManagerState.mBackStack.length);
                    for (int i6 = 0; i6 < fragmentManagerState.mBackStack.length; i6++) {
                        BackStackRecord instantiate2 = fragmentManagerState.mBackStack[i6].instantiate(this);
                        if (DEBUG) {
                            String str5 = TAG;
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("restoreAllState: back stack #");
                            sb7.append(i6);
                            sb7.append(" (index ");
                            sb7.append(instantiate2.mIndex);
                            sb7.append("): ");
                            sb7.append(instantiate2);
                            Log.v(str5, sb7.toString());
                            PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
                            instantiate2.dump("  ", printWriter, false);
                            printWriter.close();
                        }
                        this.mBackStack.add(instantiate2);
                        if (instantiate2.mIndex >= 0) {
                            setBackStackIndex(instantiate2.mIndex, instantiate2);
                        }
                    }
                } else {
                    this.mBackStack = null;
                }
                if (fragmentManagerState.mPrimaryNavActiveIndex >= 0) {
                    this.mPrimaryNav = (Fragment) this.mActive.get(fragmentManagerState.mPrimaryNavActiveIndex);
                }
                this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
            }
        }
    }

    private void burpActive() {
        if (this.mActive != null) {
            for (int size = this.mActive.size() - 1; size >= 0; size--) {
                if (this.mActive.valueAt(size) == null) {
                    this.mActive.delete(this.mActive.keyAt(size));
                }
            }
        }
    }

    public void attachController(FragmentHostCallback fragmentHostCallback, FragmentContainer fragmentContainer, Fragment fragment) {
        if (this.mHost != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mHost = fragmentHostCallback;
        this.mContainer = fragmentContainer;
        this.mParent = fragment;
    }

    public void noteStateNotSaved() {
        this.mSavedNonConfig = null;
        this.mStateSaved = false;
        int size = this.mAdded.size();
        for (int i = 0; i < size; i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }

    public void dispatchCreate() {
        this.mStateSaved = false;
        dispatchStateChange(1);
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        dispatchStateChange(2);
    }

    public void dispatchStart() {
        this.mStateSaved = false;
        dispatchStateChange(4);
    }

    public void dispatchResume() {
        this.mStateSaved = false;
        dispatchStateChange(5);
    }

    public void dispatchPause() {
        dispatchStateChange(4);
    }

    public void dispatchStop() {
        this.mStateSaved = true;
        dispatchStateChange(3);
    }

    public void dispatchReallyStop() {
        dispatchStateChange(2);
    }

    public void dispatchDestroyView() {
        dispatchStateChange(1);
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }

    /* JADX INFO: finally extract failed */
    private void dispatchStateChange(int i) {
        try {
            this.mExecutingActions = true;
            moveToState(i, false);
            this.mExecutingActions = false;
            execPendingActions();
        } catch (Throwable th) {
            this.mExecutingActions = false;
            throw th;
        }
    }

    public void dispatchMultiWindowModeChanged(boolean z) {
        for (int size = this.mAdded.size() - 1; size >= 0; size--) {
            Fragment fragment = (Fragment) this.mAdded.get(size);
            if (fragment != null) {
                fragment.performMultiWindowModeChanged(z);
            }
        }
    }

    public void dispatchPictureInPictureModeChanged(boolean z) {
        for (int size = this.mAdded.size() - 1; size >= 0; size--) {
            Fragment fragment = (Fragment) this.mAdded.get(size);
            if (fragment != null) {
                fragment.performPictureInPictureModeChanged(z);
            }
        }
    }

    public void dispatchConfigurationChanged(Configuration configuration) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null) {
                fragment.performConfigurationChanged(configuration);
            }
        }
    }

    public void dispatchLowMemory() {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null) {
                fragment.performLowMemory();
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        boolean z = false;
        ArrayList<Fragment> arrayList = null;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performCreateOptionsMenu(menu, menuInflater)) {
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                arrayList.add(fragment);
                z = true;
            }
        }
        if (this.mCreatedMenus != null) {
            for (int i2 = 0; i2 < this.mCreatedMenus.size(); i2++) {
                Fragment fragment2 = (Fragment) this.mCreatedMenus.get(i2);
                if (arrayList == null || !arrayList.contains(fragment2)) {
                    fragment2.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = arrayList;
        return z;
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        boolean z = false;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performPrepareOptionsMenu(menu)) {
                z = true;
            }
        }
        return z;
    }

    public boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public boolean dispatchContextItemSelected(MenuItem menuItem) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null) {
                fragment.performOptionsMenuClosed(menu);
            }
        }
    }

    public void setPrimaryNavigationFragment(Fragment fragment) {
        if (fragment == null || (this.mActive.get(fragment.mIndex) == fragment && (fragment.mHost == null || fragment.getFragmentManager() == this))) {
            this.mPrimaryNav = fragment;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Fragment ");
        sb.append(fragment);
        sb.append(" is not an active fragment of FragmentManager ");
        sb.append(this);
        throw new IllegalArgumentException(sb.toString());
    }

    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }

    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean z) {
        this.mLifecycleCallbacks.add(new Pair(fragmentLifecycleCallbacks, Boolean.valueOf(z)));
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        synchronized (this.mLifecycleCallbacks) {
            int i = 0;
            int size = this.mLifecycleCallbacks.size();
            while (true) {
                if (i >= size) {
                    break;
                } else if (((Pair) this.mLifecycleCallbacks.get(i)).first == fragmentLifecycleCallbacks) {
                    this.mLifecycleCallbacks.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentPreAttached(Fragment fragment, Context context, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentPreAttached(fragment, context, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentPreAttached(this, fragment, context);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentAttached(Fragment fragment, Context context, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentAttached(fragment, context, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentAttached(this, fragment, context);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentPreCreated(Fragment fragment, Bundle bundle, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentPreCreated(fragment, bundle, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentPreCreated(this, fragment, bundle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentCreated(Fragment fragment, Bundle bundle, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentCreated(fragment, bundle, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentCreated(this, fragment, bundle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentActivityCreated(Fragment fragment, Bundle bundle, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentActivityCreated(fragment, bundle, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentActivityCreated(this, fragment, bundle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentViewCreated(Fragment fragment, View view, Bundle bundle, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentViewCreated(fragment, view, bundle, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentViewCreated(this, fragment, view, bundle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentStarted(Fragment fragment, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentStarted(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentStarted(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentResumed(Fragment fragment, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentResumed(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentResumed(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentPaused(Fragment fragment, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentPaused(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentPaused(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentStopped(Fragment fragment, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentStopped(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentStopped(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentSaveInstanceState(Fragment fragment, Bundle bundle, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentSaveInstanceState(this, fragment, bundle);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentViewDestroyed(Fragment fragment, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentViewDestroyed(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentViewDestroyed(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentDestroyed(Fragment fragment, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentDestroyed(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentDestroyed(this, fragment);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispatchOnFragmentDetached(Fragment fragment, boolean z) {
        if (this.mParent != null) {
            FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) fragmentManager).dispatchOnFragmentDetached(fragment, true);
            }
        }
        Iterator it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (!z || ((Boolean) pair.second).booleanValue()) {
                ((FragmentLifecycleCallbacks) pair.first).onFragmentDetached(this, fragment);
            }
        }
    }

    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        Fragment fragment;
        Context context2 = context;
        AttributeSet attributeSet2 = attributeSet;
        if (!"fragment".equals(str)) {
            return null;
        }
        String attributeValue = attributeSet2.getAttributeValue(null, "class");
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet2, FragmentTag.Fragment);
        int i = 0;
        if (attributeValue == null) {
            attributeValue = obtainStyledAttributes.getString(0);
        }
        String str2 = attributeValue;
        int resourceId = obtainStyledAttributes.getResourceId(1, -1);
        String string = obtainStyledAttributes.getString(2);
        obtainStyledAttributes.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), str2)) {
            return null;
        }
        if (view != null) {
            i = view.getId();
        }
        if (i == -1 && resourceId == -1 && string == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(attributeSet.getPositionDescription());
            sb.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
            sb.append(str2);
            throw new IllegalArgumentException(sb.toString());
        }
        Fragment findFragmentById = resourceId != -1 ? findFragmentById(resourceId) : null;
        if (findFragmentById == null && string != null) {
            findFragmentById = findFragmentByTag(string);
        }
        if (findFragmentById == null && i != -1) {
            findFragmentById = findFragmentById(i);
        }
        if (DEBUG) {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onCreateView: id=0x");
            sb2.append(Integer.toHexString(resourceId));
            sb2.append(" fname=");
            sb2.append(str2);
            sb2.append(" existing=");
            sb2.append(findFragmentById);
            Log.v(str3, sb2.toString());
        }
        if (findFragmentById == null) {
            Fragment instantiate = this.mContainer.instantiate(context2, str2, null);
            instantiate.mFromLayout = true;
            instantiate.mFragmentId = resourceId != 0 ? resourceId : i;
            instantiate.mContainerId = i;
            instantiate.mTag = string;
            instantiate.mInLayout = true;
            instantiate.mFragmentManager = this;
            instantiate.mHost = this.mHost;
            instantiate.onInflate(this.mHost.getContext(), attributeSet2, instantiate.mSavedFragmentState);
            addFragment(instantiate, true);
            fragment = instantiate;
        } else if (findFragmentById.mInLayout) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(attributeSet.getPositionDescription());
            sb3.append(": Duplicate id 0x");
            sb3.append(Integer.toHexString(resourceId));
            sb3.append(", tag ");
            sb3.append(string);
            sb3.append(", or parent id 0x");
            sb3.append(Integer.toHexString(i));
            sb3.append(" with another fragment for ");
            sb3.append(str2);
            throw new IllegalArgumentException(sb3.toString());
        } else {
            findFragmentById.mInLayout = true;
            findFragmentById.mHost = this.mHost;
            if (!findFragmentById.mRetaining) {
                findFragmentById.onInflate(this.mHost.getContext(), attributeSet2, findFragmentById.mSavedFragmentState);
            }
            fragment = findFragmentById;
        }
        if (this.mCurState >= 1 || !fragment.mFromLayout) {
            moveToState(fragment);
        } else {
            moveToState(fragment, 1, 0, 0, false);
        }
        if (fragment.mView == null) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Fragment ");
            sb4.append(str2);
            sb4.append(" did not create a view.");
            throw new IllegalStateException(sb4.toString());
        }
        if (resourceId != 0) {
            fragment.mView.setId(resourceId);
        }
        if (fragment.mView.getTag() == null) {
            fragment.mView.setTag(string);
        }
        return fragment.mView;
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }
}
