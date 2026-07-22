package com.hm.picplz.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.usecase.SearchPhotographersUseCase
import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSearchViewModel
    @Inject
    constructor(
        private val searchPhotographersUseCase: SearchPhotographersUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MainSearchState.idle())
        val state: StateFlow<MainSearchState> = _state.asStateFlow()
        private var previewJob: Job? = null
        private var searchJob: Job? = null

        fun handleIntent(intent: MainSearchIntent) {
            when (intent) {
                is MainSearchIntent.QueryChanged -> {
                    searchJob?.cancel()
                    _state.reduce(intent)
                    requestPreview(intent.query)
                }

                is MainSearchIntent.FocusChanged -> {
                    _state.reduce(intent)
                    if (!intent.isFocused) previewJob?.cancel()
                }

                is MainSearchIntent.SearchSubmitted -> {
                    previewJob?.cancel()
                    searchJob?.cancel()
                    _state.reduce(intent)
                    intent.query.trim().takeIf(String::isNotBlank)?.let { query ->
                        loadSearchPage(
                            query = query,
                            sortType = _state.value.selectedSortType,
                            page = 0,
                            append = false,
                        )
                    }
                }

                is MainSearchIntent.SortSelected -> {
                    searchJob?.cancel()
                    _state.reduce(intent)
                    val currentState = _state.value
                    if (currentState.hasSearched && currentState.query.isNotBlank()) {
                        loadSearchPage(
                            query = currentState.query,
                            sortType = intent.sortType,
                            page = 0,
                            append = false,
                        )
                    }
                }

                MainSearchIntent.LoadNextPage -> loadNextPage()
                else -> _state.reduce(intent)
            }
        }

        private fun requestPreview(rawQuery: String) {
            previewJob?.cancel()
            val query = rawQuery.trim()
            if (query.isBlank()) return

            previewJob =
                viewModelScope.launch {
                    delay(PREVIEW_DEBOUNCE_MILLIS)
                    _state.reduce(MainSearchIntent.PreviewLoading(query))
                    searchPhotographersUseCase(
                        keyword = query,
                        sortType = _state.value.selectedSortType.apiValue,
                        page = 0,
                        size = PREVIEW_PAGE_SIZE,
                    ).onSuccess { page ->
                        _state.reduce(
                            MainSearchIntent.PreviewLoaded(
                                query = query,
                                photographers = page.photographers.map { it.toSearchItem() },
                            ),
                        )
                    }.onFailure {
                        _state.reduce(MainSearchIntent.PreviewLoadFailed(query))
                    }
                }
        }

        private fun loadNextPage() {
            val currentState = _state.value
            if (
                currentState.isLoading ||
                currentState.isLoadingMore ||
                !currentState.hasNextPage ||
                currentState.query.isBlank()
            ) {
                return
            }

            loadSearchPage(
                query = currentState.query,
                sortType = currentState.selectedSortType,
                page = currentState.nextPage,
                append = true,
            )
        }

        private fun loadSearchPage(
            query: String,
            sortType: SortType,
            page: Int,
            append: Boolean,
        ) {
            _state.reduce(MainSearchIntent.SearchLoading(append))
            searchJob =
                viewModelScope.launch {
                    searchPhotographersUseCase(
                        keyword = query,
                        sortType = sortType.apiValue,
                        page = page,
                        size = SEARCH_PAGE_SIZE,
                    ).onSuccess { result ->
                        _state.reduce(
                            MainSearchIntent.SearchPageLoaded(
                                query = query,
                                sortType = sortType,
                                page = result.page,
                                photographers = result.photographers.map { it.toSearchItem() },
                                hasNextPage = result.hasNext,
                                append = append,
                            ),
                        )
                    }.onFailure {
                        _state.reduce(MainSearchIntent.SearchLoadFailed(append))
                    }
                }
        }

        private fun Photographer.toSearchItem(): MainSearchPhotographerItem =
            MainSearchPhotographerItem(
                id = id.toString(),
                name = name,
                profileImageUri = profileImageUri,
                areaSummary = activeAreas.joinToString(", "),
                isAvailableNow = isActive,
                moodTags = photoMoods,
                distance = distance,
            )

        private fun MutableStateFlow<MainSearchState>.reduce(intent: MainSearchIntent) {
            update { MainSearchReducer.reduce(it, intent) }
        }

        private val SortType.apiValue: String
            get() =
                when (this) {
                    SortType.POPULAR -> "REVIEW"
                    SortType.RATING -> "RATING"
                    SortType.FOLLOWER -> "FOLLOWER"
                }

        private companion object {
            const val PREVIEW_DEBOUNCE_MILLIS = 300L
            const val PREVIEW_PAGE_SIZE = 5
            const val SEARCH_PAGE_SIZE = 20
        }
    }
