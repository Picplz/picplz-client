package com.hm.picplz.ui.screen.quick_shoot.handler

import androidx.compose.ui.geometry.Offset
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.ui.screen.quick_shoot.QuickShootIntent
import com.hm.picplz.ui.screen.quick_shoot.QuickShootState
import com.hm.picplz.ui.screen.quick_shoot.composable.QuickShootSortType
import com.hm.picplz.ui.screen.quick_shoot.util.OffsetGenerator

class PhotographerSearchHandler(
    private val offsetGenerator: OffsetGenerator,
) {
    fun process(
        intent: QuickShootIntent,
        state: QuickShootState,
    ): QuickShootState? {
        return when (intent) {
            is QuickShootIntent.SetIsSearchingPhotographer -> {
                state.copy(isSearchingPhotographer = intent.isSearchingPhotographer)
            }

            is QuickShootIntent.SetNearbyPhotographerLoadFailed -> {
                state.copy(nearbyPhotographerLoadFailed = intent.failed)
            }

            is QuickShootIntent.SetNearbyPhotographers -> {
                state.copy(
                    nearbyPhotographers =
                        intent.nearbyPhotographers.sortedBy(state.selectedSortType),
                )
            }

            is QuickShootIntent.CenterSelectedPhotographer -> {
                val newOffset =
                    Offset(
                        x = -intent.offset.x,
                        y = -intent.offset.y,
                    )
                state.copy(centerOffset = newOffset)
            }

            is QuickShootIntent.DistributeRandomOffsets -> {
                val randomOffsets = offsetGenerator.generateNonOverlappingOffsets(intent.photographers)
                state.copy(randomOffsets = randomOffsets)
            }

            is QuickShootIntent.ToggleSortSheet -> {
                state.copy(showSortSheet = intent.visible)
            }

            is QuickShootIntent.SelectSortType -> {
                state.copy(
                    selectedSortType = intent.sortType,
                    nearbyPhotographers = state.nearbyPhotographers.sortedBy(intent.sortType),
                )
            }

            else -> null
        }
    }
}

private fun FilteredPhotographers.sortedBy(sortType: QuickShootSortType) =
    when (sortType) {
        QuickShootSortType.DISTANCE ->
            copy(
                active = active.sortedBy { it.distance },
                inactive = inactive.sortedBy { it.distance },
            )

        QuickShootSortType.REVIEW_COUNT,
        QuickShootSortType.RATING,
        -> this
    }
