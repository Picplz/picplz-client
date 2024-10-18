package com.hm.picplz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.model.User
import com.hm.picplz.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import com.hm.picplz.viewmodel.MainActivityUiState.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = mainRepository.userData.map { user ->
        if(user.id == "0") { Unauthenticated } else { Success(user) }
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    init {
        viewModelScope.launch {
            fetchUserData()
        }
    }

    private suspend fun fetchUserData() {
        try {
            mainRepository.fetchUserData()
        } catch (e: Exception) {
            Log.e("MainActivityViewModel", "유저 데이터 fetch 에러", e)
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: User) : MainActivityUiState
    data object Unauthenticated : MainActivityUiState
}