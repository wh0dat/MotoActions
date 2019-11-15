package android.support.p001v4.app;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.p001v4.util.ArrayMap;
import android.support.p001v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;

/* renamed from: android.support.v4.app.FragmentTransition */
class FragmentTransition {
    private static final int[] INVERSE_OPS = {0, 3, 0, 1, 5, 4, 7, 6, 9, 8};

    /* renamed from: android.support.v4.app.FragmentTransition$FragmentContainerTransition */
    static class FragmentContainerTransition {
        public Fragment firstOut;
        public boolean firstOutIsPop;
        public BackStackRecord firstOutTransaction;
        public Fragment lastIn;
        public boolean lastInIsPop;
        public BackStackRecord lastInTransaction;

        FragmentContainerTransition() {
        }
    }

    FragmentTransition() {
    }

    static void startTransitions(FragmentManagerImpl fragmentManagerImpl, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i, int i2, boolean z) {
        if (fragmentManagerImpl.mCurState >= 1 && VERSION.SDK_INT >= 21) {
            SparseArray sparseArray = new SparseArray();
            for (int i3 = i; i3 < i2; i3++) {
                BackStackRecord backStackRecord = (BackStackRecord) arrayList.get(i3);
                if (((Boolean) arrayList2.get(i3)).booleanValue()) {
                    calculatePopFragments(backStackRecord, sparseArray, z);
                } else {
                    calculateFragments(backStackRecord, sparseArray, z);
                }
            }
            if (sparseArray.size() != 0) {
                View view = new View(fragmentManagerImpl.mHost.getContext());
                int size = sparseArray.size();
                for (int i4 = 0; i4 < size; i4++) {
                    int keyAt = sparseArray.keyAt(i4);
                    ArrayMap calculateNameOverrides = calculateNameOverrides(keyAt, arrayList, arrayList2, i, i2);
                    FragmentContainerTransition fragmentContainerTransition = (FragmentContainerTransition) sparseArray.valueAt(i4);
                    if (z) {
                        configureTransitionsReordered(fragmentManagerImpl, keyAt, fragmentContainerTransition, view, calculateNameOverrides);
                    } else {
                        configureTransitionsOrdered(fragmentManagerImpl, keyAt, fragmentContainerTransition, view, calculateNameOverrides);
                    }
                }
            }
        }
    }

    private static ArrayMap<String, String> calculateNameOverrides(int i, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i2, int i3) {
        ArrayList<String> arrayList3;
        ArrayList arrayList4;
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        for (int i4 = i3 - 1; i4 >= i2; i4--) {
            BackStackRecord backStackRecord = (BackStackRecord) arrayList.get(i4);
            if (backStackRecord.interactsWith(i)) {
                boolean booleanValue = ((Boolean) arrayList2.get(i4)).booleanValue();
                if (backStackRecord.mSharedElementSourceNames != null) {
                    int size = backStackRecord.mSharedElementSourceNames.size();
                    if (booleanValue) {
                        arrayList3 = backStackRecord.mSharedElementSourceNames;
                        arrayList4 = backStackRecord.mSharedElementTargetNames;
                    } else {
                        ArrayList arrayList5 = backStackRecord.mSharedElementSourceNames;
                        arrayList3 = backStackRecord.mSharedElementTargetNames;
                        arrayList4 = arrayList5;
                    }
                    for (int i5 = 0; i5 < size; i5++) {
                        String str = (String) arrayList4.get(i5);
                        String str2 = (String) arrayList3.get(i5);
                        String str3 = (String) arrayMap.remove(str2);
                        if (str3 != null) {
                            arrayMap.put(str, str3);
                        } else {
                            arrayMap.put(str, str2);
                        }
                    }
                }
            }
        }
        return arrayMap;
    }

