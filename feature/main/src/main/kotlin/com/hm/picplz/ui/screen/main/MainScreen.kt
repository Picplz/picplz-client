package com.hm.picplz.ui.screen.main

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.feature.main.R
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.navigation.model.Dev
import com.hm.picplz.navigation.model.MainSearch
import com.hm.picplz.ui.navigation.BottomNavigationBar
import com.hm.picplz.ui.screen.common.CommonLocationPermissionRationale
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val selectedRegion = state.selectedRegion.ifBlank { stringResource(R.string.main_region_all_seoul) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted =
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            viewModel.handleIntent(MainIntent.LocationPermissionResult(granted = granted))
        }

    LaunchedEffect(Unit) {
        val hasLocationPermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
        viewModel.handleIntent(MainIntent.EnterScreen(hasLocationPermission = hasLocationPermission))
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                MainSideEffect.RequestLocationPermission -> {
                    launcher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        ),
                    )
                }
                MainSideEffect.NavigateToSearch -> {
                    navController.navigate(MainSearch)
                }
                is MainSideEffect.NavigateToPhotographerDetail -> {
                    navController.navigate(DetailPhotographer(sideEffect.photographerId.toInt()))
                }
                MainSideEffect.ShowReportUnavailable -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.main_report_unavailable),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                MainSideEffect.NavigateToDev -> {
                    navController.navigate(Dev)
                }
            }
        }
    }

    if (!state.locationPermissionGranted && !state.hasRequestedPermission) {
        CommonLocationPermissionRationale(
            titlePrefix = stringResource(R.string.main_location_rationale_title_prefix),
            highlightedTitle = stringResource(R.string.main_location_rationale_title_permission),
            titleSuffix = stringResource(R.string.main_location_rationale_title_suffix),
            description = stringResource(R.string.main_location_rationale_description),
            buttonText = stringResource(R.string.main_location_rationale_button),
            iconContentDescription = stringResource(R.string.main_location_rationale_icon_desc),
            onNextClick = { viewModel.handleIntent(MainIntent.RequestLocationPermission) },
            modifier = modifier,
        )
    } else {
        MainContent(
            modifier = modifier,
            navController = navController,
            state = state.copy(selectedRegion = selectedRegion),
            actions =
                MainContentActions(
                    onRetry = { viewModel.handleIntent(MainIntent.RetryLoad) },
                    onLoadNextPage = { viewModel.handleIntent(MainIntent.LoadNextPage) },
                    onPortfolioVisible = { viewModel.handleIntent(MainIntent.PortfolioVisible(it)) },
                    onSearchClick = { viewModel.handleIntent(MainIntent.SearchClicked) },
                    onPhotographerClick = { viewModel.handleIntent(MainIntent.PhotographerClicked(it)) },
                    onReportClick = { viewModel.handleIntent(MainIntent.ReportClicked) },
                    onDevEntryClick = { viewModel.handleIntent(MainIntent.DevEntryClicked) },
                    onRegionSelected = { viewModel.handleIntent(MainIntent.RegionSelected(it)) },
                ),
        )
    }
}

private data class MainContentActions(
    val onRetry: () -> Unit,
    val onLoadNextPage: () -> Unit,
    val onPortfolioVisible: (Long) -> Unit,
    val onSearchClick: () -> Unit,
    val onPhotographerClick: (Long) -> Unit,
    val onReportClick: () -> Unit,
    val onDevEntryClick: () -> Unit,
    val onRegionSelected: (String) -> Unit,
)

@Composable
private fun MainContent(
    modifier: Modifier,
    navController: NavHostController,
    state: MainState,
    actions: MainContentActions,
) {
    Scaffold(
        containerColor = MainThemeColor.White,
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        modifier =
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            HomeSearchHeader(
                selectedRegion = state.selectedRegion,
                onSearchClick = actions.onSearchClick,
                onDevEntryClick = actions.onDevEntryClick,
                onRegionSelected = actions.onRegionSelected,
            )

            when {
                state.isLoading -> MainLoadingContent()
                !state.locationPermissionGranted -> MainPermissionDeniedContent(onRetry = actions.onRetry)
                state.homeItems.isEmpty() -> MainEmptyContent(onRetry = actions.onRetry)
                else ->
                    MainHomeFeed(
                        items = state.homeItems,
                        isLoadingMore = state.isLoadingMore,
                        hasNextPage = state.hasNextPage,
                        loadMoreFailed = state.loadMoreFailed,
                        onLoadNextPage = actions.onLoadNextPage,
                        onPortfolioVisible = actions.onPortfolioVisible,
                        onPhotographerClick = actions.onPhotographerClick,
                        onReportClick = actions.onReportClick,
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    PicplzTheme {
        MainContent(
            modifier = Modifier,
            navController = rememberNavController(),
            state =
                MainState(
                    locationPermissionGranted = true,
                    homeItems =
                        listOf(
                            CustomerHomeItem(
                                photographerId = 1L,
                                photographerName = "유가영",
                                profileImageUri = null,
                                activeArea = "마포구 · 동작구",
                                portfolioId = 1L,
                                portfolioImageUris =
                                    listOf(
                                        "https://picsum.photos/seed/portfolio-preview-1/500/600",
                                        "https://picsum.photos/seed/portfolio-preview-2/500/600",
                                    ),
                                location = "서울 마포구",
                                uploadDate = "2026-06-11",
                                photoCount = 4,
                                isActive = true,
                            ),
                        ),
                ),
            actions =
                MainContentActions(
                    onRetry = {},
                    onLoadNextPage = {},
                    onPortfolioVisible = {},
                    onSearchClick = {},
                    onPhotographerClick = {},
                    onReportClick = {},
                    onDevEntryClick = {},
                    onRegionSelected = {},
                ),
        )
    }
}
