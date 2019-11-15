package kotlin.p017io;

import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0007"}, mo14495d2 = {"walk", "Lkotlin/io/FileTreeWalk;", "Ljava/io/File;", "direction", "Lkotlin/io/FileWalkDirection;", "walkBottomUp", "walkTopDown", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/io/FilesKt")
/* renamed from: kotlin.io.FilesKt__FileTreeWalkKt */
/* compiled from: FileTreeWalk.kt */
class FilesKt__FileTreeWalkKt extends FilesKt__FileReadWriteKt {
    @NotNull
    public static /* bridge */ /* synthetic */ FileTreeWalk walk$default(File file, FileWalkDirection fileWalkDirection, int i, Object obj) {
        if ((i & 1) != 0) {
            fileWalkDirection = FileWalkDirection.TOP_DOWN;
        }
        return FilesKt.walk(file, fileWalkDirection);
    }

    @NotNull
    public static final FileTreeWalk walk(@NotNull File file, @NotNull FileWalkDirection fileWalkDirection) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        Intrinsics.checkParameterIsNotNull(fileWalkDirection, "direction");
        return new FileTreeWalk(file, fileWalkDirection);
    }

    @NotNull
    public static final FileTreeWalk walkTopDown(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        return FilesKt.walk(file, FileWalkDirection.TOP_DOWN);
    }

    @NotNull
    public static final FileTreeWalk walkBottomUp(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "$receiver");
        return FilesKt.walk(file, FileWalkDirection.BOTTOM_UP);
    }
}