    @RequiresApi(21)
    private static void configureTransitionsReordered(FragmentManagerImpl fragmentManagerImpl, int i, FragmentContainerTransition fragmentContainerTransition, View view, ArrayMap<String, String> arrayMap) {
        Object obj;
        FragmentManagerImpl fragmentManagerImpl2 = fragmentManagerImpl;
        FragmentContainerTransition fragmentContainerTransition2 = fragmentContainerTransition;
        View view2 = view;
        ViewGroup viewGroup = fragmentManagerImpl2.mContainer.onHasView() ? (ViewGroup) fragmentManagerImpl2.mContainer.onFindViewById(i) : null;
        if (viewGroup != null) {
            Fragment fragment = fragmentContainerTransition2.lastIn;
            Fragment fragment2 = fragmentContainerTransition2.firstOut;
            boolean z = fragmentContainerTransition2.lastInIsPop;
            boolean z2 = fragmentContainerTransition2.firstOutIsPop;
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            Object enterTransition = getEnterTransition(fragment, z);
            Object exitTransition = getExitTransition(fragment2, z2);
            Object obj2 = exitTransition;
            Object configureSharedElementsReordered = configureSharedElementsReordered(viewGroup, view2, arrayMap, fragmentContainerTransition2, arrayList2, arrayList, enterTransition, exitTransition);
            if (enterTransition == null && configureSharedElementsReordered == null) {
                obj = obj2;
                if (obj == null) {
                    return;
                }
            } else {
                obj = obj2;
            }
            ArrayList configureEnteringExitingViews = configureEnteringExitingViews(obj, fragment2, arrayList2, view2);
            ArrayList configureEnteringExitingViews2 = configureEnteringExitingViews(enterTransition, fragment, arrayList, view2);
            setViewVisibility(configureEnteringExitingViews2, 4);
            Object mergeTransitions = mergeTransitions(enterTransition, obj, configureSharedElementsReordered, fragment, z);
            if (mergeTransitions != null) {
                replaceHide(obj, fragment2, configureEnteringExitingViews);
                ArrayList prepareSetNameOverridesReordered = FragmentTransitionCompat21.prepareSetNameOverridesReordered(arrayList);
                FragmentTransitionCompat21.scheduleRemoveTargets(mergeTransitions, enterTransition, configureEnteringExitingViews2, obj, configureEnteringExitingViews, configureSharedElementsReordered, arrayList);
                FragmentTransitionCompat21.beginDelayedTransition(viewGroup, mergeTransitions);
                FragmentTransitionCompat21.setNameOverridesReordered(viewGroup, arrayList2, arrayList, prepareSetNameOverridesReordered, arrayMap);
                setViewVisibility(configureEnteringExitingViews2, 0);
                FragmentTransitionCompat21.swapSharedElementTargets(configureSharedElementsReordered, arrayList2, arrayList);
            }
        }
    }

    @RequiresApi(21)
    private static void replaceHide(Object obj, Fragment fragment, final ArrayList<View> arrayList) {
        if (fragment != null && obj != null && fragment.mAdded && fragment.mHidden && fragment.mHiddenChanged) {
            fragment.setHideReplaced(true);
            FragmentTransitionCompat21.scheduleHideFragmentView(obj, fragment.getView(), arrayList);
            OneShotPreDrawListener.add(fragment.mContainer, new Runnable() {
                public void run() {
                    FragmentTransition.setViewVisibility(arrayList, 4);
                }
            });
        }
    }

    @RequiresApi(21)
    private static void configureTransitionsOrdered(FragmentManagerImpl fragmentManagerImpl, int i, FragmentContainerTransition fragmentContainerTransition, View view, ArrayMap<String, String> arrayMap) {
        Object obj;
        FragmentManagerImpl fragmentManagerImpl2 = fragmentManagerImpl;
        FragmentContainerTransition fragmentContainerTransition2 = fragmentContainerTransition;
        View view2 = view;
        ArrayMap<String, String> arrayMap2 = arrayMap;
        ViewGroup viewGroup = fragmentManagerImpl2.mContainer.onHasView() ? (ViewGroup) fragmentManagerImpl2.mContainer.onFindViewById(i) : null;
        if (viewGroup != null) {
            Fragment fragment = fragmentContainerTransition2.lastIn;
            Fragment fragment2 = fragmentContainerTransition2.firstOut;
            boolean z = fragmentContainerTransition2.lastInIsPop;
            boolean z2 = fragmentContainerTransition2.firstOutIsPop;
            Object enterTransition = getEnterTransition(fragment, z);
            Object exitTransition = getExitTransition(fragment2, z2);
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList2;
            ArrayList arrayList4 = arrayList;
            Object obj2 = exitTransition;
            Object configureSharedElementsOrdered = configureSharedElementsOrdered(viewGroup, view2, arrayMap2, fragmentContainerTransition2, arrayList, arrayList2, enterTransition, exitTransition);
            if (enterTransition == null && configureSharedElementsOrdered == null) {
                obj = obj2;
                if (obj == null) {
                    return;
                }
            } else {
                obj = obj2;
            }
            ArrayList configureEnteringExitingViews = configureEnteringExitingViews(obj, fragment2, arrayList4, view2);
            Object obj3 = (configureEnteringExitingViews == null || configureEnteringExitingViews.isEmpty()) ? null : obj;
            FragmentTransitionCompat21.addTarget(enterTransition, view2);
            Object mergeTransitions = mergeTransitions(enterTransition, obj3, configureSharedElementsOrdered, fragment, fragmentContainerTransition2.lastInIsPop);
            if (mergeTransitions != null) {
                ArrayList arrayList5 = new ArrayList();
                FragmentTransitionCompat21.scheduleRemoveTargets(mergeTransitions, enterTransition, arrayList5, obj3, configureEnteringExitingViews, configureSharedElementsOrdered, arrayList3);
                Object obj4 = mergeTransitions;
                scheduleTargetChange(viewGroup, fragment, view2, arrayList3, enterTransition, arrayList5, obj3, configureEnteringExitingViews);
                ArrayList arrayList6 = arrayList3;
                FragmentTransitionCompat21.setNameOverridesOrdered(viewGroup, arrayList6, arrayMap2);
                FragmentTransitionCompat21.beginDelayedTransition(viewGroup, obj4);
                FragmentTransitionCompat21.scheduleNameReset(viewGroup, arrayList6, arrayMap2);
            }
        }
    }

