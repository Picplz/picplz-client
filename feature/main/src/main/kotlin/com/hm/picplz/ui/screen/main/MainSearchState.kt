package com.hm.picplz.ui.screen.main

import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType

data class MainSearchState(
    val query: String = "",
    val isFocused: Boolean = false,
    val hasSearched: Boolean = false,
    val isLoading: Boolean = false,
    val recentSearchQueries: List<String> = defaultRecentSearchQueries,
    val popularSpots: List<String> = defaultPopularSpots,
    val selectedSortType: SortType = SortType.POPULAR,
    val nearbyPhotographers: List<MainSearchPhotographerItem> = emptyList(),
    val results: List<MainSearchPhotographerItem> = emptyList(),
) {
    val uiState: SearchUiState
        get() =
            when {
                hasSearched -> SearchUiState.Complete(query = query, hasResults = results.isNotEmpty())
                isFocused && query.isNotEmpty() -> SearchUiState.Typing
                else -> SearchUiState.Empty
            }

    companion object {
        private val defaultRecentSearchQueries = listOf("연희동", "성수", "홍익대", "연남동", "송파구")
        private val defaultPopularSpots = listOf("성수동", "연남동", "서교동", "합정동", "망원동")

        fun idle(): MainSearchState = MainSearchState()
    }
}

data class MainSearchPhotographerItem(
    val id: String,
    val name: String,
    val profileImageUri: String?,
    val areaSummary: String,
    val isAvailableNow: Boolean,
    val moodTags: List<String>,
    val distance: Long,
)

sealed class SearchUiState {
    data object Empty : SearchUiState()

    data object Typing : SearchUiState()

    data class Complete(
        val query: String,
        val hasResults: Boolean,
    ) : SearchUiState()
}
