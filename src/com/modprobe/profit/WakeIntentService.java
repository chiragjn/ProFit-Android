package com.modprobe.profit;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public abstract class WakeIntentService extends IntentService {

    abstract void doReminderWork(Intent intent);

    public static final String LOCK_NAME_STATIC = "com.modprobe.profit.static";
    private static PowerManager.WakeLock lockStatic = null;

    public static void acquireStaticLock(Context context) {
        getLock(context).acquire();
    }

    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic == null) {
            PowerManager powManager = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            lockStatic = powManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }
        return (lockStatic);
    }

    public WakeIntentService(String name) {
        super(name);
    }

    @Override
    final protected void onHandleIntent(Intent intent) {
        try {
            doReminderWork(intent);
        } finally {
            getLock(this).release();
        }
    }}