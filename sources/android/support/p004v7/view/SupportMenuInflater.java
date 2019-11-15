package android.support.p004v7.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.internal.view.SupportMenu;
import android.support.p001v4.view.ActionProvider;
import android.support.p001v4.view.MenuItemCompat;
import android.support.p004v7.appcompat.C0383R;
import android.support.p004v7.view.menu.MenuItemImpl;
import android.support.p004v7.view.menu.MenuItemWrapperICS;
import android.support.p004v7.widget.DrawableUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v7.view.SupportMenuInflater */
public class SupportMenuInflater extends MenuInflater {
    static final Class<?>[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
    static final Class<?>[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE = {Context.class};
    static final String LOG_TAG = "SupportMenuInflater";
    static final int NO_ID = 0;
    private static final String XML_GROUP = "group";
    private static final String XML_ITEM = "item";
    private static final String XML_MENU = "menu";
    final Object[] mActionProviderConstructorArguments = this.mActionViewConstructorArguments;
    final Object[] mActionViewConstructorArguments;
    Context mContext;
    private Object mRealOwner;

    /* renamed from: android.support.v7.view.SupportMenuInflater$InflatedOnMenuItemClickListener */
    private static class InflatedOnMenuItemClickListener implements OnMenuItemClickListener {
        private static final Class<?>[] PARAM_TYPES = {MenuItem.class};
        private Method mMethod;
        private Object mRealOwner;

        public InflatedOnMenuItemClickListener(Object obj, String str) {
            this.mRealOwner = obj;
            Class cls = obj.getClass();
            try {
                this.mMethod = cls.getMethod(str, PARAM_TYPES);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Couldn't resolve menu item onClick handler ");
                sb.append(str);
                sb.append(" in class ");
                sb.append(cls.getName());
                InflateException inflateException = new InflateException(sb.toString());
                inflateException.initCause(e);
                throw inflateException;
            }
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            try {
                if (this.mMethod.getReturnType() == Boolean.TYPE) {
                    return ((Boolean) this.mMethod.invoke(this.mRealOwner, new Object[]{menuItem})).booleanValue();
                }
                this.mMethod.invoke(this.mRealOwner, new Object[]{menuItem});
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* renamed from: android.support.v7.view.SupportMenuInflater$MenuState */
    private class MenuState {
        private static final int defaultGroupId = 0;
        private static final int defaultItemCategory = 0;
        private static final int defaultItemCheckable = 0;
        private static final boolean defaultItemChecked = false;
        private static final boolean defaultItemEnabled = true;
        private static final int defaultItemId = 0;
        private static final int defaultItemOrder = 0;
        private static final boolean defaultItemVisible = true;
        private int groupCategory;
        private int groupCheckable;
        private boolean groupEnabled;
        private int groupId;
        private int groupOrder;
        private boolean groupVisible;
        ActionProvider itemActionProvider;
        private String itemActionProviderClassName;
        private String itemActionViewClassName;
        private int itemActionViewLayout;
        private boolean itemAdded;
        private int itemAlphabeticModifiers;
        private char itemAlphabeticShortcut;
        private int itemCategoryOrder;
        private int itemCheckable;
        private boolean itemChecked;
        private CharSequence itemContentDescription;
        private boolean itemEnabled;
        private int itemIconResId;
        private ColorStateList itemIconTintList = null;
        private Mode itemIconTintMode = null;
        private int itemId;
        private String itemListenerMethodName;
        private int itemNumericModifiers;
        private char itemNumericShortcut;
        private int itemShowAsAction;
        private CharSequence itemTitle;
        private CharSequence itemTitleCondensed;
        private CharSequence itemTooltipText;
        private boolean itemVisible;
        private Menu menu;

        public MenuState(Menu menu2) {
            this.menu = menu2;
            resetGroup();
        }

        public void resetGroup() {
            this.groupId = 0;
            this.groupCategory = 0;
            this.groupOrder = 0;
            this.groupCheckable = 0;
            this.groupVisible = true;
            this.groupEnabled = true;
        }

        public void readGroup(AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = SupportMenuInflater.this.mContext.obtainStyledAttributes(attributeSet, C0383R.styleable.MenuGroup);
            this.groupId = obtainStyledAttributes.getResourceId(C0383R.styleable.MenuGroup_android_id, 0);
            this.groupCategory = obtainStyledAttributes.getInt(C0383R.styleable.MenuGroup_android_menuCategory, 0);
            this.groupOrder = obtainStyledAttributes.getInt(C0383R.styleable.MenuGroup_android_orderInCategory, 0);
            this.groupCheckable = obtainStyledAttributes.getInt(C0383R.styleable.MenuGroup_android_checkableBehavior, 0);
            this.groupVisible = obtainStyledAttributes.getBoolean(C0383R.styleable.MenuGroup_android_visible, true);
            this.groupEnabled = obtainStyledAttributes.getBoolean(C0383R.styleable.MenuGroup_android_enabled, true);
            obtainStyledAttributes.recycle();
        }

        public void readItem(AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = SupportMenuInflater.this.mContext.obtainStyledAttributes(attributeSet, C0383R.styleable.MenuItem);
            this.itemId = obtainStyledAttributes.getResourceId(C0383R.styleable.MenuItem_android_id, 0);
            this.itemCategoryOrder = (obtainStyledAttributes.getInt(C0383R.styleable.MenuItem_android_menuCategory, this.groupCategory) & SupportMenu.CATEGORY_MASK) | (obtainStyledAttributes.getInt(C0383R.styleable.MenuItem_android_orderInCategory, this.groupOrder) & SupportMenu.USER_MASK);
            this.itemTitle = obtainStyledAttributes.getText(C0383R.styleable.MenuItem_android_title);
            this.itemTitleCondensed = obtainStyledAttributes.getText(C0383R.styleable.MenuItem_android_titleCondensed);
            this.itemIconResId = obtainStyledAttributes.getResourceId(C0383R.styleable.MenuItem_android_icon, 0);
            this.itemAlphabeticShortcut = getShortcut(obtainStyledAttributes.getString(C0383R.styleable.MenuItem_android_alphabeticShortcut));
            this.itemAlphabeticModifiers = obtainStyledAttributes.getInt(C0383R.styleable.MenuItem_alphabeticModifiers, 4096);
            this.itemNumericShortcut = getShortcut(obtainStyledAttributes.getString(C0383R.styleable.MenuItem_android_numericShortcut));
            this.itemNumericModifiers = obtainStyledAttributes.getInt(C0383R.styleable.MenuItem_numericModifiers, 4096);
            if (obtainStyledAttributes.hasValue(C0383R.styleable.MenuItem_android_checkable)) {
                this.itemCheckable = obtainStyledAttributes.getBoolean(C0383R.styleable.MenuItem_android_checkable, false) ? 1 : 0;
            } else {
                this.itemCheckable = this.groupCheckable;
            }
            this.itemChecked = obtainStyledAttributes.getBoolean(C0383R.styleable.MenuItem_android_checked, false);
            this.itemVisible = obtainStyledAttributes.getBoolean(C0383R.styleable.MenuItem_android_visible, this.groupVisible);
            this.itemEnabled = obtainStyledAttributes.getBoolean(C0383R.styleable.MenuItem_android_enabled, this.groupEnabled);
            this.itemShowAsAction = obtainStyledAttributes.getInt(C0383R.styleable.MenuItem_showAsAction, -1);
            this.itemListenerMethodName = obtainStyledAttributes.getString(C0383R.styleable.MenuItem_android_onClick);
            this.itemActionViewLayout = obtainStyledAttributes.getResourceId(C0383R.styleable.MenuItem_actionLayout, 0);
            this.itemActionViewClassName = obtainStyledAttributes.getString(C0383R.styleable.MenuItem_actionViewClass);
            this.itemActionProviderClassName = obtainStyledAttributes.getString(C0383R.styleable.MenuItem_actionProviderClass);
            boolean z = this.itemActionProviderClassName != null;
            if (z && this.itemActionViewLayout == 0 && this.itemActionViewClassName == null) {
                this.itemActionProvider = (ActionProvider) newInstance(this.itemActionProviderClassName, SupportMenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionProviderConstructorArguments);
            } else {
                if (z) {
                    Log.w(SupportMenuInflater.LOG_TAG, "Ignoring attribute 'actionProviderClass'. Action view already specified.");
                }
                this.itemActionProvider = null;
            }
            this.itemContentDescription = obtainStyledAttributes.getText(C0383R.styleable.MenuItem_contentDescription);
            this.itemTooltipText = obtainStyledAttributes.getText(C0383R.styleable.MenuItem_tooltipText);
            if (obtainStyledAttributes.hasValue(C0383R.styleable.MenuItem_iconTintMode)) {
                this.itemIconTintMode = DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(C0383R.styleable.MenuItem_iconTintMode, -1), this.itemIconTintMode);
            } else {
                this.itemIconTintMode = null;
            }
            if (obtainStyledAttributes.hasValue(C0383R.styleable.MenuItem_iconTint)) {
                this.itemIconTintList = obtainStyledAttributes.getColorStateList(C0383R.styleable.MenuItem_iconTint);
            } else {
                this.itemIconTintList = null;
            }
            obtainStyledAttributes.recycle();
            this.itemAdded = false;
        }

        private char getShortcut(String str) {
            if (str == null) {
                return 0;
            }
            return str.charAt(0);
        }

        private void setItem(MenuItem menuItem) {
            boolean z = false;
            menuItem.setChecked(this.itemChecked).setVisible(this.itemVisible).setEnabled(this.itemEnabled).setCheckable(this.itemCheckable >= 1).setTitleCondensed(this.itemTitleCondensed).setIcon(this.itemIconResId);
            if (this.itemShowAsAction >= 0) {
                menuItem.setShowAsAction(this.itemShowAsAction);
            }
            if (this.itemListenerMethodName != null) {
                if (SupportMenuInflater.this.mContext.isRestricted()) {
                    throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
                }
                menuItem.setOnMenuItemClickListener(new InflatedOnMenuItemClickListener(SupportMenuInflater.this.getRealOwner(), this.itemListenerMethodName));
            }
            boolean z2 = menuItem instanceof MenuItemImpl;
            if (z2) {
                MenuItemImpl menuItemImpl = (MenuItemImpl) menuItem;
            }
            if (this.itemCheckable >= 2) {
                if (z2) {
                    ((MenuItemImpl) menuItem).setExclusiveCheckable(true);
                } else if (menuItem instanceof MenuItemWrapperICS) {
                    ((MenuItemWrapperICS) menuItem).setExclusiveCheckable(true);
                }
            }
            if (this.itemActionViewClassName != null) {
                menuItem.setActionView((View) newInstance(this.itemActionViewClassName, SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionViewConstructorArguments));
                z = true;
            }
            if (this.itemActionViewLayout > 0) {
                if (!z) {
                    menuItem.setActionView(this.itemActionViewLayout);
                } else {
                    Log.w(SupportMenuInflater.LOG_TAG, "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
                }
            }
            if (this.itemActionProvider != null) {
                MenuItemCompat.setActionProvider(menuItem, this.itemActionProvider);
            }
            MenuItemCompat.setContentDescription(menuItem, this.itemContentDescription);
            MenuItemCompat.setTooltipText(menuItem, this.itemTooltipText);
            MenuItemCompat.setAlphabeticShortcut(menuItem, this.itemAlphabeticShortcut, this.itemAlphabeticModifiers);
            MenuItemCompat.setNumericShortcut(menuItem, this.itemNumericShortcut, this.itemNumericModifiers);
            if (this.itemIconTintMode != null) {
                MenuItemCompat.setIconTintMode(menuItem, this.itemIconTintMode);
            }
            if (this.itemIconTintList != null) {
                MenuItemCompat.setIconTintList(menuItem, this.itemIconTintList);
            }
        }

        public void addItem() {
            this.itemAdded = true;
            setItem(this.menu.add(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle));
        }

        public SubMenu addSubMenuItem() {
            this.itemAdded = true;
            SubMenu addSubMenu = this.menu.addSubMenu(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
            setItem(addSubMenu.getItem());
            return addSubMenu;
        }

        public boolean hasAddedItem() {
            return this.itemAdded;
        }

        private <T> T newInstance(String str, Class<?>[] clsArr, Object[] objArr) {
            try {
                Constructor constructor = SupportMenuInflater.this.mContext.getClassLoader().loadClass(str).getConstructor(clsArr);
                constructor.setAccessible(true);
                return constructor.newInstance(objArr);
            } catch (Exception e) {
                String str2 = SupportMenuInflater.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot instantiate class: ");
                sb.append(str);
                Log.w(str2, sb.toString(), e);
                return null;
            }
        }
    }

    public SupportMenuInflater(Context context) {
        super(context);
        this.mContext = context;
        this.mActionViewConstructorArguments = new Object[]{context};
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void inflate(int r3, android.view.Menu r4) {
        /*
            r2 = this;
            boolean r0 = r4 instanceof android.support.p001v4.internal.view.SupportMenu
            if (r0 != 0) goto L_0x0008
            super.inflate(r3, r4)
            return
        L_0x0008:
            r0 = 0
            android.content.Context r1 = r2.mContext     // Catch:{ XmlPullParserException -> 0x0034, IOException -> 0x002b }
            android.content.res.Resources r1 = r1.getResources()     // Catch:{ XmlPullParserException -> 0x0034, IOException -> 0x002b }
            android.content.res.XmlResourceParser r3 = r1.getLayout(r3)     // Catch:{ XmlPullParserException -> 0x0034, IOException -> 0x002b }
            android.util.AttributeSet r0 = android.util.Xml.asAttributeSet(r3)     // Catch:{ XmlPullParserException -> 0x0026, IOException -> 0x0023, all -> 0x0020 }
            r2.parseMenu(r3, r0, r4)     // Catch:{ XmlPullParserException -> 0x0026, IOException -> 0x0023, all -> 0x0020 }
            if (r3 == 0) goto L_0x001f
            r3.close()
        L_0x001f:
            return
        L_0x0020:
            r2 = move-exception
            r0 = r3
            goto L_0x003d
        L_0x0023:
            r2 = move-exception
            r0 = r3
            goto L_0x002c
        L_0x0026:
            r2 = move-exception
            r0 = r3
            goto L_0x0035
        L_0x0029:
            r2 = move-exception
            goto L_0x003d
        L_0x002b:
            r2 = move-exception
        L_0x002c:
            android.view.InflateException r3 = new android.view.InflateException     // Catch:{ all -> 0x0029 }
            java.lang.String r4 = "Error inflating menu XML"
            r3.<init>(r4, r2)     // Catch:{ all -> 0x0029 }
            throw r3     // Catch:{ all -> 0x0029 }
        L_0x0034:
            r2 = move-exception
        L_0x0035:
            android.view.InflateException r3 = new android.view.InflateException     // Catch:{ all -> 0x0029 }
            java.lang.String r4 = "Error inflating menu XML"
            r3.<init>(r4, r2)     // Catch:{ all -> 0x0029 }
            throw r3     // Catch:{ all -> 0x0029 }
        L_0x003d:
            if (r0 == 0) goto L_0x0042
            r0.close()
        L_0x0042:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.view.SupportMenuInflater.inflate(int, android.view.Menu):void");
    }

    private void parseMenu(XmlPullParser xmlPullParser, AttributeSet attributeSet, Menu menu) throws XmlPullParserException, IOException {
        MenuState menuState = new MenuState(menu);
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType != 2) {
                eventType = xmlPullParser.next();
                if (eventType == 1) {
                    break;
                }
            } else {
                String name = xmlPullParser.getName();
                if (name.equals(XML_MENU)) {
                    eventType = xmlPullParser.next();
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Expecting menu, got ");
                    sb.append(name);
                    throw new RuntimeException(sb.toString());
                }
            }
        }
        int i = eventType;
        String str = null;
        boolean z = false;
        boolean z2 = false;
        while (!z) {
            switch (i) {
                case 1:
                    throw new RuntimeException("Unexpected end of document");
                case 2:
                    if (!z2) {
                        String name2 = xmlPullParser.getName();
                        if (!name2.equals(XML_GROUP)) {
                            if (!name2.equals(XML_ITEM)) {
                                if (!name2.equals(XML_MENU)) {
                                    z2 = true;
                                    str = name2;
                                    break;
                                } else {
                                    parseMenu(xmlPullParser, attributeSet, menuState.addSubMenuItem());
                                    break;
                                }
                            } else {
                                menuState.readItem(attributeSet);
                                break;
                            }
                        } else {
                            menuState.readGroup(attributeSet);
                            break;
                        }
                    } else {
                        break;
                    }
                case 3:
                    String name3 = xmlPullParser.getName();
                    if (!z2 || !name3.equals(str)) {
                        if (!name3.equals(XML_GROUP)) {
                            if (!name3.equals(XML_ITEM)) {
                                if (!name3.equals(XML_MENU)) {
                                    break;
                                } else {
                                    z = true;
                                    break;
                                }
                            } else if (!menuState.hasAddedItem()) {
                                if (menuState.itemActionProvider != null && menuState.itemActionProvider.hasSubMenu()) {
                                    menuState.addSubMenuItem();
                                    break;
                                } else {
                                    menuState.addItem();
                                    break;
                                }
                            } else {
                                break;
                            }
                        } else {
                            menuState.resetGroup();
                            break;
                        }
                    } else {
                        str = null;
                        z2 = false;
                        break;
                    }
            }
            i = xmlPullParser.next();
        }
    }

    /* access modifiers changed from: 0000 */
    public Object getRealOwner() {
        if (this.mRealOwner == null) {
            this.mRealOwner = findRealOwner(this.mContext);
        }
        return this.mRealOwner;
    }

    private Object findRealOwner(Object obj) {
        return (!(obj instanceof Activity) && (obj instanceof ContextWrapper)) ? findRealOwner(((ContextWrapper) obj).getBaseContext()) : obj;
    }
}
