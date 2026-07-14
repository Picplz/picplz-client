package com.hm.picplz.ui.screen.main.modalBottomSheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import org.junit.Assert.assertTrue
import org.junit.Test

class RegionExploreBottomSheetStateTest {
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun `hidden transition is allowed so scrim tap and drag can dismiss sheet`() {
        assertTrue(canRegionExploreSheetTransitionTo(SheetValue.Hidden))
    }
}
