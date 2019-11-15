package com.google.android.gms.common.server;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.DeviceProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.text.Typography;

public class BaseApi {

    public static abstract class BaseApiaryOptions<DerivedClassType extends BaseApiaryOptions<DerivedClassType>> {
        private final ArrayList<String> zzvt = new ArrayList<>();
        private final HashMap<String, String> zzvu = new HashMap<>();
        private String zzvv;
        private final Collector zzvw = new Collector<>();

        public final class Collector {
            private boolean zzvx;
            private boolean zzvy;
            private int zzvz;
            private StringBuilder zzwa = new StringBuilder();

            public Collector() {
            }

            private final void append(String str) {
                StringBuilder sb;
                String str2;
                if (this.zzvx) {
                    this.zzvx = false;
                    sb = this.zzwa;
                    str2 = ",";
                } else {
                    if (this.zzvy) {
                        this.zzvy = false;
                        sb = this.zzwa;
                        str2 = "/";
                    }
                    this.zzwa.append(str);
                }
                sb.append(str2);
                this.zzwa.append(str);
            }

            public final void addPiece(String str) {
                append(str);
                this.zzvy = true;
            }

            public final void beginSubCollection(String str) {
                append(str);
                this.zzwa.append("(");
                this.zzvz++;
            }

            public final void endSubCollection() {
                this.zzwa.append(")");
                this.zzvz--;
                if (this.zzvz == 0) {
                    BaseApiaryOptions.this.addField(this.zzwa.toString());
                    this.zzwa.setLength(0);
                    this.zzvx = false;
                    this.zzvy = false;
                    return;
                }
                this.zzvx = true;
            }

            public final void finishPiece(String str) {
                append(str);
                if (this.zzvz == 0) {
                    BaseApiaryOptions.this.addField(this.zzwa.toString());
                    this.zzwa.setLength(0);
                    return;
                }
                this.zzvx = true;
            }
        }

        private static String zzcy() {
            return String.valueOf(!DeviceProperties.isUserBuild());
        }

        public final DerivedClassType addField(String str) {
            this.zzvt.add(str);
            return this;
        }

        @Deprecated
        public final String appendParametersToUrl(String str) {
            String append = BaseApi.append(str, "prettyPrint", zzcy());
            if (this.zzvv != null) {
                append = BaseApi.append(append, "trace", getTrace());
            }
            return !this.zzvt.isEmpty() ? BaseApi.append(append, "fields", TextUtils.join(",", getFields().toArray())) : append;
        }

        public void appendParametersToUrl(StringBuilder sb) {
            BaseApi.append(sb, "prettyPrint", zzcy());
            if (this.zzvv != null) {
                BaseApi.append(sb, "trace", getTrace());
            }
            if (!this.zzvt.isEmpty()) {
                BaseApi.append(sb, "fields", TextUtils.join(",", getFields().toArray()));
            }
        }

        public final DerivedClassType buildFrom(BaseApiaryOptions<?> baseApiaryOptions) {
            if (baseApiaryOptions.zzvv != null) {
                this.zzvv = baseApiaryOptions.zzvv;
            }
            if (!baseApiaryOptions.zzvt.isEmpty()) {
                this.zzvt.clear();
                this.zzvt.addAll(baseApiaryOptions.zzvt);
            }
            return this;
        }

        /* access modifiers changed from: protected */
        public final Collector getCollector() {
            return this.zzvw;
        }

        public final List<String> getFields() {
            return this.zzvt;
        }

        public final Map<String, String> getHeaders() {
            return this.zzvu;
        }

        public final String getTrace() {
            return this.zzvv;
        }

        public final DerivedClassType setEtag(String str) {
            return setHeader("ETag", str);
        }

        public final DerivedClassType setHeader(String str, String str2) {
            this.zzvu.put(str, str2);
            return this;
        }

        public final DerivedClassType setTraceByLdap(String str) {
            String valueOf = String.valueOf("email:");
            String valueOf2 = String.valueOf(str);
            this.zzvv = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
            return this;
        }

        public final DerivedClassType setTraceByToken(String str) {
            String valueOf = String.valueOf("token:");
            String valueOf2 = String.valueOf(str);
            this.zzvv = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
            return this;
        }
    }

    public static class FieldCollection<Parent> {
        private final Collector zzvw;
        private final Parent zzwc;

        /* JADX WARNING: Incorrect type for immutable var: ssa=Parent, code=com.google.android.gms.common.server.BaseApi$FieldCollection, for r1v0, types: [Parent, com.google.android.gms.common.server.BaseApi$FieldCollection] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected FieldCollection(com.google.android.gms.common.server.BaseApi.FieldCollection r1, com.google.android.gms.common.server.BaseApi.BaseApiaryOptions.Collector r2) {
            /*
                r0 = this;
                r0.<init>()
                if (r1 != 0) goto L_0x0006
                r1 = r0
            L_0x0006:
                r0.zzwc = r1
                r0.zzvw = r2
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.server.BaseApi.FieldCollection.<init>(java.lang.Object, com.google.android.gms.common.server.BaseApi$BaseApiaryOptions$Collector):void");
        }

        /* access modifiers changed from: protected */
        public Collector getCollector() {
            return this.zzvw;
        }

        /* access modifiers changed from: protected */
        public Parent getParent() {
            return this.zzwc;
        }
    }

    @Deprecated
    public static String append(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str.indexOf("?") != -1 ? Typography.amp : '?');
        sb.append(str2);
        sb.append('=');
        sb.append(str3);
        return sb.toString();
    }

    public static void append(StringBuilder sb, String str, String str2) {
        sb.append(sb.indexOf("?") != -1 ? Typography.amp : '?');
        sb.append(str);
        sb.append('=');
        sb.append(str2);
    }

    public static String enc(String str) {
        Preconditions.checkNotNull(str, "Encoding a null parameter!");
        return Uri.encode(str);
    }

    protected static List<String> enc(List<String> list) {
        int size = list.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(enc((String) list.get(i)));
        }
        return arrayList;
    }
}
