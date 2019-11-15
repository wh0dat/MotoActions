package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;

final class zza implements zza<Integer> {
    zza() {
    }

    public final /* synthetic */ Object zzh(FastParser fastParser, BufferedReader bufferedReader) throws ParseException, IOException {
        return Integer.valueOf(fastParser.zzd(bufferedReader));
    }
}
