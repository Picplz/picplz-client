package com.hm.picplz.ui.screen.main

sealed interface MainSideEffect {
    data object RequestLocationPermission : MainSideEffect

    data object NavigateToSearch : MainSideEffect

    data class NavigateToPhotographerDetail(val photographerId: Long) : MainSideEffect

    data object ShowReportUnavailable : MainSideEffect

    data object NavigateToDev : MainSideEffect
}
