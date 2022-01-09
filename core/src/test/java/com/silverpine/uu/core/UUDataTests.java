package com.silverpine.uu.core;

import android.util.Pair;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteOrder;
import java.util.ArrayList;

public class UUDataTests
{
    @Test
    public void testSubData()
    {
        byte[] input = UUString.hexToByte("AABBCCDDEEFF00112233445566778899");
        byte[] sub = UUData.subData(input, 0, 3);
        Assert.assertNotNull(sub);
        Assert.assertArrayEquals(sub, new byte[] { (byte)0xAA, (byte)0xBB, (byte)0xCC });

        sub = UUData.subData(input, 0, 500);
        Assert.assertNotNull(sub);
        Assert.assertArrayEquals(sub, input);

        sub = UUData.subData(input, 13 , 7);
        Assert.assertNotNull(sub);
        Assert.assertArrayEquals(sub, new byte[] { (byte)0x77, (byte)0x88, (byte)0x99 });
    }

    @Test
    public void testSlice()
    {
        byte[] input = UUString.hexToByte("AABBCCDDEEFF00112233445566778899");
        ArrayList<byte[]> slices = UUData.slice(input, 3);
        Assert.assertEquals(slices.size(), 6);
        Assert.assertArrayEquals(slices.get(0), new byte[] { (byte)0xAA, (byte)0xBB, (byte)0xCC });
        Assert.assertArrayEquals(slices.get(1), new byte[] { (byte)0xDD, (byte)0xEE, (byte)0xFF });
        Assert.assertArrayEquals(slices.get(2), new byte[] { (byte)0x00, (byte)0x11, (byte)0x22  });
        Assert.assertArrayEquals(slices.get(3), new byte[] { (byte)0x33, (byte)0x44, (byte)0x55  });
        Assert.assertArrayEquals(slices.get(4), new byte[] { (byte)0x66, (byte)0x77, (byte)0x88  });
        Assert.assertArrayEquals(slices.get(5), new byte[] { (byte)0x99 });
    }

    class InputPair<T>
    {
        ByteOrder order;
        String source;
        T expected;
        int index;

        InputPair(ByteOrder bo, String src, T exp, int idx)
        {
            order = bo;
            source = src;
            expected = exp;
            index = idx;
        }

        InputPair(ByteOrder bo, String src, T exp)
        {
            this(bo, src, exp, 0);
        }

        InputPair(String src, T exp, int idx)
        {
            this(ByteOrder.LITTLE_ENDIAN, src, exp, idx);
        }

        InputPair(String src, T exp)
        {
            this(src, exp, 0);
        }

        byte[] bytes()
        {
            return UUString.hexToByte(source);
        }
    }

