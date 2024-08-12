package com.hm.picplz.ui.component.login_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.hm.picplz.data.model.User

@Composable
fun UserItem(user: User) {
    Column {
        Text(text = user.name)
        Text(text = user.email)
    }
}