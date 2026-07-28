package com.hm.picplz.ui.screen.quick_shoot.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hm.picplz.domain.model.Photographer
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard
import com.hm.picplz.core.ui.R as CoreUiR
import com.hm.picplz.feature.photographer.R as PhotographerR

@Composable
fun PhotographerSheet(
    photographer: Photographer,
    onNavigateToDetail: (Long) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onNavigateToDetail(photographer.id)
                }
                .padding(bottom = 5.dp),
    ) {
        PhotographerIdentity(photographer)
        Spacer(modifier = Modifier.height(3.dp))
        PhotographerAvailability(photographer)

        val tags = photographer.photoMoods.take(1) + photographer.equipment.take(1)
        if (tags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(18.dp))
            VibeTags(tags = tags)
        }

        if (photographer.portfolioPhotos.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                photographer.portfolioPhotos.take(PREVIEW_PHOTO_COUNT).forEach { photoUrl ->
                    AsyncImage(
                        model = photoUrl,
                        contentDescription =
                            stringResource(
                                PhotographerR.string.quick_shoot_portfolio_image,
                            ),
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(5.dp)),
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotographerIdentity(photographer: Photographer) {
    Row(
        verticalAlignment = Alignment.Top,
    ) {
        AsyncImage(
            model = photographer.profileImageUri,
            contentDescription =
                stringResource(
                    PhotographerR.string.quick_shoot_photographer_profile_image,
                ),
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(1.dp, MainThemeColor.Gray2, CircleShape),
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text =
                stringResource(
                    CoreUiR.string.photographer_name_format,
                    photographer.name,
                ),
            style =
                TextStyle(
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 18.sp * 1.4,
                    letterSpacing = 0.sp,
                ),
            color = MainThemeColor.Black,
            maxLines = 1,
        )

        val instagramHandle = photographer.instagram
        if (!instagramHandle.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text =
                    stringResource(
                        PhotographerR.string.quick_shoot_instagram_format,
                        instagramHandle,
                    ),
                style =
                    TextStyle(
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        lineHeight = 12.sp * 1.4,
                        letterSpacing = 0.sp,
                    ),
                color = MainThemeColor.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PhotographerAvailability(photographer: Photographer) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (photographer.isActive) {
            ActiveStatusBadge(
                text = stringResource(PhotographerR.string.quick_shoot_active_status),
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        val remainingAreaCount = (photographer.activeAreas.size - DISPLAY_AREA_COUNT).coerceAtLeast(0)
        val overflowText =
            if (remainingAreaCount > 0) {
                stringResource(
                    PhotographerR.string.quick_shoot_area_overflow_format,
                    remainingAreaCount,
                )
            } else {
                null
            }
        val areasText = formatActiveAreas(photographer.activeAreas, overflowText)
        if (areasText.isNotEmpty()) {
            Text(
                text = areasText,
                style =
                    TextStyle(
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 14.sp * 1.4,
                        letterSpacing = 0.sp,
                    ),
                color = MainThemeColor.Gray4,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun formatActiveAreas(
    areas: List<String>,
    overflowText: String?,
): String {
    if (areas.isEmpty()) return ""
    val displayed = areas.take(DISPLAY_AREA_COUNT).joinToString(", ", transform = ::formatAreaName)
    return listOfNotNull(displayed, overflowText).joinToString(" ")
}

private fun formatAreaName(area: String): String {
    val segments = area.split(" ")
    return segments.firstOrNull { it.endsWith("구") }
        ?: segments.lastOrNull().orEmpty()
}

private const val DISPLAY_AREA_COUNT = 3
private const val PREVIEW_PHOTO_COUNT = 3
