package kotlin.collections;

import com.motorola.mod.ModAuthRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.TypeCastException;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0001\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0018\n\u0002\u0010\u0005\n\u0002\u0010\u0012\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0013\n\u0002\u0010\u0007\n\u0002\u0010\u0014\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\u0010\t\n\u0002\u0010\u0016\n\u0002\u0010\n\n\u0002\u0010\u0017\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\b\u0006\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\f\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010\u0004\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00050\u0001*\u00020\u0006\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00070\u0001*\u00020\b\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\t0\u0001*\u00020\n\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0001*\u00020\f\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\r0\u0001*\u00020\u000e\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0001*\u00020\u0010\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00110\u0001*\u00020\u0012\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00130\u0001*\u00020\u0014\u001aU\u0010\u0015\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010\u001c\u001a9\u0010\u0015\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010\u001d\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00072\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\n2\u0006\u0010\u0016\u001a\u00020\t2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\r2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010\u0015\u001a\u00020\u000f*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a0\u0010\u001e\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u000e\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\f¢\u0006\u0002\u0010 \u001a \u0010!\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\b¢\u0006\u0002\u0010\"\u001a \u0010#\u001a\u00020$\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\b¢\u0006\u0002\u0010%\u001a0\u0010&\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u000e\u0010\u001f\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\f¢\u0006\u0002\u0010 \u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0006H\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\b2\u0006\u0010\u001f\u001a\u00020\bH\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u001f\u001a\u00020\nH\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\f2\u0006\u0010\u001f\u001a\u00020\fH\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u000e2\u0006\u0010\u001f\u001a\u00020\u000eH\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u0010H\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0012H\f\u001a\u0015\u0010&\u001a\u00020\u0005*\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u0014H\f\u001a \u0010'\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\b¢\u0006\u0002\u0010\"\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0006H\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\bH\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\nH\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\fH\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u000eH\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0010H\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0012H\b\u001a\r\u0010'\u001a\u00020\u000f*\u00020\u0014H\b\u001a \u0010(\u001a\u00020$\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\b¢\u0006\u0002\u0010%\u001a\r\u0010(\u001a\u00020$*\u00020\u0006H\b\u001a\r\u0010(\u001a\u00020$*\u00020\bH\b\u001a\r\u0010(\u001a\u00020$*\u00020\nH\b\u001a\r\u0010(\u001a\u00020$*\u00020\fH\b\u001a\r\u0010(\u001a\u00020$*\u00020\u000eH\b\u001a\r\u0010(\u001a\u00020$*\u00020\u0010H\b\u001a\r\u0010(\u001a\u00020$*\u00020\u0012H\b\u001a\r\u0010(\u001a\u00020$*\u00020\u0014H\b\u001a$\u0010)\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\b¢\u0006\u0002\u0010*\u001a.\u0010)\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010+\u001a\u00020\u000fH\b¢\u0006\u0002\u0010,\u001a\r\u0010)\u001a\u00020\u0006*\u00020\u0006H\b\u001a\u0015\u0010)\u001a\u00020\u0006*\u00020\u00062\u0006\u0010+\u001a\u00020\u000fH\b\u001a\r\u0010)\u001a\u00020\b*\u00020\bH\b\u001a\u0015\u0010)\u001a\u00020\b*\u00020\b2\u0006\u0010+\u001a\u00020\u000fH\b\u001a\r\u0010)\u001a\u00020\n*\u00020\nH\b\u001a\u0015\u0010)\u001a\u00020\n*\u00020\n2\u0006\u0010+\u001a\u00020\u000fH\b\u001a\r\u0010)\u001a\u00020\f*\u00020\fH\b\u001a\u0015\u0010)\u001a\u00020\f*\u00020\f2\u0006\u0010+\u001a\u00020\u000fH\b\u001a\r\u0010)\u001a\u00020\u000e*\u00020\u000eH\b\u001a\u0015\u0010)\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010+\u001a\u00020\u000fH\b\u001a\r\u0010)\u001a\u00020\u0010*\u00020\u0010H\b\u001a\u0015\u0010)\u001a\u00020\u0010*\u00020\u00102\u0006\u0010+\u001a\u00020\u000fH\b\u001a\r\u0010)\u001a\u00020\u0012*\u00020\u0012H\b\u001a\u0015\u0010)\u001a\u00020\u0012*\u00020\u00122\u0006\u0010+\u001a\u00020\u000fH\b\u001a\r\u0010)\u001a\u00020\u0014*\u00020\u0014H\b\u001a\u0015\u0010)\u001a\u00020\u0014*\u00020\u00142\u0006\u0010+\u001a\u00020\u000fH\b\u001a4\u0010-\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b¢\u0006\u0002\u0010.\u001a\u001d\u0010-\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b\u001a\u001d\u0010-\u001a\u00020\b*\u00020\b2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b\u001a\u001d\u0010-\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b\u001a\u001d\u0010-\u001a\u00020\f*\u00020\f2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b\u001a\u001d\u0010-\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b\u001a\u001d\u0010-\u001a\u00020\u0010*\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b\u001a\u001d\u0010-\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b\u001a\u001d\u0010-\u001a\u00020\u0014*\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u000fH\b\u001a7\u0010/\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u00101\u001a&\u0010/\u001a\u000200*\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00072\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\n2\u0006\u0010\u0016\u001a\u00020\t2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\r2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a&\u0010/\u001a\u000200*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a-\u00102\u001a\b\u0012\u0004\u0012\u0002H30\u0001\"\u0004\b\u0000\u00103*\u0006\u0012\u0002\b\u00030\u00032\f\u00104\u001a\b\u0012\u0004\u0012\u0002H305¢\u0006\u0002\u00106\u001aA\u00107\u001a\u0002H8\"\u0010\b\u0000\u00108*\n\u0012\u0006\b\u0000\u0012\u0002H309\"\u0004\b\u0001\u00103*\u0006\u0012\u0002\b\u00030\u00032\u0006\u0010:\u001a\u0002H82\f\u00104\u001a\b\u0012\u0004\u0012\u0002H305¢\u0006\u0002\u0010;\u001a,\u0010<\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u0002H\u0002¢\u0006\u0002\u0010=\u001a4\u0010<\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u000e\u0010>\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\u0002¢\u0006\u0002\u0010?\u001a2\u0010<\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\f\u0010>\u001a\b\u0012\u0004\u0012\u0002H\u00020@H\u0002¢\u0006\u0002\u0010A\u001a\u0015\u0010<\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u0005H\u0002\u001a\u0015\u0010<\u001a\u00020\u0006*\u00020\u00062\u0006\u0010>\u001a\u00020\u0006H\u0002\u001a\u001b\u0010<\u001a\u00020\u0006*\u00020\u00062\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00050@H\u0002\u001a\u0015\u0010<\u001a\u00020\b*\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0007H\u0002\u001a\u0015\u0010<\u001a\u00020\b*\u00020\b2\u0006\u0010>\u001a\u00020\bH\u0002\u001a\u001b\u0010<\u001a\u00020\b*\u00020\b2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00070@H\u0002\u001a\u0015\u0010<\u001a\u00020\n*\u00020\n2\u0006\u0010\u0016\u001a\u00020\tH\u0002\u001a\u0015\u0010<\u001a\u00020\n*\u00020\n2\u0006\u0010>\u001a\u00020\nH\u0002\u001a\u001b\u0010<\u001a\u00020\n*\u00020\n2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\t0@H\u0002\u001a\u0015\u0010<\u001a\u00020\f*\u00020\f2\u0006\u0010\u0016\u001a\u00020\u000bH\u0002\u001a\u0015\u0010<\u001a\u00020\f*\u00020\f2\u0006\u0010>\u001a\u00020\fH\u0002\u001a\u001b\u0010<\u001a\u00020\f*\u00020\f2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u000b0@H\u0002\u001a\u0015\u0010<\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\rH\u0002\u001a\u0015\u0010<\u001a\u00020\u000e*\u00020\u000e2\u0006\u0010>\u001a\u00020\u000eH\u0002\u001a\u001b\u0010<\u001a\u00020\u000e*\u00020\u000e2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\r0@H\u0002\u001a\u0015\u0010<\u001a\u00020\u0010*\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u000fH\u0002\u001a\u0015\u0010<\u001a\u00020\u0010*\u00020\u00102\u0006\u0010>\u001a\u00020\u0010H\u0002\u001a\u001b\u0010<\u001a\u00020\u0010*\u00020\u00102\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u000f0@H\u0002\u001a\u0015\u0010<\u001a\u00020\u0012*\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0011H\u0002\u001a\u0015\u0010<\u001a\u00020\u0012*\u00020\u00122\u0006\u0010>\u001a\u00020\u0012H\u0002\u001a\u001b\u0010<\u001a\u00020\u0012*\u00020\u00122\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00110@H\u0002\u001a\u0015\u0010<\u001a\u00020\u0014*\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0013H\u0002\u001a\u0015\u0010<\u001a\u00020\u0014*\u00020\u00142\u0006\u0010>\u001a\u00020\u0014H\u0002\u001a\u001b\u0010<\u001a\u00020\u0014*\u00020\u00142\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00130@H\u0002\u001a,\u0010B\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0016\u001a\u0002H\u0002H\b¢\u0006\u0002\u0010=\u001a\u001d\u0010C\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010D\u001a*\u0010C\u001a\u000200\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020E*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003H\b¢\u0006\u0002\u0010F\u001a1\u0010C\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010G\u001a\n\u0010C\u001a\u000200*\u00020\b\u001a\u001e\u0010C\u001a\u000200*\u00020\b2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\n\u001a\u001e\u0010C\u001a\u000200*\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\f\u001a\u001e\u0010C\u001a\u000200*\u00020\f2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u000e\u001a\u001e\u0010C\u001a\u000200*\u00020\u000e2\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u0010\u001a\u001e\u0010C\u001a\u000200*\u00020\u00102\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u0012\u001a\u001e\u0010C\u001a\u000200*\u00020\u00122\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a\n\u0010C\u001a\u000200*\u00020\u0014\u001a\u001e\u0010C\u001a\u000200*\u00020\u00142\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f\u001a9\u0010H\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0019¢\u0006\u0002\u0010I\u001aM\u0010H\u001a\u000200\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u000f¢\u0006\u0002\u0010J\u001a-\u0010K\u001a\b\u0012\u0004\u0012\u0002H\u00020L\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020E*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0003¢\u0006\u0002\u0010M\u001a?\u0010K\u001a\b\u0012\u0004\u0012\u0002H\u00020L\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0019¢\u0006\u0002\u0010N\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00050L*\u00020\u0006\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00070L*\u00020\b\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\t0L*\u00020\n\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u000b0L*\u00020\f\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\r0L*\u00020\u000e\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u000f0L*\u00020\u0010\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00110L*\u00020\u0012\u001a\u0010\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00130L*\u00020\u0014\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00050\u0003*\u00020\u0006¢\u0006\u0002\u0010P\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00070\u0003*\u00020\b¢\u0006\u0002\u0010Q\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\t0\u0003*\u00020\n¢\u0006\u0002\u0010R\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003*\u00020\f¢\u0006\u0002\u0010S\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\r0\u0003*\u00020\u000e¢\u0006\u0002\u0010T\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0003*\u00020\u0010¢\u0006\u0002\u0010U\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00110\u0003*\u00020\u0012¢\u0006\u0002\u0010V\u001a\u0015\u0010O\u001a\b\u0012\u0004\u0012\u00020\u00130\u0003*\u00020\u0014¢\u0006\u0002\u0010W¨\u0006X"}, mo14495d2 = {"asList", "", "T", "", "([Ljava/lang/Object;)Ljava/util/List;", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "binarySearch", "element", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "fromIndex", "toIndex", "([Ljava/lang/Object;Ljava/lang/Object;Ljava/util/Comparator;II)I", "([Ljava/lang/Object;Ljava/lang/Object;II)I", "contentDeepEquals", "other", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", "contentDeepHashCode", "([Ljava/lang/Object;)I", "contentDeepToString", "", "([Ljava/lang/Object;)Ljava/lang/String;", "contentEquals", "contentHashCode", "contentToString", "copyOf", "([Ljava/lang/Object;)[Ljava/lang/Object;", "newSize", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "copyOfRange", "([Ljava/lang/Object;II)[Ljava/lang/Object;", "fill", "", "([Ljava/lang/Object;Ljava/lang/Object;II)V", "filterIsInstance", "R", "klass", "Ljava/lang/Class;", "([Ljava/lang/Object;Ljava/lang/Class;)Ljava/util/List;", "filterIsInstanceTo", "C", "", "destination", "([Ljava/lang/Object;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "plus", "([Ljava/lang/Object;Ljava/lang/Object;)[Ljava/lang/Object;", "elements", "([Ljava/lang/Object;[Ljava/lang/Object;)[Ljava/lang/Object;", "", "([Ljava/lang/Object;Ljava/util/Collection;)[Ljava/lang/Object;", "plusElement", "sort", "([Ljava/lang/Object;)V", "", "([Ljava/lang/Comparable;)V", "([Ljava/lang/Object;II)V", "sortWith", "([Ljava/lang/Object;Ljava/util/Comparator;)V", "([Ljava/lang/Object;Ljava/util/Comparator;II)V", "toSortedSet", "Ljava/util/SortedSet;", "([Ljava/lang/Comparable;)Ljava/util/SortedSet;", "([Ljava/lang/Object;Ljava/util/Comparator;)Ljava/util/SortedSet;", "toTypedArray", "([Z)[Ljava/lang/Boolean;", "([B)[Ljava/lang/Byte;", "([C)[Ljava/lang/Character;", "([D)[Ljava/lang/Double;", "([F)[Ljava/lang/Float;", "([I)[Ljava/lang/Integer;", "([J)[Ljava/lang/Long;", "([S)[Ljava/lang/Short;", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/collections/ArraysKt")
/* compiled from: _ArraysJvm.kt */
class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt {
    @NotNull
    public static final <R> List<R> filterIsInstance(@NotNull Object[] objArr, @NotNull Class<R> cls) {
        Intrinsics.checkParameterIsNotNull(objArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(cls, "klass");
        return (List) ArraysKt.filterIsInstanceTo(objArr, new ArrayList(), cls);
    }

    @NotNull
    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(@NotNull Object[] objArr, @NotNull C c, @NotNull Class<R> cls) {
        Intrinsics.checkParameterIsNotNull(objArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(c, "destination");
        Intrinsics.checkParameterIsNotNull(cls, "klass");
        for (Object obj : objArr) {
            if (cls.isInstance(obj)) {
                c.add(obj);
            }
        }
        return c;
    }

    @NotNull
    public static final <T> List<T> asList(@NotNull T[] tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        List<T> asList = ArraysUtilJVM.asList(tArr);
        Intrinsics.checkExpressionValueIsNotNull(asList, "ArraysUtilJVM.asList(this)");
        return asList;
    }

    @NotNull
    public static final List<Byte> asList(@NotNull byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        return new ArraysKt___ArraysJvmKt$asList$1<>(bArr);
    }

    @NotNull
    public static final List<Short> asList(@NotNull short[] sArr) {
        Intrinsics.checkParameterIsNotNull(sArr, "$receiver");
        return new ArraysKt___ArraysJvmKt$asList$2<>(sArr);
    }

    @NotNull
    public static final List<Integer> asList(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "$receiver");
        return new ArraysKt___ArraysJvmKt$asList$3<>(iArr);
    }

    @NotNull
    public static final List<Long> asList(@NotNull long[] jArr) {
        Intrinsics.checkParameterIsNotNull(jArr, "$receiver");
        return new ArraysKt___ArraysJvmKt$asList$4<>(jArr);
    }

    @NotNull
    public static final List<Float> asList(@NotNull float[] fArr) {
        Intrinsics.checkParameterIsNotNull(fArr, "$receiver");
        return new ArraysKt___ArraysJvmKt$asList$5<>(fArr);
    }

    @NotNull
    public static final List<Double> asList(@NotNull double[] dArr) {
        Intrinsics.checkParameterIsNotNull(dArr, "$receiver");
        return new ArraysKt___ArraysJvmKt$asList$6<>(dArr);
    }

    @NotNull
    public static final List<Boolean> asList(@NotNull boolean[] zArr) {
        Intrinsics.checkParameterIsNotNull(zArr, "$receiver");
        return new ArraysKt___ArraysJvmKt$asList$7<>(zArr);
    }

    @NotNull
    public static final List<Character> asList(@NotNull char[] cArr) {
        Intrinsics.checkParameterIsNotNull(cArr, "$receiver");
        return new ArraysKt___ArraysJvmKt$asList$8<>(cArr);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(Object[] objArr, Object obj, Comparator comparator, int i, int i2, int i3, Object obj2) {
        if ((i3 & 4) != 0) {
            i = 0;
        }
        if ((i3 & 8) != 0) {
            i2 = objArr.length;
        }
        return ArraysKt.binarySearch(objArr, obj, comparator, i, i2);
    }

    public static final <T> int binarySearch(@NotNull T[] tArr, T t, @NotNull Comparator<? super T> comparator, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return Arrays.binarySearch(tArr, i, i2, t, comparator);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(Object[] objArr, Object obj, int i, int i2, int i3, Object obj2) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = objArr.length;
        }
        return ArraysKt.binarySearch((T[]) objArr, (T) obj, i, i2);
    }

    public static final <T> int binarySearch(@NotNull T[] tArr, T t, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        return Arrays.binarySearch(tArr, i, i2, t);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(byte[] bArr, byte b, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        return ArraysKt.binarySearch(bArr, b, i, i2);
    }

    public static final int binarySearch(@NotNull byte[] bArr, byte b, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        return Arrays.binarySearch(bArr, i, i2, b);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(short[] sArr, short s, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = sArr.length;
        }
        return ArraysKt.binarySearch(sArr, s, i, i2);
    }

    public static final int binarySearch(@NotNull short[] sArr, short s, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(sArr, "$receiver");
        return Arrays.binarySearch(sArr, i, i2, s);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(int[] iArr, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = iArr.length;
        }
        return ArraysKt.binarySearch(iArr, i, i2, i3);
    }

    public static final int binarySearch(@NotNull int[] iArr, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(iArr, "$receiver");
        return Arrays.binarySearch(iArr, i2, i3, i);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(long[] jArr, long j, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = jArr.length;
        }
        return ArraysKt.binarySearch(jArr, j, i, i2);
    }

    public static final int binarySearch(@NotNull long[] jArr, long j, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(jArr, "$receiver");
        return Arrays.binarySearch(jArr, i, i2, j);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(float[] fArr, float f, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = fArr.length;
        }
        return ArraysKt.binarySearch(fArr, f, i, i2);
    }

    public static final int binarySearch(@NotNull float[] fArr, float f, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(fArr, "$receiver");
        return Arrays.binarySearch(fArr, i, i2, f);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(double[] dArr, double d, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = dArr.length;
        }
        return ArraysKt.binarySearch(dArr, d, i, i2);
    }

    public static final int binarySearch(@NotNull double[] dArr, double d, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(dArr, "$receiver");
        return Arrays.binarySearch(dArr, i, i2, d);
    }

    public static /* bridge */ /* synthetic */ int binarySearch$default(char[] cArr, char c, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = cArr.length;
        }
        return ArraysKt.binarySearch(cArr, c, i, i2);
    }

    public static final int binarySearch(@NotNull char[] cArr, char c, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(cArr, "$receiver");
        return Arrays.binarySearch(cArr, i, i2, c);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> boolean contentDeepEquals(@NotNull T[] tArr, T[] tArr2) {
        return Arrays.deepEquals(tArr, tArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> int contentDeepHashCode(@NotNull T[] tArr) {
        return Arrays.deepHashCode(tArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> String contentDeepToString(@NotNull T[] tArr) {
        String deepToString = Arrays.deepToString(tArr);
        Intrinsics.checkExpressionValueIsNotNull(deepToString, "java.util.Arrays.deepToString(this)");
        return deepToString;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> boolean contentEquals(@NotNull T[] tArr, T[] tArr2) {
        return Arrays.equals(tArr, tArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull byte[] bArr, byte[] bArr2) {
        return Arrays.equals(bArr, bArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull short[] sArr, short[] sArr2) {
        return Arrays.equals(sArr, sArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull int[] iArr, int[] iArr2) {
        return Arrays.equals(iArr, iArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull long[] jArr, long[] jArr2) {
        return Arrays.equals(jArr, jArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull float[] fArr, float[] fArr2) {
        return Arrays.equals(fArr, fArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull double[] dArr, double[] dArr2) {
        return Arrays.equals(dArr, dArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull boolean[] zArr, boolean[] zArr2) {
        return Arrays.equals(zArr, zArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final boolean contentEquals(@NotNull char[] cArr, char[] cArr2) {
        return Arrays.equals(cArr, cArr2);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> int contentHashCode(@NotNull T[] tArr) {
        return Arrays.hashCode(tArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull byte[] bArr) {
        return Arrays.hashCode(bArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull short[] sArr) {
        return Arrays.hashCode(sArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull int[] iArr) {
        return Arrays.hashCode(iArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull long[] jArr) {
        return Arrays.hashCode(jArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull float[] fArr) {
        return Arrays.hashCode(fArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull double[] dArr) {
        return Arrays.hashCode(dArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull boolean[] zArr) {
        return Arrays.hashCode(zArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int contentHashCode(@NotNull char[] cArr) {
        return Arrays.hashCode(cArr);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> String contentToString(@NotNull T[] tArr) {
        String arrays = Arrays.toString(tArr);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull byte[] bArr) {
        String arrays = Arrays.toString(bArr);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull short[] sArr) {
        String arrays = Arrays.toString(sArr);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull int[] iArr) {
        String arrays = Arrays.toString(iArr);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull long[] jArr) {
        String arrays = Arrays.toString(jArr);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull float[] fArr) {
        String arrays = Arrays.toString(fArr);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull double[] dArr) {
        String arrays = Arrays.toString(dArr);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull boolean[] zArr) {
        String arrays = Arrays.toString(zArr);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String contentToString(@NotNull char[] cArr) {
        String arrays = Arrays.toString(cArr);
        Intrinsics.checkExpressionValueIsNotNull(arrays, "java.util.Arrays.toString(this)");
        return arrays;
    }

    @InlineOnly
    private static final <T> T[] copyOf(@NotNull T[] tArr) {
        T[] copyOf = Arrays.copyOf(tArr, tArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final byte[] copyOf(@NotNull byte[] bArr) {
        byte[] copyOf = Arrays.copyOf(bArr, bArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final short[] copyOf(@NotNull short[] sArr) {
        short[] copyOf = Arrays.copyOf(sArr, sArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final int[] copyOf(@NotNull int[] iArr) {
        int[] copyOf = Arrays.copyOf(iArr, iArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final long[] copyOf(@NotNull long[] jArr) {
        long[] copyOf = Arrays.copyOf(jArr, jArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final float[] copyOf(@NotNull float[] fArr) {
        float[] copyOf = Arrays.copyOf(fArr, fArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final double[] copyOf(@NotNull double[] dArr) {
        double[] copyOf = Arrays.copyOf(dArr, dArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final boolean[] copyOf(@NotNull boolean[] zArr) {
        boolean[] copyOf = Arrays.copyOf(zArr, zArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final char[] copyOf(@NotNull char[] cArr) {
        char[] copyOf = Arrays.copyOf(cArr, cArr.length);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    @InlineOnly
    private static final byte[] copyOf(@NotNull byte[] bArr, int i) {
        byte[] copyOf = Arrays.copyOf(bArr, i);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final short[] copyOf(@NotNull short[] sArr, int i) {
        short[] copyOf = Arrays.copyOf(sArr, i);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final int[] copyOf(@NotNull int[] iArr, int i) {
        int[] copyOf = Arrays.copyOf(iArr, i);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final long[] copyOf(@NotNull long[] jArr, int i) {
        long[] copyOf = Arrays.copyOf(jArr, i);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final float[] copyOf(@NotNull float[] fArr, int i) {
        float[] copyOf = Arrays.copyOf(fArr, i);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final double[] copyOf(@NotNull double[] dArr, int i) {
        double[] copyOf = Arrays.copyOf(dArr, i);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final boolean[] copyOf(@NotNull boolean[] zArr, int i) {
        boolean[] copyOf = Arrays.copyOf(zArr, i);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final char[] copyOf(@NotNull char[] cArr, int i) {
        char[] copyOf = Arrays.copyOf(cArr, i);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final <T> T[] copyOf(@NotNull T[] tArr, int i) {
        T[] copyOf = Arrays.copyOf(tArr, i);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    @InlineOnly
    private static final <T> T[] copyOfRange(@NotNull T[] tArr, int i, int i2) {
        T[] copyOfRange = Arrays.copyOfRange(tArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    private static final byte[] copyOfRange(@NotNull byte[] bArr, int i, int i2) {
        byte[] copyOfRange = Arrays.copyOfRange(bArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    private static final short[] copyOfRange(@NotNull short[] sArr, int i, int i2) {
        short[] copyOfRange = Arrays.copyOfRange(sArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    private static final int[] copyOfRange(@NotNull int[] iArr, int i, int i2) {
        int[] copyOfRange = Arrays.copyOfRange(iArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    private static final long[] copyOfRange(@NotNull long[] jArr, int i, int i2) {
        long[] copyOfRange = Arrays.copyOfRange(jArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    private static final float[] copyOfRange(@NotNull float[] fArr, int i, int i2) {
        float[] copyOfRange = Arrays.copyOfRange(fArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    private static final double[] copyOfRange(@NotNull double[] dArr, int i, int i2) {
        double[] copyOfRange = Arrays.copyOfRange(dArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    private static final boolean[] copyOfRange(@NotNull boolean[] zArr, int i, int i2) {
        boolean[] copyOfRange = Arrays.copyOfRange(zArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    @InlineOnly
    private static final char[] copyOfRange(@NotNull char[] cArr, int i, int i2) {
        char[] copyOfRange = Arrays.copyOfRange(cArr, i, i2);
        Intrinsics.checkExpressionValueIsNotNull(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    public static /* bridge */ /* synthetic */ void fill$default(Object[] objArr, Object obj, int i, int i2, int i3, Object obj2) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = objArr.length;
        }
        ArraysKt.fill((T[]) objArr, (T) obj, i, i2);
    }

    public static final <T> void fill(@NotNull T[] tArr, T t, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        Arrays.fill(tArr, i, i2, t);
    }

    public static /* bridge */ /* synthetic */ void fill$default(byte[] bArr, byte b, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        ArraysKt.fill(bArr, b, i, i2);
    }

    public static final void fill(@NotNull byte[] bArr, byte b, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        Arrays.fill(bArr, i, i2, b);
    }

    public static /* bridge */ /* synthetic */ void fill$default(short[] sArr, short s, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = sArr.length;
        }
        ArraysKt.fill(sArr, s, i, i2);
    }

    public static final void fill(@NotNull short[] sArr, short s, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(sArr, "$receiver");
        Arrays.fill(sArr, i, i2, s);
    }

    public static /* bridge */ /* synthetic */ void fill$default(int[] iArr, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = iArr.length;
        }
        ArraysKt.fill(iArr, i, i2, i3);
    }

    public static final void fill(@NotNull int[] iArr, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(iArr, "$receiver");
        Arrays.fill(iArr, i2, i3, i);
    }

    public static /* bridge */ /* synthetic */ void fill$default(long[] jArr, long j, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = jArr.length;
        }
        ArraysKt.fill(jArr, j, i, i2);
    }

    public static final void fill(@NotNull long[] jArr, long j, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(jArr, "$receiver");
        Arrays.fill(jArr, i, i2, j);
    }

    public static /* bridge */ /* synthetic */ void fill$default(float[] fArr, float f, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = fArr.length;
        }
        ArraysKt.fill(fArr, f, i, i2);
    }

    public static final void fill(@NotNull float[] fArr, float f, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(fArr, "$receiver");
        Arrays.fill(fArr, i, i2, f);
    }

    public static /* bridge */ /* synthetic */ void fill$default(double[] dArr, double d, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = dArr.length;
        }
        ArraysKt.fill(dArr, d, i, i2);
    }

    public static final void fill(@NotNull double[] dArr, double d, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(dArr, "$receiver");
        Arrays.fill(dArr, i, i2, d);
    }

    public static /* bridge */ /* synthetic */ void fill$default(boolean[] zArr, boolean z, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = zArr.length;
        }
        ArraysKt.fill(zArr, z, i, i2);
    }

    public static final void fill(@NotNull boolean[] zArr, boolean z, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(zArr, "$receiver");
        Arrays.fill(zArr, i, i2, z);
    }

    public static /* bridge */ /* synthetic */ void fill$default(char[] cArr, char c, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = cArr.length;
        }
        ArraysKt.fill(cArr, c, i, i2);
    }

    public static final void fill(@NotNull char[] cArr, char c, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(cArr, "$receiver");
        Arrays.fill(cArr, i, i2, c);
    }

    @NotNull
    public static final <T> T[] plus(@NotNull T[] tArr, T t) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        int length = tArr.length;
        T[] copyOf = Arrays.copyOf(tArr, length + 1);
        copyOf[length] = t;
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final byte[] plus(@NotNull byte[] bArr, byte b) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        int length = bArr.length;
        byte[] copyOf = Arrays.copyOf(bArr, length + 1);
        copyOf[length] = b;
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final short[] plus(@NotNull short[] sArr, short s) {
        Intrinsics.checkParameterIsNotNull(sArr, "$receiver");
        int length = sArr.length;
        short[] copyOf = Arrays.copyOf(sArr, length + 1);
        copyOf[length] = s;
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final int[] plus(@NotNull int[] iArr, int i) {
        Intrinsics.checkParameterIsNotNull(iArr, "$receiver");
        int length = iArr.length;
        int[] copyOf = Arrays.copyOf(iArr, length + 1);
        copyOf[length] = i;
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final long[] plus(@NotNull long[] jArr, long j) {
        Intrinsics.checkParameterIsNotNull(jArr, "$receiver");
        int length = jArr.length;
        long[] copyOf = Arrays.copyOf(jArr, length + 1);
        copyOf[length] = j;
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final float[] plus(@NotNull float[] fArr, float f) {
        Intrinsics.checkParameterIsNotNull(fArr, "$receiver");
        int length = fArr.length;
        float[] copyOf = Arrays.copyOf(fArr, length + 1);
        copyOf[length] = f;
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final double[] plus(@NotNull double[] dArr, double d) {
        Intrinsics.checkParameterIsNotNull(dArr, "$receiver");
        int length = dArr.length;
        double[] copyOf = Arrays.copyOf(dArr, length + 1);
        copyOf[length] = d;
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final boolean[] plus(@NotNull boolean[] zArr, boolean z) {
        Intrinsics.checkParameterIsNotNull(zArr, "$receiver");
        int length = zArr.length;
        boolean[] copyOf = Arrays.copyOf(zArr, length + 1);
        copyOf[length] = z;
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final char[] plus(@NotNull char[] cArr, char c) {
        Intrinsics.checkParameterIsNotNull(cArr, "$receiver");
        int length = cArr.length;
        char[] copyOf = Arrays.copyOf(cArr, length + 1);
        copyOf[length] = c;
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final <T> T[] plus(@NotNull T[] tArr, @NotNull Collection<? extends T> collection) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = tArr.length;
        T[] copyOf = Arrays.copyOf(tArr, collection.size() + length);
        for (T t : collection) {
            int i = length + 1;
            copyOf[length] = t;
            length = i;
        }
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final byte[] plus(@NotNull byte[] bArr, @NotNull Collection<Byte> collection) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = bArr.length;
        byte[] copyOf = Arrays.copyOf(bArr, collection.size() + length);
        for (Number byteValue : collection) {
            int i = length + 1;
            copyOf[length] = byteValue.byteValue();
            length = i;
        }
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final short[] plus(@NotNull short[] sArr, @NotNull Collection<Short> collection) {
        Intrinsics.checkParameterIsNotNull(sArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = sArr.length;
        short[] copyOf = Arrays.copyOf(sArr, collection.size() + length);
        for (Number shortValue : collection) {
            int i = length + 1;
            copyOf[length] = shortValue.shortValue();
            length = i;
        }
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final int[] plus(@NotNull int[] iArr, @NotNull Collection<Integer> collection) {
        Intrinsics.checkParameterIsNotNull(iArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = iArr.length;
        int[] copyOf = Arrays.copyOf(iArr, collection.size() + length);
        for (Number intValue : collection) {
            int i = length + 1;
            copyOf[length] = intValue.intValue();
            length = i;
        }
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final long[] plus(@NotNull long[] jArr, @NotNull Collection<Long> collection) {
        Intrinsics.checkParameterIsNotNull(jArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = jArr.length;
        long[] copyOf = Arrays.copyOf(jArr, collection.size() + length);
        for (Number longValue : collection) {
            int i = length + 1;
            copyOf[length] = longValue.longValue();
            length = i;
        }
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final float[] plus(@NotNull float[] fArr, @NotNull Collection<Float> collection) {
        Intrinsics.checkParameterIsNotNull(fArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = fArr.length;
        float[] copyOf = Arrays.copyOf(fArr, collection.size() + length);
        for (Number floatValue : collection) {
            int i = length + 1;
            copyOf[length] = floatValue.floatValue();
            length = i;
        }
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final double[] plus(@NotNull double[] dArr, @NotNull Collection<Double> collection) {
        Intrinsics.checkParameterIsNotNull(dArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = dArr.length;
        double[] copyOf = Arrays.copyOf(dArr, collection.size() + length);
        for (Number doubleValue : collection) {
            int i = length + 1;
            copyOf[length] = doubleValue.doubleValue();
            length = i;
        }
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final boolean[] plus(@NotNull boolean[] zArr, @NotNull Collection<Boolean> collection) {
        Intrinsics.checkParameterIsNotNull(zArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = zArr.length;
        boolean[] copyOf = Arrays.copyOf(zArr, collection.size() + length);
        for (Boolean booleanValue : collection) {
            int i = length + 1;
            copyOf[length] = booleanValue.booleanValue();
            length = i;
        }
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final char[] plus(@NotNull char[] cArr, @NotNull Collection<Character> collection) {
        Intrinsics.checkParameterIsNotNull(cArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        int length = cArr.length;
        char[] copyOf = Arrays.copyOf(cArr, collection.size() + length);
        for (Character charValue : collection) {
            int i = length + 1;
            copyOf[length] = charValue.charValue();
            length = i;
        }
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final <T> T[] plus(@NotNull T[] tArr, @NotNull T[] tArr2) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(tArr2, "elements");
        int length = tArr.length;
        int length2 = tArr2.length;
        T[] copyOf = Arrays.copyOf(tArr, length + length2);
        System.arraycopy(tArr2, 0, copyOf, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final byte[] plus(@NotNull byte[] bArr, @NotNull byte[] bArr2) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(bArr2, "elements");
        int length = bArr.length;
        int length2 = bArr2.length;
        byte[] copyOf = Arrays.copyOf(bArr, length + length2);
        System.arraycopy(bArr2, 0, copyOf, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final short[] plus(@NotNull short[] sArr, @NotNull short[] sArr2) {
        Intrinsics.checkParameterIsNotNull(sArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(sArr2, "elements");
        int length = sArr.length;
        int length2 = sArr2.length;
        short[] copyOf = Arrays.copyOf(sArr, length + length2);
        System.arraycopy(sArr2, 0, copyOf, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final int[] plus(@NotNull int[] iArr, @NotNull int[] iArr2) {
        Intrinsics.checkParameterIsNotNull(iArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(iArr2, "elements");
        int length = iArr.length;
        int length2 = iArr2.length;
        int[] copyOf = Arrays.copyOf(iArr, length + length2);
        System.arraycopy(iArr2, 0, copyOf, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final long[] plus(@NotNull long[] jArr, @NotNull long[] jArr2) {
        Intrinsics.checkParameterIsNotNull(jArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(jArr2, "elements");
        int length = jArr.length;
        int length2 = jArr2.length;
        long[] copyOf = Arrays.copyOf(jArr, length + length2);
        System.arraycopy(jArr2, 0, copyOf, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final float[] plus(@NotNull float[] fArr, @NotNull float[] fArr2) {
        Intrinsics.checkParameterIsNotNull(fArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(fArr2, "elements");
        int length = fArr.length;
        int length2 = fArr2.length;
        float[] copyOf = Arrays.copyOf(fArr, length + length2);
        System.arraycopy(fArr2, 0, copyOf, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final double[] plus(@NotNull double[] dArr, @NotNull double[] dArr2) {
        Intrinsics.checkParameterIsNotNull(dArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(dArr2, "elements");
        int length = dArr.length;
        int length2 = dArr2.length;
        double[] copyOf = Arrays.copyOf(dArr, length + length2);
        System.arraycopy(dArr2, 0, copyOf, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final boolean[] plus(@NotNull boolean[] zArr, @NotNull boolean[] zArr2) {
        Intrinsics.checkParameterIsNotNull(zArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(zArr2, "elements");
        int length = zArr.length;
        int length2 = zArr2.length;
        boolean[] copyOf = Arrays.copyOf(zArr, length + length2);
        System.arraycopy(zArr2, 0, copyOf, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @NotNull
    public static final char[] plus(@NotNull char[] cArr, @NotNull char[] cArr2) {
        Intrinsics.checkParameterIsNotNull(cArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(cArr2, "elements");
        int length = cArr.length;
        int length2 = cArr2.length;
        char[] copyOf = Arrays.copyOf(cArr, length + length2);
        System.arraycopy(cArr2, 0, copyOf, length, length2);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, ModAuthRequest.KEY_RESULT);
        return copyOf;
    }

    @InlineOnly
    private static final <T> T[] plusElement(@NotNull T[] tArr, T t) {
        return ArraysKt.plus(tArr, t);
    }

    public static final void sort(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "$receiver");
        if (iArr.length > 1) {
            Arrays.sort(iArr);
        }
    }

    public static final void sort(@NotNull long[] jArr) {
        Intrinsics.checkParameterIsNotNull(jArr, "$receiver");
        if (jArr.length > 1) {
            Arrays.sort(jArr);
        }
    }

    public static final void sort(@NotNull byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        if (bArr.length > 1) {
            Arrays.sort(bArr);
        }
    }

    public static final void sort(@NotNull short[] sArr) {
        Intrinsics.checkParameterIsNotNull(sArr, "$receiver");
        if (sArr.length > 1) {
            Arrays.sort(sArr);
        }
    }

    public static final void sort(@NotNull double[] dArr) {
        Intrinsics.checkParameterIsNotNull(dArr, "$receiver");
        if (dArr.length > 1) {
            Arrays.sort(dArr);
        }
    }

    public static final void sort(@NotNull float[] fArr) {
        Intrinsics.checkParameterIsNotNull(fArr, "$receiver");
        if (fArr.length > 1) {
            Arrays.sort(fArr);
        }
    }

    public static final void sort(@NotNull char[] cArr) {
        Intrinsics.checkParameterIsNotNull(cArr, "$receiver");
        if (cArr.length > 1) {
            Arrays.sort(cArr);
        }
    }

    @InlineOnly
    private static final <T extends Comparable<? super T>> void sort(@NotNull T[] tArr) {
        if (tArr == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
        }
        ArraysKt.sort((T[]) (Object[]) tArr);
    }

    public static final <T> void sort(@NotNull T[] tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        if (tArr.length > 1) {
            Arrays.sort(tArr);
        }
    }

    public static /* bridge */ /* synthetic */ void sort$default(Object[] objArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = objArr.length;
        }
        ArraysKt.sort((T[]) objArr, i, i2);
    }

    public static final <T> void sort(@NotNull T[] tArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        Arrays.sort(tArr, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(byte[] bArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = bArr.length;
        }
        ArraysKt.sort(bArr, i, i2);
    }

    public static final void sort(@NotNull byte[] bArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        Arrays.sort(bArr, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(short[] sArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = sArr.length;
        }
        ArraysKt.sort(sArr, i, i2);
    }

    public static final void sort(@NotNull short[] sArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(sArr, "$receiver");
        Arrays.sort(sArr, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(int[] iArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = iArr.length;
        }
        ArraysKt.sort(iArr, i, i2);
    }

    public static final void sort(@NotNull int[] iArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(iArr, "$receiver");
        Arrays.sort(iArr, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(long[] jArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = jArr.length;
        }
        ArraysKt.sort(jArr, i, i2);
    }

    public static final void sort(@NotNull long[] jArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(jArr, "$receiver");
        Arrays.sort(jArr, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(float[] fArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = fArr.length;
        }
        ArraysKt.sort(fArr, i, i2);
    }

    public static final void sort(@NotNull float[] fArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(fArr, "$receiver");
        Arrays.sort(fArr, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(double[] dArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = dArr.length;
        }
        ArraysKt.sort(dArr, i, i2);
    }

    public static final void sort(@NotNull double[] dArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(dArr, "$receiver");
        Arrays.sort(dArr, i, i2);
    }

    public static /* bridge */ /* synthetic */ void sort$default(char[] cArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = cArr.length;
        }
        ArraysKt.sort(cArr, i, i2);
    }

    public static final void sort(@NotNull char[] cArr, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(cArr, "$receiver");
        Arrays.sort(cArr, i, i2);
    }

    public static final <T> void sortWith(@NotNull T[] tArr, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        if (tArr.length > 1) {
            Arrays.sort(tArr, comparator);
        }
    }

    public static /* bridge */ /* synthetic */ void sortWith$default(Object[] objArr, Comparator comparator, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = objArr.length;
        }
        ArraysKt.sortWith(objArr, comparator, i, i2);
    }

    public static final <T> void sortWith(@NotNull T[] tArr, @NotNull Comparator<? super T> comparator, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        Arrays.sort(tArr, i, i2, comparator);
    }

    @NotNull
    public static final Byte[] toTypedArray(@NotNull byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        Byte[] bArr2 = new Byte[bArr.length];
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            bArr2[i] = Byte.valueOf(bArr[i]);
        }
        return bArr2;
    }

    @NotNull
    public static final Short[] toTypedArray(@NotNull short[] sArr) {
        Intrinsics.checkParameterIsNotNull(sArr, "$receiver");
        Short[] shArr = new Short[sArr.length];
        int length = sArr.length;
        for (int i = 0; i < length; i++) {
            shArr[i] = Short.valueOf(sArr[i]);
        }
        return shArr;
    }

    @NotNull
    public static final Integer[] toTypedArray(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "$receiver");
        Integer[] numArr = new Integer[iArr.length];
        int length = iArr.length;
        for (int i = 0; i < length; i++) {
            numArr[i] = Integer.valueOf(iArr[i]);
        }
        return numArr;
    }

    @NotNull
    public static final Long[] toTypedArray(@NotNull long[] jArr) {
        Intrinsics.checkParameterIsNotNull(jArr, "$receiver");
        Long[] lArr = new Long[jArr.length];
        int length = jArr.length;
        for (int i = 0; i < length; i++) {
            lArr[i] = Long.valueOf(jArr[i]);
        }
        return lArr;
    }

    @NotNull
    public static final Float[] toTypedArray(@NotNull float[] fArr) {
        Intrinsics.checkParameterIsNotNull(fArr, "$receiver");
        Float[] fArr2 = new Float[fArr.length];
        int length = fArr.length;
        for (int i = 0; i < length; i++) {
            fArr2[i] = Float.valueOf(fArr[i]);
        }
        return fArr2;
    }

    @NotNull
    public static final Double[] toTypedArray(@NotNull double[] dArr) {
        Intrinsics.checkParameterIsNotNull(dArr, "$receiver");
        Double[] dArr2 = new Double[dArr.length];
        int length = dArr.length;
        for (int i = 0; i < length; i++) {
            dArr2[i] = Double.valueOf(dArr[i]);
        }
        return dArr2;
    }

    @NotNull
    public static final Boolean[] toTypedArray(@NotNull boolean[] zArr) {
        Intrinsics.checkParameterIsNotNull(zArr, "$receiver");
        Boolean[] boolArr = new Boolean[zArr.length];
        int length = zArr.length;
        for (int i = 0; i < length; i++) {
            boolArr[i] = Boolean.valueOf(zArr[i]);
        }
        return boolArr;
    }

    @NotNull
    public static final Character[] toTypedArray(@NotNull char[] cArr) {
        Intrinsics.checkParameterIsNotNull(cArr, "$receiver");
        Character[] chArr = new Character[cArr.length];
        int length = cArr.length;
        for (int i = 0; i < length; i++) {
            chArr[i] = Character.valueOf(cArr[i]);
        }
        return chArr;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(@NotNull T[] tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        return (SortedSet) ArraysKt.toCollection(tArr, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Byte> toSortedSet(@NotNull byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "$receiver");
        return (SortedSet) ArraysKt.toCollection(bArr, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Short> toSortedSet(@NotNull short[] sArr) {
        Intrinsics.checkParameterIsNotNull(sArr, "$receiver");
        return (SortedSet) ArraysKt.toCollection(sArr, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Integer> toSortedSet(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "$receiver");
        return (SortedSet) ArraysKt.toCollection(iArr, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Long> toSortedSet(@NotNull long[] jArr) {
        Intrinsics.checkParameterIsNotNull(jArr, "$receiver");
        return (SortedSet) ArraysKt.toCollection(jArr, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Float> toSortedSet(@NotNull float[] fArr) {
        Intrinsics.checkParameterIsNotNull(fArr, "$receiver");
        return (SortedSet) ArraysKt.toCollection(fArr, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Double> toSortedSet(@NotNull double[] dArr) {
        Intrinsics.checkParameterIsNotNull(dArr, "$receiver");
        return (SortedSet) ArraysKt.toCollection(dArr, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Boolean> toSortedSet(@NotNull boolean[] zArr) {
        Intrinsics.checkParameterIsNotNull(zArr, "$receiver");
        return (SortedSet) ArraysKt.toCollection(zArr, new TreeSet());
    }

    @NotNull
    public static final SortedSet<Character> toSortedSet(@NotNull char[] cArr) {
        Intrinsics.checkParameterIsNotNull(cArr, "$receiver");
        return (SortedSet) ArraysKt.toCollection(cArr, new TreeSet());
    }

    @NotNull
    public static final <T> SortedSet<T> toSortedSet(@NotNull T[] tArr, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        return (SortedSet) ArraysKt.toCollection(tArr, new TreeSet(comparator));
    }
}
