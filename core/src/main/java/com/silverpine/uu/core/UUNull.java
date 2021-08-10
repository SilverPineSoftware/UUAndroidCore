package com.silverpine.uu.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UUNull
{
    public static long getLong(@Nullable final Long val, final long defaultValue)
    {
        return (val != null ? val : defaultValue);
    }

    public static int getInt(@Nullable final Integer val, final int defaultValue)
    {
        return (val != null ? val : defaultValue);
    }

    public static short getShort(@Nullable final Short val, final short defaultValue)
    {
        return (val != null ? val : defaultValue);
    }

    public static byte getByte(@Nullable final Byte val, final byte defaultValue)
    {
        return (val != null ? val : defaultValue);
    }

    public static boolean getBool(@Nullable final Boolean val, final boolean defaultValue)
    {
        return (val != null ? val : defaultValue);
    }

    @NonNull
    public static String getString(@Nullable final String val, @NonNull final String defaultValue)
    {
        return (val != null ? val : defaultValue);
    }

    @NonNull
    public static byte[] getByteArray(@Nullable final byte[] val, @NonNull final byte[] defaultValue)
    {
        return (val != null ? val : defaultValue);
    }

    public static double getDouble(@Nullable final Double val, @NonNull final Double defaultValue)
    {
        return (val != null ? val : defaultValue);
    }

    public static long getLong(@Nullable final Long val)
    {
        return getLong(val, 0L);
    }

    public static int getInt(@Nullable final Integer val)
    {
        return getInt(val, 0);
    }

    public static short getShort(@Nullable final Short val)
    {
        return getShort(val, (short)0);
    }

    public static byte getByte(@Nullable final Byte val)
    {
        return getByte(val, (byte)0);
    }

    public static boolean getBool(@Nullable final Boolean val)
    {
        return getBool(val, false);
    }

    public static double getDouble(@Nullable final Double val)
    {
        return getDouble(val, 0.0);
    }

    @NonNull
    public static String getString(@Nullable final String val)
    {
        return getString(val, "");
    }

    @NonNull
    public static byte[] getByteArray(@Nullable final byte[] val)
    {
        return getByteArray(val, new byte[0]);
    }
}