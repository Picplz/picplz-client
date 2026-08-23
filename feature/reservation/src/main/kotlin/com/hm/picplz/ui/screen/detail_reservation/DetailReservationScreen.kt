package com.hm.picplz.ui.screen.detail_reservation

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
import com.hm.picplz.ui.screen.common.CommonSuccessModal
import com.hm.picplz.ui.screen.detail_reservation.composable.DetailReservationBottomButtons
import com.hm.picplz.ui.screen.detail_reservation.composable.DetailReservationMap
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationCancelDialog
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationInfoSection
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationProgressStepper
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationRefundPolicyDialog
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationStatusHeader
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import com.hm.picplz.ui.theme.MainThemeColor

@Suppress("LongParameterList")
@Composable
fun DetailReservationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCancelReservation: (reservationId: Long) -> Unit,
    onNavigateToOrderDetail: (reservationId: Long) -> Unit,
    onNavigateToWriteReview: (reservationId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailReservationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is DetailReservationSideEffect.NavigateToPrev -> onNavigateBack()

                is DetailReservationSideEffect.NavigateToCancelReservation -> {
                    onNavigateToCancelReservation(state.reservationId)
                }

                is DetailReservationSideEffect.NavigateToOrderDetail -> onNavigateToOrderDetail(state.reservationId)

                is DetailReservationSideEffect.NavigateToWriteReview -> onNavigateToWriteReview(state.reservationId)
            }
        }
    }

    DetailReservationScreen(
        modifier = modifier,
        state = state,
        onChatClick = {
            viewModel.handelIntent(DetailReservationIntent.NavigateToChat)
        },
        onDealCompleteClick = {
            viewModel.handelIntent(DetailReservationIntent.ConfirmReservation)
        },
        onReviewClick = {
            viewModel.handelIntent(DetailReservationIntent.NavigateToWriteReview)
        },
        onCancelClick = {
            viewModel.handelIntent(DetailReservationIntent.ShowCancelDialog)
        },
        onCancelDialogDismiss = {
            viewModel.handelIntent(DetailReservationIntent.DismissCancelDialog)
        },
        onCancelDialogConfirm = {
            viewModel.handelIntent(DetailReservationIntent.ConfirmCancel)
        },
        onInfoClick = {
            viewModel.handelIntent(DetailReservationIntent.ShowRefundPolicyDialog)
        },
        onRefundPolicyDismiss = {
            viewModel.handelIntent(DetailReservationIntent.DismissRefundPolicyTooltip)
        },
        onCloseClick = {
            viewModel.handelIntent(DetailReservationIntent.NavigateBack)
        },
    )
}

@Suppress("LongParameterList")
@Composable
private fun DetailReservationScreen(
    state: DetailReservationState,
    onChatClick: () -> Unit,
    onDealCompleteClick: () -> Unit,
    onReviewClick: () -> Unit,
    onCancelClick: () -> Unit,
    onCancelDialogDismiss: () -> Unit,
    onCancelDialogConfirm: () -> Unit,
    onInfoClick: () -> Unit,
    onRefundPolicyDismiss: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MainThemeColor.White,
    ) { innerPadding ->
        if (state.showCancelDialog) {
            ReservationCancelDialog(
                status = state.reservationStatus,
                refundCondition = state.refundCondition,
                onDismiss = onCancelDialogDismiss,
                onCancel = onCancelDialogDismiss,
                onConfirm = onCancelDialogConfirm,
                onInfoClick = onInfoClick,
            )
        }

        if (state.showRefundPolicyTooltip) {
            ReservationRefundPolicyDialog(
                onDismissRequest = onRefundPolicyDismiss,
            )
        }

        if (state.showDealCompleteModal) {
            // 잠시 뒤 리뷰 작성 화면으로 자동 이동하므로 사용자가 닫지 않도록 합니다.
            CommonSuccessModal(
                message = stringResource(R.string.reservation_deal_complete_modal_message),
                onDismissRequest = {},
                dismissible = false,
            )
        }

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
                    ReservationStatusHeader(
                        modifier = Modifier.padding(vertical = 20.dp),
                        currentReservationStatus = state.reservationStatus,
                        onCancelClick = onCancelClick,
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
                        shootingDateText = state.reservationStatus.shootingDateText(state.confirmedDateTimeMillis),
                    )
                }
            }

            DetailReservationBottomButtons(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 48.dp),
                currentReservationStatus = state.reservationStatus,
                hasWrittenReview = state.hasWrittenReview,
                onChatClick = onChatClick,
                onDealCompleteClick = onDealCompleteClick,
                onReviewClick = onReviewClick,
            )
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
private fun DetailReservationScreenPreview() {
    DetailReservationScreen(
        state = DetailReservationState(),
        onChatClick = {},
        onDealCompleteClick = {},
        onReviewClick = {},
        onCancelClick = {},
        onCancelDialogDismiss = {},
        onCancelDialogConfirm = {},
        onInfoClick = {},
        onRefundPolicyDismiss = {},
        onCloseClick = {},
    )
}
