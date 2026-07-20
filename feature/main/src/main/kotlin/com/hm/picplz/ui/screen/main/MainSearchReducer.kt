package com.hm.picplz.ui.screen.main

import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType

object MainSearchReducer {
    fun reduce(
        state: MainSearchState,
        intent: MainSearchIntent,
    ): MainSearchState =
        when (intent) {
            is MainSearchIntent.QueryChanged -> {
                state.copy(
                    query = intent.query,
                    hasSearched = false,
                    results = emptyList(),
                )
            }

            is MainSearchIntent.FocusChanged -> {
                state.copy(isFocused = intent.isFocused)
            }

            is MainSearchIntent.SearchSubmitted -> {
                val submittedQuery = intent.query.trim()
                state.copy(
                    query = submittedQuery,
                    isFocused = false,
                    hasSearched = true,
                    isLoading = submittedQuery.isNotBlank(),
                    recentSearchQueries = state.recentSearchQueries.withSubmittedQuery(submittedQuery),
                    results = state.nearbyPhotographers.searchResultsFor(submittedQuery, state.selectedSortType),
                )
            }

            is MainSearchIntent.NearbyPhotographersLoaded -> {
                val nearbyPhotographers = intent.photographers
                state.copy(
                    isLoading = false,
                    nearbyPhotographers = nearbyPhotographers,
                    results = nearbyPhotographers.searchResultsFor(state.query, state.selectedSortType),
                )
            }

            MainSearchIntent.SearchLoadFailed -> {
                state.copy(
                    isLoading = false,
                    results = emptyList(),
                )
            }

            is MainSearchIntent.SortSelected -> {
                state.copy(
                    selectedSortType = intent.sortType,
                    results = state.results.sortedBy(intent.sortType),
                )
            }

            is MainSearchIntent.RecentSearchRemoved -> {
                state.copy(recentSearchQueries = state.recentSearchQueries - intent.query)
            }

            MainSearchIntent.RecentSearchCleared -> {
                state.copy(recentSearchQueries = emptyList())
            }
        }

    private fun List<String>.withSubmittedQuery(query: String): List<String> {
        if (query.isBlank() || query in this) return this
        return listOf(query) + this
    }

    private fun List<MainSearchPhotographerItem>.searchResultsFor(
        query: String,
        sortType: SortType,
    ): List<MainSearchPhotographerItem> {
        if (query.isBlank()) return emptyList()

        val normalizedQuery = query.lowercase()
        return filter { item ->
            item.id.lowercase().contains(normalizedQuery) ||
                item.areaSummary.lowercase().contains(normalizedQuery) ||
                item.name.lowercase().contains(normalizedQuery) ||
                item.moodTags.any { it.lowercase().contains(normalizedQuery) }
        }.sortedBy(sortType)
    }

    private fun List<MainSearchPhotographerItem>.sortedBy(sortType: SortType): List<MainSearchPhotographerItem> =
        when (sortType) {
            SortType.POPULAR ->
                sortedWith(
                    compareByDescending<MainSearchPhotographerItem> { it.isAvailableNow }
                        .thenBy { it.distance },
                )
            SortType.RATING -> sortedByDescending { it.moodTags.size }
            SortType.FOLLOWER -> sortedBy { it.distance }
        }
}
