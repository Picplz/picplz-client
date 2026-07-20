package com.hm.picplz.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hm.picplz.feature.main.R
import com.hm.picplz.ui.screen.common.CommonIconButton
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
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
internal fun CustomerHomeCard(
    item: CustomerHomeItem,
    onVisible: () -> Unit,
    onClick: () -> Unit,
    onReportClick: () -> Unit,
) {
    val portfolioImages: List<String?> =
        if (item.portfolioImageUris.isEmpty()) {
            listOf(null)
        } else {
            item.portfolioImageUris
        }
    val pagerState = rememberPagerState(pageCount = { portfolioImages.size })
    LaunchedEffect(item.photographerId) {
        onVisible()
    }

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
                onClick = onReportClick,
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
        Row(verticalAlignment = Alignment.CenterVertically) {
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

private fun String.toPortfolioDisplayDate(): String {
    val dateParts = split("-")
    return if (dateParts.size == 3) dateParts.joinToString(". ") else this
}
