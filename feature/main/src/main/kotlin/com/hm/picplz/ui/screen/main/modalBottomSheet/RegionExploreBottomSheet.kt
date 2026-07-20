package com.hm.picplz.ui.screen.main.modalBottomSheet

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.main.R
import com.hm.picplz.ui.screen.common.CommonModalBottomSheet
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont

@OptIn(ExperimentalMaterial3Api::class)
internal fun canRegionExploreSheetTransitionTo(target: SheetValue): Boolean =
    when (target) {
        SheetValue.Hidden,
        SheetValue.PartiallyExpanded,
        SheetValue.Expanded,
        -> true
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionExploreBottomSheet(
    visible: Boolean,
    currentRegion: String = "",
    onDismiss: () -> Unit,
    onApply: (String) -> Unit = {},
) {
    val regionTabs = stringArrayResource(R.array.main_region_tabs).toList()
    val districtsByRegion =
        listOf(
            stringArrayResource(R.array.main_region_districts_seoul).toList(),
            stringArrayResource(R.array.main_region_districts_gyeonggi).toList(),
            stringArrayResource(R.array.main_region_districts_incheon).toList(),
            stringArrayResource(R.array.main_region_districts_busan).toList(),
            stringArrayResource(R.array.main_region_districts_jeju).toList(),
        )
    val allLabels = regionTabs.map { stringResource(R.string.main_region_all_format, it) }
    val defaultRegion = stringResource(R.string.main_region_all_seoul)
    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = ::canRegionExploreSheetTransitionTo,
        )
    var selectedTab by remember { mutableIntStateOf(0) }
    val region = regionTabs[selectedTab]
    val allLabel = allLabels[selectedTab]
    var selected by remember(defaultRegion) { mutableStateOf(defaultRegion) }

    LaunchedEffect(visible, currentRegion, regionTabs, allLabels) {
        if (visible) {
            val effectiveRegion = currentRegion.ifBlank { defaultRegion }
            val tab = regionTabs.indexOfFirst { effectiveRegion.startsWith(it) }.coerceAtLeast(0)
            selectedTab = tab
            val remainder = effectiveRegion.removePrefix("${regionTabs[tab]} ")
            selected = if (effectiveRegion == allLabels[tab] || remainder.isBlank()) allLabels[tab] else remainder
        }
    }

    val rows = listOf(allLabel) + districtsByRegion[selectedTab]
    val appliedRegion =
        if (selected == allLabel) {
            allLabel
        } else {
            stringResource(R.string.main_region_selection_format, region, selected)
        }

    CommonModalBottomSheet(
        visible = visible,
        visibleCloseButton = true,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(width = 40.dp, height = 4.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MainThemeColor.Gray2),
                )
            }
        },
        sheetMinHeight = 680.dp,
        sheetMaxHeight = 680.dp,
        expandToMaxHeight = false,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.main_region_explore_title),
                style = MainThemeFont.TitleSmall,
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp, bottom = 20.dp),
            )

            val density = LocalDensity.current
            val tabBounds = remember { mutableStateMapOf<Int, Pair<Dp, Dp>>() }
            val target = tabBounds[selectedTab]
            val indicatorLeft by animateDpAsState(targetValue = target?.first ?: 0.dp, label = "indicatorLeft")
            val indicatorWidth by animateDpAsState(targetValue = target?.second ?: 0.dp, label = "indicatorWidth")

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    regionTabs.forEachIndexed { index, title ->
                        val isSelected = index == selectedTab
                        Text(
                            text = title,
                            style = MainThemeFont.BodyBold,
                            color = if (isSelected) MainThemeColor.Black else MainThemeColor.Gray3,
                            maxLines = 1,
                            softWrap = false,
                            modifier =
                                Modifier
                                    .onGloballyPositioned { coords ->
                                        tabBounds[index] =
                                            with(density) {
                                                coords.positionInParent().x.toDp() to coords.size.width.toDp()
                                            }
                                    }
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                    ) {
                                        selectedTab = index
                                        selected = allLabels[index]
                                    },
                        )
                    }
                }
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = indicatorLeft)
                            .width(indicatorWidth)
                            .height(2.dp)
                            .background(MainThemeColor.Black),
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(rows) { row ->
                        val isSelected = row == selected
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .clickable { selected = row }
                                    .background(if (isSelected) MainThemeColor.Gray1 else MainThemeColor.White)
                                    .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = row,
                                style = if (row == allLabel) MainThemeFont.BodyBold else MainThemeFont.Body,
                                color = MainThemeColor.Gray5,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 45.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MainThemeColor.Black)
                            .clickable {
                                onApply(appliedRegion)
                                onDismiss()
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.main_region_apply),
                        style = MainThemeFont.BodyBold,
                        color = MainThemeColor.White,
                    )
                }
            }
        }
    }
}
