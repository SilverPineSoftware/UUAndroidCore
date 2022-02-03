package com.silverpine.uu.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import com.silverpine.uu.logging.UULog;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
/**
 * Helper class for requesting runtime permissions in Android 6.0 and up
 *
 * Example usage:
 *
 * UUPermissions.requestPermissions(this, "android.permission.ACCESS_FINE_LOCATION", 1234, new UUPermissions.UUPermissionDelegate()
 {
 `@Override`
 public void onPermissionRequestComplete(String permission, boolean granted)
 {
 if (granted)
 {
 // Do something with the permission
 }
 else
 {
 // Handle the error
 }
 }
 });


 Step 2:  Handle onRequestPermissionsResult in your activity and pass the result to the permissions manager

 `@Override`
 public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
 {
 boolean handled = UUPermissions.handleRequestPermissionsResult(this, requestCode, permissions, grantResults);
 if (!handled)
 {
 // This permission request didn't come from UUPermissions
 }
 }

 *
 */
public class UUPermissions
{
    private static final HashMap<Integer, UUMultiplePermissionDelegate> callbacks = new HashMap<>();

    public interface UUSinglePermissionDelegate
    {
        void onPermissionRequestComplete(String permission, boolean granted);
    }

    public interface UUMultiplePermissionDelegate
    {
        void onPermissionRequestComplete(HashMap<String, Boolean> results);
    }

    public static boolean hasPermission(final Context context, final String permission)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        return (permissionCheck == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean hasAllPermissions(final Context context, final String[] permissions)
    {
        for (String permission: permissions)
        {
            if (!hasPermission(context, permission))
            {
                return false;
            }
        }

        return true;
    }

    private static boolean hasEverRequestedPermission(final Context context, final String permission)
    {
        SharedPreferences prefs = context.getSharedPreferences(UUPermissions.class.getName(), Activity.MODE_PRIVATE);
        return prefs.getBoolean("HAS_REQUESTED_" + permission, false);
    }

    private static void setHasRequestedPermission(final Context context, final String permission)
    {
        SharedPreferences prefs = context.getSharedPreferences(UUPermissions.class.getName(), Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean("HAS_REQUESTED_" + permission, true);
        prefsEditor.apply();
    }

    public static boolean canRequestPermission(@NonNull final Activity activity, @NonNull final String permission)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                return !hasEverRequestedPermission(activity, permission) || activity.shouldShowRequestPermissionRationale(permission);
            }
        }
        catch (Exception ex)
        {
            UULog.debug(UUPermissions.class, "canRequestPermission", ex);
        }

        return true;
    }

    public static boolean canRequestAllPermissions(@NonNull final Activity activity, final String[] permissions)
    {
        boolean canRequestAll = true;
        for (String p: permissions)
        {
            if (!canRequestPermission(activity, p))
            {
                canRequestAll = false;
                break;
            }
        }

        return canRequestAll;
    }

    public static void requestPermissions(final Activity activity, final String permission, final int requestId, final UUSinglePermissionDelegate delegate)
    {
        requestMultiplePermissions(activity, new String[]{permission}, requestId, results ->
        {
            boolean granted = false;
            if (results != null)
            {
                Boolean result = results.get(permission);
                if (result != null)
                {
                    granted = result;
                }
            }

            safeNotifyDelegate(delegate, permission, granted);
        });
    }

    public static void requestMultiplePermissions(final Activity activity, final String[] permissions, final int requestId, final UUMultiplePermissionDelegate delegate)
    {
        if (hasAllPermissions(activity, permissions))
        {
            HashMap<String,Boolean> results = new HashMap<>();
            for (String p: permissions)
            {
                results.put(p, Boolean.TRUE);
            }

            safeNotifyDelegate(delegate, results);
            removeDelegate(requestId);
        }
        else
        {
            // Wait for the results
            saveDelegate(delegate, requestId);
            ActivityCompat.requestPermissions(activity, permissions, requestId);
        }

    }

    public static boolean handleRequestPermissionsResult(final Activity activity, int requestCode, String permissions[], int[] grantResults)
    {
        try
        {
            UUMultiplePermissionDelegate delegate = null;

            if (callbacks.containsKey(requestCode))
            {
                delegate = callbacks.get(requestCode);

                if (permissions.length == grantResults.length)
                {
                    HashMap<String, Boolean> results = new HashMap<>();

                    for (int i = 0; i < permissions.length; i++)
                    {
                        String permission = permissions[i];
                        setHasRequestedPermission(activity, permission);
                        int result = grantResults[i];
                        results.put(permission, (result == PackageManager.PERMISSION_GRANTED));
                    }

                    safeNotifyDelegate(delegate, results);
                }

                removeDelegate(requestCode);
                return true;
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUPermissions.class, "handleRequestPermissionsResult", ex);
        }

        return false;
    }

    private static synchronized void saveDelegate(final UUMultiplePermissionDelegate delegate, final Integer requestId)
    {
        try
        {
            if (delegate != null)
            {
                callbacks.put(requestId, delegate);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUPermissions.class, "saveDelegate", ex);
        }
    }

    private static synchronized void removeDelegate(final Integer requestId)
    {
        try
        {
            if (callbacks.containsKey(requestId))
            {
                callbacks.remove(requestId);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUPermissions.class, "safeNotifyDelegate", ex);
        }
    }

    private static void safeNotifyDelegate(final UUSinglePermissionDelegate delegate, final String permission, final boolean granted)
    {
        try
        {
            if (delegate != null)
            {
                delegate.onPermissionRequestComplete(permission, granted);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUPermissions.class, "safeNotifyDelegate", ex);
        }
    }

    private static void safeNotifyDelegate(final UUMultiplePermissionDelegate delegate, final HashMap<String, Boolean> results)
    {
        try
        {
            if (delegate != null)
            {
                delegate.onPermissionRequestComplete(results);
            }
        }
        catch (Exception ex)
        {
            UULog.error(UUPermissions.class, "safeNotifyDelegate", ex);
        }
    }
}