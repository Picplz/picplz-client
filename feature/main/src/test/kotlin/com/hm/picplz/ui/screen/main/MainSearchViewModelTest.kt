package com.hm.picplz.ui.screen.main

import android.content.Context
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.domain.model.FilteredPhotographers
import com.hm.picplz.domain.model.KaKaoLoginResponse
import com.hm.picplz.domain.model.KakaoUserInfo
import com.hm.picplz.domain.model.LocationCoordinate
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.domain.repository.AuthRepository
import com.hm.picplz.domain.repository.LocationRepository
import com.hm.picplz.domain.repository.MemberRepository
import com.hm.picplz.domain.repository.PhotographerRepository
import com.hm.picplz.domain.usecase.GetCurrentLocationUseCase
import com.hm.picplz.domain.usecase.GetCurrentMemberIdUseCase
import com.hm.picplz.domain.usecase.GetNearbyPhotographersUseCase
import com.hm.picplz.domain.usecase.UpdateMemberLocationUseCase
import com.hm.picplz.ui.screen.photographer_main.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainSearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `query change keeps typing state without area lookup`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(MainSearchIntent.FocusChanged(true))
            viewModel.handleIntent(MainSearchIntent.QueryChanged("강"))
            advanceUntilIdle()

            assertEquals("강", viewModel.state.value.query)
            assertEquals(SearchUiState.Typing, viewModel.state.value.uiState)
        }

    @Test
    fun `blank query change keeps empty state`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(MainSearchIntent.FocusChanged(true))
            viewModel.handleIntent(MainSearchIntent.QueryChanged(""))
            advanceUntilIdle()

            assertEquals(SearchUiState.Empty, viewModel.state.value.uiState)
        }

    @Test
    fun `search submit loads nearby photographers and filters results`() =
        runTest {
            val viewModel =
                createViewModel(
                    locationRepository = FakeSearchLocationRepository(location = LocationCoordinate(37.5, 127.0)),
                    photographerRepository =
                        FakeSearchPhotographerRepository(
                            nearbyResult =
                                Result.success(
                                    FilteredPhotographers(
                                        active =
                                            listOf(
                                                Photographer(
                                                    id = 1L,
                                                    name = "유가영 작가",
                                                    profileImageUri = null,
                                                    isActive = true,
                                                    distance = 100,
                                                    photoMoods = listOf("필름"),
                                                    activeAreas = listOf("서울 강남구 도곡동"),
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                )

            viewModel.handleIntent(MainSearchIntent.SearchSubmitted("강남"))
            advanceUntilIdle()

            assertEquals(SearchUiState.Complete(query = "강남", hasResults = true), viewModel.state.value.uiState)
            assertEquals("유가영 작가", viewModel.state.value.results.single().name)
        }

    @Test
    fun `search submit continues nearby lookup when member location update fails`() =
        runTest {
            val viewModel =
                createViewModel(
                    locationRepository = FakeSearchLocationRepository(location = LocationCoordinate(37.5, 127.0)),
                    memberRepository =
                        FakeSearchMemberRepository(
                            updateLocationResult = Result.failure(IllegalStateException()),
                        ),
                    photographerRepository =
                        FakeSearchPhotographerRepository(
                            nearbyResult =
                                Result.success(
                                    FilteredPhotographers(
                                        active =
                                            listOf(
                                                Photographer(
                                                    id = 1L,
                                                    name = "유가영 작가",
                                                    profileImageUri = null,
                                                    isActive = true,
                                                    distance = 100,
                                                    photoMoods = listOf("필름"),
                                                    activeAreas = listOf("서울 강남구 도곡동"),
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                )

            viewModel.handleIntent(MainSearchIntent.SearchSubmitted("강남"))
            advanceUntilIdle()

            assertEquals(SearchUiState.Complete(query = "강남", hasResults = true), viewModel.state.value.uiState)
            assertEquals("유가영 작가", viewModel.state.value.results.single().name)
        }

    private fun createViewModel(
        locationRepository: LocationRepository = FakeSearchLocationRepository(),
        authRepository: AuthRepository = FakeSearchAuthRepository(),
        memberRepository: MemberRepository = FakeSearchMemberRepository(),
        photographerRepository: PhotographerRepository = FakeSearchPhotographerRepository(),
    ): MainSearchViewModel =
        MainSearchViewModel(
            getCurrentLocationUseCase = GetCurrentLocationUseCase(locationRepository),
            getCurrentMemberIdUseCase = GetCurrentMemberIdUseCase(authRepository),
            updateMemberLocationUseCase = UpdateMemberLocationUseCase(memberRepository),
            getNearbyPhotographersUseCase = GetNearbyPhotographersUseCase(photographerRepository),
        )
}

private class FakeSearchLocationRepository(
    private val location: LocationCoordinate? = null,
) : LocationRepository {
    override fun getCurrentLocation(
        onLocationReceived: (LocationCoordinate) -> Unit,
        onPermissionDenied: () -> Unit,
    ) {
        location?.let(onLocationReceived) ?: onPermissionDenied()
    }
}

private class FakeSearchPhotographerRepository(
    private val nearbyResult: AppResult<FilteredPhotographers> = Result.success(FilteredPhotographers()),
) : PhotographerRepository {
    override suspend fun getNearbyPhotographers(
        longitude: Double,
        latitude: Double,
        distance: Long,
    ): AppResult<FilteredPhotographers> = nearbyResult

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

private class FakeSearchAuthRepository(
    private val memberId: Long? = 3L,
) : AuthRepository {
    override suspend fun loginWithKakao(context: Context): AppResult<KaKaoLoginResponse> = error("Not used")

    override suspend fun getKakaoUserInfo(): AppResult<KakaoUserInfo> = error("Not used")

    override suspend fun unlinkKakao(): AppResult<Unit> = error("Not used")

    override fun isKakaoTalkLoginAvailable(context: Context): Boolean = error("Not used")

    override fun getCurrentMemberId(): Long? = memberId
}

private class FakeSearchMemberRepository(
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
