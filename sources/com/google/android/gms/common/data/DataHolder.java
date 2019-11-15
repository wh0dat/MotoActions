package com.google.android.gms.common.data;

import android.content.ContentValues;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import com.google.android.gms.common.sqlite.CursorWrapper;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@KeepName
@Class(creator = "DataHolderCreator", validate = true)
public final class DataHolder extends AbstractSafeParcelable implements Closeable {
    public static final Creator<DataHolder> CREATOR = new DataHolderCreator();
    private static final Builder zznt = new zza(new String[0], null);
    private boolean mClosed;
    @VersionField(mo9711id = 1000)
    private final int zzal;
    @Field(getter = "getStatusCode", mo9705id = 3)
    private final int zzam;
    @Field(getter = "getColumns", mo9705id = 1)
    private final String[] zznm;
    private Bundle zznn;
    @Field(getter = "getWindows", mo9705id = 2)
    private final CursorWindow[] zzno;
    @Field(getter = "getMetadata", mo9705id = 4)
    private final Bundle zznp;
    private int[] zznq;
    private int zznr;
    private boolean zzns;

    public static class Builder {
        /* access modifiers changed from: private */
        public final String[] zznm;
        /* access modifiers changed from: private */
        public final ArrayList<HashMap<String, Object>> zznu;
        private final String zznv;
        private final HashMap<Object, Integer> zznw;
        private boolean zznx;
        private String zzny;

        private Builder(String[] strArr, String str) {
            this.zznm = (String[]) Preconditions.checkNotNull(strArr);
            this.zznu = new ArrayList<>();
            this.zznv = str;
            this.zznw = new HashMap<>();
            this.zznx = false;
            this.zzny = null;
        }

        /* synthetic */ Builder(String[] strArr, String str, zza zza) {
            this(strArr, str);
        }

        private final boolean zzg(String str) {
            Asserts.checkNotNull(str);
            return this.zznx && str.equals(this.zzny);
        }

        public DataHolder build(int i) {
            return new DataHolder(this, i, (Bundle) null, (zza) null);
        }

        public DataHolder build(int i, Bundle bundle) {
            DataHolder dataHolder = new DataHolder(this, i, bundle, -1, (zza) null);
            return dataHolder;
        }

        public DataHolder build(int i, Bundle bundle, int i2) {
            DataHolder dataHolder = new DataHolder(this, i, bundle, i2, (zza) null);
            return dataHolder;
        }

        public boolean containsRowWithValue(String str, Object obj) {
            int size = this.zznu.size();
            for (int i = 0; i < size; i++) {
                if (Objects.equal(((HashMap) this.zznu.get(i)).get(str), obj)) {
                    return true;
                }
            }
            return false;
        }

        public Builder descendingSort(String str) {
            if (zzg(str)) {
                return this;
            }
            sort(str);
            Collections.reverse(this.zznu);
            return this;
        }

        public int getCount() {
            return this.zznu.size();
        }

        public void modifyUniqueRowValue(Object obj, String str, Object obj2) {
            if (this.zznv != null) {
                Integer num = (Integer) this.zznw.get(obj);
                if (num != null) {
                    ((HashMap) this.zznu.get(num.intValue())).put(str, obj2);
                }
            }
        }

        public Builder removeRowsWithValue(String str, Object obj) {
            for (int size = this.zznu.size() - 1; size >= 0; size--) {
                if (Objects.equal(((HashMap) this.zznu.get(size)).get(str), obj)) {
                    this.zznu.remove(size);
                }
            }
            return this;
        }

        public Builder sort(String str) {
            if (zzg(str)) {
                return this;
            }
            Collections.sort(this.zznu, new zza(str));
            if (this.zznv != null) {
                this.zznw.clear();
                int size = this.zznu.size();
                for (int i = 0; i < size; i++) {
                    Object obj = ((HashMap) this.zznu.get(i)).get(this.zznv);
                    if (obj != null) {
                        this.zznw.put(obj, Integer.valueOf(i));
                    }
                }
            }
            this.zznx = true;
            this.zzny = str;
            return this;
        }

