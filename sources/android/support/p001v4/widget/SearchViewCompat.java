package android.support.p001v4.widget;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import android.widget.SearchView;

@Deprecated
/* renamed from: android.support.v4.widget.SearchViewCompat */
public final class SearchViewCompat {

    @Deprecated
    /* renamed from: android.support.v4.widget.SearchViewCompat$OnCloseListener */
    public interface OnCloseListener {
        boolean onClose();
    }

    @Deprecated
    /* renamed from: android.support.v4.widget.SearchViewCompat$OnCloseListenerCompat */
    public static abstract class OnCloseListenerCompat implements OnCloseListener {
        public boolean onClose() {
            return false;
        }
    }

    @Deprecated
    /* renamed from: android.support.v4.widget.SearchViewCompat$OnQueryTextListener */
    public interface OnQueryTextListener {
        boolean onQueryTextChange(String str);

        boolean onQueryTextSubmit(String str);
    }

    @Deprecated
    /* renamed from: android.support.v4.widget.SearchViewCompat$OnQueryTextListenerCompat */
    public static abstract class OnQueryTextListenerCompat implements OnQueryTextListener {
        public boolean onQueryTextChange(String str) {
            return false;
        }

        public boolean onQueryTextSubmit(String str) {
            return false;
        }
    }

    private static void checkIfLegalArg(View view) {
        if (view == null) {
            throw new IllegalArgumentException("searchView must be non-null");
        } else if (!(view instanceof SearchView)) {
            throw new IllegalArgumentException("searchView must be an instance of android.widget.SearchView");
        }
    }

    private SearchViewCompat(Context context) {
    }

    @Deprecated
    public static View newSearchView(Context context) {
        return new SearchView(context);
    }

    @Deprecated
    public static void setSearchableInfo(View view, ComponentName componentName) {
        checkIfLegalArg(view);
        ((SearchView) view).setSearchableInfo(((SearchManager) view.getContext().getSystemService("search")).getSearchableInfo(componentName));
    }

    @Deprecated
    public static void setImeOptions(View view, int i) {
        checkIfLegalArg(view);
        ((SearchView) view).setImeOptions(i);
    }

    @Deprecated
    public static void setInputType(View view, int i) {
        checkIfLegalArg(view);
        ((SearchView) view).setInputType(i);
    }

    @Deprecated
    public static void setOnQueryTextListener(View view, OnQueryTextListener onQueryTextListener) {
        checkIfLegalArg(view);
        ((SearchView) view).setOnQueryTextListener(newOnQueryTextListener(onQueryTextListener));
    }

    private static android.widget.SearchView.OnQueryTextListener newOnQueryTextListener(final OnQueryTextListener onQueryTextListener) {
        return new android.widget.SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return onQueryTextListener.onQueryTextSubmit(str);
            }

            public boolean onQueryTextChange(String str) {
                return onQueryTextListener.onQueryTextChange(str);
            }
        };
    }

    @Deprecated
    public static void setOnCloseListener(View view, OnCloseListener onCloseListener) {
        checkIfLegalArg(view);
        ((SearchView) view).setOnCloseListener(newOnCloseListener(onCloseListener));
    }

    private static android.widget.SearchView.OnCloseListener newOnCloseListener(final OnCloseListener onCloseListener) {
        return new android.widget.SearchView.OnCloseListener() {
            public boolean onClose() {
                return onCloseListener.onClose();
            }
        };
    }

    @Deprecated
    public static CharSequence getQuery(View view) {
        checkIfLegalArg(view);
        return ((SearchView) view).getQuery();
    }

    @Deprecated
    public static void setQuery(View view, CharSequence charSequence, boolean z) {
        checkIfLegalArg(view);
        ((SearchView) view).setQuery(charSequence, z);
    }

    @Deprecated
    public static void setQueryHint(View view, CharSequence charSequence) {
        checkIfLegalArg(view);
        ((SearchView) view).setQueryHint(charSequence);
    }

    @Deprecated
    public static void setIconified(View view, boolean z) {
        checkIfLegalArg(view);
        ((SearchView) view).setIconified(z);
    }

    @Deprecated
    public static boolean isIconified(View view) {
        checkIfLegalArg(view);
        return ((SearchView) view).isIconified();
    }

    @Deprecated
    public static void setSubmitButtonEnabled(View view, boolean z) {
        checkIfLegalArg(view);
        ((SearchView) view).setSubmitButtonEnabled(z);
    }

    @Deprecated
    public static boolean isSubmitButtonEnabled(View view) {
        checkIfLegalArg(view);
        return ((SearchView) view).isSubmitButtonEnabled();
    }

    @Deprecated
    public static void setQueryRefinementEnabled(View view, boolean z) {
        checkIfLegalArg(view);
        ((SearchView) view).setQueryRefinementEnabled(z);
    }

    @Deprecated
    public static boolean isQueryRefinementEnabled(View view) {
        checkIfLegalArg(view);
        return ((SearchView) view).isQueryRefinementEnabled();
    }

    @Deprecated
    public static void setMaxWidth(View view, int i) {
        checkIfLegalArg(view);
        ((SearchView) view).setMaxWidth(i);
    }
}
