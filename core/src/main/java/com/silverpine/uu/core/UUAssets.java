package com.silverpine.uu.core;

import android.content.Context;
import android.content.res.AssetManager;

import com.silverpine.uu.logging.UULog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UUAssets
{
    @Nullable
    public static byte[] readRawAsset(@NonNull final Context context, @NonNull final String assetName)
    {
        return readRawAsset(context, assetName, 10240);
    }

    @Nullable
    public static byte[] readRawAsset(@NonNull final Context context, @NonNull final String assetName, final int chunkSize)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = null;
        byte[] result;

        try
        {
            AssetManager assetManager = context.getAssets();
            is = assetManager.open(assetName);

            byte[] buffer = new byte[chunkSize];

            int bytesRead;

            do
            {
                bytesRead = is.read(buffer, 0, buffer.length);
                if (bytesRead > 0)
                {
                    bos.write(buffer, 0, bytesRead);
                }
            }
            while (bytesRead > 0);

            result = bos.toByteArray();
        }
        catch (Exception ex)
        {
            UULog.error(UUAssets.class, "readRawAsset", ex);
            result = null;
        }
        finally
        {
            UUCloseable.safeClose(is);
            UUCloseable.safeClose(bos);
        }

        return result;
    }
}
