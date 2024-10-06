package com.hm.picplz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.data.model.NicknameFieldError
import com.hm.picplz.data.model.SelectionState
import com.hm.picplz.ui.screen.sign_up.sign_up_common.DestinationByUserType.*
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonIntent.*
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpSideEffect
import com.hm.picplz.ui.screen.sign_up.sign_up_common.SignUpCommonState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.hm.picplz.data.model.User
import com.hm.picplz.data.model.UserType.*

class SignUpCommonViewModel : ViewModel() {

    private val _state = MutableStateFlow<SignUpCommonState>(SignUpCommonState.idle())
    val state: StateFlow<SignUpCommonState> get() = _state

    private val _sideEffect = MutableSharedFlow<SignUpSideEffect>()
    val sideEffect: SharedFlow<SignUpSideEffect> get() = _sideEffect

    /**
     * 임시 데이터
     * Todo: 카카오 로그인에서 반환받은 유저 정보 호출
     */
    private var userData: User = emptyUserData

    fun handleIntent(intent: SignUpCommonIntent) {
        when (intent) {
            is NavigateToPrev -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpSideEffect.NavigateToPrev)
                }
            }
            is ClickUserTypeButton -> {
                val newUserType = if (_state.value.selectedUserType == intent.userType) {
                    null
                } else {
                    intent.userType
                }
                val newPhotographerSelectionState = if (newUserType == Photographer) {
                    SelectionState.SELECTED
                } else if (newUserType == User) {
                    SelectionState.DESELECTED
                } else {
                    SelectionState.UNSELECTED
                }
                val newUserSelectionState = if (newUserType == User) {
                    SelectionState.SELECTED
                } else if (newUserType == Photographer) {
                    SelectionState.DESELECTED
                } else {
                    SelectionState.UNSELECTED
                }

                _state.value = _state.value.copy(
                    selectedUserType = newUserType,
                    photographerSelectionState = newPhotographerSelectionState,
                    userSelectionState = newUserSelectionState
                )            }
            is NavigateToSelected -> {
                viewModelScope.launch {
                    _state.value.selectedUserType?.let { selectedUserType ->
                        val destination = when (selectedUserType) {
                            User -> SignUpClient
                            Photographer -> SignUpPhotographer
                        }
                        _sideEffect.emit(SignUpSideEffect.SelectUserTypeScreenSideEffect.NavigateToSelected(destination, userData))
                    }
                }
            }
            is ResetSelectedUserType -> {
                _state.value = _state.value.copy(selectedUserType = null)
            }
            is SetNickname -> {
                val errors = validateNickname(intent.newNickname)
                val newNicknameState = _state.value.copy(
                    nickname = intent.newNickname,
                    nicknameFieldErrors = errors
                )
                _state.value = newNicknameState
            }
            is SetProfileImageUri -> {
                val newProfileImageUriState = _state.value.copy(profileImageUri = intent.newProfileImageUri)
                _state.value = newProfileImageUriState
            }
            is Navigate -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpSideEffect.Navigate(intent.destination))
                }
            }
            is ShowFileUploadDialog -> {
                viewModelScope.launch {
                    _sideEffect.emit(SignUpSideEffect.ShowFileUploadDialog)
                }
            }
        }
    }
    /** *
     * Todo: 닉네임 유효성 검사 로직
     *  중복 검사는 이후 api통신 필요
     * **/
    private fun validateNickname( newNickname: String ): List<NicknameFieldError> {
        val errors = mutableListOf<NicknameFieldError>()
        if (newNickname.isEmpty()) {
            errors.add(NicknameFieldError.Required())
        }
        if (newNickname.length < 2 || newNickname.length > 15) {
            errors.add(NicknameFieldError.Length())
        }
        if (!newNickname.matches(Regex("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s]+$"))) {
            errors.add(NicknameFieldError.InvalidChar())
        }
        if (newNickname.contains(Regex("[\\p{So}\\p{Cn}\\p{Sk}\\p{Sc}\\p{Sm}]"))) {
            errors.add(NicknameFieldError.InvalidSpecialCharacter())
        }
        if (newNickname.startsWith(" ") || newNickname.endsWith(" ")) {
            errors.add(NicknameFieldError.Whitespace())
        }
        return errors
    }
}

val emptyUserData = User(
    id = 0,
    name = "Unknown",
    email = "unknown@example.com"
)