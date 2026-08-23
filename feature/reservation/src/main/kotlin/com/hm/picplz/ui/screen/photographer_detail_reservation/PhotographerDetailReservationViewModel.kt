package com.hm.picplz.ui.screen.photographer_detail_reservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hm.picplz.data.service.ReservationService
import com.hm.picplz.domain.model.ReservationDetail
import com.hm.picplz.feature.reservation.R
import com.hm.picplz.navigation.model.PhotographerDetailReservation
import com.hm.picplz.ui.screen.detail_reservation.model.ReservationStatus
import com.hm.picplz.ui.screen.model.toUiStatusOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotographerDetailReservationViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val reservationService: ReservationService,
    ) : ViewModel() {
        private val reservationId: Long =
            savedStateHandle.toRoute<PhotographerDetailReservation>().reservationId

        private val _state = MutableStateFlow(PhotographerDetailReservationState(reservationId = reservationId))
        val state: StateFlow<PhotographerDetailReservationState> get() = _state

        private val _sideEffect = MutableSharedFlow<PhotographerDetailReservationSideEffect>()
        val sideEffect: SharedFlow<PhotographerDetailReservationSideEffect> get() = _sideEffect

        init {
            loadReservation()
        }

        fun handelIntent(intent: PhotographerDetailReservationIntent) {
            when (intent) {
                is PhotographerDetailReservationIntent.ApproveReservation -> approveReservation()

                // 거래 완료로 전환하는 서버 API가 없어(상태 enum에도 없음) 화면에서만 진행시킵니다.
                // 백엔드에 전환 API 추가를 요청해 둔 상태입니다.
                is PhotographerDetailReservationIntent.ConfirmReservation -> {
                    _state.update { it.copy(reservationStatus = ReservationStatus.COMPLETED) }
                }

                // 이 화면은 채팅방에서 진입하므로 뒤로 가면 채팅방으로 돌아간다.
                // 채팅방을 거치지 않은 경로(Dev 메뉴 등)에서는 그냥 이전 화면으로 간다.
                is PhotographerDetailReservationIntent.NavigateToChat -> {
                    emitSideEffect(PhotographerDetailReservationSideEffect.NavigateToPrev)
                }

                is PhotographerDetailReservationIntent.RejectReservation -> {
                    emitSideEffect(
                        PhotographerDetailReservationSideEffect.NavigateToRejectReason(
                            reservationId = _state.value.reservationId,
                        ),
                    )
                }

                is PhotographerDetailReservationIntent.NavigateToCancelReservation -> {
                    emitSideEffect(
                        PhotographerDetailReservationSideEffect.NavigateToCancelReservation(
                            reservationId = _state.value.reservationId,
                        ),
                    )
                }

                is PhotographerDetailReservationIntent.NavigateBack -> {
                    emitSideEffect(PhotographerDetailReservationSideEffect.NavigateToPrev)
                }

                is PhotographerDetailReservationIntent.OnToastDismiss -> {
                    _state.update { it.copy(showToast = false) }
                }
            }
        }

        /**
         * 유효한 id 가 없으면 조회하지 않습니다.
         *
         * 채팅방의 예약 카드가 아직 더미라 id 0 으로 진입하는 경로가 있는데,
         * 그대로 호출하면 404 를 받아 "불러오지 못했어요" 토스트만 뜹니다.
         * 서버에 물어볼 게 없는 상황이므로 기존처럼 기본값 화면을 보여줍니다.
         */
        private fun loadReservation() {
            if (reservationId <= 0L) return

            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                reservationService
                    .getReservation(reservationId)
                    .onSuccess { detail -> _state.update { it.applyDetail(detail) } }
                    .onFailure {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                toastMessageResId = R.string.reservation_error_load_failed,
                                showToast = true,
                            )
                        }
                    }
            }
        }

        private fun approveReservation() {
            if (_state.value.isLoading) return

            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                reservationService
                    .acceptReservation(reservationId)
                    .onSuccess { loadReservation() }
                    .onFailure {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                toastMessageResId = R.string.reservation_error_accept_failed,
                                showToast = true,
                            )
                        }
                    }
            }
        }

        private fun emitSideEffect(sideEffect: PhotographerDetailReservationSideEffect) {
            viewModelScope.launch {
                _sideEffect.emit(sideEffect)
            }
        }
    }

/**
 * 서버에서 받은 값만 덮어씁니다.
 *
 * 응답에 없는 확정 시각·고객 이름은 기존 값을 유지하고, `REJECTED`/`CANCELED` 처럼
 * 대응하는 화면 상태가 없는 경우에도 직전 상태를 그대로 둡니다.
 */
private fun PhotographerDetailReservationState.applyDetail(
    detail: ReservationDetail,
): PhotographerDetailReservationState =
    copy(
        isLoading = false,
        reservationStatus = detail.status?.toUiStatusOrNull() ?: reservationStatus,
        shootingDateTimeMillis = detail.shootingDateTimeMillis ?: shootingDateTimeMillis,
        packageName = detail.packageName,
        place = detail.place,
    )