        public Builder withRow(ContentValues contentValues) {
            Asserts.checkNotNull(contentValues);
            HashMap hashMap = new HashMap(contentValues.size());
            for (Entry entry : contentValues.valueSet()) {
                hashMap.put((String) entry.getKey(), entry.getValue());
            }
            return withRow(hashMap);
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:0x0033  */
        /* JADX WARNING: Removed duplicated region for block: B:11:0x0039  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.google.android.gms.common.data.DataHolder.Builder withRow(java.util.HashMap<java.lang.String, java.lang.Object> r5) {
            /*
                r4 = this;
                com.google.android.gms.common.internal.Asserts.checkNotNull(r5)
                java.lang.String r0 = r4.zznv
                r1 = -1
                if (r0 != 0) goto L_0x000a
            L_0x0008:
                r0 = r1
                goto L_0x0031
            L_0x000a:
                java.lang.String r0 = r4.zznv
                java.lang.Object r0 = r5.get(r0)
                if (r0 != 0) goto L_0x0013
                goto L_0x0008
            L_0x0013:
                java.util.HashMap<java.lang.Object, java.lang.Integer> r2 = r4.zznw
                java.lang.Object r2 = r2.get(r0)
                java.lang.Integer r2 = (java.lang.Integer) r2
                if (r2 != 0) goto L_0x002d
                java.util.HashMap<java.lang.Object, java.lang.Integer> r2 = r4.zznw
                java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.Object>> r3 = r4.zznu
                int r3 = r3.size()
                java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
                r2.put(r0, r3)
                goto L_0x0008
            L_0x002d:
                int r0 = r2.intValue()
            L_0x0031:
                if (r0 != r1) goto L_0x0039
                java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.Object>> r0 = r4.zznu
                r0.add(r5)
                goto L_0x0043
            L_0x0039:
                java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.Object>> r1 = r4.zznu
                r1.remove(r0)
                java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.Object>> r1 = r4.zznu
                r1.add(r0, r5)
            L_0x0043:
                r5 = 0
                r4.zznx = r5
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.data.DataHolder.Builder.withRow(java.util.HashMap):com.google.android.gms.common.data.DataHolder$Builder");
        }
    }

    public static class DataHolderException extends RuntimeException {
        public DataHolderException(String str) {
            super(str);
        }
    }

    private static final class zza implements Comparator<HashMap<String, Object>> {
        private final String zznz;

        zza(String str) {
            this.zznz = (String) Preconditions.checkNotNull(str);
        }

        public final /* synthetic */ int compare(Object obj, Object obj2) {
            HashMap hashMap = (HashMap) obj2;
            Object checkNotNull = Preconditions.checkNotNull(((HashMap) obj).get(this.zznz));
            Object checkNotNull2 = Preconditions.checkNotNull(hashMap.get(this.zznz));
            if (checkNotNull.equals(checkNotNull2)) {
                return 0;
            }
            if (checkNotNull instanceof Boolean) {
                return ((Boolean) checkNotNull).compareTo((Boolean) checkNotNull2);
            }
            if (checkNotNull instanceof Long) {
                return ((Long) checkNotNull).compareTo((Long) checkNotNull2);
            }
            if (checkNotNull instanceof Integer) {
                return ((Integer) checkNotNull).compareTo((Integer) checkNotNull2);
            }
            if (checkNotNull instanceof String) {
                return ((String) checkNotNull).compareTo((String) checkNotNull2);
            }
            if (checkNotNull instanceof Double) {
                return ((Double) checkNotNull).compareTo((Double) checkNotNull2);
            }
            if (checkNotNull instanceof Float) {
                return ((Float) checkNotNull).compareTo((Float) checkNotNull2);
            }
            String valueOf = String.valueOf(checkNotNull);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 24);
            sb.append("Unknown type for lValue ");
            sb.append(valueOf);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    @Constructor
    DataHolder(@Param(mo9708id = 1000) int i, @Param(mo9708id = 1) String[] strArr, @Param(mo9708id = 2) CursorWindow[] cursorWindowArr, @Param(mo9708id = 3) int i2, @Param(mo9708id = 4) Bundle bundle) {
        this.mClosed = false;
        this.zzns = true;
        this.zzal = i;
        this.zznm = strArr;
        this.zzno = cursorWindowArr;
        this.zzam = i2;
        this.zznp = bundle;
    }

    public DataHolder(Cursor cursor, int i, Bundle bundle) {
        this(new CursorWrapper(cursor), i, bundle);
    }

    private DataHolder(Builder builder, int i, Bundle bundle) {
        this(builder.zznm, zza(builder, -1), i, bundle);
    }

    private DataHolder(Builder builder, int i, Bundle bundle, int i2) {
        this(builder.zznm, zza(builder, i2), i, bundle);
    }

    /* synthetic */ DataHolder(Builder builder, int i, Bundle bundle, int i2, zza zza2) {
        this(builder, i, bundle, i2);
    }

    /* synthetic */ DataHolder(Builder builder, int i, Bundle bundle, zza zza2) {
        this(builder, i, (Bundle) null);
    }

    public DataHolder(CursorWrapper cursorWrapper, int i, Bundle bundle) {
        this(cursorWrapper.getColumnNames(), zza(cursorWrapper), i, bundle);
    }

    public DataHolder(String[] strArr, CursorWindow[] cursorWindowArr, int i, Bundle bundle) {
        this.mClosed = false;
        this.zzns = true;
        this.zzal = 1;
        this.zznm = (String[]) Preconditions.checkNotNull(strArr);
        this.zzno = (CursorWindow[]) Preconditions.checkNotNull(cursorWindowArr);
        this.zzam = i;
        this.zznp = bundle;
        validateContents();
    }

    public static Builder builder(String[] strArr) {
        return new Builder(strArr, null, null);
    }

    public static Builder builder(String[] strArr, String str) {
        Preconditions.checkNotNull(str);
        return new Builder(strArr, str, null);
    }

    public static DataHolder empty(int i) {
        return empty(i, null);
    }

    public static DataHolder empty(int i, Bundle bundle) {
        return new DataHolder(zznt, i, bundle);
    }

    private final void zza(String str, int i) {
        if (this.zznn == null || !this.zznn.containsKey(str)) {
            String str2 = "No such column: ";
            String valueOf = String.valueOf(str);
            throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        } else if (isClosed()) {
            throw new IllegalArgumentException("Buffer is closed.");
        } else if (i < 0 || i >= this.zznr) {
            throw new CursorIndexOutOfBoundsException(i, this.zznr);
        }
    }

    private static CursorWindow[] zza(Builder builder, int i) {
        long j;
        if (builder.zznm.length == 0) {
            return new CursorWindow[0];
        }
        List zzb = (i < 0 || i >= builder.zznu.size()) ? builder.zznu : builder.zznu.subList(0, i);
        int size = zzb.size();
        CursorWindow cursorWindow = new CursorWindow(false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(cursorWindow);
        cursorWindow.setNumColumns(builder.zznm.length);
        boolean z = false;
        CursorWindow cursorWindow2 = cursorWindow;
        int i2 = 0;
        while (i2 < size) {
            try {
                if (!cursorWindow2.allocRow()) {
                    StringBuilder sb = new StringBuilder(72);
                    sb.append("Allocating additional cursor window for large data set (row ");
                    sb.append(i2);
                    sb.append(")");
                    Log.d("DataHolder", sb.toString());
                    cursorWindow2 = new CursorWindow(false);
                    cursorWindow2.setStartPosition(i2);
                    cursorWindow2.setNumColumns(builder.zznm.length);
                    arrayList.add(cursorWindow2);
                    if (!cursorWindow2.allocRow()) {
                        Log.e("DataHolder", "Unable to allocate row to hold data.");
                        arrayList.remove(cursorWindow2);
                        return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
                    }
                }
                Map map = (Map) zzb.get(i2);
                boolean z2 = true;
                for (int i3 = 0; i3 < builder.zznm.length && z2; i3++) {
                    String str = builder.zznm[i3];
                    Object obj = map.get(str);
                    if (obj == null) {
                        z2 = cursorWindow2.putNull(i2, i3);
                    } else if (obj instanceof String) {
                        z2 = cursorWindow2.putString((String) obj, i2, i3);
                    } else {
                        if (obj instanceof Long) {
                            j = ((Long) obj).longValue();
                        } else if (obj instanceof Integer) {
                            z2 = cursorWindow2.putLong((long) ((Integer) obj).intValue(), i2, i3);
                        } else if (obj instanceof Boolean) {
                            j = ((Boolean) obj).booleanValue() ? 1 : 0;
                        } else if (obj instanceof byte[]) {
                            z2 = cursorWindow2.putBlob((byte[]) obj, i2, i3);
                        } else if (obj instanceof Double) {
                            z2 = cursorWindow2.putDouble(((Double) obj).doubleValue(), i2, i3);
                        } else if (obj instanceof Float) {
                            z2 = cursorWindow2.putDouble((double) ((Float) obj).floatValue(), i2, i3);
                        } else {
                            String valueOf = String.valueOf(obj);
                            StringBuilder sb2 = new StringBuilder(String.valueOf(str).length() + 32 + String.valueOf(valueOf).length());
                            sb2.append("Unsupported object for column ");
                            sb2.append(str);
                            sb2.append(": ");
                            sb2.append(valueOf);
                            throw new IllegalArgumentException(sb2.toString());
                        }
                        z2 = cursorWindow2.putLong(j, i2, i3);
                    }
                }
                if (z2) {
                    z = false;
                } else if (z) {
                    throw new DataHolderException("Could not add the value to a new CursorWindow. The size of value may be larger than what a CursorWindow can handle.");
                } else {
                    StringBuilder sb3 = new StringBuilder(74);
                    sb3.append("Couldn't populate window data for row ");
                    sb3.append(i2);
                    sb3.append(" - allocating new window.");
                    Log.d("DataHolder", sb3.toString());
                    cursorWindow2.freeLastRow();
                    cursorWindow2 = new CursorWindow(false);
                    cursorWindow2.setStartPosition(i2);
                    cursorWindow2.setNumColumns(builder.zznm.length);
                    arrayList.add(cursorWindow2);
                    i2--;
                    z = true;
                }
                i2++;
            } catch (RuntimeException e) {
                int size2 = arrayList.size();
                for (int i4 = 0; i4 < size2; i4++) {
                    ((CursorWindow) arrayList.get(i4)).close();
                }
                throw e;
            }
        }
        return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
    }

    /* JADX INFO: finally extract failed */
    private static CursorWindow[] zza(CursorWrapper cursorWrapper) {
        int i;
        ArrayList arrayList = new ArrayList();
        try {
            int count = cursorWrapper.getCount();
            CursorWindow window = cursorWrapper.getWindow();
            if (window == null || window.getStartPosition() != 0) {
                i = 0;
            } else {
                window.acquireReference();
                cursorWrapper.setWindow(null);
                arrayList.add(window);
                i = window.getNumRows();
            }
            while (i < count && cursorWrapper.moveToPosition(i)) {
                CursorWindow window2 = cursorWrapper.getWindow();
                if (window2 != null) {
                    window2.acquireReference();
                    cursorWrapper.setWindow(null);
                } else {
                    window2 = new CursorWindow(false);
                    window2.setStartPosition(i);
                    cursorWrapper.fillWindow(i, window2);
                }
                if (window2.getNumRows() == 0) {
                    break;
                }
                arrayList.add(window2);
                i = window2.getStartPosition() + window2.getNumRows();
            }
            cursorWrapper.close();
            return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
        } catch (Throwable th) {
            cursorWrapper.close();
            throw th;
        }
    }

    public final void clearColumn(String str, int i, int i2) {
        zza(str, i);
        this.zzno[i2].putNull(i, this.zznn.getInt(str));
    }

    public final void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (CursorWindow close : this.zzno) {
                    close.close();
                }
            }
        }
    }

