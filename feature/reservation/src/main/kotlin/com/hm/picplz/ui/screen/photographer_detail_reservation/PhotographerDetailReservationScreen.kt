package com.hm.picplz.ui.screen.photographer_detail_reservation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.common.util.DateTimeUtil
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.detail_reservation.composable.DetailReservationMap
import com.hm.picplz.ui.screen.detail_reservation.composable.PhotographerDetailReservationBottomButtons
import com.hm.picplz.ui.screen.detail_reservation.composable.PhotographerReservationStatusHeader
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationApproveButton
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationInfoSection
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationProgressStepper
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun PhotographerDetailReservationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCancelReservation: (orderId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PhotographerDetailReservationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is PhotographerDetailReservationSideEffect.NavigateToPrev -> onNavigateBack()

                is PhotographerDetailReservationSideEffect.NavigateToCancelReservation ->
                    onNavigateToCancelReservation(sideEffect.orderId)
            }
        }
    }

    PhotographerDetailReservationScreen(
        modifier = modifier,
        state = state,
        onChatClick = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.NavigateToChat)
        },
        onDealCompleteClick = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.ConfirmReservation)
        },
        onCancelClick = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.NavigateToCancelReservation)
        },
        onCancelReject = {
            // TODO: 예약 거절 플로우 연결 (별도 작업)
        },
        onReservationApproveClick = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.ApproveReservation)
        },
        onCloseClick = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.NavigateBack)
        },
    )
}

@Suppress("LongParameterList")
@Composable
private fun PhotographerDetailReservationScreen(
    state: PhotographerDetailReservationState,
    onChatClick: () -> Unit,
    onDealCompleteClick: () -> Unit,
    onCancelClick: () -> Unit,
    onCancelReject: () -> Unit,
    onReservationApproveClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            DetailReservationMap(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                onCloseClick = onCloseClick,
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                item {
                    PhotographerReservationStatusHeader(
                        modifier = Modifier.padding(vertical = 20.dp),
                        currentReservationStatus = state.reservationStatus,
                        onCancelClick = onCancelClick,
                        onCancelReject = onCancelReject,
                    )
                }

                item {
                    ReservationProgressStepper(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                        currentReservationStep = state.reservationStatus.step,
                    )
                }

                item {
                    ReservationInfoSection(
                        modifier = Modifier.padding(top = 28.dp, bottom = 24.dp),
                        customerName = state.customerName,
                        shootingDateText = state.reservationStatus.shootingDateText(state.confirmedDateTimeMillis),
                    )
                }
            }

            if (state.reservationStatus != ReservationStatus.WAITING_APPROVAL) {
                PhotographerDetailReservationBottomButtons(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 48.dp),
                    currentReservationStatus = state.reservationStatus,
                    onChatClick = onChatClick,
                    onDealCompleteClick = onDealCompleteClick,
                )
            } else {
                ReservationApproveButton(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 48.dp),
                    onClick = onReservationApproveClick,
                )
            }
        }
    }
}

/**
 * 촬영 일시 표시 텍스트.
 * 일시 미확정(예약 대기) 단계는 "작가와 협의", 확정 이후(촬영 진행/거래 완료)는 확정된 일시를 표시합니다.
 */
@Composable
private fun ReservationStatus.shootingDateText(confirmedDateTimeMillis: Long): String =
    when (this) {
        ReservationStatus.RESERVED,
        ReservationStatus.COMPLETED,
        -> DateTimeUtil.getFormattedReservationDateTime(confirmedDateTimeMillis)

        ReservationStatus.WAITING_APPROVAL,
        ReservationStatus.WAITING_SCHEDULE,
        -> stringResource(R.string.reservation_schedule_tbd)
    }

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun PhotographerDetailReservationScreenPreview() {
    PhotographerDetailReservationScreen(
        state = PhotographerDetailReservationState(),
        onChatClick = {},
        onDealCompleteClick = {},
        onCancelClick = {},
        onCancelReject = {},
        onReservationApproveClick = {},
        onCloseClick = {},
    )
}
