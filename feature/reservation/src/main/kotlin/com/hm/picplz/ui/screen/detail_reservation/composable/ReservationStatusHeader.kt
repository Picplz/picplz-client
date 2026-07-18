@file:Suppress("UnusedPrivateMember")

package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus.Companion.showCancelButton
import com.hm.picplz.ui.theme.MainFontFamily.bodyBold
import com.hm.picplz.ui.theme.MainFontFamily.caption
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun ReservationStatusHeader(
    currentReservationStatus: ReservationStatus,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ReservationStatusInfo(
            title = stringResource(currentReservationStatus.customerTitleResId),
            description = stringResource(currentReservationStatus.customerDescriptionResId),
        )

        if (currentReservationStatus.showCancelButton()) {
            ReservationCancelButton(
                text = stringResource(R.string.reservation_cancel),
                onClick = onCancelClick,
            )
        }
    }
}

@Composable
fun PhotographerReservationStatusHeader(
    currentReservationStatus: ReservationStatus,
    onCancelClick: () -> Unit,
    onCancelReject: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ReservationStatusInfo(
            title = stringResource(currentReservationStatus.photographerTitleResId),
            description = stringResource(currentReservationStatus.photographerDescriptionResId),
        )

        when (currentReservationStatus) {
            ReservationStatus.WAITING_APPROVAL -> {
                ReservationCancelButton(
                    text = stringResource(R.string.reservation_reject),
                    onClick = onCancelReject,
                )
            }

            ReservationStatus.WAITING_SCHEDULE,
            ReservationStatus.RESERVED,
            -> {
                ReservationCancelButton(
                    text = stringResource(R.string.reservation_cancel),
                    onClick = onCancelClick,
                )
            }

            else -> {
                return
            }
        }
    }
}

@Composable
private fun ReservationStatusInfo(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = pretendardTypography.titleMedium,
            color = MainThemeColor.Black,
        )
        Text(
            text = description,
            style = caption,
            color = MainThemeColor.Green120,
        )
    }
}

@Composable
private fun ReservationCancelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides 32.dp,
        LocalViewConfiguration provides SmallTouchTargetViewConfiguration,
    ) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, MainThemeColor.Gray3),
            color = Color.Transparent,
            onClick = onClick,
        ) {
            Text(
                text = text,
                modifier =
                    Modifier
                        .padding(horizontal = 15.dp, vertical = 6.dp)
                        .heightIn(min = 20.dp)
                        .wrapContentHeight(Alignment.CenterVertically),
                style = bodyBold,
                color = MainThemeColor.Gray5,
            )
        }
    }
}

private val SmallTouchTargetViewConfiguration =
    CustomViewConfiguration(
        minimumTouchTargetSize = DpSize(32.dp, 32.dp),
    )

class CustomViewConfiguration(
    override val minimumTouchTargetSize: DpSize,
    override val longPressTimeoutMillis: Long = 400,
    override val doubleTapTimeoutMillis: Long = 300,
    override val doubleTapMinTimeMillis: Long = 40,
    override val touchSlop: Float = 8f,
) : ViewConfiguration

@Preview
@Composable
private fun ReservationStatusHeaderWaitingApprovalPreview() {
    ReservationStatusHeader(
        currentReservationStatus = ReservationStatus.WAITING_APPROVAL,
        onCancelClick = { },
    )
}

@Preview
@Composable
private fun ReservationStatusHeaderWaitingSchedulePreview() {
    ReservationStatusHeader(
        currentReservationStatus = ReservationStatus.WAITING_SCHEDULE,
        onCancelClick = { },
    )
}

@Preview
@Composable
private fun ReservationStatusHeaderReservedPreview() {
    ReservationStatusHeader(
        currentReservationStatus = ReservationStatus.RESERVED,
        onCancelClick = { },
    )
}

@Preview
@Composable
private fun ReservationStatusHeaderCompletedPreview() {
    ReservationStatusHeader(
        currentReservationStatus = ReservationStatus.COMPLETED,
        onCancelClick = { },
    )
}
