package com.hm.picplz.ui.screen.quick_shoot

import android.util.Log
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.data.provider.TokenManager
import com.hm.picplz.data.service.KakaoMapService
import com.hm.picplz.data.service.LocationService
import com.hm.picplz.domain.model.Area
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.model.PhotographerDetail
import com.hm.picplz.domain.model.PhotographerInfo
import com.hm.picplz.domain.model.PhotographerReviewData
import com.hm.picplz.domain.model.PhotographerReviewSummary
import com.hm.picplz.domain.model.PortfolioDetail
import com.hm.picplz.domain.model.PortfolioSummary
import com.hm.picplz.domain.repository.PhotographerRepository
import com.hm.picplz.domain.repository.PortfolioRepository
import com.hm.picplz.domain.usecase.GetNearbyPhotographersUseCase
import com.hm.picplz.domain.usecase.GetPhotographerDetailUseCase
import com.hm.picplz.domain.usecase.GetPhotographerPortfoliosUseCase
import com.hm.picplz.domain.usecase.GetPortfolioUseCase
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockedStatic
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class QuickShootViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockedLog: MockedStatic<Log>

    @Before
    fun setUp() {
        mockedLog = mockStatic(Log::class.java)
    }

    @After
    fun tearDown() {
        mockedLog.close()
    }

    @Test
    fun `fetch nearby photographers uses current location and quick shoot radius`() =
        runTest {
            val repository =
                FakePhotographerRepository(
                    results = ArrayDeque(listOf(Result.success(nearbyPhotographers()))),
                )
            val viewModel = createViewModel(repository)

            viewModel.handleIntent(
                QuickShootIntent.SetCurrentLocation(
                    LatLng.from(TEST_LATITUDE, TEST_LONGITUDE),
                ),
            )
            viewModel.handleIntent(QuickShootIntent.FetchNearbyPhotographers)
            advanceUntilIdle()

            assertEquals(TEST_LONGITUDE, repository.lastLongitude ?: 0.0, 0.0)
            assertEquals(TEST_LATITUDE, repository.lastLatitude ?: 0.0, 0.0)
            assertEquals(2_000L, repository.lastDistance)
            assertFalse(viewModel.state.value.isSearchingPhotographer)
            assertFalse(viewModel.state.value.nearbyPhotographerLoadFailed)
            assertEquals(listOf(2L, 1L), viewModel.state.value.nearbyPhotographers.active.map { it.id })
        }

    @Test
    fun `failed nearby photographer request exposes retry state`() =
        runTest {
            val repository =
                FakePhotographerRepository(
                    results = ArrayDeque(listOf(Result.failure(IllegalStateException("network")))),
                )
            val viewModel = createViewModel(repository)

            viewModel.handleIntent(
                QuickShootIntent.SetCurrentLocation(
                    LatLng.from(TEST_LATITUDE, TEST_LONGITUDE),
                ),
            )
            viewModel.handleIntent(QuickShootIntent.FetchNearbyPhotographers)
            advanceUntilIdle()

            assertFalse(viewModel.state.value.isSearchingPhotographer)
            assertTrue(viewModel.state.value.nearbyPhotographerLoadFailed)
            assertTrue(viewModel.state.value.nearbyPhotographers.active.isEmpty())
        }

    @Test
    fun `duplicate nearby photographer request is ignored while loading`() =
        runTest {
            val repository =
                FakePhotographerRepository(
                    results = ArrayDeque(listOf(Result.success(nearbyPhotographers()))),
                )
            val viewModel = createViewModel(repository)
            viewModel.handleIntent(
                QuickShootIntent.SetCurrentLocation(
                    LatLng.from(TEST_LATITUDE, TEST_LONGITUDE),
                ),
            )

            viewModel.handleIntent(QuickShootIntent.FetchNearbyPhotographers)
            viewModel.handleIntent(QuickShootIntent.RefetchNearbyPhotographers)
            advanceUntilIdle()

            assertEquals(1, repository.callCount)
        }

    @Test
    fun `refetch clears failure after successful response`() =
        runTest {
            val repository =
                FakePhotographerRepository(
                    results =
                        ArrayDeque(
                            listOf(
                                Result.failure(IllegalStateException("network")),
                                Result.success(nearbyPhotographers()),
                            ),
                        ),
                )
            val viewModel = createViewModel(repository)
            viewModel.handleIntent(
                QuickShootIntent.SetCurrentLocation(
                    LatLng.from(TEST_LATITUDE, TEST_LONGITUDE),
                ),
            )

            viewModel.handleIntent(QuickShootIntent.FetchNearbyPhotographers)
            advanceUntilIdle()
            viewModel.handleIntent(QuickShootIntent.RefetchNearbyPhotographers)
            advanceUntilIdle()

            assertEquals(2, repository.callCount)
            assertFalse(viewModel.state.value.nearbyPhotographerLoadFailed)
            assertEquals(2, viewModel.state.value.nearbyPhotographers.active.size)
        }

    @Test
    fun `selecting a photographer shows loading then exposes api preview`() =
        runTest {
            val repository =
                FakePhotographerRepository(
                    results = ArrayDeque(listOf(Result.success(nearbyPhotographers()))),
                    detailResult = Result.success(photographerDetail()),
                )
            val viewModel = createViewModel(repository)
            viewModel.handleIntent(
                QuickShootIntent.SetCurrentLocation(
                    LatLng.from(TEST_LATITUDE, TEST_LONGITUDE),
                ),
            )
            viewModel.handleIntent(QuickShootIntent.FetchNearbyPhotographers)
            advanceUntilIdle()

            viewModel.handleIntent(QuickShootIntent.SetSelectedPhotographerId(1L))

            assertTrue(viewModel.state.value.isLoadingSelectedPhotographer)
            assertEquals(1L, viewModel.state.value.selectedPhotographerId)

            advanceUntilIdle()

            assertFalse(viewModel.state.value.isLoadingSelectedPhotographer)
            assertEquals(1, repository.detailCallCount)
            assertEquals("상세 작가", viewModel.state.value.selectedPhotographerPreview?.name)
            assertEquals(listOf("마포구"), viewModel.state.value.selectedPhotographerPreview?.activeAreas)
            assertEquals(
                listOf("one.jpg", "two.jpg", "three.jpg"),
                viewModel.state.value.selectedPhotographerPreview?.portfolioPhotos,
            )
        }

    private fun createViewModel(repository: PhotographerRepository): QuickShootViewModel {
        val tokenManager = mock(TokenManager::class.java)
        `when`(tokenManager.hasRequestedLocationPermission()).thenReturn(true)
        val portfolioRepository = FakePortfolioRepository()
        return QuickShootViewModel(
            getNearbyPhotographersUseCase = GetNearbyPhotographersUseCase(repository),
            getPhotographerDetailUseCase = GetPhotographerDetailUseCase(repository),
            getPhotographerPortfoliosUseCase = GetPhotographerPortfoliosUseCase(portfolioRepository),
            getPortfolioUseCase = GetPortfolioUseCase(portfolioRepository),
            locationService = mock(LocationService::class.java),
            kakaoMapService = mock(KakaoMapService::class.java),
            tokenManager = tokenManager,
        )
    }
}

