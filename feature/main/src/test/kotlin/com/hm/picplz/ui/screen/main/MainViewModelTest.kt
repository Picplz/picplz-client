package com.hm.picplz.ui.screen.main

import android.content.Context
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo
import com.hm.picplz.domain.model.LocationCoordinate
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.model.PortfolioDetail
import com.hm.picplz.domain.model.PortfolioSummary
import com.hm.picplz.domain.repository.AuthRepository
import com.hm.picplz.domain.repository.LocationRepository
import com.hm.picplz.domain.repository.MemberRepository
import com.hm.picplz.domain.repository.PhotographerRepository
import com.hm.picplz.domain.repository.PortfolioRepository
import com.hm.picplz.domain.usecase.GetCurrentLocationUseCase
import com.hm.picplz.domain.usecase.GetCurrentMemberIdUseCase
import com.hm.picplz.domain.usecase.GetNearbyPhotographersUseCase
import com.hm.picplz.domain.usecase.GetPhotographerActiveAreasUseCase
import com.hm.picplz.domain.usecase.GetPhotographerPortfoliosUseCase
import com.hm.picplz.domain.usecase.GetPortfolioUseCase
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
    fun `permission granted loads first local chunk without eager card requests`() =
        runTest {
            val photographerRepository =
                FakePhotographerRepository(
                    nearbyResult =
                        Result.success(
                            FilteredPhotographers(active = (1L..8L).map(::photographer)),
                        ),
                )
            val portfolioRepository = FakePortfolioRepository()
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(LocationCoordinate(37.5, 127.0)),
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
            assertEquals(1, photographerRepository.nearbyRequestCount)
            assertTrue(portfolioRepository.requestedPhotographerIds.isEmpty())
            assertTrue(photographerRepository.requestedActiveAreaIds.isEmpty())
        }

    @Test
    fun `nearby failure exposes empty retry state`() =
        runTest {
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(LocationCoordinate(37.5, 127.0)),
                    photographerRepository =
                        FakePhotographerRepository(
                            nearbyResult = Result.failure(IllegalStateException("network")),
                        ),
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()

            assertFalse(viewModel.state.value.isLoading)
            assertTrue(viewModel.state.value.homeItems.isEmpty())
            assertEquals(MainLoadError.NearbyPhotographers, viewModel.state.value.errorMessage)
        }

    @Test
    fun `member location update failure does not block nearby feed`() =
        runTest {
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(LocationCoordinate(37.5, 127.0)),
                    memberRepository =
                        FakeMemberRepository(
                            updateLocationResult = Result.failure(IllegalStateException()),
                        ),
                    photographerRepository =
                        FakePhotographerRepository(
                            nearbyResult =
                                Result.success(
                                    FilteredPhotographers(active = listOf(photographer(1L))),
                                ),
                        ),
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()

            assertNull(viewModel.state.value.errorMessage)
            assertEquals("유가영", viewModel.state.value.homeItems.single().photographerName)
        }

    @Test
    fun `visible card loads activity area and keeps portfolio location separate`() =
        runTest {
            val photographerRepository =
                FakePhotographerRepository(
                    nearbyResult =
                        Result.success(
                            FilteredPhotographers(active = listOf(photographer(1L))),
                        ),
                    activeAreaResults =
                        mapOf(
                            1L to
                                Result.success(
                                    listOf(
                                        area(1L, "서울특별시 마포구 마포동"),
                                        area(2L, "서울특별시 동작구 동작동"),
                                    ),
                                ),
                        ),
                )
            val portfolioRepository =
                FakePortfolioRepository(
                    results =
                        mapOf(
                            1L to
                                Result.success(
                                    listOf(
                                        PortfolioSummary(
                                            id = 11L,
                                            representativeImage = "https://image/summary.jpg",
                                            photoCount = 2,
                                            location = "서울 마포구 와우산로",
                                            uploadDate = "2026-06-11",
                                        ),
                                    ),
                                ),
                        ),
                    detailResults =
                        mapOf(
                            11L to
                                Result.success(
                                    PortfolioDetail(
                                        id = 11L,
                                        imageUris =
                                            listOf(
                                                "https://image/detail-1.jpg",
                                                "https://image/detail-2.jpg",
                                            ),
                                        location = "서울 마포구 와우산로",
                                        uploadDate = "2026-07-21",
                                    ),
                                ),
                        ),
                )
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(LocationCoordinate(37.5, 127.0)),
                    photographerRepository = photographerRepository,
                    portfolioRepository = portfolioRepository,
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()
            viewModel.handleIntent(MainIntent.PortfolioVisible(photographerId = 1L))
            viewModel.handleIntent(MainIntent.PortfolioVisible(photographerId = 1L))
            advanceUntilIdle()

            val item = viewModel.state.value.homeItems.single()
            assertEquals("마포구 · 동작구", item.activeArea)
            assertEquals("서울 마포구 와우산로", item.location)
            assertEquals(listOf(1L), photographerRepository.requestedActiveAreaIds)
            assertEquals(listOf(1L), portfolioRepository.requestedPhotographerIds)
            assertEquals(listOf(11L), portfolioRepository.requestedPortfolioIds)
        }

    @Test
    fun `portfolio failure keeps visible photographer with no image`() =
        runTest {
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(LocationCoordinate(37.5, 127.0)),
                    photographerRepository =
                        FakePhotographerRepository(
                            nearbyResult =
                                Result.success(
                                    FilteredPhotographers(active = listOf(photographer(1L))),
                                ),
                        ),
                    portfolioRepository =
                        FakePortfolioRepository(
                            results = mapOf(1L to Result.failure(IllegalStateException("network"))),
                        ),
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()
            viewModel.handleIntent(MainIntent.PortfolioVisible(photographerId = 1L))
            advanceUntilIdle()

            assertTrue(viewModel.state.value.homeItems.single().portfolioImageUris.isEmpty())
        }

    @Test
    fun `next page reveals next local chunk without another nearby request`() =
        runTest {
            val photographerRepository =
                FakePhotographerRepository(
                    nearbyResult =
                        Result.success(
                            FilteredPhotographers(active = (1L..7L).map(::photographer)),
                        ),
                )
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(LocationCoordinate(37.5, 127.0)),
                    photographerRepository = photographerRepository,
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()
            viewModel.handleIntent(MainIntent.LoadNextPage)

            assertEquals((1L..7L).toList(), viewModel.state.value.homeItems.map { it.photographerId })
            assertEquals(1, photographerRepository.nearbyRequestCount)
            assertFalse(viewModel.state.value.hasNextPage)
            assertEquals(2, viewModel.state.value.nextPage)
        }

    @Test
    fun `repeated next page intents stop after all local photographers are visible`() =
        runTest {
            val photographerRepository =
                FakePhotographerRepository(
                    nearbyResult =
                        Result.success(
                            FilteredPhotographers(active = (1L..6L).map(::photographer)),
                        ),
                )
            val viewModel =
                createViewModel(
                    locationRepository = FakeLocationRepository(LocationCoordinate(37.5, 127.0)),
                    photographerRepository = photographerRepository,
                )

            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = true))
            advanceUntilIdle()
            viewModel.handleIntent(MainIntent.LoadNextPage)
            viewModel.handleIntent(MainIntent.LoadNextPage)

            assertEquals((1L..6L).toList(), viewModel.state.value.homeItems.map { it.photographerId })
            assertEquals(1, photographerRepository.nearbyRequestCount)
            assertFalse(viewModel.state.value.hasNextPage)
            assertFalse(viewModel.state.value.loadMoreFailed)
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

    @Test
    fun `report click emits unavailable side effect`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(MainIntent.ReportClicked)
            advanceUntilIdle()

            assertEquals(MainSideEffect.ShowReportUnavailable, viewModel.sideEffect.first())
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
            getNearbyPhotographersUseCase = GetNearbyPhotographersUseCase(photographerRepository),
            getPhotographerActiveAreasUseCase = GetPhotographerActiveAreasUseCase(photographerRepository),
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
    private val nearbyResult: AppResult<FilteredPhotographers> =
        Result.success(FilteredPhotographers()),
    private val activeAreaResults: Map<Long, AppResult<List<Area>>> = emptyMap(),
) : PhotographerRepository {
    var nearbyRequestCount = 0
    val requestedActiveAreaIds = mutableListOf<Long>()

    override suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): AppResult<FilteredPhotographers> {
        nearbyRequestCount += 1
        return nearbyResult
    }

    override suspend fun getPhotographerDetail(
        photographerId: Long,
        reviewSort: String,
    ) = error("Not used")

    override suspend fun getPhotographerMoodKeywords(photographerId: Long) = error("Not used")

    override suspend fun addPhotoMood(photoMood: String) = error("Not used")

    override suspend fun deletePhotoMood(photoMood: String) = error("Not used")

    override suspend fun getActiveAreas(photographerId: Long): AppResult<List<Area>> {
        requestedActiveAreaIds += photographerId
        return activeAreaResults[photographerId] ?: Result.success(emptyList())
    }

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
        activeAreas = emptyList(),
    )

private fun area(
    id: Long,
    name: String,
): Area =
    Area(
        id = id,
        name = name,
        dong = name,
        ri = null,
    )
