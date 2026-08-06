package com.hm.picplz.ui.screen.quick_shoot

import androidx.compose.ui.geometry.Offset
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.ui.screen.quick_shoot.composable.QuickShootSortType
import com.kakao.vectormap.LatLng

data class QuickShootState(
    val locationPermissionGranted: Boolean = false,
    val hasRequestedPermission: Boolean = false,
    val address: String? = null,
    val centerCoords: LatLng = LatLng.from(37.406960, 127.115587),
    val userLocation: LatLng? = null,
    val isFetchingGPS: Boolean = false,
    val isSearchingPhotographer: Boolean = false,
    val nearbyPhotographerLoadFailed: Boolean = false,
    val nearbyPhotographers: FilteredPhotographers = FilteredPhotographers(),
    val randomOffsets: Map<Long, Offset> = emptyMap(),
    val selectedPhotographerId: Long? = null,
    val selectedPhotographerPreview: Photographer? = null,
    val isLoadingSelectedPhotographer: Boolean = false,
    val centerOffset: Offset? = null,
    val showSortSheet: Boolean = false,
    val selectedSortType: QuickShootSortType = QuickShootSortType.DISTANCE,
) {
    companion object {
        fun idle(): QuickShootState {
            return QuickShootState()
        }
    }
}
