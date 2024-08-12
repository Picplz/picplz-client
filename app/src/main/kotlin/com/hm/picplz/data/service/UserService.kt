package com.hm.picplz.data.service

import com.hm.picplz.data.model.User

interface UserService {
    suspend fun getUsers(): List<User>
}