package com.hm.picplz.ui.screen.photographer_cancel_reservation

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
import com.hm.picplz.ui.screen.cancel_reservation.composable.CheckboxWithLabel
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonButtonModal
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.photographer_cancel_reservation.PhotographerCancelReservationState.Step
import com.hm.picplz.ui.screen.photographer_cancel_reservation.composable.PhotographerCancelPolicyContent
import com.hm.picplz.ui.screen.photographer_cancel_reservation.composable.PhotographerCancelReasonContent
import com.hm.picplz.ui.screen.photographer_cancel_reservation.composable.PhotographerCancelStepIndicator
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.MainThemeFont
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun PhotographerCancelReservationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCancelConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PhotographerCancelReservationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.handleIntent(PhotographerCancelReservationIntent.OnBackClick)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                PhotographerCancelReservationSideEffect.NavigateBack -> onNavigateBack()
                PhotographerCancelReservationSideEffect.NavigateToCancelConfirm -> onNavigateToCancelConfirm()
            }
        }
    }

    PhotographerCancelReservationScreenContent(
        modifier = modifier,
        state = state,
        onBackClick = { viewModel.handleIntent(PhotographerCancelReservationIntent.OnBackClick) },
        onReasonToggle = { reason ->
            viewModel.handleIntent(PhotographerCancelReservationIntent.ToggleReason(reason))
        },
        onDirectInputChange = { text ->
            viewModel.handleIntent(PhotographerCancelReservationIntent.UpdateDirectInput(text))
        },
        onAgreedWithCustomerChange = { agreed ->
            viewModel.handleIntent(PhotographerCancelReservationIntent.SetAgreedWithCustomer(agreed))
        },
        onAgreedToPolicyChange = { agreed ->
            viewModel.handleIntent(PhotographerCancelReservationIntent.SetAgreedToPolicy(agreed))
        },
        onNextClick = { viewModel.handleIntent(PhotographerCancelReservationIntent.OnNextClick) },
        onSubmitClick = { viewModel.handleIntent(PhotographerCancelReservationIntent.OnSubmitClick) },
        onDialogConfirm = { viewModel.handleIntent(PhotographerCancelReservationIntent.OnConfirmDialogConfirm) },
        onDialogDismiss = { viewModel.handleIntent(PhotographerCancelReservationIntent.OnConfirmDialogDismiss) },
        onToastDismiss = { viewModel.handleIntent(PhotographerCancelReservationIntent.OnToastDismiss) },
    )
}

@Suppress("LongParameterList")
@Composable
private fun PhotographerCancelReservationScreenContent(
    state: PhotographerCancelReservationState,
    onBackClick: () -> Unit,
    onReasonToggle: (PhotographerCancelReason) -> Unit,
    onDirectInputChange: (String) -> Unit,
    onAgreedWithCustomerChange: (Boolean) -> Unit,
    onAgreedToPolicyChange: (Boolean) -> Unit,
    onNextClick: () -> Unit,
    onSubmitClick: () -> Unit,
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
                text = stringResource(R.string.photographer_cancel_reservation_top_bar_title),
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
            PhotographerCancelStepIndicator(
                currentStep = state.currentStep,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            )

            when (state.currentStep) {
                Step.REASON -> {
                    PhotographerCancelReasonContent(
                        modifier = Modifier.weight(1f),
                        selectedReasons = state.selectedReasons,
                        directInputText = state.directInputText,
                        onReasonToggle = onReasonToggle,
                        onDirectInputChange = onDirectInputChange,
                    )

                    CheckboxWithLabel(
                        text = stringResource(R.string.photographer_cancel_reason_agreement),
                        isSelected = state.agreedWithCustomer,
                        onToggle = { onAgreedWithCustomerChange(!state.agreedWithCustomer) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    )

                    CommonBottomButton(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 48.dp),
                        text = stringResource(R.string.photographer_cancel_reason_button_next),
                        onClick = onNextClick,
                        enabled = state.isReasonStepValid(),
                    )
                }

                Step.POLICY -> {
                    PhotographerCancelPolicyContent(
                        modifier = Modifier.weight(1f),
                    )

                    CheckboxWithLabel(
                        text = stringResource(R.string.photographer_cancel_policy_agreement),
                        isSelected = state.agreedToPolicy,
                        onToggle = { onAgreedToPolicyChange(!state.agreedToPolicy) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    )

                    CommonBottomButton(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 48.dp),
                        text = stringResource(R.string.photographer_cancel_policy_button_submit),
                        onClick = onSubmitClick,
                        enabled = state.isPolicyStepValid(),
                    )
                }
            }
        }

        if (state.showConfirmDialog) {
            PhotographerCancelConfirmDialog(
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
        )
    }
}

@Composable
private fun PhotographerCancelConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    CommonButtonModal(
        onDismissRequest = onDismiss,
        cancelText = stringResource(R.string.photographer_cancel_dialog_button_no),
        confirmText = stringResource(R.string.photographer_cancel_dialog_button_yes),
        onCancel = onDismiss,
        onConfirm = onConfirm,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.photographer_cancel_dialog_title),
                style = MainThemeFont.TitleSmall,
                color = MainThemeColor.Black,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.photographer_cancel_dialog_desc),
                style = MainThemeFont.Caption,
                color = MainThemeColor.Gray4,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun PhotographerCancelReservationReasonPreview() {
    PicplzTheme {
        PhotographerCancelReservationScreenContent(
            state =
                PhotographerCancelReservationState(
                    reservationId = 15L,
                    currentStep = Step.REASON,
                    selectedReasons = setOf(PhotographerCancelReason.SCHEDULE),
                ),
            onBackClick = {},
            onReasonToggle = {},
            onDirectInputChange = {},
            onAgreedWithCustomerChange = {},
            onAgreedToPolicyChange = {},
            onNextClick = {},
            onSubmitClick = {},
            onDialogConfirm = {},
            onDialogDismiss = {},
            onToastDismiss = {},
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun PhotographerCancelReservationPolicyPreview() {
    PicplzTheme {
        PhotographerCancelReservationScreenContent(
            state =
                PhotographerCancelReservationState(
                    reservationId = 15L,
                    currentStep = Step.POLICY,
                    agreedToPolicy = true,
                ),
            onBackClick = {},
            onReasonToggle = {},
            onDirectInputChange = {},
            onAgreedWithCustomerChange = {},
            onAgreedToPolicyChange = {},
            onNextClick = {},
            onSubmitClick = {},
            onDialogConfirm = {},
            onDialogDismiss = {},
            onToastDismiss = {},
        )
    }
}