    public final void copyToBuffer(String str, int i, int i2, CharArrayBuffer charArrayBuffer) {
        zza(str, i);
        this.zzno[i2].copyStringToBuffer(i, this.zznn.getInt(str), charArrayBuffer);
    }

    public final void disableLeakDetection() {
        this.zzns = false;
    }

    /* access modifiers changed from: protected */
    public final void finalize() throws Throwable {
        try {
            if (this.zzns && this.zzno.length > 0 && !isClosed()) {
                close();
                String obj = toString();
                StringBuilder sb = new StringBuilder(String.valueOf(obj).length() + 178);
                sb.append("Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (internal object: ");
                sb.append(obj);
                sb.append(")");
                Log.e("DataBuffer", sb.toString());
            }
        } finally {
            super.finalize();
        }
    }

    public final boolean getBoolean(String str, int i, int i2) {
        zza(str, i);
        return Long.valueOf(this.zzno[i2].getLong(i, this.zznn.getInt(str))).longValue() == 1;
    }

    public final byte[] getByteArray(String str, int i, int i2) {
        zza(str, i);
        return this.zzno[i2].getBlob(i, this.zznn.getInt(str));
    }

    public final int getCount() {
        return this.zznr;
    }

    public final double getDouble(String str, int i, int i2) {
        zza(str, i);
        return this.zzno[i2].getDouble(i, this.zznn.getInt(str));
    }

