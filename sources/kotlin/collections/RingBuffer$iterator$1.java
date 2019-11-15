package kotlin.collections;

import kotlin.Metadata;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u00020\u0007H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006\b"}, mo14495d2 = {"kotlin/collections/RingBuffer$iterator$1", "Lkotlin/collections/AbstractIterator;", "(Lkotlin/collections/RingBuffer;)V", "count", "", "index", "computeNext", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: SlidingWindow.kt */
public final class RingBuffer$iterator$1 extends AbstractIterator<T> {
    private int count;
    private int index;
    final /* synthetic */ RingBuffer this$0;

    RingBuffer$iterator$1(RingBuffer ringBuffer) {
        this.this$0 = ringBuffer;
        this.count = ringBuffer.size();
        this.index = ringBuffer.startIndex;
    }

    /* access modifiers changed from: protected */
    public void computeNext() {
        if (this.count == 0) {
            done();
            return;
        }
        setNext(this.this$0.buffer[this.index]);
        this.index = (this.index + 1) % this.this$0.getCapacity();
        this.count--;
    }
}
