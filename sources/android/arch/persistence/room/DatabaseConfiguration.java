package android.arch.persistence.room;

import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Factory;
import android.arch.persistence.room.RoomDatabase.Callback;
import android.arch.persistence.room.RoomDatabase.MigrationContainer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.util.List;

public class DatabaseConfiguration {
    public final boolean allowMainThreadQueries;
    @Nullable
    public final List<Callback> callbacks;
    @NonNull
    public final Context context;
    @NonNull
    public final MigrationContainer migrationContainer;
    @Nullable
    public final String name;
    public final boolean requireMigration;
    @NonNull
    public final Factory sqliteOpenHelperFactory;

    @RestrictTo({Scope.LIBRARY_GROUP})
    public DatabaseConfiguration(@NonNull Context context2, @Nullable String str, @NonNull Factory factory, @NonNull MigrationContainer migrationContainer2, @Nullable List<Callback> list, boolean z, boolean z2) {
        this.sqliteOpenHelperFactory = factory;
        this.context = context2;
        this.name = str;
        this.migrationContainer = migrationContainer2;
        this.callbacks = list;
        this.allowMainThreadQueries = z;
        this.requireMigration = z2;
    }
}
