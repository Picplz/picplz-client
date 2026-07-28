package com.hm.picplz.ui.screen.main

import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.model.PhotographerPage
import com.hm.picplz.domain.repository.PhotographerSearchRepository
import com.hm.picplz.domain.usecase.SearchPhotographersUseCase
import com.hm.picplz.ui.screen.main.modalBottomSheet.SortType
import com.hm.picplz.ui.screen.photographer_main.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainSearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `typing query loads API preview after debounce`() =
        runTest {
            val repository = FakeSearchPhotographerRepository()
            val viewModel = createViewModel(repository)

            viewModel.handleIntent(MainSearchIntent.FocusChanged(true))
            viewModel.handleIntent(MainSearchIntent.QueryChanged("유가"))

            advanceTimeBy(299)
            assertTrue(repository.requests.isEmpty())

            advanceTimeBy(1)
            advanceUntilIdle()

            assertEquals(SearchRequest("유가", "REVIEW", 0, 5), repository.requests.single())
            assertEquals(SearchUiState.Typing, viewModel.state.value.uiState)
            assertEquals("유가영", viewModel.state.value.suggestions.single().name)
            assertFalse(viewModel.state.value.isPreviewLoading)
        }

    @Test
    fun `new typing query cancels previous preview request`() =
        runTest {
            val repository = FakeSearchPhotographerRepository()
            val viewModel = createViewModel(repository)

            viewModel.handleIntent(MainSearchIntent.FocusChanged(true))
            viewModel.handleIntent(MainSearchIntent.QueryChanged("유"))
            advanceTimeBy(200)
            viewModel.handleIntent(MainSearchIntent.QueryChanged("유가"))
            advanceTimeBy(300)
            advanceUntilIdle()

            assertEquals(listOf(SearchRequest("유가", "REVIEW", 0, 5)), repository.requests)
        }

    @Test
    fun `refocusing after preview cancellation restarts request`() =
        runTest {
            val repository = FakeSearchPhotographerRepository()
            val viewModel = createViewModel(repository)

            viewModel.handleIntent(MainSearchIntent.FocusChanged(true))
            viewModel.handleIntent(MainSearchIntent.QueryChanged("유가"))
            advanceTimeBy(100)
            viewModel.handleIntent(MainSearchIntent.FocusChanged(false))

            assertFalse(viewModel.state.value.isPreviewLoading)
            assertTrue(repository.requests.isEmpty())

            viewModel.handleIntent(MainSearchIntent.FocusChanged(true))
            assertTrue(viewModel.state.value.isPreviewLoading)
            advanceTimeBy(300)
            advanceUntilIdle()

            assertEquals(listOf(SearchRequest("유가", "REVIEW", 0, 5)), repository.requests)
            assertEquals("유가영", viewModel.state.value.suggestions.single().name)
            assertFalse(viewModel.state.value.isPreviewLoading)
        }

    @Test
    fun `search submit loads first page from API`() =
        runTest {
            val repository = FakeSearchPhotographerRepository()
            val viewModel = createViewModel(repository)

            viewModel.handleIntent(MainSearchIntent.SearchSubmitted(" 유가영 "))
            advanceUntilIdle()

            assertEquals(SearchRequest("유가영", "REVIEW", 0, 20), repository.requests.single())
            assertEquals(SearchUiState.Complete("유가영", hasResults = true), viewModel.state.value.uiState)
            assertEquals("유가영", viewModel.state.value.results.single().name)
            assertEquals(1, viewModel.state.value.nextPage)
            assertTrue(viewModel.state.value.hasNextPage)
        }

    @Test
    fun `sort selection reloads first page with server sort`() =
        runTest {
            val repository = FakeSearchPhotographerRepository()
            val viewModel = createViewModel(repository)

            viewModel.handleIntent(MainSearchIntent.SearchSubmitted("유가영"))
            advanceUntilIdle()
            viewModel.handleIntent(MainSearchIntent.SortSelected(SortType.RATING))
            advanceUntilIdle()

            assertEquals(
                listOf(
                    SearchRequest("유가영", "REVIEW", 0, 20),
                    SearchRequest("유가영", "RATING", 0, 20),
                ),
                repository.requests,
            )
            assertEquals(SortType.RATING, viewModel.state.value.selectedSortType)
        }

    @Test
    fun `next page appends unique photographers and stops at last page`() =
        runTest {
            val repository =
                FakeSearchPhotographerRepository { request ->
                    if (request.page == 0) {
                        Result.success(page(0, hasNext = true, photographer(1)))
                    } else {
                        Result.success(page(1, hasNext = false, photographer(1), photographer(2)))
                    }
                }
            val viewModel = createViewModel(repository)

            viewModel.handleIntent(MainSearchIntent.SearchSubmitted("작가"))
            advanceUntilIdle()
            viewModel.handleIntent(MainSearchIntent.LoadNextPage)
            advanceUntilIdle()
            viewModel.handleIntent(MainSearchIntent.LoadNextPage)
            advanceUntilIdle()

            assertEquals(listOf(1L, 2L), viewModel.state.value.results.map { it.id.toLong() })
            assertFalse(viewModel.state.value.hasNextPage)
            assertEquals(2, repository.requests.size)
        }

    @Test
    fun `additional load failure keeps results and can retry`() =
        runTest {
            var additionalAttempt = 0
            val repository =
                FakeSearchPhotographerRepository { request ->
                    if (request.page == 0) {
                        Result.success(page(0, hasNext = true, photographer(1)))
                    } else if (additionalAttempt++ == 0) {
                        Result.failure(IllegalStateException("temporary"))
                    } else {
                        Result.success(page(1, hasNext = false, photographer(2)))
                    }
                }
            val viewModel = createViewModel(repository)

            viewModel.handleIntent(MainSearchIntent.SearchSubmitted("작가"))
            advanceUntilIdle()
            viewModel.handleIntent(MainSearchIntent.LoadNextPage)
            advanceUntilIdle()

            assertEquals(listOf("1"), viewModel.state.value.results.map(MainSearchPhotographerItem::id))
            assertTrue(viewModel.state.value.loadMoreFailed)

            viewModel.handleIntent(MainSearchIntent.LoadNextPage)
            advanceUntilIdle()

            assertEquals(listOf("1", "2"), viewModel.state.value.results.map(MainSearchPhotographerItem::id))
            assertFalse(viewModel.state.value.loadMoreFailed)
        }

    private fun createViewModel(repository: PhotographerSearchRepository = FakeSearchPhotographerRepository()) =
        MainSearchViewModel(SearchPhotographersUseCase(repository))
}

private data class SearchRequest(
    val keyword: String,
    val sortType: String,
    val page: Int,
    val size: Int,
)

private class FakeSearchPhotographerRepository(
    private val result: suspend (SearchRequest) -> AppResult<PhotographerPage> = {
        Result.success(page(0, hasNext = true, photographer(1)))
    },
) : PhotographerSearchRepository {
    val requests = mutableListOf<SearchRequest>()

    override suspend fun searchPhotographers(
        keyword: String,
        sortType: String,
        page: Int,
        size: Int,
    ): AppResult<PhotographerPage> {
        val request = SearchRequest(keyword, sortType, page, size)
        requests += request
        return result(request)
    }
}

private fun page(
    page: Int,
    hasNext: Boolean,
    vararg photographers: Photographer,
) = PhotographerPage(
    photographers = photographers.toList(),
    page = page,
    hasNext = hasNext,
)

private fun photographer(id: Long) =
    Photographer(
        id = id,
        name = if (id == 1L) "유가영" else "강주은",
        profileImageUri = null,
        isActive = id == 1L,
        distance = 0,
        photoMoods = listOf("필름"),
    )