    public final float getFloat(String str, int i, int i2) {
        zza(str, i);
        return this.zzno[i2].getFloat(i, this.zznn.getInt(str));
    }

    public final int getInteger(String str, int i, int i2) {
        zza(str, i);
        return this.zzno[i2].getInt(i, this.zznn.getInt(str));
    }

    public final long getLong(String str, int i, int i2) {
        zza(str, i);
        return this.zzno[i2].getLong(i, this.zznn.getInt(str));
    }

    public final Bundle getMetadata() {
        return this.zznp;
    }

    public final int getStatusCode() {
        return this.zzam;
    }

    public final String getString(String str, int i, int i2) {
        zza(str, i);
        return this.zzno[i2].getString(i, this.zznn.getInt(str));
    }

    public final int getWindowIndex(int i) {
        int i2 = 0;
        Preconditions.checkState(i >= 0 && i < this.zznr);
        while (true) {
            if (i2 >= this.zznq.length) {
                break;
            } else if (i < this.zznq[i2]) {
                i2--;
                break;
            } else {
                i2++;
            }
        }
        return i2 == this.zznq.length ? i2 - 1 : i2;
    }

    public final boolean hasColumn(String str) {
        return this.zznn.containsKey(str);
    }

    public final boolean hasNull(String str, int i, int i2) {
        zza(str, i);
        return this.zzno[i2].isNull(i, this.zznn.getInt(str));
    }

