package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;

final class zze implements zza<Boolean> {
    zze() {
    }

    public final /* synthetic */ Object zzh(FastParser fastParser, BufferedReader bufferedReader) throws ParseException, IOException {
        return Boolean.valueOf(fastParser.zza(bufferedReader, false));
    }
}
