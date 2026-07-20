package com.hm.picplz.ui.screen.main

import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType

sealed interface MainSearchIntent {
    data class QueryChanged(val query: String) : MainSearchIntent

    data class FocusChanged(val isFocused: Boolean) : MainSearchIntent

    data class SearchSubmitted(val query: String) : MainSearchIntent

    data class NearbyPhotographersLoaded(val photographers: List<MainSearchPhotographerItem>) : MainSearchIntent

    data object SearchLoadFailed : MainSearchIntent

    data class SortSelected(val sortType: SortType) : MainSearchIntent

    data class RecentSearchRemoved(val query: String) : MainSearchIntent

    data object RecentSearchCleared : MainSearchIntent
}
