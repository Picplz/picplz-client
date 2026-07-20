package com.hm.picplz.ui.screen.main

import android.content.Context
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo
import com.hm.picplz.domain.model.LocationCoordinate
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.model.PhotographerPage
import com.hm.picplz.domain.model.PortfolioDetail
import com.hm.picplz.domain.model.PortfolioSummary
import com.hm.picplz.domain.repository.AuthRepository
import com.hm.picplz.domain.repository.LocationRepository
import com.hm.picplz.domain.repository.MemberRepository
import com.hm.picplz.domain.repository.PhotographerRepository
import com.hm.picplz.domain.repository.PhotographerSearchRepository
import com.hm.picplz.domain.repository.PortfolioRepository
import com.hm.picplz.domain.usecase.GetCurrentLocationUseCase
import com.hm.picplz.domain.usecase.GetCurrentMemberIdUseCase
import com.hm.picplz.domain.usecase.GetPhotographerPortfoliosUseCase
import com.hm.picplz.domain.usecase.GetPortfolioUseCase
import com.hm.picplz.domain.usecase.SearchPhotographersUseCase
import com.hm.picplz.domain.usecase.UpdateMemberLocationUseCase
import com.hm.picplz.ui.screen.photographer_main.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `first entry without permission shows rationale`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(MainIntent.EnterScreen(hasLocationPermission = false))

            assertFalse(viewModel.state.value.locationPermissionGranted)
            assertFalse(viewModel.state.value.hasRequestedPermission)
            assertTrue(viewModel.state.value.homeItems.isEmpty())
        }

    @Test
    fun `permission granted loads first photographer page and portfolios`() =
        runTest {
            val locationRepository = FakeLocationRepository(location = LocationCoordinate(37.5, 127.0))
            val photographerRepository =
                FakePhotographerRepository(
                    searchResults =
                        mapOf(
                            0 to
                                Result.success(
                                    PhotographerPage(
                                        photographers = (1L..5L).map(::photographer),
                                        page = 0,
                                        hasNext = true,
                                    ),
                                ),
                        ),
                )
            val portfolioRepository =
                FakePortfolioRepository(
                    results =
                        (1L..5L).associateWith { id ->
                            Result.success(
                                listOf(
                                    PortfolioSummary(
                                        id = id,
                                        representativeImage = "https://image/$id.jpg",
                                        photoCount = 4,
                                        location = "서울 강남구",
                                        uploadDate = "2026-06-11",
                                    ),
                                ),
                            )
                        },
                    detailResults =
                        (1L..5L).associateWith { id ->
                            Result.success(
                                PortfolioDetail(
                                    id = id,
                                    imageUris =
                                        listOf(
                                            "https://image/$id-1.jpg",
                                            "https://image/$id-2.jpg",
                                        ),
                                    location = "서울 강남구",
                                    uploadDate = "2026-06-11",
                                ),
                            )
                        },
                )
            val viewModel =
                createViewModel(
                    locationRepository = locationRepository,
                    photographerRepository = photographerRepository,
                    portfolioRepository = portfolioRepository,
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()

            val state = viewModel.state.value
            assertTrue(state.locationPermissionGranted)
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
            assertEquals(5, state.homeItems.size)
            assertTrue(state.hasNextPage)
            assertEquals(1, state.nextPage)
            assertEquals(
                listOf("https://image/1-1.jpg", "https://image/1-2.jpg"),
                state.homeItems.first().portfolioImageUris,
            )
            assertEquals(listOf(1L, 2L, 3L, 4L, 5L), portfolioRepository.requestedPhotographerIds)
            assertEquals(listOf(1L, 2L, 3L, 4L, 5L), portfolioRepository.requestedPortfolioIds)
        }

    @Test
    fun `first page failure exposes empty retry state without local fallback`() =
        runTest {
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(location = LocationCoordinate(37.5, 127.0)),
                    photographerRepository =
                        FakePhotographerRepository(
                            searchResults = mapOf(0 to Result.failure(IllegalStateException("network"))),
                        ),
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()

            val state = viewModel.state.value
            assertFalse(state.isLoading)
            assertTrue(state.homeItems.isEmpty())
            assertEquals(MainLoadError.NearbyPhotographers, state.errorMessage)
        }

    @Test
    fun `member location update failure does not block photographer feed`() =
        runTest {
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(location = LocationCoordinate(37.5, 127.0)),
                    memberRepository =
                        FakeMemberRepository(
                            updateLocationResult = Result.failure(IllegalStateException()),
                        ),
                    photographerRepository =
                        FakePhotographerRepository(
                            searchResults =
                                mapOf(
                                    0 to
                                        Result.success(
                                            PhotographerPage(
                                                photographers = listOf(photographer(1L)),
                                                page = 0,
                                                hasNext = false,
                                            ),
                                        ),
                                ),
                        ),
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()

            val state = viewModel.state.value
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
            assertEquals("유가영 작가", state.homeItems.single().photographerName)
        }

    @Test
    fun `portfolio failure keeps photographer with no image`() =
        runTest {
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(location = LocationCoordinate(37.5, 127.0)),
                    photographerRepository =
                        FakePhotographerRepository(
                            searchResults =
                                mapOf(
                                    0 to
                                        Result.success(
                                            PhotographerPage(
                                                photographers = listOf(photographer(1L)),
                                                page = 0,
                                                hasNext = false,
                                            ),
                                        ),
                                ),
                        ),
                    portfolioRepository =
                        FakePortfolioRepository(
                            results = mapOf(1L to Result.failure(IllegalStateException("network"))),
                        ),
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()

            val item = viewModel.state.value.homeItems.single()
            assertEquals("유가영 작가", item.photographerName)
            assertTrue(item.portfolioImageUris.isEmpty())
        }

    @Test
    fun `next page appends unique photographers and advances page`() =
        runTest {
            val photographerRepository =
                FakePhotographerRepository(
                    searchResults =
                        mapOf(
                            0 to
                                Result.success(
                                    PhotographerPage(
                                        photographers = listOf(photographer(1L), photographer(2L)),
                                        page = 0,
                                        hasNext = true,
                                    ),
                                ),
                            1 to
                                Result.success(
                                    PhotographerPage(
                                        photographers = listOf(photographer(2L), photographer(3L)),
                                        page = 1,
                                        hasNext = false,
                                    ),
                                ),
                        ),
                )
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(location = LocationCoordinate(37.5, 127.0)),
                    photographerRepository = photographerRepository,
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()
            viewModel.handleIntent(MainIntent.LoadNextPage)
            advanceUntilIdle()

            assertEquals(listOf(1L, 2L, 3L), viewModel.state.value.homeItems.map { it.photographerId })
            assertEquals(listOf(0, 1), photographerRepository.requestedPages)
            assertFalse(viewModel.state.value.hasNextPage)
            assertEquals(2, viewModel.state.value.nextPage)
        }

    @Test
    fun `next page failure keeps existing items and exposes retry state`() =
        runTest {
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(location = LocationCoordinate(37.5, 127.0)),
                    photographerRepository =
                        FakePhotographerRepository(
                            searchResults =
                                mapOf(
                                    0 to
                                        Result.success(
                                            PhotographerPage(
                                                photographers = listOf(photographer(1L)),
                                                page = 0,
                                                hasNext = true,
                                            ),
                                        ),
                                    1 to Result.failure(IllegalStateException("network")),
                                ),
                        ),
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()
            viewModel.handleIntent(MainIntent.LoadNextPage)
            advanceUntilIdle()

            assertEquals(listOf(1L), viewModel.state.value.homeItems.map { it.photographerId })
            assertTrue(viewModel.state.value.loadMoreFailed)
            assertFalse(viewModel.state.value.isLoadingMore)
        }

    @Test
    fun `repeated next page intents request the page once while loading`() =
        runTest {
            val photographerRepository =
                FakePhotographerRepository(
                    searchResults =
                        mapOf(
                            0 to
                                Result.success(
                                    PhotographerPage(
                                        photographers = listOf(photographer(1L)),
                                        page = 0,
                                        hasNext = true,
                                    ),
                                ),
                            1 to
                                Result.success(
                                    PhotographerPage(
                                        photographers = listOf(photographer(2L)),
                                        page = 1,
                                        hasNext = false,
                                    ),
                                ),
                        ),
                )
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(location = LocationCoordinate(37.5, 127.0)),
                    photographerRepository = photographerRepository,
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()
            viewModel.handleIntent(MainIntent.LoadNextPage)
            viewModel.handleIntent(MainIntent.LoadNextPage)
            advanceUntilIdle()

            assertEquals(listOf(0, 1), photographerRepository.requestedPages)
        }

    @Test
    fun `search click emits search navigation side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(MainIntent.SearchClicked)
            advanceUntilIdle()

            assertEquals(MainSideEffect.NavigateToSearch, viewModel.sideEffect.first())
        }

    @Test
    fun `photographer click emits detail navigation side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(MainIntent.PhotographerClicked(7L))
            advanceUntilIdle()

            assertEquals(MainSideEffect.NavigateToPhotographerDetail(7L), viewModel.sideEffect.first())
        }

    private fun createViewModel(
        locationRepository: LocationRepository = FakeLocationRepository(),
        authRepository: AuthRepository = FakeAuthRepository(),
        memberRepository: MemberRepository = FakeMemberRepository(),
        photographerRepository: FakePhotographerRepository = FakePhotographerRepository(),
        portfolioRepository: PortfolioRepository = FakePortfolioRepository(),
    ): MainViewModel =
        MainViewModel(
            getCurrentLocationUseCase = GetCurrentLocationUseCase(locationRepository),
            getCurrentMemberIdUseCase = GetCurrentMemberIdUseCase(authRepository),
            updateMemberLocationUseCase = UpdateMemberLocationUseCase(memberRepository),
            searchPhotographersUseCase = SearchPhotographersUseCase(photographerRepository),
            getPhotographerPortfoliosUseCase = GetPhotographerPortfoliosUseCase(portfolioRepository),
            getPortfolioUseCase = GetPortfolioUseCase(portfolioRepository),
        )
}

