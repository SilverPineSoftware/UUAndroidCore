package com.spsw.uu.core;

import com.silverpine.uu.core.UUData;
import com.silverpine.uu.core.UUString;

import org.junit.Assert;
import org.junit.Test;

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
}
