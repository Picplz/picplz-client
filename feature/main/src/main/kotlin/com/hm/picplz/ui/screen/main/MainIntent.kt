package com.hm.picplz.ui.screen.main

sealed interface MainIntent {
    data class EnterScreen(val hasLocationPermission: Boolean) : MainIntent

    data object RequestLocationPermission : MainIntent

    data class LocationPermissionResult(val granted: Boolean) : MainIntent

    data object RetryLoad : MainIntent

    data object SearchClicked : MainIntent

    data class PhotographerClicked(val photographerId: Long) : MainIntent

    data object DevEntryClicked : MainIntent

    data class RegionSelected(val region: String) : MainIntent
}
