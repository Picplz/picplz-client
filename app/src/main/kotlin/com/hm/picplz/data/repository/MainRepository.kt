package com.hm.picplz.data.repository

import android.util.Log
import com.hm.picplz.data.model.User
import com.hm.picplz.data.source.UserDataSource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val userDataSource: UserDataSource
) {

    private val _userData = MutableSharedFlow<User>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private val currentUserData get() = _userData.replayCache.firstOrNull() ?: emptyUserData

    val userData: Flow<User> = _userData.filterNotNull()


    suspend fun fetchUserData() {
        val fetchedUserData = userDataSource.getUserData()
        _userData.emit(fetchedUserData)
    }

    companion object {
        val emptyUserData = User(
            id = "0",
            nickname = "Unknown",
            email = "unknown@example.com",
            userType = null,
            profileImageUri = null,
            photographyExperience = null,
            photographyVibes = null,
        )
    }
}