private class FakePortfolioRepository : PortfolioRepository {
    override suspend fun getPhotographerPortfolios(
        photographerId: Long,
        page: Int,
        size: Int,
    ): AppResult<List<PortfolioSummary>> =
        Result.success(
            listOf(
                PortfolioSummary(
                    id = 1L,
                    representativeImage = "cover.jpg",
                    photoCount = 4,
                    location = null,
                    uploadDate = null,
                ),
            ),
        )

    override suspend fun getPortfolio(portfolioId: Long): AppResult<PortfolioDetail> =
        Result.success(
            PortfolioDetail(
                id = portfolioId,
                imageUris = listOf("one.jpg", "two.jpg", "three.jpg", "four.jpg"),
                location = null,
                uploadDate = null,
            ),
        )
}

private class FakePhotographerRepository(
    private val results: ArrayDeque<AppResult<FilteredPhotographers>>,
    private val detailResult: AppResult<PhotographerDetail> = Result.failure(NotImplementedError()),
) : PhotographerRepository {
    var callCount: Int = 0
        private set
    var lastLongitude: Double? = null
        private set
    var lastLatitude: Double? = null
        private set
    var lastDistance: Long? = null
        private set
    var detailCallCount: Int = 0
        private set

    override suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): AppResult<FilteredPhotographers> {
        callCount += 1
        lastLongitude = longitude
        lastLatitude = latitude
        lastDistance = distance
        return results.removeFirst()
    }

    override suspend fun getPhotographerDetail(
        photographerId: Long,
        reviewSort: String,
    ): AppResult<PhotographerDetail> {
        detailCallCount += 1
        return detailResult
    }

    override suspend fun getPhotographerMoodKeywords(photographerId: Long): AppResult<List<String>> =
        Result.failure(NotImplementedError())

    override suspend fun addPhotoMood(photoMood: String): AppResult<Unit> = Result.failure(NotImplementedError())

    override suspend fun deletePhotoMood(photoMood: String): AppResult<Unit> = Result.failure(NotImplementedError())

    override suspend fun getActiveAreas(photographerId: Long): AppResult<List<Area>> =
        Result.failure(NotImplementedError())

    override suspend fun updateActiveAreas(areas: List<Area>): AppResult<List<Area>> =
        Result.failure(NotImplementedError())
}

private fun nearbyPhotographers() =
    FilteredPhotographers(
        active =
            listOf(
                photographer(id = 1L, distance = 300L),
                photographer(id = 2L, distance = 100L),
            ),
    )

private fun photographer(
    id: Long,
    distance: Long,
) = Photographer(
    id = id,
    name = "작가$id",
    profileImageUri = null,
    isActive = true,
    distance = distance,
    photoMoods = emptyList(),
)

private fun photographerDetail() =
    PhotographerDetail(
        profileInfo =
            PhotographerInfo(
                id = 1,
                name = "상세 작가",
                socialAccount = "@photographer",
                infoText = "",
                isActive = true,
                isFollow = false,
                followCount = 0,
                profileImageUri = "https://example.com/profile.jpg",
                workingArea = listOf("마포구"),
                keyword = listOf("캐주얼"),
                photoPortfolios = emptyList(),
            ),
        reviewData =
            PhotographerReviewData(
                summary =
                    PhotographerReviewSummary(
                        averageRating = 0f,
                        keywordBars = emptyList(),
                        totalReviewCount = 0,
                        totalPhotoReviewCount = 0,
                        photoReviews = emptyList(),
                    ),
                reviews = emptyList(),
            ),
        shootingPackages = emptyList(),
    )

private const val TEST_LATITUDE = 37.406960
private const val TEST_LONGITUDE = 127.115587