    @RequiresApi(21)
    private static void scheduleTargetChange(ViewGroup viewGroup, Fragment fragment, View view, ArrayList<View> arrayList, Object obj, ArrayList<View> arrayList2, Object obj2, ArrayList<View> arrayList3) {
        final Object obj3 = obj;
        final View view2 = view;
        final Fragment fragment2 = fragment;
        final ArrayList<View> arrayList4 = arrayList;
        final ArrayList<View> arrayList5 = arrayList2;
        final ArrayList<View> arrayList6 = arrayList3;
        final Object obj4 = obj2;
        C02032 r0 = new Runnable() {
            public void run() {
                if (obj3 != null) {
                    FragmentTransitionCompat21.removeTarget(obj3, view2);
                    arrayList5.addAll(FragmentTransition.configureEnteringExitingViews(obj3, fragment2, arrayList4, view2));
                }
                if (arrayList6 != null) {
                    if (obj4 != null) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(view2);
                        FragmentTransitionCompat21.replaceTargets(obj4, arrayList6, arrayList);
                    }
                    arrayList6.clear();
                    arrayList6.add(view2);
                }
            }
        };
        OneShotPreDrawListener.add(viewGroup, r0);
    }

    @RequiresApi(21)
    private static Object getSharedElementTransition(Fragment fragment, Fragment fragment2, boolean z) {
        Object obj;
        if (fragment == null || fragment2 == null) {
            return null;
        }
        if (z) {
            obj = fragment2.getSharedElementReturnTransition();
        } else {
            obj = fragment.getSharedElementEnterTransition();
        }
        return FragmentTransitionCompat21.wrapTransitionInSet(FragmentTransitionCompat21.cloneTransition(obj));
    }

    @RequiresApi(21)
    private static Object getEnterTransition(Fragment fragment, boolean z) {
        Object obj;
        if (fragment == null) {
            return null;
        }
        if (z) {
            obj = fragment.getReenterTransition();
        } else {
            obj = fragment.getEnterTransition();
        }
        return FragmentTransitionCompat21.cloneTransition(obj);
    }

    @RequiresApi(21)
    private static Object getExitTransition(Fragment fragment, boolean z) {
        Object obj;
        if (fragment == null) {
            return null;
        }
        if (z) {
            obj = fragment.getReturnTransition();
        } else {
            obj = fragment.getExitTransition();
        }
        return FragmentTransitionCompat21.cloneTransition(obj);
    }

    @RequiresApi(21)
    private static Object configureSharedElementsReordered(ViewGroup viewGroup, View view, ArrayMap<String, String> arrayMap, FragmentContainerTransition fragmentContainerTransition, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object obj, Object obj2) {
        Object obj3;
        final Rect rect;
        final View view2;
        Fragment fragment = fragmentContainerTransition.lastIn;
        Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment != null) {
            fragment.getView().setVisibility(0);
        }
        if (fragment == null || fragment2 == null) {
            return null;
        }
        boolean z = fragmentContainerTransition.lastInIsPop;
        if (arrayMap.isEmpty()) {
            obj3 = null;
        } else {
            obj3 = getSharedElementTransition(fragment, fragment2, z);
        }
        ArrayMap captureOutSharedElements = captureOutSharedElements(arrayMap, obj3, fragmentContainerTransition);
        ArrayMap captureInSharedElements = captureInSharedElements(arrayMap, obj3, fragmentContainerTransition);
        if (arrayMap.isEmpty()) {
            if (captureOutSharedElements != null) {
                captureOutSharedElements.clear();
            }
            if (captureInSharedElements != null) {
                captureInSharedElements.clear();
            }
            obj3 = null;
        } else {
            addSharedElementsWithMatchingNames(arrayList, captureOutSharedElements, arrayMap.keySet());
            addSharedElementsWithMatchingNames(arrayList2, captureInSharedElements, arrayMap.values());
        }
        if (obj == null && obj2 == null && obj3 == null) {
            return null;
        }
        callSharedElementStartEnd(fragment, fragment2, z, captureOutSharedElements, true);
        if (obj3 != null) {
            arrayList2.add(view);
            FragmentTransitionCompat21.setSharedElementTargets(obj3, view, arrayList);
            setOutEpicenter(obj3, obj2, captureOutSharedElements, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
            Rect rect2 = new Rect();
            View inEpicenterView = getInEpicenterView(captureInSharedElements, fragmentContainerTransition, obj, z);
            if (inEpicenterView != null) {
                FragmentTransitionCompat21.setEpicenter(obj, rect2);
            }
            rect = rect2;
            view2 = inEpicenterView;
        } else {
            view2 = null;
            rect = null;
        }
        final Fragment fragment3 = fragment;
        final Fragment fragment4 = fragment2;
        final boolean z2 = z;
        final ArrayMap arrayMap2 = captureInSharedElements;
        C02043 r9 = new Runnable() {
            public void run() {
                FragmentTransition.callSharedElementStartEnd(fragment3, fragment4, z2, arrayMap2, false);
                if (view2 != null) {
                    FragmentTransitionCompat21.getBoundsOnScreen(view2, rect);
                }
            }
        };
        OneShotPreDrawListener.add(viewGroup, r9);
        return obj3;
    }

    private static void addSharedElementsWithMatchingNames(ArrayList<View> arrayList, ArrayMap<String, View> arrayMap, Collection<String> collection) {
        for (int size = arrayMap.size() - 1; size >= 0; size--) {
            View view = (View) arrayMap.valueAt(size);
            if (collection.contains(ViewCompat.getTransitionName(view))) {
                arrayList.add(view);
            }
        }
    }

    @RequiresApi(21)
    private static Object configureSharedElementsOrdered(ViewGroup viewGroup, View view, ArrayMap<String, String> arrayMap, FragmentContainerTransition fragmentContainerTransition, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object obj, Object obj2) {
        Object obj3;
        ArrayMap<String, String> arrayMap2;
        Object obj4;
        View view2;
        final FragmentContainerTransition fragmentContainerTransition2 = fragmentContainerTransition;
        final ArrayList<View> arrayList3 = arrayList;
        final Object obj5 = obj;
        Object obj6 = obj2;
        final Fragment fragment = fragmentContainerTransition2.lastIn;
        final Fragment fragment2 = fragmentContainerTransition2.firstOut;
        Rect rect = null;
        if (fragment == null || fragment2 == null) {
            return null;
        }
        final boolean z = fragmentContainerTransition2.lastInIsPop;
        if (arrayMap.isEmpty()) {
            arrayMap2 = arrayMap;
            obj3 = null;
        } else {
            obj3 = getSharedElementTransition(fragment, fragment2, z);
            arrayMap2 = arrayMap;
        }
        ArrayMap captureOutSharedElements = captureOutSharedElements(arrayMap2, obj3, fragmentContainerTransition2);
        if (arrayMap.isEmpty()) {
            obj4 = null;
        } else {
            arrayList3.addAll(captureOutSharedElements.values());
            obj4 = obj3;
        }
        if (obj5 == null && obj6 == null && obj4 == null) {
            return null;
        }
        callSharedElementStartEnd(fragment, fragment2, z, captureOutSharedElements, true);
        if (obj4 != null) {
            rect = new Rect();
            view2 = view;
            FragmentTransitionCompat21.setSharedElementTargets(obj4, view2, arrayList3);
            setOutEpicenter(obj4, obj6, captureOutSharedElements, fragmentContainerTransition2.firstOutIsPop, fragmentContainerTransition2.firstOutTransaction);
            if (obj5 != null) {
                FragmentTransitionCompat21.setEpicenter(obj5, rect);
            }
        } else {
            view2 = view;
        }
        Rect rect2 = rect;
        final ArrayMap<String, String> arrayMap3 = arrayMap2;
        final Object obj7 = obj4;
        final ArrayList<View> arrayList4 = arrayList2;
        final View view3 = view2;
        final Rect rect3 = rect2;
        C02054 r0 = new Runnable() {
            public void run() {
                ArrayMap access$300 = FragmentTransition.captureInSharedElements(arrayMap3, obj7, fragmentContainerTransition2);
                if (access$300 != null) {
                    arrayList4.addAll(access$300.values());
                    arrayList4.add(view3);
                }
                FragmentTransition.callSharedElementStartEnd(fragment, fragment2, z, access$300, false);
                if (obj7 != null) {
                    FragmentTransitionCompat21.swapSharedElementTargets(obj7, arrayList3, arrayList4);
                    View access$400 = FragmentTransition.getInEpicenterView(access$300, fragmentContainerTransition2, obj5, z);
                    if (access$400 != null) {
                        FragmentTransitionCompat21.getBoundsOnScreen(access$400, rect3);
                    }
                }
            }
        };
        OneShotPreDrawListener.add(viewGroup, r0);
        return obj4;
    }

    @RequiresApi(21)
    private static ArrayMap<String, View> captureOutSharedElements(ArrayMap<String, String> arrayMap, Object obj, FragmentContainerTransition fragmentContainerTransition) {
        ArrayList<String> arrayList;
        SharedElementCallback sharedElementCallback;
        if (arrayMap.isEmpty() || obj == null) {
            arrayMap.clear();
            return null;
        }
        Fragment fragment = fragmentContainerTransition.firstOut;
        ArrayMap<String, View> arrayMap2 = new ArrayMap<>();
        FragmentTransitionCompat21.findNamedViews(arrayMap2, fragment.getView());
        BackStackRecord backStackRecord = fragmentContainerTransition.firstOutTransaction;
        if (fragmentContainerTransition.firstOutIsPop) {
            sharedElementCallback = fragment.getEnterTransitionCallback();
            arrayList = backStackRecord.mSharedElementTargetNames;
        } else {
            sharedElementCallback = fragment.getExitTransitionCallback();
            arrayList = backStackRecord.mSharedElementSourceNames;
        }
        arrayMap2.retainAll(arrayList);
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(arrayList, arrayMap2);
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                String str = (String) arrayList.get(size);
                View view = (View) arrayMap2.get(str);
                if (view == null) {
                    arrayMap.remove(str);
                } else if (!str.equals(ViewCompat.getTransitionName(view))) {
                    arrayMap.put(ViewCompat.getTransitionName(view), (String) arrayMap.remove(str));
                }
            }
        } else {
            arrayMap.retainAll(arrayMap2.keySet());
        }
        return arrayMap2;
    }

    /* access modifiers changed from: private */
    @RequiresApi(21)
    public static ArrayMap<String, View> captureInSharedElements(ArrayMap<String, String> arrayMap, Object obj, FragmentContainerTransition fragmentContainerTransition) {
        SharedElementCallback sharedElementCallback;
        ArrayList<String> arrayList;
        Fragment fragment = fragmentContainerTransition.lastIn;
        View view = fragment.getView();
        if (arrayMap.isEmpty() || obj == null || view == null) {
            arrayMap.clear();
            return null;
        }
        ArrayMap<String, View> arrayMap2 = new ArrayMap<>();
        FragmentTransitionCompat21.findNamedViews(arrayMap2, view);
        BackStackRecord backStackRecord = fragmentContainerTransition.lastInTransaction;
        if (fragmentContainerTransition.lastInIsPop) {
            sharedElementCallback = fragment.getExitTransitionCallback();
            arrayList = backStackRecord.mSharedElementSourceNames;
        } else {
            sharedElementCallback = fragment.getEnterTransitionCallback();
            arrayList = backStackRecord.mSharedElementTargetNames;
        }
        if (arrayList != null) {
            arrayMap2.retainAll(arrayList);
        }
        if (sharedElementCallback != null) {
            sharedElementCallback.onMapSharedElements(arrayList, arrayMap2);
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                String str = (String) arrayList.get(size);
                View view2 = (View) arrayMap2.get(str);
                if (view2 == null) {
                    String findKeyForValue = findKeyForValue(arrayMap, str);
                    if (findKeyForValue != null) {
                        arrayMap.remove(findKeyForValue);
                    }
                } else if (!str.equals(ViewCompat.getTransitionName(view2))) {
                    String findKeyForValue2 = findKeyForValue(arrayMap, str);
                    if (findKeyForValue2 != null) {
                        arrayMap.put(findKeyForValue2, ViewCompat.getTransitionName(view2));
                    }
                }
            }
        } else {
            retainValues(arrayMap, arrayMap2);
        }
        return arrayMap2;
    }

    private static String findKeyForValue(ArrayMap<String, String> arrayMap, String str) {
        int size = arrayMap.size();
        for (int i = 0; i < size; i++) {
            if (str.equals(arrayMap.valueAt(i))) {
                return (String) arrayMap.keyAt(i);
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static View getInEpicenterView(ArrayMap<String, View> arrayMap, FragmentContainerTransition fragmentContainerTransition, Object obj, boolean z) {
        String str;
        BackStackRecord backStackRecord = fragmentContainerTransition.lastInTransaction;
        if (obj == null || arrayMap == null || backStackRecord.mSharedElementSourceNames == null || backStackRecord.mSharedElementSourceNames.isEmpty()) {
            return null;
        }
        if (z) {
            str = (String) backStackRecord.mSharedElementSourceNames.get(0);
        } else {
            str = (String) backStackRecord.mSharedElementTargetNames.get(0);
        }
        return (View) arrayMap.get(str);
    }

    @RequiresApi(21)
    private static void setOutEpicenter(Object obj, Object obj2, ArrayMap<String, View> arrayMap, boolean z, BackStackRecord backStackRecord) {
        String str;
        if (backStackRecord.mSharedElementSourceNames != null && !backStackRecord.mSharedElementSourceNames.isEmpty()) {
            if (z) {
                str = (String) backStackRecord.mSharedElementTargetNames.get(0);
            } else {
                str = (String) backStackRecord.mSharedElementSourceNames.get(0);
            }
            View view = (View) arrayMap.get(str);
            FragmentTransitionCompat21.setEpicenter(obj, view);
            if (obj2 != null) {
                FragmentTransitionCompat21.setEpicenter(obj2, view);
            }
        }
    }

    private static void retainValues(ArrayMap<String, String> arrayMap, ArrayMap<String, View> arrayMap2) {
        for (int size = arrayMap.size() - 1; size >= 0; size--) {
            if (!arrayMap2.containsKey((String) arrayMap.valueAt(size))) {
                arrayMap.removeAt(size);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void callSharedElementStartEnd(Fragment fragment, Fragment fragment2, boolean z, ArrayMap<String, View> arrayMap, boolean z2) {
        SharedElementCallback sharedElementCallback;
        int i;
        if (z) {
            sharedElementCallback = fragment2.getEnterTransitionCallback();
        } else {
            sharedElementCallback = fragment.getEnterTransitionCallback();
        }
        if (sharedElementCallback != null) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            if (arrayMap == null) {
                i = 0;
            } else {
                i = arrayMap.size();
            }
            for (int i2 = 0; i2 < i; i2++) {
                arrayList2.add(arrayMap.keyAt(i2));
                arrayList.add(arrayMap.valueAt(i2));
            }
            if (z2) {
                sharedElementCallback.onSharedElementStart(arrayList2, arrayList, null);
            } else {
                sharedElementCallback.onSharedElementEnd(arrayList2, arrayList, null);
            }
        }
    }

    /* access modifiers changed from: private */
    @RequiresApi(21)
    public static ArrayList<View> configureEnteringExitingViews(Object obj, Fragment fragment, ArrayList<View> arrayList, View view) {
        if (obj == null) {
            return null;
        }
        ArrayList<View> arrayList2 = new ArrayList<>();
        View view2 = fragment.getView();
        if (view2 != null) {
            FragmentTransitionCompat21.captureTransitioningViews(arrayList2, view2);
        }
        if (arrayList != null) {
            arrayList2.removeAll(arrayList);
        }
        if (arrayList2.isEmpty()) {
            return arrayList2;
        }
        arrayList2.add(view);
        FragmentTransitionCompat21.addTargets(obj, arrayList2);
        return arrayList2;
    }

    /* access modifiers changed from: private */
    public static void setViewVisibility(ArrayList<View> arrayList, int i) {
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                ((View) arrayList.get(size)).setVisibility(i);
            }
        }
    }

    @RequiresApi(21)
    private static Object mergeTransitions(Object obj, Object obj2, Object obj3, Fragment fragment, boolean z) {
        boolean z2 = (obj == null || obj2 == null || fragment == null) ? true : z ? fragment.getAllowReturnTransitionOverlap() : fragment.getAllowEnterTransitionOverlap();
        if (z2) {
            return FragmentTransitionCompat21.mergeTransitionsTogether(obj2, obj, obj3);
        }
        return FragmentTransitionCompat21.mergeTransitionsInSequence(obj2, obj, obj3);
    }

    public static void calculateFragments(BackStackRecord backStackRecord, SparseArray<FragmentContainerTransition> sparseArray, boolean z) {
        int size = backStackRecord.mOps.size();
        for (int i = 0; i < size; i++) {
            addToFirstInLastOut(backStackRecord, (C0188Op) backStackRecord.mOps.get(i), sparseArray, false, z);
        }
    }

    public static void calculatePopFragments(BackStackRecord backStackRecord, SparseArray<FragmentContainerTransition> sparseArray, boolean z) {
        if (backStackRecord.mManager.mContainer.onHasView()) {
            for (int size = backStackRecord.mOps.size() - 1; size >= 0; size--) {
                addToFirstInLastOut(backStackRecord, (C0188Op) backStackRecord.mOps.get(size), sparseArray, true, z);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0035, code lost:
        if (r10.mAdded != false) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x006d, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x006f, code lost:
        r1 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x007a, code lost:
        r13 = r1;
        r1 = false;
        r12 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0089, code lost:
        if (r10.mHidden == false) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x008b, code lost:
        r1 = true;
     */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x009a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void addToFirstInLastOut(android.support.p001v4.app.BackStackRecord r16, android.support.p001v4.app.BackStackRecord.C0188Op r17, android.util.SparseArray<android.support.p001v4.app.FragmentTransition.FragmentContainerTransition> r18, boolean r19, boolean r20) {
        /*
            r0 = r16
            r1 = r17
            r2 = r18
            r3 = r19
            android.support.v4.app.Fragment r10 = r1.fragment
            if (r10 != 0) goto L_0x000d
            return
        L_0x000d:
            int r11 = r10.mContainerId
            if (r11 != 0) goto L_0x0012
            return
        L_0x0012:
            if (r3 == 0) goto L_0x001b
            int[] r4 = INVERSE_OPS
            int r1 = r1.cmd
            r1 = r4[r1]
            goto L_0x001d
        L_0x001b:
            int r1 = r1.cmd
        L_0x001d:
            r4 = 0
            r5 = 1
            if (r1 == r5) goto L_0x007e
            switch(r1) {
                case 3: goto L_0x0054;
                case 4: goto L_0x003c;
                case 5: goto L_0x0029;
                case 6: goto L_0x0054;
                case 7: goto L_0x007e;
                default: goto L_0x0024;
            }
        L_0x0024:
            r1 = r4
            r12 = r1
            r13 = r12
            goto L_0x0092
        L_0x0029:
            if (r20 == 0) goto L_0x0038
            boolean r1 = r10.mHiddenChanged
            if (r1 == 0) goto L_0x008d
            boolean r1 = r10.mHidden
            if (r1 != 0) goto L_0x008d
            boolean r1 = r10.mAdded
            if (r1 == 0) goto L_0x008d
            goto L_0x008b
        L_0x0038:
            boolean r1 = r10.mHidden
            goto L_0x008e
        L_0x003c:
            if (r20 == 0) goto L_0x004b
            boolean r1 = r10.mHiddenChanged
            if (r1 == 0) goto L_0x006f
            boolean r1 = r10.mAdded
            if (r1 == 0) goto L_0x006f
            boolean r1 = r10.mHidden
            if (r1 == 0) goto L_0x006f
        L_0x004a:
            goto L_0x006d
        L_0x004b:
            boolean r1 = r10.mAdded
            if (r1 == 0) goto L_0x006f
            boolean r1 = r10.mHidden
            if (r1 != 0) goto L_0x006f
            goto L_0x004a
        L_0x0054:
            if (r20 == 0) goto L_0x0071
            boolean r1 = r10.mAdded
            if (r1 != 0) goto L_0x006f
            android.view.View r1 = r10.mView
            if (r1 == 0) goto L_0x006f
            android.view.View r1 = r10.mView
            int r1 = r1.getVisibility()
            if (r1 != 0) goto L_0x006f
            float r1 = r10.mPostponedAlpha
            r6 = 0
            int r1 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r1 < 0) goto L_0x006f
        L_0x006d:
            r1 = r5
            goto L_0x007a
        L_0x006f:
            r1 = r4
            goto L_0x007a
        L_0x0071:
            boolean r1 = r10.mAdded
            if (r1 == 0) goto L_0x006f
            boolean r1 = r10.mHidden
            if (r1 != 0) goto L_0x006f
            goto L_0x006d
        L_0x007a:
            r13 = r1
            r1 = r4
            r12 = r5
            goto L_0x0092
        L_0x007e:
            if (r20 == 0) goto L_0x0083
            boolean r1 = r10.mIsNewlyAdded
            goto L_0x008e
        L_0x0083:
            boolean r1 = r10.mAdded
            if (r1 != 0) goto L_0x008d
            boolean r1 = r10.mHidden
            if (r1 != 0) goto L_0x008d
        L_0x008b:
            r1 = r5
            goto L_0x008e
        L_0x008d:
            r1 = r4
        L_0x008e:
            r12 = r4
            r13 = r12
            r4 = r1
            r1 = r5
        L_0x0092:
            java.lang.Object r6 = r2.get(r11)
            android.support.v4.app.FragmentTransition$FragmentContainerTransition r6 = (android.support.p001v4.app.FragmentTransition.FragmentContainerTransition) r6
            if (r4 == 0) goto L_0x00a4
            android.support.v4.app.FragmentTransition$FragmentContainerTransition r6 = ensureContainer(r6, r2, r11)
            r6.lastIn = r10
            r6.lastInIsPop = r3
            r6.lastInTransaction = r0
        L_0x00a4:
            r14 = r6
            r9 = 0
            if (r20 != 0) goto L_0x00cc
            if (r1 == 0) goto L_0x00cc
            if (r14 == 0) goto L_0x00b2
            android.support.v4.app.Fragment r1 = r14.firstOut
            if (r1 != r10) goto L_0x00b2
            r14.firstOut = r9
        L_0x00b2:
            android.support.v4.app.FragmentManagerImpl r4 = r0.mManager
            int r1 = r10.mState
            if (r1 >= r5) goto L_0x00cc
            int r1 = r4.mCurState
            if (r1 < r5) goto L_0x00cc
            boolean r1 = r0.mReorderingAllowed
            if (r1 != 0) goto L_0x00cc
            r4.makeActive(r10)
            r6 = 1
            r7 = 0
            r8 = 0
            r1 = 0
            r5 = r10
            r9 = r1
            r4.moveToState(r5, r6, r7, r8, r9)
        L_0x00cc:
            if (r13 == 0) goto L_0x00de
            if (r14 == 0) goto L_0x00d4
            android.support.v4.app.Fragment r1 = r14.firstOut
            if (r1 != 0) goto L_0x00de
        L_0x00d4:
            android.support.v4.app.FragmentTransition$FragmentContainerTransition r14 = ensureContainer(r14, r2, r11)
            r14.firstOut = r10
            r14.firstOutIsPop = r3
            r14.firstOutTransaction = r0
        L_0x00de:
            if (r20 != 0) goto L_0x00eb
            if (r12 == 0) goto L_0x00eb
            if (r14 == 0) goto L_0x00eb
            android.support.v4.app.Fragment r0 = r14.lastIn
            if (r0 != r10) goto L_0x00eb
            r0 = 0
            r14.lastIn = r0
        L_0x00eb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p001v4.app.FragmentTransition.addToFirstInLastOut(android.support.v4.app.BackStackRecord, android.support.v4.app.BackStackRecord$Op, android.util.SparseArray, boolean, boolean):void");
    }

    private static FragmentContainerTransition ensureContainer(FragmentContainerTransition fragmentContainerTransition, SparseArray<FragmentContainerTransition> sparseArray, int i) {
        if (fragmentContainerTransition != null) {
            return fragmentContainerTransition;
        }
        FragmentContainerTransition fragmentContainerTransition2 = new FragmentContainerTransition();
        sparseArray.put(i, fragmentContainerTransition2);
        return fragmentContainerTransition2;
    }
}
