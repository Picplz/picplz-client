package com.hm.picplz.ui.screen.photographer_reject_reservation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonButtonModal
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.photographer_reject_reservation.composable.PhotographerRejectReasonContent
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

/** 하단 버튼(높이 + 바깥 여백)을 피해 토스트를 띄우기 위한 오프셋 */
private val toastBottomOffset = 120.dp

@Composable
fun PhotographerRejectReservationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChat: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PhotographerRejectReservationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.handleIntent(PhotographerRejectReservationIntent.OnBackClick)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                PhotographerRejectReservationSideEffect.NavigateBack -> onNavigateBack()
                PhotographerRejectReservationSideEffect.NavigateToChat -> onNavigateToChat()
            }
        }
    }

    PhotographerRejectReservationScreenContent(
        modifier = modifier,
        state = state,
        onBackClick = { viewModel.handleIntent(PhotographerRejectReservationIntent.OnBackClick) },
        onReasonSelect = { reason ->
            viewModel.handleIntent(PhotographerRejectReservationIntent.SelectReason(reason))
        },
        onDirectInputChange = { text ->
            viewModel.handleIntent(PhotographerRejectReservationIntent.UpdateDirectInput(text))
        },
        onNextClick = { viewModel.handleIntent(PhotographerRejectReservationIntent.OnNextClick) },
        onDialogConfirm = { viewModel.handleIntent(PhotographerRejectReservationIntent.OnConfirmDialogConfirm) },
        onDialogDismiss = { viewModel.handleIntent(PhotographerRejectReservationIntent.OnConfirmDialogDismiss) },
        onToastDismiss = { viewModel.handleIntent(PhotographerRejectReservationIntent.OnToastDismiss) },
    )
}

@Suppress("LongParameterList")
@Composable
private fun PhotographerRejectReservationScreenContent(
    state: PhotographerRejectReservationState,
    onBackClick: () -> Unit,
    onReasonSelect: (PhotographerRejectReason) -> Unit,
    onDirectInputChange: (String) -> Unit,
    onNextClick: () -> Unit,
    onDialogConfirm: () -> Unit,
    onDialogDismiss: () -> Unit,
    onToastDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MainThemeColor.White,
        topBar = {
            CommonTopBar(
                text = stringResource(R.string.reservation_reject),
                onClickBack = onBackClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            PhotographerRejectReasonContent(
                modifier = Modifier.weight(1f),
                selectedReason = state.selectedReason,
                directInputText = state.directInputText,
                onReasonSelect = onReasonSelect,
                onDirectInputChange = onDirectInputChange,
            )

            CommonBottomButton(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 48.dp),
                text = stringResource(R.string.reject_button_next),
                onClick = onNextClick,
                enabled = state.isNextEnabled(),
            )
        }

        if (state.showConfirmDialog) {
            PhotographerRejectConfirmDialog(
                onConfirm = onDialogConfirm,
                onDismiss = onDialogDismiss,
            )
        }

        // CommonToast는 항상 컴포즈해 두고 isVisible만 토글합니다(퇴장 애니메이션 유지).
        CommonToast(
            modifier = Modifier.padding(innerPadding),
            message = state.toastMessageResId?.let { stringResource(it) }.orEmpty(),
            isVisible = state.showToast,
            onDismiss = onToastDismiss,
            // 기본 오프셋(50dp)은 하단 버튼과 겹치므로 버튼 위로 띄웁니다.
            bottomOffset = toastBottomOffset,
        )
    }
}

@Composable
private fun PhotographerRejectConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    CommonButtonModal(
        onDismissRequest = onDismiss,
        cancelText = stringResource(R.string.reject_dialog_button_no),
        confirmText = stringResource(R.string.reject_dialog_button_yes),
        onCancel = onDismiss,
        onConfirm = onConfirm,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.reject_dialog_desc),
                style = MainThemeFont.Body,
                color = MainThemeColor.Gray5,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun PhotographerRejectReservationReasonPreview() {
    PicplzTheme {
        PhotographerRejectReservationScreenContent(
            state =
                PhotographerRejectReservationState(
                    reservationId = 15L,
                    selectedReason = PhotographerRejectReason.AREA,
                ),
            onBackClick = {},
            onReasonSelect = {},
            onDirectInputChange = {},
            onNextClick = {},
            onDialogConfirm = {},
            onDialogDismiss = {},
            onToastDismiss = {},
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun PhotographerRejectReservationDialogPreview() {
    PicplzTheme {
        PhotographerRejectReservationScreenContent(
            state =
                PhotographerRejectReservationState(
                    reservationId = 15L,
                    selectedReason = PhotographerRejectReason.DIRECT_INPUT,
                    directInputText = "다른 지역 일정과 겹쳤어요",
                    showConfirmDialog = true,
                ),
            onBackClick = {},
            onReasonSelect = {},
            onDirectInputChange = {},
            onNextClick = {},
            onDialogConfirm = {},
            onDialogDismiss = {},
            onToastDismiss = {},
        )
    }
}
