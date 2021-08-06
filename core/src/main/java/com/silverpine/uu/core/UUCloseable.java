package com.silverpine.uu.core;

import androidx.annotation.Nullable;

import java.io.Closeable;

import com.silverpine.uu.logging.UULog;

/**
 * Simple helpers for the Closable interface
 */
public class UUCloseable
{
    private UUCloseable() {}

    public static void safeClose(@Nullable final Closeable closeable)
    {
        try
        {
            if (closeable != null)
            {
                closeable.close();
            }
        }
        catch (Exception ex)
        {
            UULog.debug(UUCloseable.class, "safeClose", ex);
        }
    }
}
