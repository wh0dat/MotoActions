package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Class(creator = "FieldMappingDictionaryCreator")
public class FieldMappingDictionary extends AbstractSafeParcelable {
    public static final Creator<FieldMappingDictionary> CREATOR = new FieldMappingDictionaryCreator();
    @VersionField(mo9711id = 1)
    private final int zzal;
    private final HashMap<String, Map<String, Field<?, ?>>> zzxk;
    @SafeParcelable.Field(getter = "getSerializedDictionary", mo9705id = 2)
    private final ArrayList<Entry> zzxl;
    @SafeParcelable.Field(getter = "getRootClassName", mo9705id = 3)
    private final String zzxm;

    @Class(creator = "FieldMappingDictionaryEntryCreator")
    public static class Entry extends AbstractSafeParcelable {
        public static final Creator<Entry> CREATOR = new FieldMappingDictionaryEntryCreator();
        @SafeParcelable.Field(mo9705id = 2)
        final String className;
        @VersionField(mo9711id = 1)
        private final int versionCode;
        @SafeParcelable.Field(mo9705id = 3)
        final ArrayList<FieldMapPair> zzxn;

        @Constructor
        Entry(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) String str, @Param(mo9708id = 3) ArrayList<FieldMapPair> arrayList) {
            this.versionCode = i;
            this.className = str;
            this.zzxn = arrayList;
        }

        Entry(String str, Map<String, Field<?, ?>> map) {
            ArrayList<FieldMapPair> arrayList;
            this.versionCode = 1;
            this.className = str;
            if (map == null) {
                arrayList = null;
            } else {
                arrayList = new ArrayList<>();
                for (String str2 : map.keySet()) {
                    arrayList.add(new FieldMapPair(str2, (Field) map.get(str2)));
                }
            }
            this.zzxn = arrayList;
        }

        public void writeToParcel(Parcel parcel, int i) {
            int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
            SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
            SafeParcelWriter.writeString(parcel, 2, this.className, false);
            SafeParcelWriter.writeTypedList(parcel, 3, this.zzxn, false);
            SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
        }
    }

    @Class(creator = "FieldMapPairCreator")
    public static class FieldMapPair extends AbstractSafeParcelable {
        public static final Creator<FieldMapPair> CREATOR = new FieldMapPairCreator();
        @VersionField(mo9711id = 1)
        private final int versionCode;
        @SafeParcelable.Field(mo9705id = 2)
        final String zzxo;
        @SafeParcelable.Field(mo9705id = 3)
        final Field<?, ?> zzxp;

        @Constructor
        FieldMapPair(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) String str, @Param(mo9708id = 3) Field<?, ?> field) {
            this.versionCode = i;
            this.zzxo = str;
            this.zzxp = field;
        }

        FieldMapPair(String str, Field<?, ?> field) {
            this.versionCode = 1;
            this.zzxo = str;
            this.zzxp = field;
        }

        public void writeToParcel(Parcel parcel, int i) {
            int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
            SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
            SafeParcelWriter.writeString(parcel, 2, this.zzxo, false);
            SafeParcelWriter.writeParcelable(parcel, 3, this.zzxp, i, false);
            SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
        }
    }

    @Constructor
    FieldMappingDictionary(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) ArrayList<Entry> arrayList, @Param(mo9708id = 3) String str) {
        this.zzal = i;
        this.zzxl = null;
        HashMap<String, Map<String, Field<?, ?>>> hashMap = new HashMap<>();
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            Entry entry = (Entry) arrayList.get(i2);
            String str2 = entry.className;
            HashMap hashMap2 = new HashMap();
            int size2 = entry.zzxn.size();
            for (int i3 = 0; i3 < size2; i3++) {
                FieldMapPair fieldMapPair = (FieldMapPair) entry.zzxn.get(i3);
                hashMap2.put(fieldMapPair.zzxo, fieldMapPair.zzxp);
            }
            hashMap.put(str2, hashMap2);
        }
        this.zzxk = hashMap;
        this.zzxm = (String) Preconditions.checkNotNull(str);
        linkFields();
    }

    public FieldMappingDictionary(Class<? extends FastJsonResponse> cls) {
        this.zzal = 1;
        this.zzxl = null;
        this.zzxk = new HashMap<>();
        this.zzxm = cls.getCanonicalName();
    }

    public void copyInternalFieldMappings() {
        for (String str : this.zzxk.keySet()) {
            Map map = (Map) this.zzxk.get(str);
            HashMap hashMap = new HashMap();
            for (String str2 : map.keySet()) {
                hashMap.put(str2, ((Field) map.get(str2)).copyForDictionary());
            }
            this.zzxk.put(str, hashMap);
        }
    }

    @VisibleForTesting
    public Map<String, Field<?, ?>> getFieldMapping(Class<? extends FastJsonResponse> cls) {
        return (Map) this.zzxk.get(cls.getCanonicalName());
    }

    public Map<String, Field<?, ?>> getFieldMapping(String str) {
        return (Map) this.zzxk.get(str);
    }

    public String getRootClassName() {
        return this.zzxm;
    }

    public boolean hasFieldMappingForClass(Class<? extends FastJsonResponse> cls) {
        return this.zzxk.containsKey(cls.getCanonicalName());
    }

    public void linkFields() {
        for (String str : this.zzxk.keySet()) {
            Map map = (Map) this.zzxk.get(str);
            for (String str2 : map.keySet()) {
                ((Field) map.get(str2)).setFieldMappingDictionary(this);
            }
        }
    }

    public void put(Class<? extends FastJsonResponse> cls, Map<String, Field<?, ?>> map) {
        this.zzxk.put(cls.getCanonicalName(), map);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String str : this.zzxk.keySet()) {
            sb.append(str);
            sb.append(":\n");
            Map map = (Map) this.zzxk.get(str);
            for (String str2 : map.keySet()) {
                sb.append("  ");
                sb.append(str2);
                sb.append(": ");
                sb.append(map.get(str2));
            }
        }
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        ArrayList arrayList = new ArrayList();
        for (String str : this.zzxk.keySet()) {
            arrayList.add(new Entry(str, (Map) this.zzxk.get(str)));
        }
        SafeParcelWriter.writeTypedList(parcel, 2, arrayList, false);
        SafeParcelWriter.writeString(parcel, 3, getRootClassName(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
