package com.hm.picplz.ui.screen.write_review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.navigation.model.WriteReview
import com.hm.picplz.ui.screen.write_review.WriteReviewState.Step
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO(#217): 예약 정보 API 연동 시 실제 고객/작가 이름으로 교체
private const val DUMMY_CUSTOMER_NICKNAME = "세연"
private const val DUMMY_PHOTOGRAPHER_NAME = "유가영"

// TODO(#217): 리뷰 등록 API 응답의 reviewId로 교체
private const val DUMMY_REVIEW_ID = 1

@HiltViewModel
class WriteReviewViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val orderId: String = savedStateHandle.toRoute<WriteReview>().orderId

        private val _state =
            MutableStateFlow(
                WriteReviewState.idle(orderId).copy(
                    customerNickname = DUMMY_CUSTOMER_NICKNAME,
                    photographerName = DUMMY_PHOTOGRAPHER_NAME,
                ),
            )
        val state: StateFlow<WriteReviewState> = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<WriteReviewSideEffect>()
        val sideEffect: SharedFlow<WriteReviewSideEffect> = _sideEffect.asSharedFlow()

        fun handleIntent(intent: WriteReviewIntent) {
            when (intent) {
                is WriteReviewIntent.SelectRating -> {
                    _state.update { it.copy(selectedRating = intent.rating) }
                }

                is WriteReviewIntent.UpdateNegativeFeedback -> {
                    _state.update { it.copy(negativeFeedbackText = intent.text.take(NEGATIVE_FEEDBACK_MAX_LENGTH)) }
                }

                is WriteReviewIntent.UpdateContent -> {
                    _state.update { it.copy(contentText = intent.text.take(REVIEW_CONTENT_MAX_LENGTH)) }
                }

                WriteReviewIntent.OnRatingSubmitClick -> {
                    _state.update { it.copy(currentStep = Step.CONTENT) }
                }

                WriteReviewIntent.OnReviewSubmitClick -> submitReview()

                WriteReviewIntent.OnBackClick -> onBackClick()

                WriteReviewIntent.OnExitDialogConfirm -> {
                    _state.update { it.copy(showExitDialog = false) }
                    emitSideEffect(WriteReviewSideEffect.NavigateBack)
                }

                WriteReviewIntent.OnExitDialogDismiss -> {
                    _state.update { it.copy(showExitDialog = false) }
                }

                WriteReviewIntent.DismissToast -> {
                    _state.update { it.copy(toastMessageResId = null) }
                }
            }
        }

        /**
         * 촬영 경험이 최소 글자 수에 못 미치면 이동 대신 안내 토스트를 띄웁니다.
         * (버튼은 비활성 외형이지만 클릭은 받습니다)
         */
        private fun submitReview() {
            if (!_state.value.isContentStepValid()) {
                _state.update { it.copy(toastMessageResId = R.string.write_review_content_too_short) }
                return
            }

            // TODO(#217): 리뷰 등록 API 연동
            emitSideEffect(WriteReviewSideEffect.NavigateToReviewDetail(reviewId = DUMMY_REVIEW_ID))
        }

        /**
         * 촬영 경험 단계에서는 입력한 내용이 사라지므로 중단 여부를 먼저 확인합니다.
         * 별점 단계에서는 바로 이탈합니다.
         */
        private fun onBackClick() {
            when (_state.value.currentStep) {
                Step.RATING -> emitSideEffect(WriteReviewSideEffect.NavigateBack)
                Step.CONTENT -> _state.update { it.copy(showExitDialog = true) }
            }
        }

        private fun emitSideEffect(sideEffect: WriteReviewSideEffect) {
            viewModelScope.launch {
                _sideEffect.emit(sideEffect)
            }
        }
    }
