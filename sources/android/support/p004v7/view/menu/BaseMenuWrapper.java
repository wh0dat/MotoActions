package android.support.p004v7.view.menu;

import android.content.Context;
import android.support.p001v4.internal.view.SupportMenuItem;
import android.support.p001v4.internal.view.SupportSubMenu;
import android.support.p001v4.util.ArrayMap;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.Iterator;
import java.util.Map;

/* renamed from: android.support.v7.view.menu.BaseMenuWrapper */
abstract class BaseMenuWrapper<T> extends BaseWrapper<T> {
    final Context mContext;
    private Map<SupportMenuItem, MenuItem> mMenuItems;
    private Map<SupportSubMenu, SubMenu> mSubMenus;

    BaseMenuWrapper(Context context, T t) {
        super(t);
        this.mContext = context;
    }

    /* access modifiers changed from: 0000 */
    public final MenuItem getMenuItemWrapper(MenuItem menuItem) {
        if (!(menuItem instanceof SupportMenuItem)) {
            return menuItem;
        }
        SupportMenuItem supportMenuItem = (SupportMenuItem) menuItem;
        if (this.mMenuItems == null) {
            this.mMenuItems = new ArrayMap();
        }
        MenuItem menuItem2 = (MenuItem) this.mMenuItems.get(menuItem);
        if (menuItem2 == null) {
            menuItem2 = MenuWrapperFactory.wrapSupportMenuItem(this.mContext, supportMenuItem);
            this.mMenuItems.put(supportMenuItem, menuItem2);
        }
        return menuItem2;
    }

    /* access modifiers changed from: 0000 */
    public final SubMenu getSubMenuWrapper(SubMenu subMenu) {
        if (!(subMenu instanceof SupportSubMenu)) {
            return subMenu;
        }
        SupportSubMenu supportSubMenu = (SupportSubMenu) subMenu;
        if (this.mSubMenus == null) {
            this.mSubMenus = new ArrayMap();
        }
        SubMenu subMenu2 = (SubMenu) this.mSubMenus.get(supportSubMenu);
        if (subMenu2 == null) {
            subMenu2 = MenuWrapperFactory.wrapSupportSubMenu(this.mContext, supportSubMenu);
            this.mSubMenus.put(supportSubMenu, subMenu2);
        }
        return subMenu2;
    }

    /* access modifiers changed from: 0000 */
    public final void internalClear() {
        if (this.mMenuItems != null) {
            this.mMenuItems.clear();
        }
        if (this.mSubMenus != null) {
            this.mSubMenus.clear();
        }
    }

    /* access modifiers changed from: 0000 */
    public final void internalRemoveGroup(int i) {
        if (this.mMenuItems != null) {
            Iterator it = this.mMenuItems.keySet().iterator();
            while (it.hasNext()) {
                if (i == ((MenuItem) it.next()).getGroupId()) {
                    it.remove();
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final void internalRemoveItem(int i) {
        if (this.mMenuItems != null) {
            Iterator it = this.mMenuItems.keySet().iterator();
            while (true) {
                if (it.hasNext()) {
                    if (i == ((MenuItem) it.next()).getItemId()) {
                        it.remove();
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }
}
