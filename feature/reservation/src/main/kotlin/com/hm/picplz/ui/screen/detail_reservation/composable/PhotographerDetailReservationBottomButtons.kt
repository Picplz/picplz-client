@file:Suppress("UnusedPrivateMember")

package com.hm.picplz.ui.screen.detail_reservation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonBottomOutlinedButton
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus

/**
 * 작가 예약 상세 하단 버튼.
 * 고객 버튼과 구성이 다르므로 별도 관리하며, 현재는 기존 동작을 유지합니다.
 * (작가 피그마 확보 후 후속 이슈에서 정리)
 */
@Composable
fun PhotographerDetailReservationBottomButtons(
    currentReservationStatus: ReservationStatus,
    onChatClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (currentReservationStatus) {
        ReservationStatus.WAITING_APPROVAL,
        ReservationStatus.WAITING_SCHEDULE,
        -> {
            ChatButton(
                modifier = modifier,
                onClick = onChatClick,
            )
        }

        ReservationStatus.RESERVED -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                HistoryButton(
                    modifier = Modifier.weight(1f),
                    onClick = onHistoryClick,
                )

                ConfirmButton(
                    modifier = Modifier.weight(1f),
                    onClick = onConfirmClick,
                )
            }
        }

        ReservationStatus.COMPLETED -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                HistoryButton(
                    modifier = Modifier.weight(1f),
                    onClick = onHistoryClick,
                )

                ChatButton(
                    modifier = Modifier.weight(1f),
                    onClick = onChatClick,
                )
            }
        }
    }
}

@Composable
fun ReservationApproveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonBottomButton(
        modifier = modifier,
        text = stringResource(R.string.reservation_button_reservation_approve),
        onClick = onClick,
    )
}

@Composable
private fun ChatButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonBottomButton(
        modifier = modifier,
        text = stringResource(R.string.reservation_button_chat),
        onClick = onClick,
    )
}

@Composable
private fun HistoryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonBottomOutlinedButton(
        modifier = modifier,
        text = stringResource(R.string.reservation_button_history),
        onClick = onClick,
    )
}

@Composable
private fun ConfirmButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CommonBottomButton(
        modifier = modifier,
        text = stringResource(R.string.reservation_button_confirm),
        onClick = onClick,
    )
}

@Preview
@Composable
private fun PhotographerDetailReservationBottomButtonsReservedPreview() {
    PhotographerDetailReservationBottomButtons(
        currentReservationStatus = ReservationStatus.RESERVED,
        onChatClick = {},
        onHistoryClick = {},
        onConfirmClick = {},
    )
}

@Preview
@Composable
private fun PhotographerDetailReservationBottomButtonsCompletedPreview() {
    PhotographerDetailReservationBottomButtons(
        currentReservationStatus = ReservationStatus.COMPLETED,
        onChatClick = {},
        onHistoryClick = {},
        onConfirmClick = {},
    )
}
