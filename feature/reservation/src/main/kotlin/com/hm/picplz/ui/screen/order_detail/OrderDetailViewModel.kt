package com.hm.picplz.ui.screen.order_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hm.picplz.navigation.model.OrderDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val reservationId: Long = savedStateHandle.toRoute<OrderDetail>().reservationId

    private val _state = MutableStateFlow(OrderDetailState.idle())
    val state: StateFlow<OrderDetailState> = _state

    private val _sideEffect = MutableSharedFlow<OrderDetailSideEffect>()
    val sideEffect: SharedFlow<OrderDetailSideEffect> = _sideEffect

    init {
        loadOrderDetail(reservationId)
    }

    fun handleIntent(intent: OrderDetailIntent) {
        when (intent) {
            is OrderDetailIntent.OnBackClick -> {
                viewModelScope.launch {
                    _sideEffect.emit(OrderDetailSideEffect.NavigateBack)
                }
            }
            is OrderDetailIntent.OnNextClick -> {
                viewModelScope.launch {
                    _sideEffect.emit(OrderDetailSideEffect.NavigateToNextStep)
                }
            }
        }
    }

    fun loadOrderDetail(bookingId: Long) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                _state.value =
                    OrderDetailState(
                        orderNumber = bookingId.toString(),
                        orderTime = "2025-03-09 19:09:14",
                        customerName = "가나다",
                        phoneNumber = "01023293185",
                        photographerName = "주로그",
                        productName = "남친생기는 프사❤️",
                        productImageUrl = "https://picsum.photos/id/222/300/300",
                        price = 12000,
                        productDescription = "작가가 해놓은 한줄소개 기렁지면 이렇게 처리 작가가 해놓은 한줄소개 기렁지면 이렇게 처리",
                        shootingTime = "25.01.09 - 오후 02:00",
                        isLoading = false,
                    )
            } catch (e: Exception) {
                _state.value =
                    _state.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "주문 정보 조회 실패",
                    )
            }
        }
    }
}
