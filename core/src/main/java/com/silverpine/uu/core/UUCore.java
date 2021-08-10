package com.silverpine.uu.core;

import androidx.annotation.NonNull;

public final class UUCore
{
    /**
     * Gets the current framework version
     *
     * @since 1.0.0
     *
     */
    @NonNull
    public static final String BUILD_VERSION = UUNull.getString(BuildConfig.BUILD_VERSION);

    /**
     * Returns the build branch
     *
     * @since 1.0.0
     */
    @NonNull
    public static final String BUILD_BRANCH = UUNull.getString(BuildConfig.BUILD_BRANCH);

    /**
     * Returns the full hash of the Git latest git commit
     *
     * @since 1.0.0
     */
    @NonNull
    public static final String BUILD_COMMIT_HASH = UUNull.getString(BuildConfig.BUILD_COMMIT_HASH);

    /**
     * Returns the date the framework was built.
     *
     * @since 1.0.0
     */
    @NonNull
    public static final String BUILD_DATE = UUNull.getString(BuildConfig.BUILD_DATE);
}
