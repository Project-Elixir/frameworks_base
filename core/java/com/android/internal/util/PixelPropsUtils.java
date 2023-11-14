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

import android.app.ActivityTaskManager;
import android.app.Application;
import android.app.TaskStackListener;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Binder;
import android.os.Process;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PixelPropsUtils {

    private static final String TAG = "PixelPropsUtils";
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_KEYS = false;
    private static final boolean DEBUG_PACKAGES = false;

    // Packages to Spoof as Pixel 7 Pro
    private static final String[] sPixel7ProPackages = {
        "com.google.android.apps.emojiwallpaper",
        "com.amazon.avod.thirdpartyclient",
        "com.breel.wallpapers20",
        "com.disney.disneyplus",
        "com.microsoft.android.smsorganizer",
        "com.netflix.mediaclient",
        "com.nhs.online.nhsonline",
        "com.nothing.smartcenter",
        "in.startv.hotstar"
    };

    // Packages to Spoof Samsung S23   
    private static final String[] sS23Packages = {
        "com.google.android.youtube",
        "com.google.android.apps.youtube.music"
    };

    private static final Map<String, Object> sPixelProps = Map.of(
        "BRAND", "google",
        "MANUFACTURER", "Google",
        "DEVICE", "redfin",
        "HARDWARE", "redfin",
        "ID", "UP1A.231005.007",
        "PRODUCT", "redfin",
        "MODEL", "Pixel 5",
        "FINGERPRINT", "google/redfin/redfin:14/UP1A.231005.007/10754064:user/release-keys"
    );

    private static final Map<String, Object> sS23Props = Map.of(
        "BRAND", "samsung",
        "MANUFACTURER", "samsung",
        "DEVICE", "dm1q",
        "MODEL", "SM-S911B",
        "FINGERPRINT", "samsung/dm1qxxx/dm1q:13/TP1A.220624.014/S911BXXS3AWF7:user/release-keys"
    );

    private static final Map<String, Object> sPixel7Props =
            createGoogleSpoofProps("cheetah", "Pixel 7 Pro",
                    "google/cheetah/cheetah:14/UP1A.231005.007/10754064:user/release-keys");

    private static String getBuildID(String fingerprint) {
        Pattern pattern = Pattern.compile("([A-Za-z0-9]+\\.\\d+\\.\\d+\\.\\w+)");
        Matcher matcher = pattern.matcher(fingerprint);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static final Map<String, Object> sPixelXLProps =
            createGoogleSpoofProps("marlin", "Pixel XL",
                    "google/marlin/marlin:10/QP1A.191005.007.A3/5972272:user/release-keys");

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
        "com.vng.pubgmobile",
        "com.pubg.imobile"
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
        "com.google.android.apps.motionsense.bridge",
        "com.google.android.apps.pixelmigrate",
        "com.google.android.apps.recorder",
        "com.google.android.apps.restore",
        "com.google.android.apps.tachyon",
        "com.google.android.apps.tycho",
        "com.google.android.apps.wearables.maestro.companion",
        "com.google.android.settings.intelligence",
        "com.google.android.apps.youtube.kids",
        "com.google.android.as",
        "com.google.android.dialer",
        "com.google.android.euicc",
        "com.google.android.setupwizard",
        "com.google.ar.core",
        "com.google.oslo"
    );

    private static final List<String> sFeatureBlacklist = List.of(
        "PIXEL_2017_PRELOAD",
        "PIXEL_2018_PRELOAD",
        "PIXEL_2019_MIDYEAR_PRELOAD",
        "PIXEL_2019_PRELOAD",
        "PIXEL_2020_EXPERIENCE",
        "PIXEL_2020_MIDYEAR_EXPERIENCE",
        "PIXEL_2021_EXPERIENCE",
        "PIXEL_2021_MIDYEAR_EXPERIENCE",
        "PIXEL_2022_EXPERIENCE",
        "PIXEL_2022_MIDYEAR_EXPERIENCE"
    );

    private static final String PACKAGE_PREFIX_GOOGLE = "com.google.android.";
    private static final String PACKAGE_GPHOTOS = "com.google.android.apps.photos";
    private static final String PACKAGE_FINSKY = "com.android.vending";
    private static final String PACKAGE_GMS = "com.google.android.gms";
    private static final String PROCESS_GMS_UNSTABLE = PACKAGE_GMS + ".unstable";
    private static final String SYS_GAMES_SPOOF = "persist.sys.pixelprops.games";

    private static final ComponentName GMS_ADD_ACCOUNT_ACTIVITY = ComponentName.unflattenFromString(
            "com.google.android.gms/.auth.uiflows.minutemaid.MinuteMaidActivity");

    private static volatile String sProcessName;

    private static final String GMS_FINGERPRINT =
            SystemProperties.get("ro.build.gms_fingerprint");

    private static volatile boolean sIsGms = false;
    private static volatile boolean sIsFullGms = false;
    private static volatile boolean sIsFinsky = false;
    private static volatile boolean sIsPhotos = false;
    private static volatile boolean sIsGamesPropEnabled = SystemProperties.getBoolean(SYS_GAMES_SPOOF, false);

    public static void setProps(Context context) {
        final String packageName = context.getPackageName();
        final String processName = Application.getProcessName();

        sProcessName = processName;

        if (DEBUG_PACKAGES) {
            Log.d(TAG, "setProps packageName=" + packageName + " processName=" + processName);
        }

        if (TextUtils.isEmpty(packageName) || processName == null
                || sPackageWhitelist.contains(packageName)) {
            return;
        }

        sIsGms = packageName.equals(PACKAGE_GMS) && processName.equals(PROCESS_GMS_UNSTABLE);
        sIsFullGms = packageName.equals(PACKAGE_GMS);
        sIsFinsky = packageName.equals(PACKAGE_FINSKY);
        sIsPhotos = packageName.equals(PACKAGE_GPHOTOS);

        if (sIsGms) {
            dlog("Spoofing build for GMS");
            setCertifiedPropsForGms();
        } else if (sIsFullGms) {
            dlog("Spoofing build time for GMS");
            setPropValue("TIME", System.currentTimeMillis());
        } else if (sIsPhotos) {
            dlog("Spoofing Pixel XL for Google Photos");
            sPixelXLProps.forEach(PixelPropsUtils::setPropValue);
        } else if ((Arrays.asList(sPixel7ProPackages).contains(packageName))) {
            dlog("Spoofing Pixel 7 Pro for :- " + packageName);
            sPixel7Props.forEach(PixelPropsUtils::setPropValue);
        } else if ((Arrays.asList(sS23Packages).contains(packageName))) {
            dlog("Spoofing Samsung S23 for :- " + packageName);
            sS23Props.forEach(PixelPropsUtils::setPropValue);
        } else if ((packageName.startsWith(PACKAGE_PREFIX_GOOGLE)
                && !packageName.toLowerCase().contains("camera"))
                || sExtraPackages.contains(packageName)) {
            dlog("Setting pixel props for: " + packageName + " process: " + processName);
            sPixelProps.forEach(PixelPropsUtils::setPropValue);
        }
        setGamesProp(packageName, processName);
    }

    private static Map<String, Object> createGoogleSpoofProps(String device, String model, String fingerprint) {
        Map<String, Object> props = new HashMap<>();
        props.put("BRAND", "google");
        props.put("MANUFACTURER", "Google");
        props.put("ID", getBuildID(fingerprint));
        props.put("DEVICE", device);
        props.put("PRODUCT", device);
        props.put("MODEL", model);
        props.put("FINGERPRINT", fingerprint);
        props.put("TYPE", "user");
        props.put("TAGS", "release-keys");
        return props;
    }

    private static void setCertifiedPropsForGms() {
        final boolean was = isGmsAddAccountActivityOnTop();
        final TaskStackListener taskStackListener = new TaskStackListener() {
            @Override
            public void onTaskStackChanged() {
                final boolean is = isGmsAddAccountActivityOnTop();
                if (is ^ was) {
                    dlog("GmsAddAccountActivityOnTop is:" + is + " was:" + was +
                            ", killing myself!"); // process will restart automatically later
                    Process.killProcess(Process.myPid());
                }
            }
        };
        if (!was) {
            dlog("Spoofing build for GMS");
            setPropValue("MANUFACTURER", "Asus");
            setPropValue("BRAND", "Asus");
            setPropValue("DEVICE", "ASUS_X00HD_4");
            setPropValue("PRODUCT", "WW_Phone");
            setPropValue("MODEL", "ASUS_X00HD");
            setPropValue("FINGERPRINT", "asus/WW_Phone/ASUS_X00HD_4:7.1.1/NMF26F/14.2016.1801.372-20180119:user/release-keys");
        } else {
            dlog("Skip spoofing build for GMS, because GmsAddAccountActivityOnTop");
        }
        try {
            ActivityTaskManager.getService().registerTaskStackListener(taskStackListener);
        } catch (Exception e) {
            Log.e(TAG, "Failed to register task stack listener!", e);
        }
    }

    private static boolean isGmsAddAccountActivityOnTop() {
        try {
            final ActivityTaskManager.RootTaskInfo focusedTask =
                    ActivityTaskManager.getService().getFocusedRootTaskInfo();
            return focusedTask != null && focusedTask.topActivity != null
                    && focusedTask.topActivity.equals(GMS_ADD_ACCOUNT_ACTIVITY);
        } catch (Exception e) {
            Log.e(TAG, "Unable to get top activity!", e);
        }
        return false;
    }

    public static boolean shouldBypassTaskPermission(Context context) {
        // GMS doesn't have MANAGE_ACTIVITY_TASKS permission
        final int callingUid = Binder.getCallingUid();
        final int gmsUid;
        try {
            gmsUid = context.getPackageManager().getApplicationInfo(PACKAGE_GMS, 0).uid;
            dlog("shouldBypassTaskPermission: gmsUid:" + gmsUid + " callingUid:" + callingUid);
        } catch (Exception e) {
            Log.e(TAG, "shouldBypassTaskPermission: unable to get gms uid", e);
            return false;
        }
        return gmsUid == callingUid;
    }

    private static void setGamesProp(String packageName, String processName) {
        if (!sIsGamesPropEnabled) {
            // Games prop switch is turned off
            return;
        }
        if (packagesToChangeROG6.contains(packageName)) {
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
            keylog("Setting prop " + key + " to " + value.toString());
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
        if (DEBUG) Log.d(TAG, "[" + sProcessName + "] " + msg);
    }

    private static void keylog(String msg) {
        if (DEBUG_KEYS) Log.d(TAG, "[" + sProcessName + "] " + msg);
    }
}
