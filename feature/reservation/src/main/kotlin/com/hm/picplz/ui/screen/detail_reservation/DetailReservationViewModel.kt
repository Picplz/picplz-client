package com.hm.picplz.ui.screen.detail_reservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hm.picplz.common.util.DateTimeUtil
import com.hm.picplz.navigation.model.DetailReservation
import com.hm.picplz.ui.screen.detail_reservation.model.RefundCondition
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 거래 완료 모달을 보여주고 리뷰 작성 화면으로 넘어가기까지의 시간 */
private const val DEAL_COMPLETE_MODAL_DURATION_MS = 1500L

@HiltViewModel
class DetailReservationViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val reservationId: Long = savedStateHandle.toRoute<DetailReservation>().reservationId

        private val _state = MutableStateFlow(DetailReservationState(reservationId = reservationId))
        val state: StateFlow<DetailReservationState> get() = _state

        private val _sideEffect = MutableSharedFlow<DetailReservationSideEffect>()
        val sideEffect: SharedFlow<DetailReservationSideEffect> get() = _sideEffect

        init {
            // TODO: 고객용 예약 상세 조회 API가 없어 아직 더미입니다.
            // `GET /reservations/{id}` 는 작가 전용이라 고객 토큰으로는 404가 떨어집니다.
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
                is DetailReservationIntent.NavigateToChat -> {
                    _state.update { it.copy(reservationStatus = it.reservationStatus.next()) }
                }

                is DetailReservationIntent.ConfirmReservation -> confirmReservation()

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
                                _sideEffect.emit(DetailReservationSideEffect.NavigateToCancelReservation)
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

        /**
         * "거래 완료하기" → 거래 완료 처리 → 완료 모달 → 리뷰 작성 화면.
         *
         * 리뷰를 쓰지 않고 나와도 거래는 이미 완료된 상태이므로,
         * 예약 상세에 다시 들어오면 "리뷰 쓰기" 버튼이 노출됩니다.
         */
        private fun confirmReservation() {
            // TODO: 거래 완료 API 연동 (성공 응답을 받은 뒤 아래 처리를 수행해야 합니다)
            _state.update {
                it.copy(
                    reservationStatus = ReservationStatus.COMPLETED,
                    showDealCompleteModal = true,
                )
            }

            viewModelScope.launch {
                delay(DEAL_COMPLETE_MODAL_DURATION_MS)
                _state.update { it.copy(showDealCompleteModal = false) }
                _sideEffect.emit(DetailReservationSideEffect.NavigateToWriteReview)
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
