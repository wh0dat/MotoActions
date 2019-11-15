package android.support.p004v7.util;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.p004v7.widget.RecyclerView.Adapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* renamed from: android.support.v7.util.DiffUtil */
public class DiffUtil {
    private static final Comparator<Snake> SNAKE_COMPARATOR = new Comparator<Snake>() {
        public int compare(Snake snake, Snake snake2) {
            int i = snake.f26x - snake2.f26x;
            return i == 0 ? snake.f27y - snake2.f27y : i;
        }
    };

    /* renamed from: android.support.v7.util.DiffUtil$Callback */
    public static abstract class Callback {
        public abstract boolean areContentsTheSame(int i, int i2);

        public abstract boolean areItemsTheSame(int i, int i2);

        @Nullable
        public Object getChangePayload(int i, int i2) {
            return null;
        }

        public abstract int getNewListSize();

        public abstract int getOldListSize();
    }

    /* renamed from: android.support.v7.util.DiffUtil$DiffResult */
    public static class DiffResult {
        private static final int FLAG_CHANGED = 2;
        private static final int FLAG_IGNORE = 16;
        private static final int FLAG_MASK = 31;
        private static final int FLAG_MOVED_CHANGED = 4;
        private static final int FLAG_MOVED_NOT_CHANGED = 8;
        private static final int FLAG_NOT_CHANGED = 1;
        private static final int FLAG_OFFSET = 5;
        private final Callback mCallback;
        private final boolean mDetectMoves;
        private final int[] mNewItemStatuses;
        private final int mNewListSize;
        private final int[] mOldItemStatuses;
        private final int mOldListSize;
        private final List<Snake> mSnakes;

        DiffResult(Callback callback, List<Snake> list, int[] iArr, int[] iArr2, boolean z) {
            this.mSnakes = list;
            this.mOldItemStatuses = iArr;
            this.mNewItemStatuses = iArr2;
            Arrays.fill(this.mOldItemStatuses, 0);
            Arrays.fill(this.mNewItemStatuses, 0);
            this.mCallback = callback;
            this.mOldListSize = callback.getOldListSize();
            this.mNewListSize = callback.getNewListSize();
            this.mDetectMoves = z;
            addRootSnake();
            findMatchingItems();
        }

        private void addRootSnake() {
            Snake snake = this.mSnakes.isEmpty() ? null : (Snake) this.mSnakes.get(0);
            if (snake == null || snake.f26x != 0 || snake.f27y != 0) {
                Snake snake2 = new Snake();
                snake2.f26x = 0;
                snake2.f27y = 0;
                snake2.removal = false;
                snake2.size = 0;
                snake2.reverse = false;
                this.mSnakes.add(0, snake2);
            }
        }

        private void findMatchingItems() {
            int i = this.mOldListSize;
            int i2 = this.mNewListSize;
            for (int size = this.mSnakes.size() - 1; size >= 0; size--) {
                Snake snake = (Snake) this.mSnakes.get(size);
                int i3 = snake.f26x + snake.size;
                int i4 = snake.f27y + snake.size;
                if (this.mDetectMoves) {
                    while (i > i3) {
                        findAddition(i, i2, size);
                        i--;
                    }
                    while (i2 > i4) {
                        findRemoval(i, i2, size);
                        i2--;
                    }
                }
                for (int i5 = 0; i5 < snake.size; i5++) {
                    int i6 = snake.f26x + i5;
                    int i7 = snake.f27y + i5;
                    int i8 = this.mCallback.areContentsTheSame(i6, i7) ? 1 : 2;
                    this.mOldItemStatuses[i6] = (i7 << 5) | i8;
                    this.mNewItemStatuses[i7] = (i6 << 5) | i8;
                }
                i = snake.f26x;
                i2 = snake.f27y;
            }
        }

        private void findAddition(int i, int i2, int i3) {
            if (this.mOldItemStatuses[i - 1] == 0) {
                findMatchingItem(i, i2, i3, false);
            }
        }

        private void findRemoval(int i, int i2, int i3) {
            if (this.mNewItemStatuses[i2 - 1] == 0) {
                findMatchingItem(i, i2, i3, true);
            }
        }

