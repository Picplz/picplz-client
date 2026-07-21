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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.ui.screen.detail_reservation.composable.DetailReservationMap
import com.hm.picplz.ui.screen.detail_reservation.composable.PhotographerDetailReservationBottomButtons
import com.hm.picplz.ui.screen.detail_reservation.composable.PhotographerReservationStatusHeader
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationApproveButton
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationCancelDialog
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationInfoSection
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationProgressStepper
import com.hm.picplz.ui.screen.detail_reservation.composable.ReservationRefundPolicyDialog
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import com.hm.picplz.ui.theme.MainThemeColor

@Suppress("LongParameterList")
@Composable
fun PhotographerDetailReservationScreen(
    onNavigateBack: () -> Unit,
    onNavigateRejectReservationConfirm: () -> Unit,
    onNavigateToOrderDetail: (orderId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PhotographerDetailReservationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is PhotographerDetailReservationSideEffect.NavigateToPrev -> onNavigateBack()

                is PhotographerDetailReservationSideEffect.NavigateToRejectReservationConfirm -> {
                    onNavigateRejectReservationConfirm()
                }

                is PhotographerDetailReservationSideEffect.NavigateToOrderDetail ->
                    onNavigateToOrderDetail(
                        state.orderId,
                    )
            }
        }
    }

    PhotographerDetailReservationScreen(
        modifier = modifier,
        state = state,
        onChatClick = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.NavigateToChat)
        },
        onHistoryClick = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.NavigateToHistory)
        },
        onConfirmClick = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.ConfirmReservation)
        },
        onCancelClick = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.ShowCancelDialog)
        },
        onCancelReject = {
            // TODO
        },
        onReservationApproveClick = {
            // TODO
        },
        onCancelDialogDismiss = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.DismissCancelDialog)
        },
        onCancelDialogConfirm = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.ConfirmCancel)
        },
        onInfoClick = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.ShowRefundPolicyDialog)
        },
        onRefundPolicyDismiss = {
            viewModel.handelIntent(PhotographerDetailReservationIntent.DismissRefundPolicyTooltip)
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
    onHistoryClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onCancelReject: () -> Unit,
    onReservationApproveClick: () -> Unit,
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
                    )
                }
            }

            if (state.reservationStatus != ReservationStatus.WAITING_APPROVAL) {
                PhotographerDetailReservationBottomButtons(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 48.dp),
                    currentReservationStatus = state.reservationStatus,
                    onChatClick = onChatClick,
                    onHistoryClick = onHistoryClick,
                    onConfirmClick = onConfirmClick,
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

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun PhotographerDetailReservationScreenPreview() {
    PhotographerDetailReservationScreen(
        state = PhotographerDetailReservationState(),
        onChatClick = {},
        onHistoryClick = {},
        onConfirmClick = {},
        onCancelClick = {},
        onCancelReject = {},
        onReservationApproveClick = {},
        onCancelDialogDismiss = {},
        onCancelDialogConfirm = {},
        onInfoClick = {},
        onRefundPolicyDismiss = {},
        onCloseClick = {},
    )
}
