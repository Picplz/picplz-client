package com.hm.picplz.data.model

sealed class NicknameFieldError {
    data class Required(val message: String = "닉네임을 작성해주세요") : NicknameFieldError()
    data class InvalidChar(val message: String = "한글, 영문, 숫자만 입력 가능합니다") : NicknameFieldError()
    data class DuplicateNickname(val message: String = "이미 사용 중인 닉네임입니다") : NicknameFieldError()
    data class InvalidSpecialCharacter(val message: String = "이모티콘이나 특수문자는 사용할 수 없습니다") : NicknameFieldError()
    data class LeadingOrTrailingWhitespace(val message: String = "닉네임의 처음과 끝에 공백을 사용할 수 없습니다") : NicknameFieldError()
    data class Custom(val message: String) : NicknameFieldError()
}