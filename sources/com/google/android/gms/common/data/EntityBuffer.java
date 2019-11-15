package com.google.android.gms.common.data;

import java.util.ArrayList;

public abstract class EntityBuffer<T> extends AbstractDataBuffer<T> {
    private boolean zzoa = false;
    private ArrayList<Integer> zzob;

    protected EntityBuffer(DataHolder dataHolder) {
        super(dataHolder);
    }

    private final void zzck() {
        synchronized (this) {
            if (!this.zzoa) {
                int count = this.mDataHolder.getCount();
                this.zzob = new ArrayList<>();
                if (count > 0) {
                    this.zzob.add(Integer.valueOf(0));
                    String primaryDataMarkerColumn = getPrimaryDataMarkerColumn();
                    String string = this.mDataHolder.getString(primaryDataMarkerColumn, 0, this.mDataHolder.getWindowIndex(0));
                    for (int i = 1; i < count; i++) {
                        int windowIndex = this.mDataHolder.getWindowIndex(i);
                        String string2 = this.mDataHolder.getString(primaryDataMarkerColumn, i, windowIndex);
                        if (string2 == null) {
                            StringBuilder sb = new StringBuilder(String.valueOf(primaryDataMarkerColumn).length() + 78);
                            sb.append("Missing value for markerColumn: ");
                            sb.append(primaryDataMarkerColumn);
                            sb.append(", at row: ");
                            sb.append(i);
                            sb.append(", for window: ");
                            sb.append(windowIndex);
                            throw new NullPointerException(sb.toString());
                        }
                        if (!string2.equals(string)) {
                            this.zzob.add(Integer.valueOf(i));
                            string = string2;
                        }
                    }
                }
                this.zzoa = true;
            }
        }
    }

    public final T get(int i) {
        zzck();
        return getEntry(zzi(i), getChildCount(i));
    }

    /* access modifiers changed from: protected */
    public int getChildCount(int i) {
        if (i < 0 || i == this.zzob.size()) {
            return 0;
        }
        int count = (i == this.zzob.size() - 1 ? this.mDataHolder.getCount() : ((Integer) this.zzob.get(i + 1)).intValue()) - ((Integer) this.zzob.get(i)).intValue();
        if (count == 1) {
            int zzi = zzi(i);
            int windowIndex = this.mDataHolder.getWindowIndex(zzi);
            String childDataMarkerColumn = getChildDataMarkerColumn();
            if (childDataMarkerColumn == null || this.mDataHolder.getString(childDataMarkerColumn, zzi, windowIndex) != null) {
                return count;
            }
            return 0;
        }
        return count;
    }

    /* access modifiers changed from: protected */
    public String getChildDataMarkerColumn() {
        return null;
    }

    public int getCount() {
        zzck();
        return this.zzob.size();
    }

    /* access modifiers changed from: protected */
    public abstract T getEntry(int i, int i2);

    /* access modifiers changed from: protected */
    public abstract String getPrimaryDataMarkerColumn();

    /* access modifiers changed from: 0000 */
    public final int zzi(int i) {
        if (i >= 0 && i < this.zzob.size()) {
            return ((Integer) this.zzob.get(i)).intValue();
        }
        StringBuilder sb = new StringBuilder(53);
        sb.append("Position ");
        sb.append(i);
        sb.append(" is out of bounds for this buffer");
        throw new IllegalArgumentException(sb.toString());
    }
}
