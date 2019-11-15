package com.motorola.actions.debug.items;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import com.motorola.actions.ActionsApplication;

public abstract class DebugItem {
    private String mDescription = "";
    private OnClickListener mOnClickListener;
    private String mTitle = "";

    @SuppressLint({"StaticFieldLeak"})
    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private BackgroundTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            DebugItem.this.startBackgroundWork();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            DebugItem.this.startPostExecuteWork();
        }
    }

    /* access modifiers changed from: 0000 */
    public void startBackgroundWork() {
    }

    /* access modifiers changed from: 0000 */
    public void startPostExecuteWork() {
    }

    public void updateData() {
    }

    DebugItem() {
        setOnClickListener(new DebugItem$$Lambda$0(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$new$0$DebugItem(View view) {
        startWork();
    }

    public OnClickListener getOnClickListener() {
        return this.mOnClickListener;
    }

    /* access modifiers changed from: 0000 */
    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public String getTitle() {
        return this.mTitle;
    }

    /* access modifiers changed from: 0000 */
    public void setTitle(String str) {
        this.mTitle = str;
    }

    public String getDescription() {
        return this.mDescription;
    }

    /* access modifiers changed from: 0000 */
    public void setDescription(String str) {
        this.mDescription = str;
    }

    /* access modifiers changed from: 0000 */
    public String getString(int i) {
        return ActionsApplication.getAppContext().getResources().getString(i);
    }

    /* access modifiers changed from: 0000 */
    public String getString(int i, Object... objArr) {
        return ActionsApplication.getAppContext().getResources().getString(i, objArr);
    }

    /* access modifiers changed from: 0000 */
    public void startWork() {
        new BackgroundTask().execute(new Void[0]);
    }
}
