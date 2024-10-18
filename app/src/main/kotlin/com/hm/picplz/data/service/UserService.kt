package com.hm.picplz.data.service

import com.hm.picplz.data.model.User
import com.hm.picplz.viewmodel.emptyUserData
import kotlinx.coroutines.delay

import javax.inject.Inject

class UserService @Inject constructor() {
    /**
     * 더미 데이터 출력
     * api 연결 이후 코드 수정
     */
    suspend fun getUser(): User {
        delay(3000)
        return emptyUserData
    }
}