    @Test
    public void readInt8()
    {
        ArrayList<InputPair<Byte>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>("FF", (byte)0xFF));
        testInput.add(new InputPair<>("00", (byte)0x00));
        testInput.add(new InputPair<>("11", (byte)0x11));
        testInput.add(new InputPair<>("BB", (byte)0xBB));
        testInput.add(new InputPair<>("80", Byte.MIN_VALUE));
        testInput.add(new InputPair<>("7F", Byte.MAX_VALUE));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (byte)0xCC, 2));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (byte)0xEE, 4));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (byte)0xFF, 5));

        // Test some positive cases
        for (InputPair<Byte> ti : testInput)
        {
            byte actual = UUData.readInt8(ti.bytes(), ti.index);
            byte expected = ti.expected;
            Assert.assertEquals(expected, actual);
        }

        // Try some negative test cases
        try
        {
            // Null Buffer
            try
            {
                UUData.readInt8(null, 0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.readInt8(new byte[] { }, -1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.readInt8(new byte[] { }, 1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void readInt16()
    {
        ArrayList<InputPair<Short>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFF", (short)0xFFFF));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000", (short)0x0000));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1122", (short)0x2211));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"BBDD", (short)0xDDBB));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0080", Short.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FF7F", Short.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,"AABBCCDDEEFF", (short)0xCCDD, 2));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"AABBCCDDEEFF", (short)0xEEDD, 3));

        for (InputPair<Short> ti: testInput)
        {
            short actual = UUData.readInt16(ti.order, ti.bytes(), ti.index);
            short expected = ti.expected;
            Assert.assertEquals(expected, actual);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.readInt16(ByteOrder.nativeOrder(), null, 0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.readInt16(ByteOrder.nativeOrder(), new byte[] { }, -1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.readInt16(ByteOrder.nativeOrder(), new byte[] { }, 1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void readInt32()
    {
        ArrayList<InputPair<Integer>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFF", (int)0xFFFFFFFF));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"00000000", (int)0));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"11223344", (int)0x44332211));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"AABBCCDD", (int)0xDDCCBBAA));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"00000080", Integer.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFF7F", Integer.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1234AABBCCDD9876", (int)0xDDCCBBAA, 2));

        for (InputPair<Integer> ti: testInput)
        {
            int actual = UUData.readInt32(ti.order, ti.bytes(), ti.index);
            int expected = ti.expected;
            Assert.assertEquals(expected, actual);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.readInt32(ByteOrder.nativeOrder(), null, 0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.readInt32(ByteOrder.nativeOrder(), new byte[] { }, -1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.readInt32(ByteOrder.nativeOrder(), new byte[] { }, 2);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void readInt64()
    {
        ArrayList<InputPair<Long>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "FFFFFFFFFFFFFFFF",0xFFFFFFFFFFFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFFFFFFFFFF",0xFFFFFFFFFFFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "0000000000000000",0x0000000000000000L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000000000000000",0x0000000000000000L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "1234567890ABCDEF",0x1234567890ABCDEFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1234567890ABCDEF",0xEFCDAB9078563412L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "1122334455667788",0x1122334455667788L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1122334455667788",0x8877665544332211L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "8000000000000000", Long.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000000000000080", Long.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "7FFFFFFFFFFFFFFF", Long.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFFFFFFFF7F", Long.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"ABCD1122334455667788FFDD",0x8877665544332211L, 2));

        for (InputPair<Long> ti: testInput)
        {
            long actual = UUData.readInt64(ti.order, ti.bytes(), ti.index);
            long expected = ti.expected;
            Assert.assertEquals(expected, actual);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.readInt64(ByteOrder.nativeOrder(), null, 0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.readInt64(ByteOrder.nativeOrder(), new byte[] { }, -1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.readInt64(ByteOrder.nativeOrder(), new byte[] { }, 2);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void readUInt8()
    {
        ArrayList<InputPair<Integer>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>("FF", (int)0xFF));
        testInput.add(new InputPair<>("00", (int)0x00));
        testInput.add(new InputPair<>("11", (int)0x11));
        testInput.add(new InputPair<>("BB", (int)0xBB));
        testInput.add(new InputPair<>("80", (int)0x80));
        testInput.add(new InputPair<>("7F", (int)0x7F));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (int)0xCC, 2));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (int)0xEE, 4));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (int)0xFF, 5));

        // Test some positive cases
        for (InputPair<Integer> ti : testInput)
        {
            int actual = UUData.readUInt8(ti.bytes(), ti.index);
            int expected = ti.expected;
            Assert.assertEquals(expected, actual);
        }

        // Try some negative test cases
        try
        {
            // Null Buffer
            try
            {
                UUData.readUInt8(null, 0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.readUInt8(new byte[] { }, -1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.readUInt8(new byte[] { }, 1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void readUInt16()
    {
        ArrayList<InputPair<Integer>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFF", (int)0xFFFF));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000", (int)0x0000));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1122", (int)0x2211));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"BBDD", (int)0xDDBB));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0080", (int)0x8000));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FF7F", (int)0x7FFF));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,"AABBCCDDEEFF", (int)0xCCDD, 2));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"AABBCCDDEEFF", (int)0xEEDD, 3));

        for (InputPair<Integer> ti: testInput)
        {
            int actual = UUData.readUInt16(ti.order, ti.bytes(), ti.index);
            int expected = ti.expected;
            Assert.assertEquals(expected, actual);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.readUInt16(ByteOrder.nativeOrder(), null, 0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.readUInt16(ByteOrder.nativeOrder(), new byte[] { }, -1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.readUInt16(ByteOrder.nativeOrder(), new byte[] { }, 1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void readUInt32()
    {
        ArrayList<InputPair<Long>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFF", 0xFFFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"00000000", 0L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"11223344", 0x44332211L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"AABBCCDD", 0xDDCCBBAAL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"00000080", 0x80000000L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFF7F", 0x7FFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1234AABBCCDD9876", 0xDDCCBBAAL, 2));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,"1234AABBCCDD9876", 0xAABBCCDDL, 2));

        for (InputPair<Long> ti: testInput)
        {
            long actual = UUData.readUInt32(ti.order, ti.bytes(), ti.index);
            long expected = ti.expected;
            Assert.assertEquals(expected, actual);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.readUInt32(ByteOrder.nativeOrder(), null, 0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.readUInt32(ByteOrder.nativeOrder(), new byte[] { }, -1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.readUInt32(ByteOrder.nativeOrder(), new byte[] { }, 2);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void readUInt64()
    {
        ArrayList<InputPair<Long>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "FFFFFFFFFFFFFFFF",0xFFFFFFFFFFFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFFFFFFFFFF",0xFFFFFFFFFFFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "0000000000000000",0x0000000000000000L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000000000000000",0x0000000000000000L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "1234567890ABCDEF",0x1234567890ABCDEFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1234567890ABCDEF",0xEFCDAB9078563412L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "1122334455667788",0x1122334455667788L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1122334455667788",0x8877665544332211L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "8000000000000000", Long.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000000000000080", Long.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "7FFFFFFFFFFFFFFF", Long.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFFFFFFFF7F", Long.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"ABCD1122334455667788FFDD",0x8877665544332211L, 2));

        for (InputPair<Long> ti: testInput)
        {
            long actual = UUData.readUInt64(ti.order, ti.bytes(), ti.index);
            long expected = ti.expected;
            Assert.assertEquals(expected, actual);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.readUInt64(ByteOrder.nativeOrder(), null, 0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.readUInt64(ByteOrder.nativeOrder(), new byte[] { }, -1);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.readUInt64(ByteOrder.nativeOrder(), new byte[] { }, 2);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void writeInt8()
    {
        ArrayList<InputPair<Byte>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>("FF", (byte)0xFF));
        testInput.add(new InputPair<>("00", (byte)0x00));
        testInput.add(new InputPair<>("11", (byte)0x11));
        testInput.add(new InputPair<>("BB", (byte)0xBB));
        testInput.add(new InputPair<>("80", Byte.MIN_VALUE));
        testInput.add(new InputPair<>("7F", Byte.MAX_VALUE));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (byte)0xCC, 2));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (byte)0xEE, 4));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (byte)0xFF, 5));

        // Test some positive cases
        for (InputPair<Byte> ti : testInput)
        {
            byte[] original = UUData.copy(ti.bytes());
            byte[] working = UUData.copy(ti.bytes());

            byte actual = UUData.readInt8(working, ti.index);
            byte expected = ti.expected;
            Assert.assertEquals(expected, actual);

            byte tmp = 57;
            int written = UUData.writeInt8(working, ti.index, tmp);
            Assert.assertEquals(Byte.BYTES, written);

            actual = UUData.readInt8(working, ti.index);
            expected = tmp;
            Assert.assertEquals(expected, actual);

            written = UUData.writeInt8(working, ti.index, ti.expected);
            Assert.assertEquals(Byte.BYTES, written);
            Assert.assertArrayEquals(working, original);
        }

        // Try some negative test cases
        try
        {
            // Null Buffer
            try
            {
                UUData.writeInt8(null, 0, (byte)0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.writeInt8(new byte[] { }, -1, (byte)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.writeInt8(new byte[] { }, 1, (byte)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void writeInt16()
    {
        ArrayList<InputPair<Short>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFF", (short)0xFFFF));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000", (short)0x0000));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1122", (short)0x2211));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"BBDD", (short)0xDDBB));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0080", Short.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FF7F", Short.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,"AABBCCDDEEFF", (short)0xCCDD, 2));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"AABBCCDDEEFF", (short)0xEEDD, 3));

        for (InputPair<Short> ti: testInput)
        {
            byte[] original = UUData.copy(ti.bytes());
            byte[] working = UUData.copy(ti.bytes());

            short actual = UUData.readInt16(ti.order, working, ti.index);
            short expected = ti.expected;
            Assert.assertEquals(expected, actual);

            short tmp = 57;
            int written = UUData.writeInt16(ti.order, working, ti.index, tmp);
            Assert.assertEquals(Short.BYTES, written);

            actual = UUData.readInt16(ti.order, working, ti.index);
            expected = tmp;
            Assert.assertEquals(expected, actual);

            written = UUData.writeInt16(ti.order, working, ti.index, ti.expected);
            Assert.assertEquals(Short.BYTES, written);
            Assert.assertArrayEquals(working, original);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.writeInt16(ByteOrder.nativeOrder(), null, 0, (short)0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.writeInt16(ByteOrder.nativeOrder(), new byte[] { }, -1, (short)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.writeInt16(ByteOrder.nativeOrder(), new byte[] { }, 1, (short)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void writeInt32()
    {
        ArrayList<InputPair<Integer>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFF", (int)0xFFFFFFFF));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"00000000", (int)0));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"11223344", (int)0x44332211));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"AABBCCDD", (int)0xDDCCBBAA));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"00000080", Integer.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFF7F", Integer.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1234AABBCCDD9876", (int)0xDDCCBBAA, 2));

        for (InputPair<Integer> ti: testInput)
        {
            byte[] original = UUData.copy(ti.bytes());
            byte[] working = UUData.copy(ti.bytes());

            int actual = UUData.readInt32(ti.order, working, ti.index);
            int expected = ti.expected;
            Assert.assertEquals(expected, actual);

            int tmp = 57;
            int written = UUData.writeInt32(ti.order, working, ti.index, tmp);
            Assert.assertEquals(Integer.BYTES, written);

            actual = UUData.readInt32(ti.order, working, ti.index);
            expected = tmp;
            Assert.assertEquals(expected, actual);

            written = UUData.writeInt32(ti.order, working, ti.index, ti.expected);
            Assert.assertEquals(Integer.BYTES, written);
            Assert.assertArrayEquals(working, original);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.writeInt32(ByteOrder.nativeOrder(), null, 0, (int)0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.writeInt32(ByteOrder.nativeOrder(), new byte[] { }, -1, (int)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.writeInt32(ByteOrder.nativeOrder(), new byte[] { }, 2, (int)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void writeInt64()
    {
        ArrayList<InputPair<Long>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "FFFFFFFFFFFFFFFF",0xFFFFFFFFFFFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFFFFFFFFFF",0xFFFFFFFFFFFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "0000000000000000",0x0000000000000000L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000000000000000",0x0000000000000000L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "1234567890ABCDEF",0x1234567890ABCDEFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1234567890ABCDEF",0xEFCDAB9078563412L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "1122334455667788",0x1122334455667788L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1122334455667788",0x8877665544332211L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "8000000000000000", Long.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000000000000080", Long.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "7FFFFFFFFFFFFFFF", Long.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFFFFFFFF7F", Long.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"ABCD1122334455667788FFDD",0x8877665544332211L, 2));

        for (InputPair<Long> ti: testInput)
        {
            byte[] original = UUData.copy(ti.bytes());
            byte[] working = UUData.copy(ti.bytes());

            long actual = UUData.readInt64(ti.order, working, ti.index);
            long expected = ti.expected;
            Assert.assertEquals(expected, actual);

            long tmp = 57;
            int written = UUData.writeInt64(ti.order, working, ti.index, tmp);
            Assert.assertEquals(Long.BYTES, written);

            actual = UUData.readInt64(ti.order, working, ti.index);
            expected = tmp;
            Assert.assertEquals(expected, actual);

            written = UUData.writeInt64(ti.order, working, ti.index, ti.expected);
            Assert.assertEquals(Long.BYTES, written);
            Assert.assertArrayEquals(working, original);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.writeInt64(ByteOrder.nativeOrder(), null, 0, 0L);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.writeInt64(ByteOrder.nativeOrder(), new byte[] { }, -1, 0L);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.writeInt64(ByteOrder.nativeOrder(), new byte[] { }, 2, 0L);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void writeUInt8()
    {
        ArrayList<InputPair<Integer>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>("FF", (int)0xFF));
        testInput.add(new InputPair<>("00", (int)0x00));
        testInput.add(new InputPair<>("11", (int)0x11));
        testInput.add(new InputPair<>("BB", (int)0xBB));
        testInput.add(new InputPair<>("80", (int)0x80));
        testInput.add(new InputPair<>("7F", (int)0x7F));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (int)0xCC, 2));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (int)0xEE, 4));
        testInput.add(new InputPair<>("AABBCCDDEEFF", (int)0xFF, 5));

        // Test some positive cases
        for (InputPair<Integer> ti : testInput)
        {
            byte[] original = UUData.copy(ti.bytes());
            byte[] working = UUData.copy(ti.bytes());

            int actual = UUData.readUInt8(working, ti.index);
            int expected = ti.expected;
            Assert.assertEquals(expected, actual);

            int tmp = 57;
            int written = UUData.writeUInt8(working, ti.index, tmp);
            Assert.assertEquals(Byte.BYTES, written);

            actual = UUData.readUInt8(working, ti.index);
            expected = tmp;
            Assert.assertEquals(expected, actual);

            written = UUData.writeUInt8(working, ti.index, ti.expected);
            Assert.assertEquals(Byte.BYTES, written);
            Assert.assertArrayEquals(working, original);
        }

        // Try some negative test cases
        try
        {
            // Null Buffer
            try
            {
                UUData.writeUInt8(null, 0, (int)0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.writeUInt8(new byte[] { }, -1, (int)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.writeUInt8(new byte[] { }, 1, (int)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void writeUInt16()
    {
        ArrayList<InputPair<Integer>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFF", (int)0xFFFF));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000", (int)0x0000));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1122", (int)0x2211));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"BBDD", (int)0xDDBB));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0080", (int)0x8000));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FF7F", (int)0x7FFF));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,"AABBCCDDEEFF", (int)0xCCDD, 2));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"AABBCCDDEEFF", (int)0xEEDD, 3));

        for (InputPair<Integer> ti: testInput)
        {
            byte[] original = UUData.copy(ti.bytes());
            byte[] working = UUData.copy(ti.bytes());

            int actual = UUData.readUInt16(ti.order, working, ti.index);
            int expected = ti.expected;
            Assert.assertEquals(expected, actual);

            int tmp = 57;
            int written = UUData.writeUInt16(ti.order, working, ti.index, tmp);
            Assert.assertEquals(Short.BYTES, written);

            actual = UUData.readUInt16(ti.order, working, ti.index);
            expected = tmp;
            Assert.assertEquals(expected, actual);

            written = UUData.writeUInt16(ti.order, working, ti.index, ti.expected);
            Assert.assertEquals(Short.BYTES, written);
            Assert.assertArrayEquals(working, original);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.writeUInt16(ByteOrder.nativeOrder(), null, 0, (int)0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.writeUInt16(ByteOrder.nativeOrder(), new byte[] { }, -1, (int)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.writeUInt16(ByteOrder.nativeOrder(), new byte[] { }, 1, (int)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void writeUInt32()
    {
        ArrayList<InputPair<Long>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFF", 0xFFFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"00000000", 0L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"11223344", 0x44332211L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"AABBCCDD", 0xDDCCBBAAL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"00000080", 0x80000000L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFF7F", 0x7FFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1234AABBCCDD9876", 0xDDCCBBAAL, 2));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,"1234AABBCCDD9876", 0xAABBCCDDL, 2));

        for (InputPair<Long> ti: testInput)
        {
            byte[] original = UUData.copy(ti.bytes());
            byte[] working = UUData.copy(ti.bytes());

            long actual = UUData.readUInt32(ti.order, working, ti.index);
            long expected = ti.expected;
            Assert.assertEquals(expected, actual);

            long tmp = 57;
            int written = UUData.writeUInt32(ti.order, working, ti.index, tmp);
            Assert.assertEquals(Integer.BYTES, written);

            actual = UUData.readUInt32(ti.order, working, ti.index);
            expected = tmp;
            Assert.assertEquals(expected, actual);

            written = UUData.writeUInt32(ti.order, working, ti.index, ti.expected);
            Assert.assertEquals(Integer.BYTES, written);
            Assert.assertArrayEquals(working, original);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.writeUInt32(ByteOrder.nativeOrder(), null, 0, (int)0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.writeUInt32(ByteOrder.nativeOrder(), new byte[] { }, -1, (int)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.writeUInt32(ByteOrder.nativeOrder(), new byte[] { }, 2, (int)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    @Test
    public void writeUInt64()
    {
        ArrayList<InputPair<Long>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "FFFFFFFFFFFFFFFF",0xFFFFFFFFFFFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFFFFFFFFFF",0xFFFFFFFFFFFFFFFFL));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "0000000000000000",0x0000000000000000L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000000000000000",0x0000000000000000L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "1234567890ABCDEF",0x1234567890ABCDEFL));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1234567890ABCDEF",0xEFCDAB9078563412L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "1122334455667788",0x1122334455667788L));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"1122334455667788",0x8877665544332211L));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "8000000000000000", Long.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"0000000000000080", Long.MIN_VALUE));
        testInput.add(new InputPair<>(ByteOrder.BIG_ENDIAN,   "7FFFFFFFFFFFFFFF", Long.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"FFFFFFFFFFFFFF7F", Long.MAX_VALUE));
        testInput.add(new InputPair<>(ByteOrder.LITTLE_ENDIAN,"ABCD1122334455667788FFDD",0x8877665544332211L, 2));

        for (InputPair<Long> ti: testInput)
        {
            byte[] original = UUData.copy(ti.bytes());
            byte[] working = UUData.copy(ti.bytes());

            long actual = UUData.readUInt64(ti.order, working, ti.index);
            long expected = ti.expected;
            Assert.assertEquals(expected, actual);

            long tmp = 57;
            int written = UUData.writeUInt64(ti.order, working, ti.index, tmp);
            Assert.assertEquals(Long.BYTES, written);

            actual = UUData.readUInt64(ti.order, working, ti.index);
            expected = tmp;
            Assert.assertEquals(expected, actual);

            written = UUData.writeUInt64(ti.order, working, ti.index, ti.expected);
            Assert.assertEquals(Long.BYTES, written);
            Assert.assertArrayEquals(working, original);
        }

        try
        {
            // Null Buffer
            try
            {
                UUData.writeUInt64(ByteOrder.nativeOrder(), null, 0, (int)0);
                Assert.fail("Should raise a NullPointerException");
            }
            catch (NullPointerException ignored)
            {
            }

            // Index negative
            try
            {
                UUData.writeUInt64(ByteOrder.nativeOrder(), new byte[] { }, -1, (int)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }

            // Index greater than buffer.length
            try
            {
                UUData.writeUInt64(ByteOrder.nativeOrder(), new byte[] { }, 2, (int)0);
                Assert.fail("Should raise a ArrayIndexOutOfBoundsException");
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
        catch (Exception error)
        {
            Assert.fail("An exception escaped the negative test area: " + error);
        }
    }

    // MARK: Nibble Tests

    @Test
    public void test_uuHighNibble()
    {
        ArrayList<InputPair<Byte>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>("00", (byte)0));
        testInput.add(new InputPair<>("12", (byte)1));
        testInput.add(new InputPair<>("01", (byte)0));
        testInput.add(new InputPair<>("99", (byte)9));
        testInput.add(new InputPair<>("CB", (byte)0xC));
        testInput.add(new InputPair<>("57abcd1234", (byte)5));
        testInput.add(new InputPair<>("FF", (byte)0xF));

        for (InputPair<Byte> td: testInput)
        {
            byte actual = UUData.highNibble(td.bytes(), td.index);
            byte expected = td.expected;
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void test_uuLowNibble()
    {
        ArrayList<InputPair<Byte>> testInput = new ArrayList<>();
        testInput.add(new InputPair<>("00", (byte)0));
        testInput.add(new InputPair<>("12", (byte)2));
        testInput.add(new InputPair<>("01", (byte)1));
        testInput.add(new InputPair<>("99", (byte)9));
        testInput.add(new InputPair<>("CB", (byte)0xB));
        testInput.add(new InputPair<>("57abcd1234", (byte)7));
        testInput.add(new InputPair<>("FF", (byte)0xF));

        for (InputPair<Byte> td: testInput)
        {
            byte actual = UUData.lowNibble(td.bytes(), td.index);
            byte expected = td.expected;
            Assert.assertEquals(expected, actual);
        }
    }
}
