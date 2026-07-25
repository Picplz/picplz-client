package com.hm.picplz.ui.screen.photographer_detail_reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.common.util.DateTimeUtil
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
class PhotographerDetailReservationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(PhotographerDetailReservationState())
    val state: StateFlow<PhotographerDetailReservationState> get() = _state

    private val _sideEffect = MutableSharedFlow<PhotographerDetailReservationSideEffect>()
    val sideEffect: SharedFlow<PhotographerDetailReservationSideEffect> get() = _sideEffect

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

    fun handelIntent(intent: PhotographerDetailReservationIntent) {
        when (intent) {
            // 상태 변경 확인 테스트를 위한 코드입니다.
            is PhotographerDetailReservationIntent.NavigateToChat,
            is PhotographerDetailReservationIntent.ApproveReservation,
            is PhotographerDetailReservationIntent.ConfirmReservation,
            -> {
                _state.update { it.copy(reservationStatus = it.reservationStatus.next()) }
            }

            is PhotographerDetailReservationIntent.NavigateToCancelReservation -> {
                viewModelScope.launch {
                    _sideEffect.emit(
                        PhotographerDetailReservationSideEffect.NavigateToCancelReservation(
                            orderId = _state.value.orderId,
                        ),
                    )
                }
            }

            is PhotographerDetailReservationIntent.NavigateBack -> {
                viewModelScope.launch {
                    _sideEffect.emit(PhotographerDetailReservationSideEffect.NavigateToPrev)
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
