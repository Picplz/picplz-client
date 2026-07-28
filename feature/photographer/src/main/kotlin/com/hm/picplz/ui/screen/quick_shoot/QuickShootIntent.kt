package com.hm.picplz.ui.screen.quick_shoot

import androidx.compose.ui.geometry.Offset
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.ui.screen.quick_shoot.composable.QuickShootSortType
import com.kakao.vectormap.LatLng

sealed interface QuickShootIntent {
    data object NavigateToPrev : QuickShootIntent

    data class SetAddress(val address: String) : QuickShootIntent

    data class GetAddress(val coords: LatLng) : QuickShootIntent

    data class SetCenterCoords(val centerCoords: LatLng) : QuickShootIntent

    data class SetCurrentLocation(val location: LatLng) : QuickShootIntent

    data object RequestLocationPermission : QuickShootIntent

    data class SetLocationPermissionGranted(val granted: Boolean) : QuickShootIntent

    data object GetCurrentLocation : QuickShootIntent

    data class SetIsSearchingPhotographer(val isSearchingPhotographer: Boolean) : QuickShootIntent

    data class SetNearbyPhotographerLoadFailed(val failed: Boolean) : QuickShootIntent

    data class SetNearbyPhotographers(val nearbyPhotographers: FilteredPhotographers) : QuickShootIntent

    data object FetchNearbyPhotographers : QuickShootIntent

    data object RefetchNearbyPhotographers : QuickShootIntent

    data class DistributeRandomOffsets(val photographers: FilteredPhotographers) : QuickShootIntent

    data class SetSelectedPhotographerId(val photographerId: Long?) : QuickShootIntent

    data class CenterSelectedPhotographer(val offset: Offset) : QuickShootIntent

    data class ToggleSortSheet(val visible: Boolean) : QuickShootIntent

    data class SelectSortType(val sortType: QuickShootSortType) : QuickShootIntent
}
