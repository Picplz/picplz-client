package com.hm.picplz.ui.screen.main

import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MainSearchReducerTest {
    @Test
    fun `starts empty with default recent and popular data`() {
        val state = MainSearchState.idle()

        assertEquals(SearchUiState.Empty, state.uiState)
        assertEquals(listOf("연희동", "성수", "홍익대", "연남동", "송파구"), state.recentSearchQueries)
        assertEquals(listOf("성수동", "연남동", "서교동", "합정동", "망원동"), state.popularSpots)
        assertFalse(state.hasSearched)
    }

    @Test
    fun `query change moves to typing and clears prior search`() {
        val state =
            MainSearchReducer.reduce(
                searchedState(),
                MainSearchIntent.QueryChanged("유가"),
            ).copy(isFocused = true)

        assertEquals(SearchUiState.Typing, state.uiState)
        assertEquals("유가", state.query)
        assertTrue(state.isPreviewLoading)
        assertTrue(state.results.isEmpty())
    }

    @Test
    fun `preview result is applied only to current typing query`() {
        val typingState = MainSearchState.idle().copy(query = "유가", isFocused = true)
        val stale =
            MainSearchReducer.reduce(
                typingState,
                MainSearchIntent.PreviewLoaded("유", listOf(item(1))),
            )
        val current =
            MainSearchReducer.reduce(
                stale,
                MainSearchIntent.PreviewLoaded("유가", listOf(item(2))),
            )

        assertTrue(stale.suggestions.isEmpty())
        assertEquals(listOf("2"), current.suggestions.map(MainSearchPhotographerItem::id))
    }

    @Test
    fun `preview result accepts trimmed typing query`() {
        val state =
            MainSearchReducer.reduce(
                MainSearchState.idle().copy(query = "유가 ", isFocused = true),
                MainSearchIntent.PreviewLoaded("유가", listOf(item(1))),
            )

        assertEquals(listOf("1"), state.suggestions.map(MainSearchPhotographerItem::id))
        assertFalse(state.isPreviewLoading)
    }

    @Test
    fun `search submit trims query and starts first page loading`() {
        val state =
            MainSearchReducer.reduce(
                MainSearchState.idle(),
                MainSearchIntent.SearchSubmitted(" 도곡동 "),
            )

        assertEquals(SearchUiState.Complete("도곡동", hasResults = false), state.uiState)
        assertEquals("도곡동", state.query)
        assertTrue(state.isLoading)
        assertEquals("도곡동", state.recentSearchQueries.first())
    }

    @Test
    fun `first search page replaces results and exposes next page`() {
        val state =
            MainSearchReducer.reduce(
                MainSearchState.idle().copy(
                    query = "작가",
                    hasSearched = true,
                    isLoading = true,
                ),
                MainSearchIntent.SearchPageLoaded(
                    query = "작가",
                    sortType = SortType.POPULAR,
                    page = 0,
                    photographers = listOf(item(1)),
                    hasNextPage = true,
                    append = false,
                ),
            )

        assertEquals(SearchUiState.Complete("작가", hasResults = true), state.uiState)
        assertEquals(listOf("1"), state.results.map(MainSearchPhotographerItem::id))
        assertEquals(1, state.nextPage)
        assertTrue(state.hasNextPage)
        assertFalse(state.isLoading)
    }

    @Test
    fun `additional page deduplicates photographers`() {
        val state =
            MainSearchReducer.reduce(
                searchedState(),
                MainSearchIntent.SearchPageLoaded(
                    query = "작가",
                    sortType = SortType.POPULAR,
                    page = 1,
                    photographers = listOf(item(1), item(2)),
                    hasNextPage = false,
                    append = true,
                ),
            )

        assertEquals(listOf("1", "2"), state.results.map(MainSearchPhotographerItem::id))
        assertFalse(state.hasNextPage)
        assertEquals(2, state.nextPage)
    }

    @Test
    fun `additional load failure keeps existing results`() {
        val state =
            MainSearchReducer.reduce(
                searchedState().copy(isLoadingMore = true),
                MainSearchIntent.SearchLoadFailed(append = true),
            )

        assertEquals(listOf("1"), state.results.map(MainSearchPhotographerItem::id))
        assertTrue(state.loadMoreFailed)
        assertFalse(state.isLoadingMore)
    }

    @Test
    fun `initial load failure exposes retry state`() {
        val state =
            MainSearchReducer.reduce(
                searchedState().copy(isLoading = true),
                MainSearchIntent.SearchLoadFailed(append = false),
            )

        assertTrue(state.searchFailed)
        assertFalse(state.isLoading)
        assertTrue(state.results.isEmpty())
    }

    @Test
    fun `sort selection resets result pagination for server reload`() {
        val state =
            MainSearchReducer.reduce(
                searchedState(),
                MainSearchIntent.SortSelected(SortType.FOLLOWER),
            )

        assertEquals(SortType.FOLLOWER, state.selectedSortType)
        assertTrue(state.isLoading)
        assertTrue(state.results.isEmpty())
        assertEquals(0, state.nextPage)
    }

    private fun searchedState() =
        MainSearchState.idle().copy(
            query = "작가",
            hasSearched = true,
            results = listOf(item(1)),
            hasNextPage = true,
            nextPage = 1,
        )

    private fun item(id: Int) =
        MainSearchPhotographerItem(
            id = id.toString(),
            name = "작가 $id",
            profileImageUri = null,
            areaSummary = "",
            isAvailableNow = true,
            moodTags = listOf("필름"),
            distance = 0,
        )
}