private class FakeLocationRepository(
    private val location: LocationCoordinate? = null,
) : LocationRepository {
    override fun getCurrentLocation(
        onLocationReceived: (LocationCoordinate) -> Unit,
        onPermissionDenied: () -> Unit,
    ) {
        location?.let(onLocationReceived) ?: onPermissionDenied()
    }
}

private class FakePhotographerRepository(
    private val searchResults: Map<Int, AppResult<PhotographerPage>> =
        mapOf(0 to Result.success(PhotographerPage(emptyList(), page = 0, hasNext = false))),
) : PhotographerRepository,
    PhotographerSearchRepository {
    val requestedPages = mutableListOf<Int>()

    override suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): AppResult<FilteredPhotographers> = error("Not used")

    override suspend fun searchPhotographers(
        keyword: String,
        sortType: String,
        page: Int,
        size: Int,
    ): AppResult<PhotographerPage> {
        requestedPages += page
        return searchResults[page] ?: Result.failure(IllegalStateException("Missing page $page"))
    }

    override suspend fun getPhotographerDetail(
        photographerId: Long,
        reviewSort: String,
    ) = error("Not used")

    override suspend fun getPhotographerMoodKeywords(photographerId: Long) = error("Not used")

    override suspend fun addPhotoMood(photoMood: String) = error("Not used")

    override suspend fun deletePhotoMood(photoMood: String) = error("Not used")

    override suspend fun getActiveAreas(photographerId: Long) = error("Not used")

    override suspend fun updateActiveAreas(areas: List<com.hm.picplz.domain.model.Area>) = error("Not used")
}

