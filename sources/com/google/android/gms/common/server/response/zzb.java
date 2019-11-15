package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;

final class zzb implements zza<Long> {
    zzb() {
    }

    public final /* synthetic */ Object zzh(FastParser fastParser, BufferedReader bufferedReader) throws ParseException, IOException {
        return Long.valueOf(fastParser.zze(bufferedReader));
    }
}
