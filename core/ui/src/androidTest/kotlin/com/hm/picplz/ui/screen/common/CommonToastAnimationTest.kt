package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TOAST_TEXT = "최소 10자 이상 작성해주세요."
private const val TOAST_DURATION_MS = 2_000L

/**
 * CommonToast의 진입/퇴장 애니메이션이 실제로 재생되는지 프레임 단위로 고정한다.
 *
 * 두 가지 호출 실수를 막기 위한 회귀 테스트다.
 *  1. `isVisible = true` 상수 전달 → 최초 컴포지션부터 visible이라 진입 애니메이션이 생략된다.
 *  2. 조건부 컴포지션(`resId?.let { CommonToast(...) }`) → 사라질 때 노드가 즉시 제거되어
 *     퇴장 애니메이션이 재생될 기회가 없다.
 *
 * 따라서 호출부는 CommonToast를 항상 컴포즈해 두고 isVisible만 토글해야 한다.
 */
@RunWith(AndroidJUnit4::class)
class CommonToastAnimationTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun playsEnterAnimationWhenVisibilityTurnsOn() {
        var visible by mutableStateOf(false)
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                CommonToast(message = TOAST_TEXT, isVisible = visible, onDismiss = {})
            }
        }

        composeRule.mainClock.advanceTimeByFrame()
        composeRule.runOnUiThread { visible = true }
        composeRule.mainClock.advanceTimeByFrame()
        composeRule.mainClock.advanceTimeByFrame()

        val duringEnter = composeRule.onNodeWithText(TOAST_TEXT).getBoundsInRoot().top
        composeRule.mainClock.advanceTimeBy(1_000)
        val settled = composeRule.onNodeWithText(TOAST_TEXT).getBoundsInRoot().top

        assertTrue(
            "진입 애니메이션 중에는 최종 위치보다 아래에 있어야 한다 " +
                "(during=$duringEnter, settled=$settled)",
            duringEnter.value > settled.value,
        )
    }

    @Test
    fun staysMountedWhileExitAnimationRuns() {
        var visible by mutableStateOf(false)
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                CommonToast(message = TOAST_TEXT, isVisible = visible, onDismiss = {})
            }
        }

        composeRule.mainClock.advanceTimeByFrame()
        composeRule.runOnUiThread { visible = true }
        composeRule.mainClock.advanceTimeBy(1_000)
        composeRule.onNodeWithText(TOAST_TEXT).assertIsDisplayed()

        composeRule.runOnUiThread { visible = false }
        composeRule.mainClock.advanceTimeByFrame()

        assertEquals(
            "퇴장 애니메이션이 도는 동안에는 노드가 남아 있어야 한다",
            1,
            composeRule.toastNodeCount(),
        )
    }

    /**
     * 문구를 visibility와 함께 지우면 퇴장 애니메이션 도중에 텍스트만 먼저 사라진다.
     * 호출부가 마지막 문구를 유지해야 하는 이유를 고정한다.
     */
    @Test
    fun clearingMessageWithVisibilityDropsTextImmediately() {
        var message by mutableStateOf("")
        var visible by mutableStateOf(false)
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                CommonToast(message = message, isVisible = visible, onDismiss = {})
            }
        }

        composeRule.mainClock.advanceTimeByFrame()
        composeRule.runOnUiThread {
            message = TOAST_TEXT
            visible = true
        }
        composeRule.mainClock.advanceTimeBy(1_000)
        composeRule.onNodeWithText(TOAST_TEXT).assertIsDisplayed()

        composeRule.runOnUiThread {
            message = ""
            visible = false
        }
        composeRule.mainClock.advanceTimeByFrame()

        assertEquals(
            "문구를 같이 지우면 퇴장 애니메이션 중에 텍스트가 남지 않는다",
            0,
            composeRule.toastNodeCount(),
        )
    }

    @Test
    fun autoDismissesAfterTimeout() {
        var visible by mutableStateOf(false)
        var dismissed = false
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                CommonToast(
                    message = TOAST_TEXT,
                    isVisible = visible,
                    onDismiss = { dismissed = true },
                )
            }
        }

        composeRule.mainClock.advanceTimeByFrame()
        composeRule.runOnUiThread { visible = true }
        composeRule.mainClock.advanceTimeBy(TOAST_DURATION_MS - 500)
        assertTrue("타임아웃 전에는 onDismiss가 호출되지 않아야 한다", !dismissed)

        composeRule.mainClock.advanceTimeBy(600)
        assertTrue("타임아웃 후에는 onDismiss가 호출되어야 한다", dismissed)
    }

    @Test
    fun disappearsAfterExitAnimationCompletes() {
        var visible by mutableStateOf(false)
        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                CommonToast(
                    message = TOAST_TEXT,
                    isVisible = visible,
                    onDismiss = { visible = false },
                )
            }
        }

        composeRule.mainClock.advanceTimeByFrame()
        composeRule.runOnUiThread { visible = true }
        composeRule.mainClock.advanceTimeBy(1_000)
        composeRule.mainClock.advanceTimeBy(TOAST_DURATION_MS)
        composeRule.mainClock.advanceTimeBy(3_000)

        assertEquals(
            "퇴장 애니메이션이 끝나면 노드가 사라져야 한다",
            0,
            composeRule.toastNodeCount(),
        )
    }

    private fun androidx.compose.ui.test.junit4.ComposeContentTestRule.toastNodeCount(): Int =
        onAllNodes(hasText(TOAST_TEXT)).fetchSemanticsNodes().size
}
