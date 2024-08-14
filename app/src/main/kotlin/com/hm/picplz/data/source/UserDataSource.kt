package com.hm.picplz.data.source

import com.hm.picplz.data.model.User
import com.hm.picplz.data.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun getUserData(): User {
        return withContext(Dispatchers.IO) {
            userService.getUser()
        }
    }
}