    public final boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    public final void logCursorMetadataForDebugging() {
        Log.d("DataHolder", "*******************************************");
        int length = this.zzno.length;
        StringBuilder sb = new StringBuilder(32);
        sb.append("num cursor windows : ");
        sb.append(length);
        Log.d("DataHolder", sb.toString());
        int i = this.zznr;
        StringBuilder sb2 = new StringBuilder(46);
        sb2.append("total number of objects in holder: ");
        sb2.append(i);
        Log.d("DataHolder", sb2.toString());
        int length2 = this.zznq.length;
        StringBuilder sb3 = new StringBuilder(42);
        sb3.append("total mumber of windowOffsets: ");
        sb3.append(length2);
        Log.d("DataHolder", sb3.toString());
        for (int i2 = 0; i2 < this.zznq.length; i2++) {
            int i3 = this.zznq[i2];
            StringBuilder sb4 = new StringBuilder(43);
            sb4.append("offset for window ");
            sb4.append(i2);
            sb4.append(" : ");
            sb4.append(i3);
            Log.d("DataHolder", sb4.toString());
            int numRows = this.zzno[i2].getNumRows();
            StringBuilder sb5 = new StringBuilder(45);
            sb5.append("num rows for window ");
            sb5.append(i2);
            sb5.append(" : ");
            sb5.append(numRows);
            Log.d("DataHolder", sb5.toString());
            int startPosition = this.zzno[i2].getStartPosition();
            StringBuilder sb6 = new StringBuilder(46);
            sb6.append("start pos for window ");
            sb6.append(i2);
            sb6.append(" : ");
            sb6.append(startPosition);
            Log.d("DataHolder", sb6.toString());
        }
        Log.d("DataHolder", "*******************************************");
    }

    public final Uri parseUri(String str, int i, int i2) {
        String string = getString(str, i, i2);
        if (string == null) {
            return null;
        }
        return Uri.parse(string);
    }

    public final void replaceValue(String str, int i, int i2, double d) {
        zza(str, i);
        this.zzno[i2].putDouble(d, i, this.zznn.getInt(str));
    }

    public final void replaceValue(String str, int i, int i2, long j) {
        zza(str, i);
        this.zzno[i2].putLong(j, i, this.zznn.getInt(str));
    }

    public final void replaceValue(String str, int i, int i2, String str2) {
        zza(str, i);
        this.zzno[i2].putString(str2, i, this.zznn.getInt(str));
    }

    public final void replaceValue(String str, int i, int i2, byte[] bArr) {
        zza(str, i);
        this.zzno[i2].putBlob(bArr, i, this.zznn.getInt(str));
    }

    public final void validateContents() {
        this.zznn = new Bundle();
        for (int i = 0; i < this.zznm.length; i++) {
            this.zznn.putInt(this.zznm[i], i);
        }
        this.zznq = new int[this.zzno.length];
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzno.length; i3++) {
            this.zznq[i3] = i2;
            i2 += this.zzno[i3].getNumRows() - (i2 - this.zzno[i3].getStartPosition());
        }
        this.zznr = i2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeStringArray(parcel, 1, this.zznm, false);
        SafeParcelWriter.writeTypedArray(parcel, 2, this.zzno, i, false);
        SafeParcelWriter.writeInt(parcel, 3, getStatusCode());
        SafeParcelWriter.writeBundle(parcel, 4, getMetadata(), false);
        SafeParcelWriter.writeInt(parcel, 1000, this.zzal);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
        if ((i & 1) != 0) {
            close();
        }
    }
}
