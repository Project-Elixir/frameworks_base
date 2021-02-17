/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.window.extensions;

import static android.view.Display.DEFAULT_DISPLAY;

import static androidx.window.util.ExtensionHelper.rotateRectToDisplayRotation;
import static androidx.window.util.ExtensionHelper.transformToWindowSpaceRect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.window.util.BaseDisplayFeature;
import androidx.window.util.SettingsConfigProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Reference implementation of androidx.window.extensions OEM interface for use with
 * WindowManager Jetpack.
 *
 * NOTE: This version is a work in progress and under active development. It MUST NOT be used in
 * production builds since the interface can still change before reaching stable version.
 * Please refer to {@link androidx.window.sidecar.SampleSidecarImpl} instead.
 */
class SampleExtensionImpl extends StubExtension implements
        SettingsConfigProvider.StateChangeCallback {
    private static final String TAG = "SampleExtension";

    private final SettingsConfigProvider mConfigProvider;

    SampleExtensionImpl(Context context) {
        mConfigProvider = new SettingsConfigProvider(context, this);
    }

    @Override
    public void onDevicePostureChanged() {
        updateDeviceState(new ExtensionDeviceState(mConfigProvider.getDeviceState()));
    }

    @Override
    public void onDisplayFeaturesChanged() {
        for (Activity activity : getActivitiesListeningForLayoutChanges()) {
            ExtensionWindowLayoutInfo newLayout = getWindowLayoutInfo(activity);
            updateWindowLayout(activity, newLayout);
        }
    }

    @NonNull
    private ExtensionWindowLayoutInfo getWindowLayoutInfo(@NonNull Activity activity) {
        List<ExtensionDisplayFeature> displayFeatures = getDisplayFeatures(activity);
        return new ExtensionWindowLayoutInfo(displayFeatures);
    }

    private List<ExtensionDisplayFeature> getDisplayFeatures(@NonNull Activity activity) {
        List<ExtensionDisplayFeature> features = new ArrayList<>();
        int displayId = activity.getDisplay().getDisplayId();
        if (displayId != DEFAULT_DISPLAY) {
            Log.w(TAG, "This sample doesn't support display features on secondary displays");
            return features;
        }

        if (activity.isInMultiWindowMode()) {
            // It is recommended not to report any display features in multi-window mode, since it
            // won't be possible to synchronize the display feature positions with window movement.
            return features;
        }

        List<BaseDisplayFeature> storedFeatures = mConfigProvider.getDisplayFeatures();
        for (BaseDisplayFeature baseFeature : storedFeatures) {
            Rect featureRect = baseFeature.getRect();
            rotateRectToDisplayRotation(displayId, featureRect);
            transformToWindowSpaceRect(activity, featureRect);
            features.add(new ExtensionFoldingFeature(featureRect, baseFeature.getType(),
                    baseFeature.getState()));
        }
        return features;
    }

    @Override
    protected void onListenersChanged() {
        if (hasListeners()) {
            mConfigProvider.registerObserversIfNeeded();
        } else {
            mConfigProvider.unregisterObserversIfNeeded();
        }

        onDevicePostureChanged();
        onDisplayFeaturesChanged();
    }
}
