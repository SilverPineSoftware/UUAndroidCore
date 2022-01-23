package com.silverpine.uu.core;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Android equivalent of NSError.  Simply a container for an error code, domain and a dictionary
 * of user information.
 */
public class UUError implements Parcelable
{
    public static final String USER_INFO_KEY_LOCALIZED_DESCRIPTION = "UULocalizedErrorDescription";
    public static final String USER_INFO_KEY_LOCALIZED_RECOVERY_SUGGESTION = "UULocalizedRecoverySuggestion";

    int code = 0;
    String domain = "";
    Exception exception = null;
    JSONObject userInfo = null;
    UUError underlyingError = null;

    public UUError()
    {
    }

    public UUError(@NonNull final String domain, final int code)
    {
        this.domain = domain;
        this.code = code;
    }

    public UUError(@NonNull final String domain, final int code, @Nullable final Exception exception)
    {
        this.domain = domain;
        this.code = code;
        this.exception = exception;
    }

    public UUError(@NonNull final String domain, final int code, @Nullable final UUError underlyingError)
    {
        this.domain = domain;
        this.code = code;
        this.underlyingError = underlyingError;
    }

    public int getCode()
    {
        return code;
    }

    @NonNull
    public String getDomain()
    {
        return UUString.safeString(domain);
    }

    @Nullable
    public Exception getException()
    {
        return exception;
    }

    @Nullable
    public JSONObject getUserInfo()
    {
        return userInfo;
    }

    @Nullable
    public UUError getUnderlyingError()
    {
        return underlyingError;
    }

    public void addUserInfo(@NonNull Object key, @NonNull Object value)
    {
        if (userInfo == null)
        {
            userInfo = new JSONObject();
        }

        UUJson.safePut(userInfo, key, value);
    }

    @Nullable
    public Object getUserInfoField(@NonNull final Object key)
    {
        return UUJson.safeGet(userInfo, key);
    }

    @Nullable
    public String getLocalizedErrorDescription()
    {
        return UUJson.safeGetString(userInfo, USER_INFO_KEY_LOCALIZED_DESCRIPTION);
    }

    @Nullable
    public String getLocalizedRecoverySuggestion()
    {
        return UUJson.safeGetString(userInfo, USER_INFO_KEY_LOCALIZED_RECOVERY_SUGGESTION);
    }

    public void setErrorDescription(@Nullable final String description)
    {
        addUserInfo(USER_INFO_KEY_LOCALIZED_DESCRIPTION, description);
    }

    public void setRecoverySuggestion(@Nullable final String recoverySuggestion)
    {
        addUserInfo(USER_INFO_KEY_LOCALIZED_RECOVERY_SUGGESTION, recoverySuggestion);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Object Overrides
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param o an object to compare against
     * @return true if the object is a {@link com.silverpine.uu.core.UUError} and all fields are equal
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (!(o instanceof UUError))
        {
            return false;
        }

        UUError error = (UUError) o;
        if (code != error.code)
        {
            return false;
        }

        if (!(Objects.equals(domain, error.domain)))
        {
            return false;
        }

        byte[] exceptionBytes = UUData.serializeObject(exception);
        byte[] otherExceptionBytes = UUData.serializeObject(error.exception);
        if (!(Arrays.equals(exceptionBytes, otherExceptionBytes)))
        {
            return false;
        }

        String userInfoJson = UUJson.toJsonString(userInfo);
        String otherUserInfoJson = UUJson.toJsonString(error.userInfo);

        if (!(Objects.equals(userInfoJson, otherUserInfoJson)))
        {
            return false;
        }

        if (!(Objects.equals(underlyingError, error.underlyingError)))
        {
            return false;
        }

        return true;
    }

    /**
     *
     * @return hashcode of this object
     */
    @Override
    public int hashCode()
    {
        String exceptionBytesHex = UUString.byteToHex(UUData.serializeObject(exception));
        String userInfoJson = UUJson.toJsonString(userInfo);
        return Objects.hash(code, domain, exceptionBytesHex, userInfoJson, underlyingError);
    }

    /**
     * To string override
     *
     * @return string
     */
    @NonNull
    @Override
    public String toString()
    {
        try
        {
            return String.format(Locale.US, "domain: %s, code: %s, userInfo: %s, ex: %s",
                UUString.safeToString(domain),
                UUString.safeToString(code),
                UUString.safeToString(userInfo),
                UUString.safeToString(exception));
        }
        catch (Exception ex)
        {
            return super.toString();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Parcelable Implementation
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(domain);
        dest.writeInt(code);
        dest.writeSerializable(exception);
        dest.writeString(UUJson.toJsonString(userInfo));
        dest.writeParcelable(underlyingError, flags);
    }

    public static final Parcelable.Creator<UUError> CREATOR = new Parcelable.Creator<UUError>()
    {
        public UUError createFromParcel(Parcel in)
        {
            return new UUError(in);
        }

        public UUError[] newArray(int size)
        {
            return new UUError[size];
        }
    };

    private UUError(final Parcel in)
    {
        domain = in.readString();
        code = in.readInt();
        exception = (Exception) in.readSerializable();
        userInfo = UUJson.toJsonObject(in.readString());
        underlyingError = in.readParcelable(getClass().getClassLoader());
    }
}