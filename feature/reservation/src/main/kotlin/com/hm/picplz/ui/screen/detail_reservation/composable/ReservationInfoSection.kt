@file:Suppress("UnusedPrivateMember")

package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainFontFamily.bodyLarge
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont.Body

@Composable
fun ReservationInfoSection(
    modifier: Modifier = Modifier,
    customerName: String = "",
    shootingDateText: String = "작가와 협의",
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (customerName.isNotBlank()) {
            ReservationInfoItem(
                title = "고객명",
                description = customerName,
            )
        }
        ReservationInfoItem(
            title = "촬영 상품명",
            description = "프로필 패키지",
        )
        ReservationInfoItem(
            title = "촬영 장소",
            description = "서울특별시 종로구 효자로 3, 네번째 테이블 창문 앞",
        )
        ReservationInfoItem(
            title = "촬영 일시",
            description = shootingDateText,
        )
    }
}

@Composable
private fun ReservationInfoItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        ReservationInfoItemTitle(title = title)
        ReservationInfoItemDescription(description = description)
    }
}

@Composable
private fun ReservationInfoItemTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier,
        style = bodyLarge.copy(fontWeight = FontWeight.SemiBold),
        color = Color.Black,
    )
}

@Composable
private fun ReservationInfoItemDescription(
    description: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = description,
        modifier = modifier,
        style = Body,
        color = MainThemeColor.Gray5,
    )
}

@Preview
@Composable
private fun ReservationInfoItemPreview() {
    ReservationInfoSection()
}
