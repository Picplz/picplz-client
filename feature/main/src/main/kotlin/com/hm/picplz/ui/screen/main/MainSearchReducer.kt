package com.hm.picplz.ui.screen.main

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
                    isPreviewLoading = intent.query.isNotBlank(),
                    isLoading = false,
                    searchFailed = false,
                    isLoadingMore = false,
                    hasNextPage = false,
                    nextPage = 0,
                    loadMoreFailed = false,
                    suggestions = emptyList(),
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
                    isPreviewLoading = false,
                    isLoading = submittedQuery.isNotBlank(),
                    searchFailed = false,
                    isLoadingMore = false,
                    hasNextPage = false,
                    nextPage = 0,
                    loadMoreFailed = false,
                    recentSearchQueries = state.recentSearchQueries.withSubmittedQuery(submittedQuery),
                    suggestions = emptyList(),
                    results = emptyList(),
                )
            }

            is MainSearchIntent.PreviewLoading -> {
                if (state.hasSearched || state.query.trim() != intent.query) {
                    state
                } else {
                    state.copy(isPreviewLoading = true)
                }
            }

            is MainSearchIntent.PreviewLoaded -> {
                if (state.hasSearched || state.query.trim() != intent.query) {
                    state
                } else {
                    state.copy(
                        isPreviewLoading = false,
                        suggestions = intent.photographers,
                    )
                }
            }

            is MainSearchIntent.PreviewLoadFailed -> {
                if (state.hasSearched || state.query.trim() != intent.query) {
                    state
                } else {
                    state.copy(
                        isPreviewLoading = false,
                        suggestions = emptyList(),
                    )
                }
            }

            is MainSearchIntent.SearchLoading -> {
                if (intent.append) {
                    state.copy(
                        isLoadingMore = true,
                        loadMoreFailed = false,
                    )
                } else {
                    state.copy(
                        isLoading = true,
                        searchFailed = false,
                        isLoadingMore = false,
                        hasNextPage = false,
                        nextPage = 0,
                        loadMoreFailed = false,
                        results = emptyList(),
                    )
                }
            }

            is MainSearchIntent.SearchPageLoaded -> {
                if (
                    !state.hasSearched ||
                    state.query != intent.query ||
                    state.selectedSortType != intent.sortType
                ) {
                    state
                } else {
                    val results =
                        if (intent.append) {
                            (state.results + intent.photographers).distinctBy(MainSearchPhotographerItem::id)
                        } else {
                            intent.photographers.distinctBy(MainSearchPhotographerItem::id)
                        }
                    state.copy(
                        isLoading = false,
                        searchFailed = false,
                        isLoadingMore = false,
                        hasNextPage = intent.hasNextPage,
                        nextPage = intent.page + 1,
                        loadMoreFailed = false,
                        results = results,
                    )
                }
            }

            is MainSearchIntent.SearchLoadFailed -> {
                if (intent.append) {
                    state.copy(
                        isLoadingMore = false,
                        loadMoreFailed = true,
                    )
                } else {
                    state.copy(
                        isLoading = false,
                        searchFailed = true,
                        isLoadingMore = false,
                        hasNextPage = false,
                        results = emptyList(),
                    )
                }
            }

            is MainSearchIntent.SortSelected -> {
                state.copy(
                    selectedSortType = intent.sortType,
                    isLoading = state.hasSearched && state.query.isNotBlank(),
                    searchFailed = false,
                    isLoadingMore = false,
                    hasNextPage = false,
                    nextPage = 0,
                    loadMoreFailed = false,
                    results = if (state.hasSearched) emptyList() else state.results,
                )
            }

            MainSearchIntent.LoadNextPage -> state

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
}
