package com.hm.picplz.ui.screen.write_review

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
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
import com.hm.picplz.ui.screen.common.CommonDestructiveConfirmDialog
import com.hm.picplz.ui.screen.common.CommonToast
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.write_review.WriteReviewState.Step
import com.hm.picplz.ui.screen.write_review.composable.WriteReviewExperienceContent
import com.hm.picplz.ui.screen.write_review.composable.WriteReviewRatingContent
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

/** 하단 버튼(높이 + 바깥 여백)을 피해 토스트를 띄우기 위한 오프셋 */
private val toastBottomOffset = 120.dp

@Composable
fun WriteReviewScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReviewDetail: (reviewId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WriteReviewViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Android Photo Picker. 시스템 선택기를 쓰므로 저장소 권한이 필요 없습니다.
    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(REVIEW_PHOTO_MAX_COUNT),
        ) { uris ->
            if (uris.isNotEmpty()) {
                viewModel.handleIntent(WriteReviewIntent.AddPhotos(uris.map(Uri::toString)))
            }
        }

    BackHandler {
        viewModel.handleIntent(WriteReviewIntent.OnBackClick)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                WriteReviewSideEffect.NavigateBack -> onNavigateBack()
                is WriteReviewSideEffect.NavigateToReviewDetail -> onNavigateToReviewDetail(sideEffect.reviewId)
                WriteReviewSideEffect.LaunchPhotoPicker -> {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                }
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
        onContentChange = { text ->
            viewModel.handleIntent(WriteReviewIntent.UpdateContent(text))
        },
        onAddPhotoClick = { viewModel.handleIntent(WriteReviewIntent.OnAddPhotoClick) },
        onRemovePhotoClick = { uri ->
            viewModel.handleIntent(WriteReviewIntent.RemovePhoto(uri))
        },
        onRatingSubmitClick = { viewModel.handleIntent(WriteReviewIntent.OnRatingSubmitClick) },
        onReviewSubmitClick = { viewModel.handleIntent(WriteReviewIntent.OnReviewSubmitClick) },
        onExitDialogConfirm = { viewModel.handleIntent(WriteReviewIntent.OnExitDialogConfirm) },
        onExitDialogDismiss = { viewModel.handleIntent(WriteReviewIntent.OnExitDialogDismiss) },
        onToastDismiss = { viewModel.handleIntent(WriteReviewIntent.DismissToast) },
    )
}

@Suppress("LongParameterList")
@Composable
private fun WriteReviewScreenContent(
    state: WriteReviewState,
    onBackClick: () -> Unit,
    onRatingSelect: (ReviewRating) -> Unit,
    onNegativeFeedbackChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onAddPhotoClick: () -> Unit,
    onRemovePhotoClick: (String) -> Unit,
    onRatingSubmitClick: () -> Unit,
    onReviewSubmitClick: () -> Unit,
    onExitDialogConfirm: () -> Unit,
    onExitDialogDismiss: () -> Unit,
    onToastDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = Modifier.fillMaxSize()) {
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

                        WriteReviewBottomButton(
                            text = stringResource(R.string.write_review_rating_button_submit),
                            onClick = onRatingSubmitClick,
                            enabled = state.isRatingStepValid(),
                        )
                    }

                    Step.CONTENT -> {
                        WriteReviewExperienceContent(
                            modifier = Modifier.weight(1f),
                            contentText = state.contentText,
                            photoUris = state.photoUris,
                            canAddPhoto = state.canAddPhoto(),
                            onContentChange = onContentChange,
                            onAddPhotoClick = onAddPhotoClick,
                            onRemovePhotoClick = onRemovePhotoClick,
                        )

                        WriteReviewBottomButton(
                            text = stringResource(R.string.write_review_content_button_submit),
                            onClick = onReviewSubmitClick,
                            enabled = state.isContentStepValid(),
                            // 최소 글자 수 미달이어도 눌러서 안내 토스트를 받을 수 있어야 합니다.
                            clickableWhenDisabled = true,
                        )
                    }
                }
            }
        }

        if (state.showExitDialog) {
            CommonDestructiveConfirmDialog(
                title = stringResource(R.string.write_review_exit_dialog_title),
                description = stringResource(R.string.write_review_exit_dialog_description),
                cancelText = stringResource(R.string.write_review_exit_dialog_cancel),
                confirmText = stringResource(R.string.write_review_exit_dialog_confirm),
                onDismissRequest = onExitDialogDismiss,
                onConfirm = onExitDialogConfirm,
            )
        }

        // CommonToast는 항상 컴포즈해 두고 isVisible만 토글합니다.
        // 조건부로 컴포즈하면 사라질 때 노드가 즉시 제거되어 퇴장 애니메이션이 재생되지 않고,
        // isVisible을 상수 true로 넘기면 최초 컴포지션부터 visible이라 진입 애니메이션도 생략됩니다.
        CommonToast(
            message =
                state.toastMessageResId
                    ?.let { stringResource(it, REVIEW_CONTENT_MIN_LENGTH) }
                    .orEmpty(),
            isVisible = state.showToast,
            onDismiss = onToastDismiss,
            // 기본 오프셋(50dp)은 하단 버튼과 겹치므로 버튼 위로 띄웁니다.
            bottomOffset = toastBottomOffset,
        )
    }
}

@Composable
private fun WriteReviewBottomButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    clickableWhenDisabled: Boolean = false,
) {
    CommonBottomButton(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 48.dp),
        text = text,
        onClick = onClick,
        enabled = enabled,
        clickableWhenDisabled = clickableWhenDisabled,
    )
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
            onContentChange = {},
            onAddPhotoClick = {},
            onRemovePhotoClick = {},
            onRatingSubmitClick = {},
            onReviewSubmitClick = {},
            onExitDialogConfirm = {},
            onExitDialogDismiss = {},
            onToastDismiss = {},
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun WriteReviewScreenContentStepPreview() {
    PicplzTheme {
        WriteReviewScreenContent(
            state =
                WriteReviewState(
                    orderId = "order123",
                    currentStep = Step.CONTENT,
                    selectedRating = ReviewRating.EXCELLENT,
                    contentText = "재밌었어요 사진도 잘찍으심",
                ),
            onBackClick = {},
            onRatingSelect = {},
            onNegativeFeedbackChange = {},
            onContentChange = {},
            onAddPhotoClick = {},
            onRemovePhotoClick = {},
            onRatingSubmitClick = {},
            onReviewSubmitClick = {},
            onExitDialogConfirm = {},
            onExitDialogDismiss = {},
            onToastDismiss = {},
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true)
@Composable
private fun WriteReviewScreenExitDialogPreview() {
    PicplzTheme {
        WriteReviewScreenContent(
            state =
                WriteReviewState(
                    orderId = "order123",
                    currentStep = Step.CONTENT,
                    selectedRating = ReviewRating.EXCELLENT,
                    contentText = "재밌었어요 사진도 잘찍으심",
                    showExitDialog = true,
                ),
            onBackClick = {},
            onRatingSelect = {},
            onNegativeFeedbackChange = {},
            onContentChange = {},
            onAddPhotoClick = {},
            onRemovePhotoClick = {},
            onRatingSubmitClick = {},
            onReviewSubmitClick = {},
            onExitDialogConfirm = {},
            onExitDialogDismiss = {},
            onToastDismiss = {},
        )
    }
}
