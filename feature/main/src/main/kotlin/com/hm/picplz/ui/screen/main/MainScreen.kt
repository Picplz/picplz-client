package com.hm.picplz.ui.screen.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.screen.common.CommonLocationPermissionDeniedContent
import com.hm.picplz.ui.screen.common.CommonLocationPermissionRationale
import com.hm.picplz.ui.screen.main.modalBottomSheet.RegionExploreBottomSheet
import com.hm.picplz.ui.screen.main.search.SearchNavigateButton
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.core.ui.R as CoreR

private val homeReportButtonTextStyle =
    MainThemeFont.Caption.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = (-0.3).sp,
    )

private val homeLocationTextStyle =
    MainThemeFont.Caption.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        letterSpacing = 0.sp,
    )

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
                onLoadNextPage = { viewModel.handleIntent(MainIntent.LoadNextPage) },
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
    onLoadNextPage: () -> Unit,
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
                else ->
                    MainHomeFeed(
                        items = state.homeItems,
                        isLoadingMore = state.isLoadingMore,
                        hasNextPage = state.hasNextPage,
                        loadMoreFailed = state.loadMoreFailed,
                        onLoadNextPage = onLoadNextPage,
                        onPhotographerClick = onPhotographerClick,
                    )
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
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp),
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
    isLoadingMore: Boolean,
    hasNextPage: Boolean,
    loadMoreFailed: Boolean,
    onLoadNextPage: () -> Unit,
    onPhotographerClick: (Long) -> Unit,
) {
    val listState = rememberLazyListState()
    val shouldLoadNextPage by remember {
        derivedStateOf {
            val lastVisibleIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    ?: return@derivedStateOf false
            lastVisibleIndex >= listState.layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(shouldLoadNextPage, hasNextPage, isLoadingMore, loadMoreFailed) {
        if (shouldLoadNextPage && hasNextPage && !isLoadingMore && !loadMoreFailed) {
            onLoadNextPage()
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, top = 10.dp, end = 16.dp, bottom = 24.dp),
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

        if (isLoadingMore) {
            item(key = "home-feed-loading") {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = MainThemeColor.Black,
                    )
                }
            }
        }

        if (loadMoreFailed) {
            item(key = "home-feed-retry") {
                Text(
                    text = stringResource(R.string.main_feed_load_more_retry),
                    style = MainThemeFont.BodyBold,
                    color = MainThemeColor.Black,
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onLoadNextPage)
                            .padding(vertical = 16.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CustomerHomeCard(
    item: CustomerHomeItem,
    onClick: () -> Unit,
) {
    val portfolioImages: List<String?> =
        if (item.portfolioImageUris.isEmpty()) {
            listOf(null)
        } else {
            item.portfolioImageUris
        }
    val pagerState = rememberPagerState(pageCount = { portfolioImages.size })

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 10.dp),
        ) {
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
                        .size(30.dp)
                        .clip(CircleShape),
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                Text(
                    text = stringResource(R.string.main_photographer_name_format, item.photographerName),
                    style = MainThemeFont.ButtonDefault,
                    color = MainThemeColor.Gray5,
                )
                Text(
                    text = item.moodTags.take(2).joinToString(" · "),
                    style = MainThemeFont.Caption,
                    color = MainThemeColor.Gray4,
                )
            }
            CommonIconButton(
                label = stringResource(CoreR.string.report),
                horizontalPadding = 4.dp,
                verticalPadding = 1.dp,
                backgroundColor = MainThemeColor.Gray1,
                textColor = MainThemeColor.Gray3,
                textStyle = homeReportButtonTextStyle,
                borderRadius = 5.dp,
                modifier = Modifier.height(17.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .clip(RoundedCornerShape(2.dp)),
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = portfolioImages.size > 1,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                AsyncImage(
                    model = portfolioImages[page],
                    placeholder = painterResource(CoreR.drawable.logo),
                    error = painterResource(CoreR.drawable.logo),
                    contentDescription = stringResource(R.string.main_portfolio_image_content_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            if (item.photoCount > 1) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 12.dp, end = 12.dp)
                            .size(width = 36.dp, height = 24.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MainThemeColor.Black.copy(alpha = 0.6f))
                            .padding(horizontal = 7.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text =
                            stringResource(
                                R.string.main_portfolio_photo_count,
                                pagerState.currentPage + 1,
                                item.photoCount,
                            ),
                        style = MainThemeFont.Body.copy(lineHeight = 20.sp),
                        color = MainThemeColor.Gray2,
                        maxLines = 1,
                    )
                }
            }
        }

        if (portfolioImages.size > 1) {
            CustomerHomePortfolioIndicator(pagerState = pagerState)
        }

        Spacer(modifier = Modifier.height(22.dp))

        val location = item.location.ifBlank { stringResource(R.string.main_location_unknown) }
        val locationWithDate =
            item.uploadDate
                ?.takeIf { it.isNotBlank() }
                ?.let { date ->
                    stringResource(
                        R.string.main_location_with_date_format,
                        location,
                        date.toPortfolioDisplayDate(),
                    )
                }
                ?: location
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(CoreR.drawable.marker_map_gray),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = locationWithDate,
                style = homeLocationTextStyle,
                color = MainThemeColor.Gray3,
            )
        }
        if (item.distance > 0) {
            Text(
                text = stringResource(R.string.main_distance_format, item.distance),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray3,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = MainThemeColor.Gray2)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CustomerHomePortfolioIndicator(pagerState: PagerState) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
    ) {
        repeat(pagerState.pageCount) { page ->
            val dotSize by
                animateDpAsState(
                    targetValue =
                        portfolioIndicatorDotSize(
                            page = page,
                            currentPage = pagerState.currentPage,
                            pageCount = pagerState.pageCount,
                        ),
                    animationSpec = tween(durationMillis = 200),
                    label = "portfolioIndicatorDotSize",
                )
            val dotColor by
                animateColorAsState(
                    targetValue =
                        if (pagerState.currentPage == page) {
                            MainThemeColor.Black
                        } else {
                            MainThemeColor.Gray2
                        },
                    animationSpec = tween(durationMillis = 200),
                    label = "portfolioIndicatorDotColor",
                )
            Box(
                modifier =
                    Modifier
                        .size(6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(dotSize)
                            .clip(CircleShape)
                            .background(dotColor),
                )
            }
        }
    }
}

private fun portfolioIndicatorDotSize(
    page: Int,
    currentPage: Int,
    pageCount: Int,
) = when {
    currentPage == 0 && page <= 2 -> 6.dp
    currentPage == 0 && page == 3 -> 4.dp
    currentPage == pageCount - 1 && page >= pageCount - 3 -> 6.dp
    currentPage == pageCount - 1 && page == pageCount - 4 -> 4.dp
    page == currentPage -> 6.dp
    page == currentPage - 1 || page == currentPage + 1 -> 4.dp
    else -> 2.dp
}

private fun String.toPortfolioDisplayDate(): String {
    val dateParts = split("-")
    return if (dateParts.size == 3) dateParts.joinToString(". ") else this
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
                                photographerName = "유가영",
                                profileImageUri = null,
                                portfolioImageUris =
                                    listOf(
                                        "https://picsum.photos/seed/portfolio-preview-1/500/600",
                                        "https://picsum.photos/seed/portfolio-preview-2/500/600",
                                    ),
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
            onLoadNextPage = {},
            onSearchClick = {},
            onPhotographerClick = {},
            onDevEntryClick = {},
            onRegionSelected = {},
        )
    }
}
