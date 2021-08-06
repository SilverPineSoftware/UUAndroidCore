package com.silverpine.uu.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.silverpine.uu.logging.UULog;


/**
 * UUListDelegate
 *
 * Useful Utilities - Callback interface used to deliver a result list from an async operation.
 *
 */
public interface UUListDelegate<T>
{
    void onCompleted(@NonNull final ArrayList<T> list);

    static <T extends Object> void safeInvoke(@Nullable final UUListDelegate<T> delegate, @NonNull final ArrayList<T> list)
    {
        try
        {
            if (delegate != null)
            {
                delegate.onCompleted(list);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUListDelegate.class, "safeInvoke", ex);
        }
    }

    static <T extends Object> void safeInvokeOnMainThread(@Nullable final UUListDelegate<T> delegate, @NonNull final ArrayList<T> list)
    {
        UUThread.runOnMainThread(() ->
        {
            safeInvoke(delegate, list);
        });
    }
}
