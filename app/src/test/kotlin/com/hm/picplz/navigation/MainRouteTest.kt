package com.hm.picplz.navigation

import com.hm.picplz.common.model.User
import com.hm.picplz.common.model.UserType
import com.hm.picplz.navigation.model.Login
import com.hm.picplz.navigation.model.Main
import com.hm.picplz.navigation.model.PhotographerMainGraph
import com.hm.picplz.ui.main.MainActivityUiState
import com.hm.picplz.ui.screen.sign_up.sign_up_common.views.signupCompletionDestination
import org.junit.Assert.assertSame
import org.junit.Test

class MainRouteTest {
    @Test
    fun `authenticated start destination is customer main`() {
        val destination = startDestinationFor(MainActivityUiState.Success(User(id = "user-id")))

        assertSame(Main, destination)
    }

    @Test
    fun `authenticated start destination is photographer main for photographer`() {
        val destination =
            startDestinationFor(
                MainActivityUiState.Success(
                    User(
                        id = "photographer-id",
                        userType = UserType.Photographer,
                    ),
                ),
            )

        assertSame(PhotographerMainGraph, destination)
    }

    @Test
    fun `unauthenticated start destination remains login`() {
        val destination = startDestinationFor(MainActivityUiState.Unauthenticated)

        assertSame(Login, destination)
    }

    @Test
    fun `signup completion destination is customer main`() {
        assertSame(Main, signupCompletionDestination())
    }
}
