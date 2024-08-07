package com.hm.picplz.data.source

import com.hm.picplz.data.model.User
import com.hm.picplz.data.service.UserService

class UserDataSource(private val userService: UserService) {
    suspend fun fetchUsers(): List<User> {
        return userService.getUsers()
    }
}