package com.silverpine.uu.core;

import com.silverpine.uu.logging.UULog;

import androidx.annotation.Nullable;

public class UURunnable
{
    public static void safeInvoke(@Nullable final Runnable runnable)
    {
        try
        {
            if (runnable != null)
            {
                runnable.run();
            }
        }
        catch (Exception ex)
        {
            UULog.error(UURunnable.class, "safeInvoke", ex);
        }
    }

    public static void safeInvokeOnMainThread(@Nullable final Runnable runnable)
    {
        UUThread.runOnMainThread(() ->
            safeInvoke(runnable));
    }
}
