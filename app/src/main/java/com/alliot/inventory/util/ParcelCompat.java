package com.alliot.inventory.util;

import android.content.Intent;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public final class ParcelCompat {

    private ParcelCompat() {
    }

    @SuppressWarnings({"deprecation"})
    public static <T extends Parcelable> T readParcelable(Parcel in, Class<T> clazz) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return in.readParcelable(clazz.getClassLoader(), clazz);
        } else {
            return in.readParcelable(clazz.getClassLoader());
        }
    }

    @SuppressWarnings("deprecation")
    public static <T extends Parcelable> T getParcelableExtra(Intent intent, String key, Class<T> clazz) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return intent.getParcelableExtra(key, clazz);
        } else {
            return intent.getParcelableExtra(key);
        }
    }
}