        private boolean findMatchingItem(int i, int i2, int i3, boolean z) {
            int i4;
            int i5;
            if (z) {
                i2--;
                i5 = i;
                i4 = i2;
            } else {
                i5 = i - 1;
                i4 = i5;
            }
            while (i3 >= 0) {
                Snake snake = (Snake) this.mSnakes.get(i3);
                int i6 = snake.f26x + snake.size;
                int i7 = snake.f27y + snake.size;
                int i8 = 4;
                if (z) {
                    for (int i9 = i5 - 1; i9 >= i6; i9--) {
                        if (this.mCallback.areItemsTheSame(i9, i4)) {
                            if (this.mCallback.areContentsTheSame(i9, i4)) {
                                i8 = 8;
                            }
                            this.mNewItemStatuses[i4] = (i9 << 5) | 16;
                            this.mOldItemStatuses[i9] = (i4 << 5) | i8;
                            return true;
                        }
                    }
                    continue;
                } else {
                    for (int i10 = i2 - 1; i10 >= i7; i10--) {
                        if (this.mCallback.areItemsTheSame(i4, i10)) {
                            if (this.mCallback.areContentsTheSame(i4, i10)) {
                                i8 = 8;
                            }
                            int i11 = i - 1;
                            this.mOldItemStatuses[i11] = (i10 << 5) | 16;
                            this.mNewItemStatuses[i10] = (i11 << 5) | i8;
                            return true;
                        }
                    }
                    continue;
                }
                i5 = snake.f26x;
                i2 = snake.f27y;
                i3--;
            }
            return false;
        }

        public void dispatchUpdatesTo(final Adapter adapter) {
            dispatchUpdatesTo((ListUpdateCallback) new ListUpdateCallback() {
                public void onInserted(int i, int i2) {
                    adapter.notifyItemRangeInserted(i, i2);
                }

                public void onRemoved(int i, int i2) {
                    adapter.notifyItemRangeRemoved(i, i2);
                }

                public void onMoved(int i, int i2) {
                    adapter.notifyItemMoved(i, i2);
                }

                public void onChanged(int i, int i2, Object obj) {
                    adapter.notifyItemRangeChanged(i, i2, obj);
                }
            });
        }

        public void dispatchUpdatesTo(ListUpdateCallback listUpdateCallback) {
            BatchingListUpdateCallback batchingListUpdateCallback;
            if (listUpdateCallback instanceof BatchingListUpdateCallback) {
                batchingListUpdateCallback = (BatchingListUpdateCallback) listUpdateCallback;
            } else {
                batchingListUpdateCallback = new BatchingListUpdateCallback(listUpdateCallback);
            }
            ArrayList arrayList = new ArrayList();
            int i = this.mOldListSize;
            int i2 = this.mNewListSize;
            for (int size = this.mSnakes.size() - 1; size >= 0; size--) {
                Snake snake = (Snake) this.mSnakes.get(size);
                int i3 = snake.size;
                int i4 = snake.f26x + i3;
                int i5 = snake.f27y + i3;
                if (i4 < i) {
                    dispatchRemovals(arrayList, batchingListUpdateCallback, i4, i - i4, i4);
                }
                if (i5 < i2) {
                    dispatchAdditions(arrayList, batchingListUpdateCallback, i4, i2 - i5, i5);
                }
                for (int i6 = i3 - 1; i6 >= 0; i6--) {
                    if ((this.mOldItemStatuses[snake.f26x + i6] & 31) == 2) {
                        batchingListUpdateCallback.onChanged(snake.f26x + i6, 1, this.mCallback.getChangePayload(snake.f26x + i6, snake.f27y + i6));
                    }
                }
                i = snake.f26x;
                i2 = snake.f27y;
            }
            batchingListUpdateCallback.dispatchLastEvent();
        }

        private static PostponedUpdate removePostponedUpdate(List<PostponedUpdate> list, int i, boolean z) {
            int size = list.size() - 1;
            while (size >= 0) {
                PostponedUpdate postponedUpdate = (PostponedUpdate) list.get(size);
                if (postponedUpdate.posInOwnerList == i && postponedUpdate.removal == z) {
                    list.remove(size);
                    while (size < list.size()) {
                        PostponedUpdate postponedUpdate2 = (PostponedUpdate) list.get(size);
                        postponedUpdate2.currentPos += z ? 1 : -1;
                        size++;
                    }
                    return postponedUpdate;
                }
                size--;
            }
            return null;
        }

