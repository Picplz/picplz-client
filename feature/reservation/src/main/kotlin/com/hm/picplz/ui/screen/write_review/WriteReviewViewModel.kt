package com.hm.picplz.ui.screen.write_review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
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

                WriteReviewIntent.OnRatingSubmitClick -> {
                    _state.update { it.copy(currentStep = Step.CONTENT) }
                }

                WriteReviewIntent.OnBackClick -> {
                    emitSideEffect(WriteReviewSideEffect.NavigateBack)
                }
            }
        }

        private fun emitSideEffect(sideEffect: WriteReviewSideEffect) {
            viewModelScope.launch {
                _sideEffect.emit(sideEffect)
            }
        }
    }
