package com.hm.picplz.data.model

sealed class NicknameFieldError {
    abstract val message: String

    data class Required(override val message: String = "닉네임을 작성해주세요") : NicknameFieldError()
    data class InvalidChar(override val message: String = "한글, 영문, 숫자만 입력 가능합니다") : NicknameFieldError()
    data class Length(override val message: String = "2~15자로 입력해주세요") : NicknameFieldError()
    data class DuplicateNickname(override val message: String = "이미 사용 중인 닉네임입니다") : NicknameFieldError()
    data class InvalidSpecialCharacter(override val message: String = "이모티콘이나 특수문자는 사용할 수 없습니다") : NicknameFieldError()
    data class Whitespace(override val message: String = "닉네임의 처음과 끝에 공백을 사용할 수 없습니다") : NicknameFieldError()
    data class Custom(override val message: String) : NicknameFieldError()
}