package com.hm.picplz.ui.screen.detail_reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.common.util.DateTimeUtil
import com.hm.picplz.ui.screen.detail_reservation.model.RefundCondition
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailReservationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(DetailReservationState())
    val state: StateFlow<DetailReservationState> get() = _state

    private val _sideEffect = MutableSharedFlow<DetailReservationSideEffect>()
    val sideEffect: SharedFlow<DetailReservationSideEffect> get() = _sideEffect

    init {
        // TODO: API에서 촬영 일시 데이터를 받아와야 합니다.
        // 임시로 더미 데이터 설정 (촬영일: 4일 후)
        val currentTimeMillis = System.currentTimeMillis()
        val dummyShootingDateTimeMillis = DateTimeUtil.plusDays(currentTimeMillis, 4)
        val dummyConfirmedDateTimeMillis = currentTimeMillis - (12 * 60 * 60 * 1000) // 12시간 전

        _state.update {
            it.copy(
                shootingDateTimeMillis = dummyShootingDateTimeMillis,
                confirmedDateTimeMillis = dummyConfirmedDateTimeMillis,
            )
        }
    }

    fun handelIntent(intent: DetailReservationIntent) {
        when (intent) {
            // 상태 변경 확인 테스트를 위한 코드입니다.
            is DetailReservationIntent.NavigateToChat,
            is DetailReservationIntent.ConfirmReservation,
            -> {
                _state.update { it.copy(reservationStatus = it.reservationStatus.next()) }
            }

            is DetailReservationIntent.NavigateToWriteReview -> {
                viewModelScope.launch {
                    _sideEffect.emit(DetailReservationSideEffect.NavigateToWriteReview)
                }
            }

            is DetailReservationIntent.ShowCancelDialog -> {
                val currentState = _state.value
                val refundCondition =
                    RefundCondition.calculate(
                        currentMillis = System.currentTimeMillis(),
                        shootingMillis = currentState.shootingDateTimeMillis,
                        confirmedMillis = currentState.confirmedDateTimeMillis,
                    )

                _state.update {
                    it.copy(
                        showCancelDialog = true,
                        refundCondition = refundCondition,
                    )
                }
            }

            is DetailReservationIntent.DismissCancelDialog -> {
                _state.update { it.copy(showCancelDialog = false) }
            }

            is DetailReservationIntent.ConfirmCancel -> {
                _state.update { it.copy(showCancelDialog = false) }

                viewModelScope.launch {
                    when (state.value.reservationStatus) {
                        ReservationStatus.WAITING_APPROVAL,
                        ReservationStatus.WAITING_SCHEDULE,
                        -> {
                            _sideEffect.emit(DetailReservationSideEffect.NavigateToCancelReservationConfirm)
                        }

                        ReservationStatus.RESERVED,
                        ReservationStatus.COMPLETED,
                        -> {
                            _sideEffect.emit(DetailReservationSideEffect.NavigateToOrderDetail)
                        }
                    }
                }
            }

            is DetailReservationIntent.ShowRefundPolicyDialog -> {
                _state.update {
                    it.copy(
                        showRefundPolicyTooltip = true,
                        showCancelDialog = false,
                    )
                }
            }

            is DetailReservationIntent.DismissRefundPolicyTooltip -> {
                _state.update {
                    it.copy(
                        showRefundPolicyTooltip = false,
                        showCancelDialog = true,
                    )
                }
            }

            is DetailReservationIntent.NavigateBack -> {
                viewModelScope.launch {
                    _sideEffect.emit(DetailReservationSideEffect.NavigateToPrev)
                }
            }
        }
    }

    // 상태 변경 확인 테스트를 위한 코드입니다.
    private fun ReservationStatus.next(): ReservationStatus =
        when (this) {
            ReservationStatus.WAITING_APPROVAL -> ReservationStatus.WAITING_SCHEDULE
            ReservationStatus.WAITING_SCHEDULE -> ReservationStatus.RESERVED
            ReservationStatus.RESERVED -> ReservationStatus.COMPLETED
            ReservationStatus.COMPLETED -> ReservationStatus.COMPLETED
        }
}
