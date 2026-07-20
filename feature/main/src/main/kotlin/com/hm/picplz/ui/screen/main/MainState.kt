package com.hm.picplz.ui.screen.main

data class MainState(
    val locationPermissionGranted: Boolean = false,
    val hasRequestedPermission: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasNextPage: Boolean = false,
    val nextPage: Int = 0,
    val loadMoreFailed: Boolean = false,
    val errorMessage: MainLoadError? = null,
    val homeItems: List<CustomerHomeItem> = emptyList(),
    val selectedRegion: String = "",
) {
    companion object {
        fun idle(): MainState = MainState()
    }
}

data class CustomerHomeItem(
    val photographerId: Long,
    val photographerName: String,
    val profileImageUri: String?,
    val portfolioId: Long?,
    val portfolioImageUris: List<String>,
    val location: String,
    val uploadDate: String?,
    val photoCount: Int,
    val isActive: Boolean,
    val distance: Long,
    val moodTags: List<String>,
)

enum class MainLoadError {
    LocationUnavailable,
    NearbyPhotographers,
}
