/*
 * Copyright (C) 2022 The Android Open Source Project
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

package com.android.settingslib.spa.widget.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpinnerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun spinner_initialState() {
        var selectedIndex by mutableStateOf(0)
        composeTestRule.setContent {
            Spinner(
                options = (1..3).map { "Option $it" },
                selectedIndex = selectedIndex,
                setIndex = { selectedIndex = it },
            )
        }

        composeTestRule.onNodeWithText("Option 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Option 2").assertDoesNotExist()
        assertThat(selectedIndex).isEqualTo(0)
    }

    @Test
    fun spinner_canChangeState() {
        var selectedIndex by mutableStateOf(0)
        composeTestRule.setContent {
            Spinner(
                options = (1..3).map { "Option $it" },
                selectedIndex = selectedIndex,
                setIndex = { selectedIndex = it },
            )
        }

        composeTestRule.onNodeWithText("Option 1").performClick()
        composeTestRule.onNodeWithText("Option 2").performClick()

        composeTestRule.onNodeWithText("Option 1").assertDoesNotExist()
        composeTestRule.onNodeWithText("Option 2").assertIsDisplayed()
        assertThat(selectedIndex).isEqualTo(1)
    }
}
