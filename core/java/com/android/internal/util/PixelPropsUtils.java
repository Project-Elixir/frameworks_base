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
        "FINGERPRINT", "google/redfin/redfin:13/TQ2A.230705.001/10216780:user/release-keys"
    );

    private static final Map<String, Object> sPixel7Props = Map.of(
        "BRAND", "google",
        "MANUFACTURER", "Google",
        "DEVICE", "cheetah",
        "PRODUCT", "cheetah",
        "MODEL", "Pixel 7 Pro",
        "FINGERPRINT", "google/cheetah/cheetah:13/TQ2A.230705.001/10216780:user/release-keys"
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

    private static final Map<String, Object> sK30UProps = Map.of(
        "MANUFACTURER", "Xiaomi",
        "MODEL", "M2006J10C"
    );

    private static final Map<String, Object> sROG6Props = Map.of(
        "BRAND", "asus",
        "MANUFACTURER", "asus",
        "DEVICE", "AI2201",
        "MODEL", "ASUS_AI2201"
    );

    private static final Map<String, Object> sXP5Props = Map.of(
        "MANUFACTURER", "Sony",
        "MODEL", "SO-52A"
    );

    private static final Map<String, Object> sOP8PProps = Map.of(
        "MANUFACTURER", "OnePlus",
        "MODEL", "IN2020"
    );

    private static final Map<String, Object> sOP9PProps = Map.of(
        "MANUFACTURER", "OnePlus",
        "MODEL", "LE2123"
    );

    private static final Map<String, Object> s11TProps = Map.of(
        "MANUFACTURER", "Xiaomi",
        "MODEL", "21081111RG"
    );

    private static final Map<String, Object> s13PProps = Map.of(
        "BRAND", "Xiaomi",
        "MANUFACTURER", "Xiaomi",
        "MODEL", "2210132C"
    );

    private static final Map<String, Object> sF4Props = Map.of(
        "MANUFACTURER", "Xiaomi",
        "MODEL", "22021211RG"
    );

    private static final List<String> sExtraPackages = List.of(
        "com.android.chrome",
        "com.android.vending"
    );

    private static final List<String> packagesToChangeK30U = List.of(
        "com.pubg.imobile"
    );

    private static final List<String> packagesToChangeROG6 = List.of(
        "com.activision.callofduty.shooter",
        "com.ea.gp.fifamobile",
        "com.gameloft.android.ANMP.GloftA9HM",
        "com.madfingergames.legends",
        "com.mobile.legends",
        "com.pearlabyss.blackdesertm",
        "com.pearlabyss.blackdesertm.gl"
    );

    private static final List<String> packagesToChangeXP5 = List.of(
        "com.garena.game.codm",
        "com.tencent.tmgp.kr.codm",
        "com.vng.codmvn"
    );

    private static final List<String> packagesToChangeOP8P = List.of(
        "com.netease.lztgglobal",
        "com.pubg.krmobile",
        "com.rekoo.pubgm",
        "com.riotgames.league.wildrift",
        "com.riotgames.league.wildrifttw",
        "com.riotgames.league.wildriftvn",
        "com.tencent.ig",
        "com.tencent.tmgp.pubgmhd",
        "com.vng.pubgmobile"
    );

    private static final List<String> packagesToChangeOP9P = List.of(
        "com.epicgames.fortnite",
        "com.epicgames.portal",
        "com.tencent.lolm"
    );

    private static final List<String> packagesToChange11T = List.of(
        "com.ea.gp.apexlegendsmobilefps",
        "com.levelinfinite.hotta.gp",
        "com.supercell.clashofclans",
        "com.vng.mlbbvn"
    );

    private static final List<String> packagesToChangeMI13P = List.of(
        "com.levelinfinite.sgameGlobal",
        "com.tencent.tmgp.sgame"
    );

    private static final List<String> packagesToChangeF4 = List.of(
        "com.dts.freefiremax",
        "com.dts.freefireth"
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
    private static final String SYS_GAMES_SPOOF = "persist.sys.pixelprops.games";

    private static final String GMS_FINGERPRINT =
            SystemProperties.get("ro.build.gms_fingerprint");

    private static volatile boolean sIsGms = false;
    private static volatile boolean sIsFinsky = false;
    private static volatile boolean sIsPhotos = false;
    private static volatile boolean sIsGamesPropEnabled = SystemProperties.getBoolean(SYS_GAMES_SPOOF, false);

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
        setGamesProp(packageName, processName);
    }

    private static void setGamesProp(String packageName, String processName) {
        if (!sIsGamesPropEnabled) {
            // Games prop switch is turned off
            return;
        }
        if (packagesToChangeK30U.contains(packageName)) {
            dlog("Setting Games props for: " + packageName + " process: " + processName);
            sK30UProps.forEach(PixelPropsUtils::setPropValue);
        } else if (packagesToChangeROG6.contains(packageName)) {
            dlog("Setting Games props for: " + packageName + " process: " + processName);
            sROG6Props.forEach(PixelPropsUtils::setPropValue);
        } else if (packagesToChangeXP5.contains(packageName)) {
            dlog("Setting Games props for: " + packageName + " process: " + processName);
            sXP5Props.forEach(PixelPropsUtils::setPropValue);
        } else if (packagesToChangeOP8P.contains(packageName)) {
            dlog("Setting Games props for: " + packageName + " process: " + processName);
            sOP8PProps.forEach(PixelPropsUtils::setPropValue);
        } else if (packagesToChangeOP9P.contains(packageName)) {
            dlog("Setting Games props for: " + packageName + " process: " + processName);
            sOP9PProps.forEach(PixelPropsUtils::setPropValue);
        } else if (packagesToChange11T.contains(packageName)) {
            dlog("Setting Games props for: " + packageName + " process: " + processName);
            s11TProps.forEach(PixelPropsUtils::setPropValue);
        } else if (packagesToChangeMI13P.contains(packageName)) {
            dlog("Setting Games props for: " + packageName + " process: " + processName);
            s13PProps.forEach(PixelPropsUtils::setPropValue);
        } else if (packagesToChangeF4.contains(packageName)) {
            dlog("Setting Games props for: " + packageName + " process: " + processName);
            sF4Props.forEach(PixelPropsUtils::setPropValue);
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
