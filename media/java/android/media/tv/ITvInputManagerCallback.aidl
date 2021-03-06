/*
 * Copyright (C) 2014 The Android Open Source Project
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

package android.media.tv;

import android.media.tv.TunedInfo;
import android.media.tv.TvInputInfo;

/**
 * Interface to receive callbacks from ITvInputManager regardless of sessions.
 * @hide
 */
oneway interface ITvInputManagerCallback {
    void onInputAdded(in String inputId);
    void onInputRemoved(in String inputId);
    void onInputUpdated(in String inputId);
    void onInputStateChanged(in String inputId, int state);
    void onTvInputInfoUpdated(in TvInputInfo TvInputInfo);
    void onCurrentTunedInfosUpdated(in List<TunedInfo> currentTunedInfos);
}
