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
    fun `query change with non-empty focused text moves to typing and clears prior searched state`() {
        val searchedState =
            MainSearchReducer.reduce(
                stateWithNearbyPhotographers(),
                MainSearchIntent.SearchSubmitted("연남동"),
            )

        val state =
            MainSearchReducer.reduce(
                searchedState.copy(isFocused = true),
                MainSearchIntent.QueryChanged("성수"),
            )

        assertEquals(SearchUiState.Typing, state.uiState)
        assertEquals("성수", state.query)
        assertFalse(state.hasSearched)
    }

    @Test
    fun `search submit with non-blank query moves to complete and adds recent once`() {
        val state =
            MainSearchReducer.reduce(
                stateWithNearbyPhotographers(),
                MainSearchIntent.SearchSubmitted("도곡동"),
            )

        assertEquals(SearchUiState.Complete(query = "도곡동", hasResults = true), state.uiState)
        assertEquals("도곡동", state.query)
        assertTrue(state.hasSearched)
        assertEquals(
            listOf("도곡동", "연희동", "성수", "홍익대", "연남동", "송파구"),
            state.recentSearchQueries,
        )
    }

    @Test
    fun `search submit with duplicate query does not duplicate recent`() {
        val state =
            MainSearchReducer.reduce(
                stateWithNearbyPhotographers(),
                MainSearchIntent.SearchSubmitted("성수"),
            )

        assertEquals(1, state.recentSearchQueries.count { it == "성수" })
        assertEquals(listOf("연희동", "성수", "홍익대", "연남동", "송파구"), state.recentSearchQueries)
    }

    @Test
    fun `blank search moves to no-result complete without adding blank recent`() {
        val state =
            MainSearchReducer.reduce(
                stateWithNearbyPhotographers(),
                MainSearchIntent.SearchSubmitted("   "),
            )

        assertEquals(SearchUiState.Complete(query = "", hasResults = false), state.uiState)
        assertTrue(state.hasSearched)
        assertFalse(state.recentSearchQueries.any { it.isBlank() })
        assertEquals(listOf("연희동", "성수", "홍익대", "연남동", "송파구"), state.recentSearchQueries)
    }

    @Test
    fun `non-empty search exposes deterministic photographer results`() {
        val state =
            MainSearchReducer.reduce(
                stateWithNearbyPhotographers(),
                MainSearchIntent.SearchSubmitted("강남"),
            )

        assertTrue(state.uiState is SearchUiState.Complete)
        assertEquals(3, state.results.size)
        assertTrue(state.results.all { it.name.endsWith("작가") })
    }

    @Test
    fun `ascii gangnam search exposes deterministic photographer results for adb input`() {
        val state =
            MainSearchReducer.reduce(
                stateWithNearbyPhotographers(),
                MainSearchIntent.SearchSubmitted("gangnam"),
            )

        assertEquals(SearchUiState.Complete(query = "gangnam", hasResults = true), state.uiState)
        assertEquals(3, state.results.size)
    }

    @Test
    fun `no-match search exposes only empty result state`() {
        val state =
            MainSearchReducer.reduce(
                stateWithNearbyPhotographers(),
                MainSearchIntent.SearchSubmitted("ㅁㄴㅇㄹ없는검색어"),
            )

        assertEquals(SearchUiState.Complete(query = "ㅁㄴㅇㄹ없는검색어", hasResults = false), state.uiState)
        assertEquals(emptyList<MainSearchPhotographerItem>(), state.results)
    }

    @Test
    fun `sort selection changes deterministic result order`() {
        val searchedState =
            MainSearchReducer.reduce(
                stateWithNearbyPhotographers(),
                MainSearchIntent.SearchSubmitted("강남"),
            )

        val followerSortedState =
            MainSearchReducer.reduce(
                searchedState,
                MainSearchIntent.SortSelected(SortType.FOLLOWER),
            )

        assertEquals(SortType.FOLLOWER, followerSortedState.selectedSortType)
        assertEquals(
            followerSortedState.results.minBy { it.distance }.name,
            followerSortedState.results.first().name,
        )
        assertFalse(searchedState.results.first().name == followerSortedState.results.first().name)
    }

    private fun stateWithNearbyPhotographers(): MainSearchState =
        MainSearchState.idle().copy(
            nearbyPhotographers =
                listOf(
                    MainSearchPhotographerItem(
                        id = "gangnam-studio",
                        name = "윤서 작가",
                        profileImageUri = null,
                        areaSummary = "서울 강남구 도곡동",
                        isAvailableNow = true,
                        moodTags = listOf("도시 감성", "프로필"),
                        distance = 300,
                    ),
                    MainSearchPhotographerItem(
                        id = "gangnam-film",
                        name = "민재 작가",
                        profileImageUri = null,
                        areaSummary = "서울 강남구 역삼동",
                        isAvailableNow = false,
                        moodTags = listOf("필름 무드", "커플"),
                        distance = 120,
                    ),
                    MainSearchPhotographerItem(
                        id = "gangnam-pet",
                        name = "가은 작가",
                        profileImageUri = null,
                        areaSummary = "서울 강남구 신사동",
                        isAvailableNow = true,
                        moodTags = listOf("반려동물", "자연광"),
                        distance = 520,
                    ),
                ),
        )
}