        private void dispatchAdditions(List<PostponedUpdate> list, ListUpdateCallback listUpdateCallback, int i, int i2, int i3) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onInserted(i, i2);
                return;
            }
            for (int i4 = i2 - 1; i4 >= 0; i4--) {
                int i5 = i3 + i4;
                int i6 = this.mNewItemStatuses[i5] & 31;
                if (i6 == 0) {
                    listUpdateCallback.onInserted(i, 1);
                    for (PostponedUpdate postponedUpdate : list) {
                        postponedUpdate.currentPos++;
                    }
                } else if (i6 == 4 || i6 == 8) {
                    int i7 = this.mNewItemStatuses[i5] >> 5;
                    listUpdateCallback.onMoved(removePostponedUpdate(list, i7, true).currentPos, i);
                    if (i6 == 4) {
                        listUpdateCallback.onChanged(i, 1, this.mCallback.getChangePayload(i7, i5));
                    }
                } else if (i6 != 16) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unknown flag for pos ");
                    sb.append(i5);
                    sb.append(" ");
                    sb.append(Long.toBinaryString((long) i6));
                    throw new IllegalStateException(sb.toString());
                } else {
                    list.add(new PostponedUpdate(i5, i, false));
                }
            }
        }

        private void dispatchRemovals(List<PostponedUpdate> list, ListUpdateCallback listUpdateCallback, int i, int i2, int i3) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onRemoved(i, i2);
                return;
            }
            for (int i4 = i2 - 1; i4 >= 0; i4--) {
                int i5 = i3 + i4;
                int i6 = this.mOldItemStatuses[i5] & 31;
                if (i6 == 0) {
                    listUpdateCallback.onRemoved(i + i4, 1);
                    for (PostponedUpdate postponedUpdate : list) {
                        postponedUpdate.currentPos--;
                    }
                } else if (i6 == 4 || i6 == 8) {
                    int i7 = this.mOldItemStatuses[i5] >> 5;
                    PostponedUpdate removePostponedUpdate = removePostponedUpdate(list, i7, false);
                    listUpdateCallback.onMoved(i + i4, removePostponedUpdate.currentPos - 1);
                    if (i6 == 4) {
                        listUpdateCallback.onChanged(removePostponedUpdate.currentPos - 1, 1, this.mCallback.getChangePayload(i5, i7));
                    }
                } else if (i6 != 16) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unknown flag for pos ");
                    sb.append(i5);
                    sb.append(" ");
                    sb.append(Long.toBinaryString((long) i6));
                    throw new IllegalStateException(sb.toString());
                } else {
                    list.add(new PostponedUpdate(i5, i + i4, true));
                }
            }
        }

        /* access modifiers changed from: 0000 */
        @VisibleForTesting
        public List<Snake> getSnakes() {
            return this.mSnakes;
        }
    }

    /* renamed from: android.support.v7.util.DiffUtil$PostponedUpdate */
    private static class PostponedUpdate {
        int currentPos;
        int posInOwnerList;
        boolean removal;

        public PostponedUpdate(int i, int i2, boolean z) {
            this.posInOwnerList = i;
            this.currentPos = i2;
            this.removal = z;
        }
    }

    /* renamed from: android.support.v7.util.DiffUtil$Range */
    static class Range {
        int newListEnd;
        int newListStart;
        int oldListEnd;
        int oldListStart;

        public Range() {
        }

        public Range(int i, int i2, int i3, int i4) {
            this.oldListStart = i;
            this.oldListEnd = i2;
            this.newListStart = i3;
            this.newListEnd = i4;
        }
    }

    /* renamed from: android.support.v7.util.DiffUtil$Snake */
    static class Snake {
        boolean removal;
        boolean reverse;
        int size;

        /* renamed from: x */
        int f26x;

        /* renamed from: y */
        int f27y;

        Snake() {
        }
    }

    private DiffUtil() {
    }

    public static DiffResult calculateDiff(Callback callback) {
        return calculateDiff(callback, true);
    }

    public static DiffResult calculateDiff(Callback callback, boolean z) {
        int oldListSize = callback.getOldListSize();
        int newListSize = callback.getNewListSize();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new Range(0, oldListSize, 0, newListSize));
        int abs = Math.abs(oldListSize - newListSize) + oldListSize + newListSize;
        int i = abs * 2;
        int[] iArr = new int[i];
        int[] iArr2 = new int[i];
        ArrayList arrayList3 = new ArrayList();
        while (!arrayList2.isEmpty()) {
            Range range = (Range) arrayList2.remove(arrayList2.size() - 1);
            Snake diffPartial = diffPartial(callback, range.oldListStart, range.oldListEnd, range.newListStart, range.newListEnd, iArr, iArr2, abs);
            if (diffPartial != null) {
                if (diffPartial.size > 0) {
                    arrayList.add(diffPartial);
                }
                diffPartial.f26x += range.oldListStart;
                diffPartial.f27y += range.newListStart;
                Range range2 = arrayList3.isEmpty() ? new Range() : (Range) arrayList3.remove(arrayList3.size() - 1);
                range2.oldListStart = range.oldListStart;
                range2.newListStart = range.newListStart;
                if (diffPartial.reverse) {
                    range2.oldListEnd = diffPartial.f26x;
                    range2.newListEnd = diffPartial.f27y;
                } else if (diffPartial.removal) {
                    range2.oldListEnd = diffPartial.f26x - 1;
                    range2.newListEnd = diffPartial.f27y;
                } else {
                    range2.oldListEnd = diffPartial.f26x;
                    range2.newListEnd = diffPartial.f27y - 1;
                }
                arrayList2.add(range2);
                if (!diffPartial.reverse) {
                    range.oldListStart = diffPartial.f26x + diffPartial.size;
                    range.newListStart = diffPartial.f27y + diffPartial.size;
                } else if (diffPartial.removal) {
                    range.oldListStart = diffPartial.f26x + diffPartial.size + 1;
                    range.newListStart = diffPartial.f27y + diffPartial.size;
                } else {
                    range.oldListStart = diffPartial.f26x + diffPartial.size;
                    range.newListStart = diffPartial.f27y + diffPartial.size + 1;
                }
                arrayList2.add(range);
            } else {
                arrayList3.add(range);
            }
        }
        Collections.sort(arrayList, SNAKE_COMPARATOR);
        DiffResult diffResult = new DiffResult(callback, arrayList, iArr, iArr2, z);
        return diffResult;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00e0, code lost:
        if (r4[r9 - 1] < r4[r9 + 1]) goto L_0x00f2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.support.p004v7.util.DiffUtil.Snake diffPartial(android.support.p004v7.util.DiffUtil.Callback r22, int r23, int r24, int r25, int r26, int[] r27, int[] r28, int r29) {
        /*
            r0 = r22
            r3 = r27
            r4 = r28
            int r5 = r24 - r23
            int r6 = r26 - r25
            r7 = 1
            if (r5 < r7) goto L_0x015a
            if (r6 >= r7) goto L_0x0011
            goto L_0x015a
        L_0x0011:
            int r8 = r5 - r6
            int r9 = r5 + r6
            int r9 = r9 + r7
            int r9 = r9 / 2
            int r10 = r29 - r9
            int r10 = r10 - r7
            int r11 = r29 + r9
            int r11 = r11 + r7
            r12 = 0
            java.util.Arrays.fill(r3, r10, r11, r12)
            int r10 = r10 + r8
            int r11 = r11 + r8
            java.util.Arrays.fill(r4, r10, r11, r5)
            int r10 = r8 % 2
            if (r10 == 0) goto L_0x002d
            r10 = r7
            goto L_0x002e
        L_0x002d:
            r10 = r12
        L_0x002e:
            r11 = r12
        L_0x002f:
            if (r11 > r9) goto L_0x0152
            int r13 = -r11
            r14 = r13
        L_0x0033:
            if (r14 > r11) goto L_0x00c0
            if (r14 == r13) goto L_0x0054
            if (r14 == r11) goto L_0x0047
            int r15 = r29 + r14
            int r16 = r15 + -1
            r12 = r3[r16]
            int r15 = r15 + r7
            r7 = r3[r15]
            if (r12 >= r7) goto L_0x0047
            r17 = 1
            goto L_0x0056
        L_0x0047:
            int r7 = r29 + r14
            r17 = 1
            int r7 = r7 + -1
            r7 = r3[r7]
            int r7 = r7 + 1
            r12 = r17
            goto L_0x005d
        L_0x0054:
            r17 = r7
        L_0x0056:
            int r7 = r29 + r14
            int r7 = r7 + 1
            r7 = r3[r7]
            r12 = 0
        L_0x005d:
            int r15 = r7 - r14
            r18 = r9
            r9 = r15
        L_0x0062:
            if (r7 >= r5) goto L_0x007d
            if (r9 >= r6) goto L_0x007d
            r19 = r5
            int r5 = r23 + r7
            r20 = r6
            int r6 = r25 + r9
            boolean r5 = r0.areItemsTheSame(r5, r6)
            if (r5 == 0) goto L_0x0081
            int r7 = r7 + 1
            int r9 = r9 + 1
            r5 = r19
            r6 = r20
            goto L_0x0062
        L_0x007d:
            r19 = r5
            r20 = r6
        L_0x0081:
            int r5 = r29 + r14
            r3[r5] = r7
            if (r10 == 0) goto L_0x00b3
            int r6 = r8 - r11
            r7 = 1
            int r6 = r6 + r7
            if (r14 < r6) goto L_0x00b3
            int r6 = r8 + r11
            int r6 = r6 - r7
            if (r14 > r6) goto L_0x00b3
            r6 = r3[r5]
            r7 = r4[r5]
            if (r6 < r7) goto L_0x00b3
            android.support.v7.util.DiffUtil$Snake r0 = new android.support.v7.util.DiffUtil$Snake
            r0.<init>()
            r1 = r4[r5]
            r0.f26x = r1
            int r1 = r0.f26x
            int r1 = r1 - r14
            r0.f27y = r1
            r1 = r3[r5]
            r2 = r4[r5]
            int r1 = r1 - r2
            r0.size = r1
            r0.removal = r12
            r5 = 0
            r0.reverse = r5
            return r0
        L_0x00b3:
            r5 = 0
            int r14 = r14 + 2
            r12 = r5
            r9 = r18
            r5 = r19
            r6 = r20
            r7 = 1
            goto L_0x0033
        L_0x00c0:
            r19 = r5
            r20 = r6
            r18 = r9
            r5 = r12
            r6 = r13
        L_0x00c8:
            if (r6 > r11) goto L_0x0145
            int r7 = r6 + r8
            int r9 = r11 + r8
            if (r7 == r9) goto L_0x00f0
            int r9 = r13 + r8
            if (r7 == r9) goto L_0x00e3
            int r9 = r29 + r7
            int r12 = r9 + -1
            r12 = r4[r12]
            r17 = 1
            int r9 = r9 + 1
            r9 = r4[r9]
            if (r12 >= r9) goto L_0x00e5
            goto L_0x00f2
        L_0x00e3:
            r17 = 1
        L_0x00e5:
            int r9 = r29 + r7
            int r9 = r9 + 1
            r9 = r4[r9]
            int r9 = r9 + -1
            r12 = r17
            goto L_0x00f9
        L_0x00f0:
            r17 = 1
        L_0x00f2:
            int r9 = r29 + r7
            int r9 = r9 + -1
            r9 = r4[r9]
            r12 = r5
        L_0x00f9:
            int r14 = r9 - r7
        L_0x00fb:
            if (r9 <= 0) goto L_0x0115
            if (r14 <= 0) goto L_0x0115
            int r15 = r23 + r9
            int r5 = r15 + -1
            int r15 = r25 + r14
            int r1 = r15 + -1
            boolean r1 = r0.areItemsTheSame(r5, r1)
            if (r1 == 0) goto L_0x0115
            int r9 = r9 + -1
            int r14 = r14 + -1
            r5 = 0
            r17 = 1
            goto L_0x00fb
        L_0x0115:
            int r1 = r29 + r7
            r4[r1] = r9
            if (r10 != 0) goto L_0x0140
            if (r7 < r13) goto L_0x0140
            if (r7 > r11) goto L_0x0140
            r5 = r3[r1]
            r9 = r4[r1]
            if (r5 < r9) goto L_0x0140
            android.support.v7.util.DiffUtil$Snake r0 = new android.support.v7.util.DiffUtil$Snake
            r0.<init>()
            r2 = r4[r1]
            r0.f26x = r2
            int r2 = r0.f26x
            int r2 = r2 - r7
            r0.f27y = r2
            r2 = r3[r1]
            r1 = r4[r1]
            int r2 = r2 - r1
            r0.size = r2
            r0.removal = r12
            r1 = 1
            r0.reverse = r1
            return r0
        L_0x0140:
            r1 = 1
            int r6 = r6 + 2
            r5 = 0
            goto L_0x00c8
        L_0x0145:
            r1 = 1
            int r11 = r11 + 1
            r7 = r1
            r9 = r18
            r5 = r19
            r6 = r20
            r12 = 0
            goto L_0x002f
        L_0x0152:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation."
            r0.<init>(r1)
            throw r0
        L_0x015a:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.util.DiffUtil.diffPartial(android.support.v7.util.DiffUtil$Callback, int, int, int, int, int[], int[], int):android.support.v7.util.DiffUtil$Snake");
    }
}
