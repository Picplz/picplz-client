package com.hm.picplz.ui.screen.main

import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType

sealed interface MainSearchIntent {
    data class QueryChanged(val query: String) : MainSearchIntent

    data class FocusChanged(val isFocused: Boolean) : MainSearchIntent

    data class SearchSubmitted(val query: String) : MainSearchIntent

    data class PreviewLoading(val query: String) : MainSearchIntent

    data class PreviewLoaded(
        val query: String,
        val photographers: List<MainSearchPhotographerItem>,
    ) : MainSearchIntent

    data class PreviewLoadFailed(val query: String) : MainSearchIntent

    data class SearchLoading(val append: Boolean) : MainSearchIntent

    data class SearchPageLoaded(
        val query: String,
        val sortType: SortType,
        val page: Int,
        val photographers: List<MainSearchPhotographerItem>,
        val hasNextPage: Boolean,
        val append: Boolean,
    ) : MainSearchIntent

    data class SearchLoadFailed(val append: Boolean) : MainSearchIntent

    data class SortSelected(val sortType: SortType) : MainSearchIntent

    data object LoadNextPage : MainSearchIntent

    data class RecentSearchRemoved(val query: String) : MainSearchIntent

    data object RecentSearchCleared : MainSearchIntent
}
