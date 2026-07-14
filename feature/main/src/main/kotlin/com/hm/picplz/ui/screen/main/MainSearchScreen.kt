package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hm.picplz.feature.main.R
import com.hm.picplz.navigation.model.DetailPhotographer
import com.hm.picplz.ui.screen.common.AreaTag
import com.hm.picplz.ui.screen.main.search.SearchField
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.core.ui.R as CoreR

@Composable
fun Header(
    navController: NavHostController,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
) {
    val canNavigateBack = navController.previousBackStackEntry != null

    Box(
        modifier =
            Modifier
                .zIndex(1f)
                .wrapContentSize(),
    ) {
        Column {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                        .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (canNavigateBack) {
                    Icon(
                        painter = painterResource(CoreR.drawable.triangle_left),
                        contentDescription = stringResource(R.string.main_search_back_content_description),
                        modifier =
                            Modifier
                                .size(18.dp)
                                .clickable { navController.popBackStack() },
                    )
                    Spacer(modifier = Modifier.width(17.dp))
                }
                SearchField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = stringResource(R.string.main_search_placeholder),
                    onSearchClick = onSearchClick,
                    keyboardActions = {
                        onSearchClick()
                    },
                    onFocusChanged = onFocusChanged,
                    autoFocus = true,
                )
            }
        }
    }
}

@Composable
fun RecentSearchSection(
    recentSearchQueries: List<String>,
    onRemove: (String) -> Unit,
    onClearAll: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    LaunchedEffect(recentSearchQueries.size) {
        if (recentSearchQueries.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    if (recentSearchQueries.isEmpty()) {
        return
    }

    Column(
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.main_search_recent_title),
                style = MainThemeFont.ButtonDefault,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.main_search_recent_clear_all),
                style = MainThemeFont.InnerTag,
                color = MainThemeColor.Gray4,
                textDecoration = TextDecoration.Underline,
                modifier =
                    Modifier.clickable {
                        focusManager.clearFocus()
                        onClearAll()
                    },
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (recentSearchQueries.isNotEmpty()) {
            LazyRow(
                state = listState,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(end = 16.dp),
            ) {
                items(
                    items = recentSearchQueries,
                    key = { it },
                ) { area ->
                    AreaTag(
                        label = area,
                        onRemove = {
                            focusManager.clearFocus()
                            onRemove(area)
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun PopularSpotSection(
    popularSpots: List<String>,
    onSpotClick: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.main_search_popular_spot_title),
                style = MainThemeFont.ButtonDefault,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.main_search_popular_spot_updated_at),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray3,
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        popularSpots.forEachIndexed { index, spot ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { onSpotClick(spot) }
                        .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${index + 1}",
                    style = MainThemeFont.ButtonDefault,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = spot,
                    style = MainThemeFont.Body,
                    color = MainThemeColor.Gray5,
                )
            }
        }
    }
}

@Composable
fun TypingResultSection(
    query: String,
    onSuggestionClick: (String) -> Unit,
) {
    val trimmedQuery = query.trim()
    if (trimmedQuery.isBlank()) return

    val photographerSuggestions =
        listOf(
            stringResource(R.string.main_search_suggestion_photographer_kang_jueun),
            stringResource(R.string.main_search_suggestion_photographer_dog),
            stringResource(R.string.main_search_suggestion_photographer_dog),
            stringResource(R.string.main_search_suggestion_photographer_dog),
            stringResource(R.string.main_search_suggestion_photographer_dog),
        ).filter { it.contains(trimmedQuery, ignoreCase = true) }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        itemsIndexed(photographerSuggestions) { index, photographer ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { onSuggestionClick(photographer) }
                        .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Image(
                    painter = painterResource(id = CoreR.drawable.user_undefined),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(20.dp)
                            .clip(CircleShape),
                )
                Text(
                    text = photographer,
                    style = MainThemeFont.BodyBold,
                )
            }
            if (index < photographerSuggestions.lastIndex) {
                HorizontalDivider(thickness = 1.dp, color = MainThemeColor.Gray2)
            }
        }
    }
}

@Composable
fun MainSearchScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    devMockResults: Boolean = false,
    viewModel: MainSearchViewModel = hiltViewModel(),
) {
    val viewModelState by viewModel.state.collectAsState()
    var devPreviewState by remember { mutableStateOf(devMainSearchPreviewState()) }
    val state = if (devMockResults) devPreviewState else viewModelState
    val focusManager = LocalFocusManager.current

    val doSearch: (String) -> Unit = { newQuery ->
        if (devMockResults) {
            devPreviewState =
                devPreviewState.copy(
                    query = newQuery.trim(),
                    isFocused = false,
                    hasSearched = true,
                    isLoading = false,
                    results = devSearchPreviewPhotographers(),
                )
        } else {
            viewModel.handleIntent(MainSearchIntent.SearchSubmitted(newQuery))
        }
        focusManager.clearFocus()
    }

    Scaffold(
        containerColor = MainThemeColor.White,
        modifier =
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
        ) {
            Header(
                navController = navController,
                query = state.query,
                onQueryChange = {
                    if (devMockResults) {
                        devPreviewState = devPreviewState.copy(query = it)
                    } else {
                        viewModel.handleIntent(MainSearchIntent.QueryChanged(it))
                    }
                },
                onSearchClick = { doSearch(state.query) },
                onFocusChanged = {
                    if (devMockResults) {
                        devPreviewState = devPreviewState.copy(isFocused = it)
                    } else {
                        viewModel.handleIntent(MainSearchIntent.FocusChanged(it))
                    }
                },
            )

            when (state.uiState) {
                is SearchUiState.Empty -> Unit

                is SearchUiState.Complete -> {
                    Spacer(modifier = Modifier.height(20.dp))

                    SearchResultSection(
                        results = state.results,
                        isLoading = state.isLoading,
                        selectedSortType = state.selectedSortType,
                        onSortSelected = { sortType ->
                            if (devMockResults) {
                                devPreviewState =
                                    MainSearchReducer.reduce(
                                        devPreviewState,
                                        MainSearchIntent.SortSelected(sortType),
                                    )
                            } else {
                                viewModel.handleIntent(MainSearchIntent.SortSelected(sortType))
                            }
                        },
                        onPhotographerClick = { item ->
                            item.id.toIntOrNull()?.let { photographerId ->
                                navController.navigate(DetailPhotographer(photographerId))
                            }
                        },
                    )
                }

                SearchUiState.Typing -> {
                    Spacer(modifier = Modifier.height(10.dp))

                    TypingResultSection(
                        query = state.query,
                        onSuggestionClick = { sug -> doSearch(sug) },
                    )
                }
            }
        }
    }
}
