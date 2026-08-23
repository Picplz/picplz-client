package com.hm.picplz.ui.screen.cancel_reservation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.cancel_reservation.composable.CancelReasonInputContent
import com.hm.picplz.ui.screen.cancel_reservation.composable.CancelReservationTopBar
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

/** 하단 버튼(높이 + 바깥 여백)을 피해 토스트를 띄우기 위한 오프셋 */
private val toastBottomOffset = 120.dp

@Composable
fun CancelReservationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCancelConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CancelReservationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                CancelReservationSideEffect.NavigateBack -> onNavigateBack()
                CancelReservationSideEffect.NavigateToCancelReservationConfirm -> onNavigateToCancelConfirm()
            }
        }
    }

    CancelReservationScreenContent(
        modifier = modifier,
        state = state,
        onBackClick = { viewModel.handleIntent(CancelReservationIntent.OnBackClick) },
        onReasonToggle = { reason -> viewModel.handleIntent(CancelReservationIntent.ToggleReason(reason)) },
        onDirectInputChange = { text -> viewModel.handleIntent(CancelReservationIntent.UpdateDirectInput(text)) },
        onSubmitClick = { viewModel.handleIntent(CancelReservationIntent.OnSubmitClick) },
        onToastDismiss = { viewModel.handleIntent(CancelReservationIntent.OnToastDismiss) },
    )
}

@Composable
private fun CancelReservationScreenContent(
    state: CancelReservationState,
    onBackClick: () -> Unit,
    onReasonToggle: (CancelReason) -> Unit,
    onDirectInputChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onToastDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MainThemeColor.White,
        topBar = {
            CancelReservationTopBar(onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            CancelReasonInputContent(
                modifier = Modifier.weight(1f),
                selectedReasons = state.selectedReasons,
                directInputText = state.directInputText,
                onReasonToggle = onReasonToggle,
                onDirectInputChange = onDirectInputChange,
            )

            CommonBottomButton(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 48.dp),
                text = stringResource(R.string.cancel_reservation_button_submit),
                onClick = onSubmitClick,
                enabled = state.isSubmitButtonEnabled(),
            )
        }

        // CommonToast는 항상 컴포즈해 두고 isVisible만 토글합니다(퇴장 애니메이션 유지).
        // 내부에서 fillMaxSize + BottomCenter 정렬을 하므로 별도 Box 없이 형제로 둡니다.
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

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CancelReservationScreenPreview() {
    PicplzTheme {
        CancelReservationScreenContent(
            state = CancelReservationState(reservationId = 15L),
            onBackClick = {},
            onReasonToggle = {},
            onDirectInputChange = {},
            onSubmitClick = {},
            onToastDismiss = {},
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CancelReservationScreenDirectInputPreview() {
    PicplzTheme {
        CancelReservationScreenContent(
            state =
                CancelReservationState(
                    reservationId = 15L,
                    selectedReasons = setOf(CancelReason.SCHEDULE, CancelReason.DIRECT_INPUT),
                    directInputText = "개인적인 사유로 취소하게 되었습니다.",
                ),
            onBackClick = {},
            onReasonToggle = {},
            onDirectInputChange = {},
            onSubmitClick = {},
            onToastDismiss = {},
        )
    }
}
