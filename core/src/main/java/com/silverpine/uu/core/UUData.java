package com.silverpine.uu.core;

import com.silverpine.uu.logging.UULog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Useful set of methods for manipulating byte arrays
 *
 */
@SuppressWarnings("unused")
public class UUData
{
    /**
     * Extracts a set of bytes from a byte array
     *
     * @param source the source byte array
     * @param index starting index to copy
     * @param count number of bytes to copy
     * @return a byte array, or null if index and count are not in bounds
     */
    public static @Nullable byte[] subData(final @Nullable byte[] source, final int index, final int count)
    {
        /*
        guard index >= 0 else
        {
            return nil
        }

        let upperIndex = ((index + count) > self.count) ? self.count : index + count

        guard index <= upperIndex else
        {
            return nil
        }

        */

        if (source == null)
        {
            return null;
        }

        if (index < 0)
        {
            return null;
        }

        int dataLength = source.length;

        int countAvailable = dataLength - index;
        int actualCount = count;

        if (countAvailable < count)
        {
            actualCount = countAvailable;
        }

        byte[] dest = new byte[actualCount];
        System.arraycopy(source, index, dest, 0, dest.length);
        return dest;

        /*if (source != null && offset >= 0 && count >= 0 && (offset + count) <= source.length)
        {
            byte[] dest = new byte[count];
            System.arraycopy(source, offset, dest, 0, count);
            return dest;
        }
        else
        {
            return null;
        }*/
    }

    /**
     * Returns a full copy of the byte array
     *
     * @param source the source byte array
     * @return a byte array, or null if source is null
     */
    public static @Nullable byte[] copy(final @Nullable byte[] source)
    {
        if (source != null)
        {
            return subData(source, 0, source.length);
        }
        else
        {
            return null;
        }
    }

    /**
     * Checks a byte array for null and length
     *
     * @param b the byte array to check
     * @return true if the byte array is null or the length is zero
     */
    public static boolean isEmpty(final @Nullable byte[] b)
    {
        return (b == null || b.length == 0);
    }

    /**
     * Checks a byte array for null and length
     *
     * @param b the byte array to check
     * @return true if the byte array is not null and the length is greater zero
     */
    public static boolean isNotEmpty(final @Nullable byte[] b)
    {
        return (b != null && b.length > 0);
    }

    /**
     * Safely serializes an object
     *
     * @param obj the object to serialize
     * @return an array of bytes
     */
    public static byte[] serializeObject(@Nullable final Serializable obj)
    {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        byte[] result = null;

        try
        {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            result = bos.toByteArray();
        }
        catch (Exception ex)
        {
            UULog.debug(UUData.class, "serializeObject", ex);
            result = null;
        }
        finally
        {
            UUCloseable.safeClose(oos);
            UUCloseable.safeClose(bos);
        }

        return result;
    }

    @Nullable
    public static <T extends Serializable> T deserializeObject(@NonNull final Class<T> type, @Nullable final byte[] data)
    {
        if (data == null)
        {
            return null;
        }

        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;

        T result = null;

        try
        {
            bis = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bis);
            result = UUObject.safeCast(type, ois.readObject());

        }
        catch (Exception ex)
        {
            result = null;
        }
        finally
        {
            UUCloseable.safeClose(ois);
            UUCloseable.safeClose(bis);
        }

        return result;
    }

    @NonNull
    public static ArrayList<byte[]> slice(@NonNull final byte[] data, final int sliceSize)
    {
        ArrayList<byte[]> slices = new ArrayList<>();

        int index = 0;

        while (index < data.length)
        {
            byte[] slice = subData(data, index, sliceSize);
            if (slice != null)
            {
                slices.add(slice);
            }

            index += sliceSize;
        }

        return slices;
    }
}
