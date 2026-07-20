package com.hm.picplz.ui.screen.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.hm.picplz.feature.main.BuildConfig
import com.hm.picplz.feature.main.R
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.navigation.model.Dev
import com.hm.picplz.navigation.model.MainSearch
import com.hm.picplz.ui.navigation.BottomNavigationBar
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonLocationPermissionDeniedContent
import com.hm.picplz.ui.screen.common.CommonLocationPermissionRationale
import com.hm.picplz.ui.screen.main.modalBottomSheet.RegionExploreBottomSheet
import com.hm.picplz.ui.screen.main.search.SearchNavigateButton
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.core.ui.R as CoreR

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
                MainSideEffect.NavigateToDev -> {
                    navController.navigate(Dev)
                }
            }
        }
    }

    when {
        !state.locationPermissionGranted && !state.hasRequestedPermission -> {
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
        }

        else -> {
            MainContent(
                modifier = modifier,
                navController = navController,
                state = state.copy(selectedRegion = selectedRegion),
                onRetry = { viewModel.handleIntent(MainIntent.RetryLoad) },
                onSearchClick = { viewModel.handleIntent(MainIntent.SearchClicked) },
                onPhotographerClick = { viewModel.handleIntent(MainIntent.PhotographerClicked(it)) },
                onDevEntryClick = { viewModel.handleIntent(MainIntent.DevEntryClicked) },
                onRegionSelected = { viewModel.handleIntent(MainIntent.RegionSelected(it)) },
            )
        }
    }
}

@Composable
private fun MainContent(
    modifier: Modifier,
    navController: NavHostController,
    state: MainState,
    onRetry: () -> Unit,
    onSearchClick: () -> Unit,
    onPhotographerClick: (Long) -> Unit,
    onDevEntryClick: () -> Unit,
    onRegionSelected: (String) -> Unit,
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
                onSearchClick = onSearchClick,
                onDevEntryClick = onDevEntryClick,
                onRegionSelected = onRegionSelected,
            )

            when {
                state.isLoading -> MainLoadingContent()
                !state.locationPermissionGranted -> MainPermissionDeniedContent(onRetry = onRetry)
                state.homeItems.isEmpty() -> MainEmptyContent(onRetry = onRetry)
                else -> MainHomeFeed(items = state.homeItems, onPhotographerClick = onPhotographerClick)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeSearchHeader(
    selectedRegion: String,
    onSearchClick: () -> Unit,
    onDevEntryClick: () -> Unit,
    onRegionSelected: (String) -> Unit,
) {
    var showRegionSheet by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { showRegionSheet = true },
                        onLongClick = if (BuildConfig.DEBUG) onDevEntryClick else null,
                    ),
        ) {
            Text(
                text = selectedRegion,
                style = MainThemeFont.BodyBold,
                color = MainThemeColor.Black,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painter = painterResource(CoreR.drawable.region_arrow_down),
                contentDescription = stringResource(R.string.main_region_select_content_description),
                modifier =
                    Modifier
                        .size(width = 12.dp, height = 10.dp)
                        .rotate(if (showRegionSheet) 180f else 0f),
            )
        }

        SearchNavigateButton(
            placeholder = stringResource(R.string.main_search_placeholder),
            onClick = onSearchClick,
        )
    }

    RegionExploreBottomSheet(
        visible = showRegionSheet,
        currentRegion = selectedRegion,
        onDismiss = { showRegionSheet = false },
        onApply = onRegionSelected,
    )
}

@Composable
private fun MainHomeFeed(
    items: List<CustomerHomeItem>,
    onPhotographerClick: (Long) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(
            items = items,
            key = { it.photographerId },
        ) { item ->
            CustomerHomeCard(
                item = item,
                onClick = { onPhotographerClick(item.photographerId) },
            )
        }
    }
}

@Composable
private fun CustomerHomeCard(
    item: CustomerHomeItem,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.profileImageUri,
                placeholder = painterResource(CoreR.drawable.user_undefined),
                error = painterResource(CoreR.drawable.user_undefined),
                contentDescription =
                    stringResource(
                        R.string.main_photographer_profile_content_description,
                        item.photographerName,
                    ),
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(42.dp)
                        .clip(CircleShape),
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.photographerName,
                    style = MainThemeFont.TitleSmall,
                    color = MainThemeColor.Black,
                )
                Text(
                    text = item.moodTags.take(2).joinToString(" · "),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray4,
                )
            }
            Text(
                text =
                    if (item.isActive) {
                        stringResource(R.string.main_photographer_active)
                    } else {
                        stringResource(R.string.main_photographer_inactive)
                    },
                style = MainThemeFont.InnerTag,
                color = if (item.isActive) MainThemeColor.Green120 else MainThemeColor.Gray4,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        AsyncImage(
            model = item.portfolioImageUri,
            placeholder = painterResource(CoreR.drawable.logo),
            error = painterResource(CoreR.drawable.logo),
            contentDescription = stringResource(R.string.main_portfolio_image_content_description),
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .clip(RoundedCornerShape(2.dp)),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = item.location.ifBlank { stringResource(R.string.main_location_unknown) },
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
        )
        Text(
            text = stringResource(R.string.main_distance_format, item.distance),
            style = MainThemeFont.Caption,
            color = MainThemeColor.Gray3,
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = MainThemeColor.Gray2)
    }
}

@Composable
private fun MainLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MainThemeColor.Black)
    }
}

@Composable
private fun MainPermissionDeniedContent(onRetry: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 88.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CommonLocationPermissionDeniedContent(
            title = stringResource(R.string.main_location_denied_title),
            subtitle = stringResource(R.string.main_location_denied_subtitle),
            guidePrefix = stringResource(R.string.main_location_denied_guide_prefix),
            guideSuffix = stringResource(R.string.main_location_denied_guide_suffix),
            imageContentDescription = stringResource(R.string.main_location_denied_image_desc),
        )
        Spacer(modifier = Modifier.height(28.dp))
        CommonBottomButton(
            text = stringResource(R.string.main_retry_button),
            onClick = onRetry,
        )
    }
}

@Composable
private fun MainEmptyContent(onRetry: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.main_empty_title),
            style = MainThemeFont.TitleSmall,
            color = MainThemeColor.Black,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.main_empty_subtitle),
            style = MainThemeFont.Body,
            color = MainThemeColor.Gray4,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(28.dp))
        CommonBottomButton(
            text = stringResource(R.string.main_retry_button),
            onClick = onRetry,
        )
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
                                photographerName = "유가영 작가",
                                profileImageUri = null,
                                portfolioImageUri = null,
                                location = "서울 마포구",
                                uploadDate = "2026-06-11",
                                photoCount = 4,
                                isActive = true,
                                distance = 340,
                                moodTags = listOf("무드", "필름"),
                            ),
                        ),
                ),
            onRetry = {},
            onSearchClick = {},
            onPhotographerClick = {},
            onDevEntryClick = {},
            onRegionSelected = {},
        )
    }
}
