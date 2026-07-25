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
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

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
    )
}

@Composable
private fun CancelReservationScreenContent(
    state: CancelReservationState,
    onBackClick: () -> Unit,
    onReasonToggle: (CancelReason) -> Unit,
    onDirectInputChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
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
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun CancelReservationScreenPreview() {
    PicplzTheme {
        CancelReservationScreenContent(
            state = CancelReservationState(orderId = "order123"),
            onBackClick = {},
            onReasonToggle = {},
            onDirectInputChange = {},
            onSubmitClick = {},
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
                    orderId = "order123",
                    selectedReasons = setOf(CancelReason.SCHEDULE, CancelReason.DIRECT_INPUT),
                    directInputText = "개인적인 사유로 취소하게 되었습니다.",
                ),
            onBackClick = {},
            onReasonToggle = {},
            onDirectInputChange = {},
            onSubmitClick = {},
        )
    }
}
