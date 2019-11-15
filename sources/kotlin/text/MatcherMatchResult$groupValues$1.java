package kotlin.text;

import kotlin.Metadata;
import kotlin.collections.AbstractList;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0011\u0010\b\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\u0005H\u0002R\u0014\u0010\u0004\u001a\u00020\u00058VX\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\n"}, mo14495d2 = {"kotlin/text/MatcherMatchResult$groupValues$1", "Lkotlin/collections/AbstractList;", "", "(Lkotlin/text/MatcherMatchResult;)V", "size", "", "getSize", "()I", "get", "index", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Regex.kt */
public final class MatcherMatchResult$groupValues$1 extends AbstractList<String> {
    final /* synthetic */ MatcherMatchResult this$0;

    MatcherMatchResult$groupValues$1(MatcherMatchResult matcherMatchResult) {
        this.this$0 = matcherMatchResult;
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof String) {
            return contains((String) obj);
        }
        return false;
    }

    public /* bridge */ boolean contains(String str) {
        return super.contains(str);
    }

    public final /* bridge */ int indexOf(Object obj) {
        if (obj instanceof String) {
            return indexOf((String) obj);
        }
        return -1;
    }

    public /* bridge */ int indexOf(String str) {
        return super.indexOf(str);
    }

    public final /* bridge */ int lastIndexOf(Object obj) {
        if (obj instanceof String) {
            return lastIndexOf((String) obj);
        }
        return -1;
    }

    public /* bridge */ int lastIndexOf(String str) {
        return super.lastIndexOf(str);
    }

    public int getSize() {
        return this.this$0.matchResult.groupCount() + 1;
    }

    @NotNull
    public String get(int i) {
        String group = this.this$0.matchResult.group(i);
        return group != null ? group : "";
    }
}