private class FakeAuthRepository(
    private val memberId: Long? = 3L,
) : AuthRepository {
    override suspend fun loginWithKakao(context: Context): AppResult<KaKaoLoginResponse> = error("Not used")

    override suspend fun getKakaoUserInfo(): AppResult<KakaoUserInfo> = error("Not used")

    override suspend fun unlinkKakao(): AppResult<Unit> = error("Not used")

    override fun isKakaoTalkLoginAvailable(context: Context): Boolean = error("Not used")

    override fun getCurrentMemberId(): Long? = memberId
}

private class FakeMemberRepository(
    private val updateLocationResult: AppResult<Unit> = Result.success(Unit),
) : MemberRepository {
    override suspend fun checkNicknameAvailable(nickname: String) = error("Not used")

    override suspend fun getMemberProfile(memberId: Long) = error("Not used")

    override suspend fun updateMemberProfile(command: com.hm.picplz.domain.model.UpdateMemberProfileCommand) =
        error("Not used")

    override suspend fun updateMemberLocation(
        memberId: Long,
        location: LocationCoordinate,
    ): AppResult<Unit> = updateLocationResult
}

private class FakePortfolioRepository(
    private val results: Map<Long, AppResult<List<PortfolioSummary>>> = emptyMap(),
    private val detailResults: Map<Long, AppResult<PortfolioDetail>> = emptyMap(),
) : PortfolioRepository {
    val requestedPhotographerIds = mutableListOf<Long>()
    val requestedPortfolioIds = mutableListOf<Long>()

    override suspend fun getPhotographerPortfolios(
        photographerId: Long,
        page: Int,
        size: Int,
    ): AppResult<List<PortfolioSummary>> {
        requestedPhotographerIds += photographerId
        return results[photographerId] ?: Result.success(emptyList())
    }

    override suspend fun getPortfolio(portfolioId: Long): AppResult<PortfolioDetail> {
        requestedPortfolioIds += portfolioId
        return detailResults[portfolioId] ?: Result.failure(IllegalStateException("Missing portfolio $portfolioId"))
    }
}

private fun photographer(id: Long): Photographer =
    Photographer(
        id = id,
        name = "유가영 작가",
        profileImageUri = null,
        isActive = true,
        distance = 120,
        photoMoods = listOf("무드"),
        activeAreas = listOf("서울 강남구"),
    )
