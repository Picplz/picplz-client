package com.hm.picplz.ui.screen.cancel_reservation_confirm

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonBottomOutlinedButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography
import com.hm.picplz.core.ui.R as CoreR

@Composable
fun CancelReservationConfirmScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit,
    viewModel: CancelReservationConfirmViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                CancelReservationConfirmSideEffect.NavigateToHistory -> onNavigateBack()
                CancelReservationConfirmSideEffect.NavigateToHome -> onNavigateHome()
                CancelReservationConfirmSideEffect.NavigateToPrev -> onNavigateBack()
            }
        }
    }

    CancelReservationConfirmScreenContent(
        modifier = modifier,
        onNavigateBack = onNavigateBack,
        onHistoryClick = { viewModel.handleIntent(CancelReservationConfirmIntent.NavigateToHistory) },
        onHomeClick = { viewModel.handleIntent(CancelReservationConfirmIntent.NavigateToHome) },
    )
}

@Composable
private fun CancelReservationConfirmScreenContent(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onHistoryClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        containerColor = MainThemeColor.White,
        topBar = {
            CommonTopBar(
                text = stringResource(R.string.cancel_reservation_confirm_top_bar_title),
                onClickBack = onNavigateBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
        ) {
            CancelReservationCheckIcon(
                modifier = Modifier.padding(top = 80.dp, bottom = 24.dp),
            )

            CancelReservationTitle()

            CancelReservationDescription()

            Spacer(modifier = Modifier.weight(1f))

            CancelReservationGuide()

            CancelReservationButtons(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 64.dp, bottom = 48.dp),
                onHistoryClick = onHistoryClick,
                onHomeClick = onHomeClick,
            )
        }
    }
}

@Composable
private fun CancelReservationCheckIcon(modifier: Modifier = Modifier) {
    Image(
        modifier =
            modifier
                .fillMaxWidth(),
        painter = painterResource(CoreR.drawable.check_circle),
        contentDescription = "예약 취소 완료",
    )
}

@Composable
private fun CancelReservationTitle(modifier: Modifier = Modifier) {
    Text(
        modifier =
            modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
        text = stringResource(R.string.cancel_reservation_title),
        style = pretendardTypography.titleLarge,
        color = MainThemeColor.Black,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun CancelReservationDescription(modifier: Modifier = Modifier) {
    Text(
        modifier =
            modifier
                .fillMaxWidth(),
        text = stringResource(R.string.cancel_reservation_description),
        style = pretendardTypography.bodyLarge,
        color = MainThemeColor.Black,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun CancelReservationGuide(modifier: Modifier = Modifier) {
    Text(
        modifier =
            modifier
                .padding(horizontal = 16.dp),
        text = stringResource(R.string.cancel_reservation_guide),
        style = pretendardTypography.bodyMedium,
        color = MainThemeColor.Gray4,
    )
}

@Composable
private fun CancelReservationButtons(
    onHistoryClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        CommonBottomOutlinedButton(
            text = stringResource(R.string.cancel_reservation_button_history),
            onClick = onHistoryClick,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.padding(horizontal = 8.dp))

        CommonBottomButton(
            text = stringResource(R.string.cancel_reservation_button_home),
            onClick = onHomeClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun CancelReservationConfirmScreenPreview() {
    CancelReservationConfirmScreenContent(
        onNavigateBack = { },
        onHistoryClick = { },
        onHomeClick = { },
    )
}
