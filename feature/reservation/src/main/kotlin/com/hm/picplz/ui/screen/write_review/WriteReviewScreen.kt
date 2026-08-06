package com.hm.picplz.ui.screen.write_review

import androidx.activity.compose.BackHandler
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
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.write_review.WriteReviewState.Step
import com.hm.picplz.ui.screen.write_review.composable.WriteReviewRatingContent
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@Composable
fun WriteReviewScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WriteReviewViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.handleIntent(WriteReviewIntent.OnBackClick)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                WriteReviewSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    WriteReviewScreenContent(
        modifier = modifier,
        state = state,
        onBackClick = { viewModel.handleIntent(WriteReviewIntent.OnBackClick) },
        onRatingSelect = { rating ->
            viewModel.handleIntent(WriteReviewIntent.SelectRating(rating))
        },
        onNegativeFeedbackChange = { text ->
            viewModel.handleIntent(WriteReviewIntent.UpdateNegativeFeedback(text))
        },
        onRatingSubmitClick = { viewModel.handleIntent(WriteReviewIntent.OnRatingSubmitClick) },
    )
}

@Composable
private fun WriteReviewScreenContent(
    state: WriteReviewState,
    onBackClick: () -> Unit,
    onRatingSelect: (ReviewRating) -> Unit,
    onNegativeFeedbackChange: (String) -> Unit,
    onRatingSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MainThemeColor.White,
        topBar = {
            CommonTopBar(
                text = stringResource(R.string.write_review_top_bar_title),
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
            when (state.currentStep) {
                Step.RATING -> {
                    WriteReviewRatingContent(
                        modifier = Modifier.weight(1f),
                        customerNickname = state.customerNickname,
                        photographerName = state.photographerName,
                        selectedRating = state.selectedRating,
                        negativeFeedbackText = state.negativeFeedbackText,
                        onRatingSelect = onRatingSelect,
                        onNegativeFeedbackChange = onNegativeFeedbackChange,
                    )

                    CommonBottomButton(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 48.dp),
                        text = stringResource(R.string.write_review_rating_button_submit),
                        onClick = onRatingSubmitClick,
                        enabled = state.isRatingStepValid(),
                    )
                }

                // TODO(#214): 촬영 경험 입력(2/2) 단계
                Step.CONTENT -> Unit
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun WriteReviewScreenRatingEmptyPreview() {
    PicplzTheme {
        WriteReviewScreenContent(
            state =
                WriteReviewState(
                    orderId = "order123",
                    customerNickname = "세연",
                    photographerName = "유가영",
                ),
            onBackClick = {},
            onRatingSelect = {},
            onNegativeFeedbackChange = {},
            onRatingSubmitClick = {},
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun WriteReviewScreenRatingSelectedPreview() {
    PicplzTheme {
        WriteReviewScreenContent(
            state =
                WriteReviewState(
                    orderId = "order123",
                    customerNickname = "세연",
                    photographerName = "유가영",
                    selectedRating = ReviewRating.BAD,
                ),
            onBackClick = {},
            onRatingSelect = {},
            onNegativeFeedbackChange = {},
            onRatingSubmitClick = {},
        )
    }
}
