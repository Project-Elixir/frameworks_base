/*
 * Copyright (C) 2022 The Pixel Experience Project
 *               2021-2022 crDroid Android Project
 *               2022 ReloadedOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 package com.android.internal.util;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PixelPropsUtils {

    private static final String TAG = "PixelPropsUtils";
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_PACKAGES = false;

    // Packages to Spoof as Pixel 7 Pro
    private static final String[] sPixel7ProPackages = {
        "com.google.android.apps.googleassistant",
        "com.google.android.apps.nbu.files",
        "com.google.android.apps.podcasts",
        "com.google.android.apps.privacy.wildlife",
        "com.google.android.apps.subscriptions.red",
        "com.google.android.apps.tachyon",
        "com.google.android.apps.wallpaper",
        "com.google.android.contacts",
        "com.google.android.deskclock",
        "com.google.android.inputmethod.latin",
        "com.amazon.avod.thirdpartyclient",
        "com.android.chrome",
        "com.android.vending",
        "com.breel.wallpapers20",
        "com.disney.disneyplus",
        "com.microsoft.android.smsorganizer",
        "com.netflix.mediaclient",
        "com.nhs.online.nhsonline",
        "com.nothing.smartcenter",
        "in.startv.hotstar"
};

    private static final Map<String, Object> sPixelProps = Map.of(
        "BRAND", "google",
        "MANUFACTURER", "Google",
        "DEVICE", "redfin",
        "PRODUCT", "redfin",
        "MODEL", "Pixel 5",
        "FINGERPRINT", "google/redfin/redfin:13/TQ2A.230305.008.C1/9619669:user/release-keys"
    );

    private static final Map<String, Object> sPixel7Props = Map.of(
        "BRAND", "google",
        "MANUFACTURER", "Google",
        "DEVICE", "cheetah",
        "PRODUCT", "cheetah",
        "MODEL", "Pixel 7 Pro",
        "FINGERPRINT", "google/cheetah/cheetah:13/TQ2A.230305.008.C1/9619669:user/release-keys"
    );

    private static final Map<String, Object> sPixelXLProps = Map.of(
        "BRAND", "google",
        "MANUFACTURER", "Google",
        "DEVICE", "marlin",
        "PRODUCT", "marlin",
        "MODEL", "Pixel XL",
        "FINGERPRINT", "google/marlin/marlin:10/QP1A.191005.007.A3/5972272:user/release-keys"
    );

    private static final Map<String, Object> sPixelXLSFProps = Map.of(
        "DEVICE_INITIAL_SDK_INT", Build.VERSION_CODES.N_MR1,
        "DEVICE", "marlin",
        "PRODUCT", "marlin",
        "MODEL", "Pixel XL",
        "FINGERPRINT", "google/marlin/marlin:7.1.2/NJH47F/4146041:user/release-keys"
    );

    private static final List<String> sExtraPackages = List.of(
        "com.android.chrome",
        "com.android.vending"
    );

    private static final List<String> sPackageWhitelist = List.of(
        "com.google.android.dialer",
        "com.google.android.euicc",
        "com.google.android.youtube",
        "com.google.android.apps.youtube.kids",
        "com.google.android.apps.youtube.music",
        "com.google.android.apps.recorder",
        "com.google.android.apps.wearables.maestro.companion",
        "com.google.android.settings.intelligence"
    );

    private static final List<String> sFeatureBlacklist = List.of(
        "PIXEL_2017_PRELOAD",
        "PIXEL_2018_PRELOAD",
        "PIXEL_2019_MIDYEAR_PRELOAD",
        "PIXEL_2019_PRELOAD",
        "PIXEL_2020_EXPERIENCE",
        "PIXEL_2020_MIDYEAR_EXPERIENCE",
        "PIXEL_2021_EXPERIENCE",
        "PIXEL_2021_MIDYEAR_EXPERIENCE"
    );

    private static final String PACKAGE_PREFIX_GOOGLE = "com.google.android.";
    private static final String PACKAGE_GPHOTOS = "com.google.android.apps.photos";
    private static final String PACKAGE_FINSKY = "com.android.vending";
    private static final String PACKAGE_GMS = "com.google.android.gms";
    private static final String PROCESS_GMS_UNSTABLE = PACKAGE_GMS + ".unstable";

    private static final String GMS_FINGERPRINT =
            SystemProperties.get("ro.build.gms_fingerprint");

    private static volatile boolean sIsGms = false;
    private static volatile boolean sIsFinsky = false;
    private static volatile boolean sIsPhotos = false;

    public static void setProps(Context context) {
        final String packageName = context.getPackageName();
        final String processName = Application.getProcessName();

        if (DEBUG_PACKAGES) {
            Log.d(TAG, "setProps packageName=" + packageName + " processName=" + processName);
        }

        if (TextUtils.isEmpty(packageName) || processName == null
                || sPackageWhitelist.contains(packageName)) {
            return;
        }

        sIsGms = packageName.equals(PACKAGE_GMS) && processName.equals(PROCESS_GMS_UNSTABLE);
        sIsFinsky = packageName.equals(PACKAGE_FINSKY);
        sIsPhotos = packageName.equals(PACKAGE_GPHOTOS);

        if (sIsGms) {
            dlog("Spoofing build for GMS");
            sPixelXLSFProps.forEach(PixelPropsUtils::setPropValue);
        } else if (sIsPhotos) {
            dlog("Spoofing Pixel XL for Google Photos");
            sPixelXLProps.forEach(PixelPropsUtils::setPropValue);
        } else if ((Arrays.asList(sPixel7ProPackages).contains(packageName))) {
            dlog("Spoofing Pixel 7 Pro");
            sPixel7Props.forEach(PixelPropsUtils::setPropValue);
        } else if ((packageName.startsWith(PACKAGE_PREFIX_GOOGLE)
                && !packageName.toLowerCase().contains("camera"))
                || sExtraPackages.contains(packageName)) {
            dlog("Setting pixel props for: " + packageName + " process: " + processName);
            sPixelProps.forEach(PixelPropsUtils::setPropValue);
        }
    }

    private static void setPropValue(String key, Object value) {
        try {
            dlog("Setting prop " + key + " to " + value.toString());
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to set prop " + key, e);
        }
    }

    private static boolean isCallerSafetyNet() {
        return sIsGms && Arrays.stream(Thread.currentThread().getStackTrace())
                .anyMatch(elem -> elem.getClassName().contains("DroidGuard"));
    }

    public static void onEngineGetCertificateChain() {
        // Check stack for SafetyNet or Play Integrity
        if (isCallerSafetyNet() || sIsFinsky) {
            dlog("Blocked key attestation sIsGms=" + sIsGms + " sIsFinsky=" + sIsFinsky);
            throw new UnsupportedOperationException();
        }
    }

    public static boolean hasSystemFeature(String name, boolean def) {
        if (sIsPhotos && def &&
                sFeatureBlacklist.stream().anyMatch(name::contains)) {
            dlog("Blocked system feature " + name + " for Google Photos");
            return false;
        }
        return def;
    }

    private static void dlog(String msg) {
      if (DEBUG) Log.d(TAG, msg);
    }
}
