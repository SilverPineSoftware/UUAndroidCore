package com.silverpine.uu.core;

import com.silverpine.uu.logging.UULog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import kotlin.text.Charsets;

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
        if (source == null)
        {
            return null;
        }

        if (index < 0)
        {
            return null;
        }

        int dataLength = source.length;
        int upperIndex = Math.min((index + count), dataLength);

        if (index > upperIndex)
        {
            return null;
        }

        return Arrays.copyOfRange(source, index, upperIndex);
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
        byte[] result;

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
        catch (Exception ignored)
        {
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

    private static final int UINT8_MASK = (int)0x000000FF;
    private static final int UINT16_MASK = (int)0x0000FFFF;
    private static final long UINT32_MASK = 0xFFFFFFFFL;
    private static final long UINT64_MASK = 0xFFFFFFFFFFFFFFFFL;

    /**
     * Reads a UInt8 from a byte[] array.
     *
     * @param data the data to read from
     * @param index the index to read from
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     * @return UInt8 value at the index
     */
    public static int readUInt8(@NonNull final byte[] data, final int index)
    {
        ByteBuffer bb = ByteBuffer.wrap(data);
        return ((int)bb.get(index) & UINT8_MASK);
    }

    /**
     * Reads a UInt16 from a byte[] array.
     *
     * @param order the byte order to use for reading
     * @param data the data to read from
     * @param index the index to read from
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     * @return UInt16 value at the index
     */
    public static int readUInt16(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        return ((int)bb.getShort(index) & UINT16_MASK);
    }

    /**
     * Reads a UInt32 from a byte[] array.
     *
     * @param order the byte order to use for reading
     * @param data the data to read from
     * @param index the index to read from
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     * @return UInt32 value at the index
     */
    public static long readUInt32(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        return ((long)bb.getInt(index) & UINT32_MASK);
    }

    /**
     * Reads a UInt64 from a byte[] array.
     *
     * @param order ByteOrder order
     * @param data the data to read from
     * @param index the index to read from
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     * @return UInt64 value at the index
     */
    public static long readUInt64(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        return ((long)bb.getLong(index) & UINT64_MASK);
    }

    /**
     * Reads a UInt8 from a byte[] array.
     *
     * @param data the data to read from
     * @param index the index to read from
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     * @return UInt8 value at the index
     *
     */
    public static byte readInt8(@NonNull final byte[] data, final int index)
    {
        ByteBuffer bb = ByteBuffer.wrap(data);
        return bb.get(index);
    }

    /**
     * Reads a UInt16 from a byte[] array.
     *
     * @param order the byte order to use for reading
     * @param data the data to read from
     * @param index the index to read from
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     * @return UInt16 value at the index
     */
    public static short readInt16(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        return bb.getShort(index);
    }

    /**
     * Reads a Int32 from a byte[] array.
     *
     * @param order the byte order to use for reading
     * @param data the data to read from
     * @param index the index to read from
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     * @return Int32 value at the index
     */
    public static int readInt32(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        return bb.getInt(index);
    }

    /**
     * Reads a Int64 from a byte[] array.
     *
     * @param order the byte order to use for reading
     * @param data the data to read from
     * @param index the index to read from
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     * @return Int64 value at the index
     */
    public static long readInt64(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        return bb.getLong(index);
    }

    /**
     * Writes a UInt8 at the specified index
     *
     * @param data the buffer to write
     * @param index the index to write to
     * @param value the value to write
     *
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeUInt8(@NonNull final byte[] data, final int index, final int value)
    {
        data[index] = (byte) (value & 0xFF);
        return Byte.BYTES;
    }

    /**
     * Writes a UInt16 at the specified index
     *
     * @param order the byte order to use for writing
     * @param data the buffer to write
     * @param index the index to write to
     * @param value the value to write
     *
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeUInt16(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index, final int value)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        bb.putShort(index, (short)(value & 0xFFFF));
        return Short.BYTES;
    }

    /**
     * Writes a UInt32 at the specified index
     *
     * @param order the byte order to use for writing
     * @param data the buffer to write
     * @param index the index to write to
     * @param value the value to write
     *
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeUInt32(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index, final long value)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        bb.putInt(index, (int)(value & 0xFFFFFFFFL));
        return Integer.BYTES;
    }

    /**
     * Writes a UInt64 at the specified index
     *
     * @param order the byte order to use for writing
     * @param data the buffer to write
     * @param index the index to write to
     * @param value the value to write
     *
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeUInt64(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index, final long value)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        bb.putLong(index, (long)(value & UINT64_MASK));
        return Long.BYTES;
    }

    /**
     * Writes a Int8 at the specified index
     *
     * @param data the buffer to write
     * @param index the index to write to
     * @param value the value to write
     *
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeInt8(@NonNull final byte[] data, final int index, final byte value)
    {
        data[index] = value;
        return Byte.BYTES;
    }

    /**
     * Writes a Int16 at the specified index
     *
     * @param order the byte order to use for writing
     * @param data the buffer to write
     * @param index the index to write to
     * @param value the value to write
     *
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeInt16(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index, final short value)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        bb.putShort(index, value);
        return Short.BYTES;
    }

    /**
     * Writes a Int32 at the specified index
     *
     * @param order the byte order to use for writing
     * @param data the buffer to write
     * @param index the index to write to
     * @param value the value to write
     *
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeInt32(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index, final int value)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        bb.putInt(index, value);
        return Integer.BYTES;
    }

    /**
     * Writes a Int64 at the specified index
     *
     * @param order the byte order to use for writing
     * @param data the buffer to write
     * @param index the index to write to
     * @param value the value to write
     *
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeInt64(@NonNull final ByteOrder order, @NonNull final byte[] data, final int index, final long value)
    {
        ByteBuffer bb = ByteBuffer.wrap(data).order(order);
        bb.putLong(index, value);
        return Long.BYTES;
    }

    /**
     *
     * Writes a string to a buffer
     *
     * @param data the buffer to write to
     * @param index the index to write to
     * @param value the value to write
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeData(@NonNull final byte[] data, final int index, @NonNull final byte[] value)
    {
        System.arraycopy(value, 0, data, index, value.length);
        return value.length;
    }

    /**
     *
     * Writes a string to a buffer
     *
     * @param data the buffer to write to
     * @param index the index to write to
     * @param value the value to write
     * @param charset the character encoding to use
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeString(@NonNull final byte[] data, final int index, @NonNull final String value, @NonNull final Charset charset)
    {
        byte[] buffer = value.getBytes(charset);
        return writeData(data, index, buffer);
    }

    /**
     *
     * Writes a UTF8 string to a buffer
     *
     * @param data the buffer to write to
     * @param index the index to write to
     * @param value the value to write
     * @return the number of bytes written
     *
     * @throws IndexOutOfBoundsException if the index is out of bounds
     * @throws NullPointerException if data is null
     *
     */
    public static int writeUtf8(@NonNull final byte[] data, final int index, @NonNull final String value)
    {
        return writeString(data, index, value, Charsets.UTF_8);
    }